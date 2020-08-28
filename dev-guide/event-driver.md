# 事件驱动

在jetlinks中大量使用到事件驱动,使用`事件总线(EventBus)`基于`topic`来进行数据传递.

## Topic

采用树结构来定义topic如:`/device/id/message/type` .
topic支持路径通配符,如:`/device/**` 或者`/device/*/message/*`.

::: tip
通配符`**`表示匹配多层路径,`*`表示匹配单层路径. `不支持`前后匹配,如: `/device/id-*/message`.
发布和订阅均支持通配符,发布时使用通配符时则进行广播.
:::

## 使用

订阅消息:

```java

@Subscribe("/user/*/saved")
public Mono<Void> handleDeviceMessage(UserEntity entity){
    return publishDeviecMessageToKafka(message);
}

```

发布消息:

```java

@Autowired
private EventBus eventBus;

public Mono<Void> saveUser(UserEntity entity){
    return service.saveUser(entity)
            .then(eventBus.publish("/user/"+entity.getId()+"/saved",entity))
            .then();
}

```

## topic列表

### 设备消息

所有设备消息的`topic`的前缀均为: `/device/{productId}/{deviceId}`.
如:设备`device-1`上线消息: `/device/product-1/device-1/online`.
可通过通配符订阅所有设备的指定消息,如:`/device/*/*/online`,
或者订阅所有消息:`/device/**`.

::: tip
使用通配符订阅可能将收到大量的消息,请保证消息的处理速度,否则会影响系统消息吞吐量.
:::


|  topic   | 类型  | 说明 |
|  ----  | ----  | ----|
| /online  | DeviceOnlineMessage | 设备上线   |
| /offline  | DeviceOfflineMessage |  设备离线  |
| /message/event/{eventId}  | DeviceEventMessage |  设备事件  |
| /message/property/report  | ReportPropertyMessage |  设备上报属性  |
| /message/property/read/reply  | ReadPropertyMessageReply |  读取属性回复  |
| /message/property/write/reply  | WritePropertyMessageReply |  修改属性回复  |
| /message/function/reply  | FunctionInvokeMessageReply |  调用功能回复  |
| /register  | DeviceRegisterMessage |  设备注册,通常与子设备消息配合使用  |
| /unregister  | DeviceUnRegisterMessage |  设备注销,同上  |
| /message/children/{childrenDeviceId}/{topic}  | ChildDeviceMessage |  子设备消息,{topic}为子设备消息对应的topic  |
| /message/children/reply/{childrenDeviceId}/{topic}  | ChildDeviceMessage |  子设备回复消息,同上  |
 
::: warning
列表中的topic已省略前缀`/device/{productId}/{deviceId}`,使用时请加上.
:::

## 设备告警

在配置了设备告警规则,设备发生告警时,会发送消息到消息总线.

```js
`/rule-engine/device/alarm/{productId}/{deviceId}/{ruleId}`

{
  "productId":"",
  "alarmId":"",
  "alarmName":"",
  "deviceId":"",
  "deviceName":"",
  "...":"其他从规则中获取到到信息"
}
```

## 系统日志

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