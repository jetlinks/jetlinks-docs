# 协议开发说明

平台封装了网络通信,但是具体的数据由消息协议进行解析.`协议(ProtocolSupport)`主要由`认证器(Authenticator)`,
`消息编解码器(DeviceMessageCodec)`,`消息发送拦截器(DeviceMessageSenderInterceptor)`以及`配置元数据(ConfigMetadata)`组成.

## 认证器

认证器(Authenticator)是用于在收到设备请求(例如MQTT)时,对客户端进行认证时使用,不同的网络协议(Transport)使用不同的认证器.

接口定义:

```java
public interface Authenticator {
   /**
     * 对指定对设备进行认证
     *
     * @param request 认证请求
     * @param device  设备
     * @return 认证结果
     */
    Mono<AuthenticationResponse> authenticate(@Nonnull AuthenticationRequest request,
                                              @Nonnull DeviceOperator device);

    /**
     *  在MQTT服务网关中指定了认证协议时,将调用此方法进行认证。
     *  注意: 认证通过后,需要设置设备ID.{@link AuthenticationResponse#success(String)}
     * @param request  认证请求
     * @param registry 设备注册中心
     * @return 认证结果
     */
    default Mono<AuthenticationResponse> authenticate(@Nonnull AuthenticationRequest request,
                                                      @Nonnull DeviceRegistry registry) {
        return Mono.just(AuthenticationResponse.success());
    }
}
```

参数`AuthenticationRequest`为认证请求参数,不同的网络类型请求类型也不同,请根据实际情况转换为对应的类型,例如:
`MqttAuthenticationRequest mqttRequest = (MqttAuthenticationRequest)request;`

参数`DeviceOperator`为对应的设备操作接口,可通过此接口获取设备的配置,例如:`device.getConfig("mqttUsername")`.

返回值`Mono<AuthenticationResponse>`为认证结果.

例:

```java
   Authenticator mqttAuthenticator =  (request, device) -> {
            MqttAuthenticationRequest mqttRequest = ((MqttAuthenticationRequest) request);
            return device.getConfigs("username", "password") //获取设备的配置信息,由配置元数据定义,在设备型号中进行配置.
                    .flatMap(values -> {
                        String username = values.getValue("username").map(Value::asString).orElse(null);
                        String password = values.getValue("password").map(Value::asString).orElse(null);
                        if (mqttRequest.getUsername().equals(username) && mqttRequest.getPassword().equals(password)) {
                            return Mono.just(AuthenticationResponse.success());
                        } else {
                            return Mono.just(AuthenticationResponse.error(400, "密码错误"));
                        }
                    });
        }
```

## 消息编解码器

用于将平台`统一的消息(Message)`与`设备端能处理的消息(EncodedMessage)`进行相互转换.

接口(`DeviceMessageCodec`)定义:

```java
  //此编解码器支持的网络协议,如: DefaultTransport.MQTT
  Transport getSupportTransport();
  //将平台发往设备的消息编码为设备端对消息
  Publisher<? extends EncodedMessage> encode(MessageEncodeContext context);
  //将设备发往平台的消息解码为平台统一的消息
  Publisher<? extends Message> decode(MessageDecodeContext context);
```

编码: 可以从上下文`MessageEncodeContext`中获取当前设备操作接口`DeviceOperator`以及平台统一的设备消息`Message`.根据设备侧定义的协议转换为对应的`EncodedMessage`.

::: tip 注意
不同的网络协议需要转换为不同的`EncodedMessage`类型.比如,MQTT需要转换为`MqttMessage`.

大部分情况下:`MessageDecodeContext`可转为`FromDeviceMessageContext`,可获取到当前设备的连接会话`DeviceSession`,通过会话可以直接发送消息到设备.
:::

解码: 可以从上下文`MessageDecodeContext`中获取设备操作接口`DeviceOperator`以及设备消息`EncodedMessage`,然后将消息转换为平台统一的消息.

## 消息发送拦截器

使用拦截器可以拦截消息发送和返回的动作,通过修改参数等操作实现自定义逻辑,如: 当设备离线时,将消息缓存到设备配置中,等设备上线时再重发.

```java
DeviceMessageSenderInterceptor{
     //发送前
      Mono<DeviceMessage> preSend(DeviceOperator device, DeviceMessage message);

     //发送后
      <R extends DeviceMessage> Flux<R> afterSent(DeviceOperator device, DeviceMessage message, Flux<R> reply);
}
```

在发送前,可以将参数`DeviceMessage`转为其他消息.

发送后,会将返回结果流`Flux<R>`传入,通过对该数据流对操作以实现自定义行为,如:忽略错误等.

## 配置元数据

配置元数据用于告诉平台,在使用此协议的时候,需要添加一些自定义配置到设备配置(`DeviceOperator.setConfig`)中.
在其他地方可以通过`DeviceOperator.getConfig`获取这些配置.

例如:

```java
CompositeProtocolSupport support = new CompositeProtocolSupport();
support.setId("demo-v1");
support.setName("演示协议v1");
support.setDescription("演示协议");
support.setMetadataCodec(new JetLinksDeviceMetadataCodec()); //固定为JetLinksDeviceMetadataCodec,请勿修改.

DefaultConfigMetadata mqttConfig = new DefaultConfigMetadata(
            "MQTT认证配置"
            , "")
            .add("username", "username", "MQTT用户名", new StringType())
            .add("password", "password", "MQTT密码", new PasswordType());
//设置MQTT所需要到配置
 support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);

```

## 完整例子

[演示协议](https://github.com/jetlinks/demo-protocol)
