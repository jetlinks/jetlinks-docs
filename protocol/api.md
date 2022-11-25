# 协议包核心类API文档

## 1、概述

本文档主要提供协议包开发涉及的核心类、接口及方法进行说明。

## 2、核心类及方法

### 入口类列表

1. <a href='#ProtocolSupportProvider'>ProtocolSupportProvider</a>
2. <a href='#ProtocolSupport'>ProtocolSupport</a>
3. <a href='#CompositeProtocolSupport'>CompositeProtocolSupport</a>
4. <a href='#ConfigMetadata'>ConfigMetadata</a>
5. <a href='#DeviceMetadataCodec'>DeviceMetadataCodec</a>
6. <a href='#DeviceMetadata'>DeviceMetadata</a>
7. <a href='#Transport'>Transport</a>
8. <a href='#Feature'>Feature</a>
9. <a href='#Route'>Route</a>
10. <a href='#ServiceContext'>ServiceContext</a>

### 解码器类列表

1. <a href='#DeviceMessageCodec'>DeviceMessageCodec</a>
2. <a href='#DeviceMessageEncoder'>DeviceMessageEncoder</a>
3. <a href='#DeviceMessageDecoder'>DeviceMessageDecoder</a>
4. <a href='#MessageCodecContext'>MessageCodecContext</a>
5. <a href='#MessageDecodeContext'>MessageDecodeContext</a>
6. <a href='#MessageEncodeContext'>MessageEncodeContext</a>

### 消息类列表

1. <a href='#Message'>Message</a>
2. <a href='#DeviceMessage'>DeviceMessage</a>
3. <a href='#EncodedMessage'>EncodedMessage</a>

### 认证类列表

1. <a href='#Authenticator'>Authenticator</a>
2. <a href='#AuthenticationRequest'>AuthenticationRequest</a>
3. <a href='#AuthenticationResponse'>AuthenticationResponse</a>

### 设备操作相关列表

1. <a href='#DeviceRegistry'>DeviceRegistry</a>
2. <a href='#DeviceOperator'>DeviceOperator</a>

<br xmlns="http://www.w3.org/1999/html">

<a id='ProtocolSupportProvider'>ProtocolSupportProvider</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> 
    <li>整个协议包的入口，用于创建协议支持。</li>
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {
    
    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        ...
    }
}

```

| 核心方法                                                     | 返回值                                                       | 描述         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------ |
| create( ServiceContext context ) <br>context – 服务上下文，用于从上下文中获取其他服务(如获取spring容器中的bean)、配置等操作 | Mono`<? extends ProtocolSupport> `<br>Returns : ProtocolSupport接口的实现类(消息协议支持接口，通过实现此接口来自定义消息协议) | 创建协议支持 |

<br>

<a id='ProtocolSupport'>ProtocolSupport</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
    <li>消息协议支持接口，通过实现此接口来自定义消息协议。</li>
    <p>默认实现类：<a id='CompositeProtocolSupport'>CompositeProtocolSupport</a></p>
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {
    
    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        //声明协议支持
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        
        ...
        return Mono.just(support);
    }
}

```

| 核心方法                                                     | 返回值                                                       | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| getMessageCodec( Mono<? extends Transport transport )<br> transport – 传输协议 | Mono`< ? extends DeviceMessageCodec >`<br/> Returns : 消息编解码器 | 获取设备消息编码解码器，用于将平台统一的消息对象转码为设备的消息，以及将设备发送的消息转码为平台统一的消息对象 |
| authenticate( @Nonnull AuthenticationRequest request, @Nonnull DeviceOperator deviceOperation ) <br/>request – 认证请求,不同的连接方式实现不同<br/> deviceOperation – 设备操作接口,可用于配置设备 | Mono`<AuthenticationResponse>`<br/> Returns : 认证结果       | 进行设备认证                                                 |
| authenticate(@Nonnull AuthenticationRequest request, @Nonnull DeviceRegistry registry ) <br/> request – 认证请求<br/> registry – 注册中心 | Mono`<AuthenticationResponse>`                               | 对不明确的设备进行认证                                       |
| getConfigMetadata( Transport  transport ) <br/>transport – 传输协议 | Mono`<ConfigMetadata>` <br/>Returns : 配置信息               | 获取协议所需的配置信息定义                                   |
| getInitConfigMetadata( Transport  transport )                | Mono`<ConfigMetadata>`                                       | 获取协议初始化所需要的配置定义                               |
| getDefaultMetadata( Transport transport )                    | Mono`<DeviceMetadata>`                                       | 获取默认物模型                                               |
| getMetadataExpandsConfig( Transport transport, DeviceMetadataType metadataType, String metadataId, String dataTypeId ) <br/>transport – 传输协议 <br/>metadataType – 物模型类型 <br/>metadataId – 物模型标识 <br/>dataTypeId – 数据类型ID | Mono`<DeviceMetadata>`                                       | 获取物模型拓展配置定义                                       |
| getFeatures( Transport transport )                           | Flux`<Feature>`</br> Returns : 特性集                        | 获取协议支持的某些自定义特性                                 |
| getRoutes( Transport transport )                             | Flux`<Route>` </br>Returns : 路由信息                        | 获取指定协议的路由信息，比如MQTT topic，HTTP url地址         |

