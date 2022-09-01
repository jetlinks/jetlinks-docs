# JetLinks 官方协议

除了使用自定义协议以外,jetlinks提供了默认的协议支持.
设备可以使用此协议接入平台. 设备协议已经确定并且无法修改协议的时候,建议使用[自定义协议接入](../dev-guide/protocol-support.md)

<a target='_blank' href='https://github.com/jetlinks/jetlinks-official-protocol'>查看源码</a>

## 官方协议topic主题说明

<table class='table'>
        <thead>
            <tr>
              <td>名词</td>
              <td>解释</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>上行topic</td>
            <td>设备端向平台发送</td>
          </tr>
          <tr>
            <td>下行topic</td>
            <td>平台向设备端发送</td>
          </tr>
        </tbody>
      </table>


## Topic列表
以下是官方协议包中，设备上报和平台下发数据的topic

- ### 下行Topic

<table class='table'>
        <thead>
            <tr>
              <td>设备类型</td>
              <td>名称</td>
              <td>topic</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td rowspan="3">直连</td>
            <td>读取设备属性</td>
            <td>/{productId}/{deviceId}/properties/read</td>
          </tr>
          <tr>
            <td>修改设备属性</td>
            <td>/{productId}/{deviceId}/properties/write</td>
          </tr>
          <tr>
            <td>调用设备功能</td>
            <td>/{productId}/{deviceId}/function/invoke</td>
          </tr>
          <tr>
            <td rowspan="3">网关</td>
            <td>读取子设备属性</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/properties/read</td>
          </tr>
          <tr>
            <td>修改子设备属性</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/properties/write</td>
          </tr>
          <tr>
            <td>调用子设备功能</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/function/invoke</td>
          </tr>
        </tbody>
      </table>

- ### 上行Topic

<table class='table'>
        <thead>
            <tr>
              <td>设备类型</td>
              <td>名称</td>
              <td>topic</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td rowspan="6">直连</td>
            <td>读取属性回复</td>
            <td>/{productId}/{deviceId}/properties/read/reply</td>
          </tr>
          <tr>
            <td>修改属性回复</td>
            <td>/{productId}/{deviceId}/properties/write/reply</td>
          </tr>
          <tr>
            <td>调用设备功能回复</td>
            <td>/{productId}/{deviceId}/function/invoke/reply</td>
          </tr>
          <tr>
            <td>上报设备事件</td>
            <td>/{productId}/{deviceId}/event/{eventId}</td>
          </tr>
          <tr>
            <td>上报设备属性</td>
            <td>/{productId}/{deviceId}/properties/report</td>
          </tr>
          <tr>
            <td>上报设备派生物模型</td>
            <td>/{productId}/{deviceId}/metadata/derived</td>
          </tr>
          <tr>
            <td rowspan="7">网关</td>
            <td>子设备上线消息</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/connected</td>
          </tr>
          <tr>
            <td>子设备下线消息</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/disconnect</td>
          </tr>
          <tr>
            <td>读取子设备属性回复</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/properties/read/reply</td>
          </tr>
          <tr>
            <td>修改子设备属性回复</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/properties/write/reply</td>
          </tr>
          <tr>
            <td>调用子设备功能回复</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/function/invoke/reply</td>
          </tr>
          <tr>
            <td>上报子设备事件</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/event/{eventId}</td>
          </tr>
          <tr>
            <td>上报子设备派生物模型</td>
            <td>/{productId}/{deviceId}/child/{childDeviceId}/metadata/derived</td>
          </tr>
        </tbody>
      </table>


## MQTT接入

目前支持MQTT3.1.1和3.1版本协议.

### 认证

CONNECT报文:

```text
clientId: 设备实例ID
username: secureId+"|"+timestamp
password: md5(secureId+"|"+timestamp+"|"+secureKey)
```

<a href="/basics-guide/mqtt-auth-generator.html" target="_blank">在线生成工具</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <li>`secureId`以及`secureKey`在创建设备产品和设备实例时进行配置.</li>
  <li>`timestamp`为当前系统时间戳(毫秒),与系统时间不能相差5分钟.</li>

</div>


### 读取设备属性
    
topic: `/{productId}/{deviceId}/properties/read`

方向: `下行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"消息ID",
    "deviceId":"设备ID",
    "properties":["sn","model"] //要读取到属性列表
}
```
            
回复Topic: `/{productId}/{deviceId}/properties/read/reply`

回复消息格式:
    
- 成功
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"与下行消息中的messageId相同",
    "properties":{"sn":"test","model":"test"}, //key与设备模型中定义的属性id一致
    "deviceId":"设备ID",
    "success":true
}
```

- 失败
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"与下行消息中的messageId相同",
    "success":false,
    "code":"error_code",
    "message":"失败原因"
}
```

### 修改设备属性
    
