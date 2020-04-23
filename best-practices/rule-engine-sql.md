# 规则引擎-数据转发

规则引擎中可使用SQL来订阅消息网关中到数据,并将处理后的数据转发到指定的地方,如: 发送消息通知,推送到MQ等.
支持标准sql语法与拓展函数,支持聚合函数,窗口函数.

## 创建规则

TODO

## SQL例子

::: tip
聚合处理实时数据时,必须使用`interval`函数或者`_window`函数.
:::

当温度大于40度时,将数据转发到下一步.

```sql
select 
this.properties.temperature temperature,
this.deviceId deviceId
from
"/device/*/*/properties/**" -- 订阅所有设备的所有属性消息
where this.properties.temperature > 40
```

处理指定多个型号的设备数据 

```sql
select 
this.properties.temperature temperature,
this.deviceId deviceId
from
"/device/T0001/*/properties/**" -- 订阅T0001型号下的所有设备消息
where this.properties.temperature > 40
union all   -- 实时数据只能使用 union all
select 
this.properties.temperature temperature,
this.deviceId deviceId
from
"/device/T0002/*/properties/**" -- 订阅T0002型号下的所有设备消息
where this.properties.temperature > 42

```

计算每5分钟的温度平均值,当平均温度大于40度时,将数据转发到下一步.

```sql
select
avg(this.properties.temperature) temperature
from
"/device/*/*/properties/**" -- 订阅所有设备的所有属性消息
group by interval('5m')
having temperature > 40 --having 必须使用别名.
```

计算每10条数据为一个窗口,每2条数据滚动的平均值.

```text
[1,2,3,4,5,6,7,8,9,10]  第一组
[3,4,5,6,7,8,9,10,11,12] 第二组
[5,6,7,8,9,10,11,12,13,14] 第三组
```

```sql
select 
avg(this.properties.temperature) temperature
from
"/device/*/*/properties/**" -- 订阅所有设备的所有属性消息
group by _window(10,2)
having temperature > 40 --having 必须使用别名.
```

聚合统计平均值,并且提取聚合结果中的数据.

```sql
select ,
rows_to_array(idList) deviceIdList, --将[{deviceId:1},{deviceId:2}] 转为[1,2]
avgTemp,
from
(
   select
   collect_list((select this.deviceId deviceId)) idList, --聚合结果里的
   avg(temperature)                   avgTemp ,
   from "/device/*/*/properties/**" ,
   group by interval('1m') having avgTemp > 40
)

```

::: warn
SQL中的`this`表示主表当前的数据,如果存在嵌套属性的时候,必须指定`this`或者以表别名开头. 
如: `this.properties.temperature` ,写成: `properties.temperature`是无法获取到值到.
:::

## SQL支持列表

| 函数/表达式          | 用途          | 示例                                     | 说明                                                      |
|-----------------|-------------|----------------------------------------|---------------------------------------------------------|
| \+              | 加法运算        | temp\+10                               | 对应函数: math\.plus\(temp,10\)                             |
| \-              | 减法运算        | temp\-10                               | 对应函数: math\.sub\(temp,10\)                              |
| \*              | 乘法运算        | temp\*10                               | 对应函数: math\.mul\(temp,10\)                              |
| /               | 除法运算        | temp/10                                | 对应函数: math\.divi\(temp,10\)                             |
| %               | 取模运算        | temp%2                                 | 对应函数: math\.mod\(temp,2\)                               |
| &               | 位与运算        | val&3                                  | 对应函数: bit\_and\(val,3\)                                 |
| \\\|            | 位或运算        | val\\\|3                               | 对应函数: bit\_or\(val,3\)                                  |
| ^               | 异或运算        | val^3                                  | 对应函数: bit\_mutex\(val,3\)                               |
| <<              | 位左移运算       | val<<2                                 | 对应函数: bit\_left\_shift\(val,2\)                         |
| >>              | 位右移运算       | val>>2                                 | 对应函数: bit\_right\_shift\(val,2\)                        |
| \\\|\\\|        | 字符拼接        | val\\\|\\\|'度'                         | 对应函数: concat\(val,'度'\)                                 |
| avg             | 平均值         | avg\(val\)                             |                                                         |
| sum             | 合计值         | sum\(val\)                             |                                                         |
| count           | 总数          | count\(1\)                             |                                                         |
| max             | 最大值         | max\(val\)                             |                                                         |
| min             | 最小值         | min\(val\)                             |                                                         |
| >               | 大于          | val > 10                               |                                                         |
| <               | 小于          | val < 10                               |                                                         |
| =               | 等于          | val = 10                               |                                                         |
| \!=             | 不等于         | val \!=10                              | 等同于: <> ,如: val <> 10                                   |
| >=              | 大于等于        | val>=10                                |                                                         |
| <=              | 小于等于        | val<=10                                |                                                         |
| in              | 在\.\.之中     | val in \(1,2,3\)                       |                                                         |
| not in          | 不在\.\.之中    | val not in \(1,2,3\)                   |                                                         |
| like            | 模糊匹配        | name like 'a%'                         | not like 同理                                             |
| between         | 在之间         | val between 1 and 10                   |                                                         |
| now             | 当前时间        | now\(\)                                | 默认返回时间戳,可传入格式化参数\.                                      |
| date\_format    | 格式化日期       | date\_format\(now\(\),'yyyy\-MM\-dd'\) |                                                         |
| cast            | 转换类型        | cast\(val as boolean\)                 | 支持类型: string,boolean,int,double,float,date,decimal,long |
| interval        | 时间分组        | interval\('10s'\)                      | 分组函数,按时间分组                                              |
| \_window        | 窗口分组        | \_window\(10\)                         | 窗口,支持按数量和时间窗口                                           |
| collect\_list   | 聚合结果转为list  | collect\_list\(\(select deviceId\)\)   | 把聚合的的结果转为list                                           |
| rows\_to\_array | 将结果集转为单元素数组 | rows\_to\_array\(idList\)              | 把只有一个属性的结果集中的属性转为集合                                     |
| new\_map        | 创建一个map     | new\_map\('k1',v1,'k2',v2\)            |                                                         |
| new\_array      | 创建一个集合      | new\_array\(1,2,3,4\)                  |                                                         |
| math\.ceil       | 向上取整       | math\.ceil\(val\)                       |                                                         |
| math\.floor       | 向下取整       | math\.floor\(val\)                       |                                                         |
| math\.round       | 四舍五入       | math\.round\(val\)                       |                                                         |
| math\.log       | log运算       | math\.log\(val\)                       |                                                         |
| math\.sin      | 正弦      | math\.sin\(val\)                      |                                                         |
| math\.asin       | 反正弦          | math\.asin\(val\)                       |                                                         |
| math\.sinh      | 双曲正弦         | math\.sinh\(val\)                      |                                                         |
| math\.cos       | 余弦          | math\.cos\(val\)                       |                                                         |
| math\.acos      | 反余弦         | math\.acos\(val\)                      |                                                         |
| math\.cosh      | 双曲余弦        | math\.cosh\(val\)                      |                                                         |
| math\.tan       | 正切          | math\.tan\(val\)                       |                                                         |
| math\.atan      | 反正切         | math\.atan\(val\)                      |                                                         |
| math\.tanh      | 双曲正切         | math\.tanh\(val\)                      |                                                         |
