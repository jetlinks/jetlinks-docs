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

<br>

<a id='ProtocolSupportProvider' style='text-decoration:none;cursor:default'>ProtocolSupportProvider</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> 
    自定义协议包的入口，用于创建协议支持。
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {

    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        ...
    }
}
```

| 核心方法                         | 参数                                                         | 返回值                                                       | 说明                                                                           |
| -------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |----------------------------------------------------------------------------------------------------------------------------|
| `create(ServiceContext context)` | context – 服务上下文，用于从上下文中获取其他服务(如获取spring容器中的bean)、配置等操作 | `Mono<? extends ProtocolSupport>`<br>`ProtocolSupport` 接口的实现类 ( 消息协议支持接口，通过实现此接口来自定义消息协议 ) | 创建协议支持                                                                       |

<br>

<a id='ProtocolSupport' style='text-decoration:none;cursor:default'>ProtocolSupport</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
   </p>
    消息协议支持接口，通过实现此接口来自定义消息协议。
</div>

| 核心方法                                                     | 参数                                                         | 返回值                                                       | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `getMessageCodec(Mono<? extends Transport> transport)`       | transport – 传输协议                                         | `Mono< ? extends DeviceMessageCodec >`<br/> Returns : 消息编解码器 | 获取设备消息编码解码器，用于将平台统一的消息对象转码为设备的消息，以及将设备发送的消息转码为平台统一的消息对象 |
| `authenticate(@Nonnull AuthenticationRequest request, @Nonnull DeviceOperator deviceOperation)` | request – 认证请求,不同的连接方式实现不同<br/><br> deviceOperation – 设备操作接口,可用于配置设备 | `Mono<AuthenticationResponse>`<br/> Returns : 认证结果       | 进行设备认证                                                 |
| `authenticate(@Nonnull AuthenticationRequest request, @Nonnull DeviceRegistry registry)` | request – 认证请求<br/> registry – 注册中心                  | `Mono<AuthenticationResponse>`                               | 对不明确的设备进行认证                                       |
| `getConfigMetadata(Transport  transport)`                    | transport – 传输协议                                         | `Mono<ConfigMetadata>` <br/>Returns : 配置信息               | 获取协议所需的配置信息定义                                   |
| `getInitConfigMetadata(Transport  transport)`                | transport – 传输协议                                         | `Mono<ConfigMetadata>`                                       | 获取协议初始化所需要的配置定义                               |
| `getDefaultMetadata(Transport transport)`                    | transport – 传输协议                                         | `Mono<DeviceMetadata>`                                       | 获取默认物模型                                               |
| `getMetadataExpandsConfig(Transport transport, DeviceMetadataType metadataType, String metadataId, String dataTypeId)` | transport – 传输协议 <br/>metadataType – 物模型类型 <br/>metadataId – 物模型标识 <br/>dataTypeId – 数据类型ID | `Mono<DeviceMetadata>`                                       | 获取物模型拓展配置定义                                       |
| `getFeatures(Transport transport)`                           | transport – 传输协议                                         | `Flux<Feature>`</br> Returns : 特性集                        | 获取协议支持的某些自定义特性                                 |
| `getRoutes(Transport transport)`                             | transport – 传输协议                                         | `Flux<Route>` </br>Returns : 路由信息                        | 获取指定协议的路由信息，比如MQTT topic，HTTP url地址         |
| `getStateChecker()`                                          |                                                              | `Mono<DeviceStateChecker>`<br>Returns : 设备状态检查器       | 获取自定义设备状态检查器,用于检查设备状态                    |
| `init(Map<String, Object> configuration)`                    | configuration - 包含配置信息的map                            | 无返回值                                                     | 初始化协议                                                   |
| `dispose()`                                                  |                                                              | 无返回值                                                     | 销毁协议                                                     |
| `onDeviceRegister(DeviceOperator operator)`                  | operator – 设备操作接口                                      | `Mono<Void>`                                                 | 当设备注册生效后调用                                         |
| `onDeviceUnRegister(DeviceOperator operator)`                | operator – 设备操作接口                                      | `Mono<Void>`                                                 | 当设备注销前调用                                             |
| `onProductRegister(DeviceProductOperator operator)`          | operator – 产品操作接口                                      | `Mono<Void>`                                                 | 当产品注册后调用                                             |
| `onProductUnRegister(DeviceProductOperator operator)`        | operator – 产品操作接口                                      | `Mono<Void>`                                                 | 当产品注销前调用                                             |
| `onProductMetadataChanged( DeviceProductOperator operator)`  | operator – 产品操作接口                                      | `Mono<Void>`                                                 | 当产品物模型变更时调用                                       |
| `onDeviceMetadataChanged(DeviceOperator operator)`           | operator – 设备操作接口                                      | `Mono<Void>`                                                 | 当设备物模型变更时调用                                       |
| `onClientConnect(Transport transport,                                    ClientConnection connection,                                    DeviceGatewayContext context)` | transport – 传输协议 <br/>connection – 客户端连接            | `Mono<Void>`                                                 | 客户端创建连接时调用，返回设备ID，表示此设备上线             |
| `onChildBind( DeviceOperator gateway, Flux<DeviceOperator>child )` | gateway – 网关 <br/>child – 子设备流                         | `Mono<Void>`                                                 | 触发手动绑定子设备到网关设备                                 |
| `onChildUnbind( DeviceOperator gateway, Flux<DeviceOperator> child )` | gateway – 网关 <br/>child – 子设备流                         | `Mono<Void>`                                                 | 触发手动解除绑定子设备到网关设备                             |

<br>



<a id='CompositeProtocolSupport'  style='text-decoration:none;cursor:default;'><b>CompositeProtocolSupport</b></a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
   </p>
    消息协议支持接口ProtocolSupport的默认实现类，可以通过创建该类对象来设置自定义协议的各项属性。
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {

    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        ...
        return Mono.just(support);
    }
}
```

