# 融合网关功能接口文档-设备

## 请求地址说明
融合网关所有功能接口请求地址均为`/edge/operations/{deviceId}/{functionId}/invoke`。
其中`deviceId`为边缘设备网关设备Id `functionId`为功能标识。

**注意：当在网关本地调用以下接口时，`deviceId`参数值固定为当前网关deviceId**

## 一、复合网关


### 1. 保存复合网关

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-gateway-save/invoke`

**功能标识：** `device-gateway-save`

**请求参数说明**

| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- |
|   id      |  string     |   是     |     id||
|   name    |  string     |    是    |   名称 ||
|description|  string     |    否    | 描述   ||
|  product  |  对象     |    是    | 型号实体所有信息   ||
|  network  |  对象     |    是    | 网络组件信息   ||
|  protocol  |  对象     |    是    | 协议实体信息   ||
|  gatewayProvider  |  String     |    是    | 网关类型   ||
|  configuration  |  对象     |    是    | 网关其他配置  ||


**返回参数示例**

该接口无返回值

### 2. 获取复合网关列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-gateway-list/invoke`

**功能标识：** `device-gateway-list`

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
  "result":[[
    {
      "id":"composite-gateway-01",
      "productId":"hw-somke",
      "productName":"海湾烟感",
      "transport":"MQTT",
      "networkId":"1375055162694078464",
      "networkName":"mqttServer",
      "state":{
        "text":"已停止",
        "value":"disabled"
      }
    }
  ]],
  "status":200,
  "code":"success"
}
```

### 3. 获取复合网关信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-gateway-info/invoke`

**功能标识：** `device-gateway-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  复合网关Id    | |


**返回参数示例**

```json
{
  "result":[
    {
      "id":"composite-gateway-01",
      "productId":"hw-somke",
      "productName":"海湾烟感",
      "transport":"MQTT",
      "networkId":"1375055162694078464",
      "networkName":"mqttServer",
      "state":{
        "text":"已停止",
        "value":"disabled"
      }
    }
  ],
  "status":200,
  "code":"success"
}
```

### 4. 获取网关类型

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-gateway-providers/invoke`

**功能标识：** `device-gateway-providers`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  transportProtocol      |  string     |   是     |  传输协议    | |

**返回参数示例**
```json
{
  "result":[
    [
      {
        "networkType":{
          "name":"MQTT客户端",
          "text":"MQTT客户端",
          "value":"MQTT_CLIENT"
        },
        "name":"MQTT Broker接入",
        "id":"mqtt-client-gateway"
      },
      {
        "networkType":{
          "name":"MQTT服务",
          "text":"MQTT服务",
          "value":"MQTT_SERVER"
        },
        "name":"MQTT直连接入",
        "id":"mqtt-server-gateway"
      }
    ]
  ],
  "status":200,
  "code":"success"
}
```

### 4. 启动复合网关

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-gateway-start/invoke`

**功能标识：** `device-gateway-start`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  网关id    | |


**返回参数示例**

该接口无返回值

### 4.停止复合网关

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-gateway-stop/invoke`

**功能标识：** `device-gateway-stop`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  网关id    | |


**返回参数示例**

该接口无返回值



### 4.删除复合网关

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-gateway-delete/invoke`

**功能标识：** `device-gateway-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  网关id    | |


**返回参数示例**

该接口无返回值



## 网络组件

### 1. 获取网络组件列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/network-config-list/invoke`

**功能标识：** `network-config-list`

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
  "result":[
    [
      {
        "id":"1375055162694078464",
        "name":"mqttServer",
        "type":"MQTT_SERVER",
        "state":{
          "text":"已停止",
          "value":"disabled"
        },
        "shareCluster":false,
        "cluster":[
          {
            "serverId":"jetlinks-platform:8844",
            "configuration":{
              "host":"0.0.0.0",
              "certId":"",
              "maxMessageSize":80960,
              "port":"1883",
              "ssl":""
            }
          }
        ],
        "typeObject":{
          "name":"MQTT服务",
          "text":"MQTT服务",
          "value":"MQTT_SERVER"
        }
      }
    ]
  ],
  "status":200,
  "code":"success"
}

```

### 2. 获取网络组件信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/network-config-info/invoke`

**功能标识：** `network-config-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  网络组件Id    | |


**返回参数示例**

```json
{
  "result":[
    {
      "id":"1375055162694078464",
      "name":"mqttServer",
      "type":"MQTT_SERVER",
      "state":{
        "text":"已停止",
        "value":"disabled"
      },
      "shareCluster":false,
      "cluster":[
        {
          "serverId":"jetlinks-platform:8844",
          "configuration":{
            "host":"0.0.0.0",
            "certId":"",
            "maxMessageSize":80960,
            "port":"1883",
            "ssl":""
          }
        }
      ],
      "typeObject":{
        "name":"MQTT服务",
        "text":"MQTT服务",
        "value":"MQTT_SERVER"
      }
    }
  ],
  "status":200,
  "code":"success"
}

```

## 设备实例

### 1、新建设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-insert/invoke`

**功能标识：** `device-instance-insert`

**请求参数说明**

| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- |
|   id      |  string     |   是     |     id||
|   name    |  string     |    是    |   名称 ||
|description|  string     |    否    | 描述   ||
|  productId  |  string     |    是    | 型号Id   ||
|  productName  |  string     |    是    | 型号名称   ||
|  state  |  string     |    是    | 状态   ||


