# API列表


::: tip 注意

在企业版`1.5.0`中已经集成了在线文档支持,打开`http://[ip]:[JetLinks后台端口]/doc.html`即可查看全部接口信息.   

如在demo环境中： http://demo.jetlinks.cn:9010/doc.html
:::
<!-- 
## 设备数据API

### 查询设备列表

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/_query`

http body 请求参数：

公共查询参数:请参考[公共查询参数](../query-param.md)

根据设备实例（DeviceInstanceEntity）条件查询，字段名和值分别对应[查询参数Term](../query-param.md#Term)中的column和value。  
设备实例（DeviceInstanceEntity）参数如下：  

名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
id | String | 否 | test001 | 设备实例ID
name | String | 否 | 温控设备001 | 设备实例名称
describe | String | 否 |  | 说明 
productId | String | 否 | 1236859833832701952 | 型号ID
productName | String | 否 | 智能温控 | 型号名称
configuration | Map&#60;String,Object&#62; | 否 |  | 其他配置
deriveMetadata | String | 否 |  | 派生元数据,有的设备的属性，功能，事件可能会动态的添加
state | DeviceState | 否 | online | 状态 
creatorId | String | 否 | 1199596756811550720 | 创建人ID
creatorName | String | 否 | 管理员 | 创建人名称
createTime | Long | 否 | 1584586676863 | 创建时间
registryTime | Long | 否 | online | 激活时间 
orgId | String | 否 | department | 机构ID
parentId | String | 否 |  | 父级设备ID

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | PagerResult&#60;DeviceInfo&#62; | 分页结果
status | int | 状态码
code | String  |  业务编码 

PagerResult&#60;DeviceInfo&#62;参数如下：  
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
pageIndex | int | 页码
pageSize | int | 每页数量
total | int  |  返回数据总数 
data | List&#60;DeviceInfo&#62;  |  返回数据集合

DeviceInfo参数如下：  
名称       | 类型  | 描述  
-------------- | -------------  | ------------- 
id | String | 设备ID                              
name | String | 设备名称
productId | String | 型号ID 
productName | String | 型号名称
state | [DeviceState](../enum.md#DeviceState) | 设备状态
registerTime | long | 注册时间
createTime | long | 创建时间
parentId | String | 父级设备ID

请求示例:  

```js
//请求
POST /api/v1/device/_query
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "productId",
		"value": "1236859833832701952"
	}]
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"pageIndex": 0,
		"pageSize": 1000,
		"total": 3,
		"data": [{
			"id": "test0312",
			"name": "设备0312",
			"productId": "1236859833832701952",
			"productName": "智能温控",
			"state": {
				"text": "未激活",
				"value": "notActive"
			},
			"registerTime": 0,
			"createTime": 1583997946670
		}, {
			"id": "test001",
			"name": "温控设备0309",
			"productId": "1236859833832701952",
			"productName": "智能温控",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"registerTime": 1583805253659,
			"createTime": 1585809343175
		}, {
			"id": "MQTT_FX_Client",
			"name": "mqttfx",
			"productId": "1236859833832701952",
			"productName": "智能温控",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"registerTime": 1584330967254,
			"createTime": 1584330960918
		}]
	},
	"status": 200,
	"code": "success"
}
```
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::

### 获取设备详情

请求方式： GET  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/_detail`

说明：{deviceId}需要替换为设备实例的id。

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | DeviceDetail | 返回值
status | int | 状态码
code | String  |  业务编码 

