<center>协议包核心类API文档</center>
===========


### 1、概述

本文档主要提供协议包开发涉及的核心类、接口及方法进行说明。

### 2、核心类及方法

#### 入口类列表
1. <a href='#ProtocolSupportProvider'>ProtocolSupportProvider</a>
1. <a href='#ProtocolSupport'>ProtocolSupport</a>
1. <a href='#CompositeProtocolSupport'>CompositeProtocolSupport</a>
1. <a href='#ServiceContext'>ServiceContext</a>
1. <a href='#ConfigMetadata'>ConfigMetadata</a>
1. <a href='#DeviceMetadataCodec'>DeviceMetadataCodec</a>
1. <a href='#DeviceMetadata'>DeviceMetadata</a>
1. <a href='#Transport'>Transport</a>
1. <a href='#Feature'>Feature</a>
1. <a href='#Route'>Route</a>

#### **解码器类列表**
1. <a href='#DeviceMessageCodec'>DeviceMessageCodec</a>
1. <a href='#DeviceMessageEncoder'>DeviceMessageEncoder</a>
1. <a href='#DeviceMessageDecoder'>DeviceMessageDecoder</a>
1. <a href='#MessageCodecContext'>MessageCodecContext</a>
1. <a href='#MessageDecodeContext'>MessageDecodeContext</a>
1. <a href='#MessageEncodeContext'>MessageEncodeContext</a>

#### 消息类列表
1. <a href='#Message'>Message</a>
1. <a href='#DeviceMessage'>DeviceMessage</a>
1. <a href='#EncodedMessage'>EncodedMessage</a>

#### 认证类列表
1. <a href='#Authenticator'>Authenticator</a>
1. <a href='#AuthenticationRequest'>AuthenticationRequest</a>
1. <a href='#AuthenticationResponse'>AuthenticationResponse</a>

#### 设备操作相关列表
1. <a href='#DeviceRegistry'>DeviceRegistry</a>
1. <a href='#DeviceOperator'>DeviceOperator</a>

<br>

<a id='ProtocolSupportProvider'>ProtocolSupportProvider</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>整个协议包的入口，用于创建协议支持。</li>
</div>

| 核心方法                                                     | 返回值                                                       | 描述         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------ |
| create( ServiceContext context ) <br>context – 服务上下文，用于从上下文中获取其他服务(如获取spring容器中的bean)、配置等操作 | Mono`<? extends ProtocolSupport> `<br>Returns : ProtocolSupport接口的实现类(消息协议支持接口，通过实现此接口来自定义消息协议) | 创建协议支持 |

<br>

<a id='ProtocolSupport'>ProtocolSupport</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
    </p> <li>消息协议支持接口，通过实现此接口来自定义消息协议，此接口有自带的实现<a id='CompositeProtocolSupport'>CompositeProtocolSupport</a></li>
</div>


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

<a id='ServiceContext'>ServiceContext</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>服务上下文，用于从服务中获取其他服务(如获取spring容器中的bean)，配置等操作</li>
</div>

| 核心方法                                                     | 返回值                                                | 描述                                                        |
| ------------------------------------------------------------ | ----------------------------------------------------- | ----------------------------------------------------------- |
| getConfig( ConfigKey`<String> `key ) <br>key - 配置的key     | Optional`<Value>` <br/>Returns : 是否存在对应的数据值 | 根据配置的ConfigKey获取服务(如获取spring容器中的bean)或配置 |
| getConfig( String key )                                      | Optional`<Value>`                                     | 根据key获取服务(如获取spring容器中的bean)或配置             |
| getService( Class`<T>` service ) <br/>service - 服务的类类型 | `<T>` Optional`<T> `<br/>Returns : 是否存在该服务     | 根据类类型获取服务                                          |
| getService( String service )                                 | `<T>` Optional`<T>`                                   | 根据服务名获取服务                                          |
| getServices( Class`<T>` service ) <br/>service - 服务的类类型 | `<T>` List`<T>` <br/>Returns : 多个指定类类型的服务集 | 根据类类型获取多个服务                                      |
| getServices( String service )                                | `<T>` List`<T>`                                       | 根据服务名获取多个服务                                      |

<br>

