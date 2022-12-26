# 关于消息总线

<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>消息总线：EventBus是一个基于发布者/订阅者模式的事件总线框架。发布者/订阅者模式，也就是观察者模式，其定义了对象之间的一种一对多的依赖关系，
当事件发布时，平台的其他模块，例如：产品/设备、规则引擎、设备告警等模块都可以同时订阅到该事件，能够有效地降低消息发布者和订阅者之间的耦合度。</p>

</div>

### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>使用消息总线发送消息，一处发布，多处订阅异步处理，没有先后顺序，异步解耦代码逻辑。</p>

</div>

### 发布/订阅事件实例

发布事件:

```java
      public Mono<Void> shutdown(NetworkType type,String NetworkId){
        return
        //将停止网络组件事件推送到消息总线   
        eventBus.publish("/_sys/network/"+type.getId()+"/shutdown",NetworkId)
        .then();
        }
```

订阅事件：

```java
      //使用Subscribe方法
public void doSubscribe(){
        eventBus
        //调用subscribe方法
        .subscribe(Subscription
        //构建订阅者消息
        .builder()
        //订阅者标识
        .subscriberId("network-config-manager")
        //订阅topic
        .topics("/_sys/network/*/shutdown")
        //订阅特性,有三类特性
        .justBroker()
        .build())
        //拿到消息总线中的数据进行后续处理
        .flatMap(payload->{
        ...
        })
        .subscribe();
        }

//使用Subscribe注解
@Subscribe(topics = "/_sys/media-gateway/start", features = Subscription.Feature.broker)
public Mono<Void> doStart(String id){
        return this
        .findById(id)
        .flatMap(this::doStart);
        }
```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

<p>可用通配符*替换topic中的参数，发布和订阅均支持通配符，例如：</p>

`/_sys/network/MQTT_SERVER/shutdown`，表示停止类型为MQTT服务的网络组件，
`/_sys/network/*/shutdown`，表示停止所有类型的网络组件，可订阅该topic

<p>通配符通配符*表示匹配单层路径，**表示匹配多层路径，例如：</p>

`/device/product001/*/online`，表示订阅产品product001下所有设备的上线消息
`/device/product001/**`，表示订阅产品product001下所有设备的所有消息


</div>

### 共享订阅实例

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题</span>
  </p>

 <p>如何实现共享订阅？</p>
 <p>平台根据传入的订阅特性字段判断是否为shared共享订阅，若是共享订阅则会先存到缓存中，后续再依次处理缓存中的共享订阅，在处理的过程中会
判断订阅消息是否是同一个订阅者的，若是同一个订阅者则只处理最早的那条订阅消息。</p>

</div>

编程式共享订阅消息：

```java
      public void doSubscribe(){
        eventBus
        //调用subscribe方法
        .subscribe(Subscription
        //构建订阅者消息
        .builder()
        //订阅者标识
        .subscriberId("network-config-manager")
        //订阅topic
        .topics("/_sys/network/*/shutdown")
        //订阅特性为shared
        .shared()
        .build())
        //拿到消息总线中的数据进行后续处理
        .flatMap(payload->{
        ...
        })
        .subscribe();
        }
```

注解式共享订阅消息：

```java

//订阅特性为shared
@Subscribe(topics = "/_sys/media-gateway/start", features = Subscription.Feature.shared)
public Mono<Void> doStart(String id){
        return this
        .findById(id)
        .flatMap(this::doStart);
        }
```

订阅特性字段Feature说明

| 标识         | 说明         |
|------------|------------|
| shared | 共享订阅 |
| local| 订阅本地消息     |
| broker | 订阅代理消息     |

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
  <p>在使用Subscribe注解订阅事件时，传入参数分别为订阅topic、订阅者标识和订阅特性，后两者为选填，若不填订阅者标识默认值为本方法名，订阅特性默认值为local，
当订阅特性为shared时就只能填写这一个特性，当订阅特性中没有shared时，local和broker可同时填写。</p>

</div>

### 多订阅特性实例

编程式订阅消息：

