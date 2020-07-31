# 设备自注册

原理: 自定义协议包将消息解析为`DeviceRegisterMessage`,并设置header:`productId`(必选),`deviceName`(必选),`configuration`(可选)。
平台将自动添加设备信息到设备实例中。如果是注册子设备,则解析为 `ChildDeviceMessage<DeviceRegisterMessage>`即可

::: tip

header中的`configuration`为设备的自定义配置信息，会保持到`DeviceInstanceEntity.configuration`中，类型为`Map<String,Object>`，
在后续的操作中,可通过`DeviceOperator.getSelfConfig`来获取这些配置信息。

:::
