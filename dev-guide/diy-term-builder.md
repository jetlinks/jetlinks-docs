### 自定义SQL条件构造器

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    需要自定义某些SQL语句时可以使用自定义SQL条件构造器
</div>

新建一个类，继承`AbstractTermFragmentBuilder`。使用注解`@Component`将创建的类注入到spring容器中。

```java
@Component
public class CustomTermBuilder extends AbstractTermFragmentBuilder{

    public CustomTermBuilder(){
        //custom 为条件标识,就像like一样
        super("custom","自定义查询条件");
    }

   public SqlFragments createFragments( String columnFullName,  //列全名,查询条件对应的列名
                                        RDBColumnMetadata column,  //列元数据
                                        Term term){ //条件
        
        return PrepareSqlFragments.of()
        //添加自定义的SQL语句
        .addSql("exists(select 1 from custom_table t where t.device_id =",columnFullName," and t.value = ?)")
        //自定义语句中的?值
        .addParameter(term.getValue());
   }
}
```

在后端通用crud使用

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        语句<code>repository.createQuery()</code>相当于构建了<code>SELECT 全列名 FROM custom_table</code></p>
</div>


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
第一个参数<code>deviceId$custom</code>左侧<code>deviceId</code>表示的是先前自定义语句中传入的<code>columnFullName</code>，表示列的全名如<code>custom_table.deviceId</code>。右侧<code>custom</code>表示使用哪一个条件，此处为使用自定义的<code>custom</code>来构造SQL语句。第二个参数<code>"1234"</code>表示的为自定义中的<code>term.getValue()</code>，即为条件值。</p>
</div>



```java
/**
 * 此处构建的语句相当于
 * SELECT * FROM custom_table WHERE EXISTS
 * (SELECT 1 FROM custom_table t WHERE t.device_id = custom_table.device_id AND t.value = '1234')
 * 注：此处由于篇幅原因使用*代替所有字段，实际查询的为该表所有字段的全列名
 */
repository.createQuery().where("deviceId$custom","1234").fetch();
```

在前端通用查询条件中使用

```json
{
 "where": "deviceId custom 1234"
}
```

或者

```json
{
 "terms":[
     {
         "column":"deviceId",
         "termType":"custom",
         "value":"1234"
     }
 ]
}
```
