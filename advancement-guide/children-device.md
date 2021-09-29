# 接入网关设备,并通过网关接入子设备

场景: 设备的接入是由设备侧的网关(下称父设备)代理的. 一个父设备管理着多个子设备.

在自定义消息协议里,将收到的`设备消息(ChildDeviceMessage)`转换为对应`子设备消息`即可(通过`DeviceMessage.deviceId`).

一个典型的流程:

1. 自定义消息协议编解码: `ChildDeviceMessage`则为平台发往子设备的消息.
2. 父设备通过MQTT接入平台.
3. 父设备上报子设备数据,消息协议需要解码为`ChildDeviceMessage`或者`ChildDeviceMessageReply`. `ChildDeviceMessage.deviceId`为
   父设备ID,`ChildDeviceMessage.message.deviceId`为子设备ID.
4. 如果平台需要发送消息到子设备,那么必须先对子设备进行上线: 消息协议解码为`ChildDeviceMessage.message=DeviceOnlineMessage`.
5. 通过API直接向子设备发送消息.平台将自动根据设备关联信息转换对应的消息.
6. 消息协议将收到`ChildDeviceMessage`,根据消息类型转换为对应对设备消息.

::: tip 注意
在[jetlinks-community](https://github.com/jetlinks/jetlinks-community)
中的模拟器内置了子设备模拟.启动模拟器时指定脚本`demo-children-device.js`即可.
:::

以[demo-children-device.js](https://github.com/jetlinks/jetlinks-community/blob/master/simulator/scripts/demo-children-device.js)模拟器脚本为例:

1. 进入平台
2. 使用最新的[演示协议jar包](https://github.com/jetlinks/jetlinks-community/blob/master/simulator/demo-protocol-1.0.jar)更新协议管理中对应的协议.
3. 在设备实例中新建父设备: id为`gateway-1`.
4. 在设备实例中新建子设备: id为`child-device-1`
5. 激活设备
6. 启动模拟器
7. 设备状态均为在线说明成功,可以进入子设备运行状态进行相应的操作.