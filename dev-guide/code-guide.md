# 平台使用指导手册

<div class='explanation primary'>
<p class='explanation-title-warp'>
  <span class='iconfont icon-bangzhu explanation-icon'></span>
  <span class='explanation-title font-weight'>概述</span>
</p>
  <p>本文档主要是用于指导用户如何下载及更新代码、部署过程中遇到的问题汇总以及常见的开发问题汇总。 首次使用本平台时，介绍如何如何拉取代码、更新子模块以及部署问题等基本操作。 
对于已经拉取源码的用户介绍如何在已有的代码基础上构建自己的业务系统，或者使用平台内部提供的类、接口等。
对于不满足自身业务场景部分介绍如何自行实现业务逻辑。</p>
  <p>由浅入深，带你逐步了解JetLinks物联网平台！</p>
</div>

## 上手

### 获取源码及部署

- <a target='_self' href='/dev-guide/pull-code.html'>
   拉取源码</a>
- <a target='_self' href='/dev-guide/ui-deploy.html'>
   前端部署</a>
- <a target='_self' href='/dev-guide/middleware-deploy.html'>
   中间件部署</a>
- <a target='_self' href='/dev-guide/java-deploy.html'>
   后端部署</a>
- <a target='_self' href='/dev-guide/colony-deploy.html'>
   集群部署</a>

## 开发

### 基础信息导读
- <a target="" href="/dev-guide/start.html">JetLinks开发手册</a>
- <a target="" href="/dev-guide/specification.html">开发规范</a>
- <a target="" href="/dev-guide/reactor.html">响应式</a>
- <a target="" href="/dev-guide/package-structure.html">包结构及说明</a>
- <a target="" href="/dev-guide/config-info.html">配置文件说明</a>

### 平台开发
- <a target="" href="/dev-guide/crud.html">关系型数据库增删改查</a>
- <a target="" href="/dev-guide/CustomConditionsSqlOrUseSourceSql.html">
  自定义SQL条件或使用原生SQL语句</a>
- <a href="/dev-guide/event-driver.html">事件驱动及消息总线</a>
- <a href="/dev-guide/rule-engine.html">规则引擎说明</a>
- <a href="/dev-guide/commons-api.html">平台核心类及接口说明</a>
- 集群管理
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
- <a target='_self' href='/dev-guide/request-jetlinks-interface.html'>
   调用API</a>
- <a target='_self' href='/dev-guide/application-integration.html'>
   应用集成</a>
- <a target='_self' href='/Best_practices/application_management.html#单点登录'>
   单点登录</a>
- <a target='_self' href='/System_settings/System_api_configuration.html'>
   API列表</a>
- <a target='_self' href='/dev-guide/jetlinks-transparent.html#透传协议'>
   透传协议</a>
- 自定义接入网关？
- 数据权限控制？
- 数据隔离如何操作？
- 如何横向扩展集群节点？
- <a target='_self' href='/dev-guide/i18n.html#国际化'>
   国际化</a>
- <a target='_self' href='/dev-guide/file_manager.html'>
  文件管理</a>
- <a target='_self' href='/Mocha_ITOM/certificate_management.html#证书管理'>
  证书如何生成？</a>
- <a target='_self' href='/dev-guide/log-level.html'>
   日志输出级别</a>

### 协议开发
- <a target='_self' href='/protocol/first.html'>
  单协议包支持多通信协议</a>
- <a target='_self' href='/dev-guide/custom_topic_communication.html'>
  自定义Topic通信</a>
- <a target='_self' href='/dev-guide/protocol-redis.html'>
  如何在协议包里面使用Redis？</a>
- <a target='_self' href='/dev-guide/jetlinks-protocol-use-business-method.html'>
  如何在协议包里面使用平台的业务方法？</a>
- <a target='_self' href='/dev-guide/poll-device-data.html'>
   主动从设备获取属性、事件如何操作？</a>
- <a target='_self' href='/dev-guide/sort-link.html'>
   短连接、低功耗类设备接入平台 </a>
- <a target='_self' href='/dev-guide/IoT_device_identity_authentication.html'>
   设备身份认证</a>
- <a target='_self' href='/dev-guide/device_real_state_check.html'>
   设备真实状态检测</a>

## 数据流转

- <a target='_self' href='/dev-guide/push-to-message-middleware.html'>
   设备数据推送到消息中间件</a>
- <a target='_self' href='/dev-guide/subs-platform-message.html'>
   订阅平台相关消息</a>
- <a target='_self' href='/dev-guide/device-history-logs.html'>
   调用设备历史数据接口</a>






[//]: # (## 系统压力测试)

[//]: # ()

[//]: # (- 压测场景)

[//]: # (- 压测脚本)

[//]: # (- 模拟并发)