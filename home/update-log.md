# 更新记录&开发计划

企业版代码托管在[github](https://github.com/jetlinks-v2)上,购买企业版后可获取企业版代码以及后续更新。

前端代码统一托管在[github](https://github.com/jetlinks/jetlinks-ui-antd)。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

master为最新开发分支. 线上使用请根据情况切换到对应版本的分支.


</div>

当前最新稳定版本`2.0`,对应代码分支`master`.

## 2.0-RC

代码分支: `2.0`

1. 在`1.20`版本基础上.
2. 全新的前端UI.
3. 增加[菜单管理](/System_settings/Basic_configuration13.html#菜单管理),角色按菜单赋权,角色增加数据权限设置.
4. 全新的设备接入流程.

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>设备接入说明</span>
  </p>

传输协议、网络组件和协议包的配置整合到[设备接入网关](/Mocha_ITOM/Device_access_gateway5.1.html#设备接入网关)。
产品只需要[选择设备接入网关](/device_management/product4.1.html#设备接入)。设备接入网关支持显示协议详情（路由信息、markdown文档等）.

</div>

5. 增加端口资源管理.

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>端口资源说明</span>
  </p>

新增配置: `network.resources`,配置可用网络资源,用于在添加网络组件等常见下进行端口资源选择:
```yml
network:
  resources:
     - 0.0.0.0:8080-8082/tcp
     - 127.0.0.1:8080-8082/udp
```

</div>

6. 增加物关系功能,支持物与物建立关系并通过关系来动态选择设备进行相关操作.(Pro)
7. 重构系统,网络组件监控,支持集群.新增设备管理、运维管理、告警中心、视频中心的仪表盘页面。
8. 重构[场景联动](/Rule_engine/Rule%20engine9.html#场景联动)功能，添加属性读取触发，添加内置参数，添加触发条件配置。场景联动支持将串行执行动作的返回值作为下一个动作的参数。
9. 重构[消息通知](/Notification_management/Notification_management7.html)，通知内容增加变量功能；通知方式新增Webhook；钉钉、微信通知配置新增同步用户功能.

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>通知变量说明</span>
  </p>

通知配置调试时可选择模板，自动解析内置变量和模板中的变量，显示对应的输入框。变量也可使用场景联动的内置参数输入。

</div>

10. 取消设备告警功能,由新的[告警中心](/Alarm_Center/Alarm_configuration6.1.html#告警配置)替代.
11. 增加透传消息解析功能,协议包中标记支持透传消息,在界面上通过脚本来处理透传消息为平台的消息. [协议例子](https://github.com/jetlinks/transparent-protocol)
12. 重构脚本引擎,使用新的脚本API:`Scripts`.增加安全性控制(默认禁止访问java类)以及循环执行控制(防止死循环)(Pro).
13. 平台所有脚本语言支持更换为`js`.
14. 优化集群通信性能,增加`FluxCluster`支持(集群下对`Flux`进行窗口计算等).(Pro)
15. 增加持久化缓冲工具,数据持久化到本地.用于批量写库,失败重试等操作,减少写入速度不够时的内存占用.(Pro)
16. 增加断路器`CircuitBreaker`功能,减少由于配置错误或者数据变化导致一些动态逻辑大量错误引起系统崩溃的可能性.(Pro)
17. 新增系统初始化配置功能.
18. 新增API开放范围可视化配置.
19. 新增第三方登录功能.
20. 优化[基础配置](/System_settings/Basic_configuration13.html#基础配置)。添加背景图、页签、base-path和高德地图API Key的设置.

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>更新说明</span>
  </p>

此版本与`1.x`版本不兼容.

</div>