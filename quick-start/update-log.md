# 更新记录

最新代码托管在[github](https://github.com/jetlinks/jetlinks-community)和
[gitee](https://gitee.com/jetlinks/jetlinks-community)上,master为最新开发分支.

## 1.1-RELEASE

计划更新时间: 2020-05-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.1)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.1)

主要优化:

[x] 优化设备消息topic: `/device/{deviceId}` 修改为: `/device/{productId}/{deviceId}`.  
[x] 增加设备告警功能.可通过订阅消息网关`/rule-engine/device/alarm/{productId}/{deviceId}/{ruleId}`来处理预警消息.  
[x] 增加设备告警界面,支持对设备型号统一配置告警规则以及对单个设备配置告警规则,目前支持动作: 消息通知.  
[x] 增加HTTP方式接入,设备或者第三方平台可通过HTTP推送设备数据. (Pro)  
[x] 在协议包编码时可直接回复设备消息,场景: 编码消息消息时直接调用第三方平台接口获取设备数据.  
[x] 增加`websocket`订阅消息:`/messaging/{token}`,可通过websocket订阅实时数据. 
[x] OpenAPI增加token方式,申请token后,通过token发起API请求,不用再签名.  
[x] 增加Geo支持,可通过`GeoObjectManager`统一管理Geo信息并进行搜索(支持矩形,圆形,多边形区域搜索). (Pro)  
[ ] 增加数据转发功能,可通过SQL的方式来处理实时数据并转发数据. 查看文档
[ ] 增加可视化图表配置.

主要BUG修复:

[x] 修复动态查询条件无法使用where作为参数的问题.
[x] 修复物模型中时间类型自定义格式不生效问题.
[x] 修复发布协议可能导致系统阻塞无法访问的问题.

### 文档更新记录

### 2020/4/22
- openApi增加查询geo对象并转为geoJson
- openApi增加设备批量保存
### 2020/4/21
openApi增加Geo对象管理接口

## 1.0-RELEASE

更新时间: 2020-04-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.0)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.0)

主要优化:

1. 重构`elasticsearch-component`模块.
2. 优化`elasticsearch`索引管理,支持自定义策略,如按月对索引分表.
3. 增加`GeoType`类型支持,为未来的`Gis`支持作准备.
4. 增加网关设备功能.见:[通过网关设备接入多个下挂设备](../best-practices/device-gateway-connection.md)
5. 增加属性变更`Dashboard`接口,可同时订阅一个设备对所有属性变更事件.
6. 优化设备协议,增加调试功能. 增加脚本支持(PRO).
7. 增加设备功能调用,可在界面发起功能调用指令.
8. 增加设备标签功能,可自定义设备标签,可通过标签查询设备: where("id$dev-tag","location:重庆市"); //查询标签key为location,value为重庆市的设备.
9. 优化消息网关中的设备消息topic,详见:[从消息网关中订阅设备消息](/dev-guide/subscribe-device-message.md)
10. `DeviceMessage`增加头:`Headers.keepOnline`,用于在TCP短连接的场景保持设备一直在线,仅通过keepalive超时判断离线.
11. 消息网关增加集群支持,在任意一个服务节点都可订阅设备消息.(PRO)
12. 增加UDP设备网关,可通过UDP接入设备了(PRO).
13. 增加CoAP设备网关,可通过CoAP接入设备了(PRO).
14. 设备会话支持手动指定心跳超时时间以及获取客户端地址信息.
15. 优化批量upsert,提升批量导入性能.
16. 性能优化,详见[压力测试](../advancement-guide/benchmark.md).
17. 一系列UI优化.

主要BUG修复:

1. 修复使用save保存的数据中有字段使用默认值时,更新数据会导致字段被更新为默认值的问题.
2. 修复选择网络协议时出现重复选项.
3. 修复设备属性和事件历史记录部分类型无法展示问题.
4. 修复无法将excel大文件导入设备实例的的问题.
5. 修复在某些情况下redis超时后的NPE错误.
6. 修复无法导出设备问题.
7. 修复当设备数量较多时同步设备状态可能导致崩溃的问题.

## 1.0-RC

更新时间: 2020-03-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.0-RC)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.0-RC)

首个预览版,实现基本功能
### 文档更新记录

### 2020/4/20
- 更新常见问题
- 增加内嵌平台所需环境方式启动jetlinks服务

### 2020/4/17
- 更新启动环境要求
- 增加markdown-it-checkbox插件，复选框只读

### 2020/4/15
- 增加平台部署启动环境要求
- 更新常见问题

#### 2020/4/14
- 增加ui启动目录
- 优化查询参数说明

#### 2020/4/13
- 更换OpenAPI、自定义协议开发目录  
- 优化openApi接口
- 更新常见问题

#### 2020/4/12
增加openApi接口列表

#### 2020/4/11
增加自定义协议开发

#### 2020/4/10
增加openApi接入流程，接入demo

#### 2020/4/8 
设备型号上传优化，tcp、mqtt接入优化，修改topic说明

#### 2020/4/3
模拟网关以及子设备上下线，子设备时间上报

#### 2020/4/2
通过网关设备接入多个下挂设备,网关设备手动关联子设备

#### 2020/3/31
快速部署

#### 2020/3/27 

tcp连接设备事件上报演示

#### 2020/3/24

通过tcp接入设备

#### 1.0.0

[文档代码](https://github.com/jetlinks/jetlinks-docs)

主要内容：

1. 快速安装部署。
2. 权限分配以及菜单加载。
3. 规则引擎。
4. 消息协议协定。
5. 物模型说明。
6. 设备消息协议解析SDK。
7. 使用mqtt.fx接入。
8. 通过第三方MQTT服务（EMQ）接入设备。
9. TCP透传方式接入设备。
10. 通过网关设备接入多个下挂设备。
11. OpenApi使用。
12. 平台操作手册。
13. 平台开发手册。
14. 常见问题。

首个版本，提供了基础介绍、安装部署、进阶功能、最佳实践等。