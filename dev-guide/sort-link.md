# 短连接、低功耗类设备接入平台

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <p>该功能及API在  <span class='explanation-title font-weight'>jetlinks 1.4.0</span> 后提供。</p>
  <p>默认情况下,使用tcp和mqtt方式接入时,当连接断开时,则认为设备已离线。</p>
  <p>但是在某些场景(如:低功率设备)下,无法使用长连接进行通信,可以通过指定特定配置使平台保持设备在线状态。</p>
  <p>短连接接入场景：HTTP，MQTT/TCP发完即断开连接，CoAP协议。</p>
</div>

#### 保持在线

在自定义协议包解码出消息时，可通过在消息中添加头`keepOnline`来进行设置。如:

```javascript
//设置让会话强制在线
message.addHeader(Headers.keepOnline, true);
//设置超时时间（可选,默认10分钟），如果超过这个时间没有收到任何消息则认为离线。
message.addHeader(Headers.keepOnlineTimeoutSeconds, 600);

```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <li>MQTT接入时添加到任意消息即可。TCP接入时添加到<b class='explanation-title font-weight'>DeviceOnlineMessage</b>即可。</li>
  <li>如果服务重启，将不会保持在线状态！</li>

</div>

#### 核心类说明

1. `org.jetlinks.core.message.Headers`: 平台设备消息头统一约定接口，提供消息头参数设置。

| 属性名                        | 返回值                   | 默认值                                                | 说明                                                                                            |
|----------------------------|-----------------------|----------------------------------------------------|-----------------------------------------------------------------------------------------------|
| force                      | ` HeaderKey<Boolean>` | true                                               | 强制执行                                                                                          |
| keepOnline                 | ` HeaderKey<Boolean>` | true                                               | 保持在线,与`DeviceOnlineMessage`配合使用.                                                              |
| keepOnlineIgnoreConnection | ` HeaderKey<Boolean>` | false                                              | 在保持在线时,忽略连接状态信息,设备是否在线以: `keepOnlineTimeoutSeconds`指定为准                                       |
| keepOnlineTimeoutSeconds   | ` HeaderKey<Integer>` | 600                                                | 保持在线超时时间,超过指定时间未收到消息则认为离线，单位：秒                                                                |
| async                      | ` HeaderKey<Boolean>` | false                                              | 异步消息,当发往设备的消息标记了为异步时,设备网关服务发送消息到设备后将立即回复`org.jetlinks.core.enums.ErrorCode.REQUEST_HANDLING`到发送端 |
| timeout                    | ` HeaderKey<Long>`    | 1000                                               | 指定发送消息的超时时间,默认10秒                                                                             |
| mergeLatest                | ` HeaderKey<Boolean>` | false                                              | 是否合并历史属性数据,设置此消息头后,将会把历史最新的消息合并到消息体里                                                          |
| dispatchToParent           | ` HeaderKey<Boolean>` | false                                              | 是否为转发到父设备的消息                                                                                  |
| fragmentBodyMessageId      | ` HeaderKey<String>`  | null                                               | 分片消息ID,设备将结果分片返回,通常用于处理大消息(为平台下发消息时的消息ID)                                                     |
| fragmentNumber             | ` HeaderKey<Integer>` | 0                                                  | 分片数量                                                                                          |
| fragmentLast               | ` HeaderKey<Boolean>` | false                                              | 是否最后一个分配，当分片数量不确定时，使用该参数表示分片结束了                                                               |
| fragmentPart               | ` HeaderKey<Integer>` | 0                                                  | 当前分片数                                                                                         |
| partialProperties          | ` HeaderKey<Boolean>` | false                                              | 是否属性为部分属性,如果为true,在列式存储策略下,将会把之前上报的属性合并到一起进行存储                                                |
| enableTrace                | ` HeaderKey<Boolean>` | Boolean.getBoolean("device.message.trace.enabled") | 是否开启追踪,开启后header中将添加各个操作的时间戳，参数可以在`application.yml`里面指定                                       |
| ignoreStorage              | ` HeaderKey<Boolean>` | false                                              | 标记数据不存储                                                                                       |
| ignoreLog                  | ` HeaderKey<Boolean>` | false                                              | 忽略记录日志                                                                                        |
| ignore                     | ` HeaderKey<Boolean>` | false                                              | 忽略某些操作,具体由不同的消息决定，设置该参数后，消息统一处理中心判断该参数为false时丢弃该条数据                                           |
| ignoreSession              | ` HeaderKey<Boolean>` | false                                              | 忽略会话创建,如果设备未在线,默认创建会话,设置此header为true后则不会自动创建会话                                                |
| productId                  | ` HeaderKey<Boolean>` | null                                               | 产品ID                                                                                          |
| propertyContainsGeo        | ` HeaderKey<Boolean>` | true                                               | 上报属性中是否包含`geoPoint`信息,如果设置为false,上报属性时则不处理地理位置相关逻辑,可能提高一些性能                                             |
| geoProperty                | ` HeaderKey<Boolean>` | null                                               | 明确定义上报属性中包含的geo属性字段,在设备物模型属性数量较大时有助于提升地理位置信息处理性能，填写物模型定义为`geoPoint`类型的标识                        |
| clearAllSession            | ` HeaderKey<Boolean>` | false                                              | 在设备离线时,标记是否清理所有会话.<br/>通常用于短连接方式接入平台的场景, 在集群的多台节点中存在同一个设备的会话时,默认只有集群全部会话失效时,设备才算离线. <br/>可通过在发送离线消息中指定`header:clearAllSession`来标识是否让集群全部会话都失效                                                                                          |

