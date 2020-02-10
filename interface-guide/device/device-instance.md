# 设备实例
设备实例的增删改查、单个设备详情获取、全部设备详情获取、设备发布、设备配置获取、设备状态同步以及批量导入导出数据等。

## 增删改查
待完成..

## 发布
调用DeviceInstanceController的deviceDeploy方法发布设备。

**调用该接口前，请您注意：**
- 您必须拥有device-instance权限。

### 请求参数
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
deviceId | String | 是 | 1202041662094827520 | 设备id。

### 返回数据
名称       | 类型 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- 
result | DeviceDeployResult |  | 返回值，下表将单独说明
status | int | 200 | 状态码
code | String  | success | 业务编码

DeviceDeployResult属性如下：

名称       | 类型 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- 
total | Integer |  | 发布成功数量
success | boolean | true | 是否成功
message | String  | success | 业务编码

### 示例

#### 请求示例
RequestUrl: http(s)://localhost:8844/device-instance/deploy/1202041662094827520  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  
    Content-Type:application/json  
RequestMethod: POST  

#### 正常返回示例

JSON 格式

```json
{
    "result": 1,
    "status": 200,
    "code": "success"
}
```

### 错误码

## 取消发布
调用DeviceInstanceController的cancelDeploy方法发布设备。

**调用该接口前，请您注意：**
- 您必须拥有device-instance权限。

### 请求参数
名称       | 类型 | 是否必选 | 示例值 | 描述  
-------------- | ------------- | ------------- | ------------- | ------------- 
deviceId | String | 是 | 1202041662094827520 | 设备id。

### 返回数据
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
result | Integer | 1 | 发布成功返回1
status | int | 200 | 状态码
code | String  | success | 业务编码

### 示例

#### 请求示例
RequestUrl: http(s)://localhost:8844/device-instance/cancelDeploy/1202041662094827520  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  
    Content-Type:application/json  
RequestMethod: POST  

#### 正常返回示例

JSON 格式

```json
{
    "result": 1,
    "status": 200,
    "code": "success"
}
```

### 错误码
