# 配置文件说明

## 文档指引
<table>
<tr>
   <td><a href="/dev-guide/config-info.html#jetlinks-communtiy配置文件示例">JetLinks-communtiy配置文件示例</a></td>
   <td><a href="/dev-guide/config-info.html#jetlinks-pro配置文件示例">JetLinks-Pro配置文件示例</a></td>
    <td><a href="/dev-guide/config-info.html#jetlinks-cloud配置文件示例">JetLinks-Cloud配置文件示例</a></td>

</tr>
</table>

## jetlinks-communtiy配置文件示例

配置文件地址：`/jetlinks-standalone/src/main/resources/application.yml`
```yaml
server:
  port: 8848

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
      static-locations: file:./static/,/,classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/, classpath:/public/
  redis:
    host: 127.0.0.1
    port: 6379
    lettuce:
      pool:
        max-active: 1024
    timeout: 20s
  #    database: 3
  #        max-wait: 10s
  r2dbc:
    # 需要手动创建数据库,启动会自动创建表,修改了配置easyorm相关配置也要修改
    url: r2dbc:postgresql://localhost:5432/jetlinks
    #    url: r2dbc:mysql://localhost:3306/jetlinks?ssl=false&serverZoneId=Asia/Shanghai # 修改了配置easyorm相关配置也要修改
    username: postgres
    password: jetlinks
    pool:
      max-size: 32
      max-idle-time: 2m # 值不能大于mysql server的wait_timeout配置
      max-life-time: 10m
      acquire-retry: 3
  reactor:
    debug-agent:
      enabled: false
  elasticsearch:
    uris: localhost:9200
    socket-timeout: 10s
    connection-timeout: 15s
    webclient:
      max-in-memory-size: 100MB
easyorm:
  default-schema: public # 数据库默认的schema
  dialect: postgres #数据库方言
tdengine:
  enabled: false
  database: jetlinks
  restful:
    endpoints:
      - http://localhost:6041/
    username: root
    password: taosdata
elasticsearch:
  embedded:
    enabled: false # 为true时使用内嵌的elasticsearch,不建议在生产环境中使用
    data-path: ./data/elasticsearch
    port: 9200
    host: 0.0.0.0
  index:
    default-strategy: time-by-month #默认es的索引按月进行分表, direct则为直接操作索引.
    settings:
      number-of-shards: 1 # es 分片数量
      number-of-replicas: 0 # 副本数量
device:
  message:
    writer:
      time-series:
        enabled: true #对设备数据进行持久化
captcha:
  enabled: false # 开启验证码
  ttl: 2m #验证码过期时间,2分钟
hsweb:
  cors:
    enable: true
    configs:
      - path: /**
        allowed-headers: "*"
        allowed-methods: [ "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS" ]
        allowed-origins: [ "*" ] ## 生产环境请替换为具体的域名端口如: http://xxxxx
        max-age: 1800
  dict:
    enum-packages: org.jetlinks
  file:
    upload:
      static-file-path: ./static/upload
      static-location: http://localhost:8848/upload
  webflux:
    response-wrapper:
      enabled: true #开启响应包装器(将返回值包装为ResponseMessage)
      excludes: # 这下包下的接口不包装
        - org.springdoc
  #  auth:   #默认的用户配置
  #    users:
  #      admin:
  #        username: admin
  #        password: admin
  #        name: 超级管理员
  authorize:
    auto-parse: true
  permission:
    filter:
      enabled: true # 设置为true开启权限过滤,赋权时,不能赋予比自己多的权限.
      exclude-username: admin # admin用户不受上述限制
      un-auth-strategy: ignore # error表示:发生越权时,抛出403错误. ignore表示会忽略越权的赋权.
  cache:
    type: redis
    redis:
      local-cache-type: guava
file:
  manager:
    storage-base-path: ./data/files
api:
  # 访问api接口的根地址
  base-path: http://127.0.0.1:${server.port}

jetlinks:
  server-id: ${spring.application.name}:${server.port} #设备服务网关服务ID,不同服务请设置不同的ID
  logging:
    system:
      context:
        server: ${spring.application.name}
  protocol:
    spi:
      enabled: true # 为true时开启自动加载通过依赖引入的协议包
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
    org.jetlinks.rule.engine: warn
    org.jetlinks.supports.event: warn
    org.springframework: warn
    org.jetlinks.community.device.message.writer: warn
    org.jetlinks.community.timeseries.micrometer: warn
    org.jetlinks.community.elastic.search.service.reactive: trace
    org.jetlinks.community.network: warn
    io.vertx.mqtt.impl: warn
    org.jetlinks.supports.scalecube.rpc: warn
    "org.jetlinks.community.buffer": debug
    org.elasticsearch: error
    org.elasticsearch.deprecation: off
    "io.vertx.core.impl.ContextImpl": off
  config: classpath:logback-spring.xml
vertx:
  max-event-loop-execute-time-unit: seconds
  max-event-loop-execute-time: 30
  max-worker-execute-time-unit: seconds
  max-worker-execute-time: 30
  prefer-native-transport: true
micrometer:
  time-series:
    tags:
      server: ${spring.application.name}
    metrics:
      default:
        step: 30s
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
            default-value: http://localhost:9000/api
      - id: amap
        name: 高德地图配置
        public-access: false
        properties:
          - key: apiKey # 配置id
            name: 高德地图ApiKey # 名称
management:
  health:
    elasticsearch:
      enabled: false  # 关闭elasticsearch健康检查
springdoc:
  swagger-ui:
    path: /swagger-ui.html
  #  packages-to-scan: org.jetlinks
  group-configs:
    - group: 设备管理相关接口
      packages-to-scan:
        - org.jetlinks.community.device
      paths-to-exclude:
        - /device-instance/**
        - /device-product/**
        - /protocol/**
    - group: 规则引擎相关接口
      packages-to-scan: org.jetlinks.community.rule.engine.web
      paths-to-exclude: /api/**
    - group: 通知管理相关接口
      packages-to-scan: org.jetlinks.community.notify.manager.web
    - group: 设备接入相关接口
      packages-to-scan:
        - org.jetlinks.community.network.manager.web
        - org.jetlinks.community.device.web
      paths-to-match:
        - /gateway/**
        - /network/**
        - /protocol/**
    - group: 系统管理相关接口
      packages-to-scan:
        - org.jetlinks.community.auth
        - org.hswebframework.web.system.authorization.defaults.webflux
        - org.hswebframework.web.file
        - org.hswebframework.web.authorization.basic.web
        - org.jetlinks.community.logging.controller
  cache:
    disabled: false
network:
  resources:
    - 1883-1890
    - 8800-8810
    - 5060-5061
```
### 配置文件常见修改说明
| 标识名                                   | 更改示例                                                                                 | 说明                          |
|---------------------------------------|--------------------------------------------------------------------------------------|-----------------------------|
| spring.redis.host                     | 192.168.66.171                                                                       | redis所在服务器的ipv4地址           |
| spring.redis.password                 | Jetlinks@redis                                                                       | redis密码，若redis服务未设置密码可不填    |
| spring.r2dbc.url                      | r2dbc:postgresql://192.168.66.171:5432/jetlinks                                      | 改为postgresql所在服务器的ipv4地址    |
| spring.r2dbc.url                      | r2dbc:mysql://192.168.66.171:5432:3306/jetlinks?ssl=false&serverZoneId=Asia/Shanghai | 更换mysql数据库                  |
| easyprm.default-schema                | public                                                                               | 数据库默认的schema                   |
| easyprm.dialect                       | postgres                                                                             | 数据库方言                       |
| elasticsearch.uris                    | 192.168.66.171:9200                                                                  | elasticsearch所在服务器的ipv4地址及端口号 |
| tdengine.enabled                      | ture                                                                                 | 开启tdengine                  |
| tdengine.jdbc.url                     | jdbc:TAOS://192.168.66.171:6030/jetlinks                                             | 使用jdbc方式连接tdengine          |
| tdengine.restful.endpoint             | "http://192.168.66.171:6041/"                                                        | 使用restful方式连接tdengine       |
| hswb.file.upload.static-location      | http://192.168.66.171:8844/upload                                                    | jetlinks所在服务器的ipv4地址及jetlinks端口号 |
| logging.level.org.jetlinks            | warn                                                                                 | 可在此处修改日志级别                  |
| springdoc.swagger-ui.packages-to-scan | - org.jetlinks.pro.device                                                            | 扫描更多接口可在此处配置                |
| network.resources                     | - 7000-7100                                                                          | 预留更多端口可在此处配置                |



