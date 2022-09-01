# 快速入门
## 视频中心
视频中心支持GB/T28181和固定地址2种方式的视频接入。支持视频设备的接入、通道管理、视频播放和回放、云台控制、国标级联等功能。
  
视频中心关联的功能如下：
## 流媒体服务
可在`物联网-运维管理-流媒体服务`中配置。
  
用于接收视频设备的推流，缓存并播放视频，以及各种功能的回调等。
目前支持的服务商有<a target='_blank' href='https://github.com/ZLMediaKit/ZLMediaKit>ZLMedia</a>。  

可配置多个流媒体服务。安装和配置步骤见<a href='/media-guide/media_access_process.html#_1-部署zlmedia'>ZLMedia部署</a>和<a href='/Mocha_ITOM/Device_access_gateway5.1.html#流媒体服务'>流媒体服务配置</a>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

流媒体配置保存在数据表`media_server`中，在`org.jetlinks.pro.media.server.zlm.ZLMMediaServerProvider`中使用。详细参数定义见代码`org.jetlinks.pro.media.server.zlm.ZLMConfig`。

</div>


## 设备接入网关
可在`物联网-运维管理-设备接入网关`中新增配置，选择`视频类设备接入`类别中对应的网关类型。
  
用于为视频设备的接入统一提供网络接入相关配置。例如国标信令配置（SIP相关配置）。
  
视频的设备接入网关配置参考<a href='/media-guide/media_access_process.html#_3-在设备接入网关配置信令'>在设备接入网关配置信令</a>。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

自2.0版本后，为了便于扩展与管理，平台统一了产品接入设备的标准，所有产品都需要配置接入网关（包括视频设备、第三方云平台、通道类设备Modbus/Opc-ua等）。
视频设备的产品需要配置`设备接入网关`后才能正常启用。
  
所有类型的网关都继承了接口`org.jetlinks.pro.gateway.DeviceGateway`。

</div>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

视频设备可创建的网关类别有`GB/T 28181 国标视频接入`和`视频设备-固定地址`。对应`视频设备`新增界面中的接入方式（GB/T 28181和固定地址）。  

`视频设备-固定地址`接入方式没有公共配置，地址和通道信息在`视频设备`中的查看通道页面配置。

</div>

## 视频设备
用于管理视频设备和通道，播放和回放、云台控制。
  
视频设备操作参考<a href='/Video_Center/Video_equipment10.html#视频设备-2'>操作手册-视频设备</a>。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

自2.0版本后，摄像头设备的接入，需要先创建视频设备。国标信令配置不再是全局通用，而是绑定在设备的产品上。
  
通道仍是由摄像头自动注册。

</div>

视频播放通过调用内置视频协议包中的`物模型`定义的功能实现，向设备发送SIP指令。
功能定义见`org.jetlinks.pro.media.gb28181.GB28181Function`。

## 国标级联
支持将本地平台的设备通道信息推送到上级平台。

配置参照<a href='/Best_practices/National_standard_cascade.html'>国标级联配置示例</a>。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

由`org.jetlinks.pro.media.gb28181.cascade.CascadeGateway`实现功能。包括注册注销、点播指令、处理业务消息等。

</div>









