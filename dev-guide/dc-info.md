# docker-compose文件说明

## JetLinks-pro示例
```yaml
version: '2'
services:
  jetlinks:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT
    container_name: jetlinks-pro
    ports:
      - 8844:8844 # API端口
      - 1883:1883 # MQTT端口
      - 11883:11883 # 通过openAPI使用mqtt订阅平台消息
      - 8100-8110:8100-8110 # 预留端口
      - 8200-8210:8200-8210/udp # udp端口
    volumes:
      - "./data:/application/data" # 临时保存协议目录
      - "./data/upload:/application/static/upload"  # 持久化上传的文件
    environment:
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms4G -Xmx4G -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.uris=192.168.66.171:9200"
      #        - "spring.elasticsearch.username=admin"
      #        - "spring.elasticsearch.password=admin"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=warn"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
```
常见修改参数说明

| 标识名                         | 更改示例                                                                               | 说明                          |
|-----------------------------|------------------------------------------------------------------------------------|-----------------------------|
| jetlinks.image              | registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT | 镜像仓库地址请自行更换                 |
| jetlinks.environment        | - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"            | 换为后端部署服务器的ipv4地址            |
| jetlinks.environment        | - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks"               | 换为postgresql部署服务器的ipv4地址    |
| jetlinks.environment        | - "spring.elasticsearch.uris=192.168.66.171:9200"                                  | 换为elasticsearch部署服务器的ipv4地址 |
| jetlinks.environment        | - "spring.redis.host=192.168.66.171"                                               | 换为redis部署服务器的ipv4地址         |

## JetLinks-pro集群示例
```yaml
version: '2'
services:
  jetlinks:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT
    container_name: jetlinks-pro
    ports:
      - 8844:8844 # API端口
      - 1883:1883 # MQTT端口
      - 11883:11883 # 通过openAPI使用mqtt订阅平台消息
      - 8100-8110:8100-8110 # 预留端口
      - 8200-8210:8200-8210/udp # udp端口
    volumes:
      - "./data:/application/data" # 临时保存协议目录
      - "./data/upload:/application/static/upload"  # 持久化上传的文件
    environment:
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms4G -Xmx4G -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.uris=192.168.66.171:9200"
      #        - "spring.elasticsearch.username=admin"
      #        - "spring.elasticsearch.password=admin"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "jetlinks.cluster.id=jetlinks:node1"
      - "jetlinks.cluster.external-host=192.168.66.171"
      - "jetlinks.cluster.seeds[0]=192.168.66.171:8844"
      - "jetlinks.cluster.seeds[1]=192.168.66.177:8844"
      - "jetlinks.cluster.seeds[2]=192.168.66.178:8844"
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=warn"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
```

常见修改参数说明


| 标识名                         | 更改示例                                                                               | 说明                          |
|-----------------------------|------------------------------------------------------------------------------------|-----------------------------|
| jetlinks.image              | registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT | 镜像仓库地址请自行更换                 |
| jetlinks.environment        | - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"            | 换为后端部署服务器的ipv4地址            |
| jetlinks.environment        | - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks"               | 换为postgresql部署服务器的ipv4地址    |
| jetlinks.environment        | - "spring.elasticsearch.uris=192.168.66.171:9200"                                  | 换为elasticsearch部署服务器的ipv4地址 |
| jetlinks.environment        | - "spring.redis.host=192.168.66.171"                                               | 换为redis部署服务器的ipv4地址         |
| jetlinks.environment        | - "jetlinks.cluster.id=jetlinks:node1"                                             | 集群节点名                       |
| jetlinks.environment        | - "jetlinks.cluster.external-host=192.168.66.171"                                  | 对外暴露得ip                     |
| jetlinks.environment        | - "jetlinks.cluster.seeds[0]=192.168.66.171:8844"                                  | 集群子节点                       |
| jetlinks.environment        | - "jetlinks.cluster.seeds[0]=192.168.66.177:8844"                                  | 集群子节点                       |
| jetlinks.environment        | - "jetlinks.cluster.seeds[0]=192.168.66.178:8844"                                  | 集群子节点                       |


## JetLinks-cloud示例

```yaml
version: '2'
services:
  api-gateway-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/api-gateway-service:2.0.0-SNAPSHOT
    ports:
      - 8800:8800
    environment:
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.urls=192.168.66.171:9200"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=debug"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
  authentication-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/authentication-service:2.0.0-SNAPSHOT
    ports:
      - 8100:8100
    environment:
      - "TZ=Asia/Shanghai"
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.urls=192.168.66.171:9200"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=debug"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
  file-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/file-service:2.0.0-SNAPSHOT
    ports:
      - 8300:8300
    environment:
    - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
    - "spring.cloud.nacos.discovery.register-enabled=true"
    - "TZ=Asia/Shanghai"
    - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
    - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
    - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
    - "spring.r2dbc.username=postgres"
    - "spring.r2dbc.password=jetlinks"
    - "spring.elasticsearch.uris=192.168.66.171:9200"
    - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
    - "spring.redis.host=192.168.66.171"
    - "spring.redis.port=6379"
    - "spring.redis.password="
    - "logging.level.io.r2dbc=warn"
    - "logging.level.org.springframework.data=warn"
    - "logging.level.org.springframework=warn"
    - "logging.level.org.jetlinks=debug"
    - "logging.level.org.hswebframework=warn"
    - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
    volumes:
    - "./data/upload:/application/upload"
  iot-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/iot-service:2.0.0-SNAPSHOT
    ports:
      - 8200:8200
    environment:
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.uris=192.168.66.171:9200"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=debug"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
    volumes:
    - "./data/dumps:/dumps"
```
常见修改参数说明

