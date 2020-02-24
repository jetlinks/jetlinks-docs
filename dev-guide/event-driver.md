# 事件驱动

在jetlinks中大量使用到事件驱动,在之前,我们是使用`spring event`作为事件总线进行进程内事件通知的.
由于`spring event`不支持响应式,所以使用`消息网关(MessageGateway)`来替代`spring event`.
`消息网关`有2个作用,1. 事件驱动 2. 设备消息统一管理. 

## 概念

在消息网关中分为: `消息网关(MessageGateway)`,`消息连接器(MessageConnector)`,`消息连接(MessageConnection)`,
`消息订阅器(MessageSubscriber)`,`消息发布器(MessagePublihser)`.
网关中对消息使用`topic`进行区分,而不是像`spring event`那样使用java类型来区分.

## Topic

采用树结构来定义topic如:`/device/id/message/type` .
topic支持路径通配符,如:`/device/**` 或者`/device/*/message/*`.

::: tip
`**`表示匹配多层路径,`*`表示匹配单层路径. `不支持`前后匹配,如: `/device/id-*/message`
:::


## 消息网关

`消息网关`从`连接器`中订阅`消息连接`,当有`连接创建`时,会根据连接类型进行不同的操作.
当`消息连接`是一个订阅器时(`isSubscriber`)时,会从`MessageSubscriber`中接收`消息订阅请求(onSubscribe)`,
并管理每一个连接的订阅信息.当一个`消息连接`是一个发布器时(`isPublisher`),会从`MessagePublihser`中`订阅消息(onMessage)`,
当发布器发送了`消息(TopicMessage)`时,网关会根据消息的`topic`获取订阅了此topic的`消息连接`,并将消息推送给对应的`订阅器`.

## 使用

订阅消息:

```java

@Subscribe("/device/**")
public Mono<Void> handleDeviceMessage(DeviceMessage message){
    return publishDeviecMessageToKafka(message);
}

```

发布消息:

```java

@Autowired
private MessageGateway gateway;

public Mono<Void> saveUser(UserEntity entity){
    return service.saveUser(entity)
            .then(gateway.publish("/user/"+entity.getId()+"/saved",entity))
            .then();
}

```

## 自定义连接器

消息网关还可以用于消息转发,如实现设备消息统一网关.如: 通过`CoAP`发送消息,使用`MQTT`订阅消息.

1. 实现`MessageConnector`接口.
2. 将连接器中的连接实现`MessageConnection`接口.
3. 根据情况,如果连接需要订阅消息,则还要实现`MessageSubscriber`,如果需要发布消息则实现`MessagePublisher`.

例子:

```java

public class MqttMessageConnector implements MessageConnector {

    private MqttServer mqttServer;

    private ClientAuthenticator authenticator;

    private int maxClientSize;

    @Nonnull
    @Override
    public String getId() {
        return mqttServer.getId();
    }

    @Nonnull
    @Override
    public Flux<MessageConnection> onConnection() {
        //从MQTT服务中订阅mqtt连接
        return mqttServer
            .handleConnection()
            .filter(conn -> {
                if (conn.getAuth().isPresent()) {
                    return true;
                }
                conn.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
                return false;
            })
            .flatMap(conn -> {
                MqttAuth auth = conn.getAuth().orElse(null);
                if (auth == null) {
                    return Mono.empty();
                }
                  //认证
                return authenticator
                    .authorize(new MqttAuthenticationRequest(conn.getClientId(), auth.getUsername(), auth.getUsername(), DefaultTransport.MQTT))
                    .map(resp -> new MqttMessageConnection(conn.accept(), resp));
            });
    }

    @AllArgsConstructor
    class MqttMessageConnection implements
        MessageConnection,
        MessagePublisher,
        MessageSubscriber {

        private MqttConnection mqttConnection;

        private ClientAuthentication authentication;

        @Override
        public String getId() {
            return mqttConnection.getClientId();
        }

        @Override
        public void onDisconnect(Runnable disconnectListener) {
            mqttConnection.onClose(conn -> disconnectListener.run());
        }

        @Override
        public void disconnect() {
            mqttConnection.close().subscribe();
        }

        @Override
        public boolean isAlive() {
            return mqttConnection.isAlive();
        }

        @Nonnull
        @Override
        public Flux<TopicMessage> onMessage() {
            //从MQTT连接中订阅消息
            return mqttConnection
                .handleMessage()
                .flatMap(publishing -> {
                    MqttMessage mqttMessage = publishing.getMessage();
                    String topic = mqttMessage.getTopic();
                    return authentication
                        .getAuthority(topic)
                        .filter(auth -> auth.has(TopicAuthority.PUB))
                        .map(auth -> TopicMessage.of(mqttMessage.getTopic(), mqttMessage))
                        .switchIfEmpty(Mono.fromRunnable(() -> log.warn("客户端[{}]推送无权限topic[{}]消息", getId(), topic)))
                        .doOnEach(ReactiveLogger.onError(err -> {
                            log.error("处理MQTT消息失败", err);
                        }))
                        ;
                }).onErrorContinue((err, obj) -> {

                });
        }

        @Nonnull
        @Override
        public Mono<Void> publish(@Nonnull TopicMessage message) {
            //将消息推送给MQTT
            return mqttConnection.publish(SimpleMqttMessage.builder()
                .payload(message.getMessage().getPayload())
                .topic(message.getTopic())
                .qosLevel(0)
                .build());
        }

        @Nonnull
        @Override
        public Flux<Subscription> onSubscribe() {
            //MQTT客户端订阅topic
            return mqttConnection
                .handleSubscribe(true)
                .flatMapIterable(sub -> sub.getMessage().topicSubscriptions())
                .map(MqttTopicSubscription::topicName)
                .filterWhen(topic -> authentication.getAuthority(topic).map(auth -> auth.has(TopicAuthority.SUB)))
                .map(Subscription::new);
        }

        @Nonnull
        @Override
        public Flux<Subscription> onUnSubscribe() {
            //MQTT客户端取消订阅
            return mqttConnection
                .handleUnSubscribe(true)
                .flatMapIterable(msg -> msg.getMessage().topics())
                .map(Subscription::new);
        }

        @Override
        public boolean isShareCluster() {
            //Pro将支持共享集群的消息,如: 节点1的网关收到了消息,MQTT从服务节点2订阅了请求.
            return false;
        }
    }

}

```