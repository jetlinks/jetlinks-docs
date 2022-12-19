# 平台开发指导手册

## 在JetLinks上构建自己的业务功能

- <a target='_self' href='/dev-guide/custom-code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9D%97%E9%A1%B9%E7%9B%AE'>
  自定义模块项目</a>  
- <a target='_self' href='/dev-guide/custom-code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9D%97%E4%B8%AD%E7%BC%96%E5%86%99%E5%A2%9E%E5%88%A0%E6%94%B9%E6%9F%A5%E9%80%BB%E8%BE%91%E4%BB%A3%E7%A0%81'>
  自定义模块中编写增删改查逻辑代码</a>
- <a target='_self' href='/dev-guide/custom-code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9D%97%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8es'>
  自定义模块如何使用es</a>
- <a target='_self' href='/dev-guide/custom-code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9D%97%E5%A6%82%E4%BD%95%E5%BC%95%E5%85%A5%E4%BD%BF%E7%94%A8redis%E7%BC%93%E5%AD%98'>
  自定义模块如何引入使用redis缓存</a> 
- <a target='_self' href='/dev-guide/custom-code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9D%97%E5%A6%82%E4%BD%95%E5%88%86%E7%BB%84%E6%9F%A5%E8%AF%A2%E4%BA%A7%E5%93%81%E8%AE%BE%E5%A4%87%E4%BF%A1%E6%81%AF'>
  自定义模块如何分组查询产品设备信息</a>
- <a target='_self' href='/dev-guide/custom-code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9D%97%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8sqlExecutor'>
  自定义模块如何使用sqlExecutor</a>




### 在JetLinks上构建自己的业务功能

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

   <p>当您想使用JetLinks平台做自己的业务，又不想将项目独立时，可以选择基于JetLinks进行开发。</p>

  <p>JetLinks平台不用自动创建数据库，遵循JPA(Java Persistence API)规范，会根据@table注解自动创建表</p>

</div>

### 自定义模块项目

#### 1.创建自定义Maven项目

![创建新的Maven项目](./images/code-guide-1-1.png)

#### 2. 自定义的Maven项目与jetlinks-pro同级（低版本IDEA窗口下，新的Maven模块后显示root字样）

![项目未加入JetLinks平台内时](./images/code-guide-1-2.png)

#### 3.将自定义模块加入JetLinks平台

##### 1. 在jetlinks-pro目录根路径下的`pom.xml`文件内声明自定义项目加入多模块管理

![在pom文件内声明模块信息](./images/code-guide-1-3.png)
示例代码:

```xml

<modules>
    <module>jetlinks-parent</module>
    <module>jetlinks-components</module>
    <module>jetlinks-manager/authentication-manager</module>
    <module>jetlinks-manager/device-manager</module>
    <module>jetlinks-manager/network-manager</module>
    <module>jetlinks-manager/notify-manager</module>
    <module>jetlinks-manager/rule-engine-manager</module>
    <module>jetlinks-manager/logging-manager</module>
    <module>jetlinks-manager/datasource-manager</module>
    <module>jetlinks-manager/things-manager</module>
    <module>jetlinks-standalone</module>
    <!--  声明加入自定义模块名称-->
    <module>my-demo</module>
    <module>test-report</module>
</modules>
```

##### 2. 在jetlinks-standalone目录路径下的`pom.xml`文件内`profiles`节点中声明以下代码

![在pom文件内声明模块信息](./images/code-guide-1-4.png)

示例代码:

```xml
<!-- 使用profile动态加入模块-->
<profile>
    <id>demo</id>
    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>my-demo</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</profile>
```

##### 3. reimport项目

以上两步操作完成之后需要使用Maven窗口的`reimport`即 `Reload ALL Maven Project`按钮，重新引入模块依赖，此时模块被加入jetlinks-pro项目下
![在pom文件内声明模块信息](./images/code-guide-1-5.png)

##### 4. 加入子模块声明后，修改自定义项目pom文件内容

示例代码：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!-- 声明父模块 -->
    <parent>
        <groupId>org.jetlinks.pro</groupId>
        <artifactId>jetlinks-parent</artifactId>
        <version>2.0.0-SNAPSHOT</version>
        <relativePath>../jetlinks-parent/pom.xml</relativePath>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>my-demo</artifactId>

    <dependencies>
        <!-- 引入hsweb依赖，该依赖主要用于业务系统crud功能模块   -->
        <dependency>
            <groupId>org.hswebframework.web</groupId>
            <artifactId>hsweb-starter</artifactId>
            <version>${hsweb.framework.version}</version>
        </dependency>

        <dependency>
            <groupId>org.hswebframework</groupId>
            <artifactId>hsweb-easy-orm-rdb</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <nonFilteredFileExtensions>
                        <nonFilteredFileExtension>zip</nonFilteredFileExtension>
                        <nonFilteredFileExtension>jar</nonFilteredFileExtension>
                    </nonFilteredFileExtensions>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>