topic: `/{productId}/{deviceId}/properties/write`

方向: `下行`

消息格式: 
    
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"消息ID",
    "deviceId":"设备ID",
    "properties":{"color":"red"} //要设置的属性
}
```

回复Topic: `/{productId}/{deviceId}/properties/write/reply`

方向: `上行`

回复消息格式:

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"与下行消息中的messageId相同",
    "properties":{"color":"red"}, //设置成功后的属性,可不返回
    "success":true
}
```

### 设备属性上报

topic: `/{productId}/{deviceId}/properties/report`

方向: `上行`

消息格式: 

```json5
{
    "deviceId":"设备id",
    "properties":{"temp":36.8} //上报数据
}
```

### 调用设备功能
 
topic: `/{productId}/{deviceId}/function/invoke`

方向: `下行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"消息ID",
    "deviceId":"设备ID",
    "function":"playVoice",//功能ID
    "inputs":[{"name":"text","value":"播放声音"}] //参数
}
```

         
回复Topic: `/{productId}/{deviceId}/function/invoke/reply`
      
方向: `上行`

消息格式:

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"与下行消息中的messageId相同",
    "output":"success", //返回执行结果,具体类型与物模型中功能输出类型一致
    "success":true
}
```    

### 设备事件上报
     
topic: `/{productId}/{deviceId}/event/{eventId}`
      
方向: `上行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"随机消息ID",
    "data":100 //上报数据,类型与物模型事件中定义的类型一致
}
```

### 子设备注册

与子设备消息配合使用,实现设备与网关设备进行自动绑定.
     
topic: `/{productId}/{deviceId}/child/{childDeviceId}/register`
      
方向: `上行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"随机消息ID",
    "deviceId":"子设备ID",
    "headers":{
        "productId":"子设备在平台的产品ID",
        "deviceName":"子设备名称",
        "configuration":{
            "selfManageState":true //子设备自己管理状态(默认false).为true时,平台将发送DeviceCheckMessage到网关来检查子设备状态
        }
     }
}
```

### 子设备注销

与子设备消息配合使用,实现设备与网关设备进行自动解绑.
     
topic: `/{productId}/{deviceId}/child/{childDeviceId}/unregister`
      
方向: `上行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"随机消息ID",
    "deviceId":"子设备ID"
}
```

### 子设备上线

与子设备消息配合使用,实现关联到网关的子设备上线.(默认情况下,网关上线,子设备也会全部自动上线.)
     
topic: `/{productId}/{deviceId}/child/{childDeviceId}/online`
      
方向: `上行`

消息格式: 

```json5
{
    "deviceId":"子设备ID", //毫秒时间戳
    "messageId":"随机消息ID",
    "timestamp":1584331469964 //时间戳
}
```


### 子设备离线

与子设备消息配合使用,实现关联到网关的子设备离线.(默认情况下,网关离线,子设备也会全部自动离线.)
     
topic: `/{productId}/{deviceId}/child/{childDeviceId}/offline`
      
方向: `上行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"随机消息ID"
}
```

### 断开子设备连接

平台主动断开设备连接时，会发送此指令到网关
     
topic: `/{productId}/{deviceId}/child/{childDeviceId}/disconnect`
      
方向: `下行`

消息格式: 

```json5
{
    "deviceId":"子设备ID", //子设备ID
    "messageId":"随机消息ID",
    "timestamp":1584331469964 //时间戳
}
```

### 断开子设备连接回复

平台主动断开设备连接时，会发送此指令到网关
     
topic: `/{productId}/{deviceId}/child-reply/{childDeviceId}/disconnect/reply`
      
方向: `上行`

消息格式: 

```json5
{
    "deviceId":"子设备ID", //子设备ID
    "messageId":"断开连接指令的ID",
    "timestamp":1584331469964 //时间戳
}
```

### 子设备状态检查

在查看子设备详情或者检查设备状态时，将会收到此消息
     
topic: `/{productId}/{deviceId}/child/{childDeviceId}/state-check`
      
方向: `下行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "deviceId":"子设备ID", //子设备ID
    "messageId":"随机消息ID"
}
```

### 子设备状态检查回复

在查看子设备详情或者检查设备状态时，将会收到此消息
     
topic: `/{productId}/{deviceId}/child-reply/{childDeviceId}/state-check/reply`
      
方向: `上行`

消息格式: 

```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "deviceId":"子设备ID", //子设备ID
    "messageId":"状态检查指令的消息ID",
    "state":1 // 1在线，-1离线
}
```

### 子设备消息
     
topic: `/{productId}/{deviceId}/child/{childDeviceId}/{topic}`
      
方向: `上行或下行`, 根据{topic}决定.

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

{topic} 以及数据格式与设备topic定义一致. 如: 获取子设备属性: `/1/d1/child/c1/properties/read`,

</div>

### 子设备指令回复消息
     
topic: `/{productId}/{deviceId}/child-reply/{childDeviceId}/{topic}`
      
方向: `上行`, 根据{topic}决定.

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

{topic} 以及数据格式与设备topic定义一致. 如: 获取子设备属性回复: `/1/d1/child-reply/c1/properties/read/reply`,

</div>

### 更新标签消息

topic: `/{productId}/{deviceId}/tags`

方向`上行`,更新平台中的设备标签数据

消息格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "tags":{
        "key":"value",
        "key2":"value2"
    }
}
```