<br>

<a id='DeviceMetadataCodec'>DeviceMetadataCodec</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <li>物模型编解码器,用于将物模型与字符串进行互相转换</li>
    <p>协议包内添加物模型解码器支持，默认实现类：JetLinksDeviceMetadataCodec</p>
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {
    

    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        //声明使用JetLinks默认物模型编解码器
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        return Mono.just(support);
    }
}

```

| 核心方法                                        | 返回值                                       | 描述                 |
| ----------------------------------------------- | -------------------------------------------- | -------------------- |
| decode( String source ) <br>source – 数据       | Mono`<DeviceMetadata> `<br/>Returns : 物模型 | 将数据解码为物模型   |
| encode( DeviceMetadata ) <br/>metadata – 物模型 | Mono`<String>` <br/>Returns : 物模型字符串   | 将物模型编码为字符串 |

<br>

<a id='ConfigMetadata'>ConfigMetadata</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> 
    <li>配置信息定义，用于定义配置信息。</li>
    <p>在ProtocolSupportProvider内添加支持后，可以在产品、设备页面展示配置项</p>
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {

    private static final DefaultConfigMetadata mqttConfig = new DefaultConfigMetadata(
            "MQTT认证配置"
            , "MQTT认证时需要的配置,mqtt用户名,密码算法:\n" +
            "username=secureId|timestamp\n" +
            "password=md5(secureId|timestamp|secureKey)\n" +
            "\n" +
            "timestamp为时间戳,与服务时间不能相差5分钟")
            .add("secureId", "secureId", "密钥ID", new StringType())
            .add("secureKey", "secureKey", "密钥KEY", new PasswordType());
    
    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        //对指定通信协议添加动态配置项支持
        support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);
        return Mono.just(support);
    }
}

```

| 核心方法                                        | 返回值                                | 描述                                                         |
| ----------------------------------------------- | ------------------------------------- | ------------------------------------------------------------ |
| getProperties()                                 | List`<ConfigPropertyMetadata>`        | 返回配置属性信息                                             |
| copy( ConfigScope... scopes ) <br>scopes – 范围 | ConfigMetadata <br>Returns : 新的配置 | 复制为新的配置，并按指定的scope过滤属性，只返回符合scope的属性，ConfigScope为配置作用域，请使用枚举实现此接口 |

<br>

<a id='DeviceMetadata'>DeviceMetadata</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> 
    <li>设备物模型定义</li>
    <p>JetLinks设备物模型定义默认实现：JetLinksDeviceMetadata</p>
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {

    private static final DeviceMetadata mqttConfig = new DefaultConfigMetadata(
            "MQTT认证配置"
            , "MQTT认证时需要的配置,mqtt用户名,密码算法:\n" +
            "username=secureId|timestamp\n" +
            "password=md5(secureId|timestamp|secureKey)\n" +
            "\n" +
            "timestamp为时间戳,与服务时间不能相差5分钟")
            .add("secureId", "secureId", "密钥ID", new StringType())
            .add("secureKey", "secureKey", "密钥KEY", new PasswordType());
    
    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);
        //添加默认物模型信息
        support.addDefaultMetadata(DefaultTransport.MQTT,new JetLinksDeviceMetadata("temperature","温度"));
        return Mono.just(support);
    }
}