```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
<p>Q：如何确认模块被引入？</p>
<p>A：可以使用Maven工具或者命令打包时出现自定义模块的名称说明模块被引入。</p>


```text
[INFO] Reactor Summary:
[INFO] 
[INFO] jetlinks-parent .................................... SUCCESS [  1.346 s]
[INFO] jetlinks-components ................................ SUCCESS [  0.084 s]
[INFO] test-component ..................................... SUCCESS [  9.813 s]
[INFO] common-component ................................... SUCCESS [ 13.924 s]
[INFO] timeseries-component ............................... SUCCESS [  5.407 s]
[INFO] dashboard-component ................................ SUCCESS [  8.283 s]
[INFO] network-component .................................. SUCCESS [  0.108 s]
[INFO] network-core ....................................... SUCCESS [ 10.744 s]
[INFO] gateway-component .................................. SUCCESS [ 11.436 s]
[INFO] assets-component ................................... SUCCESS [ 12.147 s]
[INFO] geo-component ...................................... SUCCESS [  7.302 s]
[INFO] api-component ...................................... SUCCESS [  8.520 s]
[INFO] configure-component ................................ SUCCESS [ 11.337 s]
[INFO] script-component ................................... SUCCESS [  8.600 s]
[INFO] streaming-component ................................ SUCCESS [  3.830 s]
[INFO] things-component ................................... SUCCESS [ 21.910 s]
[INFO] elasticsearch-component ............................ SUCCESS [ 13.424 s]
[INFO] relation-component ................................. SUCCESS [  9.770 s]
[INFO] rule-engine-component .............................. SUCCESS [  8.008 s]
[INFO] notify-component ................................... SUCCESS [  0.102 s]
[INFO] notify-core ........................................ SUCCESS [  8.918 s]
[INFO] notify-sms ......................................... SUCCESS [  8.037 s]
[INFO] io-component ....................................... SUCCESS [  8.139 s]
[INFO] notify-email ....................................... SUCCESS [  8.770 s]
[INFO] notify-wechat ...................................... SUCCESS [  9.273 s]
[INFO] notify-dingtalk .................................... SUCCESS [  9.235 s]
[INFO] notify-voice ....................................... SUCCESS [  7.400 s]
[INFO] notify-webhook ..................................... SUCCESS [  7.467 s]
[INFO] coap-component ..................................... SUCCESS [ 15.998 s]
[INFO] mqtt-component ..................................... SUCCESS [ 12.371 s]
[INFO] http-component ..................................... SUCCESS [ 13.102 s]
[INFO] tcp-component ...................................... SUCCESS [ 17.454 s]
[INFO] websocket-component ................................ SUCCESS [ 17.322 s]
[INFO] udp-component ...................................... SUCCESS [ 16.092 s]
[INFO] simulator-component ................................ SUCCESS [ 10.106 s]
[INFO] protocol-component ................................. SUCCESS [ 10.202 s]
[INFO] datasource-component ............................... SUCCESS [ 10.831 s]
[INFO] messaging-component ................................ SUCCESS [  0.102 s]
[INFO] rabbitmq-component ................................. SUCCESS [  7.784 s]
[INFO] kafka-component .................................... SUCCESS [  7.923 s]
[INFO] logging-component .................................. SUCCESS [  5.452 s]
[INFO] tenant-component ................................... SUCCESS [  8.594 s]
[INFO] influxdb-component ................................. SUCCESS [  9.655 s]
[INFO] tdengine-component ................................. SUCCESS [ 10.927 s]
[INFO] clickhouse-component ............................... SUCCESS [ 10.780 s]
[INFO] cassandra-component ................................ SUCCESS [ 11.147 s]
[INFO] function-component ................................. SUCCESS [  0.107 s]
[INFO] function-api ....................................... SUCCESS [  3.569 s]
[INFO] function-manager ................................... SUCCESS [ 14.735 s]
[INFO] collector-component ................................ SUCCESS [ 12.584 s]
[INFO] application-component .............................. SUCCESS [ 14.226 s]
[INFO] authentication-manager ............................. SUCCESS [ 18.356 s]
[INFO] device-manager ..................................... SUCCESS [ 15.212 s]
[INFO] network-manager .................................... SUCCESS [ 13.248 s]
[INFO] notify-manager ..................................... SUCCESS [  9.168 s]
[INFO] rule-engine-manager ................................ SUCCESS [ 11.895 s]
[INFO] logging-manager .................................... SUCCESS [  7.648 s]
[INFO] datasource-manager ................................. SUCCESS [  8.070 s]
[INFO] things-manager ..................................... SUCCESS [  9.319 s]
[INFO] jetlinks-ctwing .................................... SUCCESS [  6.386 s]
[INFO] jetlinks-onenet .................................... SUCCESS [  6.444 s]
[INFO] jetlinks-opc-ua .................................... SUCCESS [ 11.776 s]
[INFO] jetlinks-aliyun-bridge-gateway ..................... SUCCESS [  8.857 s]
[INFO] jetlinks-dueros .................................... SUCCESS [  7.087 s]
[INFO] jetlinks-media ..................................... SUCCESS [ 13.981 s]
[INFO] network-card-manager ............................... SUCCESS [  9.961 s]
[INFO] jetlinks-modbus .................................... SUCCESS [ 12.166 s]
[INFO] jetlinks-standalone ................................ SUCCESS [  9.283 s]
//Maven的编译或打包信息内出现自定义项目的名称表明，该项目已被加入jetlinks-pro的多模块管理内
[INFO] my-demo ............................................ SUCCESS [  1.987 s]
[INFO] test-report ........................................ SUCCESS [  9.504 s]
[INFO] jetlinks-pro ....................................... SUCCESS [  0.953 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 02:48 min (Wall Clock)
[INFO] Finished at: 2022-11-29T14:05:27+08:00
[INFO] Final Memory: 451M/3453M
[INFO] ------------------------------------------------------------------------

Process finished with exit code 0
```

</div>

#### 常见问题

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
<p>Q：如何将自定义的接口加入swagger扫描并在API配置中显示出来？</p>
<p>A：在平台的<a>application.yml</a>文件内swagger下声明该项目扫描路径。</p></div>




```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
  group-configs:
    - group: 设备接入相关接口
      packages-to-scan:
        - org.jetlinks.pro.network.manager.web
        - org.jetlinks.pro.device.web
      paths-to-match:
        - /gateway/**
        - /network/**
        - /protocol/**
    - group: 系统管理相关接口
      packages-to-scan:
        - org.jetlinks.pro.auth
        - org.hswebframework.web.system.authorization.defaults.webflux
        - org.hswebframework.web.file
        - org.hswebframework.web.authorization.basic.web
        - org.jetlinks.pro.openapi.manager.web
        - org.jetlinks.pro.logging.controller
        - org.jetlinks.pro.tenant.web
    - group: 自定义接口
      packages-to-scan:
        - com.example.mydemo.web
```

效果图：
![效果图](./images/code-guide-1-7.png)




<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>


  <p>Q：自定义的接口不想被平台鉴权拦截？</p>
  <p>A：在类上或者方法上声明<span class="explanation-title font-weight">@Authorize(ignore = true)</span>。</p>
  <p>类上注解表明该类所有方法均不受鉴权拦截，方法上则仅限当前被注解的方法不受鉴权拦截。</p>

</div>

### 自定义模块中编写增删改查逻辑代码

#### 1.添加简单的Controller类、Service、Entity等

简单的业务系统目录结构如下图：

![自定义项目目录结构](./images/code-guide-1-6.png)



#### 2.web层：`org.example.mydemo.web.CustomController`

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
#### 3.Service层 ：`org.example.mydemo.service.CustomService`

  ```java
package org.example.mydemo.service;

 @Service
 public class CustomService extends GenericReactiveCrudService<CustomEntity,String> {
 }
  ```
#### 4. entity层 ：`org.example.mydemo.entity.CustomEntity`

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



#### 5. 在`org.jetlinks.pro.standalone`的启动类`JetLinksApplication`上加入自定义项目的扫描路径

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
#### 6. 核心类说明

| 类名                            | 说明                                              |
|-------------------------------|-------------------------------------------------|
| `ReactiveServiceCrudController<E, K>` | 实体CRUD操作的抽象接口，该接口继承了CRUD相关操作的接口，这些接口内封装了默认的实现方法,`<E> `实体类类型 ` <K> `主键类型 |
| `GenericEntity<PK>`             | 实体类需要继承该类，需要声明实体id数据类型                          |
| `GenericReactiveCrudService<E, K>`    | 业务层实体需要继承该类，该类有默认的crud方法的实现                     |

核心接口说明

`<E>` 实体类类型 `<K>` 主键类型

service层接口`org.hswebframework.web.crud.service.ReactiveCrudService<E, K>`

| 方法名 | 返回值 | 参数值 | 说明  |
|------- |--------|----------|------------|
|`getRepository()` |`ReactiveRepository<E, K>`|无|`响应式实体操作仓库`|

核心方法（响应式参数）

| 方法名                                                       | 返回值             | 参数值                                                       | 说明             |
| ------------------------------------------------------------ | ------------------ | ------------------------------------------------------------ | ---------------- |
| `findById(Mono<K> publisher)`                                | `Mono<E>`          | `Mono<K> publisher` publisher-ID流                           | 根据ID集合流查询 |
| `findById(Flux<K> publisher)`                                | `Flux<E>`          | `Flux<K> publisher ` publisher-ID流                          | 根据ID集合流查询 |
| `save(Publisher<E> entityPublisher)`                         | `Mono<SaveResult>` | `Publisher<E> entityPublisher` publisher-数据实体流          | 异步保存数据     |
| `updateById(K id, Mono<E> entityPublisher)`                  | `Mono<Integer>`    | K id-ID值 `Mono<E>` entityPublisher-更新数据流               | 异步保存数据     |
| `insertBatch(Publisher<? extends Collection<E>> entityPublisher)` | `Mono<Integer>`    | `Publisher<? extends Collection<E>>` entityPublisher -保存数据集合流 | 异步批量保存数据 |
| `insert(Publisher<E> entityPublisher)`                       | `Mono<Integer>`    | `Publisher<E>` entityPublisher-保存数据流                    | 异步保存数据     |
| `count(Mono<? extends QueryParamEntity> queryParamMono)`     | `Mono<Integer>`    | `Mono<? extends QueryParamEntity>` queryParamMono-查询参数流 | 查询记录数       |

核心方法（非响应式参数）

1、动态DSL接口相关

| 方法名           | 返回值              | 参数值 | 说明                                                         |
| ---------------- | ------------------- | ------ | ------------------------------------------------------------ |
| `createQuery()`  | `ReactiveQuery<E>`  | 无     | 创建一个DSL的动态查询接口,可使用DSL方式进行链式调用来构造动态查询条件 |
| `createUpdate()` | `ReactiveUpdate<E>` | 无     | 创建一个DSL动态更新接口,可使用DSL方式进行链式调用来构造动态更新条件 |
| `createDelete()` | `ReactiveDelete`    | 无     | 创建一个DSL动态删除接口,可使用DSL方式进行链式调用来构造动态删除条件 |

2、其他简单方法

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



#### DSL语法相关

##### 1、核心接口`org.hswebframework.ezorm.rdb.mapping.ReactiveUpdate`

| 方法名                                                       | 返回值              | 参数值                                                       | 说明                                                         |
| ------------------------------------------------------------ | ------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `execute()`                                                  | `Mono<Integer>`     | 无                                                           | 执行更新                                                     |
| `onExecute(BiFunction<ReactiveUpdate<E>, Mono<Integer>, Mono<Integer>> consumer)` | `ReactiveUpdate<E>` | `BiFunction<ReactiveUpdate<E>,</br>Mono<Integer>,<br/>Mono<Integer>> consumer-函数式接口参数)` | 执行结果处理器<br/>`ReactiveUpdate<E>`-实体数据<br/>`Mono<Integer>`-更新执行器<br/> `Mono<Integer>`-新的执行器 |



##### 2、核心接口`org.hswebframework.ezorm.rdb.mapping.ReactiveDelete`

| 方法名                                                       | 返回值           | 参数                                                         |                                                              |
| ------------------------------------------------------------ | ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `execute()`                                                  | `Mono<Integer>`  | 无                                                           | 执行异步删除,返回被删除的数据条数                            |
| `onExecute(BiFunction<ReactiveDelete, Mono<Integer>,Mono<Integer>> mapper)` | `ReactiveDelete` | `BiFunction<ReactiveDelete, Mono<Integer>,Mono<Integer>> mapper-函数式接口参数` | 执行结果处理器<br/>ReactiveDelete-当前动态删除接口<br/>`Mono<Integer>`-删除执行器<br/>`Mono<Integer>`-新的执行器 |



##### 3、核心接口`org.hswebframework.ezorm.rdb.mapping.ReactiveQuery<T>`

| 方法名       | 返回值          | 参数值 | 说明                                                        |
| ------------ | --------------- | ------ | ----------------------------------------------------------- |
| `fetch()`    | `Flux<T>`       | 无     | 执行查询并获取返回数据流,如果未查询到结果将返回Flux.empty() |
| `count()`    | `Mono<Integer>` | 无     | 执行count查询,并返回count查询结果.                          |
| `fetchOne()` | `Mono<T>`       | 无     | 执行查询并返回单个数据，T表示实体类                         |

##### 4、相关代码示例

`org.example.mydemo.web.CustomController`

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



#### 常见问题

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>

  <p>Q：启动报错，Field repository in org.hswebframework.web.crud.service.GenericReactiveCrudService required a bean of type 'org.hswebframework.ezorm.rdb.mapping.ReactiveRepository' that could not be found.？</p>
  <p>A：检查`org.jetlinks.pro.standalone`的启动类`JetLinksApplication`的 @EnableEasyormRepository注解的实体扫描包
  如：@EnableEasyormRepository({"org.jetlinks.pro.**.entity",<span class="explanation-title font-weight">"org.example.xxx.entity.*"</span>})</p>
<p>应改为：<br/><li>@EnableEasyormRepository({"org.jetlinks.pro.**.entity",<span class="explanation-title font-weight">"org.example.xxx.entity"</span>})</li>
    或者：<br/>
    <li>@EnableEasyormRepository({"org.jetlinks.pro.**.entity",<span class="explanation-title font-weight">"org.example.xxx.entity.**"</span>})</li>
   原因：<br/>
    //todo  待补充
</p>

</div>

<div class='explanation error'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>危险</span>
  </p>
  <p><li>响应式返回Mono&lt;Object&gt;或者Flux&lt;Object&gt;会报错，必须指出明确的返回类型。 </li></p>
  <p><li>响应式使用@RequestBody注解的参数必须使用流包裹。</li></p>
  <p><li>JetLinks从上至下使用全部使用响应式，基于JetLinks平台构建自己的业务代码时也请使用响应式。</li></p>
  <p><li>如果不会写响应式，建议最好独立项目不要与JetLinks混合使用非响应式，可能会导致项目出现阻塞。</li></p>

</div>




### 自定义模块如何使用es
#### 1、在自定义的项目中引入平台es的模块

##### 1.`properties`

```
<properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <jetlinks.version>1.20.0-SNAPSHOT</jetlinks.version>
</properties>
```

##### 2.引入依赖：

```java
  <dependency>
    <groupId>org.jetlinks.pro</groupId>
    <artifactId>elasticsearch-component</artifactId>
    <version>${jetlinks.version}</version>
    <scope>compile</scope>
  </dependency>
```

#### 2、创建自定义枚举类实现 `ElasticIndex`，定义es索引模板名称

代码：

  ```java
  package org.example.mydemo.enums;
  import org.jetlinks.pro.elastic.search.index.ElasticIndex;

  @Getter
  @AllArgsConstructor
  public enum CustomIndexEnum implements ElasticIndex {
  custom("custom");
  private String index;
  }
  ```
#### 3、在创建自定义es索引前，需要先创建es模板，启动程序时会根据如下配置代码创建es索引模板

代码：

   ```java
package org.example.mydemo.config;


@Component
@Setter
@AllArgsConstructor
public class Configurations implements CommandLineRunner {
    //引入indexManager
    private final ElasticSearchIndexManager indexManager;

    @Override
    public void run(String... args) throws Exception {
        indexManager.putIndex(
                //设置es自定义模板名称
                new DefaultElasticSearchIndexMetadata(CustomIndexEnum.custom.getIndex())
                        //添加自定义模板属性
                        .addProperty("device_id", new StringType())
                        .addProperty("type",  new StringType())
                        .addProperty("timestamp", new DateTimeType())
        ).subscribe();
    }
}
   ```

#### 4、 在controller层引入`ElasticSearchService`服务

在`org.example.mydemo.web.CustomController`中引入`elasticSearchService`

```java
 private final ElasticSearchService elasticSearchService;
```

#### 5、`ElasticSearchService`的核心方法

| 核心方法                                                     | 返回值                | 参数                                                         | 描述           |
| ------------------------------------------------------------ | --------------------- | ------------------------------------------------------------ | -------------- |
| `queryPager(String[] index, QueryParam queryParam, Function<Map<String, Object>, T> mapper)` | `Mono<PagerResult<T>` | String[] index-索引数组<br/> QueryParam queryParam-查询参数</br> `Function<Map<String, Object>, T>` mapper-函数式参数，传入Map，返回T | 分页查询数据   |
| `queryPager(String index, QueryParam queryParam, Function<Map<String, Object>, T> mapper)` | `Flux<T>`             | String index-索引<br/> QueryParam queryParam-查询参数</br> `Function<Map<String, Object>, T>` mapper-函数式参数，传入Map，返回T | 查询数据       |
| `query(String[] index, QueryParam queryParam, Function<Map<String, Object>, T> mapper)` | `Flux<T>`             | String[] index-索引数组<br/> QueryParam queryParam-查询参数</br> `Function<Map<String, Object>, T>` mapper-函数式参数，传入Map，返回T | 查询数据       |
| `multiQuery(String[] index, Collection<QueryParam> queryParam, Function<Map<String, Object>, T> mapper)` | `Flux<T>`             | String[] index-索引数组<br/> `Collection<QueryParam> `queryParam-查询参数集合</br> Function<Map<String, Object>, T> mapper-函数式参数，传入Map，返回T | 查询数据       |
| `count(String[] index, QueryParam queryParam)`               | `Mono<Long>`          | String[] index-索引数组<br/> QueryParam queryParam-查询参数  | 查询数据记录数 |
| `delete(String index, QueryParam queryParam)`                | `Mono<Long>`          | String index-索引<br/> QueryParam queryParam-删除数据参数    | 删除数据记录   |
| `commit(String index, T payload)`                            | `Mono<Void>`          | String index-索引<br/> T payload-提交的数据                  | 提交数据       |
| `commit(String index, Collection<T> payload)`                | `Mono<Void>`          | String index-索引<br/> `Collection<T>` payload-提交的数据集合 | 提交数据       |
| `commit(String index, Publisher<T> data)`                    | `Mono<Void>`          | String index-索引<br/> `Publisher<T>` data-提交的数据流      | 提交数据       |
| `save(String index, T payload)`                              | `Mono<Void>`          | String index-索引<br/> T payload-保存的数据                  | 保存数据       |
| `save(String index, Collection<T> payload)`                  | `Mono<Void>`          | String index-索引<br/> `Collection<T>` payload-保存的数据集合 | 保存数据       |
| `save(String index, Publisher<T> data)`                      | `Mono<Void>`          | String index-索引<br/> `Publisher<T>` data-保存的数据流      | 保存数据       |

#### 6、es相关简单代码示例

```java
package org.example.mydemo.web;



@RestController
@RequestMapping("/demo")
@Getter
@AllArgsConstructor
@Resource(id = "demo", name = "自定义接口")
@Tag(name = "自定义接口")
@Authorize(ignore = true)

public class CustomController implements ReactiveServiceCrudController<CustomEntity, String> {

    private final CustomService customService;
    //引入es
    private final ElasticSearchService elasticSearchService;

    /**
     * 将参数实体内容存储到es中
     * @param entity 存储参数实体
     * @return
     */
    @PostMapping("/save")
    @QueryAction
    public Mono<CustomEntity> saveData(@RequestBody CustomEntity entity) {
        //保存到数据库
        return customService.insert(entity)
                .filter(number -> number > 0)
                //将成功保存到数据库的数据存储到自定义的es索引中
                .flatMap(number -> elasticSearchService
                        .save(CustomIndexEnum.custom.getIndex(), entity)
                        .thenReturn(entity)
                );
    }

    /**
     *  根据参数，查询索引中的记录数
     * @param _index  索引名称
     * @param queryParam  查询参数
     * @return
     */
    @PostMapping("/count/{_index}")
    @QueryAction
    public Mono<Long> countData(@PathVariable String _index, @Parameter QueryParamEntity queryParam) {
        return elasticSearchService.count(_index, queryParam);
    }
    
    /**
     * 根据参数，分页查询索引中的数据
     * @param _index 索引名称
     * @param queryParam 查询参数
     * @return
     */
    @PostMapping("/{_index}/query")
    @QueryAction
    public Mono<PagerResult<CustomEntity>> queryPagerData(@PathVariable String _index, QueryParamEntity queryParam) {
     //从es中得到的查询结果会存入map，被自定义实体类(CustomEntity)的of方法转换，返回给调用者
        return elasticSearchService
                .queryPager(_index, queryParam, map -> CustomEntity.of(map));
    }

    @Override
    public ReactiveCrudService<CustomEntity, String> getService() {
        return customService;
    }
}

