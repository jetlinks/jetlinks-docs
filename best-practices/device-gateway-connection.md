# 通过网关设备接入多个下挂设备

场景: 网关设备与平台进行连接,传感器连接到网关设备. 平台可对网关以及网关下的所有设备进行管理.

## 协议解析

以下操作基于[快速体验](../quick-start/demo.md).
中使用的[demo协议包](https://github.com/jetlinks/demo-protocol)
以及[模拟器脚本](https://github.com/jetlinks/jetlinks-community/tree/master/simulator).

::: tip
可根据[设备消息协议解析SDK](../basics-guide/protocol-support.md).修改协议解析方式.
子设备消息请重点关注: `ChildDeviceMessage`以及`ChildDeviceMessageReply`
:::

## 创建网关设备

**操作步骤**
1. 进入系统: `设备管理`-`设备实例`-`新建`。  

2. 输入设备信息。  

3. 点击`确定`进行保存。  
![设备新增](images/insert-device.png)

## 创建子设备

TODO

## 关联网关设备和子设备

TODO

## 设备上线

网关与平台建立连接时,会自动修改所有子设备的状态.
如果要单独更新子设备上下线状态.请根据协议解析为`DeviceOnlineMessage`或者`DeviceOfflineMessage`.

## 消息下行

平台已经自动处理了下行的子设备消息.直接调用设备消息接口即可.
如果出现无法下行消息,或者状态不同步时,尝试重新激活一下设备.
