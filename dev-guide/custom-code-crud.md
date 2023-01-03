# 自定义模块中编写增删改查逻辑代码

## 应用场景
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>在自定义模块中，根据业务需求，实现业务的逻辑代码</p>
</div>

## 指导介绍
  <p>1. <a href="/dev-guide/custom-code-crud.html#模块整体构造结构图" >模块整体构造结构图</a></p>
   <p>2. <a href="/dev-guide/custom-code-crud.html#自定义模块entity、service、controller层说明" >自定义模块entity、service、controller层说明</a></p>
   <p>3. <a href="/dev-guide/custom-code-crud.html#将自定义模块下的包交给spring管理和开启实体扫描" >如何将自定义模块下的包交给spring管理和开启实体扫描</a></p>
   <p>4. <a href="/dev-guide/custom-code-crud.html#核心类、接口、方法说明" >核心类、接口、方法说明</a></p>
   <p>5. <a href="/dev-guide/custom-code-crud.html#dsl语法示例" >DSL语法示例</a></p>


## 问题指引
<table>
<tr>
    <td><a target='_self' href='/dev-guide/custom-code-crud.html#启动报错-提示找不到reactiverepository'>启动报错-提示找不到reactiverepository</a></td>
     <td><a target='_self' href='/dev-guide/custom-code-crud.html#服务重启时-提示错误-实体类的表已经存在'>服务重启时，提示错误：实体类的表已经存在</a></td>
</tr>
</table>

## 警告
<table>
<tr>
   <td><a target='_self' href='/dev-guide/custom-code-crud.html#响应式返回值类型不应该为object、必须指出明确的返回类型'>响应式返回值类型不应该为object、必须指出明确的返回类型</a></td>
</tr>
</table>




## 模块整体构造结构图 

简单的业务系统目录结构如下图：

![自定义项目目录结构](./images/code-guide-1-6.png)


## 自定义模块entity、service、controller层说明

### 1. entity层 ：`org.example.mydemo.entity.CustomEntity`

```java
package org.example.mydemo.entity;

@Table(name = "dev_my_demo_customentity", indexes = {
        @Index(name = "idx_device_id_type", columnList = "device_id,type")
})
@Comment("自定义表")
@Setter
@Getter
public class CustomEntity extends GenericEntity<String> implements
        RecordCreationEntity, RecordModifierEntity {

    @Column(name = "device_id")
    @Comment("设备id")
    private String deviceId;

    @Column
    @Comment("类型")
    private String type;

    @Column(name = "creator_id", updatable = false)
    @Schema(
            description = "创建者ID(只读)"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String creatorId;

    @Column(name = "creator_name", updatable = false)
    @Schema(
            description = "创建者名称(只读)"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String creatorName;

    @Column(name = "create_time", updatable = false)
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(
            description = "创建时间(只读)"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long createTime;

    @Column
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(
            description = "修改时间"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long modifyTime;

    @Column(length = 64)
    @Schema(
            description = "修改人ID"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String modifierId;

    @Column(length = 64)
    @Schema(
            description = "修改人名称"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String modifierName;

}

```

### 2.Service层 ：`org.example.mydemo.service.CustomService`

  ```java
package org.example.mydemo.service;

 @Service
 public class CustomService extends GenericReactiveCrudService<CustomEntity,String> {
 }
  ```

### 3.web层：`org.example.mydemo.web.CustomController`

  ```java
  package com.example.mydemo.web;
  
@RestController
@RequestMapping("/demo")
@Getter
@AllArgsConstructor
@Resource(id = "demo", name = "自定义接口")
@Tag(name = "自定义接口")
@Authorize(ignore = true)
public class CustomController implements ReactiveServiceCrudController<CustomEntity, String> {

    private final CustomService customService;

    @GetMapping
    @QueryAction
    @Operation(summary = "自定义接口说明")
    public Flux<Void> getAll() {
        return Flux.empty();
    }

}
  ```


##  将自定义模块下的包交给spring管理和开启实体扫描
### 在`org.jetlinks.pro.standalone`的启动类`JetLinksApplication`上加入自定义项目的扫描路径