```

`CustomEntity`的`of`方法

```java
    /**
     * 使用JSONObject的parseObject的方法将传入参数转为 CustomEntity对象
     * @param map  传入参数
     * @return
     */
    public static CustomEntity of(Map<String, Object> map) {
        return  JSONObject.parseObject(JSONObject.toJSONString(map), CustomEntity.class);
    }
```

#### 常见问题

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
<p>Q：成功创建模板和索引，无法存入数据？</p>
<p>A：查看自定义模板的属性类型是否和存入数据的数据类型一致</p></div>



### 自定义模块如何引入消息总线

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
   EventBus是一个基于发布者/订阅者模式的事件总线框架。发布者/订阅者模式，也就是观察者模式，其定义了对象之间的一种一对多的依赖关系
</div>

#### 1.在`web`的`org.example.mydemo.web.CustomController`新建方法

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

#### 2.在自定义的`mydemo`下，新建`org.example.mydemo.event.CustomEventHandler`类，在`consumeMessage()`方法中，使用`eventBus`实现订阅

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



### 自定义模块如何引入使用redis缓存

#### 1.前置操作

##### 1.在自定义的pom文件中引入reids依赖

```java
    <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-redis</artifactId>
    </dependency>
```

##### 2.在自定义模块`org.example.mydemo.web.CustomController`引入`redis`

```java
以下redis二选一进行使用
//引入redis：选项一
    private final ReactiveRedisOperations<String, String> redis;