**返回参数示例**

该接口无返回值

### 2、保存设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-save/invoke`

**功能标识：** `device-instance-save`

**请求参数说明**

| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- |
|   id      |  string     |   否     |     id||
|   name    |  string     |    否    |   名称 ||
|description|  string     |    否    | 描述   ||
|  productId  |  string     |    否    | 型号Id   ||
|  productName  |  string     |    否    | 型号名称   ||
|  state  |  string     |    否    | 状态   ||


**返回参数示例**

该接口无返回值

### 3. 获取设备分页列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-page-list/invoke`

**功能标识：** `device-instance-page-list`

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
  "result":[
    {
      "pageIndex":0,
      "pageSize":25,
      "total":2,
      "data":[
        {
          "id":"somke-device-001",
          "name":"1号海湾烟感设备",
          "describe":"说明",
          "productId":"hw-somke",
          "productName":"海湾烟感",
          "state":{
            "text":"离线",
            "value":"offline"
          },
          "createTime":1617087259037,
          "registryTime":1617154892310
        }
      ]
    }
  ],
  "status":200,
  "code":"success"
}
```
### 4. 获取设备信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-info/invoke`

**功能标识：** `device-instance-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   否     |  设备Id    | |



**返回参数示例**

```json
{
  "result":[
    {
      "id":"somke-device-001",
      "name":"1号海湾烟感设备",
      "describe":"说明",
      "productId":"hw-somke",
      "productName":"海湾烟感",
      "state":{
        "text":"离线",
        "value":"offline"
      },
      "createTime":1617087259037,
      "registryTime":1617154892310
    }
  ],
  "status":200,
  "code":"success"
}
```

### 5. 激活设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-deploy/invoke`

**功能标识：** `device-instance-deploy`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备id    | |


**返回参数示例**

该接口无返回值

### 6. 禁用设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-undeploy/invoke`

**功能标识：** `device-instance-undeploy`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备id    | |


**返回参数示例**

该接口无返回值

### 7. 删除设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-delete/invoke`

**功能标识：** `device-instance-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备id    | |


**返回参数示例**

该接口无返回值


### 8. 获取设备配置定义

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-config-metadata/invoke`

**功能标识：** `device-config-metadata`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备id    | |


**返回参数示例**

```json
{
    "result":[
        [
            {
                "name":"MQTT认证配置",
                "description":"",
                "scopes":[

                ],
                "properties":[
                    {
                        "property":"username",
                        "name":"username",
                        "description":"用户名",
                        "type":{
                            "name":"字符串",
                            "id":"string",
                            "type":"string"
                        },
                        "scopes":[

                        ]
                    },
                    {
                        "property":"password",
                        "name":"password",
                        "description":"密码",
                        "type":{
                            "name":"密码",
                            "id":"password",
                            "type":"password"
                        },
                        "scopes":[

                        ]
                    }
                ]
            }
        ]
    ],
    "status":200,
    "code":"success"
}
```

### 9. 获取设备详情

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-instance-detail/invoke`

**功能标识：** `device-instance-detail`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备id    | |


**返回参数示例**

```json
{
    "result":[
        {
            "id":"2333",
            "name":"实体烟感",
            "protocol":"hw-somke-protocol",
            "protocolName":"海湾烟感",
            "transport":"MQTT",
            "productId":"hw-somke",
            "productName":"海湾烟感",
            "deviceType":{
                "text":"直连设备",
                "value":"device"
            },
            "state":{
                "text":"未启用",
                "value":"notActive"
            },
            "onlineTime":0,
            "offlineTime":0,
            "createTime":1617267220000,
            "registerTime":1617267222000,
            "metadata":"",
            "independentMetadata":false,
            "configuration":{
                "deviceGatewayName":"2号复合网关",
                "deviceGatewayId":"composite-gateway-02"
            },
            "cachedConfiguration":{

            },
            "aloneConfiguration":false,
            "tags":[

            ],
            "binds":[

            ]
        }
    ],
    "status":200,
    "code":"success"
}
```
## 设备产品

### 1、查询设备产品列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-product-list/invoke`

**功能标识：** `device-product-list`

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
    "name":"名称",
    "description":"说明",
    "classifiedId":"-41-",
    "classifiedName":"智能生活",
    "messageProtocol":"gb28181-2016",
    "protocolName":"GB28181/2016",
    "metadata":"",
    "transportProtocol":"UDP",
    "deviceType":{
      "text":"直连设备",
      "value":"device"
    },
    "state":1,
    "creatorId":"1199596756811550720",
    "createTime":1617691061219,
    "orgId":""
  }
]
```

### 2、查询设备产品信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/device-product-info/invoke`

**功能标识：** `device-product-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  产品id    | |

**返回参数示例**

```json
{
  "id":"test-ld",
  "name":"名称",
  "description":"说明",
  "classifiedId":"-41-",
  "classifiedName":"智能生活",
  "messageProtocol":"gb28181-2016",
  "protocolName":"GB28181/2016",
  "metadata":"",
  "transportProtocol":"UDP",
  "deviceType":{
    "text":"直连设备",
    "value":"device"
  },
  "state":1,
  "creatorId":"1199596756811550720",
  "createTime":1617691061219,
  "orgId":""
}
```