DeviceDetail参数如下：    
名称       | 类型  | 描述  
-------------- | -------------  | ------------- 
id | String | 设备ID                              
name | String | 设备名称
protocol | String | 消息协议标识 
transport | String | 通信协议
orgId | String | 所属机构ID
orgName | String | 所属机构名称
productId | String | 型号ID 
productName | String | 型号名称
deviceType | [DeviceType](../enum.md#DeviceType) | 设备类型
state | [DeviceState](../enum.md#DeviceState) | 设备状态
address | String | 客户端地址 
onlineTime | long | 上线时间
offlineTime | long | 离线时间 
registerTime | long | 注册时间
createTime | long | 创建时间
metadata | String | 设备元数据（在设备型号功能定义中定义）
configuration | Map&#60;String,Object&#62; | 设备配置信息 
tags | List&#60;DeviceTagEntity&#62; | 标签


标签（DeviceTagEntity）参数说明：  
名称       | 类型  | 描述  
-------------- | -------------  | ------------- 
deviceId | String | 设备ID                              
key | String | 键
name | String | 标签名 
value | String | 值
type | String | 标签类型
createTime | Date | 创建时间
description | String | 描述 

请求示例：
```js
//请求
GET /api/v1/device/1236859833832701952/_detail
X-Sign: f4823a*********e76eb1d 
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"id": "test001",
		"name": "温控设备0309",
		"protocol": "demo-v1",
		"transport": "MQTT",
		"productId": "1236859833832701952",
		"productName": "智能温控",
		"deviceType": {
			"text": "网关设备",
			"value": "gateway"
		},
		"state": {
			"text": "离线",
			"value": "offline"
		},
		"address": "/127.0.0.1:46360",
		"onlineTime": 1586422112901,
		"offlineTime": 1586424932209,
		"createTime": 1585809343175,
		"registerTime": 1583805253659,
		"metadata": "{\"events\":[{\"id\":\"fire_alarm\",\"name\":\"火警报警\",\"expands\":{\"eventType\":\"reportData\",\"level\":\"urgent\"},\"valueType\":{\"type\":\"object\",\"properties\":[{\"id\":\"a_name\",\"name\":\"区域名称\",\"valueType\":{\"type\":\"string\"}},{\"id\":\"b_name\",\"name\":\"建筑名称\",\"valueType\":{\"type\":\"string\"}},{\"id\":\"l_name\",\"name\":\"位置名称\",\"valueType\":{\"type\":\"string\"}}]}}],\"properties\":[{\"id\":\"temperature\",\"name\":\"温度\",\"valueType\":{\"type\":\"float\",\"min\":\"0\",\"max\":\"100\",\"step\":\"0.1\",\"unit\":\"celsiusDegrees\"},\"expands\":{\"readOnly\":\"true\"}}],\"functions\":[{\"id\":\"get-log\",\"name\":\"获取日志\",\"isAsync\":true,\"output\":{\"type\":\"string\",\"expands\":{\"maxLength\":\"2048\"}},\"inputs\":[{\"id\":\"start_date\",\"name\":\"开始日期\",\"valueType\":{\"type\":\"date\",\"dateFormat\":\"yyyy-MM-dd HH:mm:ss\"}},{\"id\":\"end_data\",\"name\":\"结束日期\",\"valueType\":{\"type\":\"date\",\"dateFormat\":\"yyyy-MM-dd HH:mm:ss\"}},{\"id\":\"time\",\"name\":\"分组\",\"valueType\":{\"type\":\"string\"}}]}]}",//在设备型号功能定义中定义
		"configuration": {
			"username": "test",
			"password": "test"
		},
		"tags": []
	},
	"status": 200,
	"code": "success"
}
```

 
### 查询设备详情列表

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/_detail/_query`

http body 请求参数：

公共查询参数:请参考[公共查询参数](../query-param.md)

请求示例:  

```js
//请求
POST /api/v1/device/_detail/_query
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "productId",
		"value": "1236859833832701952"
	}]
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"pageIndex": 0,
		"pageSize": 1000,
		"total": 3,
		"data": [
			{
				"id": "test001",
				"name": "温控设备0309",
				"protocol": "demo-v1",
				"transport": "MQTT",
				"productId": "1236859833832701952",
				"productName": "智能温控",
				"deviceType": {
					"text": "网关设备",
					"value": "gateway"
				},
				"state": {
					"text": "离线",
					"value": "offline"
				},
				"address": "/127.0.0.1:46360",
				"onlineTime": 1586422112901,
				"offlineTime": 1586424932209,
				"createTime": 1585809343175,
				"registerTime": 1583805253659,
				"metadata": "{\"events\":[{\"id\":\"fire_alarm\",\"name\":\"火警报警\",\"expands\":{\"eventType\":\"reportData\",\"level\":\"urgent\"},\"valueType\":{\"type\":\"object\",\"properties\":[{\"id\":\"a_name\",\"name\":\"区域名称\",\"valueType\":{\"type\":\"string\"}},{\"id\":\"b_name\",\"name\":\"建筑名称\",\"valueType\":{\"type\":\"string\"}},{\"id\":\"l_name\",\"name\":\"位置名称\",\"valueType\":{\"type\":\"string\"}}]}}],\"properties\":[{\"id\":\"temperature\",\"name\":\"温度\",\"valueType\":{\"type\":\"float\",\"min\":\"0\",\"max\":\"100\",\"step\":\"0.1\",\"unit\":\"celsiusDegrees\"},\"expands\":{\"readOnly\":\"true\"}}],\"functions\":[{\"id\":\"get-log\",\"name\":\"获取日志\",\"isAsync\":true,\"output\":{\"type\":\"string\",\"expands\":{\"maxLength\":\"2048\"}},\"inputs\":[{\"id\":\"start_date\",\"name\":\"开始日期\",\"valueType\":{\"type\":\"date\",\"dateFormat\":\"yyyy-MM-dd HH:mm:ss\"}},{\"id\":\"end_data\",\"name\":\"结束日期\",\"valueType\":{\"type\":\"date\",\"dateFormat\":\"yyyy-MM-dd HH:mm:ss\"}},{\"id\":\"time\",\"name\":\"分组\",\"valueType\":{\"type\":\"string\"}}]}]}",//在设备型号功能定义中定义
				"configuration": {
					"username": "test",
					"password": "test"
				},
				"tags": []
			}
	]
	},
	"status": 200,
	"code": "success"
}
```

### 批量保存设备

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device`

