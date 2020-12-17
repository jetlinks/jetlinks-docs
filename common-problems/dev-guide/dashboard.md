# Dashboard说明

平台封装了统一的仪表盘接口,用于统计获取仪表数据.

名词说明:

1. dashboard: 仪表盘标识,如: system-monitor
2. object: 对象,如: cpu,memory
3. measurement: 指标,如: usage
4. dimension: 维度,如: realTime,history

## 获取支持的Dashboard定义信息

```js

GET /dashboard/defs

[{
	"id": "systemMonitor",
	"name": "系统监控",
	"objects": [{
		"id": "memory",
		"name": "内存"
	}, {
		"id": "cpu",
		"name": "CPU"
	}]
}, {
	"id": "device",
	"name": "设备信息",
	"objects": [{
		"id": "demo-device",
		"name": "演示设备"
	}]
}]

```

## 获取dashboard的指标定义信息

```js

POST /dashboard/def/{dashboard}/{object}/measurements

[{
	"id": "properties", //指标标识
	"name": "属性记录",
	"dimensions": [{ //维度
		"id": "realTime",
		"name": "实时数据",
		"type": { // 返回值类型定义,和物模型类型格式一致
			"properties": [{
				"valueType": {
					"name": "字符串",
					"id": "string",
					"type": "string"
				},
				"id": "property",
				"name": "属性"
			}],
			"name": "对象类型",
			"id": "object",
			"type": "object"
		},
		"params": { // 参数属性定义
			"properties": [{  
				"property": "deviceId",
				"name": "设备",
				"description": "指定设备",
				"type": {
					"name": "字符串",
					"id": "string",
					"type": "string"
				}
			}]
		},
		"realTime": true //实时数据
	}]
}]

```

### EventSource方式获取数据

```js

GET /dashboard/device/message/quantity/agg?:X_Access_Token={token}&time=1d&from=2020-01-01&limit=10&format=M月d日&to=2020-04-10

data:{"value":13800,"timeString":"4月1日","timestamp":2}

data:{"value":13761,"timeString":"4月2日","timestamp":1}

data:{"value":13980,"timeString":"4月3日","timestamp":0}

```

### POST方式获取数据

POST方式仅能获取非实时数据.

请求:

```js

POST /dashboard/_multi

[{
	"dashboard": "gatewayMonitor",
	"object": "deviceGateway",
	"measurement": "received_message",
	"dimension": "agg",
	"group": "gateway-message", //分组,和响应数据中的分组一致.当同时请求多个dahsboad的时候,通过分组来关联是哪一个dashboad.
	"params": { //参数,不同的dashboad参数不同
		"time": "1m",
		"limit": 60,
		"format": "HH时mm分",
		"from": "2020-04-29 17:37:49",
		"to": "2020-04-29 18:37:49"
	}
}]

```

响应:

```js
{
	"result": [{
		"group": "gateway-message",
		"data": {
			"value": 0,
			"timeString": "17时38分",
			"timestamp": 1
		}
	}, {
		"group": "sameDay",
		"data": {
			"value": 0,
			"timeString": "17时39分",
			"timestamp": 0
		}
	}],
	"status": 200,
	"code": "success"
}

```

### Websocket方式获取数据

[点击查看](./websocket-subs.md#dashboard)


### 自定义Dashboard

参考类:`DeviceMessageMeasurementProvider`实现.