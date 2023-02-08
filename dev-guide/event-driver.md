# 事件驱动

在JetLinks中大量使用到事件驱动来实现功能解耦。主要由`Spring Event`和`事件总线(EventBus)`组成。

## 文档指引

<table>
  <tr>
    <td><a href="/dev-guide/event-driver.html#spring-event">Spring Event</a></td>
    <td><a href="/dev-guide/event-driver.html#eventbus">EventBus</a></td>
  </tr>
  <tr>
    <td><a target="_blank" href="/dev-guide/event-driver.html#spring-event">使用Spring Event解耦逻辑</a></td>
    <td><a target="_blank" href="/dev-guide/custom-use-eventbus.html">使用EventBus解耦逻辑</a></td>
  </tr>
</table>

## Spring Event

直接使用`spring-framework`的事件模型，通过`ApplicationEventPublisher`来发送事件，在方法上注解`@EventListener`来监听事件。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

由于`Spring Event`不支持响应式，平台封装了响应式事件抽象类。可实现接口`AsyncEvent`或者继承`DefaultAsyncEvent`来处理
响应式操作。监听响应式事件时需要使用`event.async( doSomeThing(event) )`来注册响应式操作。例如：

```java
@EventListener
public void handleEvent(EntitySavedEvent<DeviceInstanceEntity>  event){

event.async( this.sendNotify(event.getEntity()) );
  
}

public Mono<Void> this.sendNotify(List<DeviceInstanceEntity> entities){

  return ....;
}
```

</div>

### 通用CRUD事件

在实体类上注解`@EnableEntityEvent`以开启对应实体类的事件。或者通过实现接口`EntityEventListenerCustomizer`并注入到`spring`,
来实现实体类事件自定义。
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

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

事件类的泛型为想要监听的实体类。例如: `EntitySavedEvent<DeviceInstanceEntity>`。
如果想要全部实体，则直接监听`EntitySavedEvent`即可。

</div>

### 授权相关事件

使用接口`/authorize/login`进行登录时，将会触发相应的实现来实现自定义授权逻辑。如自定义验证码，密码加解密等。
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

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  资产绑定方式分为手动绑定和自动绑定, 自动绑定是在创建数据、删除数据等操作时执行的。
  手动绑定通常是通过接口进行资产分配时执行的，可以通过以下方式来区分绑定方式。

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

</div>

### 设备管理相关

+ `DeviceDeployedEvent`: 设备激活时触发
+ `DeviceUnregisterEvent`: 设备注销时触发
+ `DeviceAutoRegisterEvent`: 设备自动注册时触发,可返回是否允许自动注册
+ `DeviceProductDeployEvent`: 产品激活时触发

## EventBus

由于`Spting Event`不支持更细粒度的订阅。例如: 订阅某一个设备的消息。平台还提供了事件总线来实现粒度更细的事件支持。

### Topic

采用树结构来定义topic，如：`/device/id/message/type`。
topic支持路径通配符，如：`/device/**` 或者`/device/*/message/*`。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

通配符`**`表示匹配多层路径，`*`表示匹配单层路径。`不支持`前后匹配，如: `/device/id-*/message`。
发布和订阅均支持通配符，发布时使用通配符时则进行广播。

</div>

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

### 设备消息及平台其他消息
 
设备消息参照<a target="_blank" href='/function-description/device_message_description.html#设备消息对应事件总线topic'>设备消息对应事件总线topic</a>，
其他消息参照<a target="_blank" href='/dev-guide/subs-platform-message.html#平台内部主题'>平台内部主题</a>。



### 设备告警

在配置了设备告警规则，设备发生告警时，会发送消息到消息总线。

topic格式：`/alarm/{targetType}/{targetId}/{alarmId}`

示例：`/alarm/product/1614931310716686336/1614934749509095424`

订阅正文格式:
```json
{
    "alarmConfigId": "1614934749509095424",//告警配置id
    "alarmConfigName": "设备告警",//告警配置名
    //告警具体信息
    "alarmInfo": "{\"code\":\"TIME_OUT\",\"branch_1_group_1_action_1\":{\"headers\":{\"errorType\":\"org.jetlinks.core.exception.DeviceOperationException\",\"errorMessage\":\"error.code.time_out\"},\"functionId\":\"alarm\",\"code\":\"TIME_OUT\",\"messageType\":\"INVOKE_FUNCTION_REPLY\",\"success\":false,\"messageId\":\"1619208766628511745\",\"message\":\"error.code.time_out\",\"deviceId\":\"1614934616587407360\",\"timestamp\":1674884438088},\"deviceId\":\"1614934616587407360\",\"scene\":{\"sourceId\":\"1614931520184422400\",\"data\":{\"temperature\":48.1},\"data_temperature\":48.1,\"deviceId\":\"1614931520184422400\",\"deviceName\":\"场景联动设备\",\"productName\":\"场景联动产品\",\"timestamp\":1674884428063,\"productId\":\"1614931310716686336\",\"sourceType\":\"device\",\"traceparent\":\"00-84a84b8e2dcff1098906b8849ca74c19-bf77534ce630b8f8-01\",\"_now\":1674884428064,\"sourceName\":\"场景联动设备\",\"_uid\":\"b4X25DEfVXuZTyDq-k6RKnXMltNpf4Rh\"},\"functionId\":\"alarm\",\"messageType\":\"INVOKE_FUNCTION_REPLY\",\"timestamp\":1674884438088,\"headers\":{\"errorType\":\"org.jetlinks.core.exception.DeviceOperationException\",\"errorMessage\":\"error.code.time_out\"},\"messageId\":\"1619208766628511745\",\"message\":\"error.code.time_out\",\"success\":false}",
    "alarmRecordId": "20f0c110fed8402e862cb7c15c992569",//告警记录id
    "alarmTime": 1674884438091,//告警发生时间
    "bindings": [
      {
        "id": "1199596756811550720",
        "type": "user"
      }
    ],
    "id": "b4X25FhTnJgqofbv0RxD4uLJcvvbS0v3",
    "level": 1,//告警等级
    "sourceId": "1614931520184422400",//设备id
    "sourceName": "场景联动设备",//设备名
    "sourceType": "device",//源类型，此处为设备
    "targetId": "1614931310716686336",//目标id:产品id
    "targetName": "场景联动产品",//产品名
    "targetType": "product"//目标类型:产品
}
```

### 系统日志

topic格式：`/logging/system/{logger名称,.替换为/}/{level}`。

示例：`/logging/system/org/jetlinks/pro/TestService/{level}`

订阅正文格式:
```json


{
  "name": "org.jetlinks.pro.TestService",
  "threadName": "线程名称",  //logger名称
  "level": "日志级别",
  "className": "产生日志的类名",
  "methodName": "产生日志的方法名",
  "lineNumber": 32,         //行号
  "message": "日志内容",
  "exceptionStack": "异常栈信息",
  "createTime": "日志时间",
  "context": {}           //上下文信息

}

```