```

| 核心方法        | 返回值                   | 描述             |
| --------------- | ------------------------ | ---------------- |
| getProperties() | List`<PropertyMetadata>` | 获取所有属性定义 |
| getFunctions()  | List`<FunctionMetadata>` | 获取所有功能定义 |
| getEvents()     | List`<EventMetadata>`    | 获取事件定义     |
| getTags()       | List`<PropertyMetadata>` | 获取标签定义     |

<br>

<a id='Transport'>Transport</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>传输协议定义，如: TCP、MQTT等，通常使用枚举来定义</li>
</div>

| 核心方法                                                     | 返回值                                                | 描述                                                         |
| ------------------------------------------------------------ | ----------------------------------------------------- | ------------------------------------------------------------ |
| isSame( Transport transport )  <br/>transport – 要比较的协议 | boolean  <br/>Returns : 是否为同一个协议              | 比较与传入的协议是否为同一个协议                             |
| isSame( String transportId )  <br/>transportId – 协议ID      | boolean  <br/>Returns : 是否为同一个协议              | 使用ID进行对比，判断是否为同一个协议                         |
| of( String id )  <br/>id – 协议ID                            | Transport <br/>Returns : 协议                         | 使用指定的ID来创建协议定义                                   |
| lookup( String id ) <br/> id – 协议ID                        | Optional`<Transport >`  <br/>Returns : 是否存在该协议 | 通过ID查找协议定义，可通过Transports.register(Transport )来注册自定义的协议 |
| getAll()                                                     | List<Transport \> <br>Returns : 协议集合              | 获取全部协议定义                                             |

<br>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>Transport接口在平台有默认的实现类DefaultTransport，该类为一个枚举类，内部定义的枚举对象如下</li>
</div>

| 枚举对象       | 说明                      |
| -------------- | ------------------------- |
| MQTT           | MQTT协议                  |
| ~~MQTT_TLS~~   | 已废弃,可能在后续版本移除 |
| UDP            | UDP协议                   |
| ~~UDP_DTLS~~   | 已废弃,可能在后续版本移除 |
| CoAP           | CoAP协议                  |
| ~~CoAP_DTLS~~  | 已废弃,可能在后续版本移除 |
| TCP            | TCP协议                   |
| ~~TCP_TLS~~    | 已废弃,可能在后续版本移除 |
| HTTP           | HTTP协议                  |
| ~~HTTPS~~      | 已废弃,可能在后续版本移除 |
| WebSocket      | WebSocket协议             |
| ~~WebSockets~~ | 已废弃,可能在后续版本移除 |

<br>

<a id='Feature'>Feature</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>特性接口，一般使用枚举实现。用于定义协议或者设备支持的某些特性</li>
</div>

| 核心方法                     | 返回值                                                  | 描述                                     |
| ---------------------------- | ------------------------------------------------------- | ---------------------------------------- |
| getId()                      | String <br>Returns : 特性的id                           | 唯一标识                                 |
| getName()                    | String <br/>Returns : 特性的名称                        | 名称                                     |
| getType()                    | String <br/>Returns : 特性的类型                        | 特性类型，用于进行分类，比如: 协议特性等 |
| isDeprecated()               | boolean <br/>Returns : 是否弃用                         | 是否已经被弃用                           |
| of( String id, String name ) | Feature <br/>Returns : 根据特性的id、名称返回特定的特性 | 根据id和name获取一个特性                 |

<br>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>Feature接口在平台有4个实现类分别为 CodecFeature，DeviceFeatures，ManagementFeature，以及 MetadataFeature</li>
</div>

**CodecFeature**

| 枚举对象         | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| transparentCodec | 标识协议支持透传消息,支持透传的协议可以在界面上动态配置解析规则 |

<br>

**DeviceFeatures**

| 枚举对象        | 说明                             |
| --------------- | -------------------------------- |
| supportFirmware | 标识使用此协议的设备支持固件升级 |

<br>

**ManagementFeature**

| 枚举对象             | 说明           |
| -------------------- | -------------- |
| autoGenerateDeviceId | 自动生成设备ID |

<br>

**MetadataFeature**

| 枚举对象                  | 说明                       |
| ------------------------- | -------------------------- |
| supportDerivedMetadata    | 设备支持派生物模型         |
| propertyNotModifiable     | 物模型属性不可修改         |
| propertyTypeNotModifiable | 物模型属性数据类型不可修改 |
| propertyNotDeletable      | 物模型属性不可删除         |
| propertyNotInsertable     | 物模型属性不可新增         |
| functionNotInsertable     | 物模型功能不可新增         |
| functionNotModifiable     | 物模型功能不可修改         |
| functionNotDeletable      | 物模型功能不可删除         |
| eventNotInsertable        | 物模型事件不可新增         |
| eventNotModifiable        | 物模型事件不可修改         |
| eventNotDeletable         | 物模型事件不可删除         |

<br>

<a id='Route'>Route</a>

| 核心方法         | 返回值 | 描述     |
| ---------------- | ------ | -------- |
| getGroup()       | String | 获取分组 |
| getAddress()     | String | 获取地址 |
| getDescription() | String | 获取说明 |
| getExample()     | String | 获取示例 |

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>该接口存在两个子接口，HttpRoute 和 MqttRoute，对应接口中的内部接口Builder定义了各自不同的属性</li>
</div>


**HttpRoute.Builder**

| 属性        | 数据类型       | 说明                                                         |
| ----------- | -------------- | ------------------------------------------------------------ |
| group       | String         | 分组                                                         |
| method      | HttpMethod\[\] | 提交方法，如HTTP的 GET、POST方法等                           |
| contentType | MediaType\[\]  | 媒体形式，如application/json、application/x-www-form-urlencoded等 |
| address     | String         | 地址                                                         |
| description | String         | 描述                                                         |
| example     | String         | 示例                                                         |

| 方法      | 返回值            | 描述                                                         |
| --------- | ----------------- | ------------------------------------------------------------ |
| builder() | HttpRoute.Builder | 用于构建HttpRoute.Builder(内部接口，用于定义该接口独有的属性) |

<br>

**MqttRoute.Builder**

| 属性        | 数据类型 | 说明                  |
| ----------- | -------- | --------------------- |
| topic       | String   | 主题                  |
| upstream    | boolean  | 是否是上行            |
| downstream  | boolean  | 是否是下行            |
| qos         | int      | 消息服务质量，默认为0 |
| group       | String   | 分组                  |
| description | String   | 描述                  |
| example     | String   | 示例                  |

| 方法      | 返回值            | 描述                                                         |
| --------- | ----------------- | ------------------------------------------------------------ |
| builder() | MqttRoute.Builder | 用于构建MqttRoute.Builder(内部接口，用于定义该接口独有的属性) |

<br>

<a id='ServiceContext'>ServiceContext</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>服务上下文，用于从服务中获取其他服务(如获取spring容器中的bean)，配置等操作</li>
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {
    
    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        return Mono
                .zip(
                        Mono.justOrEmpty(context.getService(DecodedClientMessageHandler.class)),
                        Mono.justOrEmpty(context.getService(DeviceSessionManager.class)),
                        Mono.justOrEmpty(context.getService(Vertx.class))
                )
                .map(tp3 -> new TcpClientMessageSupport(tp3.getT1(), tp3.getT2(), tp3.getT3()))
    }
}

```

