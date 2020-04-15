# 快速部署

在[获取源代码](docker-start.md#获取源代码)后可通过以下两种方式部署程序。  
## docker方式

1. [安装docker](docker-start.md#安装docker)  

2. 使用maven命令将项目打包  
在代码根目录执行：  

```shell script
mvn clean package -Dmaven.test.skip=true
```
3.使用docker构建镜像  
::: tip 注意
请自行准备docker镜像仓库，此处以registry.cn-shenzhen.aliyuncs.com阿里云私有仓库为例。
:::

```shell script
cd ./jetlinks-standalone
docker build -t registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone
docker push registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone
```

4.拉取镜像  
在需要部署的服务器上拉取构建成功的镜像。  

```shell script
docker pull registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone
```

5.创建docker-compose文件  

```shell script
version: '2'
services:
  redis:
    image: redis:5.0.4
    container_name: jetlinks-redis
    ports:
      - "6379:6379"
    volumes:
      - "./data/redis:/data"
    command: redis-server --appendonly yes
    environment:
      - TZ=Asia/Shanghai
  elasticsearch:
    image: elasticsearch:6.7.2
    container_name: jetlinks-elasticsearch
    environment:
      ES_JAVA_OPTS: -Djava.net.preferIPv4Stack=true -Xms1g -Xmx1g
      transport.host: 0.0.0.0
      discovery.type: single-node
      bootstrap.memory_lock: "true"
      discovery.zen.minimum_master_nodes: 1
      discovery.zen.ping.unicast.hosts: elasticsearch
#    volumes:
#      - ./data/elasticsearch:/usr/share/elasticsearch/data
    ports:
      - "9200:9200"
      - "9300:9300"
  kibana:
    image: kibana:6.7.2
    container_name: jetlinks-kibana
    environment:
      ELASTICSEARCH_URL: http://elasticsearch:9200
    links:
      - elasticsearch:elasticsearch
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
  postgres:
    image: postgres:11-alpine
    container_name: jetlinks-postgres
    ports:
      - "5432:5432"
    volumes:
      - "./data/postgres:/var/lib/postgresql/data"
    environment:
      POSTGRES_PASSWORD: jetlinks
      POSTGRES_DB: jetlinks
      TZ: Asia/Shanghai
  rabbitmq:
    image: rabbitmq:management-alpine
    container_name: rabbitmq
    environment:
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=jetlinks
      - RABBITMQ_ERLANG_COOKIE=jetlinks
    ports:
      - "15672:15672"
      - "5672:5672"
  jetlinks:
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone
    container_name: jetlinks-pro
    environment:
      - server.port=8848 #端口可自行配置
      - spring.redis.host=redis
      - spring.r2dbc.url=r2dbc:postgresql://postgres:5432/jetlinks
      - management.metrics.export.elastic.host=http://elasticsearch:9200
      - elasticsearch.client.host=elasticsearch
    links:
      - redis
      - postgres
      - elasticsearch
      - rabbitmq
    ports:
      - "8848:8848"
```

6.运行docker-compose文件

```shell script
docker-compose up -d
```

## jar包方式

1.使用maven命令将项目打包   
   在代码根目录执行：  
   
   ```shell script
   mvn clean package -Dmaven.test.skip=true
   ```
2.将jar包上传到需要部署的服务器上。  

3.使用java命令运行jar包  

```shell script
java -jar jetlinks-standalone.jar
```

## 前端部署

1.获取源代码  
```shell script
git clone https://github.com/jetlinks/jetlinks-ui-antd.git
```

2.使用npm打包  
```shell script
npm install
npm run-script build        
```
3.构建docker镜像  
```shell script
docker build -t registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd:1.0-SNAPSHOT
```
4.运行docker镜像  
```shell script
docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http:/xxx:8848/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd:1.0-SNAPSHOT
```
::: tip 注意
环境变量`API_BASE_PATH`为后台API根地址. 由docker容器内进行自动代理. 请根据自己的系统环境配置环境变量: `API_BASE_PATH`
:::

### 前端环境要求
- nodeJs v12.14
- npm v6.13

