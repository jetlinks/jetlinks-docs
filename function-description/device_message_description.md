
# 平台统一设备消息定义

平台使用自定义的协议包将设备上报的报文解析为平台统一的消息来进行统一管理。


## 消息定义

平台统一消息基本于物模型中的定义相同，主要由`属性(property)`，`功能(function)`，`事件(event)`组成。
### 消息组成

消息主要由`deviceId`,`messageId`,`headers`,`timestamp`组成.

`deviceId`为设备的唯一标识,`messageId`为消息的唯一标识,`headers`为消息头,通常用于对自定义消息处理的行为,如是否异步消息,
是否分片消息等.

headers定义在`org.jetlinks.core.message.Headers`。常用的headers如下：

<table class='table'>
        <thead>
            <tr>
              <td>名称</td>
              <td>类型</td>
              <td>描述</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>async</td>
            <td>Boolean</td>
            <td>是否异步</td>
          </tr>
          <tr>
            <td>timeout</td>
            <td>Long</td>
            <td>超时时间，单位为毫秒</td>
          </tr>
          <tr>
            <td>frag_msg_id</td>
            <td>String</td>
            <td>

分片主消息ID，为平台下发消息时的消息ID（`messageId`）

</td>
          </tr>
          <tr>
            <td>frag_num</td>
            <td>Integer</td>
            <td>分片总数</td>
          </tr>
          <tr>
            <td>frag_num</td>
            <td>Integer</td>
            <td>分片总数</td>
          </tr>
          <tr>
            <td>frag_part</td>
            <td>Integer</td>
            <td>当前分片索引</td>
          </tr>
          <tr>
            <td>frag_last</td>
            <td>Integer</td>
            <td>

是否为最后一个分片。当无法确定分片数量的时候，可以将分片设置到足够大，最后一个分片设置`frag_last=true`来完成返回。

</td>
          </tr>
          <tr>
            <td>keepOnline</td>
            <td>Boolean</td>
            <td>

与`DeviceOnlineMessage`配合使用,在TCP短链接,保持设备一直在线状态,连接断开不会设置设备离线.

</td>
          </tr>
          <tr>
            <td>keepOnlineTimeoutSeconds</td>
            <td>Integer</td>
            <td>指定在线超时时间。在短链接时，如果超过此间隔没有收到消息则认为设备离线。</td>
          </tr>
          <tr>
            <td>ignoreStorage</td>
            <td>Boolean</td>
            <td>不存储此消息数据。如：读写属性回复默认也会记录到属性时序数据库中，设置为true后，将不记录。（1.9版本后支持）</td>
          </tr>
          <tr>
            <td>ignoreLog</td>
            <td>Boolean</td>
            <td>不记录此消息的日志。如：设置为true，将不记录此消息的日志。</td>
          </tr>
          <tr>
            <td>mergeLatest</td>
            <td>Boolean</td>
            <td>

是否合并最新属性数据。设置此消息头后，将会把最新的消息合并到消息体里（需要<a href='#记录设备最新数据到数据库'>开启最新数据存储</a>。

</td>
          </tr>
        </tbody>
   </table>



<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

messageId通常由平台自动生成,如果设备不支持消息id,可在自定义协议中通过Map的方式来做映射,将设备返回的消息与平台的messageId进行绑定。  

分片消息指，设备将一个请求的结果分片返回。通常用于处理大消息。

</div>

### 属性相关消息

1. `获取设备属性(ReadPropertyMessage)`对应设备回复的消息`ReadPropertyMessageReply`.
2. `修改设备属性(WritePropertyMessage)`对应设备回复的消息`WritePropertyMessageReply`.
3. `设备上报属性(ReportPropertyMessage)` 由设备上报.

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

设备回复的消息是通过`messageId`进行绑定，`messageId`应该注意要全局唯一。如果设备无法做到，可以在编解码时通过添加前缀等方式实现。

</div>

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

调用设备功能的消息（`FunctionInvokeMessage`）由平台发往设备，对应的返回消息为`FunctionInvokeMessageReply`。

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

事件消息`EventMessage`由设备端发往平台。

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


<table class='table'>
        <thead>
            <tr>
              <td>消息类</td>
              <td>名称</td>
              <td>描述</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>DeviceOnlineMessage</td>
            <td>设备上线消息</td>
            <td>通常用于网关代理的子设备的上线操作</td>
          </tr>
          <tr>
            <td>DeviceOfflineMessage</td>
            <td>设备离线消息</td>
            <td>通常用于网关代理的子设备的下线操作</td>
          </tr>
          <tr>
            <td>ChildDeviceMessage</td>
            <td>子设备消息</td>
            <td>通常用于网关代理的子设备的消息</td>
          </tr>
          <tr>
            <td>ChildDeviceMessageReply</td>
            <td>子设备消息回复</td>
            <td>用于平台向网关代理的子设备发送消息后设备回复给平台的结果</td>
          </tr>
          <tr>
            <td>UpdateTagMessage</td>
            <td>更新设备标签</td>
            <td></td>
          </tr>
          <tr>
            <td>DerivedMetadataMessage</td>
            <td>更新设备独立物模型</td>
            <td></td>
          </tr>
          <tr>
            <td>DeviceRegisterMessage</td>
            <td>设备注册消息</td>
            <td>

