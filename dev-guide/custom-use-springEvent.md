# 自定义模块使用事件驱动

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
Spring事件监听，也叫事件驱动,是观察者模式的一种实现，主要应用场景是程序解耦
</div>

## 指导介绍


  <p>1. <a href="/dev-guide/custom-use-springEvent.html#在平台使用springevent">使用平台springEvent</a></p>




[//]: # (## 问题指引)

[//]: # ()
[//]: # (<table>)

[//]: # ()
[//]: # (  <tr>)

[//]: # (    <td>)

[//]: # (      <a href="/dev-guide/custom-use-eventbus.html#每次重启程序后-只能在首次发布并订阅-第二次不成功">每次重启程序后-只能在首次发布并订阅-第二次不成功</a>)

[//]: # (   </td>)

[//]: # ()
[//]: # (  </tr>)

[//]: # ()
[//]: # (</table>)


##  在平台使用springEvent
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 <p>本文档只举例说明如何在JetLinks平台使用springEvent，用户在使用时请按照实际情况进行代码的编写</p>
</div>
1.新建java类，继承平台的DefaultAsyncEvent类
   <div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 <p> 由于<code>Spring Event</code>不支持响应式，平台封装了响应式事件抽象类;</p>
  <p>可实现接口<code>AsyncEvent</code>或者继承<code>DefaultAsyncEvent</code>来处理 响应式操作。</p>
</div>

```java
@Getter
@Setter
@Builder
@ToString
public class CustomSpringEvent extends DefaultAsyncEvent {
    private String eventName;
    private String eventInfo;
}
```
2.在自定义模块的`web`层的`使用springEvent

在org.example.mydemo.web.CustomController`中引入ApplicationEventPublisher
```java
private final ApplicationEventPublisher eventPublisher;
```
编写使用springEvent示例代码

```java
 /**
 * 请求时，发送一个事件推送
 * @return
 */
@GetMapping("/springEvent/publish")
public Mono<Void> springEventPublish() {
        CustomSpringEvent.builder()
        .eventName("springEvent01")
        .eventInfo("自定义模块使用springEvent示例")
        .build()
        .publish(eventPublisher);
        return Mono.empty();
        }
```

在自定义模块的`jetlinks.custom.custommodel.event.CustomEventHandler`类中编写平台SpringEvent的监听代码
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 <p>由于Spring Event不支持响应式，平台封装了响应式事件抽象类</p>
 <p>监听响应式事件时需要使用<code>event.async(doSomeThing(event))</code>来注册响应式操作。</p>
</div>
```java
 @EventListener
   public void handleCustomSpringEvent(CustomSpringEvent event) {
       event.async( this.doSomething(event) );

   }

    private Publisher<?> doSomething(CustomSpringEvent event) {
        System.out.println(event);
        return Mono.empty();
    }
```