| 核心方法                                                     | 返回值                                                | 描述                                                        |
| ------------------------------------------------------------ | ----------------------------------------------------- | ----------------------------------------------------------- |
| getConfig( ConfigKey`<String> `key ) <br>key - 配置的key     | Optional`<Value>` <br/>Returns : 是否存在对应的数据值 | 根据配置的ConfigKey获取服务(如获取spring容器中的bean)或配置 |
| getConfig( String key )                                      | Optional`<Value>`                                     | 根据key获取服务(如获取spring容器中的bean)或配置             |
| getService( Class`<T>` service ) <br/>service - 服务的类类型 | `<T>` Optional`<T> `<br/>Returns : 是否存在该服务     | 根据类类型获取服务                                          |
| getService( String service )                                 | `<T>` Optional`<T>`                                   | 根据服务名获取服务                                          |
| getServices( Class`<T>` service ) <br/>service - 服务的类类型 | `<T>` List`<T>` <br/>Returns : 多个指定类类型的服务集 | 根据类类型获取多个服务                                      |
| getServices( String service )                                | `<T>` List`<T>`                                       | 根据服务名获取多个服务                                      |

<br>

<a id='DeviceMessageCodec'>DeviceMessageCodec</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>设备消息转换器，用于对不同协议的消息进行转换</li>
</div>

| 核心方法              | 返回值                                    | 描述               |
| --------------------- | ----------------------------------------- | ------------------ |
| getSupportTransport() | Transport                                 | 返回支持的传输协议 |
| getDescription()      | Mono`<? extends MessageCodecDescription>` | 获取协议描述       |

<br>

<a id='DeviceMessageEncoder'>DeviceMessageEncoder</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>设备消息编码器，用于将消息对象编码为对应消息协议的消息</li>
</div>

| 核心方法                                                     | 返回值                                                       | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| encode( @Nonnull MessageEncodeContext context ) context – 消息编码上下文 | Publisher`<? extends EncodedMessage> `Returns : 设备能读取的编码消息 | 编码，将消息进行编码，用于发送到设备端，平台在发送指令给设备时，会调用协议包中设置的此方法，将平台消息DeviceMessage转为设备能理解的消息EncodedMessage |

<br>

<a id='DeviceMessageDecoder'>DeviceMessageDecoder</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>设备消息解码器，用于将收到设备上传的消息解码为可读的消息</li>
</div>

| 核心方法                                                     | 返回值                                                       | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| decode( @Nonnull MessageDecodeContext  context ) context – 消息解码上下文 | Publisher`<? extends Message>` Returns : 平台可识别的消息，根据设备上传的报文通过协议包进行解码之后会转换成平台消息 | 在服务器收到设备或者网络组件中发来的消息时，会调用协议包中的此方法来进行解码， 将数据EncodedMessage转为平台的统一消息DeviceMessage |

<br>

<a id='MessageCodecContext'>MessageCodecContext</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>消息编解码上下文</li>
</div>

