# 添加自定义存储策略

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

<p>当平台提供的存储策略不满足自己的需求时，可以选择自行开发.</p>

</div>

##### 操作步骤

1. 实现`ThingsDataRepositoryStrategy`接口或者继承`AbstractThingDataRepositoryStrategy`类。重写接口或父类的方法

<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

<p>推荐使用继承 <span class='explanation-title font-weight'>AbstractThingDataRepositoryStrategy</span>的方式。
AbstractThingDataRepositoryStrategy继承CacheSaveOperationsStrategy类，CacheSaveOperationsStrategy类在执行存储操作时会将
数据库操作对象存放至内存，在调用存储方法过程中减少上下文对象的创建。
</p>

</div>

```java

@AllArgsConstructor
public class CustomRowModeStrategy extends AbstractThingDataRepositoryStrategy {

    private final ThingsRegistry registry;
    //自定义存储帮助类
    private final CustomHelper helper;

    @Override
    public String getId() {
        return "custom-row";
    }

    @Override
    public String getName() {
        return "自定义-行式存储";
    }

    @Override
    public SaveOperations createOpsForSave(OperationsContext context) {
        //创建自定义SaveOperations类，返回自定义SaveOperations对象
        return new CustomColumnModeSaveOperations(
                registry,
                context.getMetricBuilder(),
                context.getSettings(),
                helper);
    }

    @Override
    protected QueryOperations createForQuery(String thingType, String templateId, String thingId, OperationsContext context) {
        //创建自定义QueryOperations类，返回自定义QueryOperations对象
        return new CustomColumnModeQueryOperations(
                thingType,
                templateId,
                thingId,
                context.getMetricBuilder(),
                context.getSettings(),
                registry,
                helper);
    }

    @Override
    protected DDLOperations createForDDL(String thingType, String templateId, String thingId, OperationsContext context) {
        //创建自定义DDLOperations类，返回自定义DDLOperations对象
        return new CustomColumnModeQueryOperations(
                thingType,
                templateId,
                thingId,
                context.getMetricBuilder(),
                context.getSettings(),
                registry,
                helper);
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}


```

2. 创建自定义存储策略的操作对象(上一步内相关注释)

```java
//创建自定义SaveOperations
//伪代码，根据自己的需求在此处实现save操作实现
public class CustomRowModeSaveOperations extends RowModeSaveOperationsBase {

    private final CustomHelper helper;


    public CustomRowModeSaveOperations(ThingsRegistry registry,
                                       MetricBuilder metricBuilder,
                                       DataSettings settings,
                                       CustomHelper helper) {
        super(registry, metricBuilder, settings);
        this.helper = helper;
    }


    @Override
    protected Map<String, Object> createRowPropertyData(String id, long timestamp, ThingMessage message, PropertyMetadata property, Object value) {
        Map<String, Object> values = super.createRowPropertyData(id, timestamp, message, property, value);
        //可能存在不同的数据库对于数据类型的处理差异，values为物模型相关信息如属性id、设备id、时间戳、数据id等。
        //此处可以做一些针对不同存储时数据类型兼容或转换的处理
        
        return values;
    }

    @Override
    protected Map<String, Object> createEventData(ThingEventMessage message, ThingMetadata metadata) {
        //伪代码，不同的数据库对于数据类型的处理存在差异，可以自行在此处处理事件内的数据类型
        return doSomeThing(message, metadata);
    }

    @Override
    protected Mono<Void> doSave(String metric, TimeSeriesData data) {
        return helper.doSave(metric, data);
    }

    @Override
    protected Mono<Void> doSave(String metric, Flux<TimeSeriesData> data) {
        return helper.doSave(metric, data);
    }
}


```

```java
//创建自定义QueryOperations
//伪代码，根据自己的需求在此处实现query操作实现
public class CustomModeQueryOperations extends RowModeQueryOperationsBase {

    private final CustomHelper helper;

    public CustomModeQueryOperations(String thingType,
                                     String thingTemplateId,
                                     String thingId,
                                     MetricBuilder metricBuilder,
                                     DataSettings settings,
                                     ThingsRegistry registry,
                                     CustomHelper helper) {
        super(thingType, thingTemplateId, thingId, metricBuilder, settings, registry);
        this.helper = helper;
    }

    @Override
    protected Flux<TimeSeriesData> doQuery(String metric, Query<?, QueryParamEntity> query) {
        return helper.doQuery(metric, query.getParam());
    }

    @Override
    protected <T> Mono<PagerResult<T>> doQueryPage(String metric, Query<?, QueryParamEntity> query, Function<TimeSeriesData, T> mapper) {
        return helper.doQueryPager(metric, query.getParam(), mapper);
    }

    @Override
    protected Flux<AggregationData> doAggregation(String metric,
                                                  AggregationRequest request,
                                                  AggregationContext context) {
        //此处伪代码，需自行在aggregationQuery内编写聚合查询实现
        return aggregationQuery();
    }

    @Override
    protected Flux<ThingPropertyDetail> queryEachProperty(@Nonnull String metric,
                                                          @Nonnull Query<?, QueryParamEntity> query,
                                                          @Nonnull ThingMetadata metadata,
                                                          @Nonnull Map<String, PropertyMetadata> properties) {
        //此处伪代码，需自行在doSomeThings内编写查询实现
        return doSomeThings();
    }

}

```

