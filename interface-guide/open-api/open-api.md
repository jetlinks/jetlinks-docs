# API列表

## 设备数据API

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

RequestUrl: `http(s)://localhost:8844/api/v1/device/1236859833832701952/_detail`  

RequestMethod: GET

RequestHeader:
    X-Sign: `f4823a*********e76eb1d`  
    X-Timestamp: `1586511766004`  
    X-Client-Id: `kF**********HRZ`    
::: tip 说明：
X-Sign为签名，`param`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::
HttpResponse：  
```json
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
RequestUrl: http://localhost:8844/api/v1/device/_query   

RequestMethod: POST  

RequestHeader:  
    X-Sign: `f4823a*********e76eb1d`  
    X-Timestamp: `1586511766004`    
    X-Client-Id: `kF**********HRZ`    
    Content-Type: application/json  
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::
RequestBody：  
```json
{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "productId",
		"value": "1236859833832701952"
	}]
}
```

HttpResponse：  
```json
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

### 根据设备ID类型和动态查询参数查询设备相关数据

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/{deviceId}/log/_query`  

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

请求示例  
RequestUrl: http://localhost:8844/api/v1/device/test001/log/_query  

RequestMethod: POST  

RequestHeader:  
    X-Sign: `f4823a*********e76eb1d`    
    X-Timestamp: `1586511766004`    
    X-Client-Id: `kF**********HRZ`    
    Content-Type: application/json  
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::
RequestBody：  
```json
{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "productId",
		"value": "1236859833832701952"
	}]
}
```
HttpResponse：  
```json
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

### 分页查询设备属性

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/{deviceId}/properties/_query`  

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

请求示例  
RequestUrl:  http://localhost:8844/api/v1/device/test001/properties/_query  

RequestMethod: POST  

RequestHeader: 
    X-Sign: `f4823a*********e76eb1d`    
    X-Timestamp: `1586511766004`    
    X-Client-Id: `kF**********HRZ`    
    Content-Type: application/json  
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::

RequestBody：  
```json
{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "property",
		"value": "temperature"
	}]
}
```

HttpResponse：  
```json
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

DeviceDetail参数如下：    
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

请求示例：

RequestUrl: http(s)://localhost:8844/api/v1/device/test001/properties/_latest  

RequestMethod: GET  

RequestHeader:    
    X-Sign: `f4823a*********e76eb1d`    
    X-Timestamp: `1586511766004`    
    X-Client-Id: `kF**********HRZ`    
::: tip 说明：
X-Sign为签名，`param`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::

HttpResponse：  
```json
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
RequestUrl: http://localhost:8844/api/v1/device/test001/event/fire_alarm/_query  

RequestMethod: POST  

RequestHeader:  
    X-Sign: `fd9cf3f*************48f373d`    
    X-Timestamp: `1586702227360`    
    X-Client-Id: `kF**********HRZ`    
    Content-Type: application/json  
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::

RequestBody：  
```json
{
	"pageSize": 25,
	"pageIndex": 0,
	"terms": [{
		"column": "a_name",//型号事件定义中的结构体值属性
		"value": "未来科技城"
	}]
}
```
HttpResponse：  
```json
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
			"a_name": "未来科技城",
			"timestamp": 1586701932647
		}]
	},
	"status": 200,
	"code": "success"
}
```

##  设备操作API

### 获取设备属性

请求方式： GET  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/property/{propertyId}

说明：{deviceId}需要替换为设备实例的id，{propertyId}需要替换成属性标识。

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | Map&#60;String, Object&#62; | 返回值
status | int | 状态码
code | String  |  业务编码

请求示例：

RequestUrl: http://localhost:8844/api/v1/device/test001/property/temperature  

RequestMethod: GET  

RequestHeader:  
X-Sign: `f4823a*********e76eb1d`    
X-Timestamp: `1586511766004`    
X-Client-Id: `kF**********HRZ`    
::: tip 说明：
X-Sign为签名，`param`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::

HttpResponse：  
```json
{
	"result": {
		"temperature": "50"
	},
	"status": 200,
	"code": "success"
}
```

### 设置设备属性

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
RequestUrl: http://localhost:8844/api/v1/device/test001/properties    

RequestMethod: POST  

RequestHeader:  
    X-Sign: `7e5f****************087bc5`  
    X-Timestamp: `1586694341284`  
    X-Client-Id: `kF**********HRZ`  
    Content-Type: application/json  
::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::

RequestBody：  
```json
{"temperature": 50.0}
```
HttpResponse：  
```json
{
	"result": {
		"temperature": "50"
	},
	"status": 200,
	"code": "success"
}
```

### 设备功能调用

请求方式： POST  

URL： `http(s)://localhost:8844/api/v1/device/{deviceId}/function/{functionId}`  

**说明：{deviceId}需要替换为设备实例的id，{functionId}需要替换为设备型号中功能定义的功能标识。**  

http body 请求参数：
此处的参数为设备型号功能定义中的功能定义内容，如：
```json
{"start_date": "2020-04-12"}
``` 

返回参数:
名称       | 类型 | 描述  
-------------- | ------------- | ------------- 
result | Map&#60;String, Object&#62; | 返回数据
status | int | 状态码
code | String  |  业务编码 

请求示例  
RequestUrl: http://localhost:8844/api/v1/device/test001/function/get-log  

RequestMethod: POST  

RequestHeader:  
    X-Sign: `8c5107***************eda4d`  
    X-Timestamp: `1586704534250`  
    X-Client-Id: `kF**********HRZ`  
    Content-Type: application/json  

::: tip 说明：
X-Sign为签名，`body`+`X-Timestamp`+`SecuryeKey`MD5加密  
X-Timestamp为时间戳  
X-Client-Id为平台openApi客户端id  
:::

RequestBody：  
```json
{
  "start_date": "2020-04-12"
}
```
HttpResponse：  
```json
{
	"result": [{
		"start_date": "2020-04-12"
	}],
	"status": 200,
	"code": "success"
}
```