代码：
```java
//新增需要Spring扫描的包
@SpringBootApplication(scanBasePackages = {"org.jetlinks.pro", "org.example.mydemo"}, exclude = {
        DataSourceAutoConfiguration.class,
        KafkaAutoConfiguration.class,
        RabbitAutoConfiguration.class,
        ElasticsearchRestClientAutoConfiguration.class,
        ElasticsearchDataAutoConfiguration.class,
        MongoReactiveAutoConfiguration.class,
})
@EnableCaching
//新增实体扫描包
@EnableEasyormRepository({"org.jetlinks.pro.**.entity","org.example.mydemo.entity.**"})
@EnableAopAuthorize
@EnableAccessLogger
public class JetLinksApplication {
    public static void main(String[] args) {
        SpringApplication.run(JetLinksApplication.class, args);
    }
    @Component
    @Slf4j
    public static class AdminAllAccess {
        @EventListener
        public void handleAuthEvent(AuthorizingHandleBeforeEvent e) {
            if (e.getContext().getAuthentication().getUser().getUsername().equals("admin")) {
                e.setAllow(true);
            }
        }
        @EventListener
        public void handleAccessLogger(AccessLoggerAfterEvent event) {
            log.info("{}=>{} {}-{}", event.getLogger().getIp(), event.getLogger().getUrl(), event.getLogger().getDescribe(), event.getLogger().getAction());
        }
    }
}

```
## 核心类、接口、方法说明
### 1. 核心类说明

| 类名                            | 说明                                              |
|-------------------------------|-------------------------------------------------|
| `ReactiveServiceCrudController<E, K>` | 实体CRUD操作的抽象接口，该接口继承了CRUD相关操作的接口，这些接口内封装了默认的实现方法,`<E> `实体类类型 ` <K> `主键类型 |
| `GenericEntity<PK>`             | 实体类需要继承该类，需要声明实体id数据类型                          |
| `GenericReactiveCrudService<E, K>`    | 业务层实体需要继承该类，该类有默认的crud方法的实现                     |

### 2. 核心接口说明

`<E>` 实体类类型 `<K>` 主键类型

service层接口`org.hswebframework.web.crud.service.ReactiveCrudService<E, K>`

| 方法名 | 返回值 | 参数值 | 说明  |
|------- |--------|----------|------------|
|`getRepository()` |`ReactiveRepository<E, K>`|无|`响应式实体操作仓库`|

### 3.核心方法（响应式参数）

| 方法名                                                       | 返回值             | 参数值                                                       | 说明             |
| ------------------------------------------------------------ | ------------------ | ------------------------------------------------------------ | ---------------- |
| `findById(Mono<K> publisher)`                                | `Mono<E>`          | `Mono<K> publisher` publisher-ID流                           | 根据ID集合流查询 |
| `findById(Flux<K> publisher)`                                | `Flux<E>`          | `Flux<K> publisher ` publisher-ID流                          | 根据ID集合流查询 |
| `save(Publisher<E> entityPublisher)`                         | `Mono<SaveResult>` | `Publisher<E> entityPublisher` publisher-数据实体流          | 异步保存数据     |
| `updateById(K id, Mono<E> entityPublisher)`                  | `Mono<Integer>`    | K id-ID值 `Mono<E>` entityPublisher-更新数据流               | 异步保存数据     |
| `insertBatch(Publisher<? extends Collection<E>> entityPublisher)` | `Mono<Integer>`    | `Publisher<? extends Collection<E>>` entityPublisher -保存数据集合流 | 异步批量保存数据 |
| `insert(Publisher<E> entityPublisher)`                       | `Mono<Integer>`    | `Publisher<E>` entityPublisher-保存数据流                    | 异步保存数据     |
| `count(Mono<? extends QueryParamEntity> queryParamMono)`     | `Mono<Integer>`    | `Mono<? extends QueryParamEntity>` queryParamMono-查询参数流 | 查询记录数       |

### 4.核心方法（非响应式参数）


#### 1、动态DSL接口相关

| 方法名           | 返回值              | 参数值 | 说明                                                         |
| ---------------- | ------------------- | ------ | ------------------------------------------------------------ |
| `createQuery()`  | `ReactiveQuery<E>`  | 无     | 创建一个DSL的动态查询接口,可使用DSL方式进行链式调用来构造动态查询条件 |
| `createUpdate()` | `ReactiveUpdate<E>` | 无     | 创建一个DSL动态更新接口,可使用DSL方式进行链式调用来构造动态更新条件 |
| `createDelete()` | `ReactiveDelete`    | 无     | 创建一个DSL动态删除接口,可使用DSL方式进行链式调用来构造动态删除条件 |

