# 从消息网关中订阅设备消息

所有设备消息均由消息网关进行路由转发.
可以在编程式或者规则引擎中订阅通过消息网关订阅设备的消息,如:

```java

//订阅所有设备上线消息
@Subscribe("/device/*/online")
public Mono<Void> handleDeviceOnline(DeviceOnlineMessage message){
    //处理消息
}

```

## Topics

所有设备消息的`topic`的前缀均为: `/device/{deviceId}`.
如:设备`device-1`上线消息: `/device/device-1/online`.
可通过通配符订阅所有设备的指定消息,如:`/device/*/online`,
或者订阅所有消息:`/device/**`.

::: tip
使用通配符订阅可能将收到大量的消息,请保证消息的处理速度,否则会影响系统消息吞吐量.
:::

## Topic列表

::: warning
列表中的topic已省略前缀`/device/{deviceId}`,使用时请加上.
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
| /message/property/report  | ReportPropertyMessage |  设备上报属性  |
| /register  | DeviceRegisterMessage |  设备注册,通常与子设备消息配合使用  |
| /unregister  | DeviceUnRegisterMessage |  设备注销,同上  |
| /message/children/{topic}  | ChildDeviceMessage |  子设备消息,{topic}为子设备消息对应的topic  |
| /message/children/reply/{topic}  | ChildDeviceMessage |  子设备回复消息,同上  |