| 核心方法                                                     | 参数                                                         | 返回值 | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------ | ------------------------------------------------------------ |
| `addMessageCodecSupport(Transport transport, DeviceMessageCodec codec)` | transport - 传输协议<br>codec - 设备消息编码解码器           | 无     | 添加消息编码解码器                                           |
| `addAuthenticator(Transport transport, Authenticator authenticator)` | transport - 传输协议<br>authenticator - 认证器               | 无     | 添加认证器                                                   |
| `addDefaultMetadata(Transport transport, DeviceMetadata metadata)` | transport - 传输协议<br>metadata - 物模型                    | 无     | 添加物模型                                                   |
| `setMetadataCodec(DeviceMetadataCodec metadataCodec)`        | metadataCodec - 物模型编解码器                               | 无     | 设置物模型编解码器，此处固定使用平台实现的`JetLinksDeviceMetadataCodec`进行物模型的编解码 |
| `addConfigMetadata(Transport transport, ConfigMetadata metadata)` | transport - 传输协议<br>metadata - 配置信息元数据            | 无     | 添加配置信息                                                 |
| `addRoutes(Transport transport, Collection<? extends Route> routes)` | transport - 传输协议<br>routes - 自定义对应协议的路由分组集合 | 无     | 添加对应协议的路由分组集合                                   |

<br>

<a id='DeviceMetadataCodec' style='text-decoration:none;cursor:default'>DeviceMetadataCodec</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    物模型编解码器，用于将物模型与字符串进行互相转换，协议包内添加物模型解码器支持，默认实现类：<a style='text-decoration:none;cursor:default;'>JetLinksDeviceMetadataCodec</a>
</div>


```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {


    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        ...
        //声明使用JetLinks默认物模型编解码器
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        return Mono.just(support);
    }
}

```

| 核心方法                 | 参数              | 返回值                                       | 说明                 |
| ------------------------ | ----------------- | -------------------------------------------- | -------------------- |
| `decode(String source)`  | source – 数据     | `Mono<DeviceMetadata>` <br/>Returns : 物模型 | 将数据解码为物模型   |
| `encode(DeviceMetadata)` | metadata – 物模型 | `Mono<String>`<br/>Returns : 物模型字符串    | 将物模型编码为字符串 |

<br>

<a id='ConfigMetadata' style='text-decoration:none;cursor:default'>ConfigMetadata</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> 
    配置信息定义，用于定义配置信息。
    在ProtocolSupportProvider内添加支持后，可以在产品、设备页面展示配置项。DefaultConfigMetadata是该接口的默认实现类。
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
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        ...
        //对指定通信协议添加动态配置项支持
        support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);
        return Mono.just(support);
    }
}

```

| 核心方法                        | 参数          | 返回值                                  | 说明                                                         |
| ------------------------------- | ------------- | --------------------------------------- | ------------------------------------------------------------ |
| `getProperties()`               | 无            | `List<ConfigPropertyMetadata>`          | 返回配置属性信息                                             |
| `copy( ConfigScope... scopes )` | scopes – 范围 | `ConfigMetadata` <br>Returns : 新的配置 | 复制为新的配置，并按指定的scope过滤属性，只返回符合scope的属性，`ConfigScope`为配置作用域，请使用枚举实现此接口 |

<br>

<a id='DeviceMetadata' style='text-decoration:none;cursor:default'>DeviceMetadata</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> 
    设备物模型定义
    JetLinks设备物模型定义默认实现：<a style='text-decoration:none;cursor:default;'>JetLinksDeviceMetadata</a>
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
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);
        ...
        //添加默认物模型信息
        support.addDefaultMetadata(DefaultTransport.MQTT, new JetLinksDeviceMetadata("temperature", "温度"));
        return Mono.just(support);
    }
}

```

| 核心方法          | 参数 | 返回值                   | 说明             |
| ----------------- | ---- | ------------------------ | ---------------- |
| `getProperties()` | 无   | `List<PropertyMetadata>` | 获取所有属性定义 |
| `getFunctions()`  | 无   | `List<FunctionMetadata>` | 获取所有功能定义 |
| `getEvents()`     | 无   | `List<EventMetadata>`    | 获取事件定义     |
| `getTags()`       | 无   | `List<PropertyMetadata>` | 获取标签定义     |

<br>

<a id='Transport' style='text-decoration:none;cursor:default'>Transport</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> 传输协议定义，如: TCP、MQTT等，通常使用枚举来定义
</div>

| 核心方法                        | 参数                     | 返回值                                              | 说明                                                         |
| ------------------------------- | ------------------------ | --------------------------------------------------- | ------------------------------------------------------------ |
| `isSame(Transport transport)  ` | transport – 要比较的协议 | `boolean`  <br/>Returns : 是否为同一个协议          | 比较与传入的协议是否为同一个协议                             |
| `isSame(String transportId)  `  | transportId – 协议ID     | `boolean`  <br/>Returns : 是否为同一个协议          | 使用ID进行对比，判断是否为同一个协议                         |
| `of(String id) `                | id – 协议ID              | `Transport` <br/>Returns : 协议                     | 使用指定的ID来创建协议定义                                   |
| `lookup(String id)  `           | id – 协议ID              | `Optional<Transport >`<br/>Returns : 是否存在该协议 | 通过ID查找协议定义，可通过Transports.register(Transport )来注册自定义的协议 |
| `getAll()`                      | 无                       | `List<Transport>` <br>Returns : 协议集合            | 获取全部协议定义                                             |

