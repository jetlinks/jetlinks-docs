## 代码仓库说明

## 概述
本文档主要是列出平台涉及的单体版和微服务版的代码仓库地址以及平台模块说明和项目的目录结构信息。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>分支说明</span>
  </p>

以下除JetLinks前端界面仓库外。所有仓库的最新代码分支均为`master`分支。

前端界面分支为`2.0`

</div>

### IOT平台模块说明


| 模块名称                        | 仓库名称 | 说明                    |
| ---------------------------- | ------ |  ------------------------- |
| 阿里云平台接入                   | <a href="https://github.com/jetlinks-v2/jetlinks-aliyun-bridge-gateway">jetlinks-aliyun-bridge-gateway</a>      | 将平台的设备接入到阿里云，实现设备操控                       |
| 电信CTWing平台接入            | <a href="https://github.com/jetlinks-v2/jetlinks-ctwing">jetlinks-ctwing</a>     | 接入CTWing平台的设备到本平台                         |
| 小度平台接入               | <a href="https://github.com/jetlinks-v2/jetlinks-dueros">jetlinks-dueros</a>      |  使用小度音响控制平台的设备                         |
| 移动OneNet平台接入              | <a href="https://github.com/jetlinks-v2/jetlinks-onenet">jetlinks-onenet</a>      |  接入OneNet平台的设备到本平台                         |
| GBT/28181视频设备接入       | <a href="https://github.com/jetlinks-v2/jetlinks-media">jetlinks-media</a>      |   使用GBT/28181接入视频设备，实现直播、录像、云台控制等                        |
| Modbus/TCP           | <a href="https://github.com/jetlinks-v2/jetlinks-modbus">jetlinks-modbus</a>      |     支持Modbus/TCP协议数采                      |
| OPC UA            | <a href="https://github.com/jetlinks-v2/jetlinks-opc-ua">jetlinks-opc-ua</a>      | 支持OPC UA协议数采                        |
| 组件库                   | <a href="https://github.com/jetlinks-v2/jetlinks-components">jetlinks-components</a>      | 组件库,容纳JetLinks众多组件功能                   |
| api组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/api-component">api-component</a>      | 提供第三方平台访问API基础支持                       |
| 应用组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/application-component">application-component</a>      | 提供应用管理功能基础支持                       |
| 资产组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/assets-component">assets-component</a>      | 提供基于各个维度的资产管理支持                       |
| cassandra组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/cassandra-component">cassandra-component</a>      | cassandra时序数据库集成                       |
| clickhouse组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/clickhouse-component">clickhouse-component</a>      | clickhouse时序数据库集成                       |
| 数据采集器组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/collector-component">collector-component</a>      | 提供opc ua、modbus等数采支持                        |
| 通用组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/common-component">common-component</a>      | 提供基础功能类、文件buffer、全局常量等通用支持                       |
| 配置组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/configure-componen">configure-component</a>      | 项目配置支持                       |
| 仪表盘            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/dashboard-component">dashboard-component</a>      | 支持展示各统计维度数据仪表盘                        |
| 数据源组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/datasource-component">datasource-component</a>      | 提供关系型数据库、rabbitMQ等数据源基础支持                      |
| elasticsearch组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/elasticsearch-component">elasticsearch-component</a>      | elasticsearch集成                       |
| 函数组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/function-component">function-component</a>      | 云函数模块                     |
| 网关组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/gateway-component">gateway-component</a>      | 网关组件,消息网关,设备接入                        |
| 地理位置组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/geo-component">geo-component</a>      | 集成地理位置支持                        |
| influxdb组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/influxdb-component">influxdb-component</a>      | influxdb集成                       |
| IO 组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/io-component">io-component</a>      | IO 组件,Excel导入导出等                        |
| 日志组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/logging-component">logging-component</a>      | 日志组件                        |
| 消息中间件组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/messaging-component">messaging-component</a>      | 消息中间件组件,RabbitMQ,Kafka等                      |
| 网络组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/network-component">network-component</a>      | 网络组件,MQTT,TCP,CoAP,UDP等                        |
| 通知组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/notify-component">notify-component</a>      | 通知组件,短信,邮件等通知                        |
| 协议组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/protocol-component">protocol-component </a>     | 提供消息协议处理支持                        |
| 规则引擎            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/rule-engine-component">rule-engine-component </a>     | 提供规则引擎计算支持                       |
| 脚本组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/script-component">script-component</a>      | 脚本组件,封装脚本引擎                        |
| 流式计算            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/streaming-component">streaming-component</a>      | 流式计算(暂未实现)                       |
| tdengine组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/tdengine-component">tdengine-component</a>      | tdengine集成                        |
| 时序数据组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/timeseries-component">timeseries-component</a>      | 时序数据组件                        |
| 单元测试组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/test-component">test-component</a>      | 单元测试组件，引入后可管理快速对其它组件进行unit test                        |
| 物管理组件            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/things-component">things-component </a>     | 提供设备与设备、设备与物、物与物之间关联支持                        |
| 管理功能模块            | <a href="https://github.com/jetlinks-v2/jetlinks-components/tree/master/">jetlinks-manager</a>      | 管理功能                        |
| 用户,权限管理            | <a href="https://github.com/jetlinks-v2/authentication-manager">authentication-manager</a>      | 用户,权限管理                        |
| 数据源管理            | <a href="https://github.com/jetlinks-v2/datasource-manager">datasource-manager</a>      | 数据源管理模块                        |
| 设备管理            | <a href="https://github.com/jetlinks-v2/device-manager">device-manager</a>      | 设备管理模块。包含设备消息分发、设备数据上下行等功能                       |
| 日志管理            | <a href="https://github.com/jetlinks-v2/logging-manager">logging-manager</a>      | 日志管理，访问日志、系统日志                        |
| 网络组件管理            | <a href="https://github.com/jetlinks-v2/network-manager">network-manager</a>      | 提供基于MQTT、TCP、UDP等各传输协议的网络组件管理                        |
| 通知管理            | <a href="https://github.com/jetlinks-v2/notify-manager">notify-manager</a>      | 通知管理 ，包含邮件、短信等                       |
| 规则引擎管理            | <a href="https://github.com/jetlinks-v2/rule-engine-manager">rule-engine-manager</a>      | 规则引擎管理                        |
| 物管理            | <a href="https://github.com/jetlinks-v2/things-manager">things-manager</a>      | 提供设备与设备、设备与物、物与物之间关联管理                        |
| 父模块            | <a href="https://github.com/jetlinks-v2/jetlinks-pro/tree/master/jetlinks-parentr">jetlinks-parentr</a>      | 父模块,统一依赖管理                        |
| 启动模块            | <a href="https://github.com/jetlinks-v2/jetlinks-pro/tree/master/jetlinks-standalone">jetlinks-standalone</a>      | 项目启动模块                        |
| 测试报告模块            | <a href="https://github.com/jetlinks-v2/jetlinks-pro/tree/master/test-report">test-report</a>      | 可用于测试报告的生成                        |
| 前端界面            | <a href="https://github.com/jetlinks/jetlinks-ui-antd">jetlinks-ui-antd</a>      | 前端界面仓库                        |

