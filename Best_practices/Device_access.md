
## MQTT直连接入
本文档以MQTTX为例，介绍使用第三方软件以MQTT协议接入物联网平台。

### 下载并安装MQTTX
前往[官网下载](https://mqttx.app/)安装

### 系统配置
#### 操作步骤
1.**登录**Jetlinks物联网平台，进入**协议管理**菜单，上传协议。</br>

<a target='_blank' href='https://github.com/jetlinks/jetlinks-official-protocol'>获取协议包源码</a>

![](./img/61.png)

2.进入**网络组件**菜单，配置**MQTT服务**类型的网络组件。</br>
![](./img/203.png)

3.进入**设备接入网关**菜单，配置接入方式为**MQTT直连**的网关。</br>
&emsp;（1）选择MQTT服务类型的网络</br>
![](./img/204.png)
&emsp;（2）选择所需的协议包</br>
![](./img/protocol-select.png)
&emsp;（3）填写设备接入网关名称</br>
![](./img/gateway-create.png)

4.创建产品，并进入**设备接入**tab，选择所需的设备接入网关然后**启用**产品。
![](./img/product-access.png)

5.在**设备接入**tab页面中填写官方协议包认证信息；然后**启用**产品。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

不同协议包在设备接入界面所需要填写的方式不同。官方协议包，需要填写设备认证所需要的的账号密码

</div>

`在设备接入tab页的MQTT认证配置项中填写  secureId为：admin    secureKey为：admin。`


![](./img/auth-info.png)



6.创建设备，选择对应的所属产品，然后**启用**设备。

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

需要先启用产品，才能基于产品创建设备

</div>

![](./img/create-device.png)

### 使用MQTTX模拟设备连接到平台
1.打开MQTTX软件，点击新建连接创建一个连接，设置**连接参数**。
![](./img/209.png)
![](./img/mqtt-accept.png)

### 连接参数说明

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>Name</td>
            <td>输入您的自定义名称。</td>
          </tr>
          <tr>
            <td>Client ID</td>
            <td> 设备Id。必须与系统中设备的ID填写一致。</td>
          </tr>
          <tr>
            <td>Host</td>
            <td>连接域名。本地连接可直接填写 `127.0.0.1`,如为远程连接，请填写产品-设备接入页-连接信息显示的连接地址。</td>
          </tr>
         <tr>
            <td>Port</td>
            <td>请填写产品-设备接入页-连接信息显示的端口。</td>
          </tr>
          <tr>
            <td>Username</td>
            <td>填写接入账号</td>
          </tr>
         <tr>
            <td>Password</td>
            <td>填写接入密码</td>
          </tr>
        </tbody>
      </table>
</div>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

使用JetLinks官方协议包接入设备，用户名和密码需要经过加密规则处理。<br />
可使用账号密码<a href="http://doc.jetlinks.cn/basics-guide/mqtt-auth-generator.html">自动生成器</a>获取

</div>

![](./img/211.png)

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

经过加密规则处理的账号密码超过5分钟后将不在可用，需重新生成

</div>


2.点击**连接**按钮，平台中设备状态变为**在线**。
![](./img/mqtt-device-info.png)


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

如您在点击连接后遇到MQTTX提示异常信息或设备未上线等问题。可在设备详情-设备诊断页中，诊断设备未上线原因

</div>

![](./img/mqtt-device-diagnosis.png)


### 设备数据上下行
设备连接上平台后，可进行一些基本的事件上报、属性读取等操作。


### 物模型创建

在产品详情-物模型tab页中分别创建属性、事件、功能三种物模型

**创建属性** 属性ID: `temperature`

![](./img/device-property-temperature.png)

**创建功能**  功能ID: `playVoice`

![](./img/device-function-palyVoice.png)

**创建事件** 事件ID: `alarm_fire`

![](./img/device-event-alarmFire.png)

#### 读取设备属性
1.**登录**Jetlinks物联网平台，点击设备**查看**按钮，进入**运行状态**tab页。</br>
2.点击设备运行状态中某个属性的**获取属性值**按钮。
![](./img/property-read.png)
MQTTX会收到平台下发指令消息
![](./img/mqttx-property-read.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
复制好订阅该topic收到的消息中的messageId。此messageId将作为回复与平台设备属性的凭据之一。
</div>

3.回复平台设备读取消息
<br />
topic格式参考：[JetLinks官方协议-读取设备属性](/dev-guide/jetlinks-protocol-support.html#读取设备属性)
<br />
消息内容格式如下

```json

{
  "deviceId": "设备Id",
  "messageId": "平台下发报文中的messageId",
  "properties":{
    "temperature": 35.6
  },
  "success": true
}

```


**回复参数说明**

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>messageId</td>
            <td>平台下发报文中的messageId</td>
          </tr>
          <tr>
            <td>deviceId </td>
            <td>设备ID</td>
          </tr>
          <tr>
            <td>success</td>
            <td>成功标识</td>
          </tr>
           <tr>
            <td>properties</td>
            <td>设备属性值对象。例如： { "temperature":"50"} </td>
          </tr>
        </tbody>
      </table>


点击**消息发送按钮图标**，向平台推送该消息。
![](./img/mqttx-property-read-reply.png)



4.平台收到MqttX推送的属性值后，将会实时展示到运行状态中。</br>



<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>
从在系统界面中点击刷新到MQTTX端回复消息，需在10秒内完成。否则平台会视为该次操作超时，导致读取属性值失败。
</div>

#### 设备事件上报
MQTTX 推送设备事件消息到平台。<br />

1.在MQTTX上，填写事件上报topic。topic格式参考[JetLinks官方协议-设备事件上报](/dev-guide/jetlinks-protocol-support.html#设备事件上报) 。</br>
2.输入事件上报Topic和要发送的事件内容，点击"发送"按钮图标，向平台推送该事件消息。
![](./img/mqttx-event-report.png)

**设备事件上报数据**

```json
{
 "data":{
   "addr":"未来科技城 C2 栋",
  "time":"12-05-2012"
 }
}
```

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>data</td>
            <td>对象类型的报文数据</td>
          </tr>
        </tbody>
      </table>

上报成功后，在**设备-运行状态**tab页，可查看事件具体信息。
![](./img/device-event-list.png)


#### 调用设备功能
1.在**设备-设备功能**tab页，选择设备功能模块,点击执行,向设备发送功能调用指令。
![](./img/device-function-invoke.png)
3.填写设备功能回复topic和报文。格式参考[JetLinks官方协议-调用设备功能](/dev-guide/jetlinks-protocol-support.html#调用设备功能)
![](./img/mqttx-function-invoke-reply.png)

**设备功能回复报文**

```json
{
 "messageId":"平台下发报文中的messageId", 
 "output":true,
 "success":true
}
```
<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>messageId </td>
            <td>平台下发报文中的messageId</td>
          </tr>
          <tr>
            <td>output</td>
            <td>返回的对象类型执行结果</td>
          </tr>
           <tr>
            <td>success</td>
            <td>成功状态</td>
          </tr>
        </tbody>
      </table>

设备功能调用成功后在**设备-设备功能**tab页显示调用回复结果。

## MQTT Broker接入
在某些场景,设备不是直接接入平台,而是通过第三方MQTT服务,如:`emqx`.
消息编解码与MQTT服务一样,从消息协议中使用`DefaultTransport.MQTT`来获取消息编解码器.
本文使用`mqttx`模拟设备端，通过`emqx`接入平台。

### 安装并启动EMQ

前往[官网下载](https://docs.emqx.cn/broker/v4.3/getting-started/install.html)安装

本文使用docker搭建

```shell script
docker run -d --name emqx -p 18083:18083 -p 1883:1883 emqx/emqx:latest
```
### 访问EMQ Dashboard

在浏览器中输入 http://127.0.0.1:18083 ,默认账号密码为用户名：admin 密码：public。
![](./img/emq-monitor.png)

### 系统配置
#### 操作步骤
1.**登录**Jetlinks物联网平台，进入**网络组件**菜单，创建MQTT客户端网络组件。</br>

`如EMQX服务在本机电脑启用，MQTT客户端网络组件填写参数参考下图填写即可`

![](./img/mqtt-client-network.png)


**网络组件填写参数说明**

| 参数        | 说明   |  
| --------   | -----:  | 
| 远程地址      | emqx启动所在服务的IP地址，emq本机启动的可填写127.0.0.1   |   
| 远程端口        |   emqx启动的服务端口   |  
| clientId        |    连接到emqx的客户端Id    | 
| 用户名        |    连接到emqx时需要的用户名    | 
| 密码        |    连接到emqx时需要的密码    | 
| 最大消息长度        |    单次收发消息的最大长度,单位:字节;    | 

2.进入**协议管理**菜单，上传协议包。</br>

<a target='_blank' href='https://github.com/jetlinks/jetlinks-official-protocol'>获取协议包源码</a>

![](./img/254.png)
3.进入**设备接入网关**，创建MQTT Broker类型的接入网关。</br>
![](./img/mqttbrokerwg.png)


网关创建完成后，可在emqx客户端中的“Subscriptions”菜单中看到订阅列表
![](./img/emqx-sub-list.png)


4.[创建产品](../Device_access/Create_product3.1.md)，并选中接入方式为MQTT Broker类型的设备接入网关。</br>
![](./img/product-select-mqtt-broker-gateway.png)

5 创建物模型

在产品详情-物模型tab页中创建温度属性物模型，属性ID:`temperature`

![](./img/device-property-temperature.png)



5.[创建设备](../Device_access/Create_Device3.2.md)，选择第4步中创建的产品。</br>


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

需要先启用产品，才能基于产品创建设备

</div>

### 使用MQTTX模拟设备与平台进行交互

**下载并安装MQTTX**。  可前往[官网下载](https://mqttx.app/)安装

1.打开MQTTX软件，点击新建连接创建一个连接
![](./img/209.png)

2.设置**连接参数**。连接到EMQX

`如EMQX服务在本机电脑启用，连接参数参数参考下图填写即可`

![](./img/mqttx-connect-to-emqx.png)

**连接参数说明**

| 参数        | 说明   |  
| --------   | -----:  | 
| Name      | 自定义名称   |   
| Client ID        |  注册到EMQX的客户端Id。可自定义填写任意字符串   |  
| Host        |    填写启动EMQX服务的IP地址    | 
| Port        |    EMQX启动的服务端口    | 



### 物模型属性上报

推送物模型属性消息到EMQX。实现平台设备上线与消息接收


![](./img/mqttx-push-msg-to-emqx.png)

推送设备上报属性topic格式和报文参考：[JetLinks官方协议-读取设备属性](/dev-guide/jetlinks-protocol-support.html#读取设备属性)

`topic:/产品Id/设备Id/properties/report`

```json
{
    "properties":{"temperature":36.8}
}
```

进入平台设备详情界面，此时设备以变成在线状态，且收到了刚刚的温度属性消息

![](./img/mqtt-client-device-dashboard.png)

### 物模型属性读取回复

1.在MQTTX中点击订阅添加按钮
![](./img/mqttx-add-sub.png)

2.添加订阅设备读取属性topic：`/+/+/properties/read`。格式参考：[JetLinks官方协议-读取设备属性](/dev-guide/jetlinks-protocol-support.html#读取设备属性)

![](./img/mqttx-add-sub2.png)

后续操作参考[读取设备属性](/Best_practices/Device_access.html#读取设备属性)


[comment]: <> (## TCP 服务接入)

[comment]: <> (本文档使用[Packet Sender]&#40;https://packetsender.com/download#show&#41;工具模拟tcp客户端接入平台。)

[comment]: <> (###  系统配置)

[comment]: <> (1.**登录**Jetlinks物联网平台，进入**网络组件**菜单，创建TCP服务网络组件。</br>)

[comment]: <> (![]&#40;./img/tcpwl.png&#41;)

[comment]: <> (2.进入**协议管理**菜单，上传协议包。</br>)

[comment]: <> (![]&#40;./img/254.png&#41;)

[comment]: <> (3.进入**设备接入网关**，创建TCP透传接入类型的接入网关。</br>)

[comment]: <> (![]&#40;./img/tcpwg.png&#41;)

[comment]: <> (4.[创建产品]&#40;../Device_access/Create_product3.1.md&#41;，并选中接入方式为TCP透传接入类型的设备接入网关。</br>)

[comment]: <> (![]&#40;./img/tcpjr.png&#41;)

[comment]: <> (5.[创建设备]&#40;../Device_access/Create_Device3.2.md&#41;，所属产品选择TCP透传接入类型的产品。</br>)

[comment]: <> (### TCP工具接入)

[comment]: <> (1.下载并安装[Packet Sender]&#40;https://packetsender.com/download#show&#41;。)

[comment]: <> (<div class='explanation primary'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-bangzhu explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>说明</span>)

[comment]: <> (  </p>)

[comment]: <> (TCP协议以二进制的数据包传输数据，此处使用Packet Sender工具将发送的消息先转成十六进制，)

[comment]: <> (再通过该工具自动转换成二进制发送到平台。)

[comment]: <> (</div>)

[comment]: <> (2.生成所需的十六进制字符串。  )

[comment]: <> (i. 检出[协议代码]&#40;https://github.com/jetlinks/demo-protocol.git&#41;  )

[comment]: <> (ii. 执行测试包org.jetlinks.demo.protocol.tcp下DemoTcpMessageTest的test方法生成设备认证所需的十六进制字符串  )

[comment]: <> (代码如下：)

[comment]: <> (```java)

[comment]: <> (    @Test)

[comment]: <> (    void test&#40;&#41; {)

[comment]: <> (        DemoTcpMessage message = DemoTcpMessage.of&#40;MessageType.AUTH_REQ, AuthRequest.of&#40;1000, "admin"&#41;&#41;;)

[comment]: <> (        byte[] data = message.toBytes&#40;&#41;;)

[comment]: <> (        System.out.println&#40;Hex.encodeHexString&#40;data&#41;&#41;;)

[comment]: <> (        DemoTcpMessage decode = DemoTcpMessage.of&#40;data&#41;;)

[comment]: <> (        System.out.println&#40;decode&#41;;)

[comment]: <> (        Assertions.assertEquals&#40;message.getType&#40;&#41;, decode.getType&#40;&#41;&#41;;)

[comment]: <> (        Assertions.assertArrayEquals&#40;message.getData&#40;&#41;.toBytes&#40;&#41;, decode.getData&#40;&#41;.toBytes&#40;&#41;&#41;;)

[comment]: <> (    })
   
[comment]: <> (```)

[comment]: <> (结果：`000d000000e80300000000000061646d696e`)

[comment]: <> (<div class='explanation primary'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-bangzhu explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>说明</span>)

[comment]: <> (  </p>)

[comment]: <> (AuthRequest.of&#40;deviceId,key&#41; 第一个参数为设备id，第二参数为产品中配置的TCP认证配置。  )

[comment]: <> (</div>)

[comment]: <> (iii. 在测试类中执行如下代码生成事件上报所需的十六进制字符串： )

[comment]: <> (```java)

[comment]: <> (    @Test)

[comment]: <> (    void encodeEvent&#40;&#41; {)

[comment]: <> (        DemoTcpMessage demoTcpMessage = DemoTcpMessage.of&#40;MessageType.FIRE_ALARM,)

[comment]: <> (                FireAlarm.builder&#40;&#41;)

[comment]: <> (                        .point&#40;ThreadLocalRandom.current&#40;&#41;.nextInt&#40;&#41;&#41;)

[comment]: <> (                        .lat&#40;36.5F&#41;)

[comment]: <> (                        .lnt&#40;122.3F&#41;)

[comment]: <> (                        .deviceId&#40;1000&#41;)

[comment]: <> (                        .build&#40;&#41;&#41;;)

[comment]: <> (        byte[] data = demoTcpMessage.toBytes&#40;&#41;;)

[comment]: <> (        System.out.println&#40;demoTcpMessage&#41;;)

[comment]: <> (        System.out.println&#40;Hex.encodeHexString&#40;data&#41;&#41;;)

[comment]: <> (    })

[comment]: <> (```  )

[comment]: <> (结果：`0614000000e8030000000000009a99f4420000124222b7c94c`)

[comment]: <> (3.设置参数)

[comment]: <> (i. 设置基本信息)

[comment]: <> (![]&#40;./img/269.png&#41;)

[comment]: <> (<table class='table'>)

[comment]: <> (        <thead>)

[comment]: <> (            <tr>)

[comment]: <> (              <td>参数</td>)

[comment]: <> (              <td>说明</td>)

[comment]: <> (            </tr>)

[comment]: <> (        </thead>)

[comment]: <> (        <tbody>)

[comment]: <> (          <tr>)

[comment]: <> (            <td>Name</td>)

[comment]: <> (            <td>输入您的自定义名称。</td>)

[comment]: <> (          </tr>)

[comment]: <> (          <tr>)

[comment]: <> (            <td>ASCII </td>)

[comment]: <> (            <td>ASCII码，输入下方十六进制字符串后会自动生成。</td>)

[comment]: <> (          </tr>)

[comment]: <> (          <tr>)

[comment]: <> (            <td>HEX</td>)

[comment]: <> (            <td>十六进制。</td>)

[comment]: <> (          </tr>)

[comment]: <> (           <tr>)

[comment]: <> (            <td>Address</td>)

[comment]: <> (            <td>TCP服务地址。</td>)

[comment]: <> (          </tr>)

[comment]: <> (           <tr>)

[comment]: <> (            <td>Port</td>)

[comment]: <> (            <td>TCP服务端口。</td>)

[comment]: <> (          </tr>)

[comment]: <> (          <tr>)

[comment]: <> (            <td>Persistent TCP</td>)

[comment]: <> (            <td>勾选之后可保持长连接。</td>)

[comment]: <> (          </tr>)

[comment]: <> (        </tbody>)

[comment]: <> (      </table>)

[comment]: <> (<div class='explanation primary'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-bangzhu explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>说明</span>)

[comment]: <> (  </p>)

[comment]: <> (设置参数时，请确保参数值中或参数值的前后均没有空格。)

[comment]: <> (</div>)

[comment]: <> (模式选择TCP。)

[comment]: <> (![]&#40;./img/270.png&#41;)

[comment]: <> (**分别保存上线参数以及事件上报参数**。</br>)

[comment]: <> (设备上线：)

[comment]: <> (![]&#40;./img/271.png&#41;)

[comment]: <> (事件上报：)

[comment]: <> (![]&#40;./img/272.png&#41;)

[comment]: <> (4.模拟设备上下线</br>)

[comment]: <> (单击packetsender工具上`Send`按钮发起请求。)

[comment]: <> (![]&#40;./img/273.png&#41;)

[comment]: <> (平台中设备状态变为上线即为连接成功,在设备日志模块可以看到设备上线日志。</br>)

[comment]: <> (勾选`Persistent TCP`packetsender上请求成功后会打开一个新的已连接页面。)

[comment]: <> (![]&#40;./img/274.png&#41;)

[comment]: <> (关闭这个新的已连接页面即可断开设备与平台的连接,平台中设备状态变为离线,同时在设备日志模块可以看到设备离线日志。</br>)

[comment]: <> (5.模拟设备上报事件</br>)

[comment]: <> (i. 在第4.步，设备上线成功后打开的新的已连接页面上选择第3.步保存的事件上报参数。)

[comment]: <> (![]&#40;./img/275.png&#41;)

[comment]: <> (ii. 上报成功后，在**设备-运行状态**页面可以查看到。)

## HTTP接入
### 系统配置
1.**登录**Jetlinks物联网平台，进入**网络组件**菜单，创建HTTP服务网络组件。</br>
![](./img/httpwl.png)
2.进入**协议管理**菜单，上传协议包。</br>
![](./img/254.png)
3.进入**设备接入网关**，创建HTTP推送接入类型的接入网关。</br>
![](./img/httpwg.png)
4.[创建产品](../Device_access/Create_product3.1.md)，并选中接入方式为HTTP推送类型的设备接入网关。</br>
![](./img/httpjr.png)
5.[创建设备](../Device_access/Create_Device3.2.md)，所属产品选择HTTP推送接入类型的产品。</br>

### 推送消息
此处使用postman模拟设备请求。
#### 模拟设备上报属性
![](./img/276.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
请求时路径中带的/report-property相当于mqtt中的topic，在demo协议将中根据路径来判断消息类型。
</div>

上报后，在**设备-运行状态**中进行查看。
#### 模拟设备事件上报
![](./img/277.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
请求时路径中带的/fire-alarm相当于mqtt中的topic，在demo协议将中根据路径来判断消息类型。
</div>

上报后，在**设备-运行状态**中，点击左侧菜单，切换至对应事件，进行查看。
![](./img/265.png)

#### 指令下发
由于http是短链接,无法直接下发指令,可以在`消息拦截器中`或者`编码时`通过将消息设置到`device.setConfig`中,在收到 http请求拉取消息时，通过`device.getSelfConfig`获取配置,并返回。

## 使用CoAP服务接入
本文档使用[coap-cli](https://www.npmjs.com/package/coap-cli)模拟设备接入平台。

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

本功能仅在企业版中提供。

</div>


### 系统配置
1.**登录**Jetlinks物联网平台，进入**网络组件**菜单，创建CoAP服务网络组件。</br>
![](./img/coapwl.png)
2.进入**协议管理**菜单，上传协议包。</br>
![](./img/254.png)
3.进入**设备接入网关**，创建CoAP接入类型的接入网关。</br>
![](./img/coapwg.png)
4.[创建产品](../Device_access/Create_product3.1.md)，并选中接入方式为CoAP接入类型的设备接入网关。</br>
![](./img/coapjr.png)
5.[创建设备](../Device_access/Create_Device3.2.md)，所属产品选择CoAP接入类型的产品。</br>

### 使用coap-cli模拟客户端接入
1.下载并安装`coap-cli`。
```shell script
npm install coap-cli -g
```

2.模拟设备设备属性上报

```shell script
echo -n '{"deviceId":"coap-test-001","properties":{"temperature":36.5}}' | coap post coap://localhost:8009/report-property
```
在**设备-运行状态**中可以看到温度属性已发生变化。  </br>
3.模拟设备上报事件

```shell script
echo -n '{"deviceId":"coap-test-001","pname":"智能温控","aid":105,"a_name":"未来科技城","b_name":"C2 栋","l_name":"12-05-201","timestamp":"2019-11-06 16:28:50","alarm_type":1,"alarm_describe":"火灾报警","event_id":1,"event_count":1}' | coap post coap://localhost:8009/fire_alarm
```
在**设备-运行状态**中点击左侧菜单，切换至对应事件，进行查看。
![](./img/265.png)

## 使用UDP接入


<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

本功能仅在企业版中提供。

</div>


### 系统配置
1.**登录**Jetlinks物联网平台，进入**网络组件**菜单，创建UDP网络组件。</br>
![](./img/udpwl.png)
2.进入**协议管理**菜单，上传协议包。</br>
![](./img/254.png)
3.进入**设备接入网关**，创建UDP接入类型的接入网关。</br>
![](./img/udpwg.png)
4.[创建产品](../Device_access/Create_product3.1.md)，并选中接入方式为UDP接入类型的设备接入网关。</br>
![](./img/udpjr.png)
5.[创建设备](../Device_access/Create_Device3.2.md)，所属产品选择UDP接入类型的产品。</br>

### 使用UDP模拟工具接入
1.下载并安装`SocketTool4`。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
此处以json方式传输数据。
</div>

2.创建udp客户端。  
![](./img/278.png)

3.模拟设备设备属性上报</br>
在SocketTool4工具的**数据发送窗口**填写发送的报文。</br>
此处使用的报文为：
```json
{
  "properties":{
      "temperature":36.5 //温度属性
     },
  "messageType": "REPORT_PROPERTY",//org.jetlinks.core.message.MessageType
  "deviceId": "udp-test-001",//设备id
  "key": "admin"//udp认证配置，udp_auth_key	
}
```
单击**发送数据**按钮发起发送数据。
![](./img/279.png)
收到上报的消息后平台中设备状态将变为上线,在**设备-运行状态**中可以看到温度属性已发生变化。</br>

4.模拟设备上报事件</br>
在SocketTool4工具的**数据发送窗口**填写发送的报文。</br>
此处使用的报文为：</br>
```json
{
    "data": {
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
    },
    "event": "fire_alarm",//事件标识
    "messageType": "EVENT",//org.jetlinks.core.message.MessageType
    "deviceId": "udp-test-001",//设备id
    "key": "admin"//udp认证配置，udp_auth_key	
}
``` 

单击**发送数据**按钮发起发送数据。  
![](./img/280.png)
在**设备-运行状态**中点击左侧菜单，切换至对应事件，进行查看。
![](./img/265.png)

## TCP、MQTT短连接接入
默认情况下,使用tcp和mqtt方式接入时,当连接断开时,则认为设备已离线。
但是在某些场景(如:低功率设备)下,无法使用长连接进行通信,可以通过指定特定配置使平台保持设备在线状态。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
以下功能及API在jetlinks 1.4.0 后提供。
</div>

### 保持在线

在自定义协议包解码出消息时，可通过在消息中添加头`keepOnline`来进行设置。如:

```java

message.addHeader(Headers.keepOnline,true); //设置让会话强制在线
message.addHeader(Headers.keepOnlineTimeoutSeconds,600);//设置超时时间（可选,默认10分钟），如果超过这个时间没有收到任何消息则认为离线。

```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
MQTT接入时添加到任意消息即可。TCP接入时添加到DeviceOnlineMessage即可。

如果服务重启，将不会保存在线状态！</p>
</div>

### 缓存下发消息

在进行消息下发时，因为会话是强制保持在线的，所以消息会直接通过session下发，但是此时设备可能已经断开了连接,
将会抛出异常`DeviceOperationException(ErrorCode.CLIENT_OFFLINE)`。这时候可以通过将消息缓存起来，等待下次设备
连接上来后再下发指令。

一、在自定义协议包中使用消息拦截器拦截异常

```java
support.addMessageSenderInterceptor(new DeviceMessageSenderInterceptor() {
    @Override
    public <R extends DeviceMessage> Flux<R> afterSent(DeviceOperator device, DeviceMessage message, Flux<R> reply) {

        return reply.onErrorResume(DeviceOperationException.class, err -> {
            if (err.getCode() == ErrorCode.CLIENT_OFFLINE) {
                return device
                    .setConfig("will-msg", message) //设置到配置中
                    .thenReturn(((RepayableDeviceMessage<?>) message)
                        .newReply()
                        .code(ErrorCode.REQUEST_HANDLING.name())
                        .message("设备处理中...")
                        .success()
                    )
                    .map(r -> (R) r);
            }
            return Mono.error(err);
        });
    }
});

```

二、获取缓存的消息

在收到设备指令后进行解码时,可以先获取是否有缓存到消息,然后发送到设备。

伪代码如下:

```java

@Override
public Mono<? extends Message> decode(MessageDecodeContext context) {

    return context.getDevice()
        .getAndRemoveConfig("will-msg")
        .map(val -> val.as(DeviceMessage.class))
        .flatMap((msg) -> {
            return ((FromDeviceMessageContext) context)
                .getSession()
                .send(doEncode(msg)); //编码并发送给设备
        })
        .thenReturn(doDecode(context)); //解码收到的消息

}

```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
以上代码仅作为演示功能逻辑,请根据实际情况进行相应的处理。
</div>

