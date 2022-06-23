# 响应式

JetLinks使用[Project Reactor](https://github.com/reactor)作为响应式编程框架,从网络层(`webflux`,`vert.x`)到持久层(`r2dbc`,`elastic`)全部
封装为`非阻塞`,`响应式`调用.

响应式可以理解为`观察者模式`,通过`订阅`和`发布`数据流中的数据对数据进行处理. 
`Project Reactor`提供了强大的API,简化多线程和异步编程开发,降低了对数据各种处理方式的复杂度,如果你已经大量使用了`java8 stream api`,使用`reactor`将很容易上手.

::: tip 注意
`响应式`与`传统编程`最大的区别是:

`响应式`中的方法调用是在`构造`一个流以及`处理流中数据`的逻辑,当`流`中产生了数据(`发布,订阅`),才会执行构造好的逻辑.

`传统编程`则是直接执行逻辑获取结果.
:::

## 优点

非阻塞,大大简化多线程异步编程.
集成`netty`等框架可实现更高的网络并发处理能力.
API丰富,实现很多复杂的功能只需要几行代码,例如:

1. 前端展示实时数据处理进度.
2. 请求撤销,可获取到连接断开事件.
3. 定时(`interval`),延迟(`delay`),超时(`timout`),以及细粒度的流量控制(`limitRate`). 
4. 分组(`groupBy`),聚合(`collect`,`reduce`)操作等

## 缺点

调试不易,异常栈难跟踪,对开发人员有更高的要求.

此问题可以通过优化代码结构来解决,比如: <span style="color:red">避免在响应式操作符中直接业务逻辑</span>,
正确的做法是<span style='color:green'>将业务逻辑抽离为独立的函数(方法)</span>,然后使用响应式来进行组合.

::: warning 注意
 响应式只是一个编程模型,并不能直接提高系统的并发处理能力.
 通常与netty(`reactor-netty`)等框架配合,从上(`网络`)到下(`持久化`)全套实现`非阻塞`,`响应式`才有意义.
:::

## 选择合适的操作符

系统中大量使用到了`reactor`,其核心类只有2个`Flux`(0-n个数据的流),`Mono`(0-1个数据的流). 
摒弃`传统编程`的思想,熟悉`Flux`,`Mono`操作符(API),就可以很好的使用响应式编程了.

常用操作符: 

1. `map`: 转换流中的元素: `flux.map(UserEntity::getId)`
2. `mapNotNull`: 转换流中的元素,并忽略null值.(`reactor 3.4`提供)
3. `flatMap`: 转换流中的元素为新的流: `flux.flatMap(this::findById)`
4. `flatMapMany`: 转换Mono中的元素为Flux(1个转多个): `mono.flatMapMany(this::findChildren)`
5. `concat`: 将多个流连接在一起组成一个流(按顺序订阅) : `Flux.concat(header,body)`
6. `merge`: 将多个流合并在一起,同时订阅流: `Flux.merge(save(info),saveDetail(detail))`
7. `zip`: 压缩多个流中的元素: `Mono.zip(getData(id),getDetail(id),UserInfo::of)`
8. `then`: 上游流完成后执行其他的操作.
9. `doOnNext`: 流中产生数据时执行.
10. `doOnError`: 发送错误时执行.
11. `doOnCancel`: 流被取消时执行.如: http未响应前,客户端断开了连接.
12. `onErrorContinue`: 流发生错误时,继续处理数据而不是终止整个流.
13. `defaultIfEmpty`: 当流为空时,使用默认值.
14. `switchIfEmpty`: 当流为空时,切换为另外一个流.
15. `as`: 将流作为参数,转为另外一个结果:`flux.as(this::save)`

完整文档请查看[官方文档](https://projectreactor.io/docs/core/release/reference/#which-operator)


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
在`reactor 3.4`后可以使用以下方式来处理可能存在null的map操作

```java
return this.findById(id)
           .mapNotNull(UserEntity::getDescription); 
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

在开发中应该将多个流组合为一个流,而不是分别处理.例如:

```java
//错误
return flux.doOnNext(data->this.save(data).subscribe());

//正确
return flux.flatMap(this::save);

//错误,没有将流组合在一起
request.flatMap(this::save);
Mono<Void> result = this.notifySaveSuccess();
return result;

//正确
return request
    .flatMap(this::save)
    .then(this.notifySaveSuccess());

```
:::


## FAQ: 我写的操作看上去是正确的,但是没有执行.

有3种可能: `上游流为空`,`多个流未组合在一起`,`在不支持响应式的地方使用了响应式`

上游流为空:

例:
```java{6-7}
public Mono<Response> handleRequest(Request request){
   
 return this
      .findOldData(request)
      .flatMap(old -> {
            //这里为什么不执行?
            return ....
      })
 
}

```

::: tip 说明
当`findOldData`返回的流为空时,下游的`flatMap`等需要`操作流中元素的操作符`是不会执行的.
可以通过`switchIfEmpty`操作符来处理空流的情况. 
例如:
```java
 return this
      .findOldData(request)
      //处理没获取到数据的情况
      .switchIfEmpty(Mono.error(()->new NotFoundException("error.data_not_found")))
      .flatMap(old -> {
            return ....
      })
```

如果`flatMap`和`switchIfEmpty`中的逻辑都没执行,那可能是下面一种情况.
:::

多个流未组合在一起

例:

```java

public Result handleRequest(Request request){
 
 //处理请求
 service.handleRequest(request);

 return ok;
 
}

```

::: tip 注意
1. <span style="color:red">只要方法返回值是`Mono`或者`Flux`,都不能单独行动.</span>
2. <span style="color:red">只要方法中调用了任何响应式操作.那这个方法也应该是响应式.(返回Mono或者Flux)</span>

因此正确的写法是:

```java
public Mono<Result> handleRequest(Request request){
 return service
         //处理请求
         .handleRequest(request)
         //记录日志
         .then(saveLog(request))
         //返回结果
         .thenReturn(ok);
}
```

:::

在不支持响应式的地方使用响应式

```java{6-7}
public Mono<Result> handleRequest(Request request){

return service
         //处理请求
         .handleRequest(request)
         //记录日志 此为错误的用法
         .doOnNext(response-> saveLog(request,response) )
         //返回结果
         .thenReturn(ok);
}
```

::: tip 说明
从`doOnNext`方法的语义以及参数`Consumer<T>`可知,此方法是不支持响应式的（`Consumer<T>只有参数没有返回值`）,因此不能在此方法中使用响应式操作.

正确的写法:

```java
return service
         //处理请求
         .handleRequest(request)
         //记录日志 此为错误的用法
         .flatMap(response-> saveLog(request,response) )
         //返回结果
         .thenReturn(ok);
```

:::

## FAQ: 我想获取流中的元素怎么办

不要试图从流中获取数据出来,而是先思考需要对流中元素做什么,
需要对流中的数据进行操作时,都应该使用操作符来处理,根据`Flux或者Mono`提供的操作符API进行组合操作.比如:

传统:
```java

public List<Book> getAllBooks(){
    List<BookEntity> bookEntities = repository.findAll();

    List<Book> books = new ArrayList(bookEntities.size());

    for(BookEntity entity : bookEntities){
        Book book = entity.copyTo(new Book());
        books.add(book);
    }

    return books;
}
```

响应式:

```java
public Flux<Book> getAllBooks(){
return repository
        .findAll()
        .map(entity-> entity.copyTo(new Book()))
}

```

## FAQ: 我需要在非响应式方法中使用响应式怎么办

如果需要阻塞获取结果,可以使用`flux.block(timeout)`.

如果需要异步获取结果,可以使用`flux.subscribe(data->{ },error->{ })`

如:

```java
public void handleRequest(Request request){

  //logService.saveLog(request).block()
    
  logService
    .saveLog(request)
    .subscribe(
        result->{
            log.debug("保存成功 {}",request)
        },
        error->{
            log.warn("保存失败 {}",request,error);
        }
    )
}
```


## 相关资料

1. [reactive-streams](http://www.reactive-streams.org/)
2. [project-reactor](https://projectreactor.io/)
3. [使用 Reactor 进行反应式编程](https://www.ibm.com/developerworks/cn/java/j-cn-with-reactor-response-encode/index.html?lnk=hmhm)
