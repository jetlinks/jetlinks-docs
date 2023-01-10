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

<p>1. <a href='/dev-guide/diy-term-builder.html#创建自定义sql片段类'>创建自定义SQL片段类</a></p>

<p>2. <a href='/dev-guide/diy-term-builder.html#在后端通用crud使用'>在后端通用crud使用</a></p>

<p>3. <a href='/dev-guide/diy-term-builder.html#在前端通用查询条件中使用'>在前端通用查询条件中使用</a></p>

<p>4. <a href='/dev-guide/diy-term-builder.html#多条件使用'>多条件使用</a></p>



## 问题指引

<table>
<tr>
    <td><a href="/dev-guide/diy-term-builder.html#在后端使用自定义sql条件时未生效">在后端使用自定义sql条件时未生效</a></td>
    <td><a href="/dev-guide/diy-term-builder.html#如何查看构建的自定义SQL语句">如何查看构建的完整的自定义SQL语句</a></td>
    </tr>
</table>



## 示例

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        以下内容以查询产品id为1且名称包含<code>x94vr</code>的所有设备为例，其中假设产品表为<code>dev_product</code>，设备表为<code>dev_device_instance</code>
    </p>
</div>




## 创建自定义SQL片段类


新建一个类，继承`AbstractTermFragmentBuilder`。使用注解`@Component`将创建的类注入到spring容器中。

```java
@Component
public class CustomSearchDeviceTerm extends AbstractTermFragmentBuilder {
    public CustomSearchDeviceTerm() {
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
                .addSql("EXISTS ( SELECT 1 FROM dev_product p WHERE p.id = ",columnFullName," AND p.id = ? )")
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
        语句<code>deviceService.createQuery()</code>相当于构建了<code>SELECT 全列名 FROM 表名</code>
    </p>
</div>



<p>
第一个where方法中<code>productId$myDevice</code>左侧<code>productId</code>表示的为自定义语句中传入的<code>columnFullName</code>，即列的全名<code>device.productId</code>。右侧<code>myDevice</code>表示使用哪一个条件，此处使用自定义的<code>myDevice</code>来构造SQL语句。第二个参数<code>my_product_id</code>表示的为自定义中的<code>term.getValue()</code>，即条件值，此处表示为<code>dev_device_instance</code>表中的productId</p>

```java
@RestController
@RequestMapping("/custom")
public class CustomController { 
    //注意使用的service需要为对应的实体类，否则在使用时可能对应不上字段
    @Autowired
    private  LocalDeviceInstanceService deviceService;
    
    /**
     * 此处构建的语句相当于
     * SELECT * 
     * FROM
     * dev_device_instance d 
     * WHERE
     * EXISTS ( SELECT 1 FROM dev_product p WHERE p."id" = d.product_id AND p."id" = 'my_product_id' )	
     * AND ddi."name" LIKE '%t%' ;
     * 注：此处由于篇幅原因使用*代替所有字段，实际查询的为该表所有字段的全列名
     */
    @GetMapping("/devices")
    public Flux<DeviceInstanceEntity> custom(){
        return deviceService.createQuery()
                    .where("productId$myDevice", "my_product_id")                         
                    .where("name$like", "%x94vr%")                       
                    .fetch();
    }
}
```



## 在前端通用查询条件中使用

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



## 多条件使用

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        在自定义SQL片段时并不是只能传入单个参数，您可以构建多个参数并使用
    </p>
</div>

使用多个选项的情况，同样的，先创建一个类继承`AbstractTermFragmentBuilder`并注入到容器中

```java
@Component
public class MultiOptionTerm extends AbstractTermFragmentBuilder {
    public MultiOptionTerm() {
        super("multi-option-term","自定义多条选项查询");
    }

    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {

        PrepareSqlFragments sqlFragments = PrepareSqlFragments.of();

        //获取传入的options
        List<String> options = term.getOptions();

        sqlFragments.addSql("exists(select 1 from ",getTableName("dev_device_instance",column)," d where d.id like ? and d.id = ", columnFullName)
            .addParameter(term.getValue());

        if (options.size() > 0) {
            //设定第一个传入的option为设备的状态
            String state = options.get(0);
            if ("offline".equals(state) || "online".equals(state) || "notActive".equals(state)) {
                sqlFragments.addSql("and d.state = ?")
                            .addParameter(state);
            }
        }

        if (options.size() > 2){
            //设定第二个option为操作符
            String operation = options.get(1);
            //设定第三个option为当前设备的创建时间
            String createTime = options.get(2);
            if (createTime.matches("[0-9]+") && operation.matches("[<=>]")){
                sqlFragments.addSql("and d.create_time",operation," ? ")
                    .addParameter(Long.parseLong(createTime));
            }
        }
        sqlFragments.addSql(")");
        return sqlFragments;
    }
}
```

在后端使用

```java
@RestController
@RequestMapping("/custom")
public class CustomController { 
    //注意使用的service需要为对应的实体类，否则在使用时可能对应不上字段
    @Autowired
    private  LocalDeviceInstanceService deviceService;
    
    @GetMapping("/multiOption")
    public Flux<DeviceInstanceEntity> multiOption(){
        //查询设备状态为离线且创建时间早于1673252491511的并且设备id包含device的设备
        return service.createQuery()
                      .where("id$multi-option-term$offline$<$1673252491511","%device%")
                      .fetch();
    }
}
```

<br>

当然也可以在自定义SQL片段时包含多个传入值

```java
@Component
public class MultiValueTerm extends AbstractTermFragmentBuilder {
    public MultiValueTerm() {
        super("multi-value-term", "自定义多传入值查询");
    }

    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        //尝试转换条件值为List,如果值为字符串则按,分割
        List<Object> values = convertList(column, term);

        PrepareSqlFragments sqlFragments = PrepareSqlFragments.of();
        sqlFragments.addSql("exists(select 1 from ", getTableName("dev_device_instance", column), " d where d.id = ", columnFullName);

        if (values.size() > 0) {
            sqlFragments.addSql("and d.id in(", values.stream().map(r -> "?").collect(Collectors.joining(",")), ")")
                        .addParameter(values);
        }
        sqlFragments.addSql(")");
        return sqlFragments;
    }
}
```

在后端使用

```java
@RestController
@RequestMapping("/custom")
public class CustomController {

    @Autowired
    private  LocalDeviceInstanceService service;

    @GetMapping("/multiValue/{ids}")
    public Flux<DeviceInstanceEntity> multiValue(@PathVariable String ids){
    	//查询传入的多个设备id值
        return service.createQuery()
                      .where("id$multi-value-term",ids)
                      .fetch();
    }
}
```

## 常见问题



### 在后端使用自定义SQL条件时未生效

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
    <p>
        Q：在后端使用自定义的SQL条件进行crud时未生效
    </p>
    <p>
        A：在使用某一个<code>service</code>进行查询时，确认该<code>service</code>继承的<code>GenericReactiveCrudService</code>所带泛型中的第一个参数实体类中有与传入的列名相对应的字段，否则会出现使用自定义SQL语句失效的情况
    </p>
</div>



### 如何查看构建的自定义SQL语句

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>
    <p>
        Q：如何查看构建的自定义SQL语句
    </p>
    <p>
        在<code>detlinks-standalone</code>模块中的<code>application.yml</code>中将配置<code>logging.level.org.hswebframework</code>设置为<code>debug</code>，也可以在重写的<code>createFragments</code>方法中将最后组成的SQL片段打印日志查看
    </p>
</div>