<br>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> Transport接口在平台有默认的实现类DefaultTransport，该类为一个枚举类，内部定义的枚举对象如下
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

<a id='Feature' style='text-decoration:none;cursor:default'>Feature</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>特性接口，一般使用枚举实现。用于定义协议或者设备支持的某些特性
</div>

| 核心方法                     | 参数                           | 返回值                                                    | 说明                                     |
| ---------------------------- | ------------------------------ | --------------------------------------------------------- | ---------------------------------------- |
| `getId()`                    | 无                             | `String` <br>Returns : 特性的id                           | 唯一标识                                 |
| `getName()`                  | 无                             | `String` <br/>Returns : 特性的名称                        | 名称                                     |
| `getType()`                  | 无                             | `String` <br/>Returns : 特性的类型                        | 特性类型，用于进行分类，比如: 协议特性等 |
| `isDeprecated()`             | 无                             | `boolean` <br/>Returns : 是否弃用                         | 是否已经被弃用                           |
| `of(String id, String name)` | id - 特性的id<br>name - 特性名 | `Feature` <br/>Returns : 根据特性的id、名称返回特定的特性 | 根据id和name获取一个特性                 |

<br>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p> Feature接口在平台有4个实现类分别为 CodecFeature，DeviceFeatures，ManagementFeature，以及 MetadataFeature
</div>




**CodecFeature**

| 枚举对象           | 说明                                                         |
| ------------------ | ------------------------------------------------------------ |
| `transparentCodec` | 标识协议支持透传消息,支持透传的协议可以在界面上动态配置解析规则 |

<br>

**DeviceFeatures**

| 枚举对象          | 说明                             |
| ----------------- | -------------------------------- |
| `supportFirmware` | 标识使用此协议的设备支持固件升级 |

<br>

**ManagementFeature**

| 枚举对象               | 说明           |
| ---------------------- | -------------- |
| `autoGenerateDeviceId` | 自动生成设备ID |

<br>

**MetadataFeature**

| 枚举对象                    | 说明                       |
| --------------------------- | -------------------------- |
| `supportDerivedMetadata`    | 设备支持派生物模型         |
| `propertyNotModifiable`     | 物模型属性不可修改         |
| `propertyTypeNotModifiable` | 物模型属性数据类型不可修改 |
| `propertyNotDeletable`      | 物模型属性不可删除         |
| `propertyNotInsertable`     | 物模型属性不可新增         |
| `functionNotInsertable`     | 物模型功能不可新增         |
| `functionNotModifiable`     | 物模型功能不可修改         |
| `functionNotDeletable`      | 物模型功能不可删除         |
| `eventNotInsertable`        | 物模型事件不可新增         |
| `eventNotModifiable`        | 物模型事件不可修改         |
| `eventNotDeletable`         | 物模型事件不可删除         |

<br>

<a id='Route' style='text-decoration:none;cursor:default'>Route</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>该接口存在两个子接口，HttpRoute 和 MqttRoute，对应接口中的内部接口Builder定义了各自不同的属性
</div>

| 核心方法           | 参数 | 返回值   | 说明     |
| ------------------ | ---- | -------- | -------- |
| `getGroup()`       | 无   | `String` | 获取分组 |
| `getAddress()`     | 无   | `String` | 获取地址 |
| `getDescription()` | 无   | `String` | 获取说明 |
| `getExample()`     | 无   | `String` | 获取示例 |

<br>



**HttpRoute.Builder**

| 属性        | 数据类型       | 说明                                                         |
| ----------- | -------------- | ------------------------------------------------------------ |
| group       | `String`       | 分组                                                         |
| method      | `HttpMethod[]` | 提交方法，如HTTP的 GET、POST方法等                           |
| contentType | `MediaType[]`  | 媒体形式，如application/json、application/x-www-form-urlencoded等 |
| address     | `String`       | 地址                                                         |
| description | `String`       | 描述                                                         |
| example     | `String`       | 示例                                                         |

| 方法        | 参数 | 返回值              | 说明                                                         |
| ----------- | ---- | ------------------- | ------------------------------------------------------------ |
| `builder()` | 无   | `HttpRoute.Builder` | 用于构建HttpRoute.Builder(内部接口，用于定义该接口独有的属性) |

<br>

**MqttRoute.Builder**

| 属性        | 数据类型  | 说明                  |
| ----------- | --------- | --------------------- |
| topic       | `String`  | 主题                  |
| upstream    | `boolean` | 是否是上行            |
| downstream  | `boolean` | 是否是下行            |
| qos         | `int`     | 消息服务质量，默认为0 |
| group       | `String`  | 分组                  |
| description | `String`  | 描述                  |
| example     | `String`  | 示例                  |

| 方法        | 参数 | 返回值              | 说明                                                         |
| ----------- | ---- | ------------------- | ------------------------------------------------------------ |
| `builder()` | 无   | `MqttRoute.Builder` | 用于构建`MqttRoute.Builder`(内部接口，用于定义该接口独有的属性) |

<br>

<a id='ServiceContext' style='text-decoration:none;cursor:default'>ServiceContext</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>服务上下文，用于从服务中获取其他服务(如获取spring容器中的bean)，配置等操作
</div>