http body 请求参数为DeviceSaveDetail集合：    

DeviceSaveDetail 参数如下：  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
id | String | 是 | test002 | 设备实例ID
name | String | 是 | 温控设备002 | 设备实例名称
productId | String | 是 | 1236859833832701952 | 型号ID
productName | String | 是 | 智能温控 | 型号名称
configuration | Map&#60;String,Object&#62; | 否 |  | 设备配置信息，根据不同的协议配置不同,如果MQTT用户名密码等
creatorId | String | 否 | 1199596756811550720 | 创建人ID
creatorName | String | 否 | 管理员 | 创建人名称
tags | List&#60;DeviceTagEntity&#62; | 否 |  | 标签

标签（DeviceTagEntity）参数说明：  
名称       | 类型  | 描述  
-------------- | -------------  | ------------- 
deviceId | String | 设备ID                              
key | String | 键
name | String | 标签名 
value | String | 值
type | String | 标签类型
createTime | Date | 创建时间
description | String | 描述 

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | int | 保存数量
status | int | 状态码
code | String  |  业务编码 

示例: 

```js
// 请求
POST /api/v1/device
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ
Content-Type: application/json

[
	{
		"id": "test002",
		"name": "设备002",
		"productId": "1236859833832701952",
		"configuration": {
			"username": "test002",
			"password": "test002"
		},
		"tags": [{
			"deviceId": "test002",
			"key": "area",
			"name": "地区",
			"value": "chongqing"
		}]
	},
	{
		"id": "test003",
		"name": "设备名称",
		"productId": "1236859833832701952",
		"configuration": {}
	}
]

// 响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{"result":3,"status":200,"code":"success"}

```
 
### 批量同步设备状态

请求方式： POST  

URL： `http://localhost:8844/api/v1/device/state/_sync`

请求参数格式为[动态查询参数](../query-param.md)

