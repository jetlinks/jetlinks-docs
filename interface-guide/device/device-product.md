# 设备型号
设备型号的增删改查、发布以及事件数据查询。

## 增删改查
待完成..

## 发布
调用DeviceProductController的deviceDeploy方法发布设备型号。

**调用该接口前，请您注意：**
- 您必须拥有device-product权限。

### 请求参数
名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
productId | String | 是 | 1202041662094827520 | 设备型号id。

### 返回数据
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
result | Integer | 1 | 发布成功返回1
status | int | 200 | 状态码
code | String  | success | 业务编码

### 示例

#### 请求示例
RequestUrl: http(s)://localhost:8844/device-product/deploy/1202041662094827520  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  
    Content-Type:application/json  
RequestMethod: POST  

#### 正常返回示例

JSON 格式

{
    "result": 1,
    "status": 200,
    "code": "success"
}

### 错误码

## 取消发布
调用DeviceProductController的cancelDeploy方法发布设备型号。

**调用该接口前，请您注意：**
- 您必须拥有device-product权限。

### 请求参数
名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
productId | String | 是 | 1202041662094827520 | 设备型号id。

### 返回数据
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
result | Integer | 1 | 发布成功返回1
status | int | 200 | 状态码
code | String  | success | 业务编码

### 示例

#### 请求示例
RequestUrl: http(s)://localhost:8844/device-product/cancelDeploy/1202041662094827520  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  
    Content-Type:application/json  
RequestMethod: POST  

#### 正常返回示例

JSON 格式

{
    "result": 1,
    "status": 200,
    "code": "success"
}

### 错误码

## 查询事件数据
调用DeviceProductController的queryPagerByDeviceEvent方法查询指定设备型号的事件数据。

**调用该接口前，请您注意：**
- 您必须拥有device-product权限。

### 请求参数
名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
productId | String | 是 | smoke001 | 设备型号id。
eventId | String | 是 | fault_alarm | 事件id。
queryParam | QueryParam | 否 |  | 公共查询参数，如deviceId:firedevice。

### 返回数据
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
result | 分页数据 |  | 返回值，下面表格单独说明
status | int | 200 | 状态码
code | String  | success | 业务编码

result的属性如下：

名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
pageIndex | Integer | 0 | 页码
pageSize | Integer | 25 | 每页数据量
total | Integer  | 50 | 总数
data | Map  |  | 根据不同的Event返回不同的数据，此处以烟感器故障事件为例。


### 示例

#### 请求示例
RequestUrl: http(s)://localhost:8844/device-product/smoke001/event/fault_alarm?terms%5B0%5D.column=deviceId&terms%5B0%5D.value=firedevice  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  
    Content-Type:application/json  
RequestMethod: GET  

#### 正常返回示例

JSON 格式

{
    "result": {
      "pageIndex": 0,
      "pageSize": 25,
      "total": 0,
      "data": []  
    },
    "status": 200,
    "code": "success"
}

### 错误码