```java
public class JetLinksProtocolSupportProvider implements ProtocolSupportProvider {

    @Override
    public Mono<CompositeProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("jetlinks.v3.0");
        support.setName("JetLinks V3.0");
        support.setDescription("JetLinks Protocol Version 3.0");
        ...
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

| 核心方法                           | 参数                   | 返回值                                                | 说明                                                        |
| ---------------------------------- | ---------------------- | ----------------------------------------------------- | ----------------------------------------------------------- |
| `getConfig(ConfigKey<String> key)` | key - 配置的key        | `Optional<Value>` <br/>Returns : 是否存在对应的数据值 | 根据配置的ConfigKey获取服务(如获取spring容器中的bean)或配置 |
| `getConfig(String key)`            | key - map的key         | `Optional<Value>`                                     | 根据key获取服务(如获取spring容器中的bean)或配置             |
| `getService(Class<T> service) `    | service - 服务的类类型 | `<T>Optional<T> `<br/>Returns : 是否存在该服务        | 根据类类型获取服务                                          |
| `getService(String service)`       | service - 服务名       | `<T>Optional<T>`                                      | 根据服务名获取服务                                          |
| `getServices(Class<T> service)`    | service - 服务的类类型 | `<T>List<T>` <br/>Returns : 多个指定类类型的服务集    | 根据类类型获取多个服务                                      |
| `getServices(String service)`      | service - 服务名       | `<T>List<T>`                                          | 根据服务名获取多个服务                                      |

<br>

<a id='DeviceMessageCodec' style='text-decoration:none;cursor:default'>DeviceMessageCodec</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>设备消息转换器，用于对不同协议的消息进行转换
</div>

| 核心方法                | 参数 | 返回值                                    | 说明               |
| ----------------------- | ---- | ----------------------------------------- | ------------------ |
| `getSupportTransport()` | 无   | `Transport`                               | 返回支持的传输协议 |
| `getDescription()`      | 无   | `Mono<? extends MessageCodecDescription>` | 获取协议描述       |

<br>

<a id='DeviceMessageEncoder' style='text-decoration:none;cursor:default'>DeviceMessageEncoder</a>



<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>设备消息编码器，用于将消息对象编码为对应消息协议的消息
</div>


```java
	 /**
	 * 消息发送方向 : 平台 -> 设备
     * @param context 消息上下文
     * @return Publisher<? extends EncodedMessage>
     */
	@Nonnull
    @Override
    public Publisher<? extends EncodedMessage> encode(@Nonnull MessageEncodeContext context) {
        Message message = context.getMessage();
        JSONObject payload = new JSONObject();
        //离线消息
        if (message instanceof DisconnectDeviceMessage) {
            return ((ToDeviceMessageContext) context)
                .disconnect()
                .then(Mono.empty());
        }
        //此处以功能调用消息为例
        if (message instanceof FunctionInvokeMessage){
            //将封装在message中下发参数的转换为map
            Map<String, Object> params = ((FunctionInvokeMessage) message)
                .getInputs()
                .stream()
                .collect(Collectors.toMap(FunctionParameter::getName, FunctionParameter::getValue));
            //根据具体的文档需求封装对应的格式
            ...
            payload.putAll(params);
        }
        ...
   
        return Mono.justOrEmpty(SimpleMqttMessage
            .builder()
            .clientId(message.getDeviceId())
            //根据具体的文档设置为对应的topic                    
            .topic("")
            .payloadType(MessagePayloadType.JSON)                    
            .payload(Unpooled.wrappedBuffer(JSONObject.toJSONBytes(payload)))
            .build());
    }
```




| 核心方法                                        | 参数                     | 返回值                                                       | 描述                                                         |
| ----------------------------------------------- | ------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `encode(@Nonnull MessageEncodeContext context)` | context – 消息编码上下文 | `Publisher<? extends EncodedMessage>` <br>Returns : 设备能读取的编码消息 | 编码，将消息进行编码，用于发送到设备端，平台在发送指令给设备时，会调用协议包中设置的此方法，将平台消息`DeviceMessage`转为设备能理解的消息`EncodedMessage` |

<br>

<a id='DeviceMessageDecoder' style='text-decoration:none;cursor:default'>DeviceMessageDecoder</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>设备消息解码器，用于将收到设备上传的消息解码为可读的消息
</div>

```java
	 /**
	 * 消息发送方向 : 设备 -> 平台
     * 根据具体文档封装对应的Message返回给平台
     * 不同的Message封装的消息内容不一致,可以参考本文档的Message部分
     * @param context 消息上下文
     * @return Flux<DeviceMessage> 
     */
	@Nonnull
    @Override
    public Flux<DeviceMessage> decode(@Nonnull MessageDecodeContext context) {
        MqttMessage message = (MqttMessage) context.getMessage();
        JSONObject payload = message.payloadAsJson();
   
        //此处以事件上报为例
        EventMessage eventMessage = new EventMessage();
        Map<String,Object> data = new HashMap<>();
        
        //message中根据具体文档设置需求的数据
        eventMessage.setDeviceId("");
        eventMessage.setMessageId("");
        eventMessage.setData(data);
        ...
        
        //TopicPayload为一个封装了topic以及byte数组的回复内容的类
        TopicPayload topicPayload = TopicPayload.builder()
            //根据具体文档构建需要回复的topic
            .topic("")
            //其中的replyMessage为具体的需要回复的Json格式数据的对应Java对象
            //例如需要回复{timestamp:1669630871452}，则需要在类中定义long timestamp
            .payload(Unpooled.wrappedBuffer(JSONObject.toJSONBytes(replyMessage)))
            .build();
        
        //根据具体文档需求确认平台需不需要回复，如果不需要回复则直接返回Mono.just(eventMessage)
        return reply(context, topicPayload)
            .thenMany(Mono.just(eventMessage));
    }

	 /**
     * 回复给设备的消息
     * @param context 会话上下文
     * @param topicPayload 封装好的回复消息体
     * @return Mono<Void>
     */
	private Mono<Void> reply(MessageCodecContext context, TopicPayload topicPayload) {
        if (context instanceof FromDeviceMessageContext) {
            return ((FromDeviceMessageContext) context)
                    .getSession()
                    .send(SimpleMqttMessage
                            .builder()
                            //需要根据具体的文档封装topic
                            .topic("")
                            .payload(topicPayload.getPayload())
                            .build())
                    .then();
        } else if (context instanceof ToDeviceMessageContext) {
            return ((ToDeviceMessageContext) context)
                    .sendToDevice(SimpleMqttMessage
                            .builder()
                            //需要根据具体的文档封装topic
                            .topic("")
                            .payload(topicPayload.getPayload())
                            .build())
                    .then();
        }
        return Mono.empty();
    }