::: tip 提示
此操作将同步设备真实状态,如果一次同步数量较大,推荐使用[websocket方式同步](../../dev-guide/websocket-subs.md#批量同步设备状态),可实时获取同步结果.
::: 

请求示例:

```js
//请求
POST /api/v1/device/state/_sync
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
  "pageSize":10
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"status":200,
	"result":[
		{
			"deviceId":"设备ID",
		    "state":{"value":"offline","text":"离线"}
		}
	]
}
```

### 批量激活设备

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/_deploy`

http body 请求参数为设备id集合，List&#60;String&#62;。    

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | int |激活数量
status | int | 状态码
code | String  |  业务编码 

请求示例:  

```js
//请求
POST /api/v1/device/_deploy
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

["test002","test003", "test004"]

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{"result":3,"status":200,"code":"success"}
```

### 批量注销设备

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/_unDeploy`  

http body 请求参数为设备id集合，List&#60;String&#62;。    

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | int |注销成功数量
status | int | 状态码
code | String  |  业务编码 

请求示例:

```js
//请求
POST /api/v1/device/_unDeploy
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

["test002","test003", "test004"]

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{"result":3,"status":200,"code":"success"}
```

### 批量删除设备

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/_delete`  

http body 请求参数为设备id集合，List&#60;String&#62;。    

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | int |注销成功数量
status | int | 状态码
code | String  |  业务编码 

请求示例:  

```js
//请求
POST /api/v1/device/_delete
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

["test002","test003", "test004"]

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{"result":3,"status":200,"code":"success"}
```

### 根据设备ID查询设备日志

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/log/_query`  

**说明：{deviceId}需要替换为设备实例的id。**  

http body 请求参数：

公共查询参数:请参考[公共查询参数](../query-param.md)

根据设备操作日志（DeviceOperationLogEntity）条件查询，字段名和值分别对应[查询参数Term](../query-param.md#Term)中的column和value。  
设备操作日志（DeviceOperationLogEntity）参数如下：  

名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
id | String | 否 | LoymU3EBCTcV5s5DbSn7 | 设备实例ID
productId | String | 否 | 1236859833832701952 | 型号ID
type | [DeviceLogType](../enum.md#DeviceLogType) | 否 | readProperty | 类型
createTime | Long | 否 | 1584586676863 | 创建时间
orgId | String | 否 | department | 机构ID

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | PagerResult&#60;DeviceInfo&#62; | 分页结果
status | int | 状态码
code | String  |  业务编码 

PagerResult&#60;DeviceInfo&#62;参数如下：  
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
pageIndex | int | 页码
pageSize | int | 每页数量
total | int  |  返回数据总数 
data | List&#60;DeviceInfo&#62;  |  返回数据集合

DeviceInfo参数如下：  
名称       | 类型  | 描述  
-------------- | -------------  | ------------- 
id | String | 设备ID                              
name | String | 设备名称
productId | String | 型号ID 
productName | String | 型号名称
state | [DeviceLogType](../enum.md#DeviceLogType) | 设备状态
registerTime | long | 注册时间
createTime | long | 创建时间
parentId | String | 父级设备ID

请求示例:

```js
//请求
POST /api/v1/device/test001/log/_query
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "createTime$btw", 
		"value": "2020-01-01,2020-06-01"
	}]
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"pageIndex": 0,
		"pageSize": 25,
		"total": 4,
		"data": [{
			"id": "fa1528090a464e3f0bf3839ce0c1315",
			"deviceId": "test001",
			"productId": "1236859833832701952",
			"type": {
				"text": "上线",
				"value": "online"
			},
			"createTime": 1586422112000,
			"content": "设备上线"
		}, {
			"id": "eb7d05c8dda18bc37434bf7f98799ba7",
			"deviceId": "test001",
			"productId": "1236859833832701952",
			"type": {
				"text": "离线",
				"value": "offline"
			},
			"createTime": 1586419084000,
			"content": "设备离线"
		}, {
			"id": "34a89eb7b89646bd98fc5ab6617b370e",
			"deviceId": "test001",
			"productId": "1236859833832701952",
			"type": {
				"text": "上线",
				"value": "online"
			},
			"createTime": 1586413536000,
			"content": "设备上线"
		}, {
			"id": "2f1aaa63865afb3cdcec4aa72771ab9",
			"deviceId": "test001",
			"productId": "1236859833832701952",
			"type": {
				"text": "读取属性回复",
				"value": "readPropertyReply"
			},
			"createTime": 1583809148000,
			"content": "{\"temperature\":\"50\"}"
		}]
	},
	"status": 200,
	"code": "success"

}
```

### 查询设备属性

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/properties/_query`  

**说明：{deviceId}需要替换为设备实例的id。**  

http body 请求参数：

公共查询参数:请参考[公共查询参数](../query-param.md)

根据设备属性（DevicePropertiesEntity）条件查询，字段名和值分别对应[查询参数Term](../query-param.md#Term)中的column和value。  
设备属性（DevicePropertiesEntity）参数如下：  

名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
id | String | 否 |  | 属性ID
productId | String | 否 | 1236859833832701952 | 型号ID
property | String | 否 | temperature | 属性标识
propertyName | String | 否 | 温度 | 属性名称
stringValue | String | 否 |  | 字符串值
formatValue | String | 否 |  | 格式化值
numberValue | String | 否 |  | 数字值
geoValue | GeoPoint | 否 |  | 坐标值
timestamp | long | 否 |  | 时间戳
objectValue | String | 否 |  | 结构体值
value | String | 否 |  | 值
timeValue | Date | 否 |  | 时间值
orgId | String | 否 |  | 机构ID

GeoPoint参数如下：  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
lat | double | 否 | 106.57 | 经度
lon | double | 否 | 29.52 | 纬度

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | PagerResult&#60;DevicePropertiesEntity&#62; | 分页结果
status | int | 状态码
code | String  |  业务编码 

PagerResult&#60;DevicePropertiesEntity&#62;参数如下：  
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
pageIndex | int | 页码
pageSize | int | 每页数量
total | int  |  返回数据总数 
data | List&#60;DevicePropertiesEntity&#62;  |  返回数据集合

DeviceInfo参数如下：  
名称       | 类型  | 描述  
-------------- | -------------  | ------------- 
id | String | 属性ID
productId | String | 型号ID
property | String | 属性标识
propertyName | String | 属性名称
stringValue | String | 字符串值
formatValue | String | 格式化值
numberValue | String | 数字值
geoValue | GeoPoint | 坐标值
timestamp | long | 否 时间戳
objectValue | String | 结构体值
value | String | 值
timeValue | Date | 时间值
orgId | String| 机构ID

请求示例:  

```js
//请求
POST /api/v1/device/test001/properties/_query  
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "property",
		"value": "temperature"
	}]
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"pageIndex": 0,
		"pageSize": 25,
		"total": 3,
		"data": [{
			"id": "amugXXEBQZKUd4flBbDN",
			"deviceId": "test001",
			"property": "temperature",
			"propertyName": "温度",
			"formatValue": "50.00℃",
			"numberValue": 50.0,
			"timestamp": 1583809148000,
			"value": "50",
			"productId": "1236859833832701952"
		}]
	},
	"status": 200,
	"code": "success"
}
```

### 获取设备最新的全部属性

请求方式： GET  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/properties/_latest`