## JetLinks-Pro配置文件示例

配置文件地址:`jetlinks-standalone/src/main/resources/application.yml`
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
    host: 192.168.66.171
    port: 6379
    database: 0
    timeout: 60s
    serializer: obj # 设置obj时,redis key使用string序列化,value使用SerializeUtils进行序列化.
  r2dbc:
    url: r2dbc:postgresql://192.168.66.171:5432/jetlinks
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
    uris: 192.168.66.171:9200
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
  endpoint: "http://192.168.66.171:8086/"
  database: jetlinks
  max-in-memory-size: 100MB
  socket-timeout: 5S
  connection-timeout: 10S
tdengine:
  enabled: true #开启tdengine
  database: jetlinks
  connector: restful # 支持restful和jdbc
  jdbc:
    url: jdbc:TAOS://192.168.66.171:6030/jetlinks
    username: root
    password: taosdata
    maximum-pool-size: 32
  restful:
    endpoint: "http://192.168.66.171:6041/"
    username: root
    password: taosdata
    max-in-memory-size: 100MB
    socket-timeout: 5S
    connection-timeout: 10S
clickhouse:
  enabled: false
  restful:
    url: http://192.168.66.171:8123
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
      static-location: http://192.168.66.171:8844/upload # 文件实际上传地址
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
    path: /swagger-ui.html #swagger页面地址
  #  packages-to-scan: org.jetlinks
  group-configs:
    - group: 设备管理相关接口
      packages-to-scan: # swagger扫描的类
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
| logging.level.org.jetlinks            | warn                                                                                 | 可在此处修改日志级别                       |
| springdoc.swagger-ui.packages-to-scan | - org.jetlinks.pro.device                                                            | 扫描更多接口可在此处配置                     |
| network.resources                     | - 7000-7100                                                                          | 预留更多端口可在此处配置                     |

