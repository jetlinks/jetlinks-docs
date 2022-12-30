# 订阅平台相关消息

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        需要将平台的数据转发到自己的业务系统，可以订阅平台的特定主题获取
    </p>
</div>


<br>

#### 订阅相关主题

| 匹配Topic                                                    | 说明                |
| ------------------------------------------------------------ | ------------------- |
| `/network/coap/client/*/_send`<br>`/network/coap/server/*/_subscribe` | CoAP调试相关消息    |
| `/dashboard/**`                                              | 仪表盘相关消息      |
| `/rule-engine/device/alarm/*/*/*`                            | 设备告警相关消息    |
| `/device-batch/*`                                            | 设备批量操作        |
| `/device-current-state`                                      | 设备当前状态消息    |
| `/device-firmware/publish`                                   | 推送设备固件更新    |
| `/device-message-sender/*/*`                                 | 设备消息发送        |
| `/device/*/*/**`                                             | 订阅设备消息        |
| `/virtual-property-debug`                                    | 虚拟属性调试        |
| `/network/http/client/*/_send`<br>`/network/http/server/*/_subscribe` | HTTP调试            |
| `/network/mqtt/client/*/_subscribe/*`<br>`/network/mqtt/client/*/_publish/*` | MQTT客户端调试      |
| `/network/mqtt/server/*/_subscribe/*`                        | MQTT服务调试        |
| `/notifications`                                             | 通知推送            |
| `/rule-engine/**`                                            | 规则引擎            |
| `/scene/*`                                                   | 场景联动事件        |
| `/network/simulator/**`                                      | 模拟器消息订阅      |
| `/network/tcp/client/*/_send`<br>`/network/tcp/client/*/_subscribe` | TCP客户端调试       |
| `/network/tcp/server/*/_subscribe`                           | TCP服务调试         |
| `/network/udp/*/_send`<br>`/network/udp/*/_subscribe`        | UDP调试             |
| `/network/websocket/client/*/_subscribe/*`<br>`/network/websocket/client/*/_publish/*` | WebSocket客户端调试 |
| `/network/websocket/server/*/_subscribe`                     | WebSocket服务调试   |

#### 如何连接到平台

1、使用websocket进行连接

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        websocket统一接口为：<code>/messaging/{token}</code>， <code>{token}</code>可通过登录系统或者使用OpenAPI获取。
    </p>
</div>


此处以JavaScript代码为例，`token`可以在已经登录了的JetLinks平台内打开控制台->网络->Fetch/XHR选择其中任意一个请求查看`X-Access-Token`请求头获取

```javascript
// 如果认证失败,会立即返回消息: {"message":"认证失败","type":"authError"},并断开连接
let ws = new WebSocket("ws://localhost:8848/messaging/19fdfae14177f5eb44ba59d66670d252");
ws.onclose=function(e){console.log(e)};
ws.onmessage=function(e){console.log(e.data)}
```

<br>

2、使用MQTT进行连接

开启配置

```yaml
messaging:
  mqtt:
    enabled: true #开启mqtt支持
    port: 11883 # 端口
    host: 0.0.0.0 #绑定网卡
```

默认使用`token`(可以使用`OpenAPI`申请token)作为`clientId`，连接地址为JetLinks平台部署的ip地址，端口11883，`username`和`password`可以不填写。可通过实现接口`MqttAuthenticationHandler`来自定义认证策略.

<br>

#### 开始订阅消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        当平台内部推送消息到某个主题上与订阅的主题相匹配才能接收到消息，type为complete时标识本此订阅已结束，通常是订阅有限数据流时（比如发送设备指令），或者取消订阅时会返回此消息。
    </p>
</div>

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>
	<p>
        如果订阅时使用通配符，可能会收到大量的消息，请保证消息的处理速度，否则会影响系统消息吞吐量。在取消订阅之前，多次传入相同的id是无效的，不会重复订阅。
    </p>
</div>

<br>

#### 订阅CoAP调试相关消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        订阅此主题分为CoAP客户端和CoAP服务端，当订阅主题以<code>/network/coap/server</code>开头时订阅为CoAP服务，否则为CoAP客户端
    </p>
</div>


订阅CoAP服务

