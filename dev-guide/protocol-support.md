# 协议开发说明

平台封装了网络通信,但是具体的数据由消息协议进行解析.`协议(ProtocolSupport)`主要由`认证器(Authenticator)`,
`消息编解码器(DeviceMessageCodec)`,`消息发送拦截器(DeviceMessageSenderInterceptor)`以及`配置元数据(ConfigMetadata)`组成.

## 设备接入协议开发说明

协议作为设备接入的核心,用于对设备进行消息编解码等操作.目前仅提供`java`版的协议开发sdk.

### 名词解释

+ *协议提供商* (`ProtocolSupportProvider`): 用于创建协议包实例,在发布协议时,将使用此接口的实现类来创建协议包实例.
+ *协议支持* (`ProtocolSupport`) : 用于解析`设备`和`平台`通信报文的插件,同时还对接入协议进行一些描述,如: 接入说明,需要的配置信息,默认物模型等.
+ *编解码* (`DeviceMessageCodec`): 对设备上报的数据进行解码,翻译为平台定义的统一的设备消息(`DeviceMessage`).以及将平台下发的消息(指令)`DeviceMessage`,编码为设备支持的报文.
+ *设备操作器*(`DeviceOperator`): 对一个设备实例的操作接口,可通过此接口获取、设置配置信息,获取物模型等操作.
+ *设备会话* (`DeviceSession`): 一个设备的连接会话信息,如: TCP,MQTT连接.
+ *设备消息* (`DeviceMessage`): 平台统一定义的设备消息,如:属性上报(`ReportPropertyMessage`),功能调用(`FunctionInvokeMessage`)等.
+ *设备原始消息* (`EncodedMessage`): 设备端原始的消息,如: MQTT(`MqttMessage`),HTTP(`HttpExchangeMessage`)等.


### 调用流程

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

<b style='color:#3399cc'>蓝色背景</b>表示协议包在整个流程中的角色

</div>

设备上报数据到平台流程

![设备上报数据流程](../protocol/img/decode-flow.svg)


平台下发指令到设备流程

![设备下发数据流程](../protocol/img/encode-flow.svg)

## 协议包开发快速开始

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

请先安装和配置`java8`和`maven3`,能正常执行`mvn`命令即可,本文不做单独介绍.

</div>

### 创建Maven项目

基于模版项目创建

`linux` 或者 `macOS` 下执行
```shell
mvn dependency:get \
-DremoteRepositories=https://nexus.hsweb.me/content/groups/public \
-DgroupId=org.jetlinks.protocol \
-DartifactId=protocol-archetype \
-Dversion=1.0.0-SNAPSHOT \
&& \
mvn archetype:generate \
-DarchetypeGroupId=org.jetlinks.protocol \
-DarchetypeArtifactId=protocol-archetype \
-DarchetypeVersion=1.0.0-SNAPSHOT \
-DoutputDirectory=./ \
-DgroupId=com.domain \
-DartifactId=custom-protocol \
-Dversion=1.0 \
-DarchetypeCatalog=local \
-DinteractiveMode=false
```

`windows`下使用`PowerShell`执行:

```shell
mvn dependency:get `
-DremoteRepositories="https://nexus.hsweb.me/content/groups/public" `
-DgroupId="org.jetlinks.protocol" `
-DartifactId="protocol-archetype" `
-Dversion="1.0.0-SNAPSHOT" 
;
mvn archetype:generate `
-DarchetypeGroupId="org.jetlinks.protocol" `
-DarchetypeArtifactId="protocol-archetype" `
-DarchetypeVersion="1.0.0-SNAPSHOT" `
-DoutputDirectory="./" `
-DgroupId="com.domain" `
-DartifactId="custom-protocol" `
-Dversion="1.0" `
-DarchetypeCatalog="local" `
-DinteractiveMode="false"
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

可根据需要修改第二个命令中的 `-DgroupId=com.domain`
以及`-DartifactId=custom-protocol`

</div>

命令执行成功后,将会在当前目录中创建名为`custom-protocol`的项目

![创建项目](../protocol/img/create-project.gif)


### 使用Idea打开

使用`File`-`open` 打开刚才创建的项目目录

![打开](../protocol/img/open-idea.png)


### 开始开发

