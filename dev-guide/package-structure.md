# 包结构

## JetLinks pro2.0

```bash
---jetlinks-pro2.0
------|---dist # docker容器部署配置文件
------|---expands-components # 扩展模块
------|-------|----jetlinks-aliyun-bridge-gateway # 阿里云IoT平台接入
------|-------|----jetlinks-bacnet # BACnet 支持
------|-------|----jetlinks-ctwing # 电信Ctwing物联网平台对接
------|-------|----jetlinks-dueros # 小度智能家居开放平台集成
------|-------|----jetlinks-edge # 边缘网关模块
------|-------|-------|---edge-agent # 边缘端模块
------|-------|-------|---edge-collector # 边缘端数据采集模块
------|-------|-------|---edge-core # 边缘网关核心模块
------|-------|-------|---edge-device # 边缘网关设备接入模块
------|-------|-------|---edge-master # 边缘网关平台管理端模块
------|-------|-------|---edge-media # 边缘流媒体模块,用于在边缘网关中接入视频设备
------|-------|-------|---edge-rule-engine # 边缘网关规则引擎模块
------|-------|----jetlinks-lwm2m # lwm2m支持
------|-------|----jetlinks-media # 音视频流媒体管理模块,实现GBT 28181相关协议功能。
------|-------|----jetlinks-miot # 小米IOT平台接入
------|-------|----jetlinks-modbus # modbus支持
------|-------|----jetlinks-onenet # 移动OneNet平台对接
------|-------|----jetlinks-opc-ua # 通过OPC-UA接入设备
------|---jetlinks-components   # 组件库
------|-------|----api-component # api模块 对API鉴权,swagger等集成
------|-------|----application-component # 应用管理模块
------|-------|----assets-component # 资产模块 对数据权限控制的支持
------|-------|----cassandra-component # cassandra集成 对cassandra的支持
------|-------|----clickhouse-component # clickhouse集成 对clickhouse的支持
------|-------|----collector-component # 数据采集器模块 统一定义平台主动采集的规范以及相关接口
------|-------|----common-component # 通用组件.工具类等
------|-------|----configure-component # 统一配置模块
------|-------|----dashboard-component # 仪表盘模块
------|-------|----datasource-component # 数据源管理配置模块
------|-------|----elasticsearch-component # ElasticSearch集成
------|-------|----function-component # 函数模块
------|-------|----gateway-component # 网关模块,统一定义网关接口等信息
------|-------|----geo-component # 地理位置模块
------|-------|----influxdb-component # 对influxdb的支持
------|-------|----io-component # IO模块,文件管理等
------|-------|----logging-component # 日志模块
------|-------|----messaging-component # 消息模块,集成消息队列等
------|-------|-------|---kafka-component # Kafka模块
------|-------|-------|---rabbitmq-component # RabbitMQ模块
------|-------|----network-component # 网络组件模块,统一定义网络组件规范以及默认实现
------|-------|----notify-component # 通知模块,统一定义通知规范以及默认实现
------|-------|----plugin-component # 插件模块
------|-------|----protocol-component # 协议模块
------|-------|----relation-component # 关系模块,用于描述物与物之间的关系
------|-------|----rule-engine-component # 规则引擎模块,集成规则引擎通用功能
------|-------|----script-component # 脚本模块,封装脚本引擎
------|-------|----streaming-component # 流式计算模块(暂未实现)
------|-------|----tdengine-component # 对tdengine的支持
------|-------|----tenant-component # 租户模块(已弃用)
------|-------|----test-component # 测试模块
------|-------|----things-component # 物管理模块
------|-------|----timeseries-component # 时序数据组件
------|---jetlinks-manager  # 管理功能
------|-------|----authentication-manager   # 用户,权限管理模块
------|-------|----datasource-manager   # 数据源管理模块
------|-------|----device-manager   # 设备管理模块
------|-------|----logging-manager   # 日志管理模块
------|-------|----network-card-manager   # 物联网卡管理模块，统一管理电信，移动物联网卡。
------|-------|----network-manager   # 网络组件管理模块
------|-------|----notify-manager   # 通知管理模块
------|-------|----rule-engine-manager   # 规则引擎管理模块
------|-------|----things-manager   # 物管理模块
------|---jetlinks-parent   # 父模块,统一依赖管理
------|---jetlinks-standalone  #单例模块 启动JetLinks平台
------|---simulator # 模拟器,现已废弃不用
```



## JetLinks-cloud

```bash
---jetlinks-cloud
------|---deploy # nginx配置文件
------|---http-test # 一些http请求测试
------|---jetlinks-components   # 组件库
------|-------|----assets-component # 资产模块 对数据权限控制的支持
------|-------|----cassandra-component # cassandra集成 对cassandra的支持
------|-------|----clickhouse-component # clickhouse集成 对clickhouse的支持
------|-------|----common-component # 通用组件.工具类等
------|-------|----configure-component # 统一配置模块
------|-------|----dashboard-component # 仪表盘模块
------|-------|----datasource-component # 数据源管理配置模块
------|-------|----elasticsearch-component # ElasticSearch集成
------|-------|----gateway-component # 网关模块,统一定义网关接口等信息
------|-------|----geo-component # 地理位置模块
------|-------|----influxdb-component # 对influxdb的支持
------|-------|----io-component # IO模块,文件管理等
------|-------|----logging-component # 日志模块
------|-------|----messaging-component # 消息模块,集成消息队列等
------|-------|-------|---kafka-component # Kafka模块
------|-------|-------|---rabbitmq-component # RabbitMQ模块
------|-------|----network-component # 网络组件模块,统一定义网络组件规范以及默认实现
------|-------|----notify-component # 通知模块,统一定义通知规范以及默认实现
------|-------|----plugin-component # 插件模块
------|-------|----protocol-component # 协议模块
------|-------|----rule-engine-component # 规则引擎模块,集成规则引擎通用功能
------|-------|----streaming-component # 流式计算模块
------|-------|----tdengine-component # 对tdengine的支持
------|-------|----tenant-component # 租户模块
------|-------|----test-component # 测试模块
------|-------|----things-component # 物管理模块
------|-------|----timeseries-component # 时序数据组件
------|---jetlinks-manager  # 管理功能
------|-------|----authentication-manager   # 用户,权限管理模块
------|-------|----datasource-manager   # 数据源管理模块
------|-------|----device-manager   # 设备管理模块
------|-------|----logging-manager   # 日志管理模块
------|-------|----network-manager   # 网络组件管理模块
------|-------|----notify-manager   # 通知管理模块
------|-------|----rule-engine-manager   # 规则引擎管理模块
------|-------|----visualization-manager   # 可视化模块
------|---jetlinks-openapi   # 基于数据签名的OpenApi模块
------|-------|----jetlinks-openapi-core   # OpenApi核心模块
------|-------|----jetlinks-openapi-manager   # OpenApi核心管理器模块
------|---jetlinks-parent   # 父模块,统一依赖管理
------|---micro-services # 微服务模块
------|-------|----api-gateway-service    # api网关服务
------|-------|----authentication-service    # 用户认证服务
------|-------|----dueros-service    # 小度智能家居开放平台微服务模块
------|-------|----file-service    # 文件服务
------|-------|----iot-service    # 物联网设备接入服务
------|-------|----service-components    # 服务通用模块配置
------|-------|----service-dependencies    # 服务统一依赖
------|-------|----visualization-service    # 可视化服务
------|---simulator     # 设备模拟器
```

