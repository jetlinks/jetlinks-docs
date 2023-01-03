# 实体变更后触发自己的业务流程

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 Spring Event 自定义事件链，实用性很强的一种设计，可以应用于业务剥离，复杂场景解耦、代码独立等，是事件驱动模型的核心，并且可以处理1对多，点对点，发布订阅的场景
</div>

## 指导介绍

 <p>1.<a href="/dev-guide/jetlinks-event-listener.html#通用crud事件">通用CRUD事件</a></p>
 <p>2.<a href="/dev-guide/jetlinks-event-listener.html#授权相关事件">授权相关事件</a></p>
 <p>3.<a href="/dev-guide/jetlinks-event-listener.html#用户管理相关">用户管理相关</a></p>
 <p>4.<a href="/dev-guide/jetlinks-event-listener.html#数据权限相关-企业版">数据权限相关(企业版)</a></p>
 <p>5.<a href="/dev-guide/jetlinks-event-listener.html#设备管理相关">设备管理相关</a></p>


<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   Spring Event不支持响应式,平台封装了响应式事件抽象类，
可实现接口AsyncEven`或者继承DefaultAsyncEvent来处理 响应式操作。
监听响应式事件时需要使用event.async( doSomeThing(event))来注册响应式操作.
例如：
</div>

```java
@EventListener
public void handleEvent(EntitySavedEvent<DeviceInstanceEntity>  event){

        event.async(this.sendNotify(event.getEntity()));
        }

public Void this.sendNotify(List<DeviceInstanceEntity> entities){
        //相关业务场景处理
        doSomeThing();
        }
```

## 核心接口说明

核心接口org.hswebframework.web.event.AsyncEvent

| 方法名 | 返回值 | 参数值 | 说明  |
|------- |--------|----------|------------|
|`publish(ApplicationEventPublisher eventPublisher)` | `Mono<Void>` | `ApplicationEventPublisher eventPublisher`|通知所有与此注册的匹配的侦听器 |

## 通用CRUD事件

<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    在实体类上注解@EnableEntityEvent以开启对应实体类的事件,表示开启实体操作事件，当实体类发生类修改，更新，删除等操作时，会触发事件，可以通过spring event的@EventListener注解监听事件
</div>

```java
EntityPrepareCreateEvent 实体类创建预处理事件,可在这个阶段修改实体类属性值,对应操作insert
        EntityPrepareModifyEvent 实体类修改预处理事件,可在这个阶段修改实体类属性值,对应操作update
        EntityPrepareSaveEvent 实体类修改预处理事件,可在这个阶段修改实体类属性值,对应操作save
        EntityBeforeCreateEvent 实体类创建前事件,可用于校验参数等操作,对应操作insert
        EntityBeforeDeleteEvent 实体类删除前事件,可用于校验是否能删除等操作,对应操作delete
        EntityBeforeModifyEvent 实体类修改事件,可用于校验参数等操作,对应操作update
        EntityBeforeQueryEvent 实体类查询前事件,可用于自定义查询条件,对应操作query
        EntityBeforeSaveEvent 实体类保存前事件,可用于校验参数等操作,对应操作save
        EntitySavedEvent 实体类保存事件,可用于记录日志等操作,对应操作save
        EntityModifyEvent 实体类修改事件,可用于记录日志等操作,对应操作update
        EntityCreatedEvent 实体类创建事件,可用于记录日志等操作,对应操作insert
        EntityDeletedEvent 实体类删除事件,可用于记录日志等操作,对应操作delete
```

### EntityCreatedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 EntityCreatedEvent 实体类创建事件,可用于记录日志等操作,对应操作insert
</div>

#### EntityCreatedEvent的事件监听：

```
org.jetlinks.pro.notify.manager.service.NotifySubscriberService.handleEvent(EntityCreatedEvent<NotifySubscriberEntity> entity)
```

### EntitySavedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 EntitySavedEvent 实体类保存事件,可用于记录日志等操作,对应操作save
</div>

#### EntitySavedEvent的事件监听：

```
org.jetlinks.pro.notify.manager.service.NotifySubscriberService.handleEvent(EntitySavedEvent<NotifySubscriberEntity> entity)
```

### EntityDeletedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
EntityDeletedEvent 实体类删除事件,可用于记录日志等操作,对应操作delete
</div>

#### EntityDeletedEvent的事件监听：

```
org.jetlinks.pro.notify.manager.service.NotifySubscriberService.handleEvent(EntityDeletedEvent<NotifySubscriberEntity> entity)
```

### EntityModifyEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
EntityModifyEvent 实体类修改事件,可用于记录日志等操作,对应操作update
</div>

#### EntityModifyEvent的事件监听：

```
org.jetlinks.pro.notify.manager.service.NotifySubscriberService.handleEvent(EntityModifyEvent<NotifySubscriberEntity> entity)
```

## 授权相关事件

<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  使用接口/authorize/login进行登录时，将会触发相应的实现来实现自定义授权逻辑，如自定义验证码，密码加解密等。 相关类:AuthorizationController
</div>

```java
AuthorizationDecodeEvent 认证解密事件，可用于自定义用户名密码加密解密
        AuthorizationBeforeEvent 认证前触发，可用于校验其他参数，比如验证码
        AuthorizationSuccessEvent 认证通过时触发，可用于认证通过后，自定义一些信息给前端返回
        AuthorizationFailedEvent 认证失败时触发，可用于自定义失败时的处理逻辑