```




| 核心方法                                         | 参数                     | 返回值                                                       | 说明                                                         |
| ------------------------------------------------ | ------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `decode(@Nonnull MessageDecodeContext  context)` | context – 消息解码上下文 | `Publisher<? extends Message>` <br>Returns : 平台可识别的消息，根据设备上传的报文通过协议包进行解码之后会转换成平台消息 | 在服务器收到设备或者网络组件中发来的消息时，会调用协议包中的此方法来进行解码， 将数据`EncodedMessage`转为平台的统一消息`DeviceMessage` |

<br>

<a id='MessageCodecContext' style='text-decoration:none;cursor:default'>MessageCodecContext</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>消息编解码上下文
</div>

| 核心方法                      | 参数              | 返回值                                       | 说明                                                         |
| ----------------------------- | ----------------- | -------------------------------------------- | ------------------------------------------------------------ |
| `getDevice()`                 | 无                | `DeviceOperator` <br>Returns : 设备操作      | 获取当前上下文中到设备操作接口，在tcp，http等场景下，此接口可能返回`null` |
| `getDeviceAsync()`            | 无                | `Mono<DeviceOperator>`                       | 获取指定设备的操作接口，如果设备不存在，则为`Mono.empty()`，可以通过`Mono.switchIfEmpty(Mono)`进行处理 |
| `getDevice(String deviceId) ` | deviceId – 设备ID | `Mono<DeviceOperator>`                       | 获取指定设备的操作接口，如果设备不存在,则为`Mono.empty()`，可以通过`Mono.switchIfEmpty(Mono)`进行处理. |
| `getConfiguration()`          | 无                | `Map<String, Object> `<br>Returns : 配置信息 | 预留功能，获取配置信息                                       |

<br>

<a id='MessageDecodeContext' style='text-decoration:none;cursor:default'>MessageDecodeContext</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>设备上报原始消息接口
</div>


| 核心方法       | 参数 | 返回值                                  | 说明                                                         |
| -------------- | ---- | --------------------------------------- | ------------------------------------------------------------ |
| `getMessage()` | 无   | `EncodedMessage`<br> Returns : 原始消息 | 获取设备上报的原始消息，根据通信协议的不同,消息类型也不同， 在使用时可能需要转换为对应的消息类型 |

<br>

<a id='MessageEncodeContext'>MessageEncodeContext</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>消息编码上下文，用于平台向设备发送指令并使用协议包进行编码时，可以从上下文中获取一些参数。 通常此接口可强制转换为ToDeviceMessageContext
</div>


| 核心方法                                                     | 参数                                          | 返回值                                     | 说明                                                         |
| ------------------------------------------------------------ | --------------------------------------------- | ------------------------------------------ | ------------------------------------------------------------ |
| `getMessage()`                                               | 无                                            | `Message` <br>Returns : 消息               | 获取平台下发的给设备的消息指令，根据物模型中定义对应不同的消息类型。 在使用时，需要判断对应的类型进行不同的处理 |
| `reply(@Nonnull Publisher<? extends DeviceMessage> replyMessage)` | replyMessage – 消息流                         | `Mono<Void>`                               | 直接回复消息给平台.在类似通过http接入时，下发指令可能是一个同步操作，则可以通过此方法直接回复平台. |
| `reply(@Nonnull DeviceMessage ... messages) `                | messages – 多个设备消息                       | `Mono<Void>`                               | 重载方法                                                     |
| `reply(@Nonnull Collection<? extends DeviceMessage>messages)` | messages – 设备消息的集合                     | `Mono<Void>`                               | 重载方法                                                     |
| `mutate(Message anotherMessage, DeviceOperator another)`     | Message – 设备消息 <br/>device – 设备操作接口 | `MessageEncodeContext`<br>Returns : 上下文 | 使用新的消息和设备，转换为新上下文                           |

<br>

<a id='Message' style='text-decoration:none;cursor:default'>Message</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  Message：设备消息，DeviceMessage是它的主要子接口之一
</div>



MessageType

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  MessageType 枚举类
</div>


| 枚举值                      | 描述                 |
| --------------------------- | -------------------- |
| `REPORT_PROPERTY`           | 上报设备属性         |
| ` READ_PROPERTY`            | 下行读属性           |
| `READ_PROPERTY `            | 下行写属性           |
| `READ_PROPERTY_REPLY`       | 上行读属性回复       |
| `WRITE_PROPERTY_REPLY`      | 上行写属性回复       |
| `INVOKE_FUNCTION `          | 下行调用功能         |
| `INVOKE_FUNCTION_REPLY`     | 上行调用功能回复     |
| `EVENT`                     | 事件消息             |
| `BROADCAST`                 | 广播,暂未支持        |
| `ONLINE`                    | 设备上线             |
| `OFFLINE`                   | 设备离线             |
| `REGISTER`                  | 注册                 |
| `UN_REGISTER`               | 注销                 |
| `DISCONNECT`                | 平台主动断开连接     |
| `DISCONNECT_REPLY`          | 断开回复             |
| `DERIVED_METADATA`          | 派生属性             |
| `CHILD`                     | 下行子设备消息       |
| `CHILD_REPLY `              | 上行子设备消息回复   |
| `READ_FIRMWARE `            | 读取设备固件信息     |
| `READ_FIRMWARE_REPLY   `    | 读取设备固件信息回复 |
| `REPORT_FIRMWARE `          | 上报设备固件信息     |
| `REQUEST_FIRMWARE `         | 设备拉取固件信息     |
| `REQUEST_FIRMWARE_REPLY`    | 设备拉取固件信息响应 |
| `UPGRADE_FIRMWARE`          | 更新设备固件         |
| `UPGRADE_FIRMWARE_REPLY`    | 更新设备固件信息回复 |
| `UPGRADE_FIRMWARE_PROGRESS` | 上报固件更新进度     |
| `DIRECT`                    | 透传消息             |
| `UPDATE_TAG`                | 更新标签             |
| `LOG`                       | 日志                 |
| `ACKNOWLEDGE`               | 应答指令             |
| `STATE_CHECK`               | 状态检查             |
| `STATE_CHECK_REPLY`         | 状态检查回复         |
| `UNKNOWN`                   | 未知消息             |

<br>

<a id='DeviceMessage' style='text-decoration:none;cursor:default'>DeviceMessage</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceMessage：设备消息
</div>


核心方法说明：

| 核心方法         | 参数                                                      | 返回值类型            | 描述           |
| ---------------- | --------------------------------------------------------- | --------------------- | -------------- |
| `getDeviceId()`  | 无                                                        | `String`              | 获取设备ID     |
| `getTimestamp()` | 无                                                        | `Long`                | 获取时间戳     |
| `getHeaders()`   | 无                                                        | `Map<String, Object>` | 获取请求头信息 |
| `addHeader()`    | String header-header的key<br/> Object value-header的value | `Message`             | 添加一个header |
| `removeHeader()` | String header-header的key                                 | `Message`             | 移除header     |


<a id='CommonDeviceMessage' style='text-decoration:none;cursor:default'>CommonDeviceMessage</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  CommonDeviceMessage：通用设备消息，实现deviceMessage接口
</div>


核心参数说明：

| 参数      | 类型     | 描述                           | 是否必填 |
| --------- | -------- | ------------------------------ | -------- |
| messageId | `String` | 消息ID，是设备与平台的通信凭证 | 是       |
| deviceId  | `String` | 设备ID                         | 是       |
| timestamp | `long`   | 毫秒时间戳                     | 否       |

<br/>



```java
CommonDeviceMessage的主要子类说明：

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
    14、UpdateTagMessage：更新物模型中标签信息
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

