# 使用MQTT订阅平台相关消息

在`1.5`企业版本后提供mqtt方式订阅平台消息的功能.
可以通过mqtt来订阅设备,规则引擎,设备告警等相关消息.

## 接口

通过配置:
```yml
messaging:
  mqtt:
    enabled: true #开启mqtt支持
    port: 11883 # 端口
    host: 0.0.0.0 #绑定网卡
```

## 认证

默认使用`token`(可以使用`OpenAPI`申请token)作为`clientId`,`username`和`password`可以不填写.

可通过实现接口`MqttAuthenticationHandler`来自定义认证策略.

::: warning 注意
平台topic使用的通配符为`*`，在使用MQTT订阅时需要将通配符转换为mqtt的通配符: `*`转为`+`,`**`转为`#`.
:::

## 订阅设备消息

与消息网关中的设备topic一致,[查看topic列表](../best-practices/start.md#设备消息对应事件总线topic).
消息负载(`payload`)将与[设备消息类型](../best-practices/start.md#平台统一设备消息定义)一致.

::: tip 提示 
1.6版本后支持分组订阅:同一个用户订阅相同的topic,只有其中一个订阅者收到消息.

在topic前增加`$shared`即可,如: `$shared/device/+/+/#`
:::