### IOT平台微服务版模块说明


|模块名称|仓库名称|说明|
|:-----  |:-----|-----                           |
|微服务版  |<a href="https://github.com/jetlinks-v2/jetlinks-cloud">jetlinks-cloud</a>   |JetLinks 微服务版  |
|API网关服务 |<a href="https://github.com/jetlinks-v2/jetlinks-cloud/tree/master/micro-services/api-gateway-service">api-gateway-service</a>   |独立启动  |
|用户认证服务 |<a href="https://github.com/jetlinks-v2/jetlinks-cloud/tree/master/micro-services/authentication-service">authentication-service</a>   |支持转换器、基础管理和基础数据源  |
|文件服务 |<a href="https://github.com/jetlinks-v2/jetlinks-cloud/tree/master/micro-services/file-service">file-service</a>   |api、websocket、外部iot数据源支持  |
|接入服务 |<a href="https://github.com/jetlinks-v2/jetlinks-cloud/tree/master/micro-services/file-service">iot-service</a>   |物联网设备接入服务  |
|通用模块配置 |<a href="https://github.com/jetlinks-v2/jetlinks-cloud/tree/master/micro-services/service-components">service-components</a>   |微服务服务通用模块配置  |
|服务统一依赖 |<a href="https://github.com/jetlinks-v2/jetlinks-cloud/tree/master/micro-services/service-dependencies">service-dependencies</a>   |微服务服务统一依赖  |

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

只有 `api-gateway-service`、`authentication-service`、`file-service`、`iot-service`  四个模块为微服务可启动单元。

</div>


### 可视化模块说明


|模块名称|仓库名称|说明|
|:-----  |:-----|-----                           |
|前端 |<a href="https://github.com/jetlinks-v2/view-ui">view-ui</a>   |前端界面  |
|启动服务 |<a href="https://github.com/jetlinks-v2/view-standalone">view-standalone</a>   |独立启动  |
|基础功能 |<a href="https://github.com/jetlinks-v2/view-basic">view-basic</a>   |支持转换器、基础管理和基础数据源  |
|外部接口服务 |<a href="https://github.com/jetlinks-v2/view-api">view-api</a>   |api、websocket、外部iot数据源支持  |
|外部鉴权服务 |<a href="https://github.com/jetlinks-v2/view-auth">view-auth</a>   |鉴权组件  |
|数据库服务 |<a href="https://github.com/jetlinks-v2/view-database">view-database</a>   |数据库数据源支持  |
|报表 |<a href="https://github.com/jetlinks-v2/view-report">view-report</a>   |支持报表数据解析  |

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


