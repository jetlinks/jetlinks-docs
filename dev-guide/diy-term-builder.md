# 自定义SQL条件构造器

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    需要自定义某些SQL语句时可以使用自定义SQL条件构造器
</div>


## 指导介绍

<p>1、<a href='/dev-guide/diy-term-builder.html#创建自定义sql片段类'>创建自定义SQL片段类</a></p>

2、<a href='/dev-guide/diy-term-builder.html#在后端通用crud使用'>在后端通用crud使用</a>



## 问题指引

<table>
<tr>
    <td><a href="/dev-guide/diy-term-builder.html#使用自定义sql条件时未生效">使用自定义sql条件时未生效</a></td>
    </tr>
</table>



## 示例

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        以下内容以查询产品id为1且名称包含<code>x94vr</code>的所有设备为例，其中假设产品表为<code>product</code>，设备表为			<code>device</code>
    </p>
</div>



## 创建自定义SQL片段类


新建一个类，继承`AbstractTermFragmentBuilder`。使用注解`@Component`将创建的类注入到spring容器中。

```java
@Component
public class CustomSearchDeviceBuilder extends AbstractTermFragmentBuilder {
    public CustomSearchDeviceBuilder() {
        //myDevice 为条件标识,就像like一样
        super("myDevice", "想要查询的符合条件的设备");
    }

    /**
     * @param columnFullName 列全名,如: schema.table
     * @param column         列对应的元数据
     * @param term           条件
     * @return 				 构建的Sql片段
     */
    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        return PrepareSqlFragments.of()
                .addSql("EXISTS ( SELECT 1 FROM product p WHERE p.id = ",columnFullName," AND p.id = ? )")
                .addParameter(term.getValue());
    }
}
```



## 在后端通用crud使用

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        语句<code>deviceService.createQuery()</code>相当于构建了<code>SELECT 全列名 FROM 表名</code></p>
</div>


<p>
第一个where方法中<code>productId$myDevice</code>左侧<code>productId</code>表示的为自定义语句中传入的<code>columnFullName</code>，即列的全名<code>device.productId</code>。右侧<code>myDevice</code>表示使用哪一个条件，此处使用自定义的<code>myDevice</code>来构造SQL语句。第二个参数<code>"1"</code>表示的为自定义中的<code>term.getValue()</code>，即条件值，此处表示为<code>device</code>表中的productId</p>


```java
/**
 * 此处构建的语句相当于
 * SELECT * 
 * FROM
 * device d 
 * WHERE
 * EXISTS ( SELECT 1 FROM product p WHERE p."id" = d.product_id AND p."id" = '1' )	
 * AND ddi."name" LIKE '%t%' ;
 * 注：此处由于篇幅原因使用*代替所有字段，实际查询的为该表所有字段的全列名
 */
deviceService.createQuery()
            .where("productId$myDevice", "1")                         
            .where("name$like", "%x94vr%")                       
            .fetch();
```

在前端通用查询条件中使用

```json
{
 "where": "productId myDevice 1"
}
```

或者

```json
{
 "terms":[
     {
         "column":"productId",
         "termType":"myDevice",
         "value":"1"
     }
 ]
}
```

## 常见问题

*对开发过程中出现的问题进行总结*



### 使用自定义SQL条件时未生效

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
    <p>
        Q：在使用自定义的SQL条件进行crud时未生效
    </p>
    <p>
        A：请确认传入的参数名与实体类中定义的数据库映射字段是否对应
    </p>
</div>

