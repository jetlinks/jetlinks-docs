# 存储策略选择

从1.5.0开始，企业版支持在产品中配置数据存储策略。
不同的策略使用不同的数据存储方式来保存设备数据。

## 存储策略

### 默认-行式存储

这是系统默认情况下使用的存储方案，使用elasticsearch存储设备数据。
每一个属性值都保存为一条索引记录。典型应用场景：设备每次只会上报一部分属性，
以及支持读取部分属性数据的时候。

**优点**: 灵活，几乎满足任意场景下的属性数据存储。

**缺点**: 设备属性个数较多时，数据量指数增长，可能性能较低。

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

1. 在确定好存储方案后，尽量不要跨类型进行修改，如将行式存储修改为列式存储，可能会导致数据结构错乱。
2. 在创建物模型时，请根据存储策略判断好是否支持此类型以及ID格式。

</div>

##### 索引结构

1. 属性索引模版: `properties_{productId}_template`

mappings:
```js
{
 "properties" : {
          "geoValue" : { // 地理位置值,物模型配置的类型为地理位置时,此字段有值
            "type" : "geo_point"
          },
          "productId" : { // 产品ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "objectValue" : { // 结构体值,物模型配置的类型为结构体时,此字段有值
            "type" : "nested"
          },
          "type" : { // 物模型中配置的属性类型
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timeValue" : { //时间类型的值
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "deviceId" : { //设备ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //平台记录数据的时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "property" : { //属性ID,与物模型属性ID一致
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "numberValue" : { //物模型中属性类型为数字相关类型时,此字段有值
            "type" : "double"
          },
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "value" : { //通用值,所有类型都转为字符串
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        }
}
```

2. 事件索引模版: `event_{productId}_{eventId}_template`

mappings:
```js
{
   "properties" : {
          "productId" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //创建时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
        
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "value" : {   //如果物模型中类型不是结构体(对象类型),则会有此字段并且类型与物模型类型相匹配
            "ignore_above" : 512,
            "type" : "keyword"
          },
          //如果物模型中类型是结构体(对象类型),会根据结构体配置的字段添加到索引中.


          "deviceId" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        } 
}
```

3. 设备日志索引模版: `device_log_{productId}_template`

mappings:
```js
{
    "properties" : {
          "productId" : { //产品ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //创建时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "type" : { //日志类型
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "deviceId" : { //设备ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "content" : { //日志内容
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        }
}
```

### 默认-列式存储

使用elasticsearch存储设备数据。
一个属性作为一列，一条属性消息作为一条索引记录进行存储，适合设备每次都上报所有的属性值的场景。

**优点**: 在属性个数较多，并且设备每次都会上报全部属性时，性能更高。

**缺点**: 设备必须上报全部属性。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

默认情况下：读取属性、修改属性指令都必须返回全部属性数据。
如果无法返回全部数据，可以在消息中设置头`partialProperties`。如: `message.addHeader("partialProperties",true)`，
用来标记是返回的部分数据。平台在保存数据前，将获取之前保存的其他属性然后一起保存。（使用此功能将降低系统的吞吐量）。

</div>

索引结构:

1. 属性索引模版: `properties_{productId}_template`

mappings:

```js
{
 "properties" : {
          "productId" : { // 产品ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "deviceId" : { //设备ID
            "ignore_above" : 512,
            "type" : "keyword"
          },
          "createTime" : { //平台记录数据的时间
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          },
          "id" : {
            "ignore_above" : 512,
            "type" : "keyword"
          },
          //根据物模型中配置的属性,一个属性对应一列
          //....

          "timestamp" : { //设备消息中的时间戳
            "format" : "epoch_millis||strict_date_hour_minute_second||strict_date_time||strict_date",
            "type" : "date"
          }
        }
}
```

2. 事件和日志与`默认-行式存储`一致

### InfluxDB-行式存储

与`默认-行式存储`行为一致，使用`influxdb`进行数据存储。

measurement说明:

1. 属性: `properties_{productId}`
2. 事件: `event_{productId}_{eventId}`
3. 日志: `device_log_{productId}`

### InfluxDB-列式存储

与`默认-列式存储`行为一致，使用`influxdb`进行数据存储。