说明：{deviceId}需要替换为设备实例的id。

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | DevicePropertiesEntity | 返回值
status | int | 状态码
code | String  |  业务编码 

DevicePropertiesEntity参数如下：    
名称       | 类型  | 描述  
-------------- | -------------  | ------------- 
id | String | 否 |  | 属性ID
productId | String | 否 | 1236859833832701952 | 型号ID
property | String | 否 | temperature | 属性标识
propertyName | String | 否 | 温度 | 属性名称
stringValue | String | 否 |  | 字符串值
formatValue | String | 否 |  | 格式化值
numberValue | String | 否 |  | 数字值
geoValue | GeoPoint | 否 |  | 坐标值
timestamp | long | 否 |  | 时间戳
objectValue | String | 否 |  | 结构体值
value | String | 否 |  | 值
timeValue | Date | 否 |  | 时间值
orgId | String | 否 |  | 机构ID
deviceId | String | 否 |  | 设备ID
type | String | 否 |  | 类型

请求示例:  

```js
//请求
POST /api/v1/device/test001/properties/_latest
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ


//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": [{
		"id": "amugXXEBQZKUd4flBbDN",
		"deviceId": "test001",
		"property": "temperature",
		"propertyName": "温度",
		"formatValue": "50.00℃",
		"numberValue": 50.0,
		"timestamp": 1583809148000,
		"value": "50",
		"productId": "1236859833832701952"
	}],
	"status": 200,
	"code": "success"
}
```

### 聚合查询设备属性

请求方式： GET  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/agg/{agg}/{property}/_query`

说明：{deviceId}需要替换为设备实例的id,  
     {agg}需要替换为聚合类型，包含：MIN, MAX, AVG, SUM, COUNT, NONE  
     {property}需要替换为设备属性。  

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | List&#60;Map&#60;String, Object&#62;&#62; | 返回值
status | int | 状态码
code | String  |  业务编码 


请求示例:  
查询过去30分钟设备test001的温度属性每分钟的平均温度。  
```js
//请求
POST /api/v1/device/test001/agg/AVG/temperature/_query
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1598579554299
X-Client-Id: kF**********HRZ

