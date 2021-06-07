# 平台内部核心类及接口说明

平台内部主要通过事件驱动实现解耦，本文列举核心的类以及接口，具体实现细节以及逻辑请查看对应类的代码。

::: tip
可以利用Idea `double shift`来搜索对应的类和接口.
:::

## 设备操作底层类,接口

1. `DeviceRegistry`: 设备注册中心,用于统一管理设备信息以及提供设备操作接口.
2. `DeviceOperator`: 设备操作接口,通过`DeviceRegister.getDevice(deviceId)`获取,用于对设备进行相关操作,如获取配置,发送消息等.
3. `DeviceProductOperator`: 产品操作接口,通过`DeviceProductOperator.getProduct(productId)`获取.
4. `EventBus`: 事件总线,通过事件总线去订阅设备数据来实现解耦.(也可以用过`@Subscribe()`注解订阅).
5. `DeviceGateway` : 设备接入网关接口,利用网络组件来接入设备消息.
6. `DeviceGatewayHelper`: 统一处理设备消息,创建Session等操作的逻辑.
7. `DecodedClientMessageHandler`: 解码后的平台消息处理器，如果是自定义实现网关或者在协议包里手动回复消息等处理,
   则可以使用此接口直接将设备消息交给平台.(如果调用了`DeviceGatewayHelper`则不需要此操作).
8. `DeviceMessageBusinessHandler`: 处理设备状态数据库同步,设备自动注册等逻辑等类.
9. `LocalDeviceInstanceService`: 设备实例管理服务类.
10. `DeviceSessionManager`: 设备会话管理器,可获取当前服务的会话信息.
11. `DeviceDataStoragePolicy`: 设备存储策略接口,实现此接口来进行自定义设备数据存储策略.
12. `DeviceMessageConnector`: 负责将设备消息转发到`事件总线`.