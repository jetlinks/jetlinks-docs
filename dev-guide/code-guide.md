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

### 公共实例入门教程
- 创建产品和设备
- 设备接入和上报数据
- 数据存储时序数据库
- 平台下发指令
- 为产品定义物模型
- 建立设备与平台的连接
- 设备接收平台指令

## 使用

### 系统管理
- <a target='_self' href='/System_settings/System_user_management.html'>
  用户管理</a>
- <a target='_self' href='/System_settings/System_org_management.html'>
  组织管理</a>
- <a target='_self' href='/System_settings/System_role_management.html'>
  角色管理</a>
- <a target='_self' href='/System_settings/System_menu_management.html'>
  菜单管理</a>
- <a target='_self' href='/System_settings/System_authentication_management.html'>
  权限管理</a>
- <a target='_self' href='/System_settings/System_relation_configuration.html'>
  关系配置</a>
- <a target='_self' href='/System_settings/System_datasource_management.html'>
  数据源管理</a>
- <a target='_self' href='/System_settings/System_api_configuration.html'>
  API配置</a>
- <a target='_self' href='/System_settings/System_application_management.html'>
  应用管理</a>

### 设备管理

- <a target='_self' href='/Device_access/Create_product3.1.html'>
  创建产品</a>
- <a target='_self' href='/Device_access/Create_Device3.2.html'>
  创建设备</a>
- <a target='_self' href='/device_management/product4.1_thing_model.html'>
  物模型</a>
- <a target='_self' href='/device_management/product4.1_device_function.html'>
   设备功能</a>
- <a target='_self' href='/device_management/product4.1_device_diagnose.html'>
   设备诊断</a>
- 在线解析
- <a target='_self' href='/Device_access/Create_gateways_and_sub_devices3.3.html'>
  网关设备和网关子设备</a>

### 数据采集

- 数据采集
- 综合查询

### 规则引擎

- <a target='_self' href='/dev-guide/rule-editor.html'>
  规则编排</a>
- 场景联动
- <a target='_self' href='/Alarm_Center/data-flow.html'>
  数据流转</a>
- 数据流转方案对比
- 设置数据流转规则
- reactorQL表达式
- 数据格式

### 告警中心
- <a target='_self' href='/Alarm_Center/Alarm_base_configuration.html'>
  基础配置</a>
- <a target='_self' href='/Alarm_Center/Alarm_configuration.html'>
  告警配置</a>
- <a target='_self' href='/Alarm_Center/Alarm_records.html'>
  告警记录</a>

### 运维管理
- <a target='_self' href='/Mocha_ITOM/Device_access_gateway.html'>
  设备接入网关</a>
- <a target='_self' href='/Mocha_ITOM/protocol_management.html'>
  协议管理</a>
- <a target='_self' href='/Mocha_ITOM/log_management.html'>
  日志管理</a>
- <a target='_self' href='/Mocha_ITOM/network_components.html'>
  网络组件</a>
- <a target='_self' href='/Mocha_ITOM/certificate_management.html'>
  证书管理</a>
- <a target='_self' href='/Mocha_ITOM/streaming_media_service.html'>
  流媒体服务</a>
- <a target='_self' href='/Mocha_ITOM/remote_upgrade.html'>
  远程升级</a>

### 北向输出

- <a target='_self' href='/Northbound_output/Northbound_output8_DuerOS.html'>
  DuerOS</a>
- <a target='_self' href='/Northbound_output/Northbound_output8_aliyun.html'>
  阿里云</a>

### 通知管理

- 通知配置
- 通知模板

### 视频中心

- <a target='_self' href='/Video_Center/Video_equipment10_device.html'>
  视频设备</a>
- <a target='_self' href='/Video_Center/Split_screen.html'>
  分屏展示</a>
- <a target='_self' href='/Video_Center/National_standard_cascade.html'>
  国标级联</a>

### 数据流转

- 数据上报服务器
- <a target='_self' href='/dev-guide/push-to-message-middleware.html'>
   设备数据推送到消息中间件</a>
- <a target='_self' href='/dev-guide/subs-platform-message.html'>
   订阅平台相关消息</a>
- 调用平台历史数据接口

## 开发

### 平台开发

- <a target='_self' href='/dev-guide/request-jetlinks-interface.html'>
   调用API</a>
- <a target='_self' href='/dev-guide/application-integration.html'>
   应用集成</a>
- 单点登录
- API列表

### 协议开发

- 单协议包支持多通信协议
- 自定义Topic通信
- <a target='_self' href='/dev-guide/protocol-redis.html'>
  如何在协议包里面使用Redis？</a>
- <a target='_self' href='/dev-guide/jetlinks-protocol-use-business-method.html'>
  如何在协议包里面使用平台的业务方法？</a>
- <a target='_self' href='/dev-guide/poll-device-data.html'>
   主动从设备获取属性、事件如何操作？</a>
- <a target='_self' href='/dev-guide/sort-link.html'>
   短连接、低功耗类设备接入平台 </a>
- IoT设备身份认证
- 设备真实状态检测

### 常见问题

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
- 自定义接入网关？
- 数据权限控制？
- 数据隔离如何操作？
- 如何横向扩展集群节点？
- 国际化
- 文件管理
- 证书如何生成？
- 日志输出级别

## 数据流转

- <a target='_self' href='/dev-guide/push-to-message-middleware.html'>
   设备数据推送到消息中间件</a>
- <a target='_self' href='/dev-guide/subs-platform-message.html'>
   订阅平台相关消息</a>
- 调用平台历史数据接口

[//]: # (## 系统压力测试)

[//]: # ()

[//]: # (- 压测场景)

[//]: # (- 压测脚本)

[//]: # (- 模拟并发)