{
	"interval": "1h",//时间间隔，单位为英文时间单位首字母，如小时h、天d等
	"format": "yyyy-MM-dd HH:mm:ss",//时间格式
	"from": "now-24h",//起始时间
	"to": "now",//终止时间
    "query": {
    		"pageSize": 25//指定返回数据条数，默认25
    	}
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": [{
		"temperature": 32.8235294117647,
		"time": "2020-08-28 09:52:00"
	}, {
		"temperature": 32.13333333333333,
		"time": "2020-08-28 09:51:00"
	}, {
		"temperature": 34.11666666666667,
		"time": "2020-08-28 09:50:00"
	}, {
		"temperature": 33.9,
		"time": "2020-08-28 09:49:00"
	}, {
		"temperature": 33.85,
		"time": "2020-08-28 09:48:00"
	}, {
		"temperature": 34.45,
		"time": "2020-08-28 09:47:00"
	}, {
		"temperature": 32.75,
		"time": "2020-08-28 09:46:00"
	}, {
		"temperature": 35.6,
		"time": "2020-08-28 09:45:00"
	}, {
		"temperature": 35.06666666666667,
		"time": "2020-08-28 09:44:00"
	}, {
		"temperature": 34.15,
		"time": "2020-08-28 09:43:00"
	}, {
		"temperature": 37.629629629629626,
		"time": "2020-08-28 09:42:00"
	}, {
		"temperature": 33.8,
		"time": "2020-08-28 09:41:00"
	}, {
		"temperature": 33.42857142857143,
		"time": "2020-08-28 09:40:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:39:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:38:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:37:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:36:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:35:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:34:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:33:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:32:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:31:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:30:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:29:00"
	}, {
		"temperature": 0.0,
		"time": "2020-08-28 09:28:00"
	}],
	"code": "success",
	"status": 200
}
```

### 查询设备事件

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/event/{eventId}/_query`  

**说明：{deviceId}需要替换为设备实例的id，{eventId}需替换为设备型号中事件标识。**  

http body 请求参数：

公共查询参数:请参考[公共查询参数](../query-param.md)  

::: tip 注意
此处的参数中Term为设备型号功能定义中的事件内容
:::

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | PagerResult&#60;Map&#60;String,Object&#62;&#62; | 分页结果
status | int | 状态码
code | String  |  业务编码 

PagerResult&#60;Map&#60;String,Object&#62;&#62;参数如下：  
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
pageIndex | int | 页码
pageSize | int | 每页数量
total | int  |  返回数据总数 
data | List&#60;Map&#60;String,Object&#62;&#62;  |  返回数据集合 

请求示例:  

```js
//请求
POST /api/v1/device/test001/event/fire_alarm/_query
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "a_name",//型号事件定义中的结构体的属性
		"value": "南岸区"
	}]
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"pageIndex": 0,
		"pageSize": 25,
		"total": 1,
		"data": [{
			"b_name": "C2 栋",
			"productId": "1236859833832701952",
			"pname": "智能温控",
			"event_count": 1,
			"l_name": "12-05-201",
			"alarm_describe": "火灾报警",
			"deviceId": "test001",
			"event_id": 1,
			"alarm_type": 1,
			"createTime": 1586701932647,
			"id": "Pn_ObnEBbNs2V4rRJb4F",
			"aid": 105,
			"a_name": "南岸区",
			"timestamp": 1586701932647
		}]
	},
	"status": 200,
	"code": "success"
}
```

##  设备操作API

### 获取设备属性

此操作将发送指令`ReadPropertyMessage`到设备.并获取设备返回数据`ReadPropertyMessageReply`.

请求方式： GET  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/property/{propertyId}

说明：{deviceId}需要替换为设备实例的id，{propertyId}需要替换成属性标识。

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | Map&#60;String, Object&#62; | 返回值
status | int | 状态码
code | String  |  业务编码

请求示例:  

```js
//请求
GET /api/v1/device/test001/property/temperature
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"temperature": "50"
	},
	"status": 200,
	"code": "success"
}
```

### 设置设备属性

