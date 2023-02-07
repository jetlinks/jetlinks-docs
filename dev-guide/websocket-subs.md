[comment]: <> (# 使用websocket订阅平台相关消息)

[comment]: <> (在`1.1`版本后提供websocket方式订阅平台消息的功能.)

[comment]: <> (可以通过websocket来订阅设备,规则引擎,设备告警等相关消息.)

[comment]: <> (## 接口)

[comment]: <> (websocket统一接口为: `/messaging/{token}`,)

[comment]: <> (`{token}`可通过登录系统或者使用OpenAPI获取.)

[comment]: <> (以前端js为例:)

[comment]: <> (```js)

[comment]: <> (var ws = new WebSocket&#40;"ws://localhost:8848/messaging/a872d8e6cf6ccd38deb0c8772f6040e3"&#41;;)

[comment]: <> (ws.onclose=function&#40;e&#41;{console.log&#40;e&#41;};)

[comment]: <> (ws.onmessage=function&#40;e&#41;{console.log&#40;e.data&#41;})

[comment]: <> (// 如果认证失败,会立即返回消息: {"message":"认证失败","type":"authError"},并断开连接.)

[comment]: <> (```)

[comment]: <> (## 订阅消息)

[comment]: <> (向websocket发送消息,格式为:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type": "sub", //固定为sub)

[comment]: <> (    "topic": "/device/*/*/**", // topic,见topic列表.)

[comment]: <> (    "parameter": {       //参数,不同的订阅请求,支持的参数不同)
        
[comment]: <> (    },)

[comment]: <> (    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (<div class='explanation warning'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-jinggao explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>注意</span>)

[comment]: <> (  </p>)

[comment]: <> (在取消订阅之前,多次传入相同的id是无效的,不会重复订阅.)

[comment]: <> (</div>)


[comment]: <> (平台推送消息:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (	"payload": //消息内容, topic不同,内容不同,)

[comment]: <> (	"requestId": "request-id", //与订阅请求的id一致)

[comment]: <> (	"topic": "/device/demo-device/test0/offline", //topic,实际产生数据的topic)

[comment]: <> (	"type": "result" //类型 result:订阅结果 complete:结束订阅 error:发生错误 )

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (<div class='explanation primary'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-bangzhu explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>提示</span>)

[comment]: <> (  </p>)

[comment]: <> (type为complete时标识本此订阅已结束,通常是订阅有限数据流时&#40;比如发送设备指令&#41;,或者取消订阅时会返回此消息.)

[comment]: <> (</div>)

[comment]: <> (## 取消订阅)

[comment]: <> (向websocket发送消息,格式为:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type":"unsub",//固定为unsub)

[comment]: <> (     "id": "request-id" //与订阅请求ID一致)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (## 订阅设备消息)

[comment]: <> (与消息网关中的设备topic一致,[查看topic列表]&#40;../function-description/device_message_description.md#设备消息对应事件总线topic&#41;.)

[comment]: <> (消息负载&#40;`payload`&#41;将与[设备消息类型]&#40;../function-description/device_message_description.md#平台统一设备消息定义&#41;一致.)

[comment]: <> (## 发送设备指令)

[comment]: <> (发送消息到websocket)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type": "sub", //固定为sub)

[comment]: <> (    "topic": "/device-message-sender/demo-device/test0,test1", // 发送消息给demo-device型号下的test0和test1设备)

[comment]: <> (    "parameter": {)

[comment]: <> (        // 消息类型,支持: READ_PROPERTY &#40;读取属性&#41;,WRITE_PROPERTY &#40;修改属性&#41;,INVOKE_FUNCTION &#40;调用功能&#41;)

[comment]: <> (        "messageType":"READ_PROPERTY" )

[comment]: <> (        //根据不同的消息,参数也不同. 具体见: 平台统一消息定义)

[comment]: <> (        "properties":["temperature"],)

[comment]: <> (        //头信息)

[comment]: <> (        "headers":{)

[comment]: <> (            "async":false // 是否异步,异步时,平台不等待设备返回指令结果.)

[comment]: <> (        })

[comment]: <> (    },)

[comment]: <> (    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (平台将推送设备返的结果:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (	"payload": {   //请求消息类型不同,结果不同)

[comment]: <> (		"deviceId": "test0",)

[comment]: <> (        "messageType": "READ_PROPERTY_REPLY",)

[comment]: <> (        "success":true, //指令是否成功)

[comment]: <> (		"properties": {)

[comment]: <> (			"temperature": 28.21)

[comment]: <> (		},)

[comment]: <> (		"timestamp": 1588148129787)

[comment]: <> (	},)

[comment]: <> (	"requestId": "request-id", //订阅请求的ID)

[comment]: <> (	"topic": "/device/demo-device/test7/offline",)

[comment]: <> (	"type": "result")

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (<div class='explanation primary'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-bangzhu explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>提示</span>)

[comment]: <> (  </p>)

[comment]: <> (`deviceId`支持`*`和逗号`,`分割,批量发送消息到设备.如: `/device-message-sender/{productId}/{deviceId}`.)

[comment]: <> (如果要终止发送,直接取消订阅即可.)

[comment]: <> (</div>)

[comment]: <> (## 批量同步设备状态)

[comment]: <> (发送消息到websocket)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type": "sub", //固定为sub)

[comment]: <> (    "topic": "/device-batch/state-sync",)

[comment]: <> (    "parameter": {)

[comment]: <> (        "query":{"where":"productId is test-device"}//查询条件为动态查询条件)

[comment]: <> (    },)

[comment]: <> (    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (平台推送:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (	"payload": {   //请求消息类型不同,结果不同)

[comment]: <> (		"deviceId": "test0",)

[comment]: <> (        "state": {)

[comment]: <> (            "value":"offline",)

[comment]: <> (            "text":"离线")

[comment]: <> (        })

[comment]: <> (	},)

[comment]: <> (	"requestId": "request-id", //订阅请求的ID)

[comment]: <> (	"topic": "/device-batch/state-sync",)

[comment]: <> (	"type": "result" //为comlete是则表示同步完成.)

[comment]: <> (})

[comment]: <> (```)


[comment]: <> (## dashboard)

[comment]: <> (订阅仪表盘数据:)

[comment]: <> (topic: `/dashboard/{dashboard}/{object}/{measurement}/{dimension}`)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type": "sub", //固定为sub)

[comment]: <> (    "topic": "/dashboard/device/demo-device/property/agg", //聚合查询属性)

[comment]: <> (    "parameter": {)

[comment]: <> (        "deviceId":"test0", //)

[comment]: <> (        "limit":"30",)

[comment]: <> (        "time":"1d",)

[comment]: <> (        "agg":"avg",)

[comment]: <> (        "from":"now-30d",)

[comment]: <> (        "to":"now",)

[comment]: <> (        "format":"MM月dd日")

[comment]: <> (    },)

[comment]: <> (    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (<div class='explanation primary'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-bangzhu explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>说明</span>)

[comment]: <> (  </p>)

[comment]: <> (详细使用见[Dashboard说明]&#40;./dashboard.md&#41;)

[comment]: <> (</div>)


[comment]: <> (## 订阅引擎事件数据)

[comment]: <> (发送消息到websocket)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type": "sub", //固定为sub)

[comment]: <> (    "topic": "/rule-engine/{instanceId}/{nodeId}/event/{event}",)

[comment]: <> (    "parameter": {},)

[comment]: <> (    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (平台推送:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (	"payload": {   )

[comment]: <> (	 //规则数据,不同的节点和事件类型数据不同)

[comment]: <> (	},)

[comment]: <> (	"requestId": "request-id", //订阅请求的ID)

[comment]: <> (	"topic": "/rule-engine/{instanceId}/{nodeId}/event/{event}",)

[comment]: <> (	"type": "result" //为comlete是则表示订阅结束.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (<div class='explanation primary'>)

[comment]: <> (  <p class='explanation-title-warp'>)

[comment]: <> (    <span class='iconfont icon-bangzhu explanation-icon'></span>)

[comment]: <> (    <span class='explanation-title font-weight'>event说明</span>)

[comment]: <> (  </p>)

[comment]: <> (**error**: 执行节点错误)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    message:"错误消息",)

[comment]: <> (    stack:"异常栈信息",)

[comment]: <> (    type::"错误类型")

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (**result**: 节点数据输出)

[comment]: <> (**complete**: 执行节点完成)

[comment]: <> (</div>)

[comment]: <> (## 订阅设备告警数据)

[comment]: <> (发送消息到websocket)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type": "sub", //固定为sub)

[comment]: <> (    "topic": "/rule-engine/device/alarm/{productId}/{deviceId}/{alarmId}",)

[comment]: <> (    "parameter": {},)

[comment]: <> (    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (平台推送:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (	"payload": {   //告警相关数据)

[comment]: <> (		"deviceId": "设备ID",)

[comment]: <> (        "deviceName": "设备名称",)

[comment]: <> (        "alarmId": "告警ID",)

[comment]: <> (        "alarmName": "告警名称")

[comment]: <> (        //...其他告警数据)

[comment]: <> (	},)

[comment]: <> (	"requestId": "request-id", //订阅请求的ID)

[comment]: <> (	"topic": "/rule-engine/device/alarm/{productId}/{deviceId}/{alarmId}",)

[comment]: <> (	"type": "result" //为comlete是则表示订阅结束.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (## 订阅场景联动)

[comment]: <> (发送消息到websocket)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (    "type": "sub", //固定为sub)

[comment]: <> (    "topic": "/scene/{alarmId}",)

[comment]: <> (    "parameter": {},)

[comment]: <> (    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.)

[comment]: <> (})

[comment]: <> (```)

[comment]: <> (平台推送:)

[comment]: <> (```js)

[comment]: <> ({)

[comment]: <> (	"payload": {   //触发场景的数据内容,触发方式不同,数据格式不同)
        
[comment]: <> (	},)

[comment]: <> (	"requestId": "request-id", //订阅请求的ID)

[comment]: <> (	"topic": "/scene/{alarmId}",)

[comment]: <> (	"type": "result" //为comlete是则表示同步完成.)

[comment]: <> (})

[comment]: <> (```)