<a id='DeviceMetadataCodec'>DeviceMetadataCodec</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> <li>ReadThingPropertyMessageReply：读取设备属性消息回复, 方向 : 设备->平台 在设备接收到ReadPropertyMessage消息后,使用此消息进行回复,回复后,指令发起方将收到响应结果</li>
</div>


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
  </p> <li>配置信息定义</li>
</div>



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
  </p> <li>设备物模型定义</li>
</div>


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

<a id='Message'>Message</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>Message：设备消息</li>
  <li>子接口：BroadcastMessage、ThingMessage</li>
</div>




| 核心方法                                                     | 返回值类型             | 描述                                                         |
| ------------------------------------------------------------ | ---------------------- | ------------------------------------------------------------ |
| getMessageType()                                             | MessageType            | 获取消息类型                                                 |
| getMessageId()                                               | String                 | 消息的唯一标识,用于在请求响应模式下对请求和响应进行关联. 注意: 此消息ID为全系统唯一. 但是在很多情况下,设备可能不支持此类型的消息ID, 此时需要在协议包中做好映射关系,比如使用:java.util.concurrent.ConcurrentHashMap进行消息绑定. 还可以使用工具类:org.jetlinks.core.message.codec.context.CodecContext来进行此操作. |
| getTimestamp()                                               | long                   | 毫秒时间戳                                                   |
| getHeaders()                                                 | ` Map<String, Object>` | 消息头,用于自定义一些消息行为, 默认的一些消息头请看:Headers，返回headers或者null |
| addHeader(String header, Object value) <br/> Params: <br/>header – header <br/>value – value | Message                | 添加一个header，返回this                                     |
| addHeaderIfAbsent(String header, Object value) Params: header – header Object value – | Message                | 添加header,如果header已存在则放弃，返回this                  |
| removeHeader(String header) <br/>Params: <br/>header – header | ` Message`             | 删除一个header                                               |



<br>



**MessageType**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>MessageType 枚举类</li>
</div>




| 枚举值                      | 描述                 |
| --------------------------- | -------------------- |
| REPORT\_PROPERTY            | 上报设备属性         |
| READ\_PROPERTY              | 下行读属性           |
| READ\_PROPERTY              | 下行写属性           |
| READ\_PROPERTY\_REPLY       | 上行读属性回复       |
| WRITE\_PROPERTY\_REPLY      | 上行写属性回复       |
| INVOKE\_FUNCTION            | 下行调用功能         |
| INVOKE\_FUNCTION\_REPLY     | 上行调用功能回复     |
| EVENT                       | 事件消息             |
| ~BROADCAST~                 | 广播,暂未支持        |
| ONLINE                      | 设备上线             |
| OFFLINE                     | 设备离线             |
| REGISTER                    | 注册                 |
| UN\_REGISTER                | 注销                 |
| DISCONNECT                  | 平台主动断开连接     |
| DISCONNECT\_REPLY           | 断开回复             |
| DERIVED\_METADATA           | 派生属性             |
| CHILD                       | 下行子设备消息       |
| CHILD\_REPLY                | 上行子设备消息回复   |
| READ\_FIRMWARE              | 读取设备固件信息     |
| READ\_FIRMWARE\_REPLY       | 读取设备固件信息回复 |
| REPORT\_FIRMWARE            | 上报设备固件信息     |
| REQUEST\_FIRMWARE           | 设备拉取固件信息     |
| REQUEST\_FIRMWARE\_REPLY    | 设备拉取固件信息响应 |
| UPGRADE\_FIRMWARE           | 更新设备固件         |
| UPGRADE\_FIRMWARE\_REPLY    | 更新设备固件信息回复 |
| UPGRADE\_FIRMWARE\_PROGRESS | 上报固件更新进度     |
| DIRECT                      | 透传消息             |
| UPDATE\_TAG                 | 更新标签             |
| LOG                         | 日志                 |
| ACKNOWLEDGE                 | 应答指令             |
| STATE\_CHECK                | 状态检查             |
| STATE\_CHECK\_REPLY         | 状态检查回复         |
| UNKNOWN                     | 未知消息             |



<br>



**HeaderKey**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>HeaderKey 顶层接口</li>
</div>




| 核心方法          | 返回值类型 | 描述                      |
| ----------------- | ---------- | ------------------------- |
| getKey()          | String     | 获取key值                 |
| getDefaultValue() | T          | 获取传入泛型类型得value值 |



<br>



**BroadcastMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>BroadcastMessage：广播消息</li>
  <li>DefaultBroadcastMessage 是它的默认实现类：</li>
</div>




| 核心方法                                                     | 返回值类型       | 描述                      |
| ------------------------------------------------------------ | ---------------- | ------------------------- |
| getAddress()                                                 | String           | 获取地址                  |
| getMessage()                                                 | Message          | 获取消息                  |
| address(String address) <BR/>Params: <BR/>address：地址      | BroadcastMessage | 添加地址                  |
| message(Message message) <BR/>Params:<BR/> message：消息信息 | BroadcastMessage | 设置默认实现类的message值 |
| getMessageType()                                             | MessageType      | 获取消息类型              |



<br>



<a id='DeviceMessage'>DeviceMessage</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>DeviceMessage：设备消息</li>
  <li>子接口：DeviceMessageReply</li>
</div>




| 核心方法      | 返回值类型 | 描述       |
| ------------- | ---------- | ---------- |
| getDeviceId() | String     | 获取设备ID |



<br>



**DeviceMessageReply**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>DeviceMessageReply：设备消息回复</li>
  <li>DeviceMessage, ThingMessageReply的子接口之一</li>
</div>




| 核心方法                  | 返回值类型         |            |
| ------------------------- | ------------------ | ---------- |
| deviceId(String deviceId) | DeviceMessageReply | 设置设备ID |



<br>



**ReadThingPropertyMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ReadThingPropertyMessage：读取设备属性消息，方向: 平台->设备,下发指令后,设备需要回复指令ReadPropertyMessageReply</li>
  <li>RepayableThingMessage的子接口之一</li>
</div>




| 核心方法                                                     | 返回值类型                    | 描述                                                         |
| ------------------------------------------------------------ | ----------------------------- | ------------------------------------------------------------ |
| getProperties()                                              | `List<String>`                | 要读取的属性列表,协议包可根据实际情况处理此参数, 有的设备可能不支持读取指定的属性,则直接读取全部属性返回即可 |
| addProperties(`List<String>` properties) Params: <BR/>`List<String>` properties-属性集合 | `ReadThingPropertyMessage<T>` | 添加属性列表                                                 |
| forThing(ThingType thingType, String deviceId) <br/>Params：<BR/> ThingType thingType-物类型String deviceId-设备ID | DefaultReadPropertyMessage    | 设置DefaultReadPropertyMessage类的ThingId和ThingType         |



<br>



**ThingType**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ThingType 物类型定义,通常使用枚举实现此接口</li>
</div>




| 核心方法  | 返回值类型 | 描述     |
| --------- | ---------- | -------- |
| getId()   | String     | 类型ID   |
| getName() | String     | 类型名称 |



<br>



**ReadThingPropertyMessageReply**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ReadThingPropertyMessageReply：读取设备属性消息回复, 方向: 设备->平台 在设备接收到ReadPropertyMessage消息后,使用此消息进行回复,回复后,指令发起方将收到响应结果.</li>
   <li>ThingMessageReply, PropertyMessage的子接口</li>
</div>




| 核心方法                                                     | 返回值类型                    | 描述                         |
| ------------------------------------------------------------ | ----------------------------- | ---------------------------- |
| success(`Map<String, Object>` properties) <BR/>Params: <BR/>properties – 属性值 | ReadThingPropertyMessageReply | 设置成功并设置返回属性值     |
| success(`List<ThingProperty>` properties) Params:<BR/> properties – 属性值 | ReadThingPropertyMessageReply | 设置成功并设置返回完整属性值 |



<br>



**RepayableDeviceMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>RepayableDeviceMessage：支持回复的消息</li>
   <li>DeviceMessage, RepayableThingMessage的子接口</li>
</div>



<br>



**RepayableThingMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>RepayableThingMessage：支持回复的消息</li>
   <li>ThingMessage的子接口</li>
</div>



<br>



**ThingEventMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ThingEventMessage：物事件消息</li>
   <li>ThingMessage的子接口</li>
</div>



| 核心方法                                            | 返回值类型        | 描述                       |
| --------------------------------------------------- | ----------------- | -------------------------- |
| getEvent()                                          | String            | 事件标识                   |
| getData()                                           | Object            | 事件数据，与物模型类型对应 |
| event(String event) <BR/>Params:<BR/> event – event | ThingEventMessage | 设置事件                   |
| data(Object data) <BR/>Params:<BR/> data – data     | ThingEventMessage | 设置事件数据               |



