# 视频接入流程
视频接入支持GB/T28181和固定地址2种方式的接入。  

## GB/T28181接入

### 1. 部署ZLMedia
国标GB/T28181视频的播放使用了<a target='_blank' href='https://github.com/ZLMediaKit/ZLMediaKit>ZLMedia</a>作为流媒体服务商，需要单独部署。
可以使用docker镜像部署或者源码部署。
   
#### docker镜像部署
jetlinks视频模块代码中的`jetlinks-pro\expands-components\jetlinks-media\zlm`目录下，提供了docker-compose的脚本以及ZLMedia配置文件。
使用命令`docker-compose pull && docker-compose up -d`即可启动。

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

<li>启动容器前需要修改配置文件`config.ini`，将回调地址中的`host.docker.internal`改为jetlinks平台部署的地址。</li>

<li>视频推流端口配置`rtp_proxy`默认为10000，docker配置的端口映射到了9100。端口对应`流媒体服务`配置中的`RTP IP`。</li>  
<li>调用api接口的端口默认为80，docker配置的端口映射到了8180。端口对应`流媒体服务`配置中的`API Host`。</li>

</div>

#### 源码部署
ZLMedia使用C++编写，需要对应gcc编译器。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

详细步骤参照<a target='_blank' href='https://github.com/ZLMediaKit/ZLMediaKit/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B'>项目官网文档</a>。

</div>

ZLMedia的启动与关闭，使用编译后目录中的脚本。

```shell
# 启动ZLMedia
./MediaServer -d &

# 热加载配置文件
killall -1 MediaServer

# 关闭
killall -2 MediaServer
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

详情参照<a target='_blank' href='https://github.com/ZLMediaKit/ZLMediaKit/wiki/%E6%9C%8D%E5%8A%A1%E5%99%A8%E7%9A%84%E5%90%AF%E5%8A%A8%E4%B8%8E%E5%85%B3%E9%97%AD'>项目官网文档-服务器的启动与关闭</a>。

</div>

#### ZLMedia配置
配置文件为`config.init`。以下为`流媒体服务`对应的配置参数：

- `[api]-secret`：api访问密钥，对应`流媒体服务`配置中的`密钥`。
- `[rtp_proxy]-port`：视频推流端口，默认为10000。对应`流媒体服务`配置中的`RTP IP`端口。  
  `[http]-port`调用api接口的端口，默认为80，对应`流媒体服务`配置中的`API Host`。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

详细配置内容参照<a target='_blank' href='https://github.com/zlmediakit/ZLMediaKit/blob/master/conf/config.ini'>项目官方示例</a>

</div>

### 2. 配置流媒体服务
在`物联网`-`运维管理`-`流媒体服务`中新建流媒体服务。配置ZLMedia配置文件对应的密钥、IP与端口。
用于设备推送流媒体数据。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

流媒体服务配置步骤参照<a href='/Mocha_ITOM/Device_access_gateway5.1.html#流媒体服务'>操作手册-流媒体服务</a>。

</div>

### 3. 在设备接入网关配置信令 
在`物联网`-`运维管理`-`设备接入网关`中新增配置，配置GB28181信令服务的信息（SIP域、SIP ID、SIP地址等）。
用于设备接入。

> SIP ID：https://www.cnblogs.com/cash/p/14177671.html (opens new window)根据这个网站提供的规则进行编写
>
> SIP 域：SIP ID取前10位

- 具体端口开放根据部署情况而定
- 如使用jar包部署且为本地部署则不受限制
- 如使用jar包部署但使用云服务器部署，需要到云服务器控制台安全组开放udp端口
- 使用docker部署，需要映射端口，如docker部署在云服务器上，仍需要开放安全组端口
- 使用docker-compose编排文件部署，查看docker-compose文件内jetlinks节点的port节点下udp预留端口，部署在云服务器上，仍需要开放安全组端口

### 4. 创建产品
创建一个视频设备的产品，选择新增的设备接入网关并启用。

### 5. 创建视频设备
   在`视频中心`-`视频设备`中新增视频设备。详细操作步骤见<a href='/Video_Center/Video_equipment10.html#视频设备-2'>操作手册-视频设备</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <li>ID为视频设备的SIP ID，可在摄像头自己的管理页面查询。</li>
  <li>选择产品，产品需要绑定视频设备接入网关。而国标信令配置在视频设备接入网关中。</li>
  <li>接入密码可自定义。摄像头的密码配置需要与其保持一致。</li>

</div>

### 6. 配置摄像头
摄像头配置平台接入，根据以上配置参数填写即可。可以参照<a href='/media-guide/media-base-config.html'>摄像头配置示例</a>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <li>信令服务SIP信息填写`设备接入网关`中的配置。</li>
  <li>注册密码填写`视频设备`中的配置。</li>

</div>

摄像头配置成功后，延迟几十秒左右，视频设备显示在线且能查看到通道数据，表示接入成功。  

#### 无法播放视频
若视频无法播放，建议检查`流媒体服务`的配置是否与部署的ZLMedia一致。以及视频设备到ZLMedia（`RTP IP`对应的IP与端口）的网络是否通畅。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <li>可使用端口扫描指令`nc -vuz [host] [port]`，测试UDP端口是否连通。</li>
  <li>可使用`tcpdump -i any port 9100 -w tcpdump.pcap`、`tcpdump -r tcpdump.pcap`，抓包分析设备推流消息。</li>

</div>








