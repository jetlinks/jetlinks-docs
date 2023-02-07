# 集群部署
## 概述
集群是一组协同工作的服务集合，一般由两个或者两个以上的服务器组成。在集群中，同样的服务可以由多个服务实体提供，因而当一个节点出现故障时，集群中的另外一个节点就可以自动接管故障节点的资源。

本文档提供JetLinks-Pro和JetLinks-Cloud集群部署的详细步骤以及部署过程中常见问题的解决。

## 集群架构图
![java image](./images/colony.png)

## JetLinks-Pro集群部署

#### 拉取源码
1. 拉取`JetLinks pro`源码
```shell
 $ git clone -b master --recurse-submodules git@github.com:jetlinks-v2/jetlinks-pro.git
```
具体操作可参考<a href="/dev-guide/pull-code.html#源码获取">源码获取</a>。

2. 修改配置文件

   配置文件示例可参考<a href="/dev-guide/config-info.html#jetlinks-pro集群">配置文件</a>。

#### 构建、推送镜像

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
请自行准备存放docker镜像的镜像仓库，此处以<a href="https://cr.console.aliyun.com/cn-shenzhen/instances">阿里云仓库</a>为例。

</div>

1. 修改`jetlinks-pro\build-and-push-docker.sh`路径下的脚本文件，修改示例如下：
```shell
#!/usr/bin/env bash
# dockerImage的参数替换为自己的镜像仓库地址
dockerImage="registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)"
./mvnw -Dmaven.test.skip=true \
#以下包含Jetlinks所有子模块，若没有部分子模块权限或不需要打包部分子模块，可自行删减
-Pmedia -Pedge -Pctwing -Ponenet -Pdueros -Paliyun-bridge -Popc-ua -Pmodbus \
-Dmaven.build.timestamp="$(date "+%Y-%m-%d %H:%M:%S")" \
-Dgit-commit-id="$(git rev-parse HEAD)" \
-Pmedia -T 12 \
clean package
if [ $? -ne 0 ];then
    echo "构建失败!"
else
  cd ./jetlinks-standalone || exit
  docker build -t "$dockerImage" . && docker push "$dockerImage"
fi
```
2. 执行脚本

```shell
./build-and-push-docker.sh
```

3. 查看镜像是否推送成功

比较本地生成的digest和镜像仓库推送的digest是否一致，若保持一致则说明推送成功。

```shell
$ docker push registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0
The push refers to repository [registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone]
187cb63c5a7d: Layer already exists
29c7de453b8e: Layer already exists
144903481aa9: Layer already exists
849ea2764450: Layer already exists
f49d20b92dc8: Layer already exists
fe342cfe5c83: Layer already exists
630e4f1da707: Layer already exists
9780f6d83e45: Layer already exists
2.0.0: digest: sha256:43d5c8582793f2b352bebb481c50c731d81701735f880478e6d05061c985ca5e size: 3259
```

![java image](./images/java-image.png)

#### 启动项目

1. 创建docker-compose文件

docker-compose文件示例可参考<a href="/dev-guide/dc-info.html#jetlinks-pro示例">docker-compose文件示例</a>。


2. 将docker-compose配置文件分别上传到每台服务器
3. 使用`docker-compose up -d`命令创建并启动容器，使用`docker ps -a`命令验证容器是否启动成功

