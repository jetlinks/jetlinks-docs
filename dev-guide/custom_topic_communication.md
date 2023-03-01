# 自定义Topic通信
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  设备发送的连接，经过设备使用的协议包中的topic进行匹配，从而实现设备和平台的通信
</div>


平台支持基于MQTT、UDP、TCP透传等协议通过自定义协议包的方式，解析不同厂家、不同设备上报的数据。协议包开发规范详见：[协议包开发快速开始](/protocol/first.html)。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  本文档以JetLinks官方协议V3分支的mqtt通信协议为例
</div>

获取JetLinks官方协议包：[官方协议包](https://github.com/jetlinks/jetlinks-official-protocol)

1.在官方协议包的`org.jetlinks.protocol.official.TopicMessageCodec`中添加自定义的主题

参数1：主题topic  参数2：当前主题使用的类  参数3：对当前主题的描述,表示时一个上行topic，用于上报物模型属性数据

JetLinks平台上行topic：设备端向平台发送  

JetLinks平台下行topic：平台端向设备发送

更多JetLinks官方协议topic详情参见：JetLinks官方协议topic列表：[官方协议详解](/dev-guide/jetlinks-protocol-support.html)
```java
 //自定义属性上报的topic
    //上报属性数据
    customReportProperty("/*/customproperties/report",
                   ReportPropertyMessage.class,
                   route -> route
                       .upstream(true)
                       .downstream(false)
                       .group("属性上报")
                       .description("上报物模型属性数据")
                       .example("{\"properties\":{\"属性ID\":\"属性值\"}}")),

```

2.在官方协议包`org.jetlinks.protocol.official.TopicMessageCodec`中定义的topic，在进行通信时会根据`org.jetlinks.protocol.official.
JetLinksMqttDeviceMessageCodec`类中的方法进行匹配

`org.jetlinks.protocol.official.JetLinksMqttDeviceMessageCodec`类的核心方法

| 核心方法  | 参数                            | 返回值               | 描述                  |
|-------|-------------------------------|-------------------|---------------------|
|encode(`@Nonnull MessageEncodeContext context`)| context- 消息上下文 | `Mono<MqttMessage>` | 对平台到设备消息进行编码，返回编码结果 |
|decode(`@Nonnull MessageDecodeContext context`)| context – 消息上下文  |   `Flux<DeviceMessage>` | 对设备到平台的消息进行解码，返回解码结果 |


3.官方协议包通信核心代码
`org.jetlinks.protocol.official.TopicMessageCodec`类核心代码

上行topic:

根据topic匹配枚举值
```java
  public static Flux<DeviceMessage> decode(ObjectMapper mapper, String[] topics, byte[] payload) {
        return Mono
                .justOrEmpty(fromTopic(topics))
                .flatMapMany(topicMessageCodec -> topicMessageCodec.doDecode(mapper, topics, payload));
    }
```
获取具体的`deviceMessage`实现类
```java
 static Optional<TopicMessageCodec> fromTopic(String[] topic) {
        for (TopicMessageCodec value : values()) {
            if (TopicUtils.match(value.pattern, topic)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
```
将上报数据封装`deviceMessage`实现类实例对象中并返回该实例对象
```java
    Publisher<DeviceMessage> doDecode(ObjectMapper mapper, String[] topic, byte[] payload) {
        return Mono
                .fromCallable(() -> {
                    DeviceMessage message = mapper.readValue(payload, type);
                    FastBeanCopier.copy(Collections.singletonMap("deviceId", topic[1]), message);

                    return message;
                });
    }
```

下行topic:
根据下发的topic，封装TopicPayload实例对象信息
```java
 TopicPayload doEncode(ObjectMapper mapper, String[] topics, DeviceMessage message) {
        refactorTopic(topics, message);
        return TopicPayload.of(String.join("/", topics), mapper.writeValueAsBytes(message));
    }
```
平台`org.jetlinks.protocol.official.TopicPayload`类
```java
public class TopicPayload {

    private String topic;

    private byte[] payload;
}
```
`org.jetlinks.protocol.official.JetLinksMqttDeviceMessageCodec`类核心代码

在`org.jetlinks.protocol.official.JetLinksMqttDeviceMessageCodec`的encode方法中封装clientId、topic、payload下发给设备

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  下行topic会封装到SimpleMqttMessage类中下发给设备，用户要根据设备的实际情况封装
</div>

```java
 if (message instanceof DeviceMessage) {//是否是设备信息
                DeviceMessage deviceMessage = ((DeviceMessage) message);

                TopicPayload convertResult = TopicMessageCodec.encode(mapper, deviceMessage);
                if (convertResult == null) {
                    return Mono.empty();
                }
                return Mono
                      //获取设备信息中的产品ID
                        .justOrEmpty(deviceMessage.getHeader("productId").map(String::valueOf))
                          //如果请求头中产品ID为空，就从消息上下文中获取设备信息，并从设备信息中获取产品ID
                        .switchIfEmpty(context.getDevice(deviceMessage.getDeviceId())
                                              .flatMap(device ->device.getSelfConfig(DeviceConfigKey.productId))
                        )
                        .defaultIfEmpty("null")
                        //封装下发给设备的信息
                        .map(productId -> SimpleMqttMessage
                                .builder()
                                .clientId(deviceMessage.getDeviceId())
                                 //封装下行topic
`                                .topic("/".concat(productId).concat(convertResult.getTopic()))
`                                .payloadType(MessagePayloadType.JSON)
                                .payload(Unpooled.wrappedBuffer(convertResult.getPayload()))
                                .build());
            }
```


