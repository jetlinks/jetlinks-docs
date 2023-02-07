# 融合网关功能接口文档-通知

## 请求地址说明
融合网关所有功能接口请求地址均为`/edge/operations/{deviceId}/{functionId}/invoke`。
其中`deviceId`为边缘设备网关设备Id `functionId`为功能标识。

**注意：当在网关本地调用以下接口时，`deviceId`参数值固定为当前网关deviceId**

## 一、通知

### 1、获取通知类型列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/notifier-type-list/invoke`

**功能标识：** `notifier-type-list`

**请求参数说明**

该接口无请求参数

**返回参数示例**

```json
{
  "result":[
    [
      {
        "id":"email",
        "name":"邮件",
        "providerInfos":[
          {
            "type":"email",
            "id":"embedded",
            "name":"默认"
          }
        ]
      },
      {
        "id":"voice",
        "name":"语音",
        "providerInfos":[
          {
            "type":"voice",
            "id":"aliyun",
            "name":"阿里云"
          }
        ]
      },
      {
        "id":"sms",
        "name":"短信",
        "providerInfos":[
          {
            "type":"sms",
            "id":"test",
            "name":"测试"
          },
          {
            "type":"sms",
            "id":"aliyunSms",
            "name":"阿里云短信服务"
          }
        ]
      }
    ]
  ],
  "status":200,
  "code":"success"
}
```

### 2、获取通知服务商列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/notifier-provider-list/invoke`

**功能标识：** `notifier-provider-list`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  typeId      |  string     |   是     |  通知类型Id    | |


**返回参数示例**

```json
{
  "result":[
    [
      {
        "type":"sms",
        "id":"test",
        "name":"测试"
      },
      {
        "type":"sms",
        "id":"aliyunSms",
        "name":"阿里云短信服务"
      }
    ]
  ],
  "status":200,
  "code":"success"
}
```

### 3、获取通知配置列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/notifier-config-list/invoke`

**功能标识：** `notifier-config-list`


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


该接口无请求参数

**返回参数示例**

```json
{
  "result":[
    [
    
    ]
  ],
  "status":200,
  "code":"success"
}
```

### 3、获取通知模板列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/notifier-template-list/invoke`

**功能标识：** `notifier-template-list`


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


该接口无请求参数

**返回参数示例**

```json
{
  "result":[
    [
    
    ]
  ],
  "status":200,
  "code":"success"
}
```