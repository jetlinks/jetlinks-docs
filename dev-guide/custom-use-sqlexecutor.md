# 自定义模块如何使用sqlexecutor

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  用户可以在JetLinks平台代码中使用原生sql操作数据库
</div>


## 指导介绍
  <p>1. <a href="/dev-guide/custom-use-sqlexecutor.html#引入平台sqlexecutor">引入平台sqlExecutor</a></p>
  <p>2. <a href="/dev-guide/custom-use-sqlexecutor.html#使用平台sqlexecutor" >使用平台sqlExecutor</a></p>
  <p>3. <a href="/dev-guide/custom-use-sqlexecutor.html#reactivesqlexecutor的核心方法说明" >reactivesqlexecutor的核心方法说明</a></p>
  <p>4. <a href="/dev-guide/custom-use-sqlexecutor.html#平台查询结果包装器大全" >平台查询结果包装器大全</a></p>

## 问题指引
<table>
   <tr>
     <td>
     <a href="/dev-guide/custom-use-sqlexecutor.html#自定义sql的参数在平台内部封装完整sql时参数值丢失-参数变为null">自定义sql的参数在平台内部封装完整sql时参数值丢失-参数变为null</a>
   </td>
   </tr>
</table>

## 引入平台sqlexecutor
 在自定义的service：`org.example.mydemo.service.CustomDeviceService`中引入平台的`sqlExecutor`
```java
@Service
@AllArgsConstructor
public class CustomDeviceService extends GenericReactiveCrudService<CustomDevcieEntity,String> {
    //引入sql执行器
    private DefaultR2dbcExecutor sqlExecutor;
}
```

## 使用平台sqlExecutor

```java
/**
     * 使用sqlExector执行sql(insert)语句
     * @param customDevcieEntity 传入参数
     * @return
     */
    public Mono<Integer> saveData(CustomDevcieEntity customDevcieEntity) {
        return sqlExecutor.update(
                "insert into custom_device_entity(`id`,`device_id`,`device_name`,`product_id`,`product_name`) " +
                        "values(#{id},#{deviceId},#{deviceName},#{productId},#{productName})",
                customDevcieEntity);
    }

    /**
     * 使用sqlExector执行sql(update)语句
     * @param customDevcieEntity 传入参数
     * @return
     */
    public Mono<Integer> updateData(CustomDevcieEntity customDevcieEntity) {
        return sqlExecutor.update(
                "update custom_device_entity " +
                        "set `device_id`=#{deviceId},`device_name`=#{deviceName},`product_id`=#{productId},`product_name`=#{productName} where `id`=#{id}",
                customDevcieEntity);
    }


   /**
     * 使用sqlExector执行sql(select)语句
     * 2个参数，平台会使用ResultWrappers.map()作为包装器
     *
     * @param customDevcieEntity 传入参数
     * @return
     */
    public Flux<Map<String, Object>> getData(CustomDevcieEntity customDevcieEntity) {
        return sqlExecutor
            .select("select `id`,`device_id`,`device_name`,`product_id`,`product_name` from custom_device_entity where id=#{id}"
                   , customDevcieEntity)
            ;
    }

    /**
     * 使用sqlExector执行sql(select)语句
     * 3个参数，第3个参数为指定的平台包装器
     *
     * @param customDevcieEntity 传入参数
     * @return
     */
    public Flux<Map<String, Object>> getData2(CustomDevcieEntity customDevcieEntity) {
        return sqlExecutor
            .select(SqlRequests
                        .of("select `id`,`device_id`,`device_name`,`product_id`,`product_name` from custom_device_entity where id=#{id}"
                            ,customDevcieEntity)
                            ,ResultWrappers.map())
            ;
    }
```



## ReactiveSqlExecutor的核心方法说明
 `org.hswebframework.ezorm.rdb.executor.reactive.ReactiveSqlExecutor`:响应式SQL执行器,用于响应式执行SQL操作

### 核心方法

| 核心方法                                                     | 返回值                      | 参数                                                         | 描述                                                         |
| ------------------------------------------------------------ | --------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `update(Publisher<SqlRequest> request)`                      | `Mono<Integer>`             | `Publisher<SqlRequest>` request-请求参数对象                 | 执行更新语句,支持 `update`,`delete`,`insert`                 |
| `execute(Publisher<SqlRequest> request)`                     | `Mono<Void>`                | `Publisher<SqlRequest>` request-请求参数对象                 | 执行SQL语句,忽略结果                                         |
| `select(Publisher<SqlRequest> request, ResultWrapper<E, ?> wrapper)` | `Mono<Void>`                | `Publisher<SqlRequest>` request-请求参数对象</br>`ResultWrapper<E, ?>` wrapper-返回结果集 | 执行查询语句,并使用同一个包装器包装返回结果,`<E>`-结果集类型 `<?>`-返回值类型 |
| `select(String sql, Object... args)`                         | `Flux<Map<String, Object>>` | String sql-sql语句</br>Object... args-sql参数                | 使用预编译执行查询SQL,并返回map结果                          |