```javascript
	//平台网络组件中想要订阅的CoAP服务组件的id
    //此id可以在浏览器中打开控制台->network->Fetch/XHR,之后F5刷新页面在左侧找到_query/的请求中查看到
    //也可以去数据库中的jetlinks数据库下的network_config表中查找
    "topic": "/network/coap/server/{id}/_subscribe"
```

订阅CoAP客户端

```json
//获取id方式同上
"topic": "/network/coap/client/{id}/_send"
```

<br>

#### 订阅仪表盘相关消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        <code>dashboard</code>仪表盘：一类监控数据的分类，例如: 系统监控，设备监控。<br><code>object</code>仪表盘对象，例如: CPU，内存，设备状态等。<br><code>measurement</code>监控指标，例如: CPU使用率，设备在线状态等。<br><code>dimension</code>指标维度，例如: 实时CPU使用率，设备历史状态等。
    </p>
</div>


```json
"topic": "/dashboard/{dashboard}/{object}/{measurement}/{dimension}"
```

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>
	<p>
        选择订阅主题时确认表格中某一格内数据的展示顺序，如果是竖着表示只能选择其中的一个，如果是横着则表示前一个维度的值可以选取当前格内的任意一个。
    </p>
</div>
可以订阅的主题有


| 仪表盘(dashboard) | 仪表盘对象(object) | 监控指标(measurement)                                        | 指标维度(dimension) | 说明         |
| ----------------- | ------------------ | ------------------------------------------------------------ | ------------------- | ------------ |
| `gatewayMonitor`  | `deviceGateway`    | `connected`<br>`disconnected`<br>`sent_message`<br>`rejected`<br>`connection`<br>`received_message` | `agg`,`history`     | 网关消息     |
| `jvmMonitor`      | `memory`<br>`cpu`  | `info`<br>`usage`                                            | `realTime`          | jvm信息      |
| `systemMonitor`   | `memory`<br>`cpu`  | `info`<br>`usage`                                            | `realTime`          | 系统信息     |
| `device`          | `session`          | `online`                                                     | `agg`               | 设备在线信息 |
| `network`         | `traffic`          | `traffic`                                                    | `network`           | 网络信息     |

<br>

#### 订阅设备告警相关消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        设备告警相关信息
    </p>
</div>

```json
//productId产品id，deviceId设备id，alarmId设备告警id
"topic": "/rule-engine/device/alarm/{productId}/{deviceId}/{alarmId}"
```

<br>

#### 订阅设备批量操作消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        <code>type</code>类型可以为<code>state-sync</code>(批量同步设备状态)或者<code>deploy</code>(批量发布设备)
    </p>
</div>

```json
"topic": "/device-batch/{type}"
```

<br>

#### 订阅设备当前状态消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        订阅某一个设备的状态信息
    </p>
</div>


```json
{
    //固定sub
    "type": "sub",
    //固定topic
    "topic": "/device-current-state",
    "parameter": {
        //必须指定设备id
        "deviceId":"deviceId"
    },
    //本次请求的id
    "id": "request-id"
}
```

<br>

#### 订阅推送设备固件更新消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        websocket订阅，<code>taskId</code>可以到数据库中的<code>jetlinks</code>数据库下的<code>dev_firmware_upgrade_task</code>表中找到
    </p>
</div>

```json
{
    //固定sub
    "type": "sub",
    //固定topic
    "topic": "/device-current-state",
    "parameter": {
        //必须指定任务id
        "taskId":"taskId"
    },
    //本次请求的id
    "id": "request-id"
}
```

<br>

#### 订阅设备消息发送

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        websocket订阅，<code>deviceId</code>可以传入多个例如device1,device2，也可以使用通配符*，如果设备id使用通配符则<code>productId</code>不能进行通配，<code>parameter</code>内的<code>messageType</code>可以为<code>READ_PROPERTY</code>(读取属性)，<code>WRITE_PROPERTY</code>(修改属性)，<code>INVOKE_FUNCTION</code>(调用功能)
    </p>
</div>

```json
{
    //固定为sub
    "type": "sub", 
    "topic": "/device-message-sender/{productId}/{deviceId}", 
    "parameter": {
        // 消息类型
        "messageType":"READ_PROPERTY",
        //根据不同的消息,参数也不同. 具体见: 平台统一消息定义
        "properties":["temperature"],
        //头信息
        "headers":{
    		// 是否异步,异步时,平台不等待设备返回指令结果
            "async":false 
        }
    },
	//请求id, 请求的标识,服务端在推送消息时,会将此标识一并返回
    "id": "request-id" 
}
```

