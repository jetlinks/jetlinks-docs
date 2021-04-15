# 融合网关功能接口文档-规则引擎

## 请求地址说明
融合网关所有功能接口请求地址均为`/edge/operations/{deviceId}/{functionId}/invoke`。
其中`deviceId`为边缘设备网关设备Id `functionId`为功能标识。

**注意：当在网关本地调用以下接口时，`deviceId`参数值固定为当前网关deviceId**

## 一、规则实例


### 1. 保存规则实例

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-instance-save/invoke`

**功能标识：** `rule-instance-save`

**请求参数说明**

| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- |
|   id      |  string     |   是     |     id|
|        name      |  string     |    是     |   名称    |
|       description      |  string     |    否    | 描述    |


**返回参数示例**

该接口无返回值

### 2. 获取规则实例列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-instance-list/invoke`

**功能标识：** `rule-instance-list`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


**返回参数示例**

```json
{
  "pageIndex":0,
  "pageSize":8,
  "total":1,
  "data":[
    {
      "id":"111",
      "modelId":"111",
      "name":"111",
      "description":"111",
      "modelType":"node-red",
      "modelMeta":"{"rev":"51e38394e0590da94e73864208a5799d","flows":[{"disabled":false,"id":"111","label":"111","type":"tab","info":"111"}]}",
      "modelVersion":1,
      "createTime":1616725735700,
      "state":{
        "text":"已停止",
        "value":"stopped"
      }
    }
  ]
}
```

### 3. 获取规则实例信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-instance-info/invoke`

**功能标识：** `rule-instance-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |

**返回参数示例**
```json
{
      "id":"111",
      "modelId":"111",
      "name":"111",
      "description":"111",
      "modelType":"node-red",
      "modelMeta":"{"rev":"51e38394e0590da94e73864208a5799d","flows":[{"disabled":false,"id":"111","label":"111","type":"tab","info":"111"}]}",
      "modelVersion":1,
      "createTime":1616725735700,
      "state":{
        "text":"已停止",
        "value":"stopped"
      }
}
```

### 4. 启动规则实例

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-instance-start/invoke`

**功能标识：** `rule-instance-start`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |


**返回参数示例**

```json
{
  "result": "true"
}

```

### 5. 停止规则实例

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-instance-stop/invoke`

**功能标识：** `rule-instance-stop`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |


**返回参数示例**

```json
{
  "result": "true"
}

```

### 6. 删除规则实例

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-instance-delete/invoke`

**功能标识：** `rule-instance-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |


**返回参数示例**

该接口无返回值



### 7. 复制规则实例

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-instance-save/invoke`

**功能标识：** `rule-instance-save`

**请求参数说明**

| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- |
|   id      |  string     |   是     |     id|
|        name      |  string     |    是     |   名称    |
|        modelMeta      |  string     |    是     |   规则模型配置    |
|        modelType      |  string     |    是     |   规则类型    |
|       description      |  string     |    否    | 描述    |



## 执行规则

### 1. 执行任务

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-task-execute/invoke`

**功能标识：** `rule-task-execute`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |
|  taskId      |  string     |   是     |  任务Id    | |
|  ruleData      |  对象     |   是     |  {<br/>"id":"节点Id",<br/>"contextId":"上下文Id",<br/>"data":{//数据},<br/>"headers":{//节点头部信息}<br/>}    | |

**返回参数示例**

该接口无返回值

### 2. 获取规则引擎执行事件

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-execute-events/invoke`

**功能标识：** `rule-engine-execute-events`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |

**返回参数示例**

```json
{
  "result":{
    "pageIndex":0,
    "pageSize":10,
    "total":1,
    "data":[
      {
        "id":"26dae0a4-5346-4748-bb62-6771895ceb74",
        "event":"error",
        "createTime":1616737500013,
        "instanceId":"test",
        "nodeId":"54c10747.e54cc8",
        "ruleData":"{}",
        "contextId":"8d69f6c9-681d-47be-bb0c-4e0ce187fd9e"
      }
    ]
  },
  "status":200,
  "code":"success"
}
```

