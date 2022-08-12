# 通过第三方MQTT服务接入设备

在某些场景,设备不是直接接入平台,而是通过第三方MQTT服务,如:`emqx`.
消息编解码与MQTT服务一样,从消息协议中使用`DefaultTransport.MQTT`来获取消息编解码器.
本文使用`mqttx`模拟设备端，通过`emqx`接入平台。

## 安装并启动EMQ

前往[官网下载](https://docs.emqx.cn/broker/v4.3/getting-started/install.html)安装

本文使用docker搭建

```shell script
docker run --name emq -p 18083:18083 -p 1883:1883 -p 8084:8084 -p 8883:8883 -p 8083:8083 -d registry.cn-hangzhou.aliyuncs.com/synbop/emqttd:2.3.6
```
### 访问EMQ Dashboard

在浏览器中输入 http://127.0.0.1:18083 ,默认账号密码为用户名：admin 密码：public。

![emq-dashboard](images/third-mqtt/emq-dashboard.png)

## 创建MQTT客户端网络组件

1. 选择 `网络组件`-->`组件管理`--> 点击`新增组件`按钮。  
![insert-mqtt-client](images/third-mqtt/insert-mqtt-client.png)  

::: tip 说明
HOST:emq所在服务的ip. PORT:emq 1883端口, 根据实际启动的emq服务来调整.
:::

2. 在创建完成的模块上点击`启动`按钮。  
![mqtt-client-start](images/third-mqtt/mqtt-client-start.png)  

## 创建MQTT客户端设备网关

1. 选择 `网络组件`-->`设备网关`--> 点击`新建`按钮。
![insert-mqtt-gateway](images/third-mqtt/insert-mqtt-gateway.png)  

::: warning 注意
在`Topics`中输入的内容需要与实际协议包中的定义对应,本文使用[官方协议](basics-guide/jetlinks-protocol-support.html)进行测试.
```text
/+/+/properties/report,/+/+/properties/write/reply,/+/+/properties/read/reply,/+/+/event/+,/+/+/child/#,/+/+/online,/+/+/offline
```
在集群模式下,请使用了`emqx`的[共享订阅功能](https://docs.emqx.com/zh/enterprise/v4.4/advanced/shared-subscriptions.html#%E5%B8%A6%E7%BE%A4%E7%BB%84%E7%9A%84%E5%85%B1%E4%BA%AB%E8%AE%A2%E9%98%85),添加对应的前缀,如: `$share/jetlinks//+/+/properties/report`.

和MQTT服务设备网关不同的是,客户端必须指定消息协议,因为平台无法通过mqtt消息识别出对应的设备标识.

:::


1. 在操作列点击`启动`按钮启动网关。  
![mqtt-gateway-start](images/third-mqtt/mqtt-gateway-start.png)   

## 创建产品和设备

参考[通过MQTT直连接入设备](mqtt-connection.html#创建产品)


## 使用MQTTx连接EMQ

![mqttfx-config](images/third-mqtt/mqttfx-config.png)  

::: tip 注意：
clientId 和用户名密码符合emq规则即可,这时的认证是通过emq,而不是平台.
:::

## 设备上下线

平台收到任意设备消息后则认为设备上线,或者推送: `/{productId}/{deviceId}/online`.

设备下线推送topic: `/{productId}/{deviceId}/offline`.

::: warning 说明
`{productId}`请替换为平台中的产品ID,`{deviceId}`请替换为平台中的设备ID.
:::

## 数据上下行

通过此方法接入对于设备端除了认证方式，上下线逻辑不同,
其他消息格式以及topic都是与[使用MQTT服务网关接入设备](mqtt-connection.html#读取设备属性)一致的
