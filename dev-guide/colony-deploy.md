# JetLinks-Pro集群部署

## 概述

本文档提供jetlinks-pro集群部署的详细步骤。

## 文档推荐

<table>
   <tr>
       <td><a href="/dev-guide/colony-cloud-deploy.html">jetlinks-cloud集群部署</a></td>

[//]: # (       <td><a href="">ES集群部署</a></td>)
   </tr>

[//]: # (   <tr>)

[//]: # (       <td><a href="">Redis集群部署</a></td>)

[//]: # (       <td><a href="">PG/Mysql集群部署</a></td>)

[//]: # (   </tr>)
</table>

## 集群架构图

![java image](./images/colony.png)

## JetLinks-Pro集群部署

#### 材料准备

1. 拉取`jetlinks-pro`源码,具体操作可参考<a target="_blank" href="/dev-guide/pull-code.html#源码获取">源码获取</a>。
2. 修改配置文件，配置文档参数修改参考<a target="_blank" href="/dev-guide/config-info.html#配置文件常见修改说明">常见参数说明</a>。
3. 源码打`jar`包或`docker`镜像请移步<a target="_blank" href="/dev-guide/java-deploy.html">部署文档指引</a>，选择对应的打包方式并按指引操作。
4. 上传部署文件（jar、基础服务组件安装包及镜像等）。

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

   <p>如需使用docker方式部署，需自行安装docker环境，<a target="_blank" href="/install-deployment/docker-start.html#安装docker">安装docker</a>。</p>

</div>

#### 各服务器上传jar、镜像等操作

相关材料上传及<a target="_blank" href="/dev-guide/middleware-deploy.html">部署基础服务</a>（Redis、ES、PG/Mysql）完成后，需在启动前修改部分参数。

<div class='explanation error'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>重要</span>
  </p>

   <p>部署集群需要修改  <span class='explanation-title font-weight'>jetlinks.server-id</span>或者
<span class='explanation-title font-weight'>jetlinks.cluster.id</span>两者中任一值，该值<span class='explanation-title font-weight'>必须不一致且固定</span>，不可设置动态随机值。</p>
<p><span class='explanation-title font-weight'>jetlinks.cluster.id</span>一致会导致集群内在某些场景下无法进行数据广播通知其余服务节点。</p>
<p><span class='explanation-title font-weight'>jetlinks.cluster.id</span>不一致启动时随机会导致在某些场景下后端应用根据此参数从缓存中拿不到历史信息。</p>

</div>

| 参数                               | 说明                                                   | 
|----------------------------------|------------------------------------------------------|
| `jetlinks.server-id`             | 当前后端应用的唯一id，该参数已被注解过时，建议使用`jetlinks.cluster.id`      | 
| `jetlinks.cluster.id`            | 引用`jetlinks.server-id`的值,和`jetlinks.server-id`二选一即可。 |
| `jetlinks.cluster.external-host` | 集群节点通信对外暴露的host                                      |
| `jetlinks.cluster.external-port` | 集群节点通信对外暴露的端口                                        | 
| `jetlinks.cluster.seeds`         | 集群种子节点,集群时,配置为集群节点的 external-host:external-port      | 
| `spring.config.location`         | 指定配置文件路径，使用该参数可以指定spring使用外置配置文件，而非jar包内部打包配置文件      | 

#### jar启动

在各服务器上执行以下命令，该命令携带了两个动态参数指定了集群相关信息，需在各应用启动时指定不同的`cluster.id`。
`seeds`则需要将所有种子节点的通信地址填入，包括当前节点地址信息。

```shell

java -jar jetlinks-standalone.jar --jetlinks.cluster.id=jetlinks:node1 --jetlinks.cluster.seeds=192.168.66.171:18844,192.168.66.177:18844,192.168.66.178:18844

```

#### docker启动

1. 上传`docker-compose`容器编排文件至各服务器（该文件以下简称`DC文件`），文件路径`jetlinks-pro\dist\docker-compose.yml`。

DC文件示例:
```shell
version: '2'
services:
  jetlinks:
    image: registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT
    container_name: jetlinks-pro
    ports:
      - 8845:8845 # API端口
      - 1883:1883 # MQTT端口
      - 11883:11883 # 通过openAPI使用mqtt订阅平台消息
      - 8100-8110:8100-8110 # 预留端口
      - 8200-8210:8200-8210/udp # udp端口
    volumes:
      - "./data:/application/data" # 临时保存协议目录
      - "./data/upload:/application/static/upload"  # 持久化上传的文件
    environment:
      ... # 此处省略的参数与jetlinks-pro单机版一致，详情参考单机版DC文件
      - "jetlinks.cluster.id=jetlinks-service:node1" # 后端应用的唯一id，请保持每个节点之间id不同
      - "jetlinks.cluster.external-host=192.168.66.171" # 集群节点通信对外暴露的host
      - "jetlinks.cluster.seeds[0]=192.168.66.171:18844" # 集群种子节点1
      - "jetlinks.cluster.seeds[1]=192.168.66.177:18844" # 集群种子节点2
      - "jetlinks.cluster.seeds[1]=192.168.66.178:18844" # 集群种子节点3
```

3. 在`DC文件`所在目录执行`docker-compose up -d`命令创建并启动容器，使用`docker ps -a`命令并查看`STATUS`为`UP`则表示启动成功。

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


#### 启动前端

1. 配置nginx.conf文件，示例如下

```bash
events {
  worker_connections  1024;
}

http{

  upstream iotserver {
      server 192.168.66.171:8844 weight=1; #轮询地址，请根据实际部署后端地址进行替换
      server 192.168.66.177:8844 weight=1;
      server 192.168.66.178:8844 weight=1;
  }

   server {
      listen 9000; #监听端口号
      listen [::]:9000;
      gzip on;
      gzip_min_length 1k;
      gzip_comp_level 9;
      gzip_types text/plain text/css text/javascript application/json application/javascript application/x-javascript application/xml;
      gzip_vary on;
      gzip_disable "MSIE [1-6]\.";
      root /usr/local/nginx/html/dist;  # 替换为dist文件上传路径
      location / {
        index  index.html;
      }

      location ^~/api/ {
         proxy_pass http://iotserver/;
         proxy_set_header X-Forwarded-Proto $scheme;
         proxy_set_header Host $host;
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
         client_max_body_size    500m;
      }
   }
}

```

2. 启动nginx

```shell
# 在nginx可执行程序所在目录下执行下方命令 -c 指定配置文件
./nginx -c /usr/local/nginx/conf/nginx.conf #启动nginx
```

#### TCP均衡负载

1. nginx.conf配置文件如下：
```shell
user  root;
worker_processes  1;

error_log  /usr/local/nginx/logs/error1.log warn;
pid        /var/run/nginx.pid;

events {
    worker_connections  1024;
}


stream {
   
   upstream tcp-test {
       hash $remote_addr consistent;
       server 192.168.66.171:1883 weight=1;
       server 192.168.66.177:1883 weight=1;
       server 192.168.66.178:1883 weight=1;
   }

   server {
     
       listen 1883;
       proxy_pass tcp-test;
   }
}
```
2. 启动nginx

```shell
# 在nginx可执行程序所在目录下执行下方命令 -c 指定配置文件
./nginx -c /usr/local/nginx/conf/nginx-tcp.conf #启动nginx
```