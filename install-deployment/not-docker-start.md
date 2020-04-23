# 非docker环境启动
请先[获取源代码](docker-start.md#获取源代码)。

## 硬件环境要求

处理器:4核及以上；  
内存：8G以上；  
磁盘：根据需求调整。 

## 启动所需环境后启动jetlinks服务

**需安装的服务:**  
postgresql 11,redis 5.x,elasticsearch 6.8.x.  

::: tip 提示
 `pwsostgresql`可更换为`mysql 5.7+`或者`sqlserver`,只需要修改配置中的`spring.r2dbc`和`easyorm`相关配置项即可.
:::

- 步骤1: 根据情况修改`jetlinks-standalone`模块下的配置文件:`application.yml`中相关配置。

```yaml
spring:
  redis:
    host: 127.0.0.1 # redis配置
    port: 6379
  r2dbc:
    url: r2dbc:postgresql://127.0.0.1:5432/jetlinks  # 数据库postgresql数据库配置
    username: postgres
    password: jetlinks
easyorm:
  default-schema: public # 数据库默认的schema
  dialect: postgres #数据库方言
elasticsearch:
  client:
    host: 127.0.0.1   # elasticsearch
    port: 9200
hsweb:
  file:
    upload:
      static-file-path: ./static/upload   # 上传的文件存储路径
      static-location: http://127.0.0.1:8848/upload # 上传的文件访问根地址
```

- 步骤2: [启动JetLinks服务](ide-docker-start.md#启动JetLinks服务)。  

- 步骤3: [启动UI](ui-start.md)。  

## 使用内嵌所需环境的方式启动jetlinks服务

- 步骤1： 修改`jetlinks-standalone`模块下的配置文件:`application.yml`中spring配置。  
```yaml
spring:
  profiles:
    active: embedded
```

- 步骤2： 根据情况修改`jetlinks-standalone`模块下的配置文件:`application-embedded.yml`中相关配置。
```yaml
spring:
  resources:
    static-locations: file:./index/, file:./static/,/,classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/, classpath:/public/
  redis:
    embedded:
      enabled: true  # 使用内置的redis,不建议在生产环境中使用.
      host: 127.0.0.1
      port: 6379
      data-path: ./data/redis
    host: 127.0.0.1
    port: 6379
    lettuce:
      pool:
        max-active: 1024
    timeout: 20s
  r2dbc:
    url: r2dbc:h2:file:///./data/h2db/jetlinks
    username: sa
    password:
    pool:
      max-size: 32
easyorm:
  default-schema: PUBLIC # 数据库默认的schema
  dialect: h2 #数据库方言
elasticsearch:
  embedded:
    enabled: true # 为true时使用内嵌的elasticsearch,不建议在生产环境中使用
    data-path: ./data/elasticsearch
    port: 9200
    host: 0.0.0.0
  client:
    host: localhost
    port: 9200
    max-conn-total: 128
    connect-timeout: 5000
    socket-timeout: 5000
    connection-request-timeout: 8000
  index:
    default-strategy: time-by-month #默认es的索引按月进行分表, direct则为直接操作索引.
    settings:
      number-of-shards: 1 # es 分片数量
      number-of-replicas: 0 # 副本数量
device:
  message:
    writer:
      time-series:
        enabled: true #写出设备消息数据到elasticsearch
logging:
  level:
    com.github.tonivade: error
```

- 步骤3： [启动JetLinks服务](ide-docker-start.md#启动JetLinks服务)。  
       
- 步骤4: [启动UI](ui-start.md)。  

## 启动成功后访问系统  

地址: `http://localhost:9000`, 用户名:`admin`,密码:`admin`。