| 参数               | 类型      | 描述     | 是否必填 |
| ------------------ | --------- | -------- | -------- |
| childDeviceId      | `String`  | 子设备ID | 是       |
| childDeviceMessage | `Message` | 消息数据 | 否       |

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

| 参数               | 类型      | 描述     | 是否必填 |
| ------------------ | --------- | -------- | -------- |
| childDeviceId      | `String`  | 子设备ID | 是       |
| childDeviceMessage | `Message` | 消息数据 | 否       |

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

| 参数     | 类型      | 描述           | 是否必填 |
| -------- | --------- | -------------- | -------- |
| metadata | `String`  | 物模型json字符 | 否       |
| all      | `boolean` | 是否是全量数据 | 否       |

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

核心方法说明：

| 核心方法           | 参数 | 返回值类型    | 描述                                      |
| ------------------ | ---- | ------------- | ----------------------------------------- |
| `getMessageType()` | 无   | `MessageType` | 返回MessageType枚举类中上线消息的实例对象 |

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

| 核心方法           | 参数 | 返回值类型    | 描述                                      |
| ------------------ | ---- | ------------- | ----------------------------------------- |
| `getMessageType()` | 无   | `MessageType` | 返回MessageType枚举类中离线消息的实例对象 |

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

| 参数    | 类型     | 描述         | 是否必填 |
| ------- | -------- | ------------ | -------- |
| payload | `byte[]` | 存放上报数据 | 否       |

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

| 参数  | 类型     | 描述           | 是否必填 |
| ----- | -------- | -------------- | -------- |
| event | `String` | 事件标识       | 是       |
| data  | `Object` | 存放上报的数据 | 否       |

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

| 参数       | 类型                      | 描述           | 是否必填 |
| ---------- | ------------------------- | -------------- | -------- |
| functionId | `String`                  | 事件标识       | 是       |
| inputs     | `List<FunctionParameter>` | 存放下发的数据 | 否       |

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

| 参数       | 类型     | 描述           | 是否必填 |
| ---------- | -------- | -------------- | -------- |
| functionId | `String` | 事件标识       | 是       |
| output     | `Object` | 存放上报的数据 | 否       |

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

| 参数       | 类型            | 描述   | 是否必填 |
| ---------- | --------------- | ------ | -------- |
| properties | ` List<String>` | 属性值 | 是       |

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

