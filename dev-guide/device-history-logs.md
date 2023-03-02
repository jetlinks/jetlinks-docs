## 调用设备历史数据接口

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        需要查询设备的相关数据。
    </p>
</div>

#### 指导介绍

<p>1. <a href='/dev-guide/device-history-logs.html#设备日志相关接口'>设备日志相关接口</a></p>
<p>2. <a href='/dev-guide/device-history-logs.html#数据查询例子'>数据查询举例</a></p>

### 设备日志相关接口

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        设备产生的数据保存在<code>Elasticsearch</code>中。
    </p>
</div>
<p>1、(GET)查询设备日志数据<code>/device/instance/{deviceId}/logs</code></p>

| 请求参数              | 参数说明                  | 请求类型  | 是否必须  | 参数类型    |
|-------------------|-----------------------|-------|-------|---------|
| pageSize          | 每页数量                  | query | false | integer |
| pageIndex         | 页码                    | query | false | integer |
| total             | 设置了此值后将不重复执行count查询总数 | query | false | integer |
| where             | 条件表达式,和terms参数冲突      | query | false | string  |
| orderBy           | 排序表达式,和sorts参数冲突      | query | false | string  |
| includes          | 指定要查询的列,多列使用逗号分隔      | query | false | string  |
| excludes          | 指定不查询的列,多列使用逗号分隔      | query | false | string  |
| terms[0].column   | 指定条件字段                | query | false | string  |
| terms[0].termType | 条件类型                  | query | false | string  |
| terms[0].type     | 多个条件组合方式              | query | false | string  |
| terms[0].value    | 条件值                   | query | false | string  |
| sorts[0].name     | 排序字段                  | query | false | string  |
| sorts[0].order    | 顺序,asc或者desc          | query | false | string  |
| deviceId          | 设备ID                  | path  | true  | string  |

<p>2、(POST)查询设备日志数据<code>/device/instance/{deviceId}/logs</code></p>

| 参数名   | 参数说明 | 请求类型 | 是否必须 | 参数类型 |
| -------- | -------- | -------- | -------- | -------- |
| deviceId | 设备ID   | path     | true     | string   |

<p>3、聚合查询设备属性<code>/device/instance/{deviceId}/agg/_query</code></p>

| 参数名   | 参数说明             | 请求类型 | 是否必须 | 参数类型 |
| -------- | -------------------- | -------- | -------- | -------- |
| deviceId | 设备ID               | path     | true     | string   |
| columns  | 属性列表及方式的集合 | body     | false    | array    |
| query    | 查询参数             | body     | false    | object   |

### 数据查询例子

<p>1、(GET)查询设备日志数据<code>/device/instance/{deviceId}/logs</code></p>

```json
//请求接口
GET http://192.168.66.203:8844/device/instance/1621406622872461312/logs?pageSize=1&pageIndex=1&sorts[0].name=timestamp&sorts[0].order=desc

//响应结果
{
	"message": "success",
	"result": {
		"pageIndex": 1,
		"pageSize": 1,
		"total": 1253,
		"data": [{
			"id": "PoYwTSLCcKO8FUbQTeG87yiAJw06AWOi",
			"deviceId": "1621406622872461312",
			"type": {
				"text": "属性上报",
				"value": "reportProperty"
			},
			"createTime": 1675847606979,
			"content": "{\"headers\":{\"deviceName\":\"LED灯_A栋_3_11\",\"productName\":\"LED灯\",\"productId\":\"1621404717110747136\",\"_uid\":\"PoYwTSLCcKO8FUbQTeG87yiAJw06AWOi\",\"creatorId\":\"1199596756811550720\",\"traceparent\":\"00-7c79fab919dfb5fa5da0b7b28799cc1e-faed50d6f0969546-01\"},\"messageType\":\"REPORT_PROPERTY\",\"deviceId\":\"1621406622872461312\",\"properties\":{\"actual_voltage\":20.504118},\"timestamp\":1675847606978}",
			"timestamp": 1675847606978
		}]
	},
	"status": 200,
	"timestamp": 1676371490537
}
```

<p>2、(POST)查询设备日志数据<code>/device/instance/{deviceId}/logs</code></p>

```json
//请求接口
POST http://192.168.66.203:8844/device-instance/mqtt-device/logs

//请求参数
{
   "pageIndex":1,
   "pageSize":1
}

//响应结果
{
	"message": "success",
	"result": {
		"pageIndex": 1,
		"pageSize": 1,
		"total": 1253,
		"data": [{
			"id": "PoYwTSLCcKO8FUbQTeG87yiAJw06AWOi",
			"deviceId": "1621406622872461312",
			"type": {
				"text": "属性上报",
				"value": "reportProperty"
			},
			"createTime": 1675847606979,
			"content": "{\"headers\":{\"deviceName\":\"LED灯_A栋_3_11\",\"productName\":\"LED灯\",\"productId\":\"1621404717110747136\",\"_uid\":\"PoYwTSLCcKO8FUbQTeG87yiAJw06AWOi\",\"creatorId\":\"1199596756811550720\",\"traceparent\":\"00-7c79fab919dfb5fa5da0b7b28799cc1e-faed50d6f0969546-01\"},\"messageType\":\"REPORT_PROPERTY\",\"deviceId\":\"1621406622872461312\",\"properties\":{\"actual_voltage\":20.504118},\"timestamp\":1675847606978}",
			"timestamp": 1675847606978
		}]
	},
	"status": 200,
	"timestamp": 1676370892777
}

```
<p>3、查询2023年2月8日16:56:00--17:00:00间每分钟属性<code>actual_voltage</code>上报的平均值。

```json
//请求接口
http://192.168.66.203:8844/device/instance/1621406622872461312/agg/_query

//请求参数
{
	"columns": [{
		"property": "actual_voltage",
		"agg": "AVG"
	}],

	"query": {
		"from": "2023-02-08 16:56:00",
		"to": "2023-02-08 17:00:00",
		"interval": "1m",
		"format": "yyyy-MM-dd HH:mm:ss",
		"limit": "5"
	}
}

//响应结果
{
    "message": "success",
    "result": [
        {
            "actual_voltage": 0.0,
            "time": "2023-02-08 17:00:00"
        },
        {
            "actual_voltage": 19.617771416666667,
            "time": "2023-02-08 16:59:00"
        },
        {
            "actual_voltage": 21.454782916666666,
            "time": "2023-02-08 16:58:00"
        },
        {
            "actual_voltage": 20.44424795833333,
            "time": "2023-02-08 16:57:00"
        },
        {
            "actual_voltage": 19.606869725000003,
            "time": "2023-02-08 16:56:00"
        }
    ],
    "status": 200,
    "timestamp": 1676370269517
}
```