通过设置消息头`message.addHeader("deviceName","设备名称");`和
`message.addHeader("productId","产品ID")`可实现设备自动注册

</td>
          </tr>
          <tr>
            <td>DeviceStateCheckMessage</td>
            <td>检查子设备状态</td>
            <td>

如果配置了状态自管理,在检查子设备状态时,会发送指令`ChildDeviceMessage<DeviceStateCheckMessage>`,
网关需要返回`ChildDeviceMessageReply<DeviceStateCheckMessageReply>`.

</td>
          </tr>
          <tr>
            <td>UpgradeFirmwareMessage</td>
            <td>更新设备固件消息</td>
            <td>

配置了`远程升级`的时，平台将根据`升级任务`的设置向设备发送`UpgradeFirmwareMessage`。消息包含升级固件需要的信息。设备需要返回`UpgradeFirmwareMessageReply`

</td>
          </tr>
          <tr>
            <td>UpgradeFirmwareProgressMessage</td>
            <td>上报固件更新进度消息</td>
            <td>

设备开始`远程升级`后，可以上报固件更新进度。平台根据上报消息判断`升级任务`是否完成

</td>
          </tr>
        </tbody>
      </table>

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
        ChildDeviceMessage{
            String deviceId;
            long timestamp;
            String childDeviceId;
            Message childDeviceMessage; //子设备消息
        }
        ChildDeviceMessageReply{
            String deviceId;
            String messageId;
            String childDeviceId;
            Message childDeviceMessage;
        }
        UpdateTagMessage{
            String deviceId;
            long timestamp;
            Map<String, Object> tags;
        }
        DerivedMetadataMessage{
            String deviceId;
            long timestamp;
            String metadata; //元数据
            boolean all; //是否是全量数据
        }
        DeviceRegisterMessage{
            String deviceId;
            long timestamp;
        }
        UpgradeFirmwareMessage{
            String deviceId;
            long timestamp;
            String url; //固件下载地址
            String version; //固件版本
            Map<String, Object> parameters; //其他参数
            String sign; //签名
            String signMethod; //签名方式,md5,sha256
            String firmwareId; //固件ID
            long size; //固件大小
        }
        UpgradeFirmwareMessageReply{
            String deviceId;
            String messageId;
            boolean success;
        }
        UpgradeFirmwareProgressMessage{
            String deviceId;
            int progress; //进度0-100
            boolean complete; //是否已完成
            String version; //升级中的固件版本
            boolean success; //是否成功
            String errorReason; //错误原因
            String firmwareId; //固件ID
        }
