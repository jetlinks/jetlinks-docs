### 使用MQTT订阅平台相关消息

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
     可以使用MQTT来订阅设备,规则引擎,设备告警等相关消息
    </div>

配置文件新增：

```yaml
messaging:
  mqtt:
    enabled: true #开启mqtt支持
    port: 11883 # 端口
    host: 0.0.0.0 #绑定网卡
```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
1.6版本后支持分组订阅：同一个用户订阅相同的topic，只有其中一个订阅者收到消息，在topic前增加<code>$shared</code>即可，如： <code>$shared/device/+/+/#</code><br>
</div>

订阅设备消息：与消息网关中的设备topic一致，[查看topic列表](http://doc.jetlinks.cn/function-description/device_message_description.html#设备消息对应事件总线topic)。消息负载(`payload`)将与[设备消息类型 ](http://doc.jetlinks.cn/function-description/device_message_description.html#消息定义)一致。

<br>