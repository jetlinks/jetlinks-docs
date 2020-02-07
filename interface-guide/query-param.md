# 公共查询参数
QueryParam查询参数对象在各查询接口中频繁使用，本文将对此对象进行说明。

各属性如下表所示：

名称       | 类型 | 示例值 | 描述
-------------- | ------------- | ------------- | ------------- 
paging | boolean | true | 是否启用分页
firstPageIndex | Integer | 0 | 第一页索引，默认为0
pageIndex | Integer  | 0 | 第几页
pageSize | Integer  | 25 | 每页显示记录条数，默认为25
sorts | List&#60;Sort&#62; |  | 用于排序的字段的集合，Sort对象在下面表格单独说明
terms | List&#60;Term&#62;  |  | 条件集合，Term对象在下面表格单独说明
includes | Set&#60;String&#62;  | ["name","id"] | 指定要处理的字段
excludes | Set&#60;String&#62;  | ["state","createTime"] | 指定不处理的字段

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
type | Type  | and | 链接类型，Type枚举包含or、and值
termType | String  | eq | 条件类型，详细内容如下表
options | List&#60;String&#62;  |  | 自定义选项,用于在某些自定义的termType中进行自定义生产sql的规则.可通过column进行设置如: name$like$reverse$func-concat,则column为name,termType为like,options为[reverse,func-concat]
termType | String  | eq | 条件类型，TermType接口提供了多种条件类型，详细内容如下表
terms | List&#60;Term&#62; |  | 嵌套的条件

### TermType
TermType各条件如下所示：

条件       | 类型值 
-------------- | ------------- 
== | eq 
!= | not 
like | like 
not like | nlike 
&#62; | gt 
&#60; | lt 
&#62;= | gte 
&#60;= | lte 
in | in 
not in | nin 
='' | empty 
!='' | nempty 
is null | isnull 
not null | notnull 
between | btw
not between | nbtw
