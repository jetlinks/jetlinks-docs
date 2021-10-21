# JetLinks 演示协议

设备协议已经确定并且无法修改协议的时候,建议使用[自定义协议接入](../basics-guide/protocol-support.md)

[查看源码](https://github.com/jetlinks/demo-protocol)

## 协议topic主题说明

|  名词   | 解释  |
|  ----  | ----  |
| 上行topic  | 设备端向平台发送 |
| 下行topic  | 平台向设备端发送 |


## Topic列表
### 下行Topic:
         读取设备属性: /read-property
         修改设备属性: /write-property
         调用设备功能: /invoke-function

### 上行Topic:
          读取属性回复: /read-property
          修改属性回复: /write-property
          设备功能回复: /invoke-function
          上报设备事件: /dev_msg
          上报设备属性: /report-property


## MQTT接入

目前支持MQTT3.1.1和3.1版本协议.

### 认证

CONNECT报文:

```text
clientId: 设备实例ID
username: 产品设备详情MQTT认证配置的username
password: 产品设备详情MQTT认证配置的password
```
 
说明: 演示协议包的用户名密码为明文密码.

### 读取设备属性
    
topic: `/read-property`

方向: `下行`

消息格式: 

```json
{
"timestamp":1601196762389, //毫秒时间戳
"messageId":"消息ID",
"deviceId":"设备ID",
"properties":["sn","model"] //要读取到属性列表
}
```
            
回复Topic: `/read-property`
方向: `上行`
回复消息格式:
    
```json
//成功
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"与下行消息中的messageId相同",
    "properties":{"sn":"test","model":"test"}, //key与设备模型中定义的属性id一致
    "deviceId":"设备ID",
    "success":true
}
//失败. 下同
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"与下行消息中的messageId相同",
    "success":false,
    "code":"error_code",
    "message":"失败原因"
}
```

### 修改设备属性
    
topic: `/write-property`

方向: `下行`

消息格式: 
    
```json
{
"timestamp":1601196762389, //毫秒时间戳
"messageId":"消息ID",
"deviceId":"设备ID",
"properties":{"color":"red"} //要设置的属性
}
```

回复Topic: `/write-property`

方向: `上行`

回复消息格式:

```json
{
"timestamp":1601196762389, //毫秒时间戳
"messageId":"与下行消息中的messageId相同",
"properties":{"color":"red"}, //设置成功后的属性,可不返回
"success":true
}
```

### 设备属性上报

topic: `/report-property`

方向: `上行`

消息格式: 

```json
{
"deviceId":"设备id",
"properties":{"temp":36.8} //上报数据
}
```

### 调用设备功能
 
topic: `/invoke-function`

方向: `下行`

消息格式: 

```json
{
"timestamp":1601196762389, //毫秒时间戳
"messageId":"消息ID",
"deviceId":"设备ID",
"function":"playVoice",//功能ID
"inputs":[{"name":"text","value":"播放声音"}] //参数
}
```

         
回复Topic: `/invoke-function`
      
方向: `上行`

消息格式:

```json
{
"timestamp":1601196762389, //毫秒时间戳
"messageId":"与下行消息中的messageId相同",
"output":"success", //返回执行结果,具体类型与物模型中功能输出类型一致
"success":true,
}
```    

### 设备事件上报
     
topic: `/dev_msg`
      
方向: `上行`

消息格式: 

```json
{
"timestamp":1601196762389, //毫秒时间戳
"messageId":"随机消息ID",
"data":100 //上报数据,类型与物模型事件中定义的类型一致
}
```

### 更新标签消息

topic: `/tags`

方向`上行`,更新平台中的设备标签数据

消息格式:
```json
{
    "timestamp":1601196762389, //毫秒时间戳
    "tags":{
        "key":"value",
        "key2":"value2"
    }
}
```


### 上报更新固件进度

topic: `/firmware/progress`

方向`上行`,上报更新固件进度.

详细格式:
```json
{
    "timestamp":1601196762389, //毫秒时间戳
    "progress":50,//进度,0-100
    "complete":false, //是否完成更新
    "version":"升级的版本号",
    "success":true,//是否更新成功,complete为true时有效
    "errorReason":"失败原因",
    "firmwareId":"固件ID"
}
```

### 拉取固件更新

topic: `/firmware/pull`

方向`上行`,拉取平台的最新固件信息.

详细格式:
```json
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"消息ID",//回复的时候会回复相同的ID
    "currentVersion":"",//当前版本,可以为null
    "requestVersion":"", //请求更新版本,为null或者空字符则为最新版本
}
```



### 上报固件版本

topic: `/firmware/report`

方向`下行`,设备向平台上报固件版本.

详细格式:
```json
{
    "timestamp":1601196762389, //毫秒时间戳
    "version":"版本号"
}
```



