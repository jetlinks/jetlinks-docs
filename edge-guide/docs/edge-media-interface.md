# 融合网关功能接口文档-视频

## 请求地址说明
融合网关所有功能接口请求地址均为`/edge/operations/{deviceId}/{functionId}/invoke`。
其中`deviceId`为边缘设备网关设备Id `functionId`为功能标识。

**注意：当在网关本地调用以下接口时，`deviceId`参数值固定为当前网关deviceId**

## 一、Onvif设备接入

提供设备发现、设备添加、设备信息查看等功能接口

### 1. 发现Onvif设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/onvif-discover/invoke`

**功能标识：**`onvif-discover`

**请求参数说明**

该接口无请求参数

**返回参数示例**

```json

[
  {
    "url":"http://192.168.33.152",//设备地址
    "name":"设备名称" //设备名称
  },
  {
    "url":"http://192.168.33.151",
    "name":"设备名称"
  }
]
```

### 2. 获取Onvif设备信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/onvif-information/invoke`

**功能标识：**`onvif-information`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- |
|   url      |  string     |   是     |     设备地址|
|        username      |  string     |    是     |   账号    |
|       password      |  string     |    是     | 密码    |



**返回参数示例**

```json

{
        "manufacturer":"Dahua", //制造商
        "model":"DH-NVR2104HS-I", //型号
        "firmwareVersion":"4.001.0000000.0, Build Date 2020-07-29", //固件版本
        "serialNumber":"6J0DFE0PAZ9D8C2",  //序列号
        "hardwareId":"V1.0", 硬件版本
        "url":"http://192.168.33.152", //地址
        "name":"Dahua-DH-NVR2104HS-I", //设备名称
        "username":"admin", //账号
        "password":"p@ssw0rd", //密码
        "mediaProfiles":[ //流媒体配置
            {
                "name":"MediaProfile_Channel1_SubStream0", 
                "token":"MediaProfile00000" 
            }
        ]
    }
```

### 3. 添加Onvif设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/onvif-add/invoke`

**功能标识：**`onvif-add`

**请求参数说明**


