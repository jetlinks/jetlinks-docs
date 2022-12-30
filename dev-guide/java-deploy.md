#   后端部署及相关问题
jetlinks的单机版和微服务版的后端部署会存在一些差异，本文档会分别说明两者部署的流程及遇到的问题
## JetLinks Pro(单机版)
### 使用docker部署后端

1. 使用maven命令将项目打包  
   在代码根目录执行：

```shell script
./mvnw clean package -Dmaven.test.skip=true
```
2. 使用docker构建镜像

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
请自行准备docker镜像仓库，此处以阿里云仓库为例。
</div>

```shell script
$ cd ./jetlinks-standalone
#注意:命令末尾的 . 不要遗漏了
$ docker build -t registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:latest .
```

3. 推送镜像

```bash
#登录阿里云镜像仓库，此处会让你输密码，就是创建镜像服务时自己设置的密码
docker login --username=[username] registry.cn-shenzhen.aliyuncs.com
#设置tag
docker tag [ImageId] registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:2.0.0
#推送到阿里云镜像仓库
$ docker push registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:2.0.0
```
4. 查看镜像是否推送成功
   ![java image](./images/java-image.png)

5. 创建docker-compose文件

替换掉前后端的镜像仓库地址
```bash
version: '2'
services:
  ui:
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0
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
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:2.0.0
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

## JetLinks Cloud(微服务版)

### 打包源码、构建镜像、推送到仓库
1. 登录到阿里云仓库
```shell
#请自行准备docker镜像仓库，这里以阿里云镜像仓库为例
$ docker login --username=aliyun0245896286 registry.cn-hangzhou.aliyuncs.com
Password: 
Login Succeeded
```
2. 运行打包脚本

在项目根路径执行
```shell
$ ./build-and-push-docker.sh 
```
脚本文件具体内容如下
```shell
#!/usr/bin/env bash
servers="$1"
if [ -z "$servers" ]||[ "$servers" = "all" ];then
servers="api-gateway-service,authentication-service,iot-service,file-service"
fi

IFS=","
arr=($a)

version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "start build : $servers : $version"
## 使用maven打包
./mvnw -Dmaven.test.skip=true \
-Dmaven.build.timestamp="$(date "+%Y-%m-%d %H:%M:%S")" \
-Dgit-commit-id="$(git rev-parse HEAD)" \
-Pmedia -T 12 \
clean package
if [ $? -ne 0 ];then
    echo "构建失败!"
else

#四个微服务分别构建镜像并推送到仓库
for s in ${servers}
do
 cd "./micro-services/${s}" || exit
 dockerImage="registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/$s:$version"
 echo "build $s docker image $dockerImage"
 docker build -t "$dockerImage" . && docker push "$dockerImage"
 cd ../../
done

fi
```
3. 查看是否推送成功
   ![java image](./images/cloud-images.png)

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题</span>
  </p>
项目打包失败并出现以下错误

```shell
[WARNING] Error injecting: org.springframework.boot.maven.RepackageMojo
java.lang.TypeNotPresentException: Type org.springframework.boot.maven.RepackageMojo not present
```
解决:指定`api-gateway-service`、`authentication-service`、`iot-service`和`file-service`四个模块`pom`文件中`maven`的版本，使`maven`版本和`spring boot`版本保持一致，例如:
```shell
<plugin>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-maven-plugin</artifactId>
   <version>${spring.boot.version}</version>
</plugin>
```

</div>
5. 创建docker-compose文件

将每个服务的镜像地址替换为之前推送的镜像仓库地址

将中间件的host地址替换为服务器的ip地址

```shell
version: '2'
services:
  nacos:
    image: nacos/nacos-server:v2.1.0
    container_name: jetlinks-nacos
    environment:
      - PREFER_HOST_MODE=hostname
      - MODE=standalone
    volumes:
      - ./data/nacos/logs/:/home/nacos/logs
    #      - ./data/nacos/custom.properties:/home/nacos/init.d/custom.properties
    ports:
      - "8848:8848"
  api-gateway-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/api-gateway-service:2.0.0-SNAPSHOT
    ports:
      - 8801:8801
    environment:
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks-cloud-2.0" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.urls=elasticsearch:9200"
      - "spring.reactor.debug-agent.enabled=false" #设置为false能提升性能
      - "spring.redis.host=192.168.66.171"
      - "spring.redis.port=6379"
      - "spring.redis.password="
      - "logging.level.io.r2dbc=warn" #定义日志级别
      - "logging.level.org.springframework.data=warn"
      - "logging.level.org.springframework=warn"
      - "logging.level.org.jetlinks=debug"
      - "logging.level.org.hswebframework=warn"
      - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
  authentication-service:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/authentication-service:2.0.0-SNAPSHOT
    ports:
      - 8100:8100
    environment:
      - "TZ=Asia/Shanghai"
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks-cloud-2.0" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.urls=elasticsearch:9200"
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
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/file-service:2.0.0-SNAPSHOT
    ports:
      - 8300:8300
    environment:
    - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
    - "spring.cloud.nacos.discovery.register-enabled=true"
    - "TZ=Asia/Shanghai"
    - "JAVA_OPTS=-Xms1g -Xms1g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
    - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
    - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks-cloud-2.0" #数据库连接地址
    - "spring.r2dbc.username=postgres"
    - "spring.r2dbc.password=jetlinks"
    - "spring.elasticsearch.urls=elasticsearch:9200"
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
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/iot-service:2.0.0-SNAPSHOT
    ports:
      - 8200:8200
    environment:
      - "spring.cloud.nacos.discovery.server-addr=192.168.66.171:8848"
      - "spring.cloud.nacos.discovery.register-enabled=true"
      - "TZ=Asia/Shanghai"
      - "JAVA_OPTS=-Xms1g -Xms4g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/application/static/upload/dump.hprof"  # jvm参数，根据情况调整
      - "hsweb.file.upload.static-location=http://192.168.66.171:9000/upload"  #上传的静态文件访问根地址,为本机的IP或者域名。需要前后端都能访问。
      - "spring.r2dbc.url=r2dbc:postgresql://192.168.66.171:5432/jetlinks-cloud-2.0" #数据库连接地址
      - "spring.r2dbc.username=postgres"
      - "spring.r2dbc.password=jetlinks"
      - "spring.elasticsearch.urls=elasticsearch:9200"
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
6.运行docker-compose文件

```shell script
# 进入docker compose文件路径下
cd ./micro-services/
docker-compose up -d
```
### jar包方式
1. 使用maven命令将项目打包  
   在代码根目录执行：
```shell 
./mvnw clean package -Dmaven.test.skip=true
```
2.将四个服务的jar包上传到需要部署的服务器上。

jar包文件地址: 

`micro-services/api-gateway-service/target/applicatione.jar`

`micro-services/authentication-service/target/applicatione.jar`

`micro-services/file-service/target/applicatione.jar`

`micro-services/iot-service/target/applicatione.jar`

3.使用java命令运行jar包，以api-gateway为例
```shell
cd ./micro-services/api-gateway-service/target/
#运行jar包并将日志存在.txt文件中
nohup java -jar demo.jar >api-gateway.txt &
```