| 核心方法                                       | 返回值                                       | 描述                                                         |
| ---------------------------------------------- | -------------------------------------------- | ------------------------------------------------------------ |
| getDevice()                                    | DeviceOperator <br>Returns : 设备操作        | 获取当前上下文中到设备操作接口，在tcp，http等场景下，此接口可能返回null |
| getDeviceAsync()                               | Mono`<DeviceOperator>`                       | 获取指定设备的操作接口，如果设备不存在，则为Mono.empty()，可以通过Mono.switchIfEmpty(Mono)进行处理 |
| getDevice( String deviceId ) deviceId – 设备ID | Mono`<DeviceOperator>`                       | 获取指定设备的操作接口，如果设备不存在,则为Mono.empty()，可以通过Mono.switchIfEmpty(Mono)进行处理. |
| getConfiguration()                             | Map`<String, Object> `<br>Returns : 配置信息 | 预留功能，获取配置信息                                       |

<br>

<a id='MessageDecodeContext'>MessageDecodeContext</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>设备上报原始消息接口</li>
</div>

| 核心方法     | 返回值                                | 描述                                                         |
| ------------ | ------------------------------------- | ------------------------------------------------------------ |
| getMessage() | EncodedMessage<br> Returns : 原始消息 | 获取设备上报的原始消息，根据通信协议的不同,消息类型也不同， 在使用时可能需要转换为对应的消息类型 |

<br>

<a id='MessageEncodeContext'>MessageEncodeContext</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>消息编码上下文，用于平台向设备发送指令并使用协议包进行编码时，可以从上下文中获取一些参数。 通常此接口可强制转换为ToDeviceMessageContext</li>
</div>

| 核心方法                                                     | 返回值                                    | 描述                                                         |
| ------------------------------------------------------------ | ----------------------------------------- | ------------------------------------------------------------ |
| getMessage()                                                 | Message <br>Returns : 消息                | 获取平台下发的给设备的消息指令，根据物模型中定义对应不同的消息类型。 在使用时，需要判断对应的类型进行不同的处理 |
| reply( @Nonnull Publisher`<? extends DeviceMessage>` replyMessage ) <br>replyMessage – 消息流 | Mono`<Void>`                              | 直接回复消息给平台.在类似通过http接入时，下发指令可能是一个同步操作，则可以通过此方法直接回复平台. |
| reply( @Nonnull DeviceMessage ... messages ) <br>messages – 多个设备消息 | Mono`<Void>`                              | 重载方法                                                     |
| reply( @Nonnull Collection`<? extends DeviceMessage>` messages ) <br>messages – 设备消息的集合 | Mono`<Void>`                              | 重载方法                                                     |
| mutate( Message anotherMessage, DeviceOperator another )<br>Message – 设备消息 <br>device – 设备操作接口 | MessageEncodeContext <br>Returns : 上下文 | 使用新的消息和设备，转换为新上下文                           |

<br>

<a id='Message' style='text-decoration:none;cursor:default'>Message</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  Message：消息的顶层接口，device是他的子接口之一
</div>

<a id='DeviceMessage' style='text-decoration:none;cursor:default'>DeviceMessage</a>
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceMessage：设备消息
</div>

核心方法说明：

|方法|类型|描述|
|----|-----|----|
|   getDeviceId()        |  String      |   获取设备ID     |
<br/>

<a id='CommonDeviceMessage' style='text-decoration:none;cursor:default'>CommonDeviceMessage</a>
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  CommonDeviceMessage：通用设备消息，实现deviceMessage接口
</div>

核心参数说明：

|参数|类型|描述| 是否必填|
|----|-----|----|-----|
|   messageId        |  String      |   消息ID，是设备与平台的通信凭证     | 是  |
|   deviceId        |  String      |   设备ID     | 是  |
|   timestamp        |  long      |   毫秒时间戳    | 否  |

<br/>

```java
CommonDeviceMessage的主要实现类说明：
    1、 ChildDeviceMessage：子设备消息
    2、 ChildDeviceMessageReply：子设备消息回复
    3、 DerivedMetadataMessage：派生物模型消息
    4、 DeviceOnlineMessage：上线消息
    5、 DeviceOfflineMessage：离线消息
    6、 DeviceRegisterMessage 设备注册消息
    7、 DirectDeviceMessage：透传消息
    8、 EventMessage：事件消息
    9、 FunctionInvokeMessage：功能调用消息
    10、FunctionInvokeMessageReply：功能调用消息得回复
    11、ReadPropertyMessage：读取属性消息
    12、ReadPropertyMessageReply：读取属性消息的回复
    13、ReportPropertyMessage：上报属性消息
    14、UpdateTagMessage：
    15、WritePropertyMessage：修改属性消息
    16、WritePropertyMessageReply：修改属性消息的回复
```