| 参数 |类型|是否必传| 描述|默认值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  设备id    | |
| url      |  string     |   是     |     设备地址  | |
| username |  string     |    是     |    账号  |  |
| password |  string     |    是     |  密码    | |
| manufacturer |     string    |    是   |    制造商   |   |
| model        |  string       |    是   |    型号     |     |   |
| firmwareVersion     |   string      |   是    |    固件版本     |
| serialNumber        |     string    |   是    |     序列号    |   |
| hardwareId        |     string    |   是    |     硬件版本    |  |
| description        |     string    |   否    |     描述    |   |
| mediaProfiles        |  数组    |   是    |     流媒体配置<br> 示例:<br/>[ //流媒体配置{ <br/>"id":"channel1", //通道Id<br/>"name":"MediaProfile_Channel1_SubStream0" //通道名称,<br/>"token":"MediaProfile00000" //认证token  <br/>}<br/> ] |   |


**返回参数示例**

该接口无返回值

### 4. 获取Onvif设备视频流地址

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/onvif-stream/invoke`

**功能标识：**`onvif-stream`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  设备id    | |
| url      |  string     |   是     |     设备地址  | |
| username |  string     |    是     |    账号  |  |
| password |  string     |    是     |  密码    | |
| mediaProfiles        |  数组对象    |   是    |     流媒体配置 | <br/>[{ <br/>"id":"channel1", //通道<br/>"token":"MediaProfile00000" //认证token <br/>} ]  |


**返回参数示例**

```json
[
    {
        "mediaUri":{
            "uri":"rtsp://admin:p%40ssw0rd@192.168.33.152:554/cam/realmonitor?channel=1&subtype=0&unicast=true&proto=Onvif",
            "timeout":"PT0S",
            "invalidAfterConnect":"true",
            "invalidAfterReboot":"true"
        }
    }
]
```

## 二、流媒体服务接入

提供流媒体服务的新增和列表查询


### 1. 保存流媒体服务

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-server-save/invoke`

**功能标识：** `media-server-save`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  流媒体Id    | |
| name      |  string     |   是     |     流媒体服务名称  | |
| provider |  string     |    是     |    流媒体服务商  |  |
| description |  string     |    否     |  描述    | |
| configuration        |  对象    |   是    |     服务配置示例 | {</br>"apiPort":8180,</br>"formats":["mp4","flv","hls","ts","rtc"],</br>"httpPort":8180,</br>"apiHost":"192.168.22.82",//网关Ip</br>"rtpPort":9100,</br>"secret":"123456", //流媒体密钥</br>"publicHost":"192.168.22.82",//网关Ip</br>"rtmpPort":1935</br>}  |


**返回参数示例**

该接口无返回值

### 2. 获取流媒体服务列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-server-list/invoke`

**功能标识：** `media-server-list`

**请求参数说明**

该接口无请求参数

**返回参数示例**

```json
[
        {
            "id":"default", //流媒体Id
            "name":"默认流媒体服务",
            "provider":"zlmedia", //流媒体服务商
            "description":"描述",
            "configuration":{
                "apiPort":8180,
                "formats":[
                    "mp4",
                    "flv",
                    "hls",
                    "ts",
                    "rtc"
                ],
                "httpPort":8180,
                "apiHost":"192.168.22.82",
                "rtpPort":9100,
                "secret":"123456",
                "publicHost":"192.168.22.82",
                "rtmpPort":1935
            }
        }
    ]
```



## 三、国际级联功能接口接入


### 1. 保存国标级联

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/gb28181-cascade-save/invoke`

**功能标识：**`gb28181-cascade-save`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  国际级联Id    | |
| name      |  string     |   是     |     级联名称  | |
| provider |  string     |    是     |    流媒体服务商  |  |
| status |  string     |    是     |  描述    |enabled、enabled |
| onlineStatus |  string     |    是     |  在线状态    |online、offline |
| proxyStream |  boolean     |    是     |  是否代理视频流</br>当级联上下级网络不互通时为true    | true、false|
| mediaServerId |  string     |    是     |  流媒体Id    | |
| sipConfigs |  数组对象     |    是     |  sip配置    |{</br>"sipId":"34020000002000000002", //sipId（必传）</br>"domain":"3402000000", //SIP域（必传）</br>"charset":"gb2312", //字符集</br>"password":"m123",  //接入密码（必传）</br>"publicAddress":"127.0.0.1", //SIP 公网地址（必传）</br>"localAddress":"0.0.0.0", //SIP本地地址</br>"port":11112, //端口（必传）</br>"publicPort":11112, //公网端口（必传）</br>"clusterNodeId":"jetlinks-platform:8844", //集群节点（必传）</br>"remoteAddress":"192.168.22.82", //SIP 远程地址（必传）</br>"remotePort":11111, //SIP 远程端口（必传）</br> "user":"34020000002000000011", //用户（必传）</br>"localSipId":"34020000002000000011", //SIP本地ID（必传）</br>"name":"测试信令服务", //名称（必传）</br>"manufacturer":"JetLinks", //厂商（必传）</br>"model":"1", //型号（必传）</br>"firmware":"1.0", //版本号（必传）</br>"transport":"UDP", //传输协议,UDP or TCP（必传）</br>"registerInterval":3600, //注册时间间隔（必传）</br>"keepaliveInterval":60, //心跳周期（必传）</br>"keepaliveTimeoutTimes":3 //心跳超时次数</br>                    } |


**返回参数示例**

该接口无返回值


### 2. 启用国标级联

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/gb28181-cascade-enable/invoke`

**功能标识：**`gb28181-cascade-enable`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  国际级联Id    | |


**返回参数示例**

该接口无返回值

### 3. 查询国标级联列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/gb28181-cascade-list/invoke`

**功能标识：** `gb28181-cascade-list`

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
[
        {
            "id":"default-id",
            "name":"测试国际级联",
            "description":"描述",
            "status":{
                "text":"启用",
                "value":"enabled"
            },
            "onlineStatus":{
                "text":"在线",
                "value":"online"
            },
            "sipConfigs":[
                {
                    "sipId":"34020000002000000002",
                    "domain":"3402000000",
                    "stackName":"org.jetlinks.pro.media.gb28181",
                    "charset":"gb2312",
                    "password":"m123",
                    "publicAddress":"127.0.0.1",
                    "localAddress":"0.0.0.0",
                    "port":11112,
                    "publicPort":11112,
                    "clusterNodeId":"",
                    "remoteAddress":"192.168.22.82",
                    "remotePort":11111,
                    "user":"34020000002000000011",
                    "localSipId":"34020000002000000011",
                    "name":"测试信令服务",
                    "manufacturer":"JetLinks",
                    "model":"1",
                    "firmware":"1.0",
                    "transport":"UDP",
                    "registerInterval":3600,
                    "keepaliveInterval":60,
                    "keepaliveTimeoutTimes":3
                }
            ],
            "proxyStream":true,
            "mediaServerId":"default"
        }
    ]
```


### 3. 禁用国标级联

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/gb28181-cascade-disable/invoke`

**功能标识：**`gb28181-cascade-disable`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  国际级联Id    | |


**返回参数示例**

该接口无返回值


### 4. 国标级联-绑定通道

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/gb28181-cascade-bind/invoke`

**功能标识：**`gb28181-cascade-bind`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  cascadeId      |  string     |   是     |  级联Id    | |
|  channelIds      |  字符串数组     |   是     |  通道Id集合    | |


**返回参数示例**

该接口无返回值

### 5. 国标级联-解绑通道

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/gb28181-cascade-unbind/invoke`

**功能标识：**`gb28181-cascade-unbind`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  cascadeId      |  string     |   是     |  级联Id    | |
|  channelIds      |  字符串数组     |   是     |  通道Id集合    | |


**返回参数示例**

该接口无返回值

##  视频播放

### 1、停止视频播放

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-device-stop-play/invoke`

**功能标识：**`media-device-stop-play`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备Id    | |
|  channelId     |  string     |   是     |  通道Id    | |

**返回参数示例**

该接口无返回值

### 2、视频播放

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-device-start-play/invoke`

**功能标识：**`media-device-start-play`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备Id    | |
|  channelId     |  string     |   是     |  通道Id    | |

**返回参数示例**

```json
{
    "result": [
        {
            "streamId": "157Id_34020000002000000998",
            "rtmp": "rtmp://192.168.32.2:1935/live/157Id_34020000002000000998",
            "flv": "http://192.168.32.2:8180/live/157Id_34020000002000000998.flv",
            "hls": "http://192.168.32.2:8180/live/157Id_34020000002000000998.live.mp4"
        }
    ],
    "status": 200,
    "code": "success"
}
```

### 3、云台控制

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-device-ptz/invoke`

**功能标识：**`media-device-ptz`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备Id    | |
|  channelId     |  string     |   是     |  通道Id    | |
|  direct     |  枚举     |   是     |  云台操作    | |
|  speed     |  int     |   是     |  速度,10,20,30...    | |
|  stopDelaySeconds     |  int     |   否     |  停止延迟时间    | |

**返回参数示例**

该接口无返回值

### 4、停止云台控制

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-device-stop-ptz/invoke`

**功能标识：**`media-device-stop-ptz`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备Id    | |
|  channelId     |  string     |   是     |  通道Id    | |

**返回参数示例**

该接口无返回值



## 四、基础功能接口

### 1. 重启

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/restart/invoke`

**功能标识：**`restart`

**请求参数说明**

该接口无请求参数

**返回参数示例**

该接口无返回值

### 2. 保存协议

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/protocol-save/invoke`

**功能标识：**`protocol-save`

**请求参数说明**

| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   是     |  协议Id    | |
|  name      |  string     |   是     |  协议名称    | |
|  description      |  string     |   是     |  说明    | |
|  type      |  string     |   是     |  类型    | |
|  state      |  string     |   是     |  状态    | |
|  configuration      |  对象     |   是     |  配置，根据协议类型动态配置（参考 协议管理 功能）    | |


**返回参数示例**

该接口无返回值

### 3. 获取协议列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/protocol-list/invoke`

**功能标识：**`protocol-list`

**请求参数说明**

该接口无请求参数

**返回参数示例**

```json
[
	{
		"id":"onvif",
		"name":"Onvif"
	},
	{
		"id":"gb28181-2016",
		"name":"GB28181/2016"
	}
]
```

### 4. 查询视频通道

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-channel-list/invoke`

**功能标识：** `media-channel-list`

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
        "pageSize":25,
        "total":3,
        "data":[
            {
                "id":"f00f20be89d4d75a6651adb9a6f25a54",
                "deviceId":"id152",
                "channelId":"channel0",
                "name":"MediaProfile_Channel1_MainStream",
                "manufacturer":"Dahua",
                "model":"DH-NVR2104HS-I",
                "provider":"onvif",
                "status":{
                    "text":"在线",
                    "value":"online"
                },
                "features":[

                ],
                "others":{
                    "name":"MediaProfile_Channel1_MainStream",
                    "id":"channel0",
                    "token":"MediaProfile00000"
                },
                "ptzType":{
                    "text":"未知",
                    "value":0
                }
            }
        ]
    }
```


### 5. 保存视频通道

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-channel-save/invoke`

**功能标识：** `media-channel-save`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  id      |  string     |   否     |  数据Id    | |
|  deviceId      |  string     |   是     |  设备Id   | |
|  channelId      |  string     |   是     |  通道Id    | |
|  name      |  string     |   是     |  通道名称    | |
|  manufacturer      |  string     |   否     |  厂商    | |
|  model      |  string     |   否     |  型号    | |
|  address      |  string     |   否     |  地址    | |
|  provider      |  string     |   否     |  接入方式    | |
|  status      |  string     |   否     |  通道状态    | |
|  features      |  枚举数组     |   否     |  通道配置    | |
|  others      |  对象     |   否     |  其它配置    | |
|  parentChannelId      |  string     |   否     |  父通道Id    | |
|  subCount      |  string     |   否     |  子设备数量    | |
|  ptzType      |  string     |   否     |  云台类型    | |


**返回参数示例**

该接口无返回值

### 6. 获取视频设备列表

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-device-list/invoke`

**功能标识：** `media-device-list`

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
        "pageSize":25,
        "total":1,
        "data":[
            {
                "id":"id152",
                "manufacturer":"Dahua",
                "model":"DH-NVR2104HS-I",
                "firmware":"4.001.0000000.0, Build Date 2020-07-29",
                "host":"192.168.33.152",
                "port":80,
                "provider":"onvif",
                "createTime":1615355122287,
                "channelNumber":3,
                "others":{
                    "password":"p@ssw0rd",
                    "mediaProfiles":[
                        {
                            "id":"channel0",
                            "name":"MediaProfile_Channel1_MainStream",
                            "token":"MediaProfile00000"
                        },
                        {
                            "id":"channel1",
                            "name":"MediaProfile_Channel1_SubStream1",
                            "token":"MediaProfile00001"
                        },
                        {
                            "id":"channel2",
                            "name":"MediaProfile_Channel1_SubStream2",
                            "token":"MediaProfile00002"
                        }
                    ],
                    "url":"http://192.168.33.152",
                    "username":"admin"
                }
            }
        ]
}
```


### 7. 获取视频设备信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-device-info/invoke`

**功能标识：** `media-device-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备Id    | |


**返回参数示例**

```json
{
  "id":"id152",
  "manufacturer":"Dahua",
  "model":"DH-NVR2104HS-I",
  "firmware":"4.001.0000000.0, Build Date 2020-07-29",
  "host":"192.168.33.152",
  "port":80,
  "provider":"onvif",
  "createTime":1615355122287,
  "channelNumber":3,
  "others":{
    "password":"p@ssw0rd",
    "mediaProfiles":[
      {
        "id":"channel0",
        "name":"MediaProfile_Channel1_MainStream",
        "token":"MediaProfile00000"
      },
      {
        "id":"channel1",
        "name":"MediaProfile_Channel1_SubStream1",
        "token":"MediaProfile00001"
      },
      {
        "id":"channel2",
        "name":"MediaProfile_Channel1_SubStream2",
        "token":"MediaProfile00002"
      }
    ],
    "url":"http://192.168.33.152",
    "username":"admin"
  }
}
```

### 7. 获取视频通道设备信息

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-channel-info/invoke`

**功能标识：** `media-channel-info`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  channelDataId      |  string     |   是     |  通道数据Id    | |


**返回参数示例**

```json
{
  "id":"f00f20be89d4d75a6651adb9a6f25a54",
  "deviceId":"id152",
  "channelId":"channel0",
  "name":"MediaProfile_Channel1_MainStream",
  "manufacturer":"Dahua",
  "model":"DH-NVR2104HS-I",
  "provider":"onvif",
  "status":{
    "text":"在线",
    "value":"online"
  },
  "features":[

  ],
  "others":{
    "name":"MediaProfile_Channel1_MainStream",
    "id":"channel0",
    "token":"MediaProfile00000"
  },
  "ptzType":{
    "text":"未知",
    "value":0
  }
}
```



### 8. 删除视频设备

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-device-delete/invoke`

**功能标识：** `media-device-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  deviceId      |  string     |   是     |  设备Id    | |

**返回参数示例**

该接口无返回值

### 9. 删除视频通道

**请求方式：** POST

**请求地址：** `/edge/operations/{deviceId}/media-channel-delete/invoke`

**功能标识：** `media-channel-delete`

**请求参数说明**


| 参数 |类型|是否必传| 描述|示例值|
| ------- | ------- | ------- | ------- | ------- | 
|  channelDataId      |  string     |   是     |  通道数据Id    | |

**返回参数示例**

该接口无返回值