```shell
$ docker ps -a
CONTAINER ID   IMAGE                                                                                COMMAND                  CREATED          STATUS        
               PORTS                                                                                                                NAMES
f303fc2fbd67   registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT   "./docker-entrypoint…"   23 seconds ago   Up 16 seconds 
               0.0.0.0:1883->1883/tcp, 0.0.0.0:8100-8110->8100-8110/tcp, 0.0.0.0:8845->8845/tcp, 0.0.0.0:8200-8210->8200-8210/udp   jetlinks-pro
4e883fed1d0d   registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0                     "/docker-entrypoint.…"   4 days ago       Up 7 minutes  
               0.0.0.0:9000->80/tcp                                                                                                 jetlinks-pro-ui
84a9379e3944   kibana:7.17.3                                                                        "/bin/tini -- /usr/l…"   3 weeks ago      Up 8 minutes  
               0.0.0.0:5601->5601/tcp                                                                                               jetlinks-kibana      
6366d9063dd0   elasticsearch:7.17.3                                                                 "/bin/tini -- /usr/l…"   3 weeks ago      Up 7 minutes  
               0.0.0.0:9200->9200/tcp, 0.0.0.0:9300->9300/tcp                                                                       jetlinks-elasticsearch
7bc603f1e897   postgres:11-alpine                                                                   "docker-entrypoint.s…"   6 weeks ago      Up 7 minutes  
               0.0.0.0:5432->5432/tcp                                                                                               jetlinks-postgres       
4bdba77584ce   redis:5.0.4                                                                          "docker-entrypoint.s…"   2 months ago     Up 7 minutes  
               0.0.0.0:6379->6379/tcp                                                                                               jetlinks-redis
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  STATUS为up为容器启动成功，STATUS为Exited为容器启动失败。

容器启动失败示例如下
```shell
f303fc2fbd67   registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT   "./docker-entrypoint…"   4 days ago   Exited (137) 2days ago
               0.0.0.0:1883->1883/tcp, 0.0.0.0:8100-8110->8100-8110/tcp, 0.0.0.0:8845->8845/tcp, 0.0.0.0:8200-8210->8200-8210/udp   jetlinks-pro                                       
```
</div>

4. 启动前端服务
```shell
docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://xxx:8844/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0
```
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

`API_BASE_PATH`中的ip地址和端口号，请根据具体部署的后端地址配置。

</div>

#### 启动nginx

1. 配置nginx.conf文件，示例如下
```yaml
events {
    worker_connections  1024;
}