### 更新固件消息

topic: `/{productId}/{deviceId}/firmware/upgrade`

方向`下行`,更新设备固件.

详细格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "url":"固件文件下载地址",
    "version":"版本号",
    "parameters":{},//其他参数
    "sign":"文件签名值",
    "signMethod":"签名方式",
    "firmwareId":"固件ID",
    "size":100000//文件大小,字节
}
```

### 更新固件消息回复

topic: `/{productId}/{deviceId}/firmware/upgrade/reply`

方向`上行`。根据`远程升级`设定的超时时间，若超过时间未回复，任务将失败。

详细格式:
```json5
{
    "success":true,
    "messageId":"1551807719374848000",
    "timestamp":1658213956066
}
```

### 上报更新固件进度

topic: `/{productId}/{deviceId}/firmware/upgrade/progress`

方向`上行`,上报更新固件进度.

详细格式:
```json5
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

topic: `/{productId}/{deviceId}/firmware/pull`

方向`上行`,拉取平台的最新固件信息.

详细格式:
```json5
{
    "headers": {
      "force":true, //是否强制拉取
      "latest":true //是否拉取最新固件
    },
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"消息ID",//回复的时候会回复相同的ID
    "currentVersion":"",//当前版本,可以为null
    "requestVersion":"", //请求更新版本,为null或者空字符则为最新版本
}
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

拉取固件信息中，可添加header参数。
  
header.force为false时，只会请求等待中状态的任务。header.force为true时，会请求所有状态的任务。即失败任务也会被重试。

header.latest为false时，将会请求版本序号（versionOrder）最小的固件。header.latest为true时，将会请求版本序号（versionOrder）最大的固件。

</div>


### 拉取固件更新回复

topic: `/{productId}/{deviceId}/firmware/pull/reply`

方向`下行`,平台回复拉取的固件信息.

详细格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"请求的ID",
    "url":"固件文件下载地址",
    "version":"版本号",
    "parameters":{},//其他参数
    "sign":"文件签名值",
    "signMethod":"签名方式",
    "firmwareId":"固件ID",
    "size":100000 //文件大小,字节
}
```

### 上报固件版本

topic: `/{productId}/{deviceId}/firmware/report`

方向`下行`,设备向平台上报固件版本.

详细格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "version":"版本号"
}
```

### 获取固件版本

topic: `/{productId}/{deviceId}/firmware/read`

方向`下行`,平台读取设备固件版本

详细格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"消息ID"
}
```

### 获取固件版本回复

topic: `/{productId}/{deviceId}/firmware/read`

方向`上行`,设备回复平台读取设备固件版本指令

详细格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "messageId":"读取指令中的消息ID",
    "version":""//版本号
}
```

### 派生物模型上报

topic: `/{productId}/{deviceId}/metadata/derived`

方向`上行`,设备上报新的物模型信息

详细格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "metadata":"物模型json字符",
    "all":false //是否全量更新
}
```

### 设备日志上报

topic: `/{productId}/{deviceId}/log`

方向`上行`,设备上报新的物模型信息

详细格式:
```json5
{
    "timestamp":1601196762389, //毫秒时间戳
    "log":"日志内容"
}
```
<a href='/function-description/metadata_description.html'>查看物模型格式</a>

### 透传消息

topic: `/{productId}/{deviceId}/direct`

方向`上行`,透传设备消息,将报文传入`mqtt payload`中


### 时间同步
 
topic: `/{productId}/{deviceId}/time-sync`

方向`上行`,用于同步服务器的时间.格式:
```json5
{
    "messageId":"消息ID"
}
```
平台回复:

 topic: `/{productId}/{deviceId}/time-sync/reply`

方向`下行`,同步服务器的时间回复.格式:
```json5
{
    "messageId":"消息ID",
    "timestamp":1601196762389 //UTC毫秒时间戳
}
```


## CoAP接入
使用CoAP协议接入仅需要对数据进行加密即可.加密算法: AES/ECB/PKCS5Padding.

将请求体进行加密,密钥为在创建设备产品和设备实例时进行配置的(`secureKey`).

请求地址(`URI`)与MQTT `Topic`相同.消息体(`payload`)与MQTT相同(只支持上行消息).

