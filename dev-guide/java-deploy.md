#   后端部署
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  
本文档部署的服务器版本为`centos7`，项目编译打包用到了`jdk 1.8.0_333`和`maven 3.6.3`，构建、推送镜像用到了`docker 20.10.17`。

在后端部署之前请先完成中间件的部署，部署详细可以参考 <a href="/dev-guide/middleware-deploy.html">中间件部署</a>

</div>



## 问题指引
<table>
<tr>
    <td><a href="/dev-guide/java-deploy.html#未指定maven版本导致打包出错">未指定maven版本导致打包出错</a></td>
      <td><a href="/dev-guide/java-deploy.html#镜像启动报错">镜像启动报错</a></td>
</tr>
<tr>
    <td><a href="/dev-guide/java-deploy.html#权限管理中权限数据不完整">权限管理中权限数据不完整</a></td>
    <td><a href="/dev-guide/java-deploy.html#点击登录按钮无法进入首页-跳转到登录页面">点击登录按钮无法进入首页</a></td>
</tr>
</table>

## 文档指引
<table>
<tr>
   <td><a href="/dev-guide/java-deploy.html#jetlinks-pro-jar包部署">JetLinks-Pro Jar包部署</a></td>
    <td><a href="/dev-guide/java-deploy.html#jetlinks-pro-docker部署">JetLinks-Pro Docker部署</a></td>

</tr>
<tr>
    <td><a href="/dev-guide/java-deploy.html#jetlinks-cloud-jar包部署">JetLinks-Cloud Jar包部署</a></td>
    <td><a href="/dev-guide/java-deploy.html#jetlinks-cloud-docker部署">JetLinks-Cloud Docker部署</a></td>
</tr>
</table>


### Jetlinks-Pro Jar包部署

#### 拉取源码
1. 拉取`JetLinks pro`源码
```shell
 $ git clone -b master --recurse-submodules git@github.com:jetlinks-v2/jetlinks-pro.git
```
具体操作可参考<a href="/dev-guide/pull-code.html#源码获取">源码获取</a>

2. 修改配置文件

配置文件示例可参考<a href="/dev-guide/config-info.html">配置文件</a>




3. 使用maven命令将项目打包，在代码根目录执行：

linux或者macOS环境下打包:
```shell script
./mvnw clean package -Dmaven.test.skip=true
```
windows环境下打包:
```shell script
mvn clean package '-Dmaven.test.skip=true'
```



4. 将jar包上传到需要部署的服务器上。

jar包文件地址: `jetlinks-standalone/target/jetlinks-standalone.jar`

5. 使用java命令运行jar包

 ```shell
#启动时，使用默认配置文件
java -jar jetlinks-standalone.jar
```

 ```shell
# 启动时，修改配置文件中的参数，格式如下
java -jar jetlinks-standalone.jar {--配置文件中的参数}
#命令示例
java -jar jetlinks-standalone.jar --spring.elasticsearch.embedded.enabled=true
 ``` 
 
 ```shell
#启动时，指定外部配置文件，格式如下
java -jar jetlinks-standalone.jar --spring.config.location={外部配置文件全路径}
#命令示例
java -jar jetlinks-standalone.jar --spring.config.location=D:\code\jetlinks-pro\jetlinks-standalone\src\main\resources\application.yml
```


### Jetlinks-Pro Docker部署

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

在线部署和离线部署的区别:

在线部署：服务器在有网络环境，能够获取到镜像仓库中的镜像时，推荐使用在线部署，在dc配置文件中填写镜像仓库的地址就能直接拉取镜像到服务器上使用。

离线部署：服务器在无网络环境，无法获取到镜像仓库中的镜像时，只有使用离线部署，镜像就只能将镜像手动上传到服务器才能使用。

</div>

#### docker在线部署
1. 执行 `jetlinks-pro\build-and-push-docker.sh`路径下的脚本，脚本具体内容如下

```shell
#!/usr/bin/env bash
# 这个的镜像仓库地址需要替换为自己的镜像仓库地址
dockerImage="registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)"
./mvnw -Dmaven.test.skip=true \
#以下的子模块若不需要打包可自行删除
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
<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
请自行准备存放docker镜像的镜像仓库，此处以<a href="https://cr.console.aliyun.com/cn-shenzhen/instances">阿里云仓库</a>为例。

</div>

2. 查看镜像是否推送成功

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

#### docker离线部署

1. 在根目录创建镜像打包脚本`build-docker.sh`，脚本内容如下:
```shell
#!/usr/bin/env bash
#定义镜像名
dockerImage="jetlinks-standalone:$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)"
#下列组件若不需要打包可自行刪去
-Pmedia -Pedge -Pctwing -Ponenet -Pdueros -Paliyun-bridge -Popc-ua -Pmodbus \
./mvnw -Dmaven.test.skip=true \
-Dmaven.build.timestamp="$(date "+%Y-%m-%d %H:%M:%S")" \
-Dgit-commit-id="$(git rev-parse HEAD)" \
-Pmedia -T 12 \
clean package
if [ $? -ne 0 ];then
    echo "构建失败!"
