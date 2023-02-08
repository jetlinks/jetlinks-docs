# 自定义模块如何使用elasticsearch

## 指导介绍

  <p>1. <a href="/dev-guide/custom-use-es.html#自定义项目引入平台elasticsearch模块" >自定义的项目引入平台elasticsearch模块</a></p>
  <p>2. <a href="/dev-guide/custom-use-es.html#构建elasticsearch索引模板" >构建elasticsearch索引模板</a></p>
  <p>3. <a href="/dev-guide/custom-use-es.html#使用平台elasticsearch" >使用平台elasticsearch</a></p>

## 问题指引

<table>
  <tr>
   <td>
    <a href="/dev-guide/custom-use-es.html#成功创建模板和索引-无法存入数据" >成功创建模板和索引-无法存入数据</a>
   </td>
  </tr>

</table>

## 自定义项目引入平台elasticsearch模块

 1.`properties`

```
<properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <jetlinks.version>1.20.0-SNAPSHOT</jetlinks.version>
</properties>
```

 2.引入依赖：

```java
  <dependency>
<groupId>org.jetlinks.pro</groupId>
<artifactId>elasticsearch-component</artifactId>
<version>${jetlinks.version}</version>
<scope>compile</scope>
</dependency>
```

## 构建elasticsearch索引模板

 1.构建枚举类，定义索引模板名称

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

 2.创建配置类，程序启动时会根据以下配置创建索引模板

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
                        .addProperty("type", new StringType())
                        .addProperty("timestamp", new DateTimeType())
        ).subscribe();
    }
}
   ```

## 使用平台elasticsearch

 1.在controller层引入`ElasticSearchService`服务

在`org.example.mydemo.web.CustomController`中引入`elasticSearchService`

```java
 private final ElasticSearchService elasticSearchService;
```

 2.es相关简单代码示例

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
public static CustomEntity of(Map<String, Object> map){
        return JSONObject.parseObject(JSONObject.toJSONString(map),CustomEntity.class);
        }
```

## `ElasticSearchService`的核心方法

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

## 常见问题

#### 成功创建模板和索引，无法存入数据

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
<p>Q：成功创建模板和索引，无法存入数据？</p>
<p>A：查看自定义模板的属性类型是否和存入数据的数据类型一致</p></div>