// 引入redis：选项二
    private final ReactiveRedisTemplate<Object,Object> redis;
```

#### 2.相关代码示例

##### 1.将数据存入`redis`:自定义模块的web层：`org.example.mydemo.web.CustomController`

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

事件订阅：`org.example.mydemo.event.CustomEventHandler`

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

##### 2.从redis取数据，用stream进行分组返回

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

### 常见问题

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
<p>Q：引入redis报错：No qualifying bean of type org.springframework.data.redis.core.ReactiveRedisTemplate available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}？</p>
<p>A：查看redis的泛型，在平台是否有注入：如，org.jetlinks.community.standalone.configuration配置类中注入了范型事Object，Object的redis</p>
</div>



### 自定义模块如何分组查询产品设备信息

#### 1.在自定义模块中新建`entity`类

`org.example.mydemo.entity.CustomProductEntity`

```java
@Setter
@Getter
@Comment("自定义产品表")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "custom_prodct_entity", indexes = {
        @Index(name = "idx_productId", columnList = "id,product_id")
})
public class CustomProductEntity extends GenericEntity<String> implements RecordCreationEntity, RecordModifierEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getId(){
        return super.getId();
    }

    @Column(name = "product_id")
    @Comment("产品id")
    private String productId;

    @Column(name = "product_name")
    @Comment("产品名称")
    private String productName;

    @Column(name="device_type")
    @Comment("产品类型")
    private String deviceType;

    @Column
    @Comment("状态")
    private Integer state;

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

