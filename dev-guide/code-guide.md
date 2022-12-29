# 平台使用指导手册

<div class='explanation primary'>
<p class='explanation-title-warp'>
  <span class='iconfont icon-bangzhu explanation-icon'></span>
  <span class='explanation-title font-weight'>概述</span>
</p>
  <p>本文档主要是用于指导用户如何下载及更新代码、部署过程中遇到的问题汇总以及常见的开发问题汇总。</p>
  <p>首次使用本平台时，介绍如何如何拉取代码、更新子模块以及部署问题等基本操作。</p>
  <p>对于已经拉取源码的用户介绍如何在已有的代码基础上构建自己的业务系统，或者使用平台内部提供的类、接口等。</p>
  <p>对于不满足接入场景的部分介绍如何自行实现该接入方式。</p>
</div>


## 获取源码

1. 拉取源码
   在拉取JetLinks源码之前，您需要先注册一个Github账号。请参见<a href="https://github.com/signup?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home">
   注册Github账号</a>。
2. 将注册的Github账号提供给JetLinks官方人员
   由官方人员将您邀请加入<a href="https://github.com/jetlinks-v2">JetLinks-V2仓库</a>
3. 配置SSH Key并Clone源码，具体操作，请参见<a target='_blank' href='/dev-guide/pull-code.html'>拉取源码及更新子模块</a>
4. 部署源码， 请参见 <a target='_blank' href="/install-deployment/start-with-source.html">本地源码启动</a>
5. 开始使用JetLinks

- <a target='_self' href='/dev-guide/pull-code.html'>
   如何拉取源码及更新子模块？</a>
- <a target='_self' href='/dev-guide/ui-deploy.html'>
   前端部署及常见问题</a>
- <a target='_self' href='/dev-guide/middleware-deploy.html'>
   中间件部署及常见问题</a>
- <a target='_self' href='/dev-guide/java-deploy.html'>
   后端部署及常见问题</a>

## 设备接入
  - MQTT
  - TCP
  - HTTP
  - UDP
  - CoAP
  - Websocket
  - 网关子设备接入
  - 电信OneNet
  - 移动ctwing
  - 阿里云
  - 小度
  - 国标28181/2016接入
  - 固定地址接入
  - OPC UA接入
  - Modbus/TCP接入

## 基本开发问题

- <a target='_self' href='/dev-guide/custom-code-guide.html'>
   在JetLinks上构建自己的业务模块？</a>
- <a target='_self' href='/dev-guide/jetlinks-event-listener.html'>
   实体变更后如何触发自己的业务流程？</a>
- <a target='_self' href='/dev-guide/subscribe-device-message.html'>
   如何使用消息总线？</a>
- <a target='_self' href='/dev-guide/custom-storage-strategy.html'>
  如何添加自定义存储策略？</a>
- <a target='_self' href='/dev-guide/rule-engine.html'>
  如何添加自定义规则引擎节点？</a>
- <a target='_self' href='/dev-guide/diy-term-builder.html'>
  自定义SQL条件构造器</a>
- 透传协议
- 数据权限控制？
- 数据隔离如何操作？
- 如何横向扩展集群节点？
- 国际化
- 文件管理

## 数据流转

- <a target='_self' href='/dev-guide/push-to-message-middleware.html'>
   设备数据推送到消息中间件</a>
- <a target='_self' href='/dev-guide/mqtt-subs.html'>
   订阅平台相关消息</a>
- 调用平台历史数据接口

## 对接第三方系统

- <a target='_self' href='/dev-guide/request-jetlinks-interface.html'>
   第三方平台请求JetLinks服务接口</a>
- 在JetLinks内集成第三方系统
- 在第三方系统内集成JetLinks
- 单点登录

## 协议开发问题

- <a target='_self' href='/dev-guide/protocol-redis.html'>
  如何在协议包里面使用Redis？</a>
- <a target='_self' href='/dev-guide/jetlinks-protocol-use-business-method.html'>
  如何在协议包里面使用平台的业务方法？</a>
- <a target='_self' href='/dev-guide/poll-device-data.html'>
   主动从设备获取属性、事件如何操作？</a>
- <a target='_self' href='/dev-guide/sort-link.html'>
   短连接、低功耗类设备接入平台 </a>

## 系统压力测试
- 压测场景
- 压测脚本
- 模拟并发