```java
eventBus
        .subscribe(Subscription.of("gateway"", "/_sys/media-gateway/start", Subscription.Feature.local, Subscription.Feature.broker))
```

注解式订阅消息：

 ```java
@Subscribe(topics = "/_sys/media-gateway/start", features = {Subscription.Feature.broker, Subscription.Feature.local})
 ```

### 关于Topic

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>所有设备消息的topic的前缀均为: <span class='explanation-title font-weight'>/device/{productId}/{deviceId}</span>。 </p>
   <p>如:设备device-1上线消息:<span class='explanation-title font-weight'>/device/product-1/device-1/online</span>。 </p>
   <p>可通过通配符订阅所有设备的指定消息,如:<span class='explanation-title font-weight'>/device/*/*/online</span>,或者订阅所有消息:<span class='explanation-title font-weight'>/device/**</span>。</p>
    <p>使用通配符订阅可能将收到大量的消息,请保证消息的处理速度,否则会影响系统消息吞吐量。 </p>

</div>

### 带权限控制的Topic



### 设备Topic列表

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

列表中的topic已省略前缀`/device/{productId}/{deviceId}`,使用时请加上.

</div>

| topic                                              | 类型                       | 说明                                      |
| -------------------------------------------------- | -------------------------- | ----------------------------------------- |
| /online                                            | DeviceOnlineMessage        | 设备上线                                  |
| /offline                                           | DeviceOfflineMessage       | 设备离线                                  |
| /message/event/{eventId}                           | DeviceEventMessage         | 设备事件                                  |
| /message/property/report                           | ReportPropertyMessage      | 设备上报属性                              |
| /message/property/read/reply                       | ReadPropertyMessageReply   | 读取属性回复                              |
| /message/property/write/reply                      | WritePropertyMessageReply  | 修改属性回复                              |
| /message/function/reply                            | FunctionInvokeMessageReply | 调用功能回复                              |
| /register                                          | DeviceRegisterMessage      | 设备注册,通常与子设备消息配合使用         |
| /unregister                                        | DeviceUnRegisterMessage    | 设备注销,同上                             |
| /message/children/{childrenDeviceId}/{topic}       | ChildDeviceMessage         | 子设备消息,{topic}为子设备消息对应的topic |
| /message/children/reply/{childrenDeviceId}/{topic} | ChildDeviceMessage         | 子设备回复消息,同上                       |

### 系统消息对应topic说明

| Topic                                            | 说明       |
|--------------------------------------------------|----------|
| /_sys/network/{typeId}/reload                    | 重启网络组件   |
| /_sys/network/{typeId}/shutdown                  | 停止网络组件   |
| /_sys/network/{typeId}/destroy                   | 删除网络组件   |
| /_sys/media-gateway/{gatewayId}/sync-channel     | 视频网关同步   |
| /_sys/media-gateway/start                        | 视频网关启动   |
| /_sys/media-gateway/stop                         | 视频网关停止   |
| /_sys/collector-conf                             | 采集器配置    |
| /_sys/collector-remove                           | 采集器删除    |
| /_sys/collector-point                            | 采集器点位    |
| /_sys/collector-channel                          | 采集器通道    |
| /_sys/collector-channel-remove                   | 删除采集器通道  |
| /sys-event/{operationType}/{operationId}/{level} | 系统事件     |
| /logging/system/{logName}/{logLevel}             | 系统日志     |
| /_sys/notifier/reload                            | 消息通知重启   |
| /_sys/registry-product/{productID}/register      | 产品注册     |
| /_sys/registry-product/{productID}/unregister    | 产品注销     |
| /_sys/registry-product/{productID}/metadata      | 产品物模型    |
| /_sys/user-dimension-changed/{userId}            | 用户维度变更   |
| /_sys/metadata-mapping-changed                   | 物模型变更    |
| /_sys/metadata-mapping-deleted                   | 物模型删除    |
| /debug/device/{deviceId}/trace                   | 设备诊断     |
| /_sys/notify-channel/register                    | 注册消息通道   |
| /_sys/notify-channel/unregister                  | 注销消息通道   |
| /_sys/alarm/config/deleted                       | 告警配置删除   |
| /_sys/alarm/config/created                       | 告警配置创建   |
| /_sys/alarm/config/saved                         | 告警配置保存   |
| /_sys/alarm/config/modified                      | 告警配置修改   |
| /_sys/device-alarm-config/save                   | 设备告警保存   |
| /_sys/device-alarm-config/bind                   | 设备告警绑定   |
| /_sys/device-alarm-config/unbind                 | 设备告警解绑   |
| /_sys/thing-connector/started                    | 启动连接器    |
| /_sys/thing-connector/stopped                    | 停止连接器    |
| /_sys/thing-connector/deleted                    | 删除连接器    |

##### typeId字段说明

| 网络组件类型标识          | 说明           |
|-------------------|--------------|
| TCP_CLIENT        | TCP客户端       |
| TCP_SERVER        | TCP服务        |
| MQTT_CLIENT       | MQTT客户端      |
| MQTT_SERVER       | MQTT服务       |
| HTTP_CLIENT       | HTTP客户端      |
| HTTP_SERVER       | HTTP服务       |
| WEB_SOCKET_CLIENT | WebSocket客户端 |
| WEB_SOCKET_SERVER | WebSocket服务  |
| COAP_CLIENT       | CoAP客户端      |
| COAP_SERVER       | CoAP服务       |
| UDP               | UDP          | 


### 核心接口说明

核心接口org.jetlinks.core.event.EventBus

| 方法名                                                                                                     | 返回值                  | 说明                         |
|---------------------------------------------------------------------------------------------------------|----------------------|----------------------------|
| subscribe(Subscription subscription)                                                                    | Flux\<TopicPayload\> | 从事件总线中订阅事件                 |
| subscribe(Subscription subscription, Function\<TopicPayload, Mono\<Void\>\> handler)                    | Disposable           | 从事件总线中订阅事件并指定handler来处理事件  |
| subscribe(Subscription subscription, Decoder\<T\> decoder)                                              | \<T\> Flux\<T\>      | 从事件总线中订阅事件并按照指定的解码器进行数据转换  |
| subscribe(Subscription subscription, Class\<T\> type)                                                   | \<T\> Flux\<T\>  | 订阅主题并将事件数据转换为指定的类型  |
| publish(String topic, Publisher\<T\> event)                                                             | \<T\> Mono\<Long\>   | 推送消息流到事件总线                 |
| publish(String topic, Publisher\<T\> event, T event)                                                    | \<T\> Mono\<Long\>   | 推送单个数据到事件总线中并指定编码器用于将事件数据进行序列化                |
| publish(String topic, Encoder\<T\> encoder, Publisher\<\? extends T\> eventStream)                      | \<T\> Mono\<Long\>   | 推送消息流到事件总线并指定编码器用于进行事件序列化  |
| publish(String topic, Encoder\<T\> encoder, Publisher\<\? extends T\> eventStream, Scheduler scheduler) | \<T\> Mono\<Long\>   | 推送消息流到事件总线并指定编码器和调度器用于进行事件序列化 |

### 设备告警实例

在配置了设备告警规则,设备发生告警时,会发送消息到消息网关.

```js
`/rule-engine/device/alarm/{productId}/{deviceId}/{ruleId}`

{
  "productId":"",
  "alarmId":"",
  "alarmName":"",
  "deviceId":"",
  "deviceName":"",
  "...":"其他从规则中获取到到信息"
}
```

### 系统日志实例

topic格式: `/logging/system/{logger名称,.替换为/}/{level}`.

```js
`/logging/system/org/jetlinks/pro/TestService/{level}`

{
"name":"org.jetlinks.pro.TestService", //logger名称
"threadName":"线程名称",
"level":"日志级别",
"className":"产生日志的类名",
"methodName":"产生日志的方法名",
"lineNumber"：32,//行号
"message":"日志内容",
"exceptionStack":"异常栈信息",
"createTime":"日志时间",
"context":{} //上下文信息
}

```