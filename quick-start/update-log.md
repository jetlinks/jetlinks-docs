# 更新记录

最新代码托管在[github](https://github.com/jetlinks/jetlinks-community)和
[gitee](https://gitee.com/jetlinks/jetlinks-community)上,master为最新开发分支.

## 1.2-RELEASE

计划更新时间: 2020-06-01

主要优化

1. 实时数据接口从SSE替换为Websocket.
2. 设备数据可视化增加实时数据.
3. 优化Geo地理位置信息,设备信息实时更新.(Pro)
4. 增加固件管理,可对设备固件进行管理,升级等操作.[查看文档](../dev-guide/device-firmware.md) (Pro)
5. 优化网络组件相关调试功能.
6. 优化启动速度
7. 增加试验性多租户功能 [查看文档](../dev-guide/multi-tenant.md) (Pro)

主要BUG修复

1. 修复物模型中,枚举类型可能无效.
2. 修复消息拦截器失效问题.
3. 修复脚本定义协议相关BUG.
4. 修复删除设备信息时地理位置信息不会被删除问题.

## 1.1-RELEASE

更新时间: 2020-05-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.1)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.1)

主要优化:

1. 优化设备消息topic: `/device/{deviceId}` 修改为: `/device/{productId}/{deviceId}`.  
2. 增加设备告警功能.可通过订阅消息网关`/rule-engine/device/alarm/{productId}/{deviceId}/{ruleId}`来处理预警消息.  
3. 增加设备告警界面,支持对设备型号统一配置告警规则以及对单个设备配置告警规则,目前支持动作: 消息通知.  
4. 增加HTTP方式接入,设备或者第三方平台可通过HTTP推送设备数据. (Pro)  
5. 在协议包编码时可直接回复设备消息,场景: 编码消息消息时直接调用第三方平台接口获取设备数据.  
6. 增加`websocket`订阅消息:`/messaging/{token}`,可通过websocket订阅实时数据. 
7. OpenAPI增加token方式,申请token后,通过token发起API请求,不用再签名.(Pro)  
8. 增加Geo支持,可通过`GeoObjectManager`统一管理Geo信息并进行搜索(支持矩形,圆形,多边形区域搜索). (Pro)  
9. 增加数据转发功能,可通过SQL的方式来处理实时数据并转发数据. [查看文档](../best-practices/rule-engine-sql.md)
10. 增加可视化图表配置.

主要BUG修复:

1. 修复动态查询条件无法使用where作为参数的问题.
2. 修复物模型中时间类型自定义格式不生效问题.
3. 修复发布协议可能导致系统阻塞无法访问的问题.
4. 修复MQTT客户端 clientId无效
5. 修复当设备在注册中心失效时(redis数据丢失),同步设备状态无法更新为未激活.
6. 修复发送消息到网关下子设备的消息时,异步消息不生效的问题.
7. 修复物模型有日期类型时,可能无法查询到数据.

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