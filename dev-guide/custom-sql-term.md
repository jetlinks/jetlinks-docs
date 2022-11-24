# 自定义通用SQL条件

平台使用可拓展的方式进行SQL条件构造，如: `like`,`gt`,`lt`等。


## 平台内置SQL条件

<table class='table'>
        <thead>
            <tr>
              <td>termType</td>
              <td>SQL</td>
              <td>DSL</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>is 或者 eq</td>
            <td> = </td>
            <td>.is()</td>
            <td>等于</td>
          </tr>
          <tr>
            <td>not</td>
            <td> != </td>
            <td>.not()</td>
            <td>不等于</td>
          </tr>
          <tr>
            <td>gt</td>
            <td> > </td>
            <td>.gt()</td>
            <td>大于</td>
          </tr>
          <tr>
            <td>lt</td>
            <td> < </td>
            <td>.lt()</td>
            <td>小于</td>
          </tr>
          <tr>
            <td>gte</td>
            <td> >= </td>
            <td>.gte()</td>
            <td>大于等于</td>
          </tr>
          <tr>
            <td>lte</td>
            <td> <= </td>
            <td>.lte()</td>
            <td>小于等于</td>
          </tr>
          <tr>
            <td>like</td>
            <td> like </td>
            <td>.like()</td>
            <td>模糊匹配，需要自己将value拼接%</td>
          </tr>
          <tr>
            <td>nlike</td>
            <td> not like </td>
            <td>.notLike()</td>
            <td>同like</td>
          </tr>
          <tr>
            <td>in</td>
            <td> in </td>
            <td>.in()</td>
            <td>值可以为以下格式：1,2,3,4字符串以半角逗号分割。[1,2,3,4] 集合</td>
          </tr>
          <tr>
            <td>nin</td>
            <td> not in </td>
            <td>.notIn()</td>
            <td>同in</td>
          </tr>
          <tr>
            <td>isnull</td>
            <td> is null </td>
            <td>.isNull()</td>
            <td>为null</td>
          </tr>
          <tr>
            <td>notnull</td>
            <td> not null </td>
            <td>.notNull()</td>
            <td>不为null</td>
          </tr>
          <tr>
            <td>empty</td>
            <td> = '' </td>
            <td>.isEmpty()</td>
            <td>为空字符</td>
          </tr>
          <tr>
            <td>nempty</td>
            <td> !='' </td>
            <td>.notEmpty()</td>
            <td>不为空字符</td>
          </tr>
          <tr>
            <td>btw</td>
            <td> between </td>
            <td>.between()</td>
            <td>在之间</td>
          </tr>
          <tr>
            <td>nbtw</td>
            <td> not between </td>
            <td>.notBetween()</td>
            <td>不在之间</td>
          </tr>
          <tr>
            <td>dev-group</td>
            <td> exists(.....) </td>
            <td>.where("deviceId$dev-group", groupId)</td>
            <td>按设备分组查询(Pro)</td>
          </tr>
          <tr>
            <td>dev-same-group</td>
            <td> exists(.....) </td>
            <td>.where("deviceId$dev-same-group", anotherDeviceId)</td>
            <td>查询同一个分组的设备，如果要包含此设备则使用：deviceId$dev-same-group$contains (Pro)</td>
          </tr>
          <tr>
            <td>dev-tag</td>
            <td> exists(.....) </td>
            <td>.where("deviceId$dev-tag", "tagKey:tagValue")</td>
            <td>

按标签查询。支持格式：`key:value`，`[{"key":"tagKey","value":"tagValue"}]`

</td>
          </tr>
          <tr>
            <td>product-info</td>
            <td> exists(.....) </td>
            <td>.where("deviceId$product-info", "accessId is mqtt-server-gateway")</td>
            <td>

根据产品信息查询设备数据，值`value`中可拼接一个完整的条件`terms`

</td>
          </tr>
          <tr>
            <td>device-creator</td>
            <td> exists(.....) </td>
            <td>.where("id$device-creator", "1")</td>
            <td>

根据设备创建者id查询。类似根据创建者ID查询的实现，详情见抽象类`AssetsCreatorTermFragmentBuilder`以及子类。

</td>
          </tr>
          <tr>
            <td>dev-latest</td>
            <td> exists(.....) </td>
            <td>.where("id$dev-latest$productId", "temp > 10")</td>
            <td>

根据设备最新的数据来查询设备或者与设备关联的数据。如: 查询温度大于30的设备列表

</td>
          </tr>
        </tbody>
      </table>

## 自定义SQL条件构造器

新建一个类，继承`AbstractTermFragmentBuilder`。注解`@Component`注入到spring。

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
        .addSql("exists(select 1 from custom_table t where t.device_id =",columnFullName," and t.value = ?)")
        .addParameter(term.getValue());
   }

}

```

### 在后端通用CRUD中使用

```java

repository.createQuery().where("deviceId$custom","1234").fetch();

```

### 在前端通用查询条件中使用

```js

{
 "where": "deviceId custom 1234"
}

```

或者

```js

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