此操作将发送指令`WritePropertyMessage`到设备.并获取设备返回数据`WritePropertyMessageReply`.

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/properties`  

**说明：{deviceId}需要替换为设备实例的id。**  

http body 请求参数：
此处的参数为设备型号功能定义中的属性，如：
```json
{"temperature": 50.0}//temperature为属性标识
``` 

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | Map&#60;String, Object&#62; | 返回数据
status | int | 状态码
code | String  |  业务编码 

请求示例: 

```js
//请求
POST /api/v1/device/test001/property/temperature
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{"temperature": 50.0}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": {
		"temperature": "50"
	},
	"status": 200,
	"code": "success"
}
```

### 设备功能调用

此操作将发送指令`FunctionInvokeMessage`到设备,并等待返回`FunctionInvokeMessageReply`.

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/function/{functionId}`  

**说明：{deviceId}需要替换为设备实例的id，{functionId}需要替换为设备型号中功能定义的功能标识。**  

请求示例: 

```js
//请求
POST /api/v1/device/test001/function/play_voice
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
  "text": "你好" //与物模型中定义到参数一致
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{
	"result": ['ok'], //注意是集合.因为可能返回多条结果
	"status": 200,
	"code": "success"
}
```


## 地理信息管理

### 根据geojson保存数据

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/geo/object/geo.json`

http body 请求参数：

GeoJson（参考[GeoJSON](https://geojson.org/)） 参数如下：  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
type | String | 是 | FeatureCollection | 类型，固定为FeatureCollection
features | List&#60;GeoJsonFeature&#62; | 否 |  | geo集合

GeoJsonFeature 参数如下：  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
type | String | 是 | Feature | 类型，固定为Feature
properties | Map&#60;String,Object&#62; | 否 |  | 拓展属性
geometry | GeoShape | 否 |  | 图形

properties 参数如下：  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
objectId | String | 是 | chongqing | 
id | String | 是 | chongqing | 
objectType | String | 是 | city | 

GeoShape 参数如下：  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
type | [Type](../enum.md#Type) | 是 | Polygon | 类型
coordinates | List&#60;Object&#62; | 是 |  | 坐标集合

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
status | int | 状态码
code | String  |  业务编码 

请求示例:  

```js
//请求
POST /api/v1/geo/object/geo.json
Content-Type: application/json
X-Sign: f4823a*********e76eb1d
X-Timestamp: 1586511766004
X-Client-Id: kF**********HRZ

{
	"type": "FeatureCollection",
	"features": [{
		"type": "Feature",
		"properties": {//拓展属性
            //必须的配置
			"id": "500242",
			"objectId": "youyang",
			"objectType": "city",
            
            //其他配置,将设置到 GeoObject.tags中,在查询时可通过filter进行搜索
			"name": "酉阳土家族苗族自治县",
			"group": "china"
		},
		"geometry": {
			"type": "Polygon",
			"coordinates": [//坐标列表
				[
					[108.3142, 28.9984],
					[108.3252, 29.0039],
					[108.3252, 28.96],
					[108.3142, 28.9984]
				]
			]
		}
	}]
}

//响应
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23fa********f25

{"status":200,"code":"success"}
```

::: tip 注意：
可使用 [http://geojson.io/#map=4/32.18/105.38](http://geojson.io/#map=4/32.18/105.38) 生成json.
或者[获取行政区划geojson](http://datav.aliyun.com/tools/atlas)/
:::

### 查询geo对象

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/geo/object/_search`

http body 请求参数：
  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
shape | Object | 否 |  | 图形，如重庆市行政区域边界(扩展属性objectId为该边界的标识)
filter | QueryParamEntity | 否 |  | 可根据GeoObject.tags中的属性进行查询


返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | GeoObject | 返回数据
status | int | 状态码
code | String  |  业务编码 

GeoObject 参数如下：
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
id | String | 唯一标识
objectType | String | 类型
shapeType | String  |  图形类型
objectId | String  |  图形唯一标识
property | String | 属性标识
point | GeoPoint  |  坐标
shape | GeoShape  |  图形
tags | String | 拓展信息
timestamp | long  |  时间戳: 数据更新的时间

GeoPoint 参数如下：  
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
lon | double | 经度
lat | double | 纬度

