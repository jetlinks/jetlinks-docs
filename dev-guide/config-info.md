# 配置文件说明

## JetLinks Pro(单机版)

### 配置文件示例
```yaml
server:
  port: 8844
  max-http-header-size: 64KB
  error:
    include-message: always
spring:
  profiles:
    active: dev
  application:
    name: jetlinks-platform
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Shanghai
    serialization:
      WRITE_DATES_AS_TIMESTAMPS: true
    default-property-inclusion: non_null
  web:
    resources:
      static-locations: file:./static,classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/, classpath:/public/
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
    #    cluster:
    #      nodes:
    #        - ubuntu:6380
    #        - ubuntu:6381
    #        - ubuntu:6382
    timeout: 60s
    serializer: obj # 设置obj时,redis key使用string序列化,value使用SerializeUtils进行序列化.
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/jetlinks
    username: postgres
    password: jetlinks
    pool:
      max-size: 32
      max-idle-time: 60s
      max-life-time: 1m
      acquire-retry: 3
  #    properties:
  #      preparedStatementCacheQueries: 100
  codec:
    max-in-memory-size: 100MB
  rabbitmq: # 开启了device.message.writer.rabbitmq.enabled=true时生效
    host: localhost
    port: 5672
    username: admin
    password: jetlinks
  kafka: # 开启了device.message.writer.kafka.enabled=true时生效
    consumer:
      client-id: ${spring.application.name}-consumer:${server.port}
      group-id: ${spring.application.name}
      max-poll-records: 1000
    producer:
      client-id: ${spring.application.name}-producer:${server.port}
      acks: 1
      retries: 3
    bootstrap-servers: [ "127.0.0.1:9092" ]
  reactor:
    debug-agent:
      enabled: true # 开启调试代理,在打印异常时将会生成调用踪栈信息
  elasticsearch:
    uris: localhost:9200
    socket-timeout: 10s
    connection-timeout: 15s
    webclient:
      max-in-memory-size: 100MB
#    username:
#    password:
easyorm:
  default-schema: public # 数据库默认的schema
  dialect: postgres #数据库方言
elasticsearch:
  index:
    default-strategy: time-by-month #默认es的索引按月进行分表
    settings:
      number-of-shards: 1 # es 分片数量，通常对应es集群节点数量
      number-of-replicas: 0 # 副本数量
#      options:
#        lifecycle.name: lifecycle_name # 在kibana中配置的生命周期
influxdb:
  enabled: true #开启inflxdb
  endpoint: "http://localhost:8086/"
  database: jetlinks
  max-in-memory-size: 100MB
  socket-timeout: 5S
  connection-timeout: 10S
tdengine:
  enabled: true #开启tdengine
  database: jetlinks
  connector: restful # 支持restful和jdbc
  jdbc:
    url: jdbc:TAOS://127.0.0.1:6030/jetlinks
    username: root
    password: taosdata
    maximum-pool-size: 32
  restful:
    endpoint: "http://localhost:6041/"
    username: root
    password: taosdata
    max-in-memory-size: 100MB
    socket-timeout: 5S
    connection-timeout: 10S
clickhouse:
  enabled: false
  restful:
    url: http://127.0.0.1:8123
    username: default
    password: jetlinks
  max-batch-size: 10000 # 最大批量提交数量
  batch-duration: 2s # 批量提交间隔
device:
  message:
    writer:
      time-series:
        enabled: true # 直接写出设备消息数据到时序数据库
      kafka:
        enabled: false  # 推送设备消息到kafka
        consumer: true  # 从kafka订阅消息并写入到时序数据库
        topic-name: device.message
      #        type:
      #          excludes:
      #          includes:
      rabbitmq:
        enabled: false  # 推送设备消息到rabbitMQ
        consumer: true  # 从rabbitMQ订阅消息并写入到时序数据库
        thread-size: 4 # 消费线程数
        auto-ack: true  # 自定应答,为true可能导致数据丢失,但是性能最高。
        topic-name: device.message  # exchange名称
      #        type:
      #          excludes:
      #          includes:
      geo:
        enable-property: true
  session:
    persistence:
      session-load-timeout: 60s
captcha:
  enabled: true # 开启验证码
  ttl: 2m #验证码过期时间,2分钟
hsweb:
  cors:
    enable: true
    configs:
      - path: /**
        allowed-headers: "*"
        allowed-methods: [ "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS" ]
        allowed-origins: [ "*" ]
        #        allow-credentials: false
        max-age: 1800
  dict:
    enum-packages: org.jetlinks
  file:
    upload:
      static-file-path: ./static/upload
      static-location: http://127.0.0.1:8844/upload
      deny-files: php,asp,jsp,exe,dll,so,cmd,bat,sh,shell,js,html
  webflux:
    response-wrapper:
      enabled: true #开启响应包装器(将返回值包装为ResponseMessage)
      excludes: # 这下包下的接口不包装
        - org.springdoc
  authorize:
    auto-parse: true
  #  user-token:
  #    allopatric-login-mode: offlineOther  ## 设置异地登录模式为 将其他地方登录的相同用户踢下线
  #    allopatric-login-modes:
  #      app: offlineOther
  permission:
    filter:
      enabled: true # 设置为true开启权限过滤,赋权时,不能赋予比自己多的权限.
      exclude-username: admin # admin用户不受上述限制
      un-auth-strategy: ignore # error表示:发生越权时,抛出403错误. ignore表示会忽略越权的赋权.
  cache:
    type: redis
    redis:
      local-cache-type: caffeine
    caffeine:
      maximum-size: 10240
jetlinks:
  server-id: ${spring.application.name}:${server.port} #设备服务网关服务ID,不同服务请设置不同的ID
  cluster:
    id: ${jetlinks.server-id}
    name: ${spring.application.name}
    port: 1${server.port} # 集群通信通信本地端口
    external-host: 127.0.0.1  #集群节点通信对外暴露的host
    external-port: ${jetlinks.cluster.port} #集群节点通信对外暴露的端口
    rpc-port: 2${server.port} # 集群节点本地RPC端口
    rpc-external-host: ${jetlinks.cluster.external-host}  #集群节点RPC对外暴露host
    rpc-external-port: 2${server.port} #集群节点RPC对外暴露端口
  #    seeds:  #集群种子节点,集群时,配置为集群节点的 external-host:external-port
  #      - 127.0.0.1:18844
  logging:
    system:
      context:
        server: ${jetlinks.server-id}
  protocol:
    spi:
      enabled: false # 为true时开启自动加载通过依赖引入的协议包
  device:
    registry:
      auto-discover: enabled  #当无法从注册中心获取设备时,尝试从数据库中获取,并自动注册到注册中心.
    storage:
      default-policy: default-row # 默认设备数据存储策略
      enable-last-data-in-db: false # 是否将设备最新到数据存储到数据库
#      log:
#        excludes: # 这些日志不存储
#          - REPORT_PROPERTY
rule:
  engine:
    server-id: ${jetlinks.server-id}
    server-name: ${spring.application.name}
logging:
  level:
    org.jetlinks: debug
    rule.engine: debug
    org.hswebframework: debug
    org.springframework.transaction: debug
    org.springframework.data.r2dbc.connectionfactory: warn
    io.micrometer: warn
    org.hswebframework.expands: error
    system: debug
    org.jetlinks.rule.engine: debug
    org.jetlinks.supports.event: warn
    org.springframework: warn
    org.apache.kafka: warn
    org.jetlinks.pro.device.message.writer: warn
    org.jetlinks.pro.elastic.search.service: warn
    org.jetlinks.pro.elastic.search.service.reactive: warn
    org.jetlinks.pro.network: warn
    org.jetlinks.demo: warn
    io.vertx.mqtt.impl: warn
    org.jetlinks.supports.device.session: warn
    org.elasticsearch.deprecation: off
    #    org.jetlinks.supports.cluster: trace
    #    org.springframework.data.elasticsearch.client: trace
    #    org.elasticsearch: error
    org.jetlinks.pro.influx: trace
    org.elasticsearch.deprecation.search.aggregations.bucket.histogram: off
  config: classpath:logback-spring.xml
  logback:
    rollingpolicy:
      max-file-size: 100MB
vertx:
  max-event-loop-execute-time-unit: seconds
  max-event-loop-execute-time: 30
  max-worker-execute-time-unit: seconds
  max-worker-execute-time: 30
  prefer-native-transport: true
micrometer:
  time-series:
    tags:
      server: ${jetlinks.server-id}
    metrics:
      default:
        step: 30s
    ignore:
      - jetlinks-metrics #忽略内部监控信息
springdoc:
  swagger-ui:
    path: /swagger-ui.html
  #  packages-to-scan: org.jetlinks
  group-configs:
    - group: 设备管理相关接口
      packages-to-scan:
        - org.jetlinks.pro.device
        - org.jetlinks.pro.geo.web
        - org.jetlinks.pro.media.web
      paths-to-exclude:
        - /device-instance/**
        - /device-product/**
        - /protocol/**
        - /api/v1/geo/object/**
    - group: 规则引擎相关接口
      packages-to-scan: org.jetlinks.pro.rule.engine.web
      paths-to-exclude: /api/**
    - group: 通知管理相关接口
      packages-to-scan: org.jetlinks.pro.notify.manager.web
    - group: 设备接入相关接口
      packages-to-scan:
        - org.jetlinks.pro.network.manager.web
        - org.jetlinks.pro.device.web
      paths-to-match:
        - /gateway/**
        - /network/**
        - /protocol/**
    - group: 系统管理相关接口
      packages-to-scan:
        - org.jetlinks.pro.auth
        - org.hswebframework.web.system.authorization.defaults.webflux
        - org.hswebframework.web.file
        - org.hswebframework.web.authorization.basic.web
        - org.jetlinks.pro.openapi.manager.web
        - org.jetlinks.pro.logging.controller
        - org.jetlinks.pro.tenant.web
  cache:
    disabled: false
messaging:
  mqtt:
    enabled: true
    port: 11883
    host: 0.0.0.0
sso:
  token-set-page-url: http://localhost:9000/api/token-set.html
  bind-page-url: http://localhost:9000/#/account/center/bind
  base-url: http://localhost:9000/jetlinks
api:
  urls:
    big-screen-path: http://localhost:9002/
  base-path: http://127.0.0.1:${server.port}/
trace:
  enabled: true
system:
  config:
    scopes:
      - id: front
        name: 前端配置
        public-access: true
      - id: paths
        name: 访问路径配置
        public-access: true
        properties:
          - key: base-path
            name: 接口根路径
            default-value: ${sso.base-path}
          - key: sso-redirect
            name: sso回调路径
            default-value: ${sso.base-url}
          - key: sso-bind
            name: sso用户绑定路径
            default-value: ${sso.bind-page-url}
          - key: sso-token-set
            name: ssoToken设置路径
            default-value: ${sso.token-set-page-url}

      - id: amap
        name: 高德地图配置
        public-access: false
        properties:
          - key: apiKey # 配置id
            name: 高德地图ApiKey # 名称
#            default-value: "请输入高德地图的apikey" # 默认值
management:
  metrics:
    export:
      simple:
        enabled: false
file:
  manager:
    storage-base-path: "./data/files"
    read-buffer-size: 64KB
network:
  resources:
    - 1883-1890
    - 8800-8810
    - 5060-5061
```