## JetLinks-Pro集群

### 配置文件示例
```yaml
server:
  port: 8845
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
  data:
    elasticsearch:
      client:
        reactive:
          endpoints:
            - http://192.168.134.129:9200
            - http://192.168.134.130:9200
          max-in-memory-size: 100MB
          socket-timeout: 10s
          connection-timeout: 15s
#  elasticsearch:
#    uris: localhost:9200
#    socket-timeout: 10s
#    connection-timeout: 15s
#    webclient:
#      max-in-memory-size: 100MB
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
        consumer: false  # 从kafka订阅消息并写入到时序数据库
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
  enabled: false # 开启验证码
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
      static-location: http://192.168.66.171:8844/upload
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
    external-host: 192.168.66.171  #集群节点通信对外暴露的host
    external-port: ${jetlinks.cluster.port} #集群节点通信对外暴露的端口
    rpc-port: 2${server.port} # 集群节点本地RPC端口
    rpc-external-host: ${jetlinks.cluster.external-host}  #集群节点RPC对外暴露host
    rpc-external-port: 2${server.port} #集群节点RPC对外暴露端口
    seeds:  #集群种子节点,集群时,配置为集群节点的 external-host:external-port
      - 192.168.66.171:18845
      - 192.168.66.177:18844
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
    org.hswebframework: warn
    org.springframework.transaction: warn
    org.springframework.data.r2dbc.connectionfactory: warn
    io.micrometer: warn
    org.hswebframework.expands: error
    system: warn
    org.jetlinks.rule.engine: warn
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
    - 7000-7900
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
| jetlinks.server-id                    | master${spring.application.name}: ${server.port}                                     | 要求每个节点的server-id不一致              |
| jetlinks.cluster.seeds                | - 192.168.66.171:18844 </br>  - 192.168.66.177:18844  </br>  - 192.168.66.178:18844  | 配置每台服务器的ipv4地址和集群端口              |
| logging.level.org.jetlinks            | warn                                                                                 | 修改日志级别可在此处配置                     |
| springdoc.swagger-ui.packages-to-scan | - org.jetlinks.pro.device                                                            | 扫描更多接口可在此处配置                     |
| network.resources                     | - 7000-7100                                                                          | 预留更多端口可在此处配置                     |


## JetLinks-Cloud配置文件示例

### api-gateway-service服务
配置文件所在路径下:

`jetlinks-cloud\micro-services\api-gateway-service\src\main\resources\application.yml`
```yaml
server:
  port: 8800
  max-http-header-size: 100KB
  error:
    include-message: always
  url: http://192.168.66.171:${server.port}