#### 2、其他简单方法

| 方法名                                        | 返回值                 | 参数值                                       | 说明                                         |
| --------------------------------------------- | ---------------------- | -------------------------------------------- | -------------------------------------------- |
| `findById(K id)`                              | `Mono<E>`              | K id-id值                                    | 根据ID查询                                   |
| `findById(Collection<K> publisher)`           | `Flux<E>`              | `Collection<K>` publisher-ID集合             | 根据ID集合查询                               |
| `save(E data)`                                | `Mono<SaveResult>`     | E data-要保存的数据                          | 保存单个数据,如果数据不存在则新增,存在则修改 |
| `save(Collection<E> collection)`              | `Mono<SaveResult>`     | `Collection<E>` collection-要保存的数据集合  | 保存多个数据,如果数据不存在则新增,存在则修改 |
| `updateById(K id, E data)`                    | `Mono<Integer>`        | K id-ID值<br/>, E data-更新的数据            | 保存多个数据,如果数据不存在则新增,存在则修改 |
| `insert(E data)`                              | `Mono<Integer>`        | E data-新增数据<br/>, E data-更新的数据      | 根据数据流新增数据                           |
| `deleteById(K id)`                            | `Mono<Integer>`        | K id-Id-ID值<br/>                            | 根据ID删除数据                               |
| `queryPager(QueryParamEntity queryParamMono)` | `Mono<PagerResult<E>>` | QueryParamEntity queryParamMono-查询参数实体 | 分页查询获取数据                             |
| `count(QueryParamEntity queryParam)`          | `Mono<PagerResult<E>>` | QueryParamEntity queryParam-查询参数实体     | 获取数据记录数                               |



## DSL语法示例
### 1、相关代码示例

```java
/**
     * 根据自定义的实体类的设备ID查询对应实体类信息
     * select * from dev_my_demo_CustomEntity where device_id=?
     *
     * @param deviceId 设备id
     * @return
     */
    @PostMapping("/{deviceId}/dsl/eq")
    public Flux<CustomEntity> createQueryEqual(@PathVariable(value = "deviceId") String deviceId) {
        return customService.createQuery()
                .where("device_id", deviceId)
                .fetch();
    }

     /**
     * 根据自定义的实体类的设备ID查询对应实体类信息
     * select * from dev_my_demo_CustomEntity where device_id= like ?+'%'
     *
     * @param deviceId 设备id
     * @return
     */
    @PostMapping("/dsl/like")
    public Flux<CustomEntity> createQueryLike(String deviceId) {
        return customService.createQuery()
                .like$("device_id",deviceId)
                .fetch();
    }


    /**
     * 根据设备ID更新设备信息，并更新自定义es索引数据
     * @param typeList type集合
     * @param deviceId 设备ID
     */
    @PostMapping("/{deviceId}/dsl/update")
    public Mono<Integer> createUpdate(@RequestBody List<Integer> typeList, @PathVariable String deviceId) {
       return customService.createUpdate()
                //设置设备ID
                .set("device_id", deviceId)
                .where()
                //查询条件 type like '%xxx'
                .like$("type", typeList)
                //更新后可以执行一些其他的操作
                .onExecute((update, result) -> {
                    return result
                            .flatMap(i -> {
                                //更新es索引
                                return elasticSearchService
                                        .save(CustomIndexEnum.custom.getIndex(), update.toQueryParam())
                                        .thenReturn(i);
                                //执行
                            });

                })
                .execute();
    }

    /**
     * 根据设备ID，删除自定义设备信息，并删除自定义es索引数据
     * @param deviceId 设备ID
     * @return
     */
    @PostMapping("/{deviceId}/dsl/delete")
    public Mono<Integer> createDelete(@PathVariable String deviceId) {
        return customService.createDelete()
                .where("device_id", deviceId)
                .onExecute((delete, result) -> {
                    return result
                            .flatMap(i -> {
                                return elasticSearchService.
                                        delete(CustomIndexEnum.custom.getIndex(), delete.toQueryParam())
                                        .thenReturn(i);
                            });

                })
                .execute();
    }
```



### 2.核心接口`org.hswebframework.ezorm.rdb.mapping.ReactiveUpdate`