| 标识名                             | 更改示例                                                                                | 说明                          |
|---------------------------------|-------------------------------------------------------------------------------------|-----------------------------|
| api-gateway-service.image       | registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/api-gateway-service:2.0.0-SNAPSHOT | 镜像仓库地址请自行更换                 |
| api-gateway-service.environment | - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"                    | 换为nacos部署服务器的ipv4地址         |
| api-gateway-service.environment | - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks"                | 换为postgre部署服务器的ipv4地址       |
| api-gateway-service.environment | - "spring.elasticsearch.uris=192.168.66.171:9200"                                   | 换为elasticsearch部署服务器的ipv4地址 |
| api-gateway-service.environment | - "spring.redis.host=192.168.66.171"                                                | 换为redis部署服务器的ipv4地址         |
| api-gateway-service.environment | - "spring.redis.password="                                                          | redis密码，若部署时为配置则可不填         |
| api-gateway-service.environment | - "hsweb.file.upload.static-location=http://192.168.66.171:9000/api/upload"         | 换为微服务部署服务器的ipv4地址           |

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <p>此处只列举了api服务的常见修改参数，剩余的authentication服务、file服务和iot服务也需要按照此处要求修改对应参数</p>
</div>

## JetLinks-cloud集群示例

```yaml
version: '2'
services:
  api-gateway-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/api-gateway-service:2.0.0-SNAPSHOT
    ports:
      - 8800:8800
    environment:
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.urls=192.168.66.171:9200"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=debug"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
  authentication-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/authentication-service:2.0.0-SNAPSHOT
    ports:
      - 8100:8100
    environment:
      - "TZ=Asia/Shanghai"
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.urls=192.168.66.171:9200"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=debug"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
  file-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/file-service:2.0.0-SNAPSHOT
    ports:
      - 8300:8300
    environment:
    - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
    - "spring.cloud.nacos.discovery.register-enabled=true"
    - "TZ=Asia/Shanghai"
    - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
    - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
    - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
    - "spring.r2dbc.username=postgres"
    - "spring.r2dbc.password=jetlinks"
    - "spring.elasticsearch.uris=192.168.66.171:9200"
    - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
    - "spring.redis.host=192.168.66.171"
    - "spring.redis.port=6379"
    - "spring.redis.password="
    - "logging.level.io.r2dbc=warn"
    - "logging.level.org.springframework.data=warn"
    - "logging.level.org.springframework=warn"
    - "logging.level.org.jetlinks=debug"
    - "logging.level.org.hswebframework=warn"
    - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
    volumes:
    - "./data/upload:/application/upload"
  iot-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/iot-service:2.0.0-SNAPSHOT
    ports:
      - 8200:8200
    environment:
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.uris=192.168.66.171:9200"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=debug"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
    volumes:
    - "./data/dumps:/dumps"
```
常见修改参数说明

| 标识名                               | 更改示例                                                                                | 说明                          |
|-----------------------------------|-------------------------------------------------------------------------------------|-----------------------------|
| api-gateway-service.image         | registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/api-gateway-service:2.0.0-SNAPSHOT | 镜像仓库地址请自行更换                 |
| api-gateway-service.environment   | - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"                    | 换为nacos部署服务器的ipv4地址         |
| api-gateway-service.environment   | - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks"                | 换为postgre部署服务器的ipv4地址       |
| api-gateway-service.environment   | - "spring.elasticsearch.uris=192.168.66.171:9200"                                   | 换为elasticsearch部署服务器的ipv4地址 |
| api-gateway-service.environment   | - "spring.redis.host=192.168.66.171"                                                | 换为redis部署服务器的ipv4地址         |
| api-gateway-service.environment   | - "spring.redis.password="                                                          | redis密码，若部署时为配置则可不填         |
| api-gateway-service.environment   | - "hsweb.file.upload.static-location=http://192.168.66.171:9000/api/upload"         | 换为微服务部署服务器的ipv4地址           |
| api-gateway-service.environment   | - "jetlinks.cluster.id=api-service:node1"                                           | 集群节点名                       |
| api-gateway-service.environment   | - "jetlinks.cluster.external-host=192.168.66.171"                                   | 对外暴露得ip                     |
| api-gateway-service.environment   | - "jetlinks.cluster.seeds[0]=192.168.66.171:18800"                                  | 集群子节点                       |
| api-gateway-service.environment   | - "jetlinks.cluster.seeds[0]=192.168.66.177:18800"                                  | 集群子节点                       |
| api-gateway-service.environment   | - "jetlinks.cluster.seeds[0]=192.168.66.178:18800"                                  | 集群子节点                       |

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <p>此处只列举了api服务的常见修改参数，剩余的authentication服务、file服务和iot服务也需要按照此处要求修改对应参数</p>
</div>