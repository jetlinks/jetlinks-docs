# 使用websocket订阅平台相关消息

在`1.1`版本后提供websocket方式订阅平台消息的功能.
可以通过websocket来订阅设备,规则引擎,设备告警等相关消息.

## 接口

websocket统一接口为: `/messaging/{token}`,
`{token}`可通过登录系统或者使用OpenAPI获取. 

以前端js为例:
```js
var ws = new WebSocket("ws://localhost:8848/messaging/a872d8e6cf6ccd38deb0c8772f6040e3");
ws.onclose=function(e){console.log(e)};
ws.onmessage=function(e){console.log(e.data)}

// 如果认证失败,会立即返回消息: {"message":"认证失败","type":"authError"},并断开连接.
```

## 订阅消息

向websocket发送消息,格式为:

```js
{
    "type": "sub", //固定为sub
    "topic": "/device/*/*/**", // topic,见topic列表.
    "parameter": {       //参数,不同的订阅请求,支持的参数不同
        
    },
    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.
}
```

::: waring 注意
在取消订阅之前,多次传入相同的id是无效的,不会重复订阅.
:::

平台推送消息:

```js
{
	"payload": //消息内容, topic不同,内容不同,
	"requestId": "request-id", //与订阅请求的id一致
	"topic": "/device/demo-device/test0/offline", //topic,实际产生数据的topic
	"type": "result" //类型 result:订阅结果 complete:结束订阅 error:发生错误 
}
```

::: tip 提示

type为complete时标识本此订阅已结束,通常是订阅有限数据流时(比如发送设备指令),或者取消订阅时会返回此消息.

:::

## 取消订阅

向websocket发送消息,格式为:

```js
{
    "type":"unsub",//固定为unsub
     "id": "request-id" //与订阅请求ID一致
}
```

## 订阅设备消息

与消息网关中的设备topic一致,[查看topic列表](./subscribe-device-message.md#设备消息).
消息内容(`payload`)将与[设备消息类型](../basics-guide/protocol-support.md#平台统一消息定义)一致.


## 发送设备指令

发送消息到websocket

```js
{
    "type": "sub", //固定为sub
    "topic": "/device-message-sender/demo-device/test0,test1", // 发送消息给demo-device型号下的test0和test1设备
    "parameter": {
        // 消息类型,支持: READ_PROPERTY (读取属性),WRITE_PROPERTY (修改属性),INVOKE_FUNCTION (调用功能)
        "messageType":"READ_PROPERTY" 
        //根据不同的消息,参数也不同. 具体见: 平台统一消息定义
        "properties":["temperature"],
        //头信息
        "headers":{
            "async":false // 是否异步,异步时,平台不等待设备返回指令结果.
        }
    },
    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.
}
```

平台将推送设备返的结果:

```js
{
	"payload": {   //请求消息类型不同,结果不同
		"deviceId": "test0",
        "messageType": "READ_PROPERTY_REPLY",
        "success":true, //指令是否成功
		"properties": {
			"temperature": 28.21
		},
		"timestamp": 1588148129787
	},
	"requestId": "request-id", //订阅请求的ID
	"topic": "/device/demo-device/test7/offline",
	"type": "result"
}
```

::: TIP 提示

`deviceId`支持`*`和逗号`,`分割,批量发送消息到设备.如: `/device-message-sender/{productId}/{deviceId}`.
如果要终止发送,直接取消订阅即可.

:::



## dashboard

订阅仪表盘数据:

topic: `/dashboard/{dashboard}/{object}/{measurement}/{dimension}`

```js
{
    "type": "sub", //固定为sub
    "topic": "/dashboard/device/demo-device/property/agg", //聚合查询属性
    "parameter": {
        "deviceId":"test0", //
        "limit":"30",
        "time":"1d",
        "agg":"avg",
        "from":"now-30d",
        "to":"now",
        "format":"MM月dd日"
    },
    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.
}
```

::: tip
详细使用见[Dashboard说明](./dashboard.md)
:::