[MQTT协议解析](../protocol/mqtt.md)

<!-- [TCP协议解析](./tcp.md) -->

<!-- [HTTP协议解析](./http.md) -->

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
        return Mono.just(AuthenticationResponse.error(500,"不支持的认证方式"));
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
设备网关从`网络组件`中接收到报文后,会调用对应协议包的消息编解码器进行处理.

接口`DeviceMessageCodec`定义：

```java

/**
 * 设备消息转换器,用于对不同协议的消息进行转换
 *
 * @author zhouhao
 * @see org.jetlinks.core.message.interceptor.DeviceMessageCodecInterceptor
 * @see org.jetlinks.core.message.codec.context.CodecContext
 * @since 1.0.0
 */
public interface DeviceMessageCodec extends DeviceMessageEncoder, DeviceMessageDecoder {

    /**
     * @return 返回支持的传输协议
     * @see DefaultTransport
     */
    Transport getSupportTransport();

    /**
     * 获取协议描述
     * @return 协议描述
     */
    default Mono<? extends MessageCodecDescription> getDescription() {
        return Mono.empty();
    }
}
```
  
接口`DeviceMessageEncoder`定义：
```java

/**
 * 设备消息编码器,用于将消息对象编码为对应消息协议的消息
 *
 * @see EncodedMessage
 * @see org.jetlinks.core.message.Message
 */
public interface DeviceMessageEncoder {

    /**
     * 编码,将消息进行编码,用于发送到设备端.
     *
     * 平台在发送指令给设备时,会调用协议包中设置的此方法,将平台消息{@link org.jetlinks.core.message.DeviceMessage}转为设备能理解的消息{@link EncodedMessage}.
     *
     * 例如:
     * <pre>
     *
     * //返回单个消息给设备,多个使用Flux&lt;EncodedMessage&gt;作为返回值
     * public Mono&lt;EncodedMessage&gt; encode(MessageEncodeContext context){
     *
     *     return Mono.just(doEncode(context.getMessage()));
     *
     * }
     *</pre>
     *
     * <pre>
     * //忽略发送给设备,直接返回结果给指令发送者
     * public Mono&lt;EncodedMessage&gt; encode(MessageEncodeContext context){
     *    DeviceMessage message = (DeviceMessage)context.getMessage();
     *
     *    return context
     *      .reply(handleMessage(message)) //返回结果给指令发送者
     *      .then(Mono.empty())
     * }
     *
     * </pre>
     *
     * 如果要串行发送数据,可以参考使用{@link ParallelIntervalHelper}工具类
     * @param context 消息上下文
     * @return 编码结果
     * @see MqttMessage
     * @see org.jetlinks.core.message.Message
     * @see org.jetlinks.core.message.property.ReadPropertyMessage 读取设备属性
     * @see org.jetlinks.core.message.property.WritePropertyMessage 修改设备属性
     * @see org.jetlinks.core.message.function.FunctionInvokeMessage 调用设备功能
     * @see org.jetlinks.core.message.ChildDeviceMessage 子设备消息
     * @see org.jetlinks.core.message.interceptor.DeviceMessageEncodeInterceptor
     * @see ParallelIntervalHelper
     * @see org.jetlinks.core.trace.DeviceTracer.SpanName#encode(String)
     */
    @Nonnull
    Publisher<? extends EncodedMessage> encode(@Nonnull MessageEncodeContext context);

}
```
  
