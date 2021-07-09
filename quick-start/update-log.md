# 更新记录

最新社区版代码托管在[github](https://github.com/jetlinks/jetlinks-community)和
[gitee](https://gitee.com/jetlinks/jetlinks-community)上.

企业版代码托管在[github](https://github.com/jetlinks/jetlinks-pro)上,购买企业版后可获取企业版代码以及后续更新。

前端代码统一托管在[github](https://github.com/jetlinks/jetlinks-ui-antd)。

::: warning 注意
master为最新开发分支. 线上使用请根据情况切换到对应版本的分支.
:::

当前最新稳定版本`1.9-RELEASE`,对应代码分支`1.9`.
## 1.10-RELEASE

预计更新时间: 2021-07-31

代码分支: `master`

1. 增加批量下发设备指令功能,支持查看下发记录,自动重试等(Pro)
2. 上报属性和读取属性回复增加`属性源时间`和`属性状态`;行式存储时,会使用源时间当作时间戳进行存储.
3. GB28181视频接入支持预置位、看守位指令。(Pro)
4. ClickHouse增加指定存储策略,支持集群轮询写,分布式读。(Pro)
5. HTTP消息增加文件上传支持`HttpExchangeMessage.multiPart()`。(Pro)
6. JetLinks后端接口国际化支持(jsr303,枚举(`I18nEnumDict`),异常(`I18nSupportException`))。[查看说明](../dev-guide/i18n.md)
7. 提供对游标分页查询支持,部分数据库可能不支持offset方式分页,当分页结果中`scoll`为`true`时,表示游标分页,此时不支持使用`pageIndex`进行分页,下一页查询时需要在动态查询条件中指定上一页返回的`scrollId`:`"context":{"scrollId":"上一页的ID"}`,并且查询条件变化后,需要重置页码以及`scrollId`.
8. 设备数据存储策略增加`cassandra`支持,可将设备数据写入到`cassandra`中(Pro).

## 1.9-RELEASE

更新时间: 2021-05-31

代码分支: `1.9`

1. 增加设备独立物模型支持,可给单独的设备配置物模型.
2. 基本实现GB28181国标视频设备接入,支持`直播`,`云台控制`,`级联操作`.(选配模块)
3. RabbitMQ增加`routeKey`配置,可在配置文件中指定`device.message.writer.rabbitmq.consumer-route-key`和`device.message.writer.rabbitmq.producer-route-key`.（Pro）
4. 当设置了`device.message.writer.rabbitmq.consumer=false`时,不创建MQ消费者.(Pro)
5. 适配`tdengine 2.0.16.0`,优化sql长度策略. (pro)
6. 优化规则引擎编辑器,实现组件模块化动态加载.（Pro）
7. 修复启动服务时,如果某个产品物模型发布失败,导致后面的产品终止发布的问题.
8. 增加`ignoreLatest`消息头,`message.addHeader("ignoreLatest",true)` 忽略记录最新数据到数据库.
9.  修复租户下操作设备告警提示无权限.(Pro)
10. 优化租户在解绑成员时,同时解绑成员的资产信息.(Pro)
11. 优化子设备消息回复处理
12. 物模型属性增加存储方式功能,可配置部分属性不存储.
13. 增加虚拟属性功能,可通过规则来计算出虚拟属性值.(Pro)
14. 增加租户成员绑定(`TenantMemberBindEvent`),解绑(`TenantMemberUnBindEvent`)事件.可通过`spring-event`订阅处理此事件.(Pro)
15. 优化子设备状态检查，当检查子设备状态时，将会尝试发送`ChildDeviceMessage<DeviceStateCheckMessage>`给网关，处理后返回`ChildDeviceMessageReply<DeviceStateCheckMessageReply>`.
16. 增加`ClickHouse`设备数据存储策略支持.(试验性)(Pro)
17. 增加权限过滤功能,可配置禁止赋予自己没有的权限给其他用户.`hsweb.permission.filter`相关配置
18. 设备和产品的租户绑定逻辑优化: 绑定设备时，自动绑定产品.解绑产品时,自动解绑设备.(Pro)
19. 用户管理增加租户权限控制.(Pro)
20. 当向`keepOnline`的设备发送消息时,如果原始连接已断开,将返回`CONNECTION_LOST`错误.
21. 设置`keepOnline`的会话将被持久化,重启服务后自动恢复.(Pro)
22. 默认关闭设备最新数据存储,通过`jetlinks.device.storage.enable-last-data-in-db=true`开启.(Pro)
23. 属性物模型增加属性值来源,配置为`手动`时,在发送修改属性指令(`WritePropertyMessage`)时,将直接生效,不会发送到设备.
24. 优化租户资产解绑逻辑,当删除数据时,解绑资产全部的绑定关系.(Pro)
25. 用户管理,机构管理增加租户端支持,租户可以自己管理自己的用户和机构.(Pro)

## 1.8-RELEASE

更新时间: 2021-01-12

代码分支: `1.8`

1. 固件相关消息增加`size`属性
2. 网络组件在集群下可以单独指定节点的配置了(Pro)
3. 修复邮件通知无法发送base64图片问题
4. 优化设备详情配置查看,通过`device.setConfig`设置的配置也可以在设备详情中查看到.
5. 增加OneNet和CTWing接入,可将设备同步到对应到平台中.以及从对应平台订阅数据(Pro可选模块)
6. 设备实例接口增加批量发送设备指令`/device/instance/messages`,可在消息体中指定`deviceId`或者通过`where`条件指定要发送到的设备.(Pro)
7. 优化设备最新消息存储,同一个产品的数据使用串行更新,防止死锁,并且丢弃来不及更新的数据.(Pro)
8. 修复es索引中存在多个mapping时无法解析的问题
9. 修复分组聚合查询属性时,如果返回过多分组时可能导致无法返回结果的问题.
10. 支持将`object`和`array`类型的属性使用`json字符串`来进行存储,减少es的压力.
11. 修复无法转发设备消息到`rabbitMQ`和`kafka`.
12. 增加默认物模型,在协议包里可配置默认物模型,创建产品时,自动添加到物模型配置中.
13. 发送功能调用指令时,增加参数校验和参数类型转换.(可通过`headers.force`跳过校验)
14. 优化设备分类数据初始化,默认使用`-`进行id分隔(使用`|`可能导致400错误.)
15. 通过jvm参数`-Djetlinks.eventbus.payload.pool.enabled=true`开启事件总线对象池,提升性能.
16. 修复在多租户可能存在的事务问题,表现: 无删除权限,仍然执行了资产解绑操作.(Pro)
17. 优化`elasticsearch 7.2.0`以上版本的聚合查询

## 1.7-RELEASE

更新时间: 2020-12-1

代码分支: `1.7`

1. 优化设备配置定义,可以指定配置作用域(单独设置产品的配置或者设备的配置),见`DefaultConfigMetadata.add`方法.
2. 设备标签查询(`id$dev-tag`)支持表达式: `tag1=value1 and tag2=value2`(没有sql注入,放心使用).
3. 增加支持数据库维护产品分类.
4. 优化拉取固件升级逻辑,使用`headers.force`标记是否强制拉取固件,使用`headers.latest`标记是否拉取最新的固件信息. (Pro)
5. 优化设备消息转发逻辑,在`headers`中增加`members`,标记此设备所属的用户id.(需要重新激活设备) (Pro)
6. 优化告警推送逻辑,解决租户添加了告警通知时,无法订阅新添加的设备告警问题.(Pro)
7. 增加`Headers.mergeLatest`消息头,设备属性相关消息设置此头,将合并旧的属性数据到消息中.(此操作会降低系统吞吐量)(Pro)
8. 修复集群下,修改物模型后其他节点不会生效问题。
9. 优化设备指令下发,指令消息ID和设备id进行绑定防止重复。
10. 事件总线集群通信增加RSocket支持以提升性能,通过配置`jetlinks.event-bus.roskcet.enabled`开启.(Pro)
11. ReactorQL增加`_window_until`和`_window_until_change`函数.(Pro)
12. 优化事件总线性能,在使用`TopicPayload`后需要手动调用`release()`释放资源.
13. 优化`设备数据行式存储策略`:取消存储`propertyName`,`formatValue`字段,`influxdb`下只存储`numberValue`,`timeValue`,`value`.
14. 优化物模型拓展信息配置,可在协议包中`support.setExpandsConfigMetadata`来指定配置(需要升级jetlinks-core 1.1.4)
15. 增加内存使用检查,当内存使用剩余低于15%时,丢弃请求防止内存溢出导致系统崩溃.可通过启动参数`-Dmemory.waterline=0.15`进行配置.(Pro)
16. 优化`elasticsearch`和`influxdb`写入策略,丢弃无法写入的Buffer防止内存溢出.(Pro)
17. 优化设备网关消息处理逻辑、优化子设备上线处理逻辑、优化自注册逻辑、离线消息无论设备是否已经离线,事件总线都将会收到消息.
18. 优化`influxdb`查询条件的类型转换，修复如果使用数字作为设备id，无法查询到数据.(Pro)
19. 设备数据转发到机构topic中`/org/device/**`,可通过`headers.orgId`获取当前设备机构ID。(需要再次激活设备).
20. 可通过`DeviceMessage.addHeader("ignoreLog",true)`指定不记录此消息日志.

## 1.6-RELEASE

更新时间: 2020-10-30

代码分支: `1.6`

主要优化

1. 集成OAuth2 Server功能。
2. 优化设备分组,支持多级分组,可通过`id$dev-group-tree`查询分组及子分组设备。
3. 集成小度智能家居平台,`小度小度,打开空调`.（Pro可选模块）
4. 增加设备消息转发到`租户`,`设备分组`对应的消息总线,**更新后请重新激活设备才能生效**(Pro)。
5. 通过mqtt订阅设备消息,使用`$shared/`作为前缀时,同一个用户只会收到一个消息通知.
6. ReactorQL增加列转行,使用`$this`作为别名时,将列对象填充到当前行中.
7. ReactorQL增加行跟踪,通过`row.index`获取行号,通过`row.elapsed`获取距离上一行的时间间隔(ms).
8. ReactorQL增加更多统计函数,`slope(斜度),skewness(偏度特征),kurtosis(峰度特征),variance(方差)....`[查看函数说明](../dev-guide/reactor-ql.md#sql支持列表)(Pro).
9. ReactorQL增加设备历史数据和最新数据查询函数[查看SQL说明](../dev-guide/reactor-ql.md#device-properties-history)(Pro).
10. 网络组件`MQTT Client`中的`clientId`,`username`,`password`支持表达式.可通过`${#env.getProperty(...)}`来获取配置文件中的配置.
11. 增加场景联动规则接口(Pro).
12. 优化对redis cluster的支持.

::: warning 更新说明
原配置`elasticsearch.client`相关配置已失效，请使用`spring.data.elasticsearch.client.reactive.endpoints`进行配置.
:::

## 1.5-RELEASE

更新时间: 2020-09-30

代码分支: `1.5`

主要优化

1. 升级elasticsearch client到7.9
2. 使用WebCliet实现ElasticSearchClient.
4. 优化设备数据存储策略,统一设备数据管理接口`DeviceDataService`.
5. 增加使用时序模块进行`行式存储`和`列式存储`设备属性数据,支持自定义策略. 
6. 优化同时查询多个设备属性到策略:按属性分组聚合取第一条数据. 
7. 增加`InfluxDB`存储设备数据. (Pro)
8. 增加`TDengine`存储设备数据.(Pro)
9. 增加swagger接口文档,访问`doc.html`即可. 
10. 增加网络模拟器,可以在线模拟设备消息了. (Pro)
11. 在DeviceMessage头中可以指定`log`来设置设备日志记录.
12. 增加使用mqtt来订阅平台设备消息. [查看文档](../dev-guide/mqtt-subs.md) (Pro)
13. 增加统一单点登录功能. [查看文档](../dev-guide/sso.md) (Pro)
14. 增加按设备和产品聚合查询多个属性API
15. 增加保存设备最新的数据到数据库中,以支持可根据最新数据来统计设备数量等需求.[查看文档](../best-practices/start.md#记录设备最新数据到数据库)(Pro)
16. 增加按时间聚合查询前N条设备属性数据接口:`/device/instance/{deviceId}/properties/_top/{numberOfTop}`(仅默认存储策略(es)支持).

功能变更

1. 废弃: `/api/v1/product/`下相关API
2. 废弃: `/api/v1/device/{deviceId}/properties/_query` API,使用`/device/instance/{deviceId}/property/{property}/_query`代替
3. 设备属性相关接口,返回值`value`以及`formatValue`修改为与物模型对应的类型值.

修复BUG

1. 修复使用mqtt可能无法进行自注册问题
2. 修复无法从设备告警中获取设备名称问题
3. 修复禁用规则引擎节点后发布会导致NPE问题
4. 修复设备不存在时无法转发设备消息问题
5. 修复es配置`number_of_shards`失效问题
6. 若干前端UI问题修复


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