### 配置文件常见修改说明
| 标识名                                   | 更改示例                                                                                 | 说明                               |
|---------------------------------------|--------------------------------------------------------------------------------------|----------------------------------|
| spring.redis.host                     | 192.168.66.171                                                                       | redis所在服务器的ipv4地址                |
| spring.redis.password                 | Jetlinks@redis                                                                       | redis密码，若redis服务未设置密码可不填         |
| spring.r2dbc.url                      | r2dbc:postgresql://192.168.66.171:5432/jetlinks                                      | 改为postgresql所在服务器的ipv4地址         |
| spring.r2dbc.url                      | r2dbc:mysql://192.168.66.171:5432:3306/jetlinks?ssl=false&serverZoneId=Asia/Shanghai | 更换mysql数据库                       |
| easyprm.default-schema                | jetlinks                                                                             | mysql数据库名                        |
| easyprm.dialect                       | mysql                                                                                | mysql数据库方言                       |
| elasticsearch.uris                    | 192.168.66.171:9200                                                                  | elasticsearch所在服务器的ipv4地址及端口号    |
| influxdb.enabled                      | ture                                                                                 | 开启inflxdb                        |
| influxdb.endpoint                     | "http://192.168.66.171:8086/"                                                        | inflxdb所在服务器的ipv4地址及端口号          |
| tdengine.enabled                      | ture                                                                                 | 开启tdengine                       |
| tdengine.jdbc.url                     | jdbc:TAOS://192.168.66.171:6030/jetlinks                                             | 使用jdbc方式连接tdengine               |
| tdengine.restful.endpoint             | "http://192.168.66.171:6041/"                                                        | 使用restful方式连接tdengine            |
| clickhouse.enabled                    | ture                                                                                 | 开启clickhouse                     |
| clickhouse.restful.url                | http://192.168.66.171:8123                                                           | clickhouse所在服务器的ipv4地址           |
| hswb.file.upload.static-location      | http://192.168.66.171:8844/upload                                                    | jetlinks所在服务器的ipv4地址及jetlinks端口号 |
| jetlinks.cluster.seeds                | - 192.168.66.171:18844  </br>     - 192.168.66.171:18844                             | 集群需在此处配置种子节点                     |
| logging.level.org.jetlinks            | warn                                                                                 | 可在此处修改日志级别                       |
| springdoc.swagger-ui.packages-to-scan | - org.jetlinks.pro.device                                                            | 扫描更多接口可在此处配置                     |
| network.resources                     | - 7000-7100                                                                          | 预留更多端口可在此处配置                     |