measurement说明:

1. 属性: `properties_{productId}`
2. 事件: `event_{productId}_{eventId}`
3. 日志: `device_log_{productId}`

### TDEngine-列式存储

与`默认-列式存储`行为一致，使用`TDEngine`进行数据存储。

超级表说明:

1. 属性: `properties_{productId}`
2. 事件: `event_{productId}_{eventId}`
3. 日志: `device_log_{productId}`

### ClickHouse-行式存储

与`默认-行式存储`行为一致，使用`ClickHouse`进行数据存储。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

ClickHouse不支持大量并发查询，请根据实际情况选择。

</div>


### Cassandra-行式存储

与`默认-行式存储`行为一致，使用`Cassandra`进行数据存储。

分区说明:

1. 设备属性: (deviceId,property,partition)
2. 设备事件: (deviceId,partition)
3. 设备日志: (deviceId,partition)

partition规则:

通过`jetlinks.device.storage.cassandra.partition-interval=MONTHS`进行配置，
支持:`MINUTES`，`HOURS`，`DAYS`，`WEEKS`，`MONTHS`，`YEARS`，`FOREVER`。

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

1. 在查询条件没有设置时间的时候，默认只会查询最近2个分区周期内的数据。
   前端在查询时，建议默认指定时间范围，并且时间间隔不能与配置的分区周期相差太多，否则会影响查询性能.
2. Cassandra仅支持使用滚动分页，跳转分页深度过大时会降低查询速度。执行count的性能也较低，建议查询时，使用[滚动分页](./../interface-guide/query-param.md#滚动分页)。

</div>


## 自定义存储策略

在后台代码中实现`org.jetlinks.pro.things.data.ThingsDataRepositoryStrategy`接口，然后注入到`Spring`即可。  

添加的存储策略将被注册到`org.jetlinks.pro.things.data.DefaultThingsDataRepository`中，并使用`org.jetlinks.pro.things.data.ThingsDataCustomizer`自定义表名逻辑。

## 记录设备最新数据到数据库

`1.5.0企业版本`中增加了记录最新设备数据到数据库中，以便进行更丰富的统计查询等操作。

通过配置文件开启:
```yml
jetlinks:
  device:
     storage:
         enable-last-data-in-db: true #是否将设备最新到数据存储到数据库
```

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>警告</span>
  </p>

开启后，设备的属性、事件的最新值将记录到表`dev_lsg_{productId}`中。主键`id`为设备ID。
开启此功能可能降低系统的吞吐量。频繁写入数据库会加大IO的负荷。

</div>


#### 物模型与数据库表字段说明

1. 属性id即字段
2. 当事件类型为结构体类型时，字段为：`{eventId}_{结构体属性ID}`，否则为：`{eventId}_value`
3. 在发布产品时，会自动创建或者修改表结构

#### 数据类型说明

1. 数字类型统一为`number(32,物模型中配置的精度)`类型
2. 时间类型为`timestamp`
3. 结构体(object)和array为`Clob`
4. 设置了字符长度`expands.maxLength`大于`2048`时，为`Clob`
5. 其他为`varchar`

<div class='explanation error'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>危险</span>
  </p>

请勿频繁修改物模型类型，可能导致数据格式错误。

</div>

#### 根据最新的设备数据查询设备数量等操作

在任何设备相关等动态查询条件中，使用自定义条件`dev-latest`来进行查询。例如：
```js
{
"terms":[
   {
    "column":"id$dev-latest$demo-device" 
    ,"value":"temp1 > 10"  //查询表达式
   }
 ]
}
```

`id$dev-latest$demo-device`说明：
**id**: 表示使用查询主表的id作为设备id
**dev-latest**: 表示使用自定义条件`dev-latest`，此处为固定值
**demo-device**: 产品ID

`temp1 > 10`说明：

`temp1`最新值大于10。条件支持`=`，`>`，`<`，`>=`，`<=`，`in`，`btw`，`isnull`等通用条件，支持嵌套查询。如：
`temp1 > 10 and (temp2 >30 or state = 1)`

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

所有参数和查询条件都经过了处理，没有SQL注入问题。请放心使用。

</div>

