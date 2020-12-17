# JetLinks 官方协议

除了使用自定义协议以外,jetlinks提供了默认的协议支持.
设备可以使用此协议接入平台. 设备协议已经确定并且无法修改协议的时候,建议使用[自定义协议接入](../basics-guide/protocol-support.md)

[查看源码](https://github.com/jetlinks/jetlinks-official-protocol)

## MQTT接入

目前支持MQTT3.1.1和3.1版本协议.

### 认证

CONNECT报文:

```text
clientId: 设备实例ID
username: secureId+"|"+timestamp
password: md5(secureId+"|"+timestamp+"|"+secureKey)
```

说明: `secureId`以及`secureKey`在创建设备产品和设备实例时进行配置.
`timestamp`为当前系统时间戳(毫秒),与系统时间不能相差5分钟.

### 读取设备属性
    
topic: `/{productId}/{deviceId}/properties/read`

方向: `下行`

消息格式: 

```json
{
"messageId":"消息ID",
"deviceId":"设备ID",
"properties":["sn","model"] //要读取到属性列表
}
```
            
回复Topic: `/{productId}/{deviceId}/properties/read/reply`

回复消息格式:
    
```json
//成功
{
    "messageId":"与下行消息中的messageId相同",
    "properties":{"sn":"test","model":"test"}, //key与设备模型中定义的属性id一致
    "deviceId":"设备ID",
    "success":true,
}
//失败. 下同
{
    "messageId":"与下行消息中的messageId相同",
    "success":false,
    "code":"error_code",
    "message":"失败原因"
}
```

### 修改设备属性:
    
topic: `/{productId}/{deviceId}/properties/write`

方向: `下行`

消息格式: 
    
```json
{
"messageId":"消息ID",
"deviceId":"设备ID",
"properties":{"color":"red"} //要设置的属性
}
```

回复Topic: `/{productId}/{deviceId}/properties/write/reply`

方向: `上行`

回复消息格式:

```json
{
"messageId":"与下行消息中的messageId相同",
"properties":{"color":"red"}, //设置成功后的属性,可不返回
"success":true,
}
```

### 设备属性上报

topic: `/{productId}/{deviceId}/properties/report`

方向: `上行`

消息格式: 

```json
{
"messageId":"随机消息ID",
"properties":{"temp":36.8} //上报数据
}
```

### 调用设备功能
 
topic: `/{productId}/{deviceId}/function/invoke`

方向: `下行`

消息格式: 

```json
{
"messageId":"消息ID",
"deviceId":"设备ID",
"function":"playVoice",//功能ID
"inputs":[{"name":"text","value":"播放声音"}] //参数
}
```

         
回复Topic: `/{productId}/{deviceId}/function/invoke/reply`
      
方向: `上行`

消息格式:

```json
{
"messageId":"与下行消息中的messageId相同",
"output":"success", //返回执行结果
"success":true,
}
```    

### 设备事件上报
     
topic: /{productId}/{deviceId}/event/{eventId}
      
方向: `上行`

消息格式: 

```json
{
"messageId":"随机消息ID",
"data":100 //上报数据
}
```

### 子设备注册

与子设备消息配合使用,实现设备与网关设备进行自动绑定.
     
topic: /{productId}/{deviceId}/child/{childDeviceId}/register
      
方向: `上行`

消息格式: 

```json
{
"messageId":"随机消息ID",
"deviceId":"子设备ID"
}
```

### 子设备注销

与子设备消息配合使用,实现设备与网关设备进行自动解绑.
     
topic: /{productId}/{deviceId}/child/{childDeviceId}/unregister
      
方向: `上行`

消息格式: 

```json
{
"messageId":"随机消息ID",
"deviceId":"子设备ID"
}
```

### 子设备上线

与子设备消息配合使用,实现关联到网关的子设备上线.(默认情况下,网关上线,子设备也会全部自动上线.)
     
topic: /{productId}/{deviceId}/child/{childDeviceId}/connected
      
方向: `上行`

消息格式: 

```json
{
"messageId":"随机消息ID",
"timestamp":1584331469964//时间戳
}
```

### 子设备离线

与子设备消息配合使用,实现关联到网关的子设备离线.(默认情况下,网关离线,子设备也会全部自动离线.)
     
topic: /{productId}/{deviceId}/child/{childDeviceId}/disconnect
      
方向: `上行`

消息格式: 

```json
{
"messageId":"随机消息ID",
"timestamp":1584331469964//时间戳
}
```

 ### 子设备消息
     
topic: /{productId}/{deviceId}/child/{childDeviceId}/{topic}
      
方向: `上行或下行`, 根据{topic}决定.

::: tip
 {topic} 以及数据格式与设备topic定义一致. 如: 获取子设备属性: `/1/d1/child/c1/properties/read`,
:::


## CoAP接入
使用CoAP协议接入仅需要对数据进行加密即可.加密算法: AES/ECB/PKCS5Padding.

使用自定义Option: `2100:设备ID` 来标识设备.

将请求体进行加密,密钥为在创建设备产品和设备实例时进行配置的(`secureKey`).

请求地址(`URI`)与MQTT `Topic`相同.消息体(`payload`)与MQTT相同(只支持上行消息).


## DTLS接入
使用CoAP DTLS 协议接入时需要先进行认证:

发送认证请求:
```text
POST /auth
Accept: application/json
Content-Format: application/json
2100: 设备ID
2110: 签名 md5(payload+secureKey)
payload: {"timestamp":"时间戳"}
```

响应结果:
```text
2.05 (Content)
payload: {"token":"令牌"}
```

之后的请求中需要将返回的令牌携带到自定义Option:2111

例如:
```text
POST /test/device1/event/fire_alarm
2100: 设备ID
2111: 令牌
...其他Option
payload: json数据
```