#### 常见问题

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

  <li>短连接下发指令平台会抛出设备离线的异常信息。</li>

[//]: # "  <li>产品禁用后，设备无法再接入。但不影响已经接入的设备。</li>"

</div>

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

<p>在进行消息下发时，因为会话是强制保持在线的，所以消息会直接通过session下发，但是此时设备可能已经断开了连接,
将会抛出异常<b class='explanation-title font-weight'>DeviceOperationException(ErrorCode.CLIENT_OFFLINE)</b>。</p>
<p>此时可以在发送消息后拦截异常将消息缓存起来，等待下次设备连接上来后再下发指令。</p>

</div>

1. 在自定义协议包中使用消息拦截器拦截异常

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {

    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();

        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");

        //实现DeviceMessageSenderInterceptor里面的afterSent方法
        support.addMessageSenderInterceptor(new DeviceMessageSenderInterceptor() {
            @Override
            public <R extends DeviceMessage> Flux<R> afterSent(DeviceOperator device, DeviceMessage message, Flux<R> reply) {
                //拦截抛出DeviceOperationException类异常
                return reply.onErrorResume(DeviceOperationException.class, err -> {
                    //处理异常状态码是离线的情况，其他异常情况仍然抛出异常信息
                    if (err.getCode() == ErrorCode.CLIENT_OFFLINE) {
                        return device
                                .setConfig("will-msg", message) //设置到设备缓存中心
                                .thenReturn(((RepayableDeviceMessage<?>) message)
                                        .newReply()
                                        .code(ErrorCode.REQUEST_HANDLING.name())
                                        .message("设备处理中...")
                                        .success()
                                )
                                .map(r -> (R) r);
                    }
                    return Mono.error(err);
                });
            }
        });
    }
}
```

2. 获取缓存的消息

平台收到设备上报数据后进行解码时,可以先获取是否有缓存到消息,然后发送到设备.

伪代码如下:

```java
public class JetLinksMqttDeviceMessageCodec implements DeviceMessageCodec {

    @Override
    public Mono<? extends Message> decode(MessageDecodeContext context) {
        //收到设备报文时，说明设备和平台已建立连接会话（session）
        //可以通过context获取会话session，将缓存的消息处理后send给设备
        return context.getDevice()
                //从缓存里获取配置信息，获取到后并删除该缓存
                .getAndRemoveConfig("will-msg")
                .map(val -> val.as(DeviceMessage.class))
                .flatMap((msg) -> {
                    //取到消息后在此处将消息send返回给设备
                    return ((FromDeviceMessageContext) context)
                            .getSession(doEncode(msg))
                            .send(); //编码并发送给设备
                })
                //同时解码上报的报文信息返回平台
                .thenReturn(doDecode(context));

    }

    public DeviceMessage doDecode(MessageDecodeContext context) {
        //处理上行报文，即设备->平台,此处伪代码，需要自行解析成平台统一的消息类型
        return Mono.empty();
    }

    public EncodedMessage doEncode(DeviceMessage message) {
        //处理下行报文，平台->设备,需要自行按照设备协议文档约定的报文格式封装指令
        return EncodedMessage.simple(
                wrapByteByf(BinaryMessageType.write(message, Unpooled.buffer()))
        );
    }
}
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

以上代码仅作为演示功能逻辑,请根据实际情况进行相应的处理.

</div>