`org.example.mydemo.entity.CustomDevcieEntity`

```java
@Setter
@Getter
@Comment("自定义设备表")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "custom_device_entity", indexes = {
        @Index(name = "idx_deviceId", columnList = "id,device_id")
})
public class CustomDevcieEntity extends GenericEntity<String> implements RecordCreationEntity, RecordModifierEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getId() {
        return super.getId();
    }

    @Column(name = "device_id")
    @Comment("设备id")
    private String deviceId;


    @Column(name = "device_name")
    @Comment("设备名称")
    private String deviceName;


    @Column(name = "product_id")
    @Comment("产品ID")
    private String productId;

    @Column(name = "product_name")
    @Comment("产品名称")
    private String productName;

    @Column
    @Comment("状态")
    @DefaultValue("notActive")
    private String state;

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

#### 2.在自定义模块中新建`service`类

`org.example.mydemo.service.CustomProductService`

```java
@Service
public class CustomProductService extends GenericReactiveCrudService<CustomProductEntity,String> {
}
```

`org.example.mydemo.service.CustomDeviceService`

```java
@Service
public class CustomDeviceService extends GenericReactiveCrudService<CustomDevcieEntity,String> {
}
```

#### 3.在自定义模块中新建`controller`类

`org.example.mydemo.web.CustomProDeviceController`

```java
@Getter
@RestController
@AllArgsConstructor
@Authorize(ignore = true)
@RequestMapping("/pro/device")
public class CustomProDeviceController implements ReactiveServiceCrudController {
    private static List<CustomDevcieEntity> list = new ArrayList<>();