<br>

#### 订阅设备消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
与消息网关中的设备topic一致，(<a href='http://doc.jetlinks.cn/function-description/device_message_description.html#设备消息对应事件总线topic' target='blank'>查看topic列表</a>)。 消息负载(<code>payload</code>)将与<a  href='http://doc.jetlinks.cn/function-description/device_message_description.html#平台统一设备消息定义' target='blank'>设备消息类型</a>一致。
    </p>
</div>


以订阅产品为`product_test`下的设备`device_test_01`的属性上报信息为例

向websocket发送订阅信息

```json
{
    "type": "sub",
    "topic": "/device/product_test/device_test_01/message/property/report",
    "parameter": {},
    "id": "request-id"
}
```

使用<a href='https://mqttx.app/zh' target='blank'>MQTTX</a>模拟设备上报一条属性，`topic`为`/product_test/device_test_01/properties/report`，使用协议为<a href='https://github.com/jetlinks/jetlinks-official-protocol' target='blank'>官方协议</a>的`v3`分支

```json
//MQTT发送的消息体
{
 "properties":{
    "temperature":48.9 //该产品配置的温度属性
 }
}
```

收到平台回复消息：

```json
{
	"payload": {
		"deviceId": "device_test_01",
		"headers": {
			"deviceName": "测试设备01",
			"productId": "product_test",
			"_uid": "1608016343709896704"
		},
		"messageType": "REPORT_PROPERTY",
		"properties": {
			"temperature": 48.9
		},
		"timestamp": 1672215946496
	},
	"requestId": "request-id",
	"topic": "/device/product_test/device_test_01/message/property/report",
	"type": "result"
}
```

<br>

#### 订阅虚拟属性调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        websocket订阅
    </p>
</div>


```json
{
    "type": "sub",
    "topic": "/virtual-property-debug",
    "parameter": {
        "virtualId":"virtualId",
        "properties": {
            
        },
    },
    "id": "request-id"
}
```

<br>

#### 订阅HTTP调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        订阅此主题分为HTTP客户端和HTTP服务端，当订阅主题以<code>/network/http/server</code>开头时订阅为HTTP服务，否则为HTTP客户端
    </p>
</div>


订阅HTTP服务

```javascript
	//平台网络组件中想要订阅的http服务组件的id
    //此id可以在浏览器中打开控制台->network->Fetch/XHR,之后F5刷新页面在左侧找到_query/的请求中查看到
    //也可以去数据库中的jetlinks数据库下的network_config表中查找
    "topic": "/network/http/server/{id}/_subscribe"
```

订阅HTTP客户端

```json
//获取id方式同上
"topic": "/network/coap/client/{id}/_send"
```

<br>

#### 订阅MQTT客户端调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        订阅此主题的消息分为订阅时的消息和推送时的消息两部分，当<code>{pubsub}</code>为<code>_subscribe</code>时是订阅组件在订阅时产生的消息，否则为订阅组件在发布时产生的消息。<code>{id}</code>为MQTT客户端组件的id，<code>{type}</code>类型可以选择为<code>JSON</code>、<code>BINARY</code>、<code>STRING</code>、<code>HEX</code>
    </p>
</div>

```json
"topic": "/network/mqtt/client/{id}/{pubsub}/{type}"
```

<br>

#### 订阅MQTT服务调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
       <code>{id}</code>为MQTT服务组件的id，<code>{type}</code>类型可以选择为<code>JSON</code>、<code>BINARY</code>、<code>STRING</code>、<code>HEX</code>
    </p>
</div>

```json
"topic": "/network/mqtt/server/{id}/_subscribe/{type}"
```

<br>

#### 订阅通知消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
       会根据<code>token</code>去订阅相应用户的通知消息
    </p>
</div>

```json
"topic": "/notifications"
```

<br>

#### 订阅规则引擎执行相关消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
       规则引擎相关
    </p>
</div>
发送消息到websocket

```json
{
    "type": "sub", //固定为sub
    "topic": "/rule-engine/{instanceId}/{nodeId}/event/{event}",
    "parameter": {},
    "id": "request-id" //请求ID, 请求的标识,服务端在推送消息时,会将此标识一并返回.
}
```

平台推送:

```json
{
	"payload": {   
	 //规则数据,不同的节点和事件类型数据不同
	},
	"requestId": "request-id", //订阅请求的ID
	"topic": "/rule-engine/{instanceId}/{nodeId}/event/{event}",
	"type": "result" //为comlete是则表示订阅结束.
}
```

<br>

#### 订阅场景联动消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
       <code>{sceneId}</code>为场景联动id
    </p>
</div>

```json
"topic": "/scene/{sceneId}"
```

<br>

#### 订阅模拟器消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
       <code>{id}</code>为创建的模拟器的id，<code>{sessionId}</code>指创建模拟器时填入的<code>clientId</code>
    </p>
</div>

```json
"topic": "/network/simulator/{id}/{sessionId}"
```

<br>

#### 订阅TCP客户端调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        订阅此主题的消息分为TCP发送和接收两个部分的消息，<code>{id}</code>为TCP客户端组件的id，<code>{type}</code>类型可以选择为<code>_send</code>或<code>_subscribe</code>
    </p>
</div>

```json
"topic": "/network/tcp/client/{id}/{type}"
```

订阅接收到的消息

```json
{
    "type": "sub",
    "topic": "/network/tcp/client/{id}/_subscribe",
    "parameter": {
        //响应的数据，16进制以0x开头
        "response":"data"
    },
    "id": "request-id"
}
```

订阅发送的消息

```json
{
    "type": "sub",
    "topic": "/network/tcp/client/{id}/_send",
    "parameter": {
        //请求的数据，16进制以0x开头
        "request":"data"
    },
    "id": "request-id"
}
```

<br>

#### 订阅TCP服务调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
       <code>{id}</code>为TCP服务组件的id
    </p>
</div>

```json
"topic": "/network/tcp/server/{id}/_subscribe"
```

订阅接收到的数据

```json
{
    "type": "sub",
    "topic": "/network/tcp/server/{id}/_subscribe",
    "parameter": {
        //响应的数据，16进制以0x开头
        "response":"data"
    },
    "id": "request-id"
}
```



<br>

#### 订阅UDP调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        订阅此主题的消息分为UDP发送和接收两个部分的消息，<code>{id}</code>为UDP组件的id，<code>{type}</code>类型可以选择为<code>_send</code>或<code>_subscribe</code>
    </p>
</div>

```json
"topic": "/network/udp/{id}/{type}"
```

订阅接收到的消息

```json
{
    "type": "sub",
    "topic": "/network/udp/{id}/_subscribe",
    "parameter": {},
    "id": "request-id"
}
```

订阅发送的消息

```json
{
    "type": "sub",
    "topic": "/network/tcp/client/{id}/_send",
    "parameter": {
        //请求的数据，16进制以0x开头
        "request":"data"
    },
    "id": "request-id"
}
```

<br>

#### 订阅WebSocket客户端调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        订阅此主题的消息分为订阅时的消息和推送时的消息两部分，当<code>{pubsub}</code>为<code>_subscribe</code>时是订阅组件在订阅时产生的消息，否则为订阅组件在发布时产生的消息。<code>{id}</code>为WebSocket客户端组件的id，<code>{type}</code>类型可以选择为<code>JSON</code>、<code>BINARY</code>、<code>STRING</code>、<code>HEX</code>
    </p>
</div>

```json
"topic": "/network/websocket/client/{id}/{pubsub}/{type}"
```

<br>

#### 订阅WebSocket服务调试消息

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
       <code>{id}</code>为WebSocket服务组件的id
    </p>
</div>

```json
"topic": "/network/websocket/server/{id}/_subscribe"
```

订阅消息

```json
{
    "type": "sub",
    "topic": "/network/websocket/server/{id}/_subscribe",
    "parameter": {
        //响应的数据，16进制以0x开头
        "response":"data"
    },
    "id": "request-id"
}
```

#### 平台回复消息格式

```json
{
	"payload":{}, //实际收到的消息内容, 与订阅的主题相关
	"requestId": "request-id", //与订阅请求的id一致
	"topic": "/device/demo-device/test0/offline", //topic,实际产生数据的topic
	"type": "result" //类型 result:订阅结果 complete:结束订阅 error:发生错误 
}
```

<br>

#### 取消订阅

向websocket发送消息，格式为：

```json
{
    "type":"unsub",//固定为unsub
     "id": "request-id" //与订阅请求id一致
}
```