http {
# api接口服务(后端)
upstream apiserver {
    server 192.168.66.171:8844;
    server 192.168.66.177:8844;
    server 192.168.66.178:8844;
}

# 前端服务
upstream webserver {
  server 192.168.66.171:9000;
}

# 文件服务
upstream fileserver {
  server 192.168.66.171:8844;
}

server {

  listen       8080;
  server_name  localhost;

  location ^~/upload/ {
    proxy_pass http://fileserver;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Real-IP  $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }

  location ^~/jetlinks/file/static {
    proxy_pass http://fileserver/file/static;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Real-IP  $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_send_timeout      30m;
    proxy_read_timeout      30m;
    client_max_body_size    100m;
  }

  location ^~/jetlinks/ {
    proxy_pass http://apiserver/;
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

  location / {
    proxy_pass http://webserver/;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Real-IP  $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }
}
}
```

2. 启动nginx
```shell
whereis nginx #查询nginx文件夹具体位置
cd ./usr/local/sbin #切换到sbin目录下
./nginx #启动nginx
```




## JetLinks-Cloud集群部署

#### 拉取源码
1. 拉取`JetLinks Cloud`源码
```shell
 $ git clone -b master --recurse-submodules git@github.com:jetlinks-v2/jetlinks-cloud.git
```
具体操作可参考<a href="/dev-guide/pull-code.html#源码获取">源码获取</a>。

2. 修改配置文件

修改示例可参考<a href="/dev-guide/config-info.html#jetlinks-pro-单机版集群">配置文件示例</a>。

#### 构建、推送镜像

1. 修改`jetlinks-pro\build-and-push-docker.sh` 路径下的脚本文件，修改示例如下
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
 dockerImage="registry.cn-hangzhou.aliyuncs.com/jetlinks-ljs/$s:$version"
 echo "build $s docker image $dockerImage"
 docker build -t "$dockerImage" . && docker push "$dockerImage"
 cd ../../
done
fi
```
2. 执行脚本

```shell
./build-and-push-docker.sh
```

3. 检查每个服务本地生成的digest和镜像仓库中的digest是否一致,此处以api服务为例
```shell
The push refers to repository [registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/authentication-service]
a621ee2c27a7: Preparing
be8c43150f09: Preparing
22678f43f6e5: Pushed
5c446ffd104b: Pushed
e6b1f13d4d74: Pushed
5c45d38be933: Pushed
b5445123c81c: Mounted from jetlinks-cloud/api-gateway-service
29c7de453b8e: Layer already exists
144903481aa9: Layer already exists
849ea2764450: Layer already exists
f49d20b92dc8: Layer already exists
fe342cfe5c83: Layer already exists
630e4f1da707: Layer already exists
9780f6d83e45: Layer already exists
2.0.0-SNAPSHOT: digest: sha256:beefbf403ff1533975e8185e08927a4c3e2c307fbbd2c0f06fd39489352f4cb7 size: 3259
```
<img src="./images/api-images.png">


#### 启动项目
1. 创建docker-compose文件

   docker-compose文件示例可参考<a href="/dev-guide/dc-info.html#jetlinks-cloud示例">docker-compose文件示例</a>。

2. 将docker-compose配置文件分别上传到每台服务器
3. 使用docker-compose up -d命令创建并启动容器，使用docker ps -a命令验证容器是否启动成功
<a target="" href="../install-deployment/docker-start.html#docker常用命令">docker常用命令</a>


```shell
$ docker ps -a
CONTAINER ID   IMAGE                                                                                    COMMAND                  CREATED          STATUS    
                    PORTS                                            NAMES
06f023229b31   registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/api-gateway-service:2.0.0-SNAPSHOT      "./docker-entrypoint…"   34 seconds ago   Up 2 hours     
                                                    micro-services-api-gateway-service
3e22eddeb8a1   registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/iot-service:2.0.0-SNAPSHOT              "./docker-entrypoint…"   34 seconds ago   Up 2 hours  
                                                    micro-services-iot-service
44f7f46fc291   registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/file-service:2.0.0-SNAPSHOT             "./docker-entrypoint…"   34 seconds ago   Up 2 hours  
                                                    micro-services-file-service
ccaefa0d4c72   registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/authentication-service:2.0.0-SNAPSHOT   "./docker-entrypoint…"   34 seconds ago   Up 2 hours  
                                                    micro-services-authentication-service
```
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  STATUS为up为容器启动成功，STATUS为Exited为容器启动失败。

容器启动失败示例如下
```shell
06f023229b31   registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/api-gateway-service:2.0.0-SNAPSHOT      "./docker-entrypoint…"   4 days ago       Exited (137) 2days ago     
                                                    micro-services-api-gateway-service                                         
```
</div>

7. 启动前端服务
```shell
docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://xxx:8844/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0
```
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

`API_BASE_PATH`中的ip地址和端口号，请根据具体部署的后端地址配置。

</div>




8. 配置`nginx.config`文件
```yaml
events {
    worker_connections  1024;
}

http {
# api接口服务(后端)
upstream apiserver {
    server 192.168.66.171:8800;
    server 192.168.66.177:8800;
    server 192.168.66.178:8800;
}

# 前端服务
upstream webserver {
  server 192.168.66.171:9000;
}

# 文件服务
upstream fileserver {
  server 192.168.66.171:8800;
}

server {

  listen       8080;
  server_name  localhost;

  location ^~/upload/ {
    proxy_pass http://fileserver;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Real-IP  $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }

  location ^~/jetlinks/file/static {
    proxy_pass http://fileserver/file/static;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Real-IP  $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_send_timeout      30m;
    proxy_read_timeout      30m;
    client_max_body_size    100m;
  }

  location ^~/jetlinks/ {
    proxy_pass http://apiserver/;
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

  location / {
    proxy_pass http://webserver/;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Real-IP  $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }
}
}
```
8. 启动nginx
```shell
whereis nginx #查询nginx文件夹具体位置
cd ./usr/local/sbin #切换到sbin目录下
./nginx #启动nginx
```