### 3. 获取规则引擎执行日志

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-execute-logs/invoke`

**功能标识：** `rule-engine-execute-logs`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |

**返回参数示例**

```json
{
  "result":{
    "pageIndex":0,
    "pageSize":10,
    "total":1,
    "data":[
      {
        "id":"26dae0a4-5346-4748-bb62-6771895ceb74",
        "instanceId":"test",
        "nodeId":"节点Id",
        "level":"日志级别",
        "message":"内容",
        "createTime":"1616737500013",
        "timestamp":"日志时间",
        "context":"上下文数据"
      }
    ]
  },
  "status":200,
  "code":"success"
}
```

## 场景规则

### 1. 保存场景规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-save/invoke`

**功能标识：** `rule-engine-scene-save`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |
|  name      |  string     |   是     |  场景名称    | |
|  actions      |  数组对象     |   是     |  执行动作    | |
|  parallel      |  string     |   是     |  是否并行执行动作    | |
|  triggers      |  数组对象     |   否     |  触发条件    | |

**返回参数示例**

该接口无返回值

### 2. 获取场景规则分页列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-page-list/invoke`

**功能标识：** `rule-engine-scene-page-list`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


**返回参数示例**

```json
{
  "pageIndex":0,
  "pageSize":8,
  "total":1,
  "data":[
    {
      "id":"test-ld",
      "name":"测试场景联动",
      "triggers":[
        {
          "trigger":"timer",
          "cron":"1213123"
        }
      ],
      "parallel":false,
      "actions":[
        {
          "executor":"device-message-sender",
          "configuration":{
            "notifyType":"dingTalk",
            "productId":"edge-gateway-product",
            "message":{
              "messageType":"WRITE_PROPERTY",
              "properties":{
                "cpuTemp":"121"
              }
            },
            "deviceId":"edge-gateway-device-001"
          }
        }
      ],
      "state":{
        "text":"已停止",
        "value":"stopped"
      }
    }
  ]
}
```

### 3. 获取场景规则列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-list/invoke`

**功能标识：** `rule-engine-scene-list`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  ruleInstanceId      |  string     |   是     |  规则实例id    | |
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


**返回参数示例**

```json
[
  {
    "id":"test-ld",
    "name":"测试场景联动",
    "triggers":[
      {
        "trigger":"timer",
        "cron":"1213123"
      }
    ],
    "parallel":false,
    "actions":[
      {
        "executor":"device-message-sender",
        "configuration":{
          "notifyType":"dingTalk",
          "productId":"edge-gateway-product",
          "message":{
            "messageType":"WRITE_PROPERTY",
            "properties":{
              "cpuTemp":"121"
            }
          },
          "deviceId":"edge-gateway-device-001"
        }
      }
    ],
    "state":{
      "text":"已停止",
      "value":"stopped"
    }
  }
]
```

### 4. 执行场景规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-execute/invoke`

**功能标识：** `rule-engine-scene-execute`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |
|  args      |  对象     |   否     |  执行参数    | |

**返回参数示例**

该接口无返回值

### 5. 删除场景规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-delete/invoke`

**功能标识：** `rule-engine-scene-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |

**返回参数示例**

该接口无返回值


### 6. 启动场景规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-start/invoke`

**功能标识：** `rule-engine-scene-start`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |

**返回参数示例**

该接口无返回值

### 7. 停止场景规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-stop/invoke`

**功能标识：** `rule-engine-scene-stop`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |

**返回参数示例**

该接口无返回值

### 8. 获取场景规则信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-scene-info/invoke`

**功能标识：** `rule-engine-scene-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |

**返回参数示例**

```json
{
    "id":"test-ld",
    "name":"测试场景联动",
    "triggers":[
      {
        "trigger":"timer",
        "cron":"1213123"
      }
    ],
    "parallel":false,
    "actions":[
      {
        "executor":"device-message-sender",
        "configuration":{
          "notifyType":"dingTalk",
          "productId":"edge-gateway-product",
          "message":{
            "messageType":"WRITE_PROPERTY",
            "properties":{
              "cpuTemp":"121"
            }
          },
          "deviceId":"edge-gateway-device-001"
        }
      }
    ],
    "state":{
      "text":"已停止",
      "value":"stopped"
    }
}
```


## 告警配置

### 1. 保存告警配置

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-save/invoke`

**功能标识：** `rule-engine-alarm-save`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  Id    | |
|  name      |  string     |   是     |  名称    | |
|  description      |  string     |   否     |  说明    | |
|  target      |  string     |   是     |  device或者product    | |
|  targetId      |  string     |   是     |  deviceId或者productId    | |
|  alarmRule      |  对象     |   是     |  规则配置    | |