### IOT项目代码结构


```bash
---jetlinks-pro
------|---expands-components   # 扩展模块.
------|-------|----jetlinks-aliyun-bridge-gateway # 阿里云IoT平台接入.
------|-------|----jetlinks-ctwing # 电信Ctwing物联网平台对接.
------|-------|----jetlinks-dueros # 小度智能家居开放平台集成.
------|-------|----jetlinks-edge # JetLinks 边缘网关.
------|-------|----jetlinks-media # 音视频流媒体管理模块,实现`GBT 28181`相关协议功能.
------|-------|----jetlinks-modbus # modbus/tcp支持.
------|-------|----jetlinks-onenet # 移动OneNet平台对接.
------|-------|----jetlinks-opc-ua # opc ua支持.
------|---jetlinks-components   # 组件库.
------|-------|----api-component # 对API鉴权,swagger等集成.
------|-------|----application-component # 应用管理，SSO、API Server、API Client等.
------|-------|----assets-component # 资产控制组件
------|-------|----cassandra-component # cassandra集成.
------|-------|----clickhouse-component # clickhouse集成.
------|-------|----common-component # 通用组件.
------|-------|----collector-component # 数据采集器组件.
------|-------|----dashboard-component # 仪表盘.
------|-------|----datasource-component # 数据源组件.
------|-------|----elasticsearch-component # elasticsearch集成.
------|-------|----function-component # 函数模块.
------|-------|----gateway-component # 网关组件,消息网关,设备接入.
------|-------|----geo-component # 地理位置组件
------|-------|----influxdb-component # influxdb集成
------|-------|----io-component # IO 组件,Excel导入导出等.
------|-------|----logging-component # 日志组件
------|-------|----messaging-component # 消息中间件组件,RabbitMQ,Kafka等
------|-------|----network-component # 网络组件,MQTT,TCP,CoAP,UDP等
------|-------|----notify-component # 通知组件,短信,邮件等通知
------|-------|----protocol-component # 协议组件
------|-------|----rule-engine-component # 规则引擎
------|-------|----script-component # 脚本组件,封装脚本引擎
------|-------|----streaming-component # 流式计算(暂未实现)
------|-------|----tdengine-component # tdengine集成.
------|-------|----timeseries-component # 时序数据组件
------|-------|----test-component # 单元测试组件
------|-------|----things-component # 物管理
------|-------|----timeseries-component # 时序模块
------|---jetlinks-manager  # 管理功能
------|-------|----authentication-manager   # 用户,权限管理
------|-------|----datasource-manager   # 数据源管理模块
------|-------|----device-manager   # 设备管理
------|-------|----logging-manager   # 日志管理
------|-------|----network-manager   # 网络组件管理
------|-------|----notify-manager   # 通知管理
------|-------|----visualization-manager   # 数据可视化管理
------|-------|----rule-engine-manager   # 规则引擎管理
------|-------|----things-manager   # 物管理
------|---jetlinks-parent   # 父模块,统一依赖管理
------|---jetlinks-standalone   # 项目启动模块
------|---simulator     # 模拟器

```


### IOT平台微服务代码结构

```bash
---jetlinks-cloud
-----|--- ****                            # 目录结构和jetlinks-pro相同
-----|--- micro-services                  # 微服务模块
-----|-------|---- api-gateway-service    # API网关服务
-----|-------|---- authentication-service # 用户认证服务
-----|-------|---- file-service           # 文件服务
-----|-------|---- iot-service            # 物联网设备接入服务
-----|-------|---- service-components     # 服务通用模块配置
-----|-------|---- service-dependencies   # 服务统一依赖
-----|-------|---- visualization-service  # 可视化服务


```


### 可视化项目代码结构

```bash
------jetlinks-view
------|------jetlinks-manager  # 管理功能
------|------|------authentication-manager   # 用户,权限管理
------|------|------logging-manager   # 日志管理
------|------|------datasource-manager   # 数据库配置管理
------|------jetlinks-parent   # 父模块,统一依赖管理
------|------jetlinks-view   # 可视化相关.
------|------|------view-api # api、websocket、外部iot数据源支持.
------|------|------view-auth # 鉴权组件.
------|------|------view-database # 数据库数据源支持.
------|------|------view-basic # 可视化基础功能.
------|------|------|------view-converter # 转换器组件.
------|------|------|------view-manager #可视化基础管理模块 .
------|------|------|------view-datasource # 可视化基础数据源.
```