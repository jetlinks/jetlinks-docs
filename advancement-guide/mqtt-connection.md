# 使用MQTT服务网关接入设备

本文档以MQTT.fx为例，介绍使用第三方软件以MQTT协议接入物联网平台。MQTT.fx是一款基于Eclipse Paho，使用Java语言编写的MQTT客户端工具。支持通过Topic订阅和发布消息。

## 创建协议

> 自定义消息协议创建，请参考[消息协议定义](../basics-guide/protocol-support.md)。

**例**

i. 选择 `设备管理`-->`协议管理`--> 点击`新建协议`按钮

![新建协议导航](../basics-guide/files/device-connection/new-protocol.png)

ii. 输入型号名称

iii. 选择型号类型为 `jar`

iv. 输入类名`org.jetlinks.demo.protocol.DemoProtocolSupportProvider`

v. 上传jar包  [demo-protocol-1.0.jar](../basics-guide/files/device-connection/demo-protocol-1.0.jar)

vi. 点击保存，完成协议新增。

### 创建完成信息展示  

![新建型号协议](../basics-guide/files/device-connection/product-protocol.png)

## 创建设备型号

> 完整的设备型号创建，请参考[添加设备型号](../basics-guide/device-manager.md#添加设备型号)。

**例**

i.下载型号文件[配置JSON](../basics-guide/files/device-connection/设备型号-智能温控.json)

ii. 选择 `设备管理`-->`设备型号`--> 点击`导入配置`按钮

![导入型号导航](../basics-guide/files/device-connection/import-product.png)

iii. 选择[配置JSON](../basics-guide/files/device-connection/设备型号-智能温控.json)文件

iv.型号导入完成效果如下图

![未发布型号](../basics-guide/files/device-connection/device-product-unpublished.png)  

 v.点击上图中`未发布`链接完成型号发布，导航栏中发布状态变为`已发布`代表发布成功。  

![已发布型号](../basics-guide/files/device-connection/device-product-published.png)

### 创建成功的型号信息展示 

i. 点击导航栏中`编辑`链接查看型号基本信息

ii. 点击弹出框中`设备定义`板块查看型号设备定义

iii. 点击属性定义、事件定义对应操作下的编辑按钮查看更详细的型号信息  

属性定义参数：  
![型号信息1](../basics-guide/files/device-connection/device-product-info1.png)  

功能定义参数：  
![型号信息2](../basics-guide/files/device-connection/device-product-info2.png)  

事件定义参数：  
![型号信息3](../basics-guide/files/device-connection/device-product-info3.png)

## 创建设备实例

> 自定义创建设备实例，请参考[添加设备实例](../basics-guide/device-manager.md#添加设备实例)。

**例**

i. 下载设备实例Excel文件[设备实例Excel](../basics-guide/files/device-connection/智能温控测试设备.xlsx)

ii. 选择 `设备管理`-->`设备实例`--> 点击`导入实例`按钮

![导入设备导航](../basics-guide/files/device-connection/import-device.png)

iii. 选择[设备实例Excel](../basics-guide/files/device-connection/智能温控测试设备.xlsx)文件

iv. 设备实例导入完成效果如下图

![未激活的设备](../basics-guide/files/device-connection/device-instance-not-active.png)

v. 点击上图中`激活`链接完成设备实例激活，状态栏中发布状态变为`离线`代表设备激活成功。

![已激活的设备](../basics-guide/files/device-connection/device-instance-offline.png)

### 创建成功的设备实例信息展示

i. 设备基本信息

![设备基本信息](../basics-guide/files/device-connection/device-instance-general-info.png)

ii. 设备运行状态信息

![设备运行状态信息](../basics-guide/files/device-connection/device-instance-run-info.png)

iii. 设备日志

![设备日志](../basics-guide/files/device-connection/device-instance-log.png)

## 创建网关配置

参照[启动设备网关服务](../basics-guide/course/device-gateway.md)

**例**
- 创建MQTT服务网络组件
- 创建MQTT服务设备网关

MQTT服务组件配置  
![MQTT服务组件配置](../basics-guide/files/device-connection/mqtt-config.png)

MQTT服务设备网关  
![MQTT服务设备网关](../basics-guide/files/device-connection/mqtt-gateway-info.png)

## 使用MQTT.fx接入

1.下载并安装MQTT.fx软件。请访问[MQTT.fx官网](https://mqttfx.jensd.de/index.php/download?spm=a2c4g.11186623.2.16.20ab5800HxuVJR)。

2.打开MQTT.fx软件，单击设置图标。

![mqttfx首页](../basics-guide/files/device-connection/mqttfx-index.png)

3.设置连接参数。

::: tip 注意
设置参数时，请确保参数值中或参数值的前后均没有空格。
:::

i. 设置基本信息

![mqtt基本信息设置](../basics-guide/files/device-connection/mqtt-connection-general.png)

| 参数         | 说明    |
| :-----   | :-----  |
| Profile Name       | 输入您的自定义名称。   |
| Profile Type        |   选择为**MQTT Broker**。   |

MQTT Broker Profile Settings

| 参数         | 说明    |
| :-----   | :-----  |
| Broker Address       | 连接域名。本地连接可直接填写 `127.0.0.1`,如为远程连接，请填写远程连接地址 |
| Broker Port        |   设置为`1889`   |
| Client ID        |    设备Id。本文档中为演示设备实例`test001`   |

General栏目下的设置项可保持系统默认，也可以根据您的具体需求设置。

ii. 单击User Credentials，设置User Name和Password。

> 在消息协议未定义前,username,password可以填写任意字符,但是不能留空。

![mqtt用户名名密码设置](../basics-guide/files/device-connection/mqtt-connection-user.png)

| 参数         | 说明    |
| :-----   | :-----  |
| User Name       | 由[消息协议定义](../basics-guide/protocol-support.md)中决定User Name值（消息协议未定义时可填写任意值）   |
| Password         |   由[消息协议定义](../basics-guide/protocol-support.md)中决定Password值  （消息协议未定义时可填写任意值） |

4.设置完成后，单击右下角的**OK**。

## 设备操作

设备连接上平台，并进行一些基本的事件收发、属性读取操作。

### 设备上下线

单击 Mqtt fx 中`Connect`进行连接

![mqtt连接](../basics-guide/files/device-connection/mqtt-connection.png)

平台中设备状态变为上线即为连接成功

![设备上线](../basics-guide/files/device-connection/device-online.png)

在设备日志模块可以看到设备上线日志

![设备上线日志](../basics-guide/files/device-connection/device-online-log.png)

单击 Mqtt fx 中`Disconnect`断开连接

![mqtt连接](../basics-guide/files/device-connection/mqtt-connection.png)

平台中设备状态变为离线即为断开连接成功

![设备离线](../basics-guide/files/device-connection/device-offline.png)

在设备日志模块可以看到设备离线日志

![设备离线日志](../basics-guide/files/device-connection/device-offline-log.png)

### 读取设备属性
::: tip 注意
第2步中回复平台属性值需要在第1步平台发送订阅以后的十秒钟内完成，否则平台会视为该次操作超时，导致读取属性值失败。
:::

1.平台告知设备（MQTT.fx）需要设备返回设备属性

在MQTT.fx上订阅消息，订阅物联网平台下发设备返回属性的Topic

i.  在MQTT.fx上，单击Subscribe。

ii. 输入平台获取设备属性会发送给网关的topic`/read-property`

iii.  单击Subscribe，订阅这个topic

![订阅topic](../basics-guide/files/device-connection/mqtt-sub.png)

iv. 平台发送订阅操作

单击设备实例页面中`test001`设备对应的`查看`链接

选择弹出框中`运行状态板块`

单击属性刷新

![平台属性订阅操作](../basics-guide/files/device-connection/device-property-refresh.png)

v. 订阅topic: `/read-property`对应的消息

![订阅topic](../basics-guide/files/device-connection/mqttfx-sub-read-property.png)

::: tip 注意:
 复制好订阅该topic收到的消息中的messageId。此messageId将作为回复与平台设备属性的凭据之一
:::

2.设备（MQTT.fx）回复平台设备属性值

在MQTT.fx上发送消息，发送平台所需要的设备属性值。

i.  在MQTT.fx上，单击 `Publish`。

ii.  输入一个回复平台属性值消息Topic和要发送的消息内容，单击Publish，向平台推送该消息。

![回复设备属性](../basics-guide/files/device-connection/mqttfx-replay-device-property.png)

| 参数         | 说明    |
| :-----   | :-----  |
| messageId       | 第一步订阅平台topic“/read-property”所收到的messageId值   |
| deviceId        |   设备Id   |
| timestamp        |   当前时间戳   |
| success        |   成功标识   |
| properties        |   设备属性值对象。例如： { "threshold":"50"}   |

该文档所使用的回复内容

```json
{
    "messageId":"第一步订阅平台topic“/read-property”所收到的messageId值",
    "deviceId":"test001",
    "timestamp":1583809148000,
    "success":true,
    "properties":{
      "temperature":36.5
     }
}
```

iii. 平台收到Mqtt.fx推送的属性值

![平台收到属性值](../basics-guide/files/device-connection/mqttfx-replyed-property-value.png)

iv. 读取设备属性回复的日志

![设备属性读取日志](../basics-guide/files/device-connection/read-device-property-reply-log.png)

#### 获取设备属性值完整演示

::: warning 注意：在下图中，从在界面上刷新属性开始直到动图结束的所有操作，需要在十秒钟内完成。否则平台会视为该次操作超时，导致读取属性值失败。
:::
![获取设备属性值](../basics-guide/files/device-connection/read-device-property.gif)

### 设备事件上报

MQTT.fx 推送设备事件消息到平台

以火灾报警事件为例。

1.在MQTT.fx上，单击 `Publish`。  

2.输入事件上报Topic和要发送的事件内容，单击Publish按钮，向平台推送该事件消息。

![设备事件上报](../basics-guide/files/device-connection/mqttfx-device-event-report.png)

该文档使用的topic: `/fire_alarm`

该文档所使用的回复内容

```json
{
   "deviceId":"test001",
    "pname":"智能温控",
    "aid":105,
    "a_name":"未来科技城",
    "b_name":"C2 栋",
    "l_name":"12-05-201",
    "timestamp":"2019-11-06 16:28:50",
    "alarm_type":1,
    "alarm_describe":"火灾报警",
    "event_id":1,
    "event_count":1
}
```

| 参数         | 说明    |
| :-----   | :-----  |
| deviceId       | 设备Id   |
| pname        |   设备型号名称   |
| aid        |   区域Id   |
| a_name        |   区域名称   |
| b_name        |   建筑名称   |
| l_name        |   位置名称   |
| timestamp        |   上报时间   |
| alarm_type        |   报警类型   |
| alarm_describe        |   报警描述   |
| event_id        |   事件 ID   |
| event_count        |   该事件上报次数   |

3.事件上报设备日志

![事件上报设备日志](../basics-guide/files/device-connection/device-event-report-log.png)

4.事件上报内容

![事件上报内容](../basics-guide/files/device-connection/device-event-info.png)
![事件上报内容1](../basics-guide/files/device-connection/device-event-info1.png)
