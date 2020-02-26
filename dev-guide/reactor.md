# 响应式

JetLinks使用[reactor](https://github.com/reactor)作为响应式编程框架,从网络层(`webflux`,`vert.x`)到持久层(`r2dbc`,`elastic`)全部
封装为`非阻塞`,`响应式`调用.

响应式可以理解为`观察者模式`,通过`订阅`和`发布`数据流中的数据对数据进行处理. 
`reactor`提供了强大的API,简化了对数据各种处理方式的复杂度,如果你已经大量使用了`java8 stream api`,使用`reactor`将很容易上手.

::: tip 注意
`响应式`与`传统编程`最大的区别是:
`响应式`中的方法调用是在`构造`一个流以及`处理流中数据`的逻辑,当`流`中产生了数据(`发布,订阅`),才会执行构造好的逻辑.
`传统编程`则是直接执行逻辑获取结果.
:::

## 优点

非阻塞,集成`netty`等框架可实现更高的网络并发处理能力.
API丰富,实现很多复杂的功能只需要几行代码,例如:

1. 前端展示实时数据处理进度.
2. 请求撤销,可获取到连接断开事件.
3. 定时(`interval`),延迟(`delay`),超时(`timout`),以及细粒度的流量控制(`limitRate`). 

## 缺点

调试不易,异常栈难跟踪,对开发人员有更高的要求.

::: warning 注意
 响应式只是一个编程模型,并不能直接提高系统的并发处理能力.
 通常与netty(`reactor-netty`)等框架配合,从上(`网络`)到下(`持久化`)全套实现`非阻塞`,`响应式`才有意义.
:::

# 说明

系统中大量使用到了`reactor`,其核心类只有2个`Flux`(0-n个数据的流),`Mono`(0-1个数据的流). 
摒弃`传统编程`的思想,熟悉`Flux`,`Mono`API,就可以很好的使用响应式编程了.

常用API: 

1. `map`: 转换流中的元素: `flux.map(UserEntity::getId)`
2. `flatMap`: 转换流中的元素为新的流: `flux.flatMap(this::findById)`
3. `flatMapMany`: 转换Mono中的元素为Flux(1转多): `mono.flatMapMany(this::findChildren)`
4. `concat`: 将多个流连接在一起组成一个流(按顺序订阅) : `Flux.concat(header,body)`
5. `merge`: 将多个流合并在一起,同时订阅流: `Flux.merge(save(info),saveDetail(detail))`
6. `zip`: 压缩多个流中的元素: `Mono.zip(getData(id),getDetail(id),UserInfo::of)`
7. `then`: 流完成后执行.
8. `doOnNext`: 流中产生数据时执行.
9. `doOnError`: 发送错误时执行.
10. `doOnCancel`: 流被取消时执行.
11. `onErrorContinue`: 流发生错误时,继续处理数据而不是终止整个流.
12. `defaultIfEmpty`: 当流为空时,使用默认值.
13. `switchIfEmpty`: 当流为空时,切换为另外一个流.
14. `as`: 将流作为参数,转为另外一个结果:`flux.as(this::save)`

完整文档请查看[官方文档](https://projectreactor.io/docs/core/release/reference/)

# 注意

## 代码格式化

使用`reactor`时,应该注意代码尽量以`.`换行并做好相应到缩进.例如:

```java

//错误
return paramMono.map(param->param.getString("id")).flatMap(this::findById);

//建议
return paramMono
            .map(param->param.getString("id")) 
            .flatMap(this::findById);

```

## lamdba

避免在一个`lambda`中编写大量的逻辑代码,推荐参考`领域模型`,将具体当逻辑放到对应到`实体`或者`领域对象`中.例如:

```java

//错误
return devicePropertyMono
        .map(prop->{
            Map<String,Object> map = new HashMap<>();
            map.put("property",prop.getProperty());
            ....
            return map;
        })
        .flatMap(this::doSomeThing)

//建议
//在DeviceProperty中编写toMap方法实现上面lambda中到逻辑.
return devicePropertyMono
        .map(DeviceProperty::toMap)
        .flatMap(this::doSomeThing)

```

## null处理

数据流中到元素不允许为`null`,因此在进行数据转换到时候要注意`null`处理.例如:

```java

//存在缺陷
return this.findById(id)
            .map(UserEntity::getDescription); //getDescription可能返回null,为null时会抛出空指针,

```

## 非阻塞与阻塞

默认情况下,`reactor`的调度器由数据的生产者(`Publisher`)决定,在`WebFlux`中则是`netty`的工作线程,
为了防止工作线程被阻塞导致服务崩溃,在一个请求的流中,禁止执行存在阻塞(如执行`JDBC`)可能的操作的,如果无法避免阻塞操作,应该指定调度器如:

```java
paramMono
  .publishOn(Schedulers.elastic()) //指定调度器去执行下面的操作
  .map(param-> jdbcService.select(param))
```

## 上下文

在响应式中,大部分情况是禁止使用`ThreadLocal`的(可能造成内存泄漏).因此基于`ThreadLocal`的功能都无法使用,reactor中引入了上下文,在一个流中,可共享此上下文
,通过上下文进行变量共享以例如:`事务`,`权限`等功能.例如:

```java

//从上下文中获取
@GetMapping
public Mono<UserInfo> getCurrentUser(){
    return Mono.subscriberContext()
            .map(ctx->userService.findById(ctx.getOrEmpty("userId").orElseThrow(IllegalArgumentException::new));
}

//定义过滤器设置数据到上下文中
class MyFilter implements WebFilter{
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
        return chain.filter(exchange)
            .subscriberContext(Context.of("userId",...))
    }
}

```

::: warning 注意

在开发中应该将多个流组合为一个流,而不是分别订阅流.例如:

```java
//错误
flux.doOnNext(data->this.save(data).subscribe());

//正确
flux.flatMap(this::save);
```
:::

# 相关资料

1. [reactive-streams](http://www.reactive-streams.org/)
2. [project-reactor](https://projectreactor.io/)
3. [使用 Reactor 进行反应式编程](https://www.ibm.com/developerworks/cn/java/j-cn-with-reactor-response-encode/index.html?lnk=hmhm)
4. [simviso视频教程](https://space.bilibili.com/2494318)