接口`DeviceMessageDecoder`定义：
```java

/**
 * 设备消息解码器，用于将收到设备上传的消息解码为可读的消息。
 *
 * @author zhouhao
 * @since 1.0
 */
public interface DeviceMessageDecoder {

    /**
     * 在服务器收到设备或者网络组件中发来的消息时，会调用协议包中的此方法来进行解码，
     * 将数据{@link EncodedMessage}转为平台的统一消息{@link org.jetlinks.core.message.DeviceMessage}
     *
     * <pre>
     * //解码并返回一个消息
     * public Mono&lt;DeviceMessage&gt; decode(MessageDecodeContext context){
     *
     *  EncodedMessage message = context.getMessage();
     *  byte[] payload = message.payloadAsBytes();//上报的数据
     *
     *  DeviceMessage message = doEncode(payload);
     *
     *  return Mono.just(message);
     * }
     *
     * //解码并返回多个消息
     * public Flux&lt;DeviceMessage&gt; decode(MessageDecodeContext context){
     *
     *  EncodedMessage message = context.getMessage();
     *  byte[] payload = message.payloadAsBytes();//上报的数据
     *
     *  List&lt;DeviceMessage&gt; messages = doEncode(payload);
     *
     *  return Flux.fromIterable(messages);
     * }
     *
     * //解码,回复设备并返回一个消息
     * public Mono&lt;DeviceMessage&gt; decode(MessageDecodeContext context){
     *
     *  EncodedMessage message = context.getMessage();
     *  byte[] payload = message.payloadAsBytes();//上报的数据
     *
     *  DeviceMessage message = doEncode(payload); //解码
     *
     *  FromDeviceMessageContext ctx = (FromDeviceMessageContext)context;
     *
     *  EncodedMessage msg = createReplyMessage(); //构造回复
     *
     *  return ctx
     *     .getSession()
     *     .send(msg) //发送回复
     *     .thenReturn(message);
     * }
     *
     * </pre>
     *
     * @param context 消息上下文
     * @return 解码结果
     * @see MqttMessage
     * @see org.jetlinks.core.message.codec.http.HttpExchangeMessage
     * @see CoapExchangeMessage
     * @see org.jetlinks.core.message.DeviceMessageReply
     * @see org.jetlinks.core.message.property.ReadPropertyMessageReply
     * @see org.jetlinks.core.message.function.FunctionInvokeMessageReply
     * @see org.jetlinks.core.message.ChildDeviceMessageReply
     * @see org.jetlinks.core.message.DeviceOnlineMessage
     * @see org.jetlinks.core.message.DeviceOfflineMessage
     * @see org.jetlinks.core.message.interceptor.DeviceMessageDecodeInterceptor
     * @see org.jetlinks.core.message.DeviceDataManager
     */
    @Nonnull
    Publisher<? extends Message> decode(@Nonnull MessageDecodeContext context);
}
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

方法返回值是响应式结果,根据情况返回`Mono`(单条消息)或者`Flux`(多条消息).

</div>

## 上下文

### 编码上下文类结构

```java
class MessageEncodeContext{
    //获取当前设备操作接口,可通过此接口获取对应设备的配置等信息
    DeviceOperator getDevice();
    //平台下发的指令,具体请查看平台统一设备消息定义
    Message getMessage();

    //强制回复设备消息,在http等场景下,通过调用http api下发指令,然后直接调用此方法回复结果即可.
    Mono<Void> reply(Publisher<? extends DeviceMessage> replyMessage);

    //获取当前会话,需要将MessageEncodeContext强制转换为ToDeviceMessageContext
    DeviceSession getSession();
}
```

### 解码上下文类结构

```java
class class MessageDecodeContext{
    //获取当前设备操作接口,可通过此接口获取对应设备的配置等信息
    DeviceOperator getDevice();

    //从网络组件中接收到的消息,不同的网络组件消息类型不同,
    //使用时根据网络方式强制转换为对应的类型.
    EncodedMessage getMessage();
}
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

不同的网络协议需要转换为不同的`EncodedMessage`类型.比如,MQTT需要转换为`MqttMessage`.

大部分情况下:`MessageDecodeContext`可转为`FromDeviceMessageContext`,可获取到当前设备的连接会话`DeviceSession`,通过会话可以直接发送消息到设备.


</div>

### EncodedMessage
从网络组件中接收到的消息,不同的网络组件消息类型不同。
公共方法:

```java
class EncodedMessage{
    //获取原始报文
    ByteBuf getPayload();
    //报文转为字符串
    String payloadAsString();
    //报文转为JSON对象
    JSONObject payloadAsJson();
    //报文转为JSON数组
    JSONArray payloadAsJsonArray();
    // 报文转为字节数组
    byte[] payloadAsBytes();
}
```

### MQTT消息