| 方法名                                                       | 返回值              | 参数值                                                       | 说明                                                         |
| ------------------------------------------------------------ | ------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `execute()`                                                  | `Mono<Integer>`     | 无                                                           | 执行更新                                                     |
| `onExecute(BiFunction<ReactiveUpdate<E>, Mono<Integer>, Mono<Integer>> consumer)` | `ReactiveUpdate<E>` | `BiFunction<ReactiveUpdate<E>,Mono<Integer>,Mono<Integer>> consumer-函数式接口参数)` | 执行结果处理器<br/>`ReactiveUpdate<E>`-实体数据<br/>`Mono<Integer>`-更新执行器<br/> `Mono<Integer>`-新的执行器 |



### 3、核心接口`org.hswebframework.ezorm.rdb.mapping.ReactiveDelete`

| 方法名                                                       | 返回值           | 参数                                                         |                                                              |
| ------------------------------------------------------------ | ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `execute()`                                                  | `Mono<Integer>`  | 无                                                           | 执行异步删除,返回被删除的数据条数                            |
| `onExecute(BiFunction<ReactiveDelete, Mono<Integer>,Mono<Integer>> mapper)` | `ReactiveDelete` | `BiFunction<ReactiveDelete, Mono<Integer>,Mono<Integer>> mapper-函数式接口参数` | 执行结果处理器<br/>ReactiveDelete-当前动态删除接口<br/>`Mono<Integer>`-删除执行器<br/>`Mono<Integer>`-新的执行器 |



### 4、核心接口`org.hswebframework.ezorm.rdb.mapping.ReactiveQuery<T>`

| 方法名       | 返回值          | 参数值 | 说明                                                        |
| ------------ | --------------- | ------ | ----------------------------------------------------------- |
| `fetch()`    | `Flux<T>`       | 无     | 执行查询并获取返回数据流,如果未查询到结果将返回Flux.empty() |
| `count()`    | `Mono<Integer>` | 无     | 执行count查询,并返回count查询结果.                          |
| `fetchOne()` | `Mono<T>`       | 无     | 执行查询并返回单个数据，T表示实体类                         |



## 常见问题


### 启动报错，提示找不到`ReactiveRepository`

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
   <p>
   Q：启动报错，<code>Field repository in org.hswebframework.web.crud.service.GenericReactiveCrudService required a bean of 
type 'org.hswebframework.ezorm.rdb.mapping.ReactiveRepository' that could not be found.？</code>
  </p>
   <p>
    A：检查<code>`org.jetlinks.pro.standalone`的启动类`JetLinksApplication`的 @EnableEasyormRepository注解的实体扫描包
  如：@EnableEasyormRepository({"org.jetlinks.pro.**.entity",<span class="explanation-title font-weight">"org.example.
xxx.entity.*"</span>})</code>
   </p>
   <p>
     应改为：<br/><p>@EnableEasyormRepository({"org.jetlinks.pro.**.entity",<span class="explanation-title 
font-weight">"org.example.xxx.entity"</span>})</p>
    或者：<br/>
    <p>@EnableEasyormRepository({"org.jetlinks.pro.**.entity",<span class="explanation-title font-weight">"org.
example.xxx.entity.**"</span>})</p>
   原因：<br/>
    平台*使用ClassUtils.getPackageName(type)获取全类名称,@EnableEasyormRepository注解底层使用的是AntPathMatcher匹配机制，在AntPathMatcher匹配规则中a.b.c不能和a.b.c.*匹配
  </p>
 </p>
</div>


###  服务重启时-提示错误：实体类的表已经存在
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>
  <p>
   Q：服务重启时-提示错误：实体类的表已经存在？
  </p>
  <p>
   A：<code>@Table(name = "xxx")</code>的<code>name</code>属性值中不能存在任何大写字母
  </p>
</div>

### 响应式返回值类型不应该为Object、必须指出明确的返回类型
<div class='explanation error'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>危险</span>
  </p>
  <p>响应式返回Mono&lt;Object&gt;或者Flux&lt;Object&gt;会报错，必须指出明确的返回类型。 </p>
  <p>响应式使用@RequestBody注解的参数必须使用流包裹。</p>
  <p>JetLinks从上至下使用全部使用响应式，基于JetLinks平台构建自己的业务代码时也请使用响应式。</p>
  <p>如果不会写响应式，建议最好独立项目不要与JetLinks混合使用非响应式，可能会导致项目出现阻塞。</p>

</div>

