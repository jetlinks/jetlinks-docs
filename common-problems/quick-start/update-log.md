# 更新记录

最新社区版代码托管在[github](https://github.com/jetlinks/jetlinks-community)和
[gitee](https://gitee.com/jetlinks/jetlinks-community)上.

专业版代码托管在[github](https://github.com/jetlinks/jetlinks-pro)上,购买专业版后可获取专业版代码以及后续更新。

前端代码统一托管在[github](https://github.com/jetlinks/jetlinks-ui-antd)。

::: warning 注意
master为最新开发分支. 线上使用请根据情况切换到对应版本的分支.
:::

## 1.5-RELEASE

预计更新时间: 2020-09-30

代码分支: `master`

主要优化

1. 升级elasticsearch client到7.9
2. 使用WebCliet实现ElasticSearchClient.
4. 优化设备数据存储策略,统一设备数据管理接口`DeviceDataService`.
5. 增加使用时序模块进行`行式存储`和`列式存储`设备属性数据,支持自定义策略. (Pro)
6. 优化同时查询多个设备属性到策略:按属性分组聚合取第一条数据. (Pro)
7. 增加`InfluxDB`存储设备数据. (Pro)
8. 增加`TDengine`存储设备数据.(Pro)
9. 增加swagger接口文档,访问`doc.html`即可. (Pro)
10. 增加网络模拟器,可以在线模拟设备消息了. (Pro)
11. 在DeviceMessage头中可以指定`log`来设置设备日志记录.

功能变更

1. 废弃: `/api/v1/product/`下相关API
2. 增加: `/api/v1/product/{productId}/agg/{agg}/{property}/_query` 聚合查询产品下设备属性API
3. 所有设备属性相关接口,返回值`value`以及`formatValue`修改为于物模型对应.去除`numberValue`,`timeValue`等字段.

修复BUG

1. 修复使用mqtt可能无法进行自注册问题
2. 


## 1.4-RELEASE
更新时间: 2020-09-01

代码分支: `1.4`

主要优化

1. 增加用户个人中心,可修改基本信息.
2. 增加用户消息中心,可动态订阅消息,并查看通知记录.
3. 增加当设备注册中心里设备失效时,自动重新注册功能.
4. 规则引擎增加日志,执行历史查看.
5. websocket订阅设备消息可以实现租户下某个成员所有设备的消息订阅.
6. 规则引擎增加作用域支持.
7. 规则引擎函数节点增加多路输出支持.
8. `spring event`增加响应式事件支持,继承`DefaultAsyncEvent`或实现`AsyncEvent`即可.
9. 弃用`MessageGateway`,重构为消息总线`EventBus`.
10. 增加设备影子,`deviceOperator.getSelfConfig(DeviceConfigKey.shadow)`.
11. 修复设备告警设置多个动作时,只有一个动作生效的问题.
12. 规则引擎http请求节点增加Oauth2认证支持.(PRO)
13. 升级spring-boot到`2.3.3-RELEASE`
14. 优化docker构建方式,使用docker更新时请看升级说明
15. MQTT增加短连接支持,[见文档](../best-practices/sort-link.md)
16. 增加登录验证码支持,通过`captcha.enabled=true`设置开启.
17. 增加密码强度验证支持,通过`hsweb.user.password.validator`进行配置,默认`maxLength=8`,`level=2`
18. 增加文件上传限制,通过`hsweb.upload.file.allow-files`或者`hsweb.upload.file.deny-files`进行配置
19. ReactorQL增加take分组函数.`group by _window('10s'),take(1) -- 10秒取第一条数据`.
20. 优化MQTT Client重试策略.
21. 优化协议加载策略,不重复下载相同的文件.

主要BUG修复

1. 修复规则引擎中使用http请求节点发起`post`,`application/json`请求时参数错误问题
2. 修复通知模版中使用表达式获取集合类型数据时,只能获取第一个元素的问题
3. 修复reactorQL在union实时数据时无效问题
4. 修复首次启动时,初始化表结构可能导致阻塞而无法启动到问题
5. 前端一堆bug修复

::: warning 升级说明

docker方式升级时,需要修改数据卷为: `"./data/upload:/application/static/upload"` 以及`"./data/protocols:/application/data/protocols"`

:::

## 1.3-RELEASE

更新时间: 2020-07-10

代码分支: `1.3`

主要优化 

1. 协议优化，使用数据库保存的协议ID作为协议的ID，此项优化将影响生产功能。(Pro)
2. 增加多租户功能。(Pro) [查看文档](../dev-guide/multi-tenant.md)
3. 增加设备分组。(Pro)
4. 设备告警增加防抖设置。[#8](https://github.com/jetlinks/jetlinks-community/issues/8)
5. 增加转发设备消息到Kafka以及RabbitMQ。(Pro) [查看文档](../dev-guide/send-device-msg-mq.md)
6. 规则引擎重构,完全重写底层实现。
7. 新的规则引擎设计器,使用`node-red`实现。
8. ReactorQL中增加查询设备属性函数:`select device.properties(deviceId)`获取设备最新的全部属性值。
9. ReactorQL增加获取设备标签函数:`select device.tags(deviceId,'tag1','tag2')`。
10. ReactorQL增加设备选择器函数: `select * from device.selector(in_group('test-group'))`。
11. 优化ReactorQL中分组聚合性能,多次聚合时不再驻留内存。
12. 优化协议包加载逻辑,先下载到本地再加载。
13. 性能优化,在某写情况下,造成reactor阻塞导致redis超时。
14. 其他代码细节优化。
15. 升级`spring-boot`到`2.2.8.RELEASE`。
16. 升级`netty`到`4.1.50.Final`。
17. 升级`hsweb-framework`到`4.0.4`。

主要BUG修复

1. 导入标签时无法识别标签类型。
2. 修复首次导入设备，并且含有地理位置标签时，地理位置数据不正确问题。
3. 修改主键默认列长度,解决id可能过长问题。
4. 修复tcp使用脚本方式进行粘拆包使,如果`fixed(0)`可能导致粘拆包失效。
5. 前端一堆bug修复

::: warning 升级说明
1. docker镜像版本号更换为`1.3.0`。
2. 本地构建请使用`1.3`分支。
   
从`1.2`升级到此版本后,请执行以下操作:

1. 重新发布协议
2. 重新编辑设备型号-选择协议-应用配置
3. 重新编辑设备网关-选择新的协议-重启
4. 重新启动规则实例
5. 如果协议出现重复,请删掉redis中`keys '*protocol*'`对应的key,然后重启

:::



## 1.2-RELEASE

更新时间: 2020-06-01

代码分支: `1.2`

主要优化

1. 实时数据接口从SSE替换为Websocket.
2. 设备数据可视化增加实时数据.
3. 优化Geo地理位置信息,设备信息实时更新.(Pro)
4. 增加固件管理,可对设备固件进行管理,升级等操作. (Pro)
5. 优化网络组件相关调试功能.
6. 优化自动DDL,优化启动速度.
7. 增加系统配置功能,可在线配置系统logo,标题等.
8. 增加WebSocket设备接入网关.[#53](https://github.com/jetlinks/jetlinks-ui-antd/issues/53)
9. HTTP,WebSocket设备接入网关支持路由,不同url使用不同协议包解析.
10. 设备告警,数据转发中动作增加设备输出,可以在触发规则时,发送指令给其他设备.
11. 消息通知中增加网络组件,支持HTTP和MQTT通知，可实现推送消息给第三方.[#34](https://github.com/jetlinks/jetlinks-ui-antd/issues/34)
12. 消息通知增加通知记录. 
13. 设备实例中配置信息支持恢复默认. [#28](https://github.com/jetlinks/jetlinks-ui-antd/issues/28)
14. 优化协议调试,支持语法高亮. [#33](https://github.com/jetlinks/jetlinks-ui-antd/issues/33)

主要BUG修复

1. 修复物模型中,枚举类型可能无效.
2. 修复消息拦截器失效问题.
3. 修复脚本定义协议相关BUG.
4. 修复删除设备信息时地理位置信息不会被删除问题.

::: tip 升级说明
1. docker镜像版本号更换为`1.2.0`。
2. 本地构建请使用`1.2`分支。
3. 如果是自己配置的nginx，请检查websocket配置是否正确，可以参考[前端配置](https://github.com/jetlinks/jetlinks-ui-antd/blob/master/docker/nginx.conf).
4. 更新后新功能菜单不会直接展示，重新给用户赋权后即可。
:::

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