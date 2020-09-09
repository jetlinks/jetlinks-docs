
## 常见名词(功能)说明

产品: 对某一型设备的分类,通常是已经存在的某一个设备型号.

设备: 具体的某一个设备.

网络组件: 用于管理各种网络服务(MQTT,TCP等),动态配置,启停. 只负责接收,发送报文,不负责任何处理逻辑。对应模块`network-component`。

协议: 用于自定义消息解析规则,用于认证、将设备发送给平台报文解析为平台统一的报文，以及处理平台下发给设备的指令。

设备网关: 负责平台侧统一的设备接入,使用网络组件处理对应的请求以及报文,使用配置的协议包解析为平台统一的设备消息(`DeviceMessage`),然后推送到事件总线。

事件总线: 基于topic和事件驱动,负责进程内的数据转发.(设备告警,规则引擎处理设备数据都是使用事件总线订阅设备消息)。

## 设备接入流程

![flow](../quick-start/flow.svg)

::: tip 说明
设备接入的核心是协议包,无论是直连设备,或者是云云对接,理论上都可以在自定义协议包里进行处理。
:::

## 设备接入最佳实践

### 物模型
在接入一个设备时,首先根据设备以及设备接入文档(报文说明),
将设备物模型的属性,功能以及事件设计好,通常: 

对于设备固有不变的信息,建议使用`设备标签`进行管理.

属性用于定义设备上报的一些数据,如:`电压`,`温度`等.
属性都应该是简单的数据类型,如:`int`,`float`,`string`等,避免使用结构体等复杂类型.
（请根据设备上报属性数据的方式,选择合适的存储策略。）

事件用于定义设备在特定条件时,发生的动作,如:`火警`,`检测到人脸`,通常为结构体类型,用于保存比较复杂的数据.

功能用于定义设备具有的一些可执行动作,例如: `消音`,`关灯`,`云台控制`.根据情况设计好输入参数和输出参数.

::: tip 注意 
在设计物模型时,尽量屏蔽掉非必要参数,让物模型简单化,通用化.
:::


### 协议包开发

使用策略模式来定义功能码，以及不同功能码对应的解析规则。如: 使用枚举来定义功能码.

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