GeoShape 参数如下：  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
type | [Type](../enum.md#Type) | 是 | Polygon | 类型
coordinates | List&#60;Object&#62; | 是 |  | 坐标集合度

请求示例:  
RequestUrl: http://localhost:8844/api/v1/geo/object/_search   

RequestMethod: POST  

RequestHeader:  
    X-Sign: `7d825f4************5724949`  
    X-Timestamp: `1587460645733`    
    X-Client-Id: `kF**********HRZ`    
    Content-Type: application/json  
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::
RequestBody：
1. 使用shape中的objectId查询：    
    ```json
    {
              "shape":{
                  "objectId":"youyang" //查询objectId为youyang的所有geo信息
              },
              "filter":{
     
              }
          }
    ```
   ::: tip 注意：
   objectId为平台对geojson进行拓展的属性，为geo对象的唯一标识，如：设备id，区域id等。  
   当物模型里面配置了`地理位置`，objectId则为该设备id，可通过objectId查询该设备的地理位置信息。
   :::
2. 使用shape的多边形的坐标集进行查询   
   ```json
   {
   	"shape": {
   			"type": "Polygon",//请参考Type
   			"coordinates": [
   				[
   					[108.3142, 28.9984],
   					[108.3252, 29.0039],
   					[108.3252, 28.96],
   					[108.3142, 28.9984]
   				]
   			]
   		},
   	"filter": {
   
   	}
   }
   ```
    ::: tip 注意：
    shape中的type请参考[Type](../enum.md#Type)。
    :::
    
3. filter的使用  
    ```json
    {
    	"shape": {
    		"objectId": "youyang"
    	},
    	"filter": {
    		"where": "tags.name=酉阳土家族苗族自治县 and tags.group=china"
    	}
    
    }
    ```
   ::: tip 注意：
   filter查询的条件来自于[根据geojson保存数据](#根据geojson保存数据)接口提交的数据中properties中设置到GeoObject.tags的属性，如：name、group;  
   filter可以单独进行查询，不需要传入`shape`。
   :::
HttpResponse：  
```json
{
	"result": [{
		"id": "youyang",
		"objectType": "city",
		"shapeType": "Polygon",
		"objectId": "youyang",
		"shape": {
			"type": "Polygon",
			"coordinates": [
				[
					[108.3142, 28.9984],
					[108.3252, 29.0039],
					[108.3252, 28.96],
					[108.3142, 28.9984]
				]
			]
		},
		"tags": {
			"name": "酉阳土家族苗族自治县",
			"group": "china"
		},
		"timestamp": 0
	}],
	"status": 200,
	"code": "success"
}
```

### 查询geo对象并转为geoJson

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/geo/object/_search/geo.json`

http body 请求参数：
  
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
shape | Object | 否 |  | 图形，如重庆市行政区域边界(扩展属性objectId为该边界的标识)
filter | QueryParamEntity | 否 |  | 可根据GeoObject.tags中的属性进行查询


返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | GeoJson | 返回数据,请参考GeoJson标准
status | int | 状态码
code | String  |  业务编码 

请求示例:  
RequestUrl: http://localhost:8844/api/v1/geo/object/_search   

RequestMethod: POST  

RequestHeader:  
    X-Sign: `f9f297a****************acf288a`  
    X-Timestamp: `1587542185752`    
    X-Client-Id: `kF**********HRZ`    
    Content-Type: application/json  
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::
RequestBody：  
**查询参数请参考[查询geo对象接口](#查询geo对象)。**  
```json
{
              "shape":{
                  "objectId":"youyang" //查询objectId为youyang geo对象内的所有geo信息
              },
              "filter":{
     
              }
          }
```

HttpResponse：  
```json
{
	"result": {
		"type": "FeatureCollection",
		"features": [{
			"type": "Feature",
			"properties": {
				"name": "酉阳土家族苗族自治县",
				"id": "youyang",
				"objectId": "youyang",
				"group": "china",
				"objectType": "city"
			},
			"geometry": {
				"type": "Polygon",
				"coordinates": [
					[
						[108.3142, 28.9984],
						[108.3252, 29.0039],
						[108.3252, 28.96],
						[108.3142, 28.9984]
					]
				]
			}
		}]
	},
	"status": 200,
	"code": "success"
}
```

 -->