```

父子设备消息处理<a href='/Device_access/Create_gateways_and_sub_devices3.3.html'>请看这里</a>

## 设备消息对应事件总线topic

协议包将设备上报后的报文解析为平台统一的设备消息后,会将消息转换为对应的topic
并发送到事件总线,可以通过从事件总线订阅消息来处理这些消息。

所有设备消息的`topic`的前缀均为: `/device/{productId}/{deviceId}`.

如:产品`product-1`下的设备`device-1`上线消息: `/device/product-1/device-1/online`.

可通过通配符订阅所有设备的指定消息,如:`/device/*/*/online`,或者订阅所有消息:`/device/**`.

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

1. 此topic和mqtt的topic没有任何关系,仅仅作为系统内部通知的方式
2. 使用通配符订阅可能将收到大量的消息,请保证消息的处理速度,否则会影响系统消息吞吐量.

</div>

### 总线topic

<table class='table'>
        <thead>
            <tr>
              <td>topic</td>
              <td>消息类型</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>/online</td>
            <td>DeviceOnlineMessage</td>
            <td>设备上线</td>
          </tr>
          <tr>
            <td>/offline</td>
            <td>DeviceOfflineMessage</td>
            <td>设备离线</td>
          </tr>
          <tr>
            <td>/message/event/{eventId}</td>
            <td>EventMessage</td>
            <td>设备事件</td>
          </tr>
          <tr>
            <td>/message/property/report</td>
            <td>ReportPropertyMessage</td>
            <td>设备上报属性</td>
          </tr>
          <tr>
            <td>/message/send/property/read</td>
            <td>ReadPropertyMessage</td>
            <td>平台下发读取消息指令</td>
          </tr>
          <tr>
            <td>/message/send/property/write</td>
            <td>WritePropertyMessage</td>
            <td>平台下发修改消息指令</td>
          </tr>
          <tr>
            <td>/message/property/read/reply</td>
            <td>ReadPropertyMessageReply</td>
            <td>读取属性回复</td>
          </tr>
          <tr>
            <td>/message/property/write/reply</td>
            <td>WritePropertyMessageReply</td>
            <td>修改属性回复</td>
          </tr>
          <tr>
            <td>/message/send/function</td>
            <td>FunctionInvokeMessage</td>
            <td>平台下发功能调用</td>
          </tr>
          <tr>
            <td>/message/function/reply</td>
            <td>FunctionInvokeMessageReply</td>
            <td>调用功能回复</td>
          </tr>
          <tr>
            <td>/register</td>
            <td>DeviceRegisterMessage</td>
            <td>设备注册，通常与子设备消息配合使用</td>
          </tr>
          <tr>
            <td>/unregister</td>
            <td>DeviceUnRegisterMessage</td>
            <td>设备注销，同上</td>
          </tr>
          <tr>
            <td>/message/children/{childrenDeviceId}/{topic}</td>
            <td>ChildDeviceMessage</td>
            <td>子设备消息，{topic}为子设备消息对应的topic</td>
          </tr>
          <tr>
            <td>/message/children/reply/{childrenDeviceId}/{topic}</td>
            <td>ChildDeviceMessageReply</td>
            <td>子设备回复消息，同上</td>
          </tr>
          <tr>
            <td>/message/direct</td>
            <td>DirectDeviceMessage</td>
            <td>透传消息</td>
          </tr>
          <tr>
            <td>/message/tags/update</td>
            <td>UpdateTagMessage</td>
            <td>更新标签消息</td>
          </tr>
          <tr>
            <td>/firmware/pull</td>
            <td>RequestFirmwareMessage</td>
            <td>拉取固件请求 (设备->平台)</td>
          </tr>
          <tr>
            <td>/firmware/pull/reply</td>
            <td>RequestFirmwareMessageReply</td>
            <td>拉取固件请求回复 (平台->设备)</td>
          </tr>
          <tr>
            <td>/firmware/report</td>
            <td>ReportFirmwareMessage</td>
            <td>上报固件信息</td>
          </tr>
          <tr>
            <td>/firmware/progresst</td>
            <td>UpgradeFirmwareProgressMessage</td>
            <td>上报更新固件进度</td>
          </tr>
          <tr>
            <td>/firmware/push</td>
            <td>UpgradeFirmwareMessage</td>
            <td>推送固件更新</td>
          </tr>
          <tr>
            <td>/firmware/push/reply</td>
            <td>UpgradeFirmwareMessageReply</td>
            <td>固件更新回复</td>
          </tr>
          <tr>
            <td>/message/log</td>
            <td>DeviceLogMessage</td>
            <td>设备日志</td>
          </tr>
          <tr>
            <td>/metadata/derived</td>
            <td>DerivedMetadataMessage</td>
            <td>更新物模型</td>
          </tr>
        </tbody>
      </table>


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

列表中的topic已省略前缀`/device/{productId}/{deviceId}`，使用时请加上。

在1.6版本后，支持订阅创建者分组下的设备消息了。

* 创建者topic前缀: `/user/{userId}`

例如: `/user/{userId}/device/{productId}/{deviceId}/**`

</div>

## 设备接入流程

![flow](function-description/img/device-flow.svg)

## 设备接入最佳实践

设备接入的核心是协议包。无论是直连设备，或者是与其他平台对接，理论上都可以在自定义协议包里进行处理。

### 物模型定义
在接入一个设备时，首先根据设备以及设备接入文档(报文说明)，
将设备物模型的属性、功能以及事件设计好。

通常情况下:

对于设备固有不变的信息，建议使用`设备标签`进行管理。

**属性**用于定义一些指标数据，如：`电压`，`温度`等。
属性都应该是简单的数据类型，如：`int`，`float`，`string`等，避免使用结构体等复杂类型。

**功能**用于定义设备具有的一些可执行动作，如：`消音`，`关灯`，`云台控制`等。根据情况设计好输入参数和输出参数。

**事件**用于定义设备在特定条件时，发生的动作，如：`火警`，`检测到人脸`，通常为结构体类型，用于保存比较复杂的数据。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

在设计物模型时，尽量屏蔽掉非必要参数，让物模型简单化、通用化。

</div>

### 协议包开发

建议使用策略模式来定义功能码，以及不同功能码对应的解析规则。如：使用枚举来定义功能码。

避免进行`array copy`，应该使用偏移量直接处理报文。

常见场景算法实践：

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

2. 使用二进制位来表示状态，0表示正常，1表示异常：

使用枚举定义二进制位表示的含义，使用位运算来判断对应数据是哪一位

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

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

应该将对报文处理的类封装为独立的类，然后在开发过程中，使用单元测试验证处理是否正确。
避免直接在`DeviceMessageCodec`里编写处理逻辑。

</div>

### 协议包特性配置
协议包提供了特性的配置接口，用于激活特定的功能。例如远程升级、数据解析等。  

特性定义统一实现了`org.jetlinks.core.metadata.Feature`接口。  

例如：添加远程升级支持
```java
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.addFeature(DeviceFeatures.supportFirmware);
```