```java
//创建自定义DDLOperations
//伪代码，根据自己的需求在此处实现ddl操作实现
public class CustomRowModeDDLOperations extends RowModeDDLOperationsBase {

    private final CustomHelper helper;

    public CustomRowModeDDLOperations(String thingType,
                                      String templateId,
                                      String thingId,
                                      DataSettings settings,
                                      MetricBuilder metricBuilder,
                                      CustomHelper helper) {
        super(thingType, templateId, thingId, settings, metricBuilder);
        this.helper = helper;
    }

    //不存储的数据类型
    static Set<String> notSaveColumns = new HashSet<>(Arrays.asList(
            ThingsDataConstants.COLUMN_PROPERTY_OBJECT_VALUE,
            ThingsDataConstants.COLUMN_PROPERTY_ARRAY_VALUE,
            ThingsDataConstants.COLUMN_LOG_TYPE,
            ThingsDataConstants.COLUMN_PROPERTY_TYPE
    ));

    @Override
    protected Mono<Void> register(MetricType metricType, String metric, List<PropertyMetadata> properties) {
        //根据物模型在时序库内创建表结构或映射
        switch (metricType) {
            case properties:
                return helper
                        .createTable(metric, properties
                                        .stream()
                                        //过滤不存储的数据类型
                                        .filter(prop -> !notSaveColumns.contains(prop.getId()))
                                        .collect(Collectors.toList()),
                                metricBuilder.getThingIdProperty(),
                                ThingsDataConstants.COLUMN_PROPERTY_ID,
                                ThingsDataConstants.COLUMN_TIMESTAMP);

            case log:
                return helper
                        .createTable(metric, properties,
                                metricBuilder.getThingIdProperty(),
                                ThingsDataConstants.COLUMN_TIMESTAMP);
            case event:
                if (settings.getEvent().eventIsAllInOne()) {
                    return helper
                            .createTable(metric, properties,
                                    metricBuilder.getThingIdProperty(),
                                    ThingsDataConstants.COLUMN_EVENT_ID,
                                    ThingsDataConstants.COLUMN_TIMESTAMP);

                }
                return helper
                        .createTable(metric, properties,
                                metricBuilder.getThingIdProperty(),
                                ThingsDataConstants.COLUMN_TIMESTAMP);
        }
        return Mono.empty();
    }

    @Override
    protected Mono<Void> reload(MetricType metricType, String metric, List<PropertyMetadata> properties) {
        return helper.reload(metric);
    }
}

```

3. 自定义`CustomHelper`存储帮助类。该类需要自行根据自定义存储的类型进行对接。

#### 核心类及接口

##### `AbstractThingDataRepositoryStrategy`

| 方法名                                                                                             | 参数  | 返回值 | 说明                   |
|-------------------------------------------------------------------------------------------------|-----|--------|----------------------|
| `createOpsForSave(OperationsContext context)`                                                   | 见下表 |`SaveOperations` | 创建save操作类型的操作对象      |
| `createForQuery(String thingType,String templateId,String thingId, OperationsContext context)`  | -   |`QueryOperations` | 创建query操作类型的操作对象     |
| `createForDDL(String thingType,String templateId,String thingId, OperationsContext context)`    | -   |`DDLOperations` | 创建数据库ddl操作类型的操作对象    |
| `opsForThing(String thingType,String templateId,String thingId, OperationsContext context)`     | -   |`ThingOperations` | 创建物（设备）相关操作类型的操作对象   |
| `opsForTemplate(String thingType,String templateId,String thingId, OperationsContext context)`  | -   |`TemplateOperations` | 创建物模版（产品）数据操作类型的操作对象 |

| 参数  | 参数含义                                                                  |
|-----|-----------------------------------------------------------------------|
|OperationsContext| 物存储操作对象                                                               |
|thingType| 物类型：通常情况下是`device`,详见`ThingsBridgingDeviceDataService`类约定查询时序库给定的默认值。 |
|templateId| 物模板id:通常情况下是产品id                                                      |
|thingId| 物id：通常情况下是设备id                                                        |
