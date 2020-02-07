# 权限菜单
权限分配以及菜单加载。

## 权限分配
调用AuthorizationSettingDetailController的saveSettings方法保存分配的权限设置。

**调用该接口前，请您注意：**
- 您必须拥有autz-setting权限。

### 请求参数
名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
targetType | String | 是 | org,role | 设置目标类型(维度)标识，长度32。
targetId | String | 是 | 1215543237527171072 | 设置目标,长度32。
merge | boolean | 否 | true,false | 冲突时是否合并 
priority | int | 否 | 10 | 冲突时优先级
permissionList | List&#60;PermissionInfo&#62; | 否 | | 权限列表，权限信息下面表格单独说明。

权限信息（PermissionInfo）参数说明： 

名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
id | String | 是 | device-product | 权限id，长度32。
actions | Set&#60;String&#62; | 否 | save,query,delete | 授权操作
fieldAccess | List&#60;FieldAccess&#62; | 否 |  | 字段权限 
dataAccess | List&#60;DataAccess&#62; | 否 |  | 数据权限

字段权限信息（FieldAccess）参数说明：

名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
name | String | 否 | id，state | 字段名称
action | Set&#60;String&#62; | 否 | save,query,delete | 操作

数据权限信息（DataAccess）参数说明：

名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
type | String | 否 | org | 维度类型
action | Set&#60;String&#62; | 否 | save,query,delete | 操作
config | Map&#60;String, Object&#62; | 否 |  | 其他配置

### 返回数据
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
result | boolean | true | 保存是否成功
status | int | 200 | 状态码
code | String  | success | 业务编码

### 示例

#### 请求示例
RequestUrl: http(s)://localhost:8844/autz-setting/detail/_save  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  
    Content-Type:application/json  
RequestMethod: POST  

RequestBody:  
{
	"targetType": "user",
	"targetId": "1207872923872436224",
	"merge": true,
	"priority": 10,
	"permissionList": [{
		"id": "device-product",
		"actions": ["save", "query"],
		"fieldAccess": [{
			"name": "name",
			"action": ["query"]
		}],
		"dataAccess": [{
			"type": "org",
			"action": ["query"]
		}]
	}]
} 

#### 正常返回示例

JSON 格式

{
    "result": true,
    "status": 200,
    "code": "success"
}

### 错误码

## 获取权限设置
调用AuthorizationSettingDetailController的getSettings方法根据`设置目标类型标识`和`设置目标`获取权限设置。

**调用该接口前，请您注意：**
- 您必须拥有autz-setting权限。

### 请求参数
名称       | 类型 | 是否必选 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- | ------------- 
targetType | String | 是 | org,role | 设置目标类型(维度)标识，长度32。
targetId | String | 是 | 1215543237527171072 | 设置目标,长度32。

### 返回数据
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
result | AuthorizationSettingDetail | true | 返回值，下表单独说明
status | int | 200 | 状态码
code | String  | success | 业务编码

result属性如下： 
 
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
targetType | String | org,role | 设置目标类型(维度)标识，长度32。
targetId | String  | 1215543237527171072 | 设置目标,长度32。
merge | boolean | true,false | 冲突时是否合并 
priority | int | 10 | 冲突时优先级
permissionList | List&#60;PermissionInfo&#62; | | 权限信息集合。

### 示例

#### 请求示例
http(s)://localhost:8844/autz-setting/detail/{targetType}/{target}  
RequestUrl:http(s)://localhost:8844/autz-setting/detail/user/1207872923872436224  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  

RequestMethod: GET  

#### 正常返回示例
JSON 格式  
{
    "result": {
        "targetType": "user",
        "targetId": "1207872923872436224",
        "merge": true,
        "priority": 10,
        "permissionList": [
            {
                "id": "device-product",
                "actions": [
                    "query",
                    "save"
                ],
                "fieldAccess": [
                    {
                        "name": "name",
                        "action": [
                            "query"
                        ]
                    }
                ],
                "dataAccess": []
            }
        ]
    },
    "status": 200,
    "code": "success"
}

### 错误码

## 菜单增删改查
待完成..

## 获取菜单列表
调用MenuController的getUserMenuAsTree方法获取用户自己的菜单列表。