<br>



**ThingFunctionInvokeMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ThingFunctionInvokeMessage：物功能调用消息</li>
   <li>RepayableThingMessage的子接口、CommonDeviceMessage的父接口</li>
   <li>DefaultFunctionInvokeMessage、FunctionInvokeMessage 是CommonDeviceMessage的两个实现类</li>
</div>




| 核心方法                                                     | 返回值类型                 | 描述                 |
| ------------------------------------------------------------ | -------------------------- | -------------------- |
| getFunctionId()                                              | String                     | 获取functionId       |
| getInputs()                                                  | `List<FunctionParameter>`  | 获取功能参数         |
| addInput(FunctionParameter parameter) </br>Params:</br> parameter– 功能参数 | ThingFunctionInvokeMessage | 添加功能参数         |
| functionId(String id) </br>Params: </br>id – functionId      | ThingFunctionInvokeMessage | 设置functionId       |
| getInput(String name)</br> Params:</br> name– 功能参数名称   | `Optional<Object>`         | 根据名称获取功能参数 |
| getInput(int index) </br>Params: </br>index– 索引            | ` Optional<Object>`        | 根据索引获取功能参数 |



<br>



**ThingFunctionInvokeMessageReply**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ThingFunctionInvokeMessageReply：物功能调用回复,用于对物模型功能调用进行响应</li>
   <li>RepayableThingMessage的子接口、CommonDeviceMessage的父接口</li>
   <li>ThingMessageReply的子接口、CommonDeviceMessageReply 的父接口</li>
   <li>DefaultFunctionInvokeMessageReply、FunctionInvokeMessageReply 是 CommonDeviceMessageReply 的两个实现类</li>
</div>




| 核心方法                                                | 返回值类型                 | 描述                                    |
| ------------------------------------------------------- | -------------------------- | --------------------------------------- |
| getFunctionId()                                         | String                     | 获取functionId,对应物模型ID             |
| getOutput()                                             | Object                     | 功能调用响应结果,根据物模型的不同而不同 |
| output(Object output) <br/>Params: <br/>output – output | ThingFunctionInvokeMessage | 设置功能的输出值                        |



<br>



**ThingMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ThingMessage：物消息</li>
   <li>Message的子接口</li>
</div>



| 核心方法                                                     | 返回值类型   | 描述             |
| ------------------------------------------------------------ | ------------ | ---------------- |
| getThingType()                                               | String       | 物类型           |
| getThingId()                                                 | String       | 物ID             |
| messageId(String messageId) </br>Params:</br> messageId – messageId | ThingMessage | 设置messageId    |
| thingId(String thingType, String thingId)</br> Params:</br> thingType – 物类型 thingId – 物ID | ThingMessage | 设置物类型和ID   |
| timestamp(long timestamp) </br>Params:</br> timestamp – 时间戳 | ThingMessage | 设置物消息时间戳 |



<br>



**ThingMessageReply**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ThingMessageReply：物消息回复</li>
   <li>ThingMessage的子接口</li>
</div>




| 核心方法                                                     | 返回值类型        | 描述                     |
| ------------------------------------------------------------ | ----------------- | ------------------------ |
| isSuccess()                                                  | boolean           | 是否成功                 |
| getCode()                                                    | String            | 业务码,具体由设备定义    |
| getMessage()                                                 | String            | 错误消息                 |
| error(ErrorCode errorCode)                                   | ThingMessageReply | 设置失败                 |
| success()                                                    | ThingMessage      | 设置物消息时间戳         |
| success(boolean success)                                     | ThingMessageReply | 设置成功                 |
| code(@NotNull String code)                                   | ThingMessageReply | 设置业务码               |
| message(@NotNull String message)                             | ThingMessageReply | 设置消息                 |
| from(@NotNull Message message) </br>Params：</br> message-消息 | ThingMessageReply | 据另外的消息填充对应属性 |



<br>



**ThingReportPropertyMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>ThingReportPropertyMessage：上报设备属性,通常由设备定时上报,方向: 设备->平台</li>
   <li>ThingMessage, PropertyMessage 的子接口</li>
   <li>DefaultReportPropertyMessage、ReportPropertyMessage 是它的两个实现类</li>
