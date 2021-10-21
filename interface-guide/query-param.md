# 动态查询参数
查询参数对象在各查询接口中频繁使用，本文将对此对象进行说明。

各属性如下表所示:

| 名称           | 类型                | 示例值                 | 描述                                             |
| -------------- | ------------------- | ---------------------- | ------------------------------------------------ |
| paging         | boolean             | true                   | 是否启用分页                                     |
| firstPageIndex | Integer             | 0                      | 第一页索引，默认为0                              |
| pageIndex      | Integer             | 0                      | 第几页                                           |
| pageSize       | Integer             | 25                     | 每页显示记录条数，默认为25                       |
| sorts          | List&#60;Sort&#62;  |                        | 用于排序的字段的集合，Sort对象在下面表格单独说明 |
| terms          | List&#60;Term&#62;  |                        | 条件集合，Term对象在下面表格单独说明             |
| where          | String              | deviceId=test          | 查询条件表达式,和terms二选一                     |
| includes       | Set&#60;String&#62; | ["name","id"]          | 指定要查询的字段                                 |
| excludes       | Set&#60;String&#62; | ["state","createTime"] | 指定不查询的字段                                 |

## Sort
Sort对象各属性如下表所示：

| 名称  | 类型   | 示例值     | 描述   |
| ----- | ------ | ---------- | ------ |
| order | String | asc        | 顺序   |
| name  | String | createTime | 字段名 |
| type  | String | long       | 类型   |

## Term
Term对象各属性如下表所示：

| 名称     | 类型               | 示例值   | 描述                     |
| -------- | ------------------ | -------- | ------------------------ |
| column   | String             | deviceId | 字段                     |
| value    | Object             | 23123213 | 值                       |
| type     | String             | and      | 链接类型 and或者or       |
| termType | String             | eq       | 条件类型，详细内容如下表 |
| terms    | List&#60;Term&#62; |          | 嵌套的条件               |

### TermType
TermType各条件如下所示：

| 类型值         | 条件                                                                                                                                 |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| eq             | ==                                                                                                                                   |
| not            | !=                                                                                                                                   |
| like           | like                                                                                                                                 |
| nlike          | not like                                                                                                                             |
| gt             | &#62;                                                                                                                                |
| lt             | &#60;                                                                                                                                |
| gte            | &#62;=                                                                                                                               |
| lte            | &#60;=                                                                                                                               |
| in             | in                                                                                                                                   |
| nin            | not in                                                                                                                               |
| empty          | =''                                                                                                                                  |
| nempty         | !=''                                                                                                                                 |
| isnull         | is null                                                                                                                              |
| notnull        | not null                                                                                                                             |
| btw            | between                                                                                                                              |
| nbtw           | not between                                                                                                                          |
| dev-group      | 按设备分组查询对应设备数据,例查询`testgroup`分组下的设备: *deviceId* dev-group `testgroup`                                           |
| dev-group-tree | 查询设备分组及子分组对应设备数据,例查询`testgroup`分组及子分组下的设备: *deviceId* dev-group-tree `testgroup`                        |
| dev-latest     | 根据设备最新属性查询设备数据,例查询查询`demo-device`产品下最新数据temp大于10的设备: *deviceId$dev-latest$demo-device* is `'temp>10'` |
| dev-same-group | 查询相同设备下的数据,例查询与设备ID`deviceId0001`相同分组下的设备: *deviceId* dev-same-group `deviceId0001`                          |
| dev-tag        | 根据设备标签查询设备数据,例查询标签key为`tagKey`值为`tagValue`的设备:  *deviceId*  dev-tag `tagKey:tagValue`                         |
| dev-prod-cat   | 根据产品分类查询设备数据,例查询产品分类为`categoryid`的设备: *deviceId* dev-prod-cat `categoryid`                                    |

::: tip 说明

平台内置了一些自定义通用条件，以方便进行一些特殊的查询需求。
以上表格中`dev-*`的条件为设备相关的自定义条件,举例中使用的是动态查询条件中的`where`表达式,在实际使用中
也可以通过动态查询条件中的`terms`属性进行指定.

以 *deviceId$dev-latest$demo-device* is 'temp>10'为例, 表示的含义:

* deviceId 表示要查询的字段,等同于`Term.column=deviceId`
* $dev-latest 表示使用自定义条件`dev-latest`,等同于`Term.termType=dev-latest`
* $demo-device 表示自定义条件需要的其他选项,等同于`Term.options=[demo-device]`
* 'temp>10' 表示条件值为`temp>10`(注意单引号起转义作用),等同于`Term.value=temp>10`
 
:::

## 示例

GET请求时:

```js
//简单条件
http://[url]:[port]/api/_query?pageSize=20&where=name like %张三%

//复杂条件
http://[url]:[port]/api/_query?pageSize=20&terms[0].column=name&terms[0].value=%张三%&terms[0].termType=like
```

POST请求时:

```js

//简单条件
{
    "pageSize":20,
    "pageIndex":0,
    "where":"name like %张三%"
}

//复杂条件
{
    "pageSize":20,
    "pageIndex":0,
    "terms":[
        [
            "column":"name",
            "value":"%张三%",
            "termType":"like"
        ]
    ]
}
```

## 滚动分页

部分存储方式支持滚动分页,能实现更快的查询性能,使用说明:

在执行分页查询时,可以根据返回分页结果中的:

```js
{
    "scroll":true, //为true时表示支持滚动分页
    "scrollId":"", //滚动ID,需要在下次查询时携带此ID,为空则表示为最后一页
    "total":100,    // 数据总数
    "data":[        // 数据内容

    ]
}
```

在执行动态查询时,通过设置滚动ID到查询上下文中进行下一页查询:

```js
{
  "context":{
    "scrollId":"" //上一页查询返回的scrollId
  }
 "terms":[],
 "total":65535 // 设置此值后,后端将不进行count操作,可以提升查询速度,滚动分页时建议设置为任意值.
}
```

::: tip 注意
当查询条件变化后,需要重置滚动ID为空.
:::