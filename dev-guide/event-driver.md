# 事件驱动

在JetLinks中大量使用到事件驱动来实现功能解耦,主要由`Spring Event`和`事件总线(EventBus)`组成.

## Spring Event

直接使用`spring-framework`的事件模型,通过`ApplicationEventPublisher`来发送事件，在方法上注解`@EventListener`来监听事件.

::: tip 注意
由于`Spring Event`不支持响应式,平台封装了响应式事件抽象类，可实现接口`AsyncEvent`或者继承`DefaultAsyncEvent`来处理
响应式操作。监听响应式事件时需要使用`event.async( doSomeThing(event) )`来注册响应式操作. 例如：

```java
@EventListener
public void handleEvent(EntitySavedEvent<DeviceInstanceEntity>  event){

event.async( this.sendNotify(event.getEntity()) );
  
}

public Mono<Void> this.sendNotify(List<DeviceInstanceEntity> entities){

  return ....;
}
```
:::

### 通用CRUD事件

在实体类上注解`@EnableEntityEvent`以开启对应实体类的事件,或者通过:实现接口`EntityEventListenerCustomizer`并注入到`spring`,
来实现实体类事件自定义.
对应的事件类如下：

+ `EntityPrepareCreateEvent` 实体类创建预处理事件,可在这个阶段修改实体类属性值,对应操作`insert`
+ `EntityPrepareModifyEvent` 实体类修改预处理事件,可在这个阶段修改实体类属性值,对应操作`update`
+ `EntityPrepareSaveEvent` 实体类修改预处理事件,可在这个阶段修改实体类属性值,对应操作`save`
+ `EntityBeforeCreateEvent` 实体类创建前事件,可用于校验参数等操作,对应操作`insert`
+ `EntityBeforeDeleteEvent` 实体类删除前事件,可用于校验是否能删除等操作,对应操作`delete`
+ `EntityBeforeModifyEvent` 实体类修改事件,可用于校验参数等操作,对应操作`update`
+ `EntityBeforeQueryEvent` 实体类查询前事件,可用于自定义查询条件,对应操作`query`
+ `EntityBeforeSaveEvent` 实体类保存前事件,可用于校验参数等操作,对应操作`save`
+ `EntitySavedEvent` 实体类保存事件,可用于记录日志等操作,对应操作`save`
+ `EntityModifyEvent` 实体类修改事件,可用于记录日志等操作,对应操作`update`
+ `EntityCreatedEvent` 实体类创建事件,可用于记录日志等操作,对应操作`insert`
+ `EntityDeletedEvent` 实体类删除事件,可用于记录日志等操作,对应操作`delete`

::: tip 注意
事件类的泛型为想要监听的实体类,例如: `EntitySavedEvent<DeviceInstanceEntity>`.
如果想要全部实体,则直接监听`EntitySavedEvent`即可.
:::

### 授权相关事件

使用接口`/authorize/login`进行登录时，将会触发相应的实现来实现自定义授权逻辑，如自定义验证码，密码加解密等。
相关类:`AuthorizationController`

+ `AuthorizationDecodeEvent` 认证解密事件，可用于自定义用户名密码加密解密
+ `AuthorizationBeforeEvent` 认证前触发，可用于校验其他参数，比如验证码
+ `AuthorizationSuccessEvent` 认证通过时触发，可用于认证通过后，自定义一些信息给前端返回
+ `AuthorizationFailedEvent` 认证失败时触发，可用于自定义失败时的处理逻辑

### 用户管理相关

+ `UserCreatedEvent` 用户创建事件
+ `UserDeletedEvent` 用户删除事件
+ `UserModifiedEvent` 用户修改事件
+ `UserStateChangedEvent` 用户状态变更事件
+ `ClearUserAuthorizationCacheEvent` 清空用户权限缓存信息事件,可用发送此事件来清理用户权限缓存

### 数据权限相关(企业版)

+ `AssetsBindEvent` 资产绑定事件
+ `AssetsUnBindEvent` 资产解绑事件
+ `AssetsUnBindAllEvent` 全部资产解绑事件
+ `TenantMemberBindEvent` 租户成员绑定事件
+ `TenantMemberUnBindEvent` 租户成员解绑事件

::: tip 注意
资产绑定方式分为手动绑定和自动绑定, 自动绑定是在创建数据,删除数据等操作时执行的.
手动绑定通常是通过接口进行资产分配时执行的.可以通过以下方式来区分绑定方式:

+ `AssetsUtils.doWhenSourceIsAutoBind`
+ `AssetsUtils.doWhenSourceIsManualBind`

```java

@EventListener
public void handleEvent(AssetsBindEvent event){
  event.async(
    AssetsUtils
      .doWhenSourceIsAutoBind(doSomeThing(event.getAssetId()),true)
  )
}

```
:::

### 设备管理相关

+ `DeviceDeployedEvent`: 设备激活时触发
+ `DeviceUnregisterEvent`: 设备注销时触发
+ `DeviceAutoRegisterEvent`: 设备自动注册时触发,可返回是否允许自动注册
+ `DeviceProductDeployEvent`: 产品激活时触发

## EventBus

由于`Spting Event`不支持更细粒度的订阅,例如: 订阅某一个设备的消息。平台还提供了事件总线来实现粒度更细的事件支持.

### Topic

采用树结构来定义topic如:`/device/id/message/type` .
topic支持路径通配符,如:`/device/**` 或者`/device/*/message/*`.

::: tip
通配符`**`表示匹配多层路径,`*`表示匹配单层路径. `不支持`前后匹配,如: `/device/id-*/message`.
发布和订阅均支持通配符,发布时使用通配符时则进行广播.
:::

### 使用

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

### 设备消息
 
 [点击查看](../best-practices/start.md#设备消息对应事件总线topic)

### 设备告警

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