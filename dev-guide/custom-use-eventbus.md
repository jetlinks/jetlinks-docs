### 自定义模块如何引入消息总线

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
   EventBus是一个基于发布者/订阅者模式的事件总线框架。发布者/订阅者模式，也就是观察者模式，其定义了对象之间的一种一对多的依赖关系
</div>

1.在`web`的`org.example.mydemo.web.CustomController`新建方法

```java
     /**
     * 新增数据，新增成功后通过事件总线发布事件消息，向es新增一条记录
     * @param customEntity
     * @return
     */
    @PostMapping("/eventBus/publish")
    public Mono<Integer> eventBuspublish(@RequestBody CustomEntity customEntity) {
        return customService.insert(customEntity)
                .filter(num->num>0)
                .flatMap(num->{
                   return eventBus
                            .publish("/demo/eventBus/insert", customEntity)
                            .thenReturn(num);
                });
    }

```

2.在自定义的`mydemo`下，新建包`org.example.mydemo.event.customEventHandler`，新建`CustomEventHandler`类

```java
package org.example.mydemo.event.customEventHandler;

import org.example.mydemo.enums.CustomIndexEnum;
import org.example.mydemo.service.CustomService;
import org.jetlinks.community.elastic.search.service.ElasticSearchService;
import org.jetlinks.core.event.EventBus;
import org.jetlinks.core.event.Subscription;
import org.springframework.context.event.EventListener;

@AllArgsConstructor
@Getter
public class CustomEventHandler {
    //引入消息总线
    private EventBus eventBus;
    //引入es
    private ElasticSearchService elasticSearchService;

    public void doSubscribe() {
        eventBus.subscribe(Subscription.builder()
                //订阅者标识
                .subscriberId("custom-device-query-event")
                .topics("/demo/eventBus/insert")
                //订阅者有三大特性
                .local().build()).flatMap(payload -> {
            //业务操作  往es里面存入数据
            return elasticSearchService
                    .save(CustomIndexEnum.custom.getIndex(), payload.getPayload());
        }).subscribe();
    }
```

##### 如何引入设备操作
##### 如何使用redis缓存
#### 在使用过程中写响应式应注意什么
#### 扩展点：
##### 比如我要在自己的项目内使用es查询设备历史数据，在自己的项目内使用es聚合查询给出示例代码