| 参数       | 类型                  | 描述   | 是否必填 |
| ---------- | --------------------- | ------ | -------- |
| properties | `Map<String, Object>` | 属性值 | 是       |

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

| 参数       | 类型                  | 描述                                                         | 是否必填 |
| ---------- | --------------------- | ------------------------------------------------------------ | -------- |
| properties | `Map<String, Object>` | 属性值信息,key为物模型中的属性ID,value为物模型对应的类型值.注意: value如果是结构体(对象类型),请勿传入在协议包中自定义的对象,应该转为Map传入. | 否       |

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

| 参数       | 类型                  | 描述                                                         | 是否必填 |
| ---------- | --------------------- | ------------------------------------------------------------ | -------- |
| properties | `Map<String, Object>` | 属性值信息,key为物模型中的属性ID,value为物模型对应的类型值.注意: value如果是结构体(对象类型),请勿传入在协议包中自定义的对象,应该转为Map传入. | 是       |

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

| 参数 | 类型                  | 描述             | 是否必填 |
| ---- | --------------------- | ---------------- | -------- |
| tags | `Map<String, Object>` | 物模型中标签信息 | 是       |

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

| 参数       | 类型                  | 描述                                                         | 是否必填 |
| ---------- | --------------------- | ------------------------------------------------------------ | -------- |
| properties | `Map<String, Object>` | 要修改的属性，key 为物模型中对应的属性ID,value为属性值，properties = new `LinkedHashMap<String, Object>()` | 是       |

<br/>

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

| 参数       | 类型                  | 描述                                                         | 是否必填 |
| ---------- | --------------------- | ------------------------------------------------------------ | -------- |
| properties | `Map<String, Object>` | 回复的属性,key为物模型中的属性ID,value为物模型对应的类型值.注意: value如果是结构体(对象类型),请勿传入在协议包中自定义的对象,应该转为Map传入. | 是       |

<br>

<a id='Authenticator' style='text-decoration:none;cursor:default'>Authenticator</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>Authenticator：认证器,用于设备连接的时候进行认证</li>
</div>


核心方法说明：

