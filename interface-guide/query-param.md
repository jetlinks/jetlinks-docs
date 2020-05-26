# 公共查询参数
查询参数对象在各查询接口中频繁使用，本文将对此对象进行说明。

各属性如下表所示:

名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
paging | boolean | true | 是否启用分页
firstPageIndex | Integer | 0 | 第一页索引，默认为0
pageIndex | Integer  | 0 | 第几页
pageSize | Integer  | 25 | 每页显示记录条数，默认为25
sorts | List&#60;Sort&#62; |  | 用于排序的字段的集合，Sort对象在下面表格单独说明
terms | List&#60;Term&#62;  |  | 条件集合，Term对象在下面表格单独说明
where | String  | deviceId=test | 查询条件表达式,和terms二选一
includes | Set&#60;String&#62;  | ["name","id"] | 指定要查询的字段
excludes | Set&#60;String&#62;  | ["state","createTime"] | 指定不查询的字段

## Sort
Sort对象各属性如下表所示：

名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
order | String | asc | 顺序
name | String | createTime | 字段名
type | String  | long | 类型

## Term
Term对象各属性如下表所示：

名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
column | String | deviceId | 字段
value | Object | 23123213 | 值
type | String  | and | 链接类型 and或者or
termType | String  | eq | 条件类型，详细内容如下表
terms | List&#60;Term&#62; |  | 嵌套的条件

### TermType
TermType各条件如下所示：

类型值       |  条件
-------------- | ------------- 
eq |  ==
not |  !=
like | like 
nlike |  not like
gt | &#62; 
lt |  &#60;
gte |  &#62;=
lte |  &#60;=
in | in 
nin |  not in
empty |  =''
nempty |  !=''
isnull |  is null
notnull |  not null
btw | between
nbtw | not between

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