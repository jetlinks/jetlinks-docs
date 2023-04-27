# 部署到服务器

## 前端部署

环境要求
- nodeJs v12.xx
- npm v6.xx

1. 获取源代码  
```shell script
$ git clone -b https://gitee.com/jetlinks/jetlinks-ui-vue.git
```

2. 使用npm打包,并将打包后的文件复制到项目的docker目录下（命令在项目根目录下执行）  
```shell script
npm install
npm run-script build 
cp -r dist docker/       
```

### 使用docker部署前端
1. 构建docker镜像  
```bash
docker build -t registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-vue:2.1.0-SNAPSHOT ./docker
```

2. 运行docker镜像  
```bash
docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://xxx:8844/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-vue:2.1.0-SNAPSHOT
```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

环境变量`API_BASE_PATH`为后台API根地址. 由docker容器内进行自动代理. 请根据自己的系统环境配置环境变量: `API_BASE_PATH`

</div>

### 使用nginx部署

1. 复制`dist`目录下到文件到`/usr/share/nginx/html`
2. 添加nginx配置文件

nginx配置参考：

```conf
server {
    listen 80;
    # gzip config
    gzip on;
    gzip_min_length 1k;
    gzip_comp_level 9;
    gzip_types text/plain text/css text/javascript application/json application/javascript application/x-javascript application/xml;
    gzip_vary on;
    gzip_disable "MSIE [1-6]\.";

    root /usr/share/nginx/html;
    include /etc/nginx/mime.types;
    location / {
        index  index.html;
    }

    location ^~/api/ {
        proxy_pass http://api:8844/; #修改此地址为后台服务地址
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Host $host:$server_port;
        proxy_set_header X-Real-IP  $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_connect_timeout   1;
        proxy_buffering off;
        chunked_transfer_encoding off;
        proxy_cache off;
        proxy_send_timeout      30m;
        proxy_read_timeout      30m;
        client_max_body_size    100m;
    }
}
```


## 后端部署

### 使用docker部署后端

1. 使用maven命令将项目打包  
在代码根目录执行：  

```shell script
mvn clean package -Dmaven.test.skip=true
```

2. 使用docker构建镜像  
<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
请自行准备docker镜像仓库，此处以registry.cn-shenzhen.aliyuncs.com阿里云私有仓库为例。
</div>

```shell script
$ cd ./jetlinks-standalone
$ docker build -t registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:latest .
```

3. 推送镜像

```bash
$ docker push registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:latest
```

4. 创建docker-compose文件  

