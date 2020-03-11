# 更新记录

最新代码托管在[github](https://github.com/jetlinks/jetlinks-community)和
[gitee](https://gitee.com/jetlinks/jetlinks-community)上,master为最新开发分支.

## 1.0-RELEASE

更新时间: 2020-04-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.0-RELEASE)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.0-RELEASE)

主要优化:

1. 重构elasticsearch-component模块.
2. 优化elasticsearch索引管理,支持自定义策略,如按月对索引分表.
3. 增加GeoType类型支持.
4. 增加父子设备支持.
5. 增加属性变更Dashboard接口,可同时订阅一个设备对所有属性变更事件.
6. 优化设备协议,增加调试功能.
7. 增加设备功能调用,可在界面发起功能调用指令.
8. 消息网关增加集群支持,在任意一个服务节点都可订阅设备消息.(PRO).
9. 增加UDP设备网关,可通过UDP接入设备了(PRO).

主要BUG修复:

1. 修复使用save保存的数据中有字段使用默认值时,更新数据会导致字段被更新为默认值的问题.
2. 修复选择网络协议时出现重复选项.
3. UI修复设备属性和事件历史记录部分类型无法展示问题.

## 1.0-RC

更新时间: 2020-03-01

[后端代码](https://github.com/jetlinks/jetlinks-community/tree/1.0-RC)
[前端代码](https://github.com/jetlinks/jetlinks-ui-antd/tree/1.0-RC)

首个预览版,实现基本功能