</div>




| 核心方法                                                     | 返回值类型                 | 描述                         |
| ------------------------------------------------------------ | -------------------------- | ---------------------------- |
| success(`Map<String, Object>` properties) <br/>Params: <br/>properties – 属性值 | ThingReportPropertyMessage | 设置成功并设置返回属性值     |
| success(`List<ThingProperty>` properties) <br/>Params: <br/>properties – 属性值 | ThingReportPropertyMessage | 设置成功并设置返回完整属性值 |



<br>



**UpdateTingTagsMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>UpdateTingTagsMessage：更新物标签消息</li>
   <li>ThingMessage的子接口、CommonThingMessage 的父接口</li>
   <li>DefaultUpdateTingTagsMessage、UpdateTagMessage 是 CommonThingMessage 的两个实现类</li>
</div>




| 核心方法                         | 返回值类型            | 描述         |
| -------------------------------- | --------------------- | ------------ |
| getTags()                        | `Map<String, Object>` | 获取标签信息 |
| tags(`Map<String, Object>` tags) | UpdateTingTagsMessage | 更新标签信息 |



<br>



**WriteThingPropertyMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>WriteThingPropertyMessage：读取设备属性消息, 方向: 平台->设备 下发指令后,设备需要回复指令 ReadPropertyMessageReply</li>
   <li>RepayableThingMessage 的子接口、CommonThingMessage 的父接口</li>
   <li>DefaultWritePropertyMessage、WritePropertyMessage 是CommonThingMessage 的两个实现类</li>
</div>




| 核心方法                                                     | 返回值类型            | 描述                                                         |
| ------------------------------------------------------------ | --------------------- | ------------------------------------------------------------ |
| getProperties()                                              | `Map<String, Object>` | 要读取的属性列表,协议包可根据实际情况处理此参数, 有的设备可能不支持读取指定的属性,则直接读取全部属性返回即可 |
| addProperty(String key, Object value)<br/> Params：<br/> key-key<br/> value-value | void                  | 添加属性                                                     |



<br>



**WriteThingPropertyMessageReply**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>WriteThingPropertyMessageReply：读取设备属性消息回复, 方向: 设备->平台 在设备接收到 ReadPropertyMessage 消息后,使用此消息进行回复,回复后,指令发起方将收到响应结果</li>
   <li>ThingMessageReply, PropertyMessage的子接口、CommonThingMessageReply 的父接口</li>
   <li>DefaultWritePropertyMessageReply、WritePropertyMessageReply 是它的两个实现类</li>
</div>



| 核心方法                                                     | 返回值类型                     | 描述                         |
| ------------------------------------------------------------ | ------------------------------ | ---------------------------- |
| success(`Map<String, Object>` properties) <br/>Params:<br/> properties – 属性值 | WriteThingPropertyMessageReply | 设置成功并设置返回属性值     |
| success(`List<ThingProperty>` properties) <br/>Params:<br/> properties – 属性值 | WriteThingPropertyMessageReply | 设置成功并设置返回完整属性值 |



<br>



**DeviceMessageReply**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>DeviceMessageReply：设备消息回复</li>
   <li>RepayableDeviceMessage：支持回复的消息</li>
</div>



| 核心方法      | 返回值类型 | 描述         |
| ------------- | ---------- | ------------ |
| getDeviceId() | String     | 获取设备ID值 |



<br>



<a id='EncodedMessage'>EncodedMessage</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>EncodedMessage：已编码的消息,通常为来自设备或者发向设备的原始报文</li>
   <li>子接口：CoapMessage、RepayableDeviceMessage</li>
</div>




| 核心方法                     | 方法返回值         | 描述         |
| ---------------------------- | ------------------ | ------------ |
| getPayload()                 | ByteBuf            | 获取原始报文 |
| @Deprecated getPayloadType() | MessagePayloadType | 报文信息类型 |



<br>



**CoapMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>CoapMessage：Coap协议消息</li>
   <li>EncodedMessage的子接口</li>
</div>




| 核心方法     | 返回值类型     | 描述       |
| ------------ | -------------- | ---------- |
| getPath()    | String         | 获取path   |
| getCode()    | CoAP.Code      | 获取code   |
| getOptions() | `List<Option>` | 获取option |



<br>



**CoapResponseMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>CoapResponseMessage：Coap协议响应消息</li>
   <li>EncodedMessage的子接口</li>
  <li>DefaultCoapResponseMessage 是他的默认实现类</li>
</div>




| 核心方法  | 返回值类型        | 描述       |
| --------- | ----------------- | ---------- |
| getCode() | CoAP.ResponseCode | 获取响应码 |



<br>



**HttpRequestMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>HttpRequestMessage：Http协议请求信息</li>
   <li>EncodedMessage 的子接口</li>
  <li>SimpleHttpRequestMessage 是他的实现类</li>
</div>




| 核心方法             | 返回值类型            | 描述             |
| -------------------- | --------------------- | ---------------- |
| getPath()            | String                | 获取消息体的path |
| getUrl()             | String                | 获取消息体的Url  |
| getMethod()          | HttpMethod            | 获取请求方法     |
| getContentType()     | MediaType             | 获取请求类型     |
| getHeaders()         | `List<Header>`        | 获取请求头信息   |
| getQueryParameters() | `Map<String, String>` | 获取查询参数     |



<br>



**HttpResponseMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>HttpResponseMessage：Http响应信息</li>
   <li>EncodedMessage 的子接口</li>
  <li>SimpleHttpResponseMessage 是他的实现类</li>
</div>




| 核心方法         | 返回值类型     | 描述         |
| ---------------- | -------------- | ------------ |
| getStatus()      | int            | 获取响应状态 |
| getContentType() | MediaType      | 获取响应类型 |
| getHeaders()     | `List<Header>` | 获取响应头   |



<br>



**MqttMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>MqttMessage：Mqtt信息</li>
   <li>EncodedMessage 的子接口</li>
  <li>SimpleMqttMessage 是它的实现类</li>
</div>




| 核心方法       | 返回值类型 | 描述         |
| -------------- | ---------- | ------------ |
| getTopic()     | String     | 获取topic    |
| getClientId()  | String     | 获取客户端id |
| getMessageId() | int        | 获取信息id   |



<br>



**MqttPublishingMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>MqttPublishingMessage：Mqtt信息推送</li>
   <li>MqttMessage 的子接口</li>
  <li>ProxyMqttPublishingMessage 是它的实现类</li>
</div>




| 核心方法                                                     | 返回值类型            | 描述                                             |
| ------------------------------------------------------------ | --------------------- | ------------------------------------------------ |
| acknowledge()                                                | void                  | 在QoS1,和QoS2时,此方法可能会被调用               |
| of(MqttMessage message, Runnable doOnAcknowledge) <br/>Params: <br/> message – 原始消息 <br/> doOnAcknowledge – 应答回调 | MqttPublishingMessage | 根据另外一个MqttMessage创建MqttPublishingMessage |



<br>



**WebSocketMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>WebSocketMessage</li>
   <li>EncodedMessage 的子接口</li>
  <li>DefaultWebSocketMessage 是他的实现类</li>
</div>




| 核心方法  | 返回值类型 | 描述          |
| --------- | ---------- | ------------- |
| getType() | Type       | Websocket消息 |



<br>



**Type**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>

  </p>
    <li>类型值的枚举</li>
</div>




| 枚举值 | 描述   |
| ------ | ------ |
| TEXT   | 文本   |
| BINARY | 二进制 |
| PING   | ping   |
| PONG   | pong   |



<br>



**WebSocketSessionMessage**

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>WebSocketSessionMessage</li>
   <li>WebSocketMessage 的子接口</li>
  <li>WebSocketSessionMessageWrapper是它的实现类</li>
</div>




| 核心方法              | 返回值类型       | 描述        |
| --------------------- | ---------------- | ----------- |
| getWebSocketSession() | WebSocketSession | 获取session |



<br>



<a id='Authenticator'>Authenticator</a>

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



<a id='AuthenticationRequest'>AuthenticationRequest</a>

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



<a id='AuthenticationResponse'>AuthenticationResponse</a>

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



<a id='DeviceRegistry'>DeviceRegistry</a>

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



<a id='DeviceOperator'>DeviceOperator</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>DeviceOperator：设备操作接口,用于发送指令到设备messageSender()以及获取配置等相关信息</li>
  <li>Thing的子接口</li>
  <li>DefaultDeviceOperator 是它的默认实现类</li>
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
