# 从事件总线中订阅消息

```java

//订阅所有设备上线消息
@Subscribe("/device/*/*/online")
public Mono<Void> handleDeviceOnline(DeviceOnlineMessage message){
    //处理消息
    return doSomeThing();
}

```

## 设备消息

### topic
所有设备消息的`topic`的前缀均为: `/device/{productId}/{deviceId}`.
如:设备`device-1`上线消息: `/device/product-1/device-1/online`.
可通过通配符订阅所有设备的指定消息,如:`/device/*/*/online`,
或者订阅所有消息:`/device/**`.

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

使用通配符订阅可能将收到大量的消息,请保证消息的处理速度,否则会影响系统消息吞吐量.

</div>

### 设备Topic列表

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

列表中的topic已省略前缀`/device/{productId}/{deviceId}`,使用时请加上.

</div>

| topic                                              | 类型                       | 说明                                      |
| -------------------------------------------------- | -------------------------- | ----------------------------------------- |
| /online                                            | DeviceOnlineMessage        | 设备上线                                  |
| /offline                                           | DeviceOfflineMessage       | 设备离线                                  |
| /message/event/{eventId}                           | DeviceEventMessage         | 设备事件                                  |
| /message/property/report                           | ReportPropertyMessage      | 设备上报属性                              |
| /message/property/read/reply                       | ReadPropertyMessageReply   | 读取属性回复                              |
| /message/property/write/reply                      | WritePropertyMessageReply  | 修改属性回复                              |
| /message/function/reply                            | FunctionInvokeMessageReply | 调用功能回复                              |
| /register                                          | DeviceRegisterMessage      | 设备注册,通常与子设备消息配合使用         |
| /unregister                                        | DeviceUnRegisterMessage    | 设备注销,同上                             |
| /message/children/{childrenDeviceId}/{topic}       | ChildDeviceMessage         | 子设备消息,{topic}为子设备消息对应的topic |
| /message/children/reply/{childrenDeviceId}/{topic} | ChildDeviceMessage         | 子设备回复消息,同上                       |

## 设备告警

在配置了设备告警规则,设备发生告警时,会发送消息到消息网关.

告警topic格式：`/alarm/{targetType}/{targetId}/{alarmId}/record`

| 占位符标识     | 名称       | 数据值说明         |
| ---------------- | ---- | ------------ |
| targetType     | 告警目标类型        | 支持`device（设备）`、`product（产品）`、`org（组织）`、`other（其它）`四种类型。   |
| targetId     | 告警目标id       | 分别为告警目标类型的数据id。其中`其它` 类型的告警目标id为场景联动id |
| alarmId     | 告警配置id       | 告警配置id |


**告警内容**

| 字段     | 名称       | 类型         |
| ---------------- | ---- | ------------ |
| id     | string        |  告警数据ID  |
| alarmConfigId     | string       | 告警配置id|
| alarmConfigName     | string       | 告警配置名称 |
| alarmRecordId     | string       | 告警记录ID |
| level     | int       | 告警级别。支持 |
| alarmTime     | long       | 告警时间 |
| description     | string       | 说明 |
| targetType     | string       | 告警目标类型 |
| targetName     | string       | 告警目标名称 |
| targetId     | string       | 告警目标id |
| sourceType     | string       | 告警源类型。由场景触发的告警，则源类型为场景（scene） |
| sourceId     | string       | 告警源Id。由场景触发的告警，则源ID为场景Id |
| sourceName     | string       | 告警源名称。由场景触发的告警，则源名称为场景名称 |
| bindings     | list       | 其他从规则中获取到到信息。如告警配置人的用户信息 |


## 日志

### 系统日志

topic格式: `/logging/system/{logger名称,.替换为/}/{level}`.

```js
`/logging/system/org/jetlinks/pro/TestService/{level}`

{
"name":"org.jetlinks.pro.TestService", //logger名称
"threadName":"线程名称",
"level":"日志级别",
"className":"产生日志的类名",
"methodName":"产生日志的方法名",
"lineNumber"：32,//行号
"message":"日志内容",
"exceptionStack":"异常栈信息",
"createTime":"日志时间",
"context":{} //上下文信息
}

```