##  平台查询结果包装器大全

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  ResultWrappers:通用查询结果包装器大全
</div>


### 核心方法

| 核心方法                                                     | 返回值                                                       | 参数                                                         | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `lowerCase(ResultWrapper<E, R> wrapper)`                     | `<E, R> ResultWrapper<E, R>`                                 | `ResultWrapper<E, R>` wrapper-下级包装器                     | 创建将列转为小写的包装器                                     |
| `map()`                                                      | `ResultWrapper<Map<String, Object>, Map<String, Object>>`    | 无                                                           | 将行转为Map的包装器,此包装器不具备收集能力,通常需要配合具备收集能力的包装器使用 |
| `mapList()`                                                  | `ResultWrapper<Map<String, Object>, List<Map<String, Object>>>` | 无                                                           | map集合结果包装器                                            |
| `mapStream()`                                                | `ResultWrapper<Map<String, Object>, Stream<Map<String, Object>>>` | 无                                                           | map流结果包装器                                              |
| `set(ResultWrapper<E, ?> wrapper)`                           | `<E> ResultWrapper<E, Set<E>>`                               | `ResultWrapper<E, ?> `wrapper-行包装器                       | Set结果包装器                                                |
| `stream(ResultWrapper<E, ?> wrapper)`                        | `<E> ResultWrapper<E, Stream<E>>`                            | `ResultWrapper<E, ?> `wrapper-流结果                         | 创建流结果包装器                                             |
| `singleMap()`                                                | `ResultWrapper<Map<String, Object>, Map<String, Object>>`    | 无                                                           | 单个map的结果包装器                                          |
| `list(ResultWrapper<E, ?> wrapper)`                          | `<E> ResultWrapper<E, List<E>>`                              | `ResultWrapper<E, ?>` wrapper-行包装器                       | 集合结果包装器,将所有行结果收集成一个集合                    |
| `optional(ResultWrapper<E, R> wrapper)`                      | `<E, R> ResultWrapper<E, Optional<R>>`                       | `ResultWrapper<E, ?>` wrapper-行包装器                       | 可选结果包装器                                               |
| `single(ResultWrapper<E, ?> wrapper)`                        | `<E> ResultWrapper<E, E>`                                    | `ResultWrapper<E, ?>` wrapper-行包装器                       | 单个结果包装器,结果可能为 `<code>null</code>`                |
| `column(String column, Decoder<R> decoder)`                  | `<R> ResultWrapper<R, R>`                                    | String column-列名</br>`Decoder<R>` decoder-解码器           | 创建单列结果包装器,只包装处理单个列的数据                    |
| `consumer(ResultWrapper<E, ?> wrapper, Consumer<E> consumer)` | `<R> ResultWrapper<R, R>`                                    | `ResultWrapper<E, ?>` wrapper-行结果包装器</br> `Consumer<E>` consumer-行结果消费者 | 创建不收集结果,只消费行结果的包装器                          |
| `consumer(ResultWrapper<E, ?> wrapper, Consumer<E> consumer, Runnable onCompleted)` | `<E> ResultWrapper<E, Integer> `                             | `ResultWrapper<E, ?>` wrapper-行结果包装器</br> `Consumer<E>` consumer-行结果消费者</br>Runnable onCompleted-当全部执行完成后执行的任务 | 创建不收集结果,只消费行结果的包装器,并支持全部消费完后,执行指定的任务 |
| `convert(ResultWrapper<E, R> wrapper, Function<R, C> converter)` | `<E, R, C> ResultWrapper<E, C>`                              | `ResultWrapper<E, R>` wrapper-原始包装器</br>` Function<R, C> `converter-结果转换器 | 创建转换结果的包装器                                         |

## <font id="5">常见问题</font>

### 自定义sql的参数在平台内部封装完整sql时参数值丢失，参数变为null
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
<p>Q：使用sqlExecutor时，业务层参数值存在，后续平台封装过程中参数值丢失？</p>
<p>A：传入参数值时按照JSON对象格式方式传递</p></div>