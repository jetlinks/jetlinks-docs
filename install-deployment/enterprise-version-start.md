# 初始化项目

## 系统环境

启动jetlinks之前，请先确定已经安装好以下环境:

1. JDK 1.8.0_2xx (需要小版本号大于200) <a href='https://adoptopenjdk.net/releases.html?variant=openjdk8&jvmVariant=hotspot'>下载jdk</a>
2. Redis 5.x
3. PostgreSQL 11 或者 mysql 5.7 +
4. ElasticSearch 6.8 + <a href='https://www.elastic.co/cn/downloads/elasticsearch'>下载</a> ,也可以直接<a href='#内嵌elasticsearch启动'>使用内嵌ElasticSearch</a>.
5. maven 3.x+

## 获取代码

第一步: 先到个人设置中[添加SSH key](https://github.com/settings/keys)

第二步: 拉取代码

```bash
 $ git clone -b master --recurse-submodules git@github.com:jetlinks-v2/jetlinks-pro.git && git submodule foreach git checkout master
```

第三步: 更新代码

JetLinks Pro使用`git多模块`管理,使用此命令更新全部模块.
```bash
$ git pull && git submodule init && git submodule update && git submodule foreach git checkout master && git submodule foreach git pull origin master
```

## 添加扩展模块仓库

### 扩展模块说明

| 模块名称                        | 仓库名称 | 说明                    |
| ---------------------------- | ------ |  ------------------------- |
| 阿里云平台接入                   | jetlinks-aliyun-bridge-gateway      | 将平台的设备接入到阿里云，实现设备操控                       |
| 电信CTWing平台接入            | jetlinks-ctwing     | 接入CTWing平台的设备到本平台                         |
| 小度平台接入               | jetlinks-dueros      |  使用小度音响控制平台的设备                         |
| 移动OneNet平台接入              | jetlinks-onenet      |  接入OneNet平台的设备到本平台                         |
| GBT/28181视频设备接入       | jetlinks-media      |   使用GBT/28181接入视频设备，实现直播、录像、云台控制等                        |
|  Modbus/TCP           | jetlinks-modbus      |     支持Modbus/TCP协议数采                      |
| OPC UA            | jetlinks-opc-ua      | 支持OPC UA协议数采                        |



<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  如您有选购扩展模块，请按如下命令添加扩展模块到项目中。
</div>


```bash
$  git remote add github "git@github.com:/jetlinks-v2/{扩展模块仓库名称}.git"
$  git submodule foreach 'git remote add github "git@github.com:/jetlinks-v2/{扩展模块仓库名称}.git"'
$  git push github master
$  git submodule foreach git push github master 
```

# 初始化启动

第一步 中间件准备

进入项目根目录，运行如下命令即可:

```bash

$ docker-compose up -d

```

第二步 启动项目

进入项目根目录，分别运行如下命令

```bash

$ mvn clean package -Dmaven.test.skip=true
$ cd ./jetlinks-standalone/target 
$ java -jar jetlinks-standalone.jar 

```

第三步 访问项目

执行前端运行命令

```bash
docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://{宿主机IP}:{后端应用端口}/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-vue:2.1.0-SNAPSHOT
```

浏览器访问  `localhost:9000` ；输入账号：admin；密码:admin。