spring:
  application:
    name: api-gateway-service
  profiles:
    active: dev,local
  cloud:
    gateway:
      routes:
        - id: iot-service
          uri: lb://iot-service
          predicates:
            - Path=/tenant/*/assets/device/*,/tenant/assets/device/*,
              /tenant/*/assets/product/*,/tenant/assets/product/*,
              /assets/*/device/**,/assets/*/product/**,
              /dictionary/**,/ctwing/**,/one-net/**,/cluster/**,
              /api/v1/product/**,/api/v1/device/**,
              /logger/**,/dashboard/**,/notify/**,/notifier/**,/notifications/**,/messaging/**,/rule-engine/**,
              /gateway/**,/rule-editor/**,/device/**,/network/**,/geo/**,/device-instance/**,/device-product/**,
              /product/**,/api/v1/device/**,/firmware/**,/protocol/**,
              /scene/**,/alarm/**,/dueros/**,/datasource/**,
              /modbus/**,/opc/**,/things/**,/edge/**
          filters:
            - Authentication
        - id: media-service
          uri: lb://iot-service
          predicates:
            - Path=/media/**
          filters:
            - Authentication
        - id: authentication-service
          uri: lb://authentication-service
          predicates:
            - Path=/application/**,/menu/**,/system/config/**,/relation/**,/role/**,/oauth2/**,/token/**,/api-client/**,
              /system/resources/**,
              /dimension-type/**,/dimension-user/**,
              /user-token/*,/user/**,/tenant/**,/authorize/**,/autz-setting/**,
              /permission/**,/organization/**,/system/config/**,/dimension/**,/sso/**,
              /asset/types,/assets/access-support/*
        - id: iot-service-no-auth
          uri: lb://iot-service
          predicates:
            - Path=/v3/api-docs/设备管理相关接口,/v3/api-docs/规则引擎相关接口,/ctwing/*/notify,/one-net/*/notify
        - id: file-service
          uri: lb://file-service
          predicates:
            - Path=/file/**
          filters:
            - Authentication
        - id: file-service-no-auth
          uri: lb://file-service
          predicates:
            - Path=/upload/**
        - id: visualization-service
          uri: lb://visualization-service
          predicates:
            - Path=/visualization/**
          filters:
            - Authentication
        - id: dueros-service-notify # 小度平台通知
          uri: lb://iot-service
          predicates:
            - Path=/dueros
        - id: auth-service-doc
          uri: lb://authentication-service
          predicates:
            - Path=/v3/api-docs/系统管理相关接口
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
          filters:
            - RewritePath='/' + serviceId + '/(?<remaining>.*)','/${remaining}'
            - Authentication
    discovery:
      enabled: true
    loadbalancer:
      ribbon:
        enabled: false
      configurations: context-zone
      zone: ${local.ip}
  reactor:
    debug-agent:
      enabled: false
  codec:
    max-in-memory-size: 50MB
logging:
  level:
    org.jetlinks: debug
    org.jetlinks.supports: warn
springdoc:
  swagger-ui:
    urls:
      - url: ${server.url}/v3/api-docs/设备管理相关接口
        name: 设备管理相关接口
      - url: ${server.url}/v3/api-docs/系统管理相关接口
        name: 系统管理相关接口
      - url: ${server.url}/v3/api-docs/规则引擎相关接口
        name: 规则引擎相关接口
jetlinks:
  server-id: ${spring.application.name}:${server.port}
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
#      - 127.0.0.1:18100
trace:
  enabled: true
  service-name: ${spring.application.name}:${server.port}
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
```
## 配置文件常见修改说明
| 标识名                                    | 更改示例                                                                         | 说明                               |
|----------------------------------------|------------------------------------------------------------------------------|----------------------------------|
| server.url                             | http://192.168.66.171: ${server.port}                                        | 微服务部署服务器的ipv4地址                  |
| spring.cloud.gateway.routes.predicates | /edge/**                                                                     | 若需要扩展模块，需要在此处填写此模块的接口路径          |
| jetlinks.server-id                     | master${spring.application.name\}: ${server.port}                            | 若需要集群部署，则需要保证每个微服务的service-id不一致 |
| jetlinks.cluster.external-host         | 192.168.66.171                                                               | 微服务部署服务器的ipv4地址                  |
| jetlinks.cluster.seeds                 | 192.168.66.171:18800 </br>  192.168.66.177:18800 </br>  192.168.66.178:18800 | 每个微服务部署服务器的ipv4地址和集群节点端口         |

### authentication-service服务
配置文件在该路径下:`jetlinks-cloud\micro-services\authentication-service\src\main\resources\application.yml`
```yaml
server:
  port: 8100
  max-http-header-size: 200KB
  error:
    include-message: always
spring:
  profiles:
    active: dev,local
  application:
    name: authentication-service
  cloud:
    loadbalancer:
      ribbon:
        enabled: false
      zone: ${local.ip}
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Shanghai
    serialization:
      WRITE_DATES_AS_TIMESTAMPS: true
    default-property-inclusion: non_null
  redis:
    host: localhost
    port: 6379
    database: 8
    lettuce:
      pool:
        max-active: 1024
    timeout: 20s
#    password: iot@cloud
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/jetlinks-cloud-2.0
    username: postgres
    password: jetlinks
    pool:
      max-size: 32
  codec:
    max-in-memory-size: 50MB
  reactor:
    debug-agent:
      enabled: true # 开启调试代理,在打印异常时将会生成调用踪栈信息
  elasticsearch:
    uris: localhost:9200
    socket-timeout: 10s
    connection-timeout: 15s
    webclient:
      max-in-memory-size: 100MB
#  rsocket:
#    server:
#      port: 8101
easyorm:
  default-schema: public # 数据库默认的schema
  dialect: postgres #数据库方言
elasticsearch:
  index:
    default-strategy: time-by-month #默认es的索引按月进行分表
    settings:
      number-of-shards: 1 # es 分片数量
      number-of-replicas: 0 # 副本数量
hsweb:
  cors:
    enable: true
    configs:
      - path: /**
        allowed-headers: "*"
        allowed-methods: ["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"]
        allowed-origins: ["*"]
#        allow-credentials: true
        max-age: 1800
  dict:
    enum-packages: org.jetlinks
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
      local-cache-type: guava
jetlinks:
  server-id: 1${spring.application.name}:${server.port} #集群节点ID,不同集群节点请设置不同的ID
  cluster:
    id: ${jetlinks.server-id}
    name: ${spring.application.name}
    port: 1${server.port} # 集群通信通信本地端口
    external-host: 192.168.66.171  #集群节点通信对外暴露的host
    external-port: ${jetlinks.cluster.port} #集群节点通信对外暴露的端口
    rpc-port: 2${server.port} # 集群节点本地RPC端口
    rpc-external-host: ${jetlinks.cluster.external-host}  #集群节点RPC对外暴露host
    rpc-external-port: 2${server.port} #集群节点RPC对外暴露端口
    seeds:  #集群种子节点,集群时,配置为api网关节点的 external-host:external-port
      - 192.168.66.171:18802
      - 192.168.66.177:18800
#        - 192.168.66.171:18801
  logging:
    system:
      context:
        server: ${spring.application.name}
  token:
    jwt:
      #用于编码jwt的私钥,禁止泄漏,生产环境请更换此KEY
      encode-key: MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAiAsMq8aUMR52UFgQF1dRdndyreEEKpjOor1uxFXafiG1VBBkozu+4qSzGHega0YIQryNjNwp77FmuHsPDnDn8QIDAQABAkEAgQqaFkXiQ0U1zLf7a2hz6P8cVDpgDVesiUlOTAVznrdPvNvirNLnsi40FphXXFSNOL4UsvL+v1JaPm8nHzyoEQIhAMfmoV6Q6dmfVk1FXzOa1VGth3Ad8JrrorNoAESWbzSzAiEArji5GkwESoxWdcARXeMnSeQ+5MvgRJmKlYdcRv3QassCIQDFFR/BoTO9R/eJJTzwHtXzuhcOtaXyxyBYqs+gz4QCOwIgex1KcPLW9XdGgd9AZoBm+yh36WbJDLET5abJ5sDlzxECIEWwcYIN0xKvZVyI1KhAD2XNrU297bgPBcILnwdeLAui
      #用于解码jwt的公钥
      decode-key: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIgLDKvGlDEedlBYEBdXUXZ3cq3hBCqYzqK9bsRV2n4htVQQZKM7vuKksxh3oGtGCEK8jYzcKe+xZrh7Dw5w5/ECAwEAAQ==
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
    org.springframework.data.r2dbc.connectionfactory: debug
    io.micrometer: warn
    org.hswebframework.expands: error
    system: warn
    org.jetlinks.rule.engine: debug
    org.jetlinks.gateway: debug
    org.springframework: warn
    org.apache.kafka: warn
    org.jetlinks.pro.device.message.writer: debug
    org.jetlinks.supports: warn
  #    org.elasticsearch: error
#  config: classpath:logback-spring.xml
springdoc:
  #  packages-to-scan: org.jetlinks
  group-configs:
    - group: 系统管理相关接口
      packages-to-scan:
        - org.jetlinks.pro.auth
        - org.hswebframework.web.system.authorization.defaults.webflux
        - org.hswebframework.web.file
        - org.hswebframework.web.authorization.basic.web
        - org.jetlinks.pro.openapi.manager.web
  cache:
    disabled: false
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
            default-value: http://localhost:9000/api
          - key: sso-redirect
            name: sso认证成功默认回调路径
            default-value: http://localhost:9000
          - key: sso-bind
            name: sso用户绑定路径
            default-value: http://localhost:9000/#/account/center/bind
          - key: sso-token-set
            name: ssoToken设置路径
            default-value:  http://localhost:9000/api/token-set.html
      - id: amap
        name: 高德地图配置
        public-access: false
        properties:
          - key: apiKey # 配置id
            name: 高德地图ApiKey # 名称
trace:
  enabled: true
  jaeger:
    enabled: false
    endpoint: "http://127.0.0.1:14250"
captcha:
  enabled: false
  ttl: 5m
```

## 配置文件常见修改说明
| 标识名                                    | 更改示例                                                                                 | 说明                            |
|----------------------------------------|--------------------------------------------------------------------------------------|-------------------------------|
| spring.redis.host                      | 192.168.66.171                                                                       | reds部署服务器的ipv4地址              |
| spring.redis.password                  | JetLinks@redis                                                                       | redis密码，若redis服务未设置密码可不填      |
| spring.r2dbc.url                       | r2dbc:postgresql://192.168.66.171:5432/jetlinks                                      | postgresql部署服务器的ipv4地址        |
| spring.r2dbc.url                       | r2dbc:mysql://192.168.66.171:5432:3306/jetlinks?ssl=false&serverZoneId=Asia/Shanghai | 更换mysql数据库                    |
| easyprm.default-schema                 | jetlinks                                                                             | 改为mysql数据库名                   |
| easyprm.dialect                        | mysql                                                                                | mysql数据库方言，固定值                |
| elasticsearch.uris                     | 192.168.66.171:9200                                                                  | elasticsearch所在服务器的ipv4地址及端口号 |
| jetlinks.server-id                     | master${spring.application.name}: ${server.port}                                     | 与api服务参数保持一致                  |
| jetlinks.cluster.external-host         | 192.168.66.171                                                                       | 与api服务参数保持一致               |
| jetlinks.cluster.seeds                 | 192.168.66.171:18800 </br>  192.168.66.177:18800 </br>  192.168.66.178:18800         | 与api服务参数保持一致     |



## file-service服务
配置文件在该路径下:`jetlinks-cloud\micro-services\file-service\src\main\resources\application.yml`
```yaml
# file-service目录下的application.yml配置文件
server:
  port: 8300
  max-http-header-size: 200KB
  error:
    include-message: always
spring:
  profiles:
    active: dev,local
  application:
    name: file-service
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Shanghai
    serialization:
      WRITE_DATES_AS_TIMESTAMPS: true
    default-property-inclusion: non_null
  web:
    resources:
      static-locations: file:./static,classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/, classpath:/public/
  codec:
    max-in-memory-size: 50MB
  reactor:
    debug-agent:
      enabled: true # 开启调试代理,在打印异常时将会生成调用踪栈信息
  redis:
    host: 192.168.66.171
    port: 6379
    database: 8
    lettuce:
      pool:
        max-active: 1024
    timeout: 20s
#    password: iot@cloud
  r2dbc:
    url: r2dbc:postgresql://192.168.66.171:5432/jetlinks-cloud-2.0
    username: postgres
    password: jetlinks
    pool:
      max-size: 32
hsweb:
  file:
    upload:
      static-file-path: ./static/upload
      static-location: http://192.168.66.171:${server.port}/upload
      deny-files: php,asp,jsp,exe,dll,so,cmd,bat,sh,shell,js,html
  webflux:
    response-wrapper:
      enabled: true #开启响应包装器(将返回值包装为ResponseMessage)
      excludes: # 这下包下的接口不包装
        - org.springdoc
easyorm:
  default-schema: public
  dialect: postgres
jetlinks:
  server-id: master${spring.application.name}:${server.port} #集群节点ID,不同集群节点请设置不同的ID
  cluster:
    id: ${jetlinks.server-id}
    name: ${spring.application.name}
    port: 1${server.port} # 集群通信通信本地端口
    external-host: 192.168.66.171  #集群节点通信对外暴露的host
    external-port: ${jetlinks.cluster.port} #集群节点通信对外暴露的端口
    rpc-port: 2${server.port} # 集群节点本地RPC端口
    rpc-external-host: ${jetlinks.cluster.external-host}  #集群节点RPC对外暴露host
    rpc-external-port: 2${server.port} #集群节点RPC对外暴露端口
    seeds:  #集群种子节点,集群时,配置为api网关节点的 external-host:external-port
      - 192.168.66.171:18802
      - 192.168.66.177:18800
  token:
    jwt:
      type: rsa
      decode-key: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIgLDKvGlDEedlBYEBdXUXZ3cq3hBCqYzqK9bsRV2n4htVQQZKM7vuKksxh3oGtGCEK8jYzcKe+xZrh7Dw5w5/ECAwEAAQ==
logging:
  level:
    org.jetlinks: debug
    rule.engine: debug
    org.hswebframework: debug
    org.springframework.transaction: debug
    org.springframework.data.r2dbc.connectionfactory: debug
    io.micrometer: warn
    org.hswebframework.expands: error
    system: warn
    org.jetlinks.rule.engine: debug
    org.jetlinks.gateway: debug
    org.springframework: warn
    org.apache.kafka: warn
    org.jetlinks.pro.device.message.writer: debug
    org.jetlinks.supports: warn
  #    org.elasticsearch: error
#  config: classpath:logback-spring.xml
file:
  manager:
    storage-base-path: ./data/files
    read-buffer-size: 64KB

```

## 配置文件常见修改说明
| 标识名                               | 更改示例                                                                                 | 说明                       |
|-----------------------------------|--------------------------------------------------------------------------------------|--------------------------|
| spring.redis.host                 | 192.168.66.171                                                                       | reds部署服务器的ipv4地址         |
| spring.redis.password             | JetLinks@redis                                                                       | redis密码，若redis服务未设置密码可不填 |
| spring.r2dbc.url                  | r2dbc:postgresql://192.168.66.171:5432/jetlinks                                      | postgresql部署服务器的ipv4地址   |
| spring.r2dbc.url                  | r2dbc:mysql://192.168.66.171:5432:3306/jetlinks?ssl=false&serverZoneId=Asia/Shanghai | 更换mysql数据库               |
| easyprm.default-schema            | jetlinks                                                                             | 改为mysql数据库名              |
| easyprm.dialect                   | mysql                                                                                | mysql数据库方言，固定值           |
| hsweb.file.upload.static-location | http://192.168.66.171: ${server.port}/upload                                         | 微服务部署服务器的ipv4地址及端口号      |
| jetlinks.server-id                | master${spring.application.name\}: ${server.port}                                    | 与api服务参数保持一致             |
| jetlinks.cluster.external-host    | 192.168.66.171                                                                       | 与api服务参数保持一致             |
| jetlinks.cluster.seeds            | 192.168.66.171:18800 </br>  192.168.66.177:18800 </br>  192.168.66.178:18800         | 与api服务参数保持一致             |

## iot-service服务
配置文件在该路径下:`jetlinks-cloud\micro-services\iot-service\src\main\resources\application.yml`
```yaml
server:
  port: 8200
  max-http-header-size: 200KB
  error:
    include-message: always
spring:
  profiles:
    active: dev,local
  application:
    name: iot-service
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
    host: localhost
    port: 6379
    database: 8
    lettuce:
      pool:
        max-active: 1024
    timeout: 20s
#    password: iot@cloud
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/jetlinks-cloud-2.0
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
hsweb:
  dict:
    enum-packages: org.jetlinks
  webflux:
    response-wrapper:
      enabled: true #开启响应包装器(将返回值包装为ResponseMessage)
      excludes: # 这下包下的接口不包装
        - org.springdoc
  authorize:
    auto-parse: true
  cache:
    type: redis
    redis:
      local-cache-type: guava
jetlinks:
  server-id: 1${spring.application.name}:${server.port} #集群节点ID,不同集群节点请设置不同的ID
  cluster:
    id: ${jetlinks.server-id}
    name: ${spring.application.name}
    port: 1${server.port} # 集群通信通信本地端口
    external-host: 192.168.66.171  #集群节点通信对外暴露的host,根据实际部署情况修改该参数
    external-port: ${jetlinks.cluster.port} #集群节点通信对外暴露的端口
    rpc-port: 2${server.port} # 集群节点本地RPC端口
    rpc-external-host: ${jetlinks.cluster.external-host}  #集群节点RPC对外暴露host
    rpc-external-port: 2${server.port} #集群节点RPC对外暴露端口
    seeds:  #集群种子节点,集群时,配置为api网关节点的 external-host:external-port
      - 192.168.66.171:18802
      - 192.168.66.177:18800
  logging:
    system:
      context:
        server: ${spring.application.name}
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
  token:
    jwt:
      type: rsa
      # rsa公钥
      decode-key: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIgLDKvGlDEedlBYEBdXUXZ3cq3hBCqYzqK9bsRV2n4htVQQZKM7vuKksxh3oGtGCEK8jYzcKe+xZrh7Dw5w5/ECAwEAAQ==
rule:
  engine:
    server-id: ${jetlinks.server-id}
    server-name: ${spring.application.name}
logging:
  level:
    org.jetlinks: debug
    rule.engine: off
    org.hswebframework: off
    org.springframework.transaction: off
    org.springframework.data.r2dbc.connectionfactory: off
    io.micrometer: off
    org.hswebframework.expands: off
    system: debug
    org.jetlinks.rule.engine: off
    org.jetlinks.supports.event: off
    org.springframework: off
    org.apache.kafka: off
    org.jetlinks.pro.device.message.writer: off
    org.jetlinks.pro.elastic.search.service: off
    org.jetlinks.pro.elastic.search.service.reactive: off
    org.jetlinks.pro.network: debug
    org.jetlinks.supports: debug
    #    org.springframework.data.elasticsearch.client: trace
    #    org.elasticsearch: error
    org.jetlinks.pro.influx: off
    org.elasticsearch.deprecation: off
  #    org.elasticsearch: error
  config: classpath:logback-spring.xml
  logback:
    rollingpolicy:
      max-file-size: 100MB
springdoc:
  group-configs:
    - group: 设备管理相关接口
      packages-to-scan:
        - org.jetlinks.pro.device
        - org.jetlinks.pro.geo.web
      paths-to-exclude:
        - /device-instance/**
        - /device-product/**
        - /protocol/**
    - group: 规则引擎相关接口
      packages-to-scan: org.jetlinks.pro.rule.engine.web
      paths-to-exclude: /api/**
  cache:
    disabled: false
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

messaging:
  mqtt:
    enabled: true
    port: 11883
    host: 0.0.0.0
management:
  metrics:
    export:
      simple:
        enabled: false
network:
  resources:
    - 1883-1890
    - 8800-8810
    - 5060-5061
    - 7000-7100
trace:
  enabled: true
  jaeger:
    enabled: false
    endpoint: "http://127.0.0.1:14250"
  ignore-spans:
    #    - "/device/*/upstream"
    #    - "/device/*/decode"
    - "/java/TimeSeriesMessageWriterConnector/writeDeviceMessageToTs"
    - "/java/DeviceStatusMeasurementProvider/incrementOnline"
    - "/java/DeviceMessageMeasurementProvider/incrementMessage"
    - "/java/DeviceGeoDataWriter/handleDeviceGeoProperty"
    - "/java/DefaultDeviceDataManager/upgradeDeviceFirstPropertyTime"

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
| jetlinks.server-id                    | master${spring.application.name}: ${server.port}                                     | 与api服务参数保持一致             |
| jetlinks.cluster.external-host        | 192.168.66.171                                                                       | 与api服务参数保持一致             |
| jetlinks.cluster.seeds                | 192.168.66.171:18800 </br>  192.168.66.177:18800 </br>  192.168.66.178:18800         | 与api服务参数保持一致             |
| logging.level.org.jetlinks            | warn                                                                                 | 可在此处修改日志级别                       |
| springdoc.swagger-ui.packages-to-scan | - org.jetlinks.pro.device                                                            | 扫描更多接口可在此处配置                     |
| network.resources                     | - 7000-7100                                                                          | 预留更多端口可在此处配置                     |


