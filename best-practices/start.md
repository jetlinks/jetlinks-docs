
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

消息主要由`deviceId`,`messageId`,`headers`组成.

`deviceId`为设备的唯一标识,`messageId`为消息的唯一标识,`headers`为消息头,通常用于对自定义消息处理的行为,如是否异步消息,
是否分片消息等.

常用的(Headers)[https://github.com/jetlinks/jetlinks-core/blob/master/src/main/java/org/jetlinks/core/message/Headers.java]:

1. async 是否异步,boolean类型.
2. timeout 指定超时时间. 毫秒.
3. frag_msg_id 分片主消息ID,为下发消息的`messageId`
4. frag_num 分片总数
5. frag_part 当前分片索引
6. frag_last 是否为最后一个分片,当无法确定分片数量的时候,可以将分片设置到足够大,最后一个分片设置:`frag_last=true`来完成返回.
7. keepOnline 与`DeviceOnlineMessage`配合使用,在TCP短链接,保持设备一直在线状态,连接断开不会设置设备离线.
8. keepOnlineTimeoutSeconds 指定在线超时时间,在短链接时,如果超过此间隔没有收到消息则认为设备离线.
   
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
4. `ChildrenDeviceMessageReply` 子设备消息回复,用于平台向网关代理的子设备发送消息后设备回复给平台的结果.

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


|  topic   | 类型  | 说明 |
|  ----  | ----  | ----|
| /online  | DeviceOnlineMessage | 设备上线   |
| /offline  | DeviceOfflineMessage |  设备离线  |
| /message/event/{eventId}  | DeviceEventMessage |  设备事件  |
| /message/property/report  | ReportPropertyMessage |  设备上报属性  |
| /message/property/read/reply  | ReadPropertyMessageReply |  读取属性回复  |
| /message/property/write/reply  | WritePropertyMessageReply |  修改属性回复  |
| /message/function/reply  | FunctionInvokeMessageReply |  调用功能回复  |
| /register  | DeviceRegisterMessage |  设备注册,通常与子设备消息配合使用  |
| /unregister  | DeviceUnRegisterMessage |  设备注销,同上  |
| /message/children/{childrenDeviceId}/{topic}  | ChildDeviceMessage |  子设备消息,{topic}为子设备消息对应的topic  |
| /message/children/reply/{childrenDeviceId}/{topic}  | ChildDeviceMessage |  子设备回复消息,同上  |
 
::: warning 注意
列表中的topic已省略前缀`/device/{productId}/{deviceId}`,使用时请加上.
:::

## 设备接入流程

![flow](../quick-start/flow.svg)

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