### 请求参数
无。

### 返回数据
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
result | MenuEntity集合 | true | 返回值，下表单独说明
status | int | 200 | 状态码
code | String  | success | 业务编码

result列表中MenuEntity属性如下： 
 
名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
name | String | 用户管理 | 菜单名称。
describe | String  | 用户管理、用户权限分配 | 菜单描述。
permissionExpression | String | resource:user | 权限表达式，用于权限控制时是否展示该菜单。 
url | String | admin/user/list.html | 菜单对应页面的地址。
icon | String | fa fa-user | 菜单图标。
status | Byte | 1 | 状态
children | List&#60;MenuEntity&#62; | | 子菜单集合。
parentId | String | e9dc96d6b677cbae865670e6813f5e8b | 父级菜单id。
path | String | sOrB-Dz7b | 树结构编码,用于快速查找, 每一层由4位字符组成,用-分割。
sortIndex | Long | 105 | 排序序号。
level | Integer | 2 | 树层级。

### 示例

#### 请求示例
RequestUrl:http(s)://localhost:8844/menu/user-own/tree  

RequestHeader:  
    X-Access-Token:1198ab9ddf6b4ba191d3285debc9dd2d  

RequestMethod: GET  

#### 正常返回示例
{
    "result": [
        {
            "id": "e9dc96d6b677cbae865670e6813f5e8b",
            "parentId": "-1",
            "path": "sOrB",
            "sortIndex": 1,
            "level": 1,
            "name": "系统设置",
            "describe": " ",
            "permissionExpression": "",
            "url": "",
            "icon": "fa fa-cogs",
            "status": 1,
            "children": [
                {
                    "id": "8db17b9ba28dd949c926b329af477a08",
                    "parentId": "e9dc96d6b677cbae865670e6813f5e8b",
                    "path": "sOrB-i2ea",
                    "sortIndex": 102,
                    "level": 2,
                    "name": "菜单管理",
                    "permissionExpression": "resource:menu",
                    "url": "admin/menu/list.html",
                    "icon": "fa fa-navicon",
                    "status": 1
                },
                {
                    "id": "a52df62b69e21fd756523faf8f0bd986",
                    "parentId": "e9dc96d6b677cbae865670e6813f5e8b",
                    "path": "sOrB-X27v",
                    "sortIndex": 103,
                    "level": 2,
                    "name": "权限管理",
                    "permissionExpression": "resource:permission",
                    "url": "admin/permission/list.html",
                    "icon": "fa fa-briefcase",
                    "status": 1
                },
                {
                    "id": "42fc4f83d12cc172e4690937eb15752a",
                    "parentId": "e9dc96d6b677cbae865670e6813f5e8b",
                    "path": "sOrB-rBBu",
                    "sortIndex": 103,
                    "level": 2,
                    "name": "维度管理",
                    "permissionExpression": "resource:dimension",
                    "url": "admin/dimension/list.html",
                    "icon": "fa fa-users",
                    "status": 1
                },
                {
                    "id": "58eba1a4371dd3c0da24fac5da8cadc2",
                    "parentId": "e9dc96d6b677cbae865670e6813f5e8b",
                    "path": "sOrB-Dz7b",
                    "sortIndex": 105,
                    "level": 2,
                    "name": "用户管理",
                    "permissionExpression": "resource:user",
                    "url": "admin/user/list.html",
                    "icon": "fa fa-user",
                    "status": 1
                },
                {
                    "id": "1199957055781179392",
                    "parentId": "e9dc96d6b677cbae865670e6813f5e8b",
                    "path": "sOrB-wCVW",
                    "sortIndex": 106,
                    "level": 2,
                    "name": "OpenApi客户端",
                    "permissionExpression": "resource:open-api",
                    "url": "admin/open-api/list.html",
                    "icon": "fa fa-language",
                    "status": 1
                },
                {
                    "id": "1207271770602430464",
                    "parentId": "e9dc96d6b677cbae865670e6813f5e8b",
                    "path": "sOrB-k12z",
                    "sortIndex": 107,
                    "level": 2,
                    "name": "机构管理",
                    "permissionExpression": "resource:dimension and resource:dimension-type",
                    "url": "admin/org/list.html",
                    "icon": "fa fa-handshake-o",
                    "status": 1
                },
                {
                    "id": "1210045024391901184",
                    "parentId": "e9dc96d6b677cbae865670e6813f5e8b",
                    "path": "sOrB-uNS1",
                    "sortIndex": 108,
                    "level": 2,
                    "name": "角色管理",
                    "permissionExpression": "resource:dimension and resource:dimension-type",
                    "url": "admin/role/list.html",
                    "icon": "fa fa-drivers-license-o",
                    "status": 1
                }
            ]
        },
        {
            "id": "1190175990379909120",
            "parentId": "",
            "path": "Y8ea",
            "sortIndex": 2,
            "level": 1,
            "name": "设备管理",
            "permissionExpression": "",
            "url": "",
            "icon": "fa fa-puzzle-piece",
            "status": 1,
            "children": [
                {
                    "id": "1191174514977075200",
                    "parentId": "1190175990379909120",
                    "path": "Y8ea-vXS7",
                    "sortIndex": 201,
                    "level": 2,
                    "name": "协议管理",
                    "permissionExpression": "resource:protocol-supports",
                    "url": "admin/protocol/list.html",
                    "icon": "fa fa-exchange",
                    "status": 1
                },
                {
                    "id": "1190181865777729536",
                    "parentId": "1190175990379909120",
                    "path": "Y8ea-B1HL",
                    "sortIndex": 202,
                    "level": 2,
                    "name": "设备型号",
                    "permissionExpression": "resource:device-product",
                    "url": "admin/device/product/list.html",
                    "icon": "fa fa-female",
                    "status": 1
                },
                {
                    "id": "1190181869565186048",
                    "parentId": "1190175990379909120",
                    "path": "Y8ea-fZut",
                    "sortIndex": 203,
                    "level": 2,
                    "name": "设备实例",
                    "permissionExpression": "resource:device-instance",
                    "url": "admin/device/instance/list.html",
                    "icon": "fa fa-print",
                    "status": 1
                },
                {
                    "id": "1204318798771785728",
                    "parentId": "1190175990379909120",
                    "path": "Y8ea-uFbf",
                    "sortIndex": 204,
                    "level": 2,
                    "name": "供应商管理",
                    "permissionExpression": "resource:supplier",
                    "url": "admin/supplier/list.html",
                    "icon": "fa fa-building",
                    "status": 1
                }
            ]
        },
        {
            "id": "1205039133958422528",
            "parentId": "",
            "path": "ue7K",
            "sortIndex": 3,
            "level": 1,
            "name": "通知管理",
            "permissionExpression": "",
            "url": "",
            "icon": "fa fa-commenting-o",
            "status": 1,
            "children": [
                {
                    "id": "1205039602281824256",
                    "parentId": "1205039133958422528",
                    "path": "d4By",
                    "sortIndex": 301,
                    "level": 1,
                    "name": "通知配置",
                    "permissionExpression": "resource:notify",
                    "url": "admin/notify/config/list.html",
                    "icon": "fa fa-commenting",
                    "status": 1
                },
                {
                    "id": "1205425344778625024",
                    "parentId": "1205039133958422528",
                    "path": "n2M4",
                    "sortIndex": 302,
                    "level": 1,
                    "name": "通知模板",
                    "permissionExpression": "resource:template",
                    "url": "admin/notify/template/list.html",
                    "icon": "fa fa-book",
                    "status": 1
                }
            ]
        },
        {
            "id": "1199950190410174464",
            "parentId": "",
            "path": "OVPo",
            "sortIndex": 4,
            "level": 1,
            "name": "网络组件",
            "permissionExpression": "",
            "url": "",
            "icon": "fa fa-puzzle-piece",
            "status": 1,
            "children": [
                {
                    "id": "11201123890436267564",
                    "parentId": "1199950190410174464",
                    "path": "OVPo-39ME",
                    "sortIndex": 401,
                    "level": 2,
                    "name": "证书管理",
                    "permissionExpression": "resource:certificate",
                    "url": "admin/certificate/list.html",
                    "icon": "fa fa-get-pocket",
                    "status": 1
                },
                {
                    "id": "1211573464298164224",
                    "parentId": "1199950190410174464",
                    "path": "OVPo-W8qx",
                    "sortIndex": 403,
                    "level": 2,
                    "name": "设备网关",
                    "permissionExpression": "resource:device-gateway",
                    "url": "admin/gateway/device/list.html",
                    "icon": "fa fa-plug",
                    "status": 1
                },
                {
                    "id": "1199954967323971584",
                    "parentId": "1199950190410174464",
                    "path": "OVPo-D9KX",
                    "sortIndex": 404,
                    "level": 2,
                    "name": "组件管理",
                    "permissionExpression": "resource:network-config",
                    "url": "admin/network/config/list.html",
                    "icon": "fa fa-usb",
                    "status": 1
                }
            ]
        },
        {
            "id": "1190181861245626012",
            "parentId": "",
            "path": "aZr2",
            "sortIndex": 5,
            "level": 1,
            "name": "规则引擎",
            "permissionExpression": "",
            "url": "",
            "icon": "fa fa-share-alt",
            "status": 1,
            "children": [
                {
                    "id": "1190181862682626172",
                    "parentId": "1190181861245626012",
                    "path": "aZr2-vs2G",
                    "sortIndex": 501,
                    "level": 2,
                    "name": "规则模型",
                    "permissionExpression": "resource:rule-model",
                    "url": "admin/rule-engine/model/list.html",
                    "icon": "fa fa-share-alt",
                    "status": 1
                },
                {
                    "id": "11201818611562601212",
                    "parentId": "1190181861245626012",
                    "path": "aZr2-QszG",
                    "sortIndex": 502,
                    "level": 2,
                    "name": "规则实例",
                    "permissionExpression": "resource:rule-instance",
                    "url": "admin/rule-engine/instance/list.html",
                    "icon": "fa fa-share-alt",
                    "status": 1
                }
            ]
        },
        {
            "id": "1198950190410174464",
            "parentId": "",
            "path": "OVPo",
            "sortIndex": 6,
            "level": 1,
            "name": "日志管理",
            "permissionExpression": "",
            "url": "",
            "icon": "fa fa-puzzle-piece",
            "status": 1,
            "children": [
                {
                    "id": "1197953754679382016",
                    "parentId": "1198950190410174464",
                    "path": "OVPo-Xmuz",
                    "sortIndex": 601,
                    "level": 2,
                    "name": "访问日志",
                    "permissionExpression": "resource:access-log",
                    "url": "admin/logger/access/list.html",
                    "icon": "fa fa-calendar-minus-o",
                    "status": 1
                },
                {
                    "id": "1196953754679382016",
                    "parentId": "1198950190410174464",
                    "path": "OVPo-Xmuz",
                    "sortIndex": 602,
                    "level": 2,
                    "name": "系统日志",
                    "permissionExpression": "resource:system-log",
                    "url": "admin/logger/system/list.html",
                    "icon": "fa fa-calendar-o",
                    "status": 1
                }
            ]
        },
        {
            "id": "1201401737817882624",
            "parentId": "",
            "path": "Xhg1",
            "sortIndex": 7,
            "level": 1,
            "name": "报表管理",
            "permissionExpression": "",
            "url": "",
            "icon": "fa fa-pie-chart",
            "status": 1,
            "children": [
                {
                    "id": "1201402278006489088",
                    "parentId": "1201401737817882624",
                    "path": "O807",
                    "sortIndex": 701,
                    "level": 1,
                    "name": "报表开发",
                    "permissionExpression": "resource:report-config,user-token",
                    "url": "admin/report/config/list.html",
                    "icon": "fa fa-deaf",
                    "status": 1
                },
                {
                    "id": "1201402363117305856",
                    "parentId": "1201401737817882624",
                    "path": "LyIU",
                    "sortIndex": 702,
                    "level": 1,
                    "name": "报表配置",
                    "permissionExpression": "resource:report-config",
                    "url": "admin/report/layout/list.html",
                    "icon": "fa fa-dashboard",
                    "status": 1
                }
            ]
        }
    ],
    "status": 200,
    "code": "success"
}

### 错误码