# 设备消息协议解析SDK

平台封装了网络通信,但是具体的数据由消息协议进行解析.`协议(ProtocolSupport)`主要由`认证器(Authenticator)`,
`消息编解码器(DeviceMessageCodec)`,`消息发送拦截器(DeviceMessageSenderInterceptor)`以及`配置元数据(ConfigMetadata)`组成.

## 认证器

认证器(Authenticator)是用于在收到设备请求(例如MQTT)时,对客户端进行认证时使用,不同的网络协议(Transport)使用不同的认证器.

接口定义:

```java
public interface Authenticator {
    Mono<AuthenticationResponse> authenticate(@Nonnull AuthenticationRequest request,
                                              @Nonnull DeviceOperator device);
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

## 平台统一消息定义

平台将设备抽象为由`属性(property)`,`功能(function)`,`事件(event)`组成.
平台接入设备之前,应该先将设备的`属性``功能``事件`设计好.

### 属性相关消息

1. `获取设备属性(ReadPropertyMessage)`对应设备回复的消息`ReadPropertyMessageReply`.
2. `修改设备属性(WritePropertyMessage)`对应设备回复的消息`WritePropertyMessageReply`.
3. `设备上报属性(ReportPropertyMessage)` 由设备上报.

::: tip 注意
设备回复的消息是通过messageId进行绑定,messageId应该注意要全局唯一,如果设备无法做到,可以在编解码时通过添加前缀等方式实现.
:::

消息定义:

```java
ReadPropertyMessage{
    String deviceId; 
    String messageId;
    List<String> properties;//可读取多个属性
}

ReadPropertyMessageReply{
    String deviceId;
    String messageId;
    long timestamp;
    boolean success;
    Map<String,Object> properties;//属性键值对
}
```

```java
WritePropertyMessage{
    String deviceId; 
    String messageId;
    Map<String,Object> properties;
}

WritePropertyMessageReply{
    String deviceId;
    String messageId;
    long timestamp;
    boolean success;
    Map<String,Object> properties; //回复被修改的属性最新值
}
```

```java
ReportPropertyMessage{
    String deviceId; 
    String messageId;
    long timestamp;
    Map<String,Object> properties;
}
```

### 功能相关消息

调用设备功能到消息(`FunctionInvokeMessage`)由平台发往设备,对应到返回消息`FunctionInvokeMessageReply`.

消息定义:

```java
FunctionInvokeMessage{
    String functionId;//功能标识,在元数据中定义.
    String deviceId;
    String messageId;
    List<FunctionParameter> inputs;//输入参数
}

FunctionParameter{
    String name;
    Object value;
}

FunctionInvokeMessageReply{
    String deviceId;
    String messageId;
    long timestamp;
    boolean success;
    Object output; //输出值,需要与元数据定义中的类型一致
}
```

### 事件消息

事件消息`EventMessage`由设备端发往平台.

消息定义:

```java
EventMessage{
    String event; //事件标识,在元数据中定义
    Object data;  //与元数据中定义的类型一致,如果是对象类型,请转为java.util.HashMap,禁止使用自定义类型.
    long timestamp;
}
```

### 其他消息

1. `DeviceOnlineMessage` 设备上线消息,通常用于网关代理的子设备的上线操作.
2. `DeviceOfflineMessage` 设备上线消息,通常用于网关代理的子设备的下线操作.
3. `ChildrenDeviceMessage` 子设备消息,通常用于网关代理的子设备的消息.

消息定义:

```java
DeviceOnlineMessage{
    String deviceId;
    long timestamp;
}

DeviceOfflineMessage{
    String deviceId;
    long timestamp;
}

```

```java
ChildDeviceMessage{
    String deviceId;
    String childDeviceId;
    Message childDeviceMessage; //子设备消息
}
```

父子设备消息处理[请看这里](../advancement-guide/children-device.md)

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

配置元数据用于告诉平台,在使用此协议等时候,需要添加一些自定义配置到设备(`DeviceOperator.setConfig`)中.

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