<br>
ChildDeviceMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  ChildDeviceMessage：子设备消息，发往子设备的消息,通常是通过网关设备接入平台的设备。<br/>方向：平台->设备
</div>

核心参数说明：

|参数|类型|描述|
|----|-----|----|
|  childDeviceId        |  String      |   子设备ID     |是|
|  childDeviceMessage   | Message      |   消息数据     |否|

<br>

ChildDeviceMessageReply
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  ChildDeviceMessageReply：子设备消息的回复，设备对发往子设备消息的回复消息。<br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|
|----|-----|----|
|  childDeviceId        |  String      |   子设备ID     |是|
|  childDeviceMessage   | Message      |   消息数据     |否|

<br>

DerivedMetadataMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DerivedMetadataMessage：派生物模型上报。<br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|  metadata        |  String      |   物模型json字符     |  否|
|  all   | boolean      |   是否是全量数据     |否|

<br>

DeviceOnlineMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceOnlineMessage：设备上线消息。<br/>方向：设备->平台
</div>

核心方法说明：
```java
//返回枚举值，表示设备上线消息
public MessageType getMessageType() {
//枚举值含义：返回一个DeviceOfflineMessage对象
  //ONLINE(DeviceOnlineMessage::new),
    return MessageType.ONLINE;
}
```
<br>

DeviceOfflineMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceOfflineMessage：设备离线消息。<br/>方向：设备->平台
</div>

核心方法说明：
```java
//返回枚举值，表示设备离线消息
public MessageType getMessageType() {
  //枚举值含义：返回一个DeviceOfflineMessage对象
  //OFFLINE(DeviceOfflineMessage::new),
    return MessageType.OFFLINE;
}

```

<br>

DeviceRegisterMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceRegisterMessage：设备注册消息,通常用于子设备连接,并自动与父设备进行绑定
</div>
<br>


DirectDeviceMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DirectDeviceMessage：透传设备消息。将报文传入mqtt payload中
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|  payload        |  byte[]      |   存放上报数据     |   否|
<br>

EventMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  EventMessage：事件消息。
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    event        |  String       |   事件标识   |   是    |
|    data       | Object        | 存放上报的数据 |   否    |
<br>

FunctionInvokeMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  FunctionInvokeMessage：平台功能调用消息。下发的json参数数据会议list集合的形式封装在inputs中
 <br/>方向：平台->设备
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    functionId        |  String       |   事件标识   |   是    |
|    inputs       | `List<FunctionParameter>`        | 存放下发的数据 |   否    |
```java
//FunctionParameter类
public class FunctionParameter implements Serializable {
    private static final long serialVersionUID = -6849794470754667710L;

    @NonNull
    private String name;

    private Object value;

	}
```
<br>
FunctionInvokeMessageReply
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  FunctionInvokeMessageReply：平台功能调用消息的回复。上报的参数数据以list集合的形式封装在output中
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    functionId        |  String       |   事件标识   |   是    |
|    output       | Object       | 存放上报的数据 |   否    |

<br>

ReadPropertyMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  ReadPropertyMessage：读取设备消息。下发指令后,设备需要回复指令ReadPropertyMessageReply,且getMessageId()值需要相同
 <br/>方向：平台->设备
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    properties        |  `List<String>`       |   属性值   |   是   |

<br>

ReadPropertyMessageReply
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  ReadPropertyMessageReply：在设备接收到ReadPropertyMessage消息后,使用此消息进行回复,回复后,指令发起方将收到响应结果.
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    properties        |  `Map<String, Object>`       |   属性值   |   是   |

<br>

ReportPropertyMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  ReportPropertyMessage：上报设备属性,通常由设备定时上报.
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    properties        |  `Map<String, Object>`       |   属性值信息,key为物模型中的属性ID,value为物模型对应的类型值.注意: value如果是结构体(对象类型),请勿传入在协议包中自定义的对象,应该转为Map传入.   |   否   |

<br>

ReportPropertyMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  ReportPropertyMessage：上报设备属性,通常由设备定时上报.
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    properties        |  `Map<String, Object>`       |   属性值信息,key为物模型中的属性ID,value为物模型对应的类型值.注意: value如果是结构体(对象类型),请勿传入在协议包中自定义的对象,应该转为Map传入.   |   是   |

<br>

UpdateTagMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  UpdateTagMessage：上报设备属性,通常由设备定时上报.
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    tags        |  `Map<String, Object>`       |  标签信息   |   是   |

<br>

