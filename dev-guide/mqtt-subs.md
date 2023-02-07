[comment]: <> (# 使用MQTT订阅平台相关消息)

[comment]: <> (在`1.5`企业版本后提供mqtt方式订阅平台消息的功能.)

[comment]: <> (可以通过mqtt来订阅设备,规则引擎,设备告警等相关消息.)

[comment]: <> (## 接口)

[comment]: <> (通过配置:)

[comment]: <> (```yml)

[comment]: <> (messaging:)

[comment]: <> (  mqtt:)

[comment]: <> (    enabled: true #开启mqtt支持)

[comment]: <> (    port: 11883 # 端口)

[comment]: <> (    host: 0.0.0.0 #绑定网卡)

[comment]: <> (```)

[comment]: <> (## 认证)

[comment]: <> (默认使用`token`&#40;可以使用`OpenAPI`申请token&#41;作为`clientId`,`username`和`password`可以不填写.)

[comment]: <> (可通过实现接口`MqttAuthenticationHandler`来自定义认证策略.)

[comment]: <> (::: warning 注意)

[comment]: <> (平台topic使用的通配符为`*`，在使用MQTT订阅时需要将通配符转换为mqtt的通配符: `*`转为`+`,`**`转为`#`.)

[comment]: <> (:::)

[comment]: <> (## 订阅设备消息)

[comment]: <> (与消息网关中的设备topic一致,[查看topic列表]&#40;../best-practices/start.md#设备消息对应事件总线topic&#41;.)

[comment]: <> (消息负载&#40;`payload`&#41;将与[设备消息类型]&#40;../best-practices/start.md#平台统一设备消息定义&#41;一致.)

[comment]: <> (::: tip 提示 )

[comment]: <> (1.6版本后支持分组订阅:同一个用户订阅相同的topic,只有其中一个订阅者收到消息.)

[comment]: <> (在topic前增加`$shared`即可,如: `$shared/device/+/+/#`)

[comment]: <> (:::)

