
# 最佳实践

## 常见名词(功能)说明

**产品**: 对某一型设备的分类,通常是已经存在的某一个设备型号.

**设备**: 具体的某一个设备.

**网络组件**: 用于管理各种网络服务(MQTT,TCP等),动态配置,启停. 只负责接收,发送报文,不负责任何处理逻辑。

**协议**: 用于自定义消息解析规则,用于认证、将设备发送给平台报文解析为平台统一的报文，以及处理平台下发给设备的指令。

**设备网关**: 负责平台侧统一的设备接入,使用网络组件处理对应的请求以及报文,使用配置的协议解析为平台统一的设备消息(`DeviceMessage`),然后推送到事件总线。

**事件总线**: 基于topic和事件驱动,负责进程内的数据转发.(设备告警,规则引擎处理设备数据都是使用事件总线订阅设备消息)。

## 平台统一设备消息定义

平台使用自定义的协议包将设备上报的报文解析为平台统一的消息,来进行统一管理。

平台统一消息基本于物模型中的定义相同,主要由`属性(property)`,`功能(function)`,`事件(event)`组成.

### 消息组成

消息主要由`deviceId`,`messageId`,`headers`,`timestamp`组成.

`deviceId`为设备的唯一标识,`messageId`为消息的唯一标识,`headers`为消息头,通常用于对自定义消息处理的行为,如是否异步消息,
是否分片消息等.