```java
//此处将具体代码实现放入
//1.对关键部分代码进行步骤梳理及注释说明
//2.对核心部分代码用醒目的文字进行说明，说明内容包括但不限于设计思想、设计模式等
```

#### 核心类说明

| 类名 | 方法名 | 返回值 | 说明 |
|----------------| -------------------------- |--------|---------------------------|-------------------|
| DeviceOperator | getSelfConfig() |`Mono<Value>` | 从缓存中获取设备自身的配置，如果不存在则返回`Mono.empty()`|

#### 常见问题

*对开发过程中出现的问题进行总结*

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>


  <li>产品在正常状态时，按钮显示为禁用；产品在启用状态时，按钮显示为启用。</li>
  <li>产品禁用后，设备无法再接入。但不影响已经接入的设备。</li>

</div>


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>


  <li>产品在正常状态时，按钮显示为禁用；产品在启用状态时，按钮显示为启用。</li>
  <li>产品禁用后，设备无法再接入。但不影响已经接入的设备。</li>

</div>

<div class='explanation error'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>危险</span>
  </p>


若设备限制数量不能满足您的业务需求，请
<a>提交工单</a>
说明您的需求。

</div>

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
若设备限制数量不能满足您的业务需求，请
<a>提交工单</a>
说明您的需求。
</div>