else
  cd ./jetlinks-standalone || exit
  docker build -t "$dockerImage" . 
fi
```
2. 在根目录执行脚本`./build-docker.sh`

3. 导出镜像
在本地导出，导出的镜像会到源码根目录下，命令格式如下:
```shell
docker save -o [命名].tar [镜像名:版本号]
```
代码示例如下:
```shell
docker save -o jetlinks-standalone.tar jetlinks-standalone:2.0.0
```

4. 导入镜像
将导出的镜像传入服务器，再使用以下命令将镜像导入服务器
```shell
#导入命令格式
docker load -i [镜像名.tar]
#导入命令示例
docker load -i jetlinks-standalone.tar 
```

#### 使用docker-compose文件启动容器
1. 修改docker-compose文件

修改示例可参考<a href="/dev-guide/dc-info.html#JetLinks-pro示例">docker-compose文件</a>


2. 运行docker-compose文件

将docker-compose文件分别上传到每台服务器，再使用`docker-compose up -d`命令创建并启动容器，最后使用`docker ps -a`命令验证容器是否启动成功

```shell
$ docker ps -a
CONTAINER ID   IMAGE                                                                                COMMAND                  CREATED          STATUS        
               PORTS                                                                                                                NAMES
f303fc2fbd67   registry.cn-hangzhou.aliyuncs.com/jetlinks-demo/jetlinks-standalone:2.0.0-SNAPSHOT   "./docker-entrypoint…"   23 seconds ago   Up 16 seconds 
               0.0.0.0:1883->1883/tcp, 0.0.0.0:8100-8110->8100-8110/tcp, 0.0.0.0:8845->8845/tcp, 0.0.0.0:8200-8210->8200-8210/udp   jetlinks-pro
4e883fed1d0d   registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-vue:1.0.0                     
"/docker-entrypoint.…"   4 days ago       Up 7 minutes  
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

容器启动失败示例如下：
```shell
c0ac281c2877   registry.cn-hangzhou.aliyuncs.com/synbop/emqttd:2.3.6          "/opt/emqttd/start.sh"   4 days ago       Exited (137) 2days ago        emq                                         
```

</div>





### JetLinks-Cloud Jar包部署

1. 修改配置文件

配置文件示例可参考<a href="/dev-guide/config-info.html#源码获取">配置文件</a>

3. 修改打包脚本

修改`/jetlinks-cloud/build-and-push-docker.sh`路径下的打包脚本，修改内如如下：
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
./mvnw -Dmaven.test.skip=true \
-Dmaven.build.timestamp="$(date "+%Y-%m-%d %H:%M:%S")" \
-Dgit-commit-id="$(git rev-parse HEAD)" \
-Pmedia -T 12 \
clean package
if [ $? -ne 0 ];then
    echo "构建失败!"
fi
```
4. 执行打包脚本

在项目根路径执行
```shell
$ ./build.sh 
```


5. 将四个服务的jar包上传到需要部署的服务器上。

jar包文件地址:
```
micro-services/api-gateway-service/target/applicatione.jar
micro-services/authentication-service/target/applicatione.jar
micro-services/file-service/target/applicatione.jar
micro-services/iot-service/target/applicatione.jar
```



6. 使用java命令运行jar包，以api-gateway为例
```shell
cd ./micro-services/api-gateway-service/target/
#启动时，使用默认配置文件
java -jar jetlinks-applicatione.jar 
# 启动时，修改配置文件中的参数，格式如下
java -jar jetlinks-applicatione.jar {--配置文件中的参数}
#命令示例
java -jar jetlinks-applicatione.jar --spring.elasticsearch.embedded.enabled=true
#启动时，指定外部配置文件，格式如下
java -jar jetlinks-applicatione.jar --spring.config.location={外部配置文件全路径}
#命令示例
java -jar jetlinks-applicatione.jar --spring.config.location=D:\code\jetlinks-cloud-2.0\micro-services\api-gateway-service\src\main\resources\application.yml
```


### JetLinks-Cloud Docker部署


1. 修改打包脚本

修改`/jetlinks-cloud/build-and-push-docker.sh`路径下的打包脚本，修改内如如下：
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

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
请自行准备存放docker镜像的镜像仓库，此处以<a href="https://cr.console.aliyun.com/cn-shenzhen/instances">阿里云仓库</a>为例。

</div>

3. 查看是否推送成功
   ![java image](./images/cloud-images.png)

4. 修改`jetlinks-cloud/micro-services/docker-compose.yml`路径下的docker-compose文件

修改示例可参考<a href="/dev-guide/dc-info.html#jetlinks-cloud示例">docker-compose文件示例</a>


5. 将docker-compose文件分别上传到服务器
6. 使用docker-compose up -d命令创建并启动容器，使用docker ps -a命令验证容器是否启动成功
   docker常用命令请参考<a target="" href="../install-deployment/docker-start.html#docker常用命令">docker常用命令</a>

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


### docker离线部署
在根目录创建镜像打包脚本build-docker.sh，脚本内容如下
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
 #定义镜像名
 dockerImage="jetlinks-$s:$version"
 echo "build $s docker image $dockerImage"
 docker build -t "$dockerImage" .
 cd ../../
done
fi
```
在根目录执行脚本`./build-docker.sh`