```java
public interface MqttMessage extends EncodedMessage {

    @Nonnull
    String getTopic();

    String getClientId();

    int getMessageId();

    default boolean isWill() {
        return false;
    }

    default int getQosLevel() {
        return 0;
    }

    default boolean isDup() {
        return false;
    }

    default boolean isRetain() {
        return false;
    }

    default String print() {
        return StringBuilderUtils
                .buildString(this, (msg, builder) -> {
                    builder.append("qos").append(getQosLevel()).append(" ").append(getTopic()).append("\n")
                            .append("messageId: ").append(getMessageId()).append("\n")
                            .append("dup: ").append(isDup()).append("\n")
                            .append("retain: ").append(isRetain()).append("\n")
                            .append("will: ").append(isWill()).append("\n\n");
                    ByteBuf payload = getPayload();
                    if (ByteBufUtil.isText(payload, StandardCharsets.UTF_8)) {
                        builder.append(payload.toString(StandardCharsets.UTF_8));
                    } else {
                        ByteBufUtil.appendPrettyHexDump(builder, payload);
                    }
                });
    }
}
```

### HTTP消息

如果是`POST`,`PUT`,`PATCH`等请求,`EncodedMessage.getPayload`即为请求体.

```java

/**
 * 可响应的http消息
 *
 * @author zhouhao
 * @see HttpRequestMessage
 * @see HttpResponseMessage
 * @see SimpleHttpResponseMessage
 * @since 1.0.2
 */
public interface HttpExchangeMessage extends HttpRequestMessage {

    @Nonnull
    Mono<Void> response(@Nonnull HttpResponseMessage message);

    default Mono<Void> ok(@Nonnull String message) {
        return response(
                SimpleHttpResponseMessage.builder()
                        .contentType(MediaType.APPLICATION_JSON)
                        .status(200)
                        .body(message)
                        .build()
        );
    }

    default Mono<Void> error(int status, @Nonnull String message) {
        return response(SimpleHttpResponseMessage.builder()
                .contentType(MediaType.APPLICATION_JSON)
                .status(status)
                .body(message)
                .build());
    }

    default SimpleHttpResponseMessage.SimpleHttpResponseMessageBuilder newResponse() {
        return SimpleHttpResponseMessage.builder();
    }
}
```

### CoAP消息
```java
public class CoapExchangeMessage implements CoapMessage {

    @Getter
    protected CoapExchange exchange;

    @Nonnull
    @Override
    public CoAP.Code getCode() {
        return exchange.getRequestCode();
    }

    public CoapExchangeMessage(CoapExchange exchange) {
        this.exchange = exchange;
    }

    static byte[] empty = new byte[0];

    public void response(CoAP.ResponseCode code) {
        Response response = new Response(code);
        exchange.advanced().sendResponse(response);
    }

    public void response(CoAP.ResponseCode code,byte[] body) {
        Response response = new Response(code);
        response.setBytes(body);
        exchange.advanced().sendResponse(response);
    }

    public void response(CoapResponseMessage message) {
        Response response = new Response(message.getCode());

        if (!CollectionUtils.isEmpty(message.getOptions())) {
            message.getOptions().forEach(response.getOptions()::addOption);
        }
        byte[] payload = message.payloadAsBytes();
        if (payload.length > 0) {
            response.setPayload(payload);
        }

        exchange.advanced().sendResponse(response);

    }

    @Nonnull
    @Override
    public ByteBuf getPayload() {
        if (exchange.getRequestPayload() == null) {
            return Unpooled.wrappedBuffer(empty);
        }
        return Unpooled.wrappedBuffer(exchange.getRequestPayload());
    }

    @Override
    public String toString() {
        return print(true);
    }

    @Override
    @Nonnull
    public String getPath() {
        return exchange.getRequestOptions().getUriPathString();
    }

    @Override
    @Nonnull
    public List<Option> getOptions() {
        return exchange
                .getRequestOptions()
                .asSortedList();
    }
}

```

### TCP,UDP消息
TCP和UDP 直接操作`EncodedMessage`中的方法即可


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

配置元数据用于告诉平台，在使用此协议的时候，针对特定的消息传输方式，需要添加一些自定义配置到设备配置(`DeviceOperator.setConfig`)中.
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
            .add("password", "password", "MQTT密码", new PasswordType())
            .add("productKey", "productKey", "产品密钥", new PasswordType(),DeviceConfigScope.product) //只有产品需要配置
            ;
//设置MQTT所需要的配置
 support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);