常用的[Headers](https://github.com/jetlinks/jetlinks-core/blob/master/src/main/java/org/jetlinks/core/message/Headers.java):

1. **async** 是否异步,boolean类型.
2. **timeout** 指定超时时间. 毫秒.
3. **frag_msg_id** 分片主消息ID,为下发消息的`messageId`
4. **frag_num** 分片总数
5. **frag_part** 当前分片索引
6. **frag_last** 是否为最后一个分片,当无法确定分片数量的时候,可以将分片设置到足够大,最后一个分片设置:`frag_last=true`来完成返回.
7. **keepOnline** 与`DeviceOnlineMessage`配合使用,在TCP短链接,保持设备一直在线状态,连接断开不会设置设备离线.
8. **keepOnlineTimeoutSeconds** 指定在线超时时间,在短链接时,如果超过此间隔没有收到消息则认为设备离线.
9. **ignoreStorage** 不存储此消息数据,如: 读写属性回复默认也会记录到属性时序数据库中,设置为true后,将不记录.(1.9版本后支持)
10. **ignoreLog** 不记录此消息到日志,如: 设置为true,将不记录此消息的日志.
11. **mergeLatest** 是否合并最新属性数据,设置此消息头后,将会把最新的消息合并到消息体里(需要[开启最新数据存储](#记录设备最新数据到数据库)).
    
::: tip
messageId通常由平台自动生成,如果设备不支持消息id,可在自定义协议中通过Map的方式来做映射,将设备返回的消息与平台的messageId进行绑定.
:::

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
    Map<String,Object> headers;
    String deviceId; 
    String messageId;
    long timestamp; //时间戳(毫秒)
    List<String> properties;//可读取多个属性
}

ReadPropertyMessageReply{
    Map<String,Object> headers;
    String deviceId;
    String messageId;
    long timestamp; //时间戳(毫秒)
    boolean success;
    Map<String,Object> properties;//属性键值对
}
```

```java
WritePropertyMessage{
    Map<String,Object> headers;
    String deviceId; 
    String messageId;
    long timestamp; //时间戳(毫秒)
    Map<String,Object> properties;
}

WritePropertyMessageReply{
    Map<String,Object> headers;
    String deviceId;
    String messageId;
    long timestamp; //时间戳(毫秒)
    boolean success;
    Map<String,Object> properties; //回复被修改的属性最新值
}
```

```java
ReportPropertyMessage{
    Map<String,Object> headers;
    String deviceId;
    String messageId; //可为空
    long timestamp; //时间戳(毫秒)
    Map<String,Object> properties;
}
```

### 功能相关消息

调用设备功能到消息(`FunctionInvokeMessage`)由平台发往设备,对应到返回消息`FunctionInvokeMessageReply`.

消息定义:

```java
FunctionInvokeMessage{
    Map<String,Object> headers;
    String functionId;//功能标识,在元数据中定义.
    String deviceId;
    String messageId;
    long timestamp; //时间戳(毫秒)
    List<FunctionParameter> inputs;//输入参数
}

FunctionParameter{
    String name;
    Object value;
}

FunctionInvokeMessageReply{
    Map<String,Object> headers;
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
    Map<String,Object> headers;
    String event; //事件标识,在元数据中定义
    Object data;  //与元数据中定义的类型一致,如果是对象类型,请转为java.util.HashMap,禁止使用自定义类型.
    long timestamp; //时间戳(毫秒)
}
```

### 其他消息

1. `DeviceOnlineMessage` 设备上线消息,通常用于网关代理的子设备的上线操作.
2. `DeviceOfflineMessage` 设备离线消息,通常用于网关代理的子设备的下线操作.
3. `ChildDeviceMessage` 子设备消息,通常用于网关代理的子设备的消息.
4. `ChildDeviceMessageReply` 子设备消息回复,用于平台向网关代理的子设备发送消息后设备回复给平台的结果.
5. `UpdateTagMessage`更新设备标签.
6. `DerivedMetadataMessage` 更新设备独立物模型.
7. `DeviceRegisterMessage` 设备注册消息,通过设置消息头`message.addHeader("deviceName","设备名称");`和
   `message.addHeader("productId","产品ID")`可实现设备自动注册.
8. 如果配置了状态自管理,在检查子设备状态时,会发送指令`ChildDeviceMessage<DeviceStateCheckMessage>`,
   网关需要返回`ChildDeviceMessageReply<DeviceStateCheckMessageReply>`.

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

父子设备消息处理[请看这里](../best-practices/device-gateway-connection.md)

### 设备消息对应事件总线topic

协议包将设备上报后的报文解析为平台统一的设备消息后,会将消息转换为对应的topic
并发送到事件总线,可以通过从事件总线订阅消息来处理这些消息。

所有设备消息的`topic`的前缀均为: `/device/{productId}/{deviceId}`.

如:产品`product-1`下的设备`device-1`上线消息: `/device/product-1/device-1/online`.

可通过通配符订阅所有设备的指定消息,如:`/device/*/*/online`,或者订阅所有消息:`/device/**`.

::: tip
1. 此topic和mqtt的topic没有任何关系,仅仅作为系统内部通知的方式
2. 使用通配符订阅可能将收到大量的消息,请保证消息的处理速度,否则会影响系统消息吞吐量.
:::


| topic                                              | 类型                           | 说明                                      |
| -------------------------------------------------- | ------------------------------ | ----------------------------------------- |
| /online                                            | DeviceOnlineMessage            | 设备上线                                  |
| /offline                                           | DeviceOfflineMessage           | 设备离线                                  |
| /message/event/{eventId}                           | DeviceEventMessage             | 设备事件                                  |
| /message/property/report                           | ReportPropertyMessage          | 设备上报属性                              |
| /message/send/property/read                        | ReadPropertyMessage            | 平台下发读取消息指令                      |
| /message/send/property/write                       | WritePropertyMessage           | 平台下发修改消息指令                      |
| /message/property/read/reply                       | ReadPropertyMessageReply       | 读取属性回复                              |
| /message/property/write/reply                      | WritePropertyMessageReply      | 修改属性回复                              |
| /message/send/function                             | FunctionInvokeMessage          | 平台下发功能调用                          |
| /message/function/reply                            | FunctionInvokeMessageReply     | 调用功能回复                              |
| /register                                          | DeviceRegisterMessage          | 设备注册,通常与子设备消息配合使用         |
| /unregister                                        | DeviceUnRegisterMessage        | 设备注销,同上                             |
| /message/children/{childrenDeviceId}/{topic}       | ChildDeviceMessage             | 子设备消息,{topic}为子设备消息对应的topic |
| /message/children/reply/{childrenDeviceId}/{topic} | ChildDeviceMessage             | 子设备回复消息,同上                       |
| /message/direct                                    | DirectDeviceMessage            | 透传消息                                  |
| /message/tags/update                               | UpdateTagMessage               | 更新标签消息 since 1.5                    |
| /firmware/pull                                     | RequestFirmwareMessage         | 拉取固件请求 (设备->平台)                 |
| /firmware/pull/reply                               | RequestFirmwareMessageReply    | 拉取固件请求回复 (平台->设备)             |
| /firmware/report                                   | ReportFirmwareMessage          | 上报固件信息                              |
| /firmware/progress                                 | UpgradeFirmwareProgressMessage | 上报更新固件进度                          |
| /firmware/push                                     | UpgradeFirmwareMessage         | 推送固件更新                              |
| /firmware/push/reply                               | UpgradeFirmwareMessageReply    | 固件更新回复                              |
| /message/log                                       | DeviceLogMessage               | 设备日志                                  |
| /message/tags/update                               | UpdateTagMessage               | 更新标签                                  |
| /metadata/derived                                  | DerivedMetadataMessage         | 更新物模型                                |


::: warning 注意
列表中的topic已省略前缀`/device/{productId}/{deviceId}`,使用时请加上.

在1.6版本后,支持订阅设备分组和租户下的设备消息了.

* 设备分组topic前缀: `/device-group/{groupId}`
* 租户topic前缀: `/tenant/{tenantId}`
* 成员topic前缀: `/user/{userId}`
  
例如: `/device-group/{groupId}/device/{productId}/{deviceId}/**`
:::

## 设备接入流程

![flow](./device-flow.svg)

## 设备接入最佳实践

设备接入的核心是协议包,无论是直连设备,或者是云云对接,理论上都可以在自定义协议包里进行处理。

### 物模型定义
在接入一个设备时,首先根据设备以及设备接入文档(报文说明),
将设备物模型的属性,功能以及事件设计好。

通常情况下: 

对于设备固有不变的信息,建议使用`设备标签`进行管理.

**属性**用于定义一些指标数据,如:`电压`,`温度`等.
属性都应该是简单的数据类型,如:`int`,`float`,`string`等,避免使用结构体等复杂类型.

**功能**用于定义设备具有的一些可执行动作,例如: `消音`,`关灯`,`云台控制`.根据情况设计好输入参数和输出参数.

**事件**用于定义设备在特定条件时,发生的动作,如:`火警`,`检测到人脸`,通常为结构体类型,用于保存比较复杂的数据.

::: tip 注意 
在设计物模型时,尽量屏蔽掉非必要参数,让物模型简单化,通用化.
:::

### 协议包开发

建议使用策略模式来定义功能码，以及不同功能码对应的解析规则。如: 使用枚举来定义功能码.

避免进行`array copy`,应该使用偏移量直接处理报文.

常见场景算法实践:

1. 使用枚举来处理不同类型的数据

伪代码如下:

```java
@AllArgsConstructor
@Getter
public enum UpstreamCommand {
    //注册命令
    register((byte) 0x00, RegisterMessage::decode),
    //数据命令
    data((byte) 0x02, DataMessage::decode),
  
    byte code;

    BiFunction<byte[], Integer, ? extends MyDeviceMessage> decoder;

    public static UpstreamCommand of(byte code) {
        for (UpstreamCommand value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new UnsupportedOperationException("不支持的命令:0x" + Hex.encodeHexString(new byte[]{data}));
    }
    
    //调用此方法解码数据
    public static MyDeviceMessage decode(byte[] body) {
        //业务流水号
        int msgId = BytesUtils.beToInt(body, 0, 2); //2字节小端转int
       
        //命令
        UpstreamCommand command = of(body[4]);
 
        return command.getDecoder().apply(body, 5);
    }
}

```

2. 使用二进制位来表示状态,0表示正常,1表示异常:

使用枚举定义二进制位表示的含义,使用位运算来判断对应数据是哪一位

```java
@AllArgsConstructor
@Getter
public enum DataType{
    OK(-9999,"正常"),
    Bit0(0,"测试"),
    Bit1(1,"火警"),
    Bit2(2,"故障")
    //....
    ;
    private int bit;
    private String text;

    public static DataType of(long value) {
    if (value == 0) {
        return OK;
    }
    for (DataType type : values()) {
        if ((value & (1 << type.bit)) != 0) {
            //数据位支持多选的话,装到集合里即可
            return partState;
        }
    }
    throw new UnsupportedOperationException("不支持的状态:" + value);
 }
}

```

::: tip 注意
应该将对报文处理的类封装为独立的类，然后在开发过程中，使用单元测试验证处理是否正确。
避免直接在`DeviceMessageCodec`里编写处理逻辑。
:::

## 存储策略选择

从1.5.0开始,企业版支持在产品中配置数据存储策略,
不同的策略使用不同的数据存储方式来保存设备数据.

### 默认-行式存储

这是系统默认情况下使用的存储方案,使用elasticsearch存储设备数据.
每一个属性值都保存为一条索引记录.典型应用场景: 设备每次只会上报一部分属性,
以及支持读取部分属性数据的时候.

**优点**: 灵活,几乎满足任意场景下的属性数据存储.

**缺点**: 设备属性个数较多时,数据量指数增长,可能性能较低.

::: warning 警告
1. 在确定好存储方案后,尽量不要跨类型进行修改,如将: 行式存储修改为列式存储,可能会导致数据结构错乱.
2. 在创建物模型时,请根据存储策略判断好是否支持此类型以及ID格式
:::

索引结构:

1. 属性索引模版: `properties_{productId}_template`

mappings:
```js
{
 "properties" : {
          "geoValue" : { // 地理位置值,物模型配置的类型为地理位置时,此字段有值
            "type" : "geo_point"
          },
          "productId" : { // 产品ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "objectValue" : { // 结构体值,物模型配置的类型为结构体时,此字段有值
            "type" : "nested"
          },
          "type" : { // 物模型中配置的属性类型
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timeValue" : { //时间类型的值
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "deviceId" : { //设备ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //平台记录数据的时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "property" : { //属性ID,与物模型属性ID一致
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "numberValue" : { //物模型中属性类型为数字相关类型时,此字段有值
            "type" : "double"
          },
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "value" : { //通用值,所有类型都转为字符串
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        }
}
```

2. 事件索引模版: `event_{productId}_{eventId}_template`

mappings:
```js
{
   "properties" : {
          "productId" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //创建时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
        
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "value" : {   //如果物模型中类型不是结构体(对象类型),则会有此字段并且类型与物模型类型相匹配
            "ignore_above" : 512,
            "type" : "keyword"
          },
          //如果物模型中类型是结构体(对象类型),会根据结构体配置的字段添加到索引中.


          "deviceId" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        } 
}
```

3. 设备日志索引模版: `device_log_{productId}_template`

mappings:
```js
{
    "properties" : {
          "productId" : { //产品ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //创建时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "type" : { //日志类型
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "deviceId" : { //设备ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "content" : { //日志内容
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        }
}
```

### 默认-列式存储

使用elasticsearch存储设备数据.
一个属性作为一列,一条属性消息作为一条索引记录进行存储,适合设备每次都上报所有的属性值的场景.

**优点**: 在属性个数较多,并且设备每次都会上报全部属性时,性能更高

**缺点**: 设备必须上报全部属性.

::: tip 说明
默认情况下:读取属性,修改属性指令,都必须返回全部属性数据.
如果无法返回全部数据,可以在消息中设置头`partialProperties`,如: `message.addHeader("partialProperties",true)`,
用来标记是返回的部分数据,平台在保存数据前,将获取之前保存的其他属性然后一起保存.(使用此功能将降低系统的吞吐量)
:::

索引结构:

1. 属性索引模版: `properties_{productId}_template`

mappings:

```js
{
 "properties" : {
          "productId" : { // 产品ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "deviceId" : { //设备ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //平台记录数据的时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          //根据物模型中配置的属性,一个属性对应一列
          //....

          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        }
}
```

2. 事件和日志与`默认-行式存储`一致

### InfluxDB-行式存储

与`默认-行式存储`行为一致,使用`influxdb`进行数据存储.

measurement说明:

1. 属性: `properties_{productId}`
2. 事件: `event_{productId}_{eventId}`
3. 日志: `device_log_{productId}`

### InfluxDB-列式存储

与`默认-列式存储`行为一致,使用`influxdb`进行数据存储

measurement说明:

1. 属性: `properties_{productId}`
2. 事件: `event_{productId}_{eventId}`
3. 日志: `device_log_{productId}`

### TDEngine-列式存储

与`默认-列式存储`行为一致,使用`TDEngine`进行数据存储

超级表说明:

1. 属性: `properties_{productId}`
2. 事件: `event_{productId}_{eventId}`
3. 日志: `device_log_{productId}`

### ClickHouse-行式存储

与`默认-行式存储`行为一致,使用`ClickHouse`进行数据存储.

::: tip 说明
ClickHouse不支持大量并发查询,请根据实际情况选择.
:::

### Cassandra-行式存储

与`默认-行式存储`行为一致,使用`Cassandra`进行数据存储.

分区说明:

1. 设备属性: (deviceId,property,partition)
2. 设备事件: (deviceId,partition)
3. 设备日志: (deviceId,partition)
   
partition规则:

通过`jetlinks.device.storage.cassandra.partition-interval=MONTHS`进行配置,
支持:`MINUTES`,`HOURS`,`DAYS`,`WEEKS`,`MONTHS`,`YEARS`,`FOREVER`.

::: tip 注意
1. 在查询条件没有设置时间的时候，默认只会查询最近2个分区周期内的数据。
前端在查询时，建议默认指定时间范围，并且时间间隔不能与配置的分区周期相差太多，否则会影响查询性能.
2. Cassandra仅支持使用滚动分页,跳转分页深度过大会降低查询速度.执行count的性能也较低,建议查询时,使用[滚动分页](./../interface-guide/query-param.md#滚动分页).
:::

### 自定义存储策略

在后台代码中实现`DeviceDataStoragePolicy`接口,然后注入到`Spring`即可。

### 记录设备最新数据到数据库

`1.5.0企业版本`中增加了记录最新设备数据到数据库中，以便进行更丰富的统计查询等操作.

通过配置文件开启:
```yml
jetlinks:
  device:
     storage:
         enable-last-data-in-db: true #是否将设备最新到数据存储到数据库
```

::: tip 注意
开启后,设备的属性,事件属性的最新值将记录到表`dev_lsg_{productId}`中. 主键`id`为设备ID.
开启此功能可能降低系统的吞吐量.
:::

#### 物模型与数据库表字段说明

1. 属性id即字段
2. 当事件类型为结构体类型时, 字段为:`{eventId}_{结构体属性ID}`,否则为:`{eventId}_value`
3. 在发布产品时,会自动创建或者修改表结构.

#### 数据类型说明

1. 数字类型统一为`number(32,物模型中配置的精度)`类型
2. 时间类型为`timestamp`
3. 结构体(object)和array为`Clob`
4. 设置了字符长度`expands.maxLength`大于`2048`时,为`Clob`
5. 其他为`varchar`
   
::: warning 提示
请勿频繁修改物模型类型,可能导致数据格式错误.
:::

#### 根据最新的设备数据查询设备数量等操作

在任何设备相关等动态查询条件中.使用自定义条件`dev-latest`来进行查询.例如:
```js
{
"terms":[
   {
    "column":"id$dev-latest$demo-device" 
    ,"value":"temp1 > 10"  //查询表达式
   }
 ]
}
```

`id$dev-latest$demo-device`说明:
**id**: 表示使用查询主表的id作为设备id
**dev-latest**: 表示使用自定义条件`dev-latest`,此处为固定值
**demo-device**: 产品ID

`temp1 > 10`说明:

`temp1`最新值大于10. 条件支持`=`,`>`,`<`,`>=`,`<=`,`in`,`btw`,`isnull`等通用条件,支持嵌套查询.如:
`temp1 > 10 and (temp2 >30 or state = 1)`

::: tip 说明
所有参数和查询条件都经过了处理,没有SQL注入问题,请放心使用.
:::
