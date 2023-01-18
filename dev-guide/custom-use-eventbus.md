# 自定义模块如何引入消息总线

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
   EventBus是一个基于发布者/订阅者模式的事件总线框架。发布者/订阅者模式，也就是观察者模式，其定义了对象之间的一种一对多的依赖关系
</div>

## 指导介绍

  <p>1. <a href="/dev-guide/custom-use-eventbus.html#引入平台eventbus" >引入平台eventBus</a></p>
  <p>2. <a href="/dev-guide/custom-use-eventbus.html#使用平台eventbus" >使用平台eventBus</a></p>


## 问题指引
<table>
  <tr>
    <td>
      <a href="/dev-guide/custom-use-eventbus.html#每次重启程序后-只能在首次发布并订阅-第二次不成功">每次重启程序后-只能在首次发布并订阅-第二次不成功</a>
   </td>
  </tr>
</table>

##  引入平台eventBus

### 在`web`的`org.example.mydemo.web.CustomController`中引入eventBus

```java
    //引入消息总线
    @Qualifier("brokerEventBus")
    private final EventBus eventBus;
```

##  使用平台eventBus

### 1.在`web`的`org.example.mydemo.web.CustomController`中新建方法,使用eventBus发布数据
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
                           //参数1：发布的主题  参数2：发布的数据，
                       //存放在org.jetlinks.core.event.TopicPayload的payload中
                            .publish("/demo/eventBus/insert", customEntity)
                            .thenReturn(num);
                });
    }

```

### 2.在自定义的`mydemo`下，新建`org.example.mydemo.event.CustomEventHandler`类，在`consumeMessage()`方法中，使用`eventBus`实现订阅


```java
@Slf4j
@Getter
@Setter
@Component
@AllArgsConstructor
public class CustomEventHandler implements CommandLineRunner {
    //引入消息总线
    @Qualifier("brokerEventBus")
    private final EventBus eventBus;
    //引入es
    private final ElasticSearchService elasticSearchService;
    //引入自定义service
    private final CustomService customService;
    
    //订阅并消费
     public void consumeMessage() {
        eventBus.subscribe(Subscription.builder()
                                       //订阅者标识
                                       .randomSubscriberId()
                                       .topics("/demo/eventBus/insert")
                                        //订阅特性字段Feature说明
                                        //local	订阅本地消息
                                        //broker	订阅代理消息
                                        //shared	共享订阅
                                       .features(Subscription.Feature.local, Subscription.Feature.broker)
                                       .build()).flatMap(payload -> {
            //业务操作  往es里面存入数据
            return elasticSearchService
                    .save(CustomIndexEnum.custom.getIndex(), payload.getPayload());
        }).subscribe();
    }
    
    @Override
    public void run(String... args) throws Exception {
        consumeMessage();
    }
    
```

## <font id="3">常见问题</font>

### 每次重启程序后，只能在首次发布并订阅，第二次不成功
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
<p>Q：每次重启程序后，只能在首次发布并订阅，第二次不成功，且使用doOnError()无错误信息提示</p>
<p>A：检查自己的代码是否有断流，如：then()会返回了空流,导致上诉问题的出现</p>
</div>



