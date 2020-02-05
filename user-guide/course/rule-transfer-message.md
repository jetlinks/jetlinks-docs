# 接收到设备事件上报消息并转换到MQTT客户端

本文档介绍通过MQTT.fx发送消息到物联网平台，并转发到另一个MQTT客户端中。

## 前提条件

1. 已在平台中创建产品和设备

2. 设备已接入平台（设备上线）

> 创建产品和设备具体操作细节，请参考[添加设备型号](../device-manager.md/#添加设备型号)、[添加设备实例](../device-manager.md/#添加设备实例)。
>
> 设备接入平台，请参考[设备接入教程](device-connection.md)

### 创建成功的设备实例信息展示

i. 设备基本信息

![设备基本信息](../files/device-connection/device-instance-general-info.png)

ii. 设备运行状态信息

![设备运行状态信息](../files/device-connection/device-instance-run-info.png)

iii. 设备日志

![设备日志](../files/device-connection/device-instance-log.png)

## 添加规则引擎

进入系统: `规则引擎`-`规则模型` 点击列表上的`添加模型` 添加一个规则模型模板。

![创建规则模型](../files/rule-transfer-message/create-rule-model.png)


1.添加`消息网关`规则节点，用于接收设备事件上报消息。

![生成消息网关节点](../files/rule-transfer-message/generate-message-gateway.png)

2.添加`MQTT客户端`规则节点，用于消息转发
