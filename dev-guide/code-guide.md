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

- 用户管理
- 组织管理
- 角色管理
- 菜单管理
- 权限管理
- 关系配置
- 数据源管理
- API配置
- 应用管理

### 设备管理

- 创建产品
- 创建设备
- 物模型
- 设备功能
- 设备诊断
- 在线解析
- 网关设备和网关子设备

### 数据采集

- 数据采集
- 综合查询

### 规则引擎

- 规则编排
- 场景联动
- 数据流转
- 数据流转方案对比
- 设置数据流转规则
- reactorQL表达式
- 数据格式

### 告警中心

- 基础配置
- 告警配置
- 告警记录

### 运维管理

- 设备接入网关
- 协议管理
- 日志管理
- 网络组件
- 证书管理
- 流媒体服务
- 远程升级

### 北向输出

- DuerOS
- 阿里云

### 通知管理

- 通知配置
- 通知模板

### 视频中心

- 视频设备
- 分屏展示
- 国标级联

### 数据流转

- 数据上报服务器
- <a target='_self' href='/dev-guide/push-to-message-middleware.html'>
   设备数据推送到消息中间件</a>
- <a target='_self' href='/dev-guide/mqtt-subs.html'>
   订阅平台相关消息</a>
- 调用平台历史数据接口

## 开发

### 平台开发

- <a target='_self' href='/dev-guide/request-jetlinks-interface.html'>
   调用API</a>
- 应用集成
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

[//]: # ()

[//]: # (## 系统压力测试)

[//]: # ()

[//]: # (- 压测场景)

[//]: # (- 压测脚本)

[//]: # (- 模拟并发)