```bash
version: '2'
services:
  redis:
    image: redis:5.0.4
    container_name: jetlinks-ce-redis
    #    ports:
    #      - "6379:6379"
    volumes:
      - "redis-volume:/data"
    command: redis-server --appendonly yes --requirepass "JetLinks@redis"
    environment:
      - TZ=Asia/Shanghai
  elasticsearch:
    image: elasticsearch:6.8.11
    container_name: jetlinks-ce-elasticsearch
    environment:
      ES_JAVA_OPTS: -Djava.net.preferIPv4Stack=true -Xms1g -Xmx1g
      transport.host: 0.0.0.0
      discovery.type: single-node
      bootstrap.memory_lock: "true"
      discovery.zen.minimum_master_nodes: 1
      discovery.zen.ping.unicast.hosts: elasticsearch
    volumes:
      - elasticsearch-volume:/usr/share/elasticsearch/data
  #    ports:
  #      - "9200:9200"
  #      - "9300:9300"
  kibana:
    image: kibana:6.8.11
    container_name: jetlinks-ce-kibana
    environment:
      ELASTICSEARCH_URL: http://elasticsearch:9200
    links:
      - elasticsearch:elasticsearch
    ports:
      - "5602:5601"
    depends_on:
      - elasticsearch
  postgres:
    image: postgres:11-alpine
    container_name: jetlinks-ce-postgres
    volumes:
      - "postgres-volume:/var/lib/postgresql/data"
    ports:
      - "5432:5432"
    environment:
      POSTGRES_PASSWORD: jetlinks
      POSTGRES_DB: jetlinks
      TZ: Asia/Shanghai
  ui:
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-vue:2.1.0-SNAPSHOT
    container_name: jetlinks-ce-ui
    ports:
      - 9000:80
    environment:
      - "API_BASE_PATH=http://jetlinks:8848/" #API根路径
    volumes:
      - "jetlinks-volume:/usr/share/nginx/html/upload"
    links:
      - jetlinks:jetlinks
  jetlinks:
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:2.0.0-SNAPSHOT
    container_name: jetlinks-ce
    ports:
      - "8848:8848" # API端口
      - "1883-1890:1883-1890" # 预留
      - "8800-8810:8800-8810" # 预留
      - "5060-5061:5060-5061" # 预留
    volumes:
      - "jetlinks-volume:/application/static/upload"  # 持久化上传的文件
      - "jetlinks-file-volume:/application/data/files"
      - "jetlinks-protocol-volume:/application/data/protocols"
    environment:
      - "JAVA_OPTS=-Duser.language=zh -XX:+UseG1GC"
      - "TZ=Asia/Shanghai"
      - "hsweb.file.upload.static-location=http://127.0.0.1:8848/upload"  #上传的静态文件访问根地址,为ui的地址.
      - "spring.r2dbc.url=r2dbc:postgresql://postgres:5432/jetlinks" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.data.elasticsearch.client.reactive.endpoints=elasticsearch:9200"
#        - "spring.data.elasticsearch.client.reactive.username=admin"
#        - "spring.data.elasticsearch.client.reactive.password=admin"
#        - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=redis"
      - "spring.redis.port=6379"
      - "file.manager.storage-base-path=/application/data/files"
      - "spring.redis.password=JetLinks@redis"
      - "logging.level.io.r2dbc=warn"
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=warn"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
      - "network.resources[0]=0.0.0.0:8800-8810/tcp"
      - "network.resources[1]=0.0.0.0:1883-1890"
      - "hsweb.cors.enable=true"
      - "hsweb.cors.configs[0].path=/**"
      - "hsweb.cors.configs[0].allowed-credentials=true"
      - "hsweb.cors.configs[0].allowed-headers=*"
      - "hsweb.cors.configs[0].allowed-origins=*"
      - "hsweb.cors.configs[0].allowed-methods[0]=GET"
      - "hsweb.cors.configs[0].allowed-methods[1]=POST"
      - "hsweb.cors.configs[0].allowed-methods[2]=PUT"
      - "hsweb.cors.configs[0].allowed-methods[3]=PATCH"
      - "hsweb.cors.configs[0].allowed-methods[4]=DELETE"
      - "hsweb.cors.configs[0].allowed-methods[5]=OPTIONS"
    links:
      - redis:redis
      - postgres:postgres
      - elasticsearch:elasticsearch
    depends_on:
      - postgres
      - redis
      - elasticsearch
volumes:
  postgres-volume:
  redis-volume:
  elasticsearch-volume:
  jetlinks-volume:
  jetlinks-file-volume:
  jetlinks-protocol-volume:
```
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

jetlinks docker镜像版本更新和源代码根目录下文件pom.xml中的版本号同步。

</div>

6.运行docker-compose文件

```shell script
docker-compose up -d
```

### jar包方式

1.使用maven命令将项目打包，在代码根目录执行：  
   
```shell script
./mvnw clean package -Dmaven.test.skip=true
```

2.将jar包上传到需要部署的服务器上。  

jar包文件地址: `jetlinks-standalone/target/jetlinks-standalone.jar`

3.使用java命令运行jar包  

```bash
$ java -jar jetlinks-standalone.jar
```
<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

请根据情况调整jvm参数等信息.

</div>