    //引入产品service
    private CustomProductService customProductService;
    //引入设备service
    private CustomDeviceService customDeviceService;

    //引入sql执行器
    private DefaultR2dbcExecutor sqlExecutor;

    /**
     * 添加产品
     *
     * @param customProductEntity 产品数据
     * @return
     */

    @PostMapping("/save/pro/")
    public Mono<CustomProductEntity> savePro(@RequestBody CustomProductEntity customProductEntity) {
        return customProductService
                .insert(customProductEntity)
                .thenReturn(customProductEntity);
    }

    /**
     * 添加设备
     *
     * @param customDevcieEntity 设备数据
     * @return
     */

    @PostMapping("/save/device")
    public Mono<CustomDevcieEntity> saveDevice(@RequestBody CustomDevcieEntity customDevcieEntity) {
        return
                customDeviceService
                        .insert(customDevcieEntity)
                        .thenReturn(customDevcieEntity);
    }
    /**
     * 查询产品设备信息，并按照产品类型分组
     *
     * @return
     */
    @GetMapping("/get")
    public Mono<Object> getDeviceGroupByPro() {
        return customProductService
                .createQuery()
                .fetch()
                .map(list -> list.getProductId())
                .collect(Collectors.toList())
                .flatMap(proLists -> customDeviceService
                        .createQuery()
                        .in("product_id", proLists)
                        .fetch()
                        .collect(Collectors.toList())
                        .flatMap(deviceList -> Mono.just(deviceList.stream()
                                                                   .collect(Collectors.groupingBy(CustomDevcieEntity::getProductId))
                                                                   .values()))
                );
    }