```

### AuthorizationDecodeEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
AuthorizationDecodeEvent 认证解密事件，可用于自定义用户名密码加密解密
</div>

#### AuthorizationDecodeEvent的事件发布：

```
org.hswebframework.web.authorization.basic.web.AuthorizationController.doLogin(Mono<Map<String, Object>> parameter)
```

#### AuthorizationDecodeEvent的事件监听：

```
org.jetlinks.pro.auth.captcha.CaptchaController.handleAuthEvent(AuthorizationDecodeEvent event)
```

### AuthorizationBeforeEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
AuthorizationBeforeEvent 认证前触发，可用于校验其他参数，比如验证码
</div>

#### AuthorizationBeforeEvent的事件发布：

```
org.hswebframework.web.authorization.basic.web.AuthorizationController.doLogin(Mono<Map<String, Object>> parameter)
```

#### AuthorizationBeforeEvent的事件监听：

```java
平台暂无实现，用户可以使用@EventListener注解自定义实现监听
```

### AuthorizationSuccessEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
AuthorizationSuccessEvent 认证通过时触发，可用于认证通过后，自定义一些信息给前端返回
</div>

#### AuthorizationSuccessEvent的事件发布：

```
org.hswebframework.web.authorization.basic.web.AuthorizationController.doLogin(Mono<Map<String, Object>> parameter)
```

#### AuthorizationSuccessEvent的事件监听：

```
org.jetlinks.pro.standalone.authorize.LoginEvent.handleLoginSuccess
```

### AuthorizationFailedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
AuthorizationFailedEvent 认证失败时触发，可用于自定义失败时的处理逻辑
</div>

#### AuthorizationFailedEvent的事件发布:

```
org.hswebframework.web.authorization.basic.web.AuthorizationController.doLogin(Mono<Map<String, Object>>
```

#### AuthorizationFailedEvent的事件监听：

```java
平台暂无实现，用户可以使用@EventListener注解自定义实现监听
```

## 用户管理相关

<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  使用接口/authorize/login进行登录时，将会触发相应的实现来实现自定义授权逻辑，如自定义验证码，密码加解密等。 相关类:AuthorizationController
</div>

```java
UserCreatedEvent 用户创建事件
        UserDeletedEvent 用户删除事件
        UserModifiedEvent 用户修改事件
        UserStateChangedEvent 用户状态变更事件
        ClearUserAuthorizationCacheEvent 清空用户权限缓存信息事件,可用发送此事件来清理用户权限缓存
```

### UserCreatedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
UserCreatedEvent 用户创建事件
</div>

#### UserCreatedEvent的事件发布：

```
org.hswebframework.web.system.authorization.defaults.service.DefaultReactiveUserService.doAdd(UserEntity userEntity)
```

#### UserCreatedEvent的事件监听：

```
平台暂无实现，用户可以使用@EventListener注解自定义实现监听
```

### UserDeletedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
UserDeletedEvent 用户删除事件
</div>

#### UserDeletedEvent的事件发布：

```
org.hswebframework.web.system.authorization.defaults.service.DefaultReactiveUserService.deleteUser(String userId)

```

#### UserDeletedEvent的事件监听：

```
org.hswebframework.web.system.authorization.defaults.service.DefaultDimensionUserService.handleUserDeleteEntity(UserDeletedEvent event)
```

### UserModifiedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
UserModifiedEvent 用户修改事件
</div>

#### UserModifiedEvent的事件发布：

```
org.hswebframework.web.system.authorization.defaults.service.DefaultReactiveUserService.doUpdate(UserEntity old, UserEntity newer)

```

#### UserModifiedEvent的事件监听：

```
org.hswebframework.web.system.authorization.defaults.service.RemoveUserTokenWhenUserDisabled.handleStateChangeEvent(UserModifiedEvent event)
```

### ClearUserAuthorizationCacheEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
ClearUserAuthorizationCacheEvent 清空用户权限缓存信息事件,可用发送此事件来清理用户权限缓存
</div>

#### ClearUserAuthorizationCacheEvent的事件发布：

```
org.hswebframework.web.system.authorization.defaults.service.DefaultDimensionUserService.clearUserCache(List<DimensionUserEntity> entities)
```

#### ClearUserAuthorizationCacheEvent的事件监听：

```
org.hswebframework.web.system.authorization.defaults.service.DefaultReactiveAuthenticationManager.handleClearAuthCache(ClearUserAuthorizationCacheEvent event)
```

## 数据权限相关(企业版)

```java
AssetsBindEvent 资产绑定事件
        AssetsUnBindEvent 资产解绑事件
        AssetsUnBindAllEvent 全部资产解绑事件
        TenantMemberBindEvent 租户成员绑定事件
        TenantMemberUnBindEvent 租户成员解绑事件
```

### AssetsBindEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
AssetsBindEvent 资产绑定事件
</div>

#### AssetsBindEvent的事件发布：

```
`org.jetlinks.pro.assets.CompositeAssetBindManager.bindAssets(@Nonnull Publisher<AssetBindRequest> requestFlux)
```

#### AssetsBindEvent的事件监听：

```
org.jetlinks.pro.device.service.DeviceTenantSynchronizer.handleUnBindEvent(AssetsBindEvent event)
```

### AssetsUnBindEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
AssetsUnBindEvent 资产解绑事件
</div>

#### AssetsUnBindEvent的事件发布：

```
org.jetlinks.pro.assets.CompositeAssetBindManager.unbindAssets(@Nonnull Publisher<AssetUnbindRequest> requestFlux)
```

#### AssetsUnBindEvent的事件监听：

```
org.jetlinks.pro.device.service.DeviceTenantSynchronizer.handleUnBindEvent(AssetsUnBindEvent event)
```

### AssetsUnBindAllEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
AssetsUnBindAllEvent 全部资产解绑事件
</div>

#### AssetsUnBindAllEvent的事件发布：

```
org.jetlinks.pro.assets.CompositeAssetBindManager.unbindAllAssets(@Nonnull String assetType,@Nonnull Collection<?> assetId)
```

#### AssetsUnBindAllEvent的事件监听：

```
org.jetlinks.pro.device.service.DeviceTenantSynchronizer.handleUnBindAllEvent(AssetsUnBindAllEvent event)
```

### TenantMemberBindEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
TenantMemberBindEvent 租户成员绑定事件
</div>

#### TenantMemberBindEvent的事件发布：

```
org.jetlinks.pro.auth.service.TenantMemberService.bindMembers(String tenantId, Flux<BindMemberRequest> bind)
```

#### TenantMemberBindEvent的事件监听：

```java
平台暂无实现,用户可以使用@EventListener注解自定义实现监听
```

### TenantMemberUnBindEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
TenantMemberUnBindEvent 租户成员解绑事件
</div>

#### TenantMemberUnBindEvent的事件发布：

```
org.jetlinks.pro.auth.service.TenantMemberService.unbindMembers(String tenantId, Flux<String> bindIdStream)
```

#### TenantMemberUnBindEvent的事件监听:

```java
平台暂无实现，用户可以使用@EventListener注解自定义实现监听
```

## 设备管理相关

```java
DeviceDeployedEvent:设备激活时触发
        DeviceUnregisterEvent:设备注销时触发
        DeviceAutoRegisterEvent:设备自动注册时触发,可返回是否允许自动注册
        DeviceProductDeployEvent:产品激活时触发
```

### DeviceDeployedEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
DeviceDeployedEvent: 设备激活时触发
</div>

#### DeviceDeployedEvent的事件发布：

```
org.jetlinks.pro.device.service.LocalDeviceInstanceService.deploy(Flux<DeviceInstanceEntity> flux,
                                       Function<? super Throwable, ? extends Mono<DeviceInstanceEntity>> fallback)
```

#### DeviceDeployedEvent的事件监听：

```
org.jetlinks.pro.device.service.DeviceGroupService.handleDeviceDeploy(DeviceDeployedEvent event)
```

### DeviceUnregisterEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
DeviceUnregisterEvent: 设备注销时触发
</div>

#### DeviceUnregisterEvent的事件发布：

```
org.jetlinks.pro.device.service.LocalDeviceInstanceService.unregisterDevice(Publisher<String> ids)
```

#### DeviceUnregisterEvent的事件监听：

```java
平台暂无实现，用户可以使用@EventListener注解自定义实现监听
```

### DeviceAutoRegisterEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
DeviceAutoRegisterEvent: 设备自动注册时触发,可返回是否允许自动注册
</div>

#### DeviceAutoRegisterEvent的事件发布：

```
org.jetlinks.pro.device.service.DeviceMessageBusinessHandler.doAutoRegister(DeviceRegisterMessage message)
```

#### DeviceAutoRegisterEvent的事件监听：

```java
平台暂无实现，用户可以使用@EventListener注解自定义实现监听
```

### DeviceProductDeployEvent
<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
DeviceProductDeployEvent: 产品激活时触发
</div>

#### DeviceProductDeployEvent的事件发布：

```
org.jetlinks.pro.device.service.LocalDeviceProductService.deploy(String id)
```

#### DeviceProductDeployEvent的事件监听：

```
org.jetlinks.pro.device.events.handler.DeviceProductDeployHandler.handlerEvent(DeviceProductDeployEvent event)
```
