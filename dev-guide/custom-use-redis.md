### 自定义模块如何引入使用redis缓存

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <p>1. <a href="#1" >在pom文件中引入redis相关依赖</a></p>
  <p>2. <a href="#2" >平台如何使用redis,提供存存储和获取示例</a></p>
  <p>3. <a href="#3" >常见问题说明</a></p>

</div>



### <font id="1">在pom文件中引入redis相关依赖</font>


```java
    <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-redis</artifactId>
    </dependency>
```

### <font id="2"> 平台如何使用redis</font>
#### 1.在自定义模块`org.example.mydemo.web.CustomController`引入`redis`

```java
以下redis二选一进行使用
//引入redis：选项一
    private final ReactiveRedisOperations<String, String> redis;
// 引入redis：选项二
    private final ReactiveRedisTemplate<Object,Object> redis;
```

#### 2.相关代码示例

1.将数据存入`redis`:自定义模块的web层：`org.example.mydemo.web.CustomController`

```java
 public Mono<CustomEntity> saveData(@RequestBody CustomEntity entity) {
        //保存到数据库
        return customService.insert(entity)
                            .filter(number -> number > 0)
                            //将成功保存到数据库的数据存储到自定义的es索引中
                            .flatMap(number -> elasticSearchService
                                    .save(CustomIndexEnum.custom.getIndex(), entity)
                                    //事件发布，存入redis
                                    .then(eventBus
                                                  .publish("/demo/put/data/toRedis", entity))
                            )
                            .thenReturn(entity);
    }
```

2.事件订阅：`org.example.mydemo.event.CustomEventHandler`

```java
@Getter
@Setter
@Component
@AllArgsConstructor
public class CustomEventHandler implements CommandLineRunner {
    //引入消息总线
    @Qualifier("brokerEventBus")
    private final EventBus eventBus;
    //引入redis
    private final ReactiveRedisOperations<String, String> redis;
    

    public void write2Redis() {
        eventBus
                .subscribe(Subscription
                                   .builder()
                                   .randomSubscriberId()
                                   .topics("/demo/put/data/toRedis")
                                   .features(Subscription.Feature.local, Subscription.Feature.broker)
                                   .build())
                .flatMap(payload ->
                                 redis
                                         .opsForValue()
                                         .set(payload.bodyToJson().getString("id"), payload.bodyToJson().toString()))
                .subscribe();
    }


    @Override
    public void run(String... args) throws Exception {
        write2Redis();
    }
}
```

3.获取redis中的数据，可以使用stream根据业务条件字段分组

```java
/**
     * 从redis缓存中获取数据
     * @param redisKeys 集合key
     * @return
     */
    @PostMapping("/get/data/fromRedis")
    public Mono<Collection<List<CustomEntity>>> getData(@RequestBody List<String> redisKeys) {
        return redis.opsForValue()
                    .multiGet(redisKeys)
                    .flatMap(list -> Mono.just(list.stream()
                                               //转换为自定义类对象
                                               .map(res -> JSONObject.parseObject(res, CustomEntity.class))
                                               .collect(Collectors.toList())
                                               .stream()
                                               //按字段（type）分组
                                               .collect(Collectors.groupingBy(customentity -> ((CustomEntity) customentity).getType()))
                                               .values()));
    }
```

### <font id="3">常见问题</font>

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
<p>Q：引入redis报错：<code>No qualifying bean of type org.springframework.data.redis.core.ReactiveRedisTemplate available: 
expected at least 1 bean which qualifies as autowire candidate. Dependency annotations？</code></p>
<p>A：查看redis的泛型，在平台是否有注入：如，<code>org.jetlinks.pro.standalone.configuration.JetLinksRedisConfiguration</code>配置类中注入了范型为<code>Object,
Object</code>的redis</p>
</div>