    @Override
    public ReactiveCrudService<CustomDevcieEntity, String> getService() {
        return customDeviceService;
    }
}
```

### 自定义模块如何使用sqlExecutor

#### 1.在自定义的service：`org.example.mydemo.service.CustomDeviceService`中引入平台的`sqlExecutor`

```java
@Service
@AllArgsConstructor
public class CustomDeviceService extends GenericReactiveCrudService<CustomDevcieEntity,String> {
    //引入sql执行器
    private DefaultR2dbcExecutor sqlExecutor;
}
```

#### 2.`org.hswebframework.ezorm.rdb.executor.reactive.ReactiveSqlExecutor`:响应式SQL执行器,用于响应式执行SQL操作

### 核心方法
| 核心方法                                                     | 返回值          | 参数                                                         | 描述                                                         |
| ------------------------------------------------------------ | --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `update(Publisher<SqlRequest> request)`                      | `Mono<Integer>` | `Publisher<SqlRequest>` request-请求参数对象                 | 执行更新语句,支持 `update`,`delete`,`insert`                 |
| `execute(Publisher<SqlRequest> request)`                     | `Mono<Void>`    | `Publisher<SqlRequest>` request-请求参数对象                 | 执行SQL语句,忽略结果                                         |
| `select(Publisher<SqlRequest> request, ResultWrapper<E, ?> wrapper)` | `Mono<Void>`    | `Publisher<SqlRequest>` request-请求参数对象</br>`ResultWrapper<E, ?>` wrapper-返回结果集 | 执行查询语句,并使用同一个包装器包装返回结果,`<E>`-结果集类型 `<?>`-返回值类型 |

#### 3.相关代码示例：

```java
     /**
    * 使用sqlExector执行sql(insert)语句
    */
public Mono<Integer> saveData(CustomDevcieEntity customDevcieEntity) {
        return sqlExecutor.update(
        "insert into custom_device_entity(`id`,`device_id`,`device_name`,`product_id`,`product_name`) " +
        "values(#{id},#{deviceId},#{deviceName},#{productId},#{productName})",
        customDevcieEntity);
        }

      /**
      * 使用sqlExector执行sql(update)语句
      */
public Mono<Integer> updateData(CustomDevcieEntity customDevcieEntity) {
        return sqlExecutor.update(
        "update custom_device_entity " +
        "set `device_id`=#{deviceId},`device_name`=#{deviceName},`product_id`=#{productId},`product_name`=#{productName} where `id`=#{id}",
        customDevcieEntity);
        }
```








