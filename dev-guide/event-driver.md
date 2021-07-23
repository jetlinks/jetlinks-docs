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

@Subscribe("/device/**")
public Mono<Void> handleDeviceMessage(DeviceMessage message){
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
 
 [点击查看](../best-practices/start.md#设备消息对应事件总线topic)

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