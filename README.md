# JetLinks 开源物联网平台

JetLinks 基于Java8,Spring Boot 2.x,WebFlux,Netty,Vert.x,Reactor等开发, 
是一个开箱即用,可二次开发的企业级物联网基础平台。平台实现了物联网相关的众多基础功能,
能帮助你快速建立物联网相关业务系统。

点击添加官方QQ:①群(已满)[2021514](https://qm.qq.com/cgi-bin/qm/qr?k=LGf0OPQqvLGdJIZST3VTcypdVWhdfAOG&jump_from=webapi)
,②群[324606263](https://qm.qq.com/cgi-bin/qm/qr?k=IMas2cH-TNsYxUcY8lRbsXqPnA2sGHYQ&jump_from=webapi)

## 核心特性

**开放源代码**

全部源代码开放,可自由二次开发.前后端分离,接口全开放.

**统一设备接入,海量设备管理**

TCP/UDP/MQTT/HTTP、TLS/DLS、不同厂商、不同设备、不同报文、统一接入，统一管理.

**强大的规则引擎**

强大的可视化规则设计器.以及多种规则模型支持(设备告警,场景联动等).
![rule-engine.png](./rule-engine.png)

**可视化大屏**

可视化大屏设计器,灵活配置大屏展示。
![big-screen.png](./big-screen.png)

**多租户**

灵活的非侵入[多租户](./dev-guide/multi-tenant.md)数据权限控制。可实现不同租户,不同用户共享数据。

在线演示地址: [http://demo.jetlinks.cn](http://demo.jetlinks.cn) 用户名:`test` 密码: `test123456`.

## 技术栈

1. [Spring Boot 2.3.x](https://spring.io/projects/spring-boot)
2. [Spring WebFlux](https://spring.io/) 响应式Web支持
3. [R2DBC](https://r2dbc.io/) 响应式关系型数据库驱动
4. [Project Reactor](https://projectreactor.io/) 响应式编程框架
4. [Netty](https://netty.io/),[Vert.x](https://vertx.io/) 高性能网络编程框架
5. [ElasticSearch](https://www.elastic.co/cn/products/enterprise-search) 全文检索，日志，时序数据存储
6. [Redis](https://redis.io/) Redis,设备配置,状态管理,缓存.
7. [PostgreSQL](https://www.postgresql.org) 业务功能数据管理
8. [hsweb framework 4](https://github.com/hs-web) 业务功能基础框架

## 架构

![platform](./platform.svg)


## 设备接入流程

![flow](./best-practices/device-flow.svg)

## 许可版本

JetLinks所有版本均开放源代码.
::: tip 注意
JetLinks使用模块化(`git submodule`+`maven`)管理,部分核心模块是单独的仓库并定期发布到maven中央仓库.
可在[github](https://github.com/jetlinks)中找到全部代码.
:::

| 功能                         | 社区版 | 企业版                    |
| ---------------------------- | ------ |  ------------------------- |
| 开放源代码                   | ✅      | ✅                         |
| 设备管理,设备接入            | ✅      | ✅                         |
| 多消息协议支持               | ✅      |  ✅                         |
| 规则引擎-设备告警            | ✅      |  ✅                         |
| 规则引擎-数据转发            | ✅      |  ✅                         |
| 系统监控,数据统计            | ✅      |  ✅                         |
| 邮件消息通知                 | ✅      |  ✅                         |
| 微信企业消息                 | ✅      | ✅                         |
| 钉钉消息通知                 | ✅      | ✅                         |
| MQTT(TLS)                    | ✅      |  ✅                         |
| TCP(TLS)                     | ✅      | ✅                         |
| UDP,CoAP(DTLS)                   | ⭕      |  ✅                         |
| Http,WebSocket(TLS)          | ⭕      |  ✅                         |
| 转发设备数据到RabbitMQ,Kafka | ⭕      |  ✅                         |
| Geo地理位置支持              | ⭕      |  ✅                         |
| 规则引擎-可视化设计器        | ⭕      |  ✅                         |
| OpenAPI,OAuth2认证           | ⭕      | ✅                         |
| 多租户逻辑数据隔离           | ⭕      |  ✅                         |
| 集群支持                     | ⭕      |  ✅                         |
| QQ群技术支持                 | ⭕      |  ✅                         |
| 一对一技术支持               | ⭕      | ✅                         |
| 微服务架构                   | ⭕      |  ✅                         |
| 可视化大屏设计器             | ⭕      | ✅                         |
| 定制开发                     | ⭕      |  ✅                         |
| 阿里云协议适配               | ⭕      |  ✅ (付费选配模块)          |
| 阿里云平台接入               | ⭕      |  ✅ (付费选配模块)          |
| 小度平台接入                 | ⭕      |  ✅ (付费选配模块)          |
| 电信CTWing平台接入           | ⭕      |  ✅ (付费选配模块)          |
| 移动OneNet平台接入           | ⭕      |  ✅ (付费选配模块)          |
| GBT/28181视频设备接入        | ⭕      |  ✅ (付费选配模块)          |
| OPC UA                     | ⭕      |  ✅ (付费选配模块)          |
| 商业限制                    | 仅用于自有项目<br>禁止售卖源代码.  | <span style='color:green;font-weight:800'>不限项目数量</span> |
| 定价                         | 免费   |  加QQ群`2021514`联系商务     |
