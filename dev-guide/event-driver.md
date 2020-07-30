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