```

## 配置基本信息
配置协议的基本信息，用于在平台中显示（例如设备接入网关和产品详情）。基本包括协议默认ID、名称、描述、路由信息、markdown文档、协议特性、默认物模型等。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

基本信息可在`ProtocolSupportProvider`的实现类中的`create`方法里添加。通过返回对象`ProtocolSupport`中的方法添加具体配置。

</div>


### 配置路由信息
可在协议包中为指定的传输方式添加路由信息，描述协议中定义的路由。例如：
```java
support.addRoutes(DefaultTransport.HTTP, Stream
                    .of(TopicMessageCodec.reportProperty,
                        TopicMessageCodec.event,
                        TopicMessageCodec.online,
                        TopicMessageCodec.offline)
                    .map(TopicMessageCodec::getRoute)
                    .filter(route -> route != null && route.isUpstream())
                    .map(route -> HttpRoute
                            .builder()
                            .address(route.getTopic())
                            .group(route.getGroup())
                            .contentType(MediaType.APPLICATION_JSON)
                            .method(HttpMethod.POST)
                            .description(route.getDescription())
                            .example(route.getExample())
                            .build())
                    .collect(Collectors.toList())
            );
```

### 配置markdown文档
可以在协议包的精通资源目录（`src/main/resources`）中添加markdown文档，然后配置到ProtocolSupport中。例如：
```java
 support.setDocument(DefaultTransport.HTTP,
                                "document-http.md",
                                JetLinksProtocolSupportProvider.class.getClassLoader());
```

### 配置协议特性
定义协议的特性。例如是否支持远程升级。例如：
```java
       support.addFeature(DefaultTransport.MQTT, DeviceFeatures.supportFirmware);
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

特性都继承于接口`org.jetlinks.core.metadata.Feature`。
  
默认定义的特性如下

<table class='table'>
        <thead>
            <tr>
              <td>特性</td>
              <td>定义的类</td>
              <td>描述</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>是否支持远程升级</td>
            <td>DeviceFeatures.supportFirmware</td>
            <td>

通过此特性判断是否支持远程升级。协议包添加此特性后，使用此协议包的产品才会出现在平台的<a href='/Mocha_ITOM/Device_access_gateway5.1.html#远程升级'>远程升级</a>产品下拉选择中

</td>
          </tr>
          <tr>
            <td>是否支持透传</td>
            <td>CodecFeature.transparentCodec</td>
            <td>

通过此特性判断是否支持透传。产品和设备详情根据此特性判断是否显示<a href='/device_management/product4.1.html#数据解析'>数据解析</a>页面

</td>
          </tr>
          <tr>
            <td>物模型控制</td>
            <td>MetadataFeature</td>
            <td>

控制物模型是否支持增删改，以及是否支持派生。

</td>
          </tr>
        </tbody>
      </table>

</div>

### 配置默认物模型
定义协议的默认物模型。产品选择接入方式后自动添加到产品物模型。

例如：
```java
            String json = "{\"properties\":[{\"valueType\":{\"type\":\"int\",\"expands\":{},\"unit\":\"volt\"},\"expands\":{\"type\":[\"read\",\"write\",\"report\"],\"metrics\":[]},\"name\":\"电压\",\"id\":\"1\",\"sortsIndex\":0}]}";
            JetLinksDeviceMetadata metadata = new JetLinksDeviceMetadata(JSON.parseObject(json));
            support.addDefaultMetadata(DefaultTransport.MQTT, metadata);
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

可在产品详情页面中的`物模型映射`中手动匹配设备上报的物模型与平台物模型。

</div>

## 设备自注册

原理: 自定义协议包将消息解析为`DeviceRegisterMessage`,并设置header:`productId`(必选),`deviceName`(必选),`configuration`(可选)。
平台将自动添加设备信息到设备实例中。如果是注册子设备,则解析为 `ChildDeviceMessage<DeviceRegisterMessage>`即可

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

header中的`configuration`为设备的自定义配置信息，会保持到`DeviceInstanceEntity.configuration`中，类型为`Map<String,Object>`，
在后续的操作中,可通过`DeviceOperator.getSelfConfig`来获取这些配置信息。

</div>


## 完整例子

[演示协议](https://github.com/jetlinks/demo-protocol)
