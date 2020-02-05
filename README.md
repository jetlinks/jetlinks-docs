# JetLinks 物联网基础平台
JetLinks 是一个物联网基础平台,用于快速建立物联网相关业务系统.

- 集成了各种常见的网络协议(HTTP,TCP,UDP,CoAP)等,并对其进行封装,
实现统一管理,监控,在线调试,在线启动,停止,更新等功能.大大降低网络编程的复杂度.

- 多消息协议支持,可在线配置消息解析规则,将自定义的消息解析为平台统一的消息格式.

- 统一的设备操作API,屏蔽各个厂家`不同协议`不同设备的差异,支持`跨服务`,同步(RRpc),异步的设备消息收发.

- 可视化拖拽规则引擎设计器,灵活可拓展的多种规则节点支持,可通过规则引擎在线动态配置数据,业务处理逻辑.

- 灵活的多维度权限控制,可支持`列`,`行`级别数据权限控制.

# 文档
[用户手册](user-guide)
[开发手册](dev-guide)

# 相关教程

- [启动MQTT服务设备网关服务,接收设备消息](user-guide/course/device-gateway.md)
- [启动MQTT客户端设备网关服务,接收设备消息](user-guide/course/mqtt-client-gateway.md)
- [启动CoAP设备网关服务,接收设备消息](user-guide/course/coap-server-gateway.md)
- [使用自定义消息协议接入设备](user-guide/course/device-connection.md)
- 在网络组件中使用CA证书
- 整合`MQTT`,`CoAP`等网络组件,进行消息路由.
- 转发消息到`RabbitMQ`,`Kafka`等消息中间件.
- 在前端订阅消息网关中的消息.
- [通过规则引擎处理设备事件消息发送钉钉通知](user-guide/course/rule-dingding.md)
- [配置用户权限](user-guide/course/user-permisson.md)
- [使用组织架构进行设备数据权限控制](user-guide/course/org-device-permission-setting.md)
- 自定义消息组件
- 自定义消息网关连接器

