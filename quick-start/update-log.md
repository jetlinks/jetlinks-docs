# 更新记录

最新代码托管在[github](https://github.com/jetlinks/jetlinks-community)和
[gitee](https://gitee.com/jetlinks/jetlinks-community)上,master为最新开发分支.

## 1.0-RELEASE

更新时间: 2020-04-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.0-RELEASE)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.0-RELEASE)

主要优化:

1. 重构`elasticsearch-component`模块.
2. 优化`elasticsearch`索引管理,支持自定义策略,如按月对索引分表.
3. 增加`GeoType`类型支持,为未来的`Gis`支持作准备.
4. 增加网关设备功能.见:[通过网关设备接入多个下挂设备](../best-practices/device-gateway-connection.md)
5. 增加属性变更`Dashboard`接口,可同时订阅一个设备对所有属性变更事件.
6. 优化设备协议,增加调试功能. 增加脚本支持(PRO)
7. 增加设备功能调用,可在界面发起功能调用指令.
8. 优化消息网关中的设备消息topic,详见: [从消息网关中订阅设备消息](/dev-guide/subscribe-device-message.md)
9. 消息网关增加集群支持,在任意一个服务节点都可订阅设备消息.(PRO)
10. 增加UDP设备网关,可通过UDP接入设备了(PRO).
11. 增加CoAP设备网关,可通过CoAP接入设备了(PRO).
12. 设备会话支持手动指定心跳超时时间以及获取客户端地址信息.
13. 性能优化,详见[压力测试](../advancement-guide/benchmark.md).
14. 一系列UI优化.

主要BUG修复:

1. 修复使用save保存的数据中有字段使用默认值时,更新数据会导致字段被更新为默认值的问题.
2. 修复选择网络协议时出现重复选项.
3. 修复设备属性和事件历史记录部分类型无法展示问题.
4. 修复无法将excel大文件导入设备实例的的问题.
5. 修复在某些情况下redis超时后的NPE错误.
6. 修复无法导出设备问题.
7. 修复当设备数量较多时同步设备状态可能导致崩溃的问题.

## 1.0-RC

更新时间: 2020-03-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.0-RC)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.0-RC)

首个预览版,实现基本功能