WritePropertyMessage
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  WritePropertyMessage：修改属性指令,方向: 平台->设备
用于修改设备属性,下发指令后,设备需要回复WritePropertyMessageReply
 <br/>方向：平台->设备
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    properties        |  `Map<String, Object>`       |  要修改的属性，key 为物模型中对应的属性ID,value为属性值，`Map<String, Object>` properties = new LinkedHashMap<>()  |   是   |

<br>

WritePropertyMessageReply
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  WritePropertyMessageReply：修改属性指令回复
设备收到修改属性指令后作出相应的逻辑处理，给平台回复WritePropertyMessageReply消息
 <br/>方向：设备->平台
</div>

核心参数说明：

|参数|类型|描述|是否必填|
|----|-----|----|------|
|    properties        |  `Map<String, Object>`       |  回复的属性,key为物模型中的属性ID,value为物模型对应的类型值.注意: value如果是结构体(对象类型),请勿传入在协议包中自定义的对象,应该转为Map传入. |   是   |

<br>



<a id='Authenticator' style='text-decoration:none;cursor:default'>Authenticator</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>Authenticator：认证器,用于设备连接的时候进行认证</li>
</div>


| 核心方法                                                     | 返回值类型                     | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------ | ------------------------------------------------------------ |
| authenticate(AuthenticationRequest request,DeviceOperator device) <br/> Params: <br/>request – 认证请求 <br/>device – 设备 | `Mono<AuthenticationResponse>` | 对指定对设备进行认证                                         |
| authenticate(@Nonnull AuthenticationRequest request, @Nonnull DeviceRegistry registry) <br/>Params:<br/> request – 认证请求<br/> registry – 设备注册中心 | `Mono<AuthenticationResponse>` | 在网络连接建立的时候,可能无法获取设备的标识(如:http,websocket等),则会调用此方法来进行认证. 注意: 认证通过后,需要设置设备ID.AuthenticationResponse.success(String) |

<br>



<a id='AuthenticationRequest' style='text-decoration:none;cursor:default'>AuthenticationRequest</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>AuthenticationRequest</li>
  <li>MqttAuthenticationRequest 是它的默认实现类</li>
</div>


| 核心方法       | 返回值类型 | 描述         |
| -------------- | ---------- | ------------ |
| getTransport() | Transport  | 返回协议类型 |

<br>



<a id='AuthenticationResponse' style='text-decoration:none;cursor:default'>AuthenticationResponse</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>AuthenticationResponse(类)：封装了认证响应信息</li>
</div>


| 核心方法                        | 放回置类型             | 描述               |
| ------------------------------- | ---------------------- | ------------------ |
| success()                       | AuthenticationResponse | 封装成功的响应信息 |
| success(String deviceId)        | AuthenticationResponse | 封装成功的响应信息 |
| error(int code, String message) | AuthenticationResponse | 封装失败的响应信息 |

<br>



<a id='DeviceRegistry' style='text-decoration:none;cursor:default'>DeviceRegistry</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>设备注册中心,用于统一管理设备以及产品的基本信息,缓存,进行设备指令下发等操作. 例如获取设备以及设备的配置缓存信息</li>
  <li>registry  
.getDevice(deviceId)  
.flatMap(device->device.getSelfConfig("my-config"))  
.flatMap(conf-> doSomeThing(...))</li>
</div>


| 核心方法                                                     | 返回值类型                    | 描述                                                         |
| ------------------------------------------------------------ | ----------------------------- | ------------------------------------------------------------ |
| getDevice(String deviceId) <br/>Params:<br/> deviceId – 设备ID<br/> Returns: <br/>设备操作接口 | `Mono<DeviceOperator>`        | 获取设备操作接口.如果设备未注册或者注册已失效(缓存丢失).则回返回Mono.empty(), 可以通过Mono.switchIfEmpty(Mono)来处理设备不存在的情况.如: registry .getDevice(deviceId) .switchIfEmpty(Mono.error(()->new DeviceNotFoundException(....))) |
| checkDeviceState(`Flux<? extends Collection<String>\>` id) <br/>Params:<br/> id – ID <br/>Returns:<br/> 设备状态信息流 | `Flux<DeviceStateInfo>`       | 批量检查设备真实状态,建议使用DeviceOperator.checkState()     |
| getProduct(String productId) <br/>Params: <br/>productId – 产ID <br/>Returns:<br/> 产品操作接口 | `Mono<DeviceProductOperator>` | 获取设备产品操作,请勿缓存返回值,注册中心已经实现本地缓存.    |
| getProduct(String productId, String version) <br/>Params:<br/> productId – 产品ID <br/>version – 版本号<br/> Returns: <br/>对应版本的产品 | `Mono<DeviceProductOperator>` | 获取指定版本的产品,在注册产品时,指定了产品版本ProductInfo.getVersion()的信息,可以通过此方法获取 |
| register(DeviceInfo deviceInfo) <br/>Params:<br/> deviceInfo – 设备基础信息 <br/>Returns:<br/> 设备操作接口 | ` Mono<DeviceOperator>`       | 注册设备,并返回设备操作接口,请勿缓存返回值,注册中心已经实现本地缓存 |
| register(ProductInfo productInfo) <br/>Params: <br/>productInfo – 产品(型号)信息 <br/>Returns: <br/>注册结果 | `Mono<DeviceProductOperator`> | 注册产品(型号)信息                                           |
| unregisterDevice(String deviceId) <br/>Params: <br/>deviceId – 设备ID <br/>Returns: <br/>void | `Mono<Void>`                  | 注销设备,注销后将无法通过getDevice(String)获取到设备信息, 此操作将触发org.jetlinks.core.ProtocolSupport.onDeviceUnRegister(DeviceOperator) |
| unregisterProduct(String productId)<br/> Params:<br/> productId – 产品ID <br/>Returns:<br/> void | `Mono<Void>`                  | 注销产品,注销后将无法通过getProduct(String) 获取到产品信息 此操作只会注销未设置版本的产品. 此操作将触发org.jetlinks.core.ProtocolSupport.onProductUnRegister(DeviceProductOperator） |
| unregisterProduct(String productId, String version) <br/>Params:<br/> productId – 产品ID <br/>version – 版本号 | `Mono<Void>`                  | 注销指定版本的产品,注销后将无法通过getProduct(String, String) 获取到产品信息 此操作将触发org.jetlinks.core.ProtocolSupport.onProductUnRegister(DeviceProductOperator) |

<br>



**DeviceProductOperator**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>DeviceProductOperator：设备产品型号操作</li>
  <li>ThingTemplate 的子接口</li>
  <li>DefaultDeviceProductOperator 是它的默认实现类</li>
</div>


| 核心方法      | 返回值类型              | 描述                 |
| ------------- | ----------------------- | -------------------- |
| getProtocol() | `Mono<ProtocolSupport>` | 获取支持协议         |
| getDevices()  | `Flux<DeviceOperator>`  | 获取产品下的所有设备 |

<br>



<a id='DeviceOperator' style='text-decoration:none;cursor:default'>DeviceOperator</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>DeviceOperator：设备操作接口,用于发送指令到设备messageSender()以及获取配置等相关信息</li>
  <li>继承Thing接口</li>
  <li>DefaultDeviceOperator是它的默认实现类</li>
</div>


| 核心方法                                                     | 返回值类型                     | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------ | ------------------------------------------------------------ |
| getDeviceId()                                                | String                         | 设备ID                                                       |
| getConnectionServerId()                                      | ` Mono<String>`                | 当前设备连接所在服务器ID，如果设备未上线 DeviceState.online，则返回null |
| getSessionId()                                               | `Mono<String>`                 | 当前设备连接会话ID                                           |
| getAddress()                                                 | ` Mono<String>`                | 获取设备地址,通常是ip地址.                                   |
| setAddress(String address) <br/>Params: <br/> address – 地址 | `Mono<Void>`                   | 设置设备地址                                                 |
| putState(byte state)  <br/>Params:  <br/>state – 状态        | `Mono<Boolean>`                | 设置状态                                                     |
| getState()                                                   | `Mono<Byte>`                   | 获取设备当前缓存的状态,此状态可能与实际的状态不一致          |
| checkState()                                                 | `Mono<Byte>`                   | 检查设备的真实状态,此操作将检查设备真实的状态. 如果设备协议中指定了ProtocolSupport.getStateChecker(),则将调用指定的状态检查器进行检查. |
| getOnlineTime()                                              | `Mono<Long>`                   | 上线时间                                                     |
| getOfflineTime()                                             | `Mono<Long>`                   | 离线时间                                                     |
| online(String serverId, String sessionId)  <br/>Params: <br/> serverId – 设备所在服务ID <br/> sessionId – 会话ID | `Mono<Boolean>`                | 设备上线,通常不需要手动调用                                  |
| isOnline()                                                   | `Mono<Boolean>`                | 是否在线                                                     |
| offline()                                                    | `Mono<Boolean>`                | 设置设备离线                                                 |
| authenticate(AuthenticationRequest request) <br/>Params: <br/>request – 授权请求 | `Mono<AuthenticationResponse>` | 进行授权                                                     |
| getProtocol()                                                | `Mono<ProtocolSupport>`        | 获取此设备使用的协议支持                                     |
| messageSender()                                              | DeviceMessageSender            | 消息发送器, 用于发送消息给设备                               |
| getProduct()                                                 | `Mono<DeviceProductOperator>`  | 获取设备对应的产品操作接口                                   |