| 核心方法         | 参数                                                         | 返回值类型                     | 描述                                                         |
| ---------------- | ------------------------------------------------------------ | ------------------------------ | ------------------------------------------------------------ |
| `authenticate()` | @Nonnull AuthenticationRequest request-请求认证信息<br/>, @Nonnull DeviceOperator device-设备操作接口 | `Mono<AuthenticationResponse>` | 对指定对设备进行认证<br/>@param<br/> request 认证请求<br/> device  设备<br/> @return <br/>认证结果 |
| `authenticate()` | @Nonnull AuthenticationRequest request-请求认证信息<br/> @Nonnull DeviceRegistry registry-设备注册信息 | `Mono<AuthenticationResponse>` | 在网络连接建立的时候,可能无法获取设备的标识(如:http,websocket等),则会调用此方法来进行认证.注意: 认证通过后,需要设置设备ID.{@link AuthenticationResponse#success(String)}<br/>@param<br/> request  认证请求<br/> registry 设备注册中心 <br/> @return <br/>认证结果 |

<br>


<a id='AuthenticationRequest' style='text-decoration:none;cursor:default'>AuthenticationRequest</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>AuthenticationRequest：封装请求认证信息</li>
  <li>MqttAuthenticationRequest 是它的默认实现类</li>
</div>


核心方法说明：

| 核心方法         | 参数 | 返回类型    | 描述               |
| ---------------- | ---- | ----------- | ------------------ |
| `getTransport()` | 无   | `Transport` | 获取支持的协议信息 |

```java
//MqttAuthenticationRequest 是AuthenticationRequest的默认实现类
public class MqttAuthenticationRequest implements AuthenticationRequest {
    private String clientId;//客户端id

    private String username;//认证的用户名

    private String password;//认证的密码

    private Transport transport;//认证协议
}
```

<br>

Transport

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>Transport：传输协议定义,如: TCP,MQTT等,通常使用枚举来定义</li>
</div>


核心方法说明：

| 核心方法                     | 参数                           | 返回值类型            | 描述                                 |
| ---------------------------- | ------------------------------ | --------------------- | ------------------------------------ |
| `getId()`                    | 无                             | `String`              | 唯一标识ID                           |
| `getName()`                  | 无                             | `String`              | 名称，默认和ID一致                   |
| `getDescription()`           | 无                             | ` String`             | 描述，默认是null                     |
| `isSame()`                   | Transport transport-传入的协议 | `boolean`             | 比较与指定等协议是否为同一种协议     |
| `isSame(String transportId)` | 传入的协议ID                   | `boolean`             | 使用ID进行对比，判断是否为同一个协议 |
| `of() `                      | String id-协议id               | `Transport`           | 使用指定的ID来创建协议定义           |
| `lookup()`                   | String id-协议id               | `Optional<Transport>` | 通过ID查找协议定义                   |
| `getAll()`                   | 无                             | `List<Transport>`     | 获取全部协议定义                     |

<a id='AuthenticationResponse' style='text-decoration:none;cursor:default'>AuthenticationResponse</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>AuthenticationResponse：封装响应信息</li>
</div>


核心参数描述

| 参数     | 类型    | 描述     | 是否必填 |
| -------- | ------- | -------- | -------- |
| deviceId | String  | 设备ID   | 是       |
| message  | String  | 响应信息 | 否       |
| code     | int     | 响应码   | 否       |
| success  | Boolean | 响应标识 | 否       |

核心方法说明：

| 核心方法    | 参数                                                    | 返回值类型               | 描述             |
| ----------- | ------------------------------------------------------- | ------------------------ | ---------------- |
| `success()` | String deviceId-设备ID                                  | `AuthenticationResponse` | 封装了成功的响应 |
| `error()`   | int code-失败响应代码, <br/>String message-失败响应信息 | `AuthenticationResponse` | 封装了失败的响应 |

```java
//成功的响应
public static AuthenticationResponse success(String deviceId) {
    AuthenticationResponse response = new AuthenticationResponse();
    response.success = true;
    response.code = 200;
    response.message = "授权通过";
    response.deviceId = deviceId;
    return response;
}
//失败的响应
public static AuthenticationResponse error(int code, String message) {
    AuthenticationResponse response = new AuthenticationResponse();
    response.success = false;
    response.code = code;
    response.message = message;
    return response;
}
```


<a id='DeviceRegistry' style='text-decoration:none;cursor:default'>DeviceRegistry</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceRegistry：设备注册中心,用于统一管理设备以及产品的基本信息,缓存,进行设备指令下发等操作.
\* 例如获取设备以及设备的配置缓存信息:
</div>


核心方法说明：

| 核心方法              | 参数                                 | 返回值类型                    | 描述                     |
| --------------------- | ------------------------------------ | ----------------------------- | ------------------------ |
| `getDevice()`         | String deviceId-设备ID               | `Mono<DeviceOperator>`        | 根据的设备ID获取设备信息 |
| `getProduct()`        | String productId-产品ID              | `Mono<DeviceProductOperator>` | 根据的产品ID获取产品信息 |
| `register()`          | DeviceInfo deviceInfo-设备详细信息   | `Mono<DeviceOperator>`        | 注册设备                 |
| `register()`          | ProductInfo productInfo-产品详细信息 | `Mono<DeviceProductOperator>` | 注册产品                 |
| ` unregisterDevice()` | String deviceId-设备ID               | `Mono<Void>`                  | 注销设备                 |
| `unregisterProduct()` | String productId-产品ID              | `Mono<Void>`                  | 注销产品                 |



DeviceProductOperator

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceProductOperator：设备产品型号操作
</div>


核心方法说明：

| 核心方法            | 参数                       | 返回值类型              | 描述                   |
| ------------------- | -------------------------- | ----------------------- | ---------------------- |
| ` getId()`          | 无                         | `String`                | 获取产品ID             |
| `getMetadata()`     | 无                         | `Mono<DeviceMetadata>`  | 设备产品物模型信息     |
| ` updateMetadata()` | String metadata-元数据信息 | `Mono<Boolean>`         | 更新设备型号元数据信息 |
| ` getProtocol()`    | 无                         | `Mono<ProtocolSupport>` | 获取协议支持           |
| ` getDevices() `    | 无                         | `Flux<DeviceOperator>`  | 获取产品下的所有设备   |



<a id='DeviceOperator' style='text-decoration:none;cursor:default'>DeviceOperator</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  DeviceOperator： 设备操作接口,用于发送指令到设备{@link    DeviceOperator#messageSender()}以及获取配置等相关信息
</div>


核心方法说明：

| 核心方法            | 参数                                                         | 返回值类型                     | 描述                                                         |
| ------------------- | ------------------------------------------------------------ | ------------------------------ | ------------------------------------------------------------ |
| `getDeviceId()`     | 无                                                           | `Mono<String>`                 | 获取设备ID                                                   |
| `getSessionId()`    | 无                                                           | `Mono<String>`                 | 当前设备连接会话ID                                           |
| `getAddress()`      | 无                                                           | `Mono<String>`                 | 获取设备地址,通常是ip地址                                    |
| `setAddress()`      | String address-地址                                          | `Mono<Void>`                   | 设置设备地址                                                 |
| `online()`          | String serverId-服务ID<br/> String sessionId-会话ID<br/> String address-地址 | `Mono<Boolean>`                | 设备上线,通常不需要手动调用                                  |
| ` online()`         | String serverId-服务ID<br/> String address-地址<br/> long onlineTime-在线时间 | `Mono<Boolean>`                | 设备上线,通常不需要手动调用，onlineTime大于0有效             |
| `getSelfConfig()`   | String key-自定义配置的key                                   | `Mono<Value>`                  | 获取设备自身的配置,如果配置不存在则返回{@link Mono#empty()}  |
| `getSelfConfigs()`  | `Collection<String>` keys-自定义配置的key的集合              | `Mono<Values>`                 | 获取设备自身的多个配置,不会返回{@link Mono#empty()},通过从{@link Values}中获取对应的值 |
| `offline() `        | 无                                                           | `Mono<Boolean>`                | 设置设备离线                                                 |
| `authenticate() `   | AuthenticationRequest request-请求认证信息                   | `Mono<AuthenticationResponse>` | 进行授权                                                     |
| `getMetadata()`     | 无                                                           | `Mono<DeviceMetadata>`         | 获取设备的元数据                                             |
| `getProtocol()`     | 无                                                           | `Mono<ProtocolSupport>`        | 获取此设备使用的协议支持                                     |
| `messageSender()`   | 无                                                           | `DeviceMessageSender`          | 消息发送器, 用于发送消息给设备                               |
| ` updateMetadata()` | String metadata-物模型                                       | `Mono<Boolean>`                | 设置当前设备的独立物模型,如果没有设置,这使用产品的物模型配置 |
| `resetMetadata()`   | 无                                                           | `Mono<Void>`                   | 重置当前设备的独立物模型                                     |
| `getProduct()`      | 无                                                           | `Mono<DeviceProductOperator>`  | 获取设备对应的产品操作接口                                   |

<br>

