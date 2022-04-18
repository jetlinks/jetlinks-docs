# 通用SQL条件

平台使用可拓展的方式进行SQL条件构造,如: `like`,`gt`,`lt`等。


## 平台内置SQL条件

| termType         | SQL                  | DSL                                                    | 说明                                                                                                    |
|------------------|----------------------|--------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| is 或者 eq         | =                    | \.is\(\)                                               | 等于                                                                                                    |
| not              | \!=                  | \.not\(\)                                              | 不等于                                                                                                   |
| gt               | >                    | \.gt\(\)                                               | 大于                                                                                                    |
| lt               | <                    | \.lt\(\)                                               | 小于                                                                                                    |
| gte              | >=                   | \.gte\(\)                                              | 大于等于                                                                                                  |
| lte              | <=                   | \.lte\(\)                                              | 小于等于                                                                                                  |
| like             | like                 | \.like\(\)                                             | 模糊匹配\. 需要自己将value拼接%\.                                                                                |
| nlike            | not like             | \.notLike\(\)                                          | 同like                                                                                                 |
| in               | in                   | \.in\(\)                                               |  值可以为以下格式:1,2,3,4 字符串以半角逗号分割\. \[1,2,3,4\] 集合\.|
| nin              | not in               | \.notIn\(\)                                            | 同in                                                                                                   |
| isnull           | is null              | \.isNull\(\)                                           | 为null                                                                                                 |
| notnull          | not null             | \.notNull\(\)                                          | 不为null                                                                                                |
| empty            | = ''                 | \.isEmpty\(\)                                          | 为空字符                                                                                                  |
| nempty           | \!=''                | \.notEmpty\(\)                                         | 不为字符                                                                                                  |
| btw              | between              | \.between\(\)                                          | 在之间                                                                                                   |
| nbtw            | not between          | \.notBetween                                           | 不在之间                                                                                                  |
| dev\-group       | exists\(\.\.\.\.\.\) | \.where\("deviceId$dev\-group",groupId\)               | 按设备分组查询 \(Pro\)                                                                                       |
| dev\-same\-group | exists\(\.\.\.\.\.\) | \.where\("deviceId$dev\-same\-group",anotherDeviceId\) | 查询同一个分组的设备，如果要包含此设备则使用: deviceId$dev\-same\-group$contains \(Pro\)                                    |
| dev\-tag         | exists\(\.\.\.\.\.\) | \.where\("deviceId$dev\-tag","tagKey:tagValue"\)       | 按标签查询,支持格式: `key:value`,`[{"key":"tagKey","value":"tagValue"}]`                                  |



## 自定义SQL条件构造器

新建一个类,继承`AbstractTermFragmentBuilder`,注解`@Component`注入到spring.

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

## 在后端通用CRUD中使用

```java

repository.createQuery().where("deviceId$custom","1234").fetch();

```

## 在前端通用查询条件中使用

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