#### 导出镜像
导出的镜像会到源码根目录下，命令格式如下:
```shell
docker save -o [命名].tar [镜像名:版本号]
```
命令示例如下:
```shell
docker save -o jetlinks-api-gateway-service.tar jetlinks-api-gateway-service:2.0.0-SNAPSHOT
docker save -o jetlinks-authentication-service.tar jetlinks-authentication-service:2.0.0-SNAPSHOT
docker save -o jetlinks-iot-service.tar jetlinks-iot-service-service:2.0.0-SNAPSHOT
docker save -o jetlinks-file-service.tar jetlinks-file-service:2.0.0-SNAPSHOT
```


2. 将docker-compose文件分别上传到每台服务器

3. 导入镜像
   使用以下命令将镜像导入服务器
```shell
#导入命令格式
docker load -i [镜像名.tar]
#导入命令示例
docker load -i jetlinks-api-gateway-service.tar 
docker load -i jetlinks-authentication-service.tar 
docker load -i jetlinks-iot-service.tar 
docker load -i jetlinks-file-service.tar 
```

4. 使用docker-compose up -d命令创建并启动容器，使用docker ps -a命令验证容器是否启动成功
   docker常用命令请参考<a target="" href="../install-deployment/docker-start.html#docker常用命令">docker常用命令</a>

```shell
$ docker ps -a
CONTAINER ID   IMAGE                                                                                    COMMAND                  CREATED          STATUS    
                    PORTS                                            NAMES
06f023229b31   jetlinks-api-gateway-service:2.0.0-SNAPSHOT      "./docker-entrypoint…"   34 seconds ago   Up 2 hours     
                                                    micro-services-api-gateway-service
3e22eddeb8a1   jetlinks-iot-service:2.0.0-SNAPSHOT              "./docker-entrypoint…"   34 seconds ago   Up 2 hours  
                                                    micro-services-iot-service
44f7f46fc291   jetlinks-file-service:2.0.0-SNAPSHOT             "./docker-entrypoint…"   34 seconds ago   Up 2 hours  
                                                    micro-services-file-service
ccaefa0d4c72   jetlinks-authentication-service:2.0.0-SNAPSHOT   "./docker-entrypoint…"   34 seconds ago   Up 2 hours  
                                                    micro-services-authentication-service
```
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  STATUS为up为容器启动成功，STATUS为Exited为容器启动失败。

容器启动失败示例如下：
```shell
06f023229b31   registry.cn-hangzhou.aliyuncs.com/jetlinks-cloud/api-gateway-service:2.0.0-SNAPSHOT      "./docker-entrypoint…"   4 days ago       Exited (137) 2days ago     
                                                    micro-services-api-gateway-service                                         
```
</div>

#### 将镜像和配置文件传入服务器

使用命令启动项目 `docker-compose up -d`



## 常见问题



### 未指定maven版本导致打包出错
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
项目打包失败并出现以下错误:

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

### 镜像启动报错 
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>

Q: `/usr/bin/env: ‘bash\r’: No such file or directory`

![java-docker-error](./images/java-docker-error.png)

A: 执行打包命令之前，需要检查整个项目换行符，需将换行符换为LF，否则运行镜像时会报以下错

</div>


选中JetLinks-pro项目，修改整个项目换行符：
![java-docker-error](./images/config-LF.png)

### 权限管理中权限数据不完整
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题3</span>
  </p>

Q: 非admin用户访问设备相关页面报错无权限。

![power-warn.png](./images/power-warn.png)


A: 在device-manager目录和things-manager目录下的pom文件中添加以下内容。

```yaml
        <dependency>
            <groupId>org.hswebframework.web</groupId>
            <artifactId>hsweb-system-authorization-default</artifactId>
            <version>${hsweb.framework.version}</version>
        </dependency>
```

</div>

### 点击登录按钮无法进入首页，跳转到登录页面
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题4</span>
  </p>
Q: 点击登录按钮无法进入首页，跳转到登录页面

A: 检查每个节点、每个服务是否连接同一个redis服务或redis集群服务，redis服务是否持久化成功。

</div>