**返回参数示例**

该接口无返回值

### 2. 获取告警配置规则分页列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-page-list/invoke`

**功能标识：** `rule-engine-alarm-page-list`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


**返回参数示例**

```json
{
  "pageIndex":0,
  "pageSize":8,
  "total":1,
  "data":[
    {
      "id":"test-ld",
      "name":"测试场景联动",
      "description":"说明",
      "target":"device",
      "targetId":"deviceId",
      "alarmRule": {
      }
    ,
      "state":{
        "text":"已停止",
        "value":"stopped"
      }
    }
  ]
}
```

### 3. 获取告警配置规则列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-list/invoke`

**功能标识：** `rule-engine-alarm-list`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


**返回参数示例**

```json
[
  {
    "id":"test-ld",
    "name":"测试场景联动",
    "description":"说明",
    "target":"device",
    "targetId":"deviceId",
    "alarmRule": {
    }
  ,
    "state":{
      "text":"已停止",
      "value":"stopped"
    }
  }
]
```

### 4. 启动告警配置规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-start/invoke`

**功能标识：** `rule-engine-alarm-start`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |
|  args      |  对象     |   否     |  执行参数    | |

**返回参数示例**

该接口无返回值

### 5. 删除告警配置规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-delete/invoke`

**功能标识：** `rule-engine-alarm-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |

**返回参数示例**

该接口无返回值


### 6. 停止告警配置规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-stop/invoke`

**功能标识：** `rule-engine-alarm-stop`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  场景Id    | |

**返回参数示例**

该接口无返回值

### 7. 获取告警配置规则信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-info/invoke`

**功能标识：** `rule-engine-alarm-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  target      |  string     |   是     |  device或product    | |
|  targetId      |  string     |   是     |  deviceId或productId    | |

**返回参数示例**

```json
{
  "id":"test-ld",
  "name":"测试场景联动",
  "description":"说明",
  "target":"device",
  "targetId":"deviceId",
  "alarmRule": {
  }
,
  "state":{
    "text":"已停止",
    "value":"stopped"
  }
}
```

## 告警记录


### 1. 获取告警记录规则分页列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-history-page-list/invoke`

**功能标识：** `rule-engine-alarm-history-page-list`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  where      |  string     |   否     |  where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16    | |
|  orderBy      |  string     |   否     |  orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc    | |
|  total      |  string     |   否     |  设置了此值后将不重复执行count查询总数    | |
|  paging      |  string     |   否     |  是否分页    | |
|  firstPageIndex      |  string     |   否     |  第一页索引    | |
|  pageSize      |  对象     |   否     |  每页数量    | |


**返回参数示例**

```json
{
  "pageIndex":0,
  "pageSize":8,
  "total":1,
  "data":[
    {
      "id":"test-ld",
      "productId":"产品Id",
      "productName":"产品名称",
      "deviceId":"设备ID",
      "deviceName":"设备名称",
      "alarmId":"告警ID",
      "alarmName":"告警名称",
      "alarmTime":"告警时间",
      "updateTime":"修改时间",
      "description":"说明",
      "alarmData": {
      }
    ,
      "state":"状态"
    }
  ]
}
```


### 2. 删除告警记录规则

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-history-delete/invoke`

**功能标识：** `rule-engine-alarm-history-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  Id    | |

**返回参数示例**

该接口无返回值


### 3. 修改告警记录

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-history-update/invoke`

**功能标识：** `rule-engine-alarm-history-update`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  Id    | |
|  state      |  string     |   是     |  状态    | |
|  descriptionMono      |  string     |   否     |  备注    | |

**返回参数示例**

该接口无返回值

### 4. 告警记录信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/rule-engine-alarm-history-info/invoke`

**功能标识：** `rule-engine-alarm-history-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  告警id    | |

**返回参数示例**

```json
{
  "id":"test-ld",
  "productId":"产品Id",
  "productName":"产品名称",
  "deviceId":"设备ID",
  "deviceName":"设备名称",
  "alarmId":"告警ID",
  "alarmName":"告警名称",
  "alarmTime":"告警时间",
  "updateTime":"修改时间",
  "description":"说明",
  "alarmData": {
  }
,
  "state":"状态"
}
```