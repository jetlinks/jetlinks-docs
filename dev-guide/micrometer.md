# 平台统计和监控

原理: 平台利用[Micrometer](https://micrometer.io/)对相关功能进行监控和统计,
默认每隔30秒,上报相关的统计指标数据,并使用`TimeSeriesManager`进行存储(默认使用`Elasticsearch`实现). 
然后可通过聚合查询等操作查看统计数据.

## 使用

在平台内部使用`MeterRegistryManager`来获取一个监控指标(一个监控指标,对应一个`Elasticsearch`索引).
然后进行相应的统计操作,具体支持的统计请查看[Micrometer Doc](https://micrometer.io/docs/concepts)
例如:
```java
@Component
public class DeviceMessageMonitor{

  private final MeterRegistry registry;

  public DeviceMessageMonitor(MeterRegistryManager registryManager.){
     registry = registryManager.getMeterRegister("device_monotor");
  }

    @Subscribe("/device/*/*/online")
    public Mono<Void> incrementOnline(DeviceMessage msg) {
        registry
            .counter("online")
            .increment();
        return Mono.empty();
    }
}

```

## 自定义标签类型
micrometer只提供`String`类型的标签支持,平台进行了拓展,以支持自定义标签类型.

例如: 
```java
@Component
public class DeviceMeterRegistryCustomizer implements MeterRegistryCustomizer {
    @Override
    public void custom(String metric, MeterRegistrySettings settings) {
        //给device_metrics添加自定义标签
        if (Objects.equals(metric, "device_metrics")) {
            settings.addTag("tenantId", new ArrayType().elementType(StringType.GLOBAL));
            settings.addTag("members", new ArrayType().elementType(StringType.GLOBAL));
            settings.addTag("bindings", new ArrayType().elementType(StringType.GLOBAL));
        }
    }
}

```

## 设备统计
平台中已经对设备数据进行了统计,监控指标:`device_metrics`.

相关实现类: `DeviceMessageMeasurementProvider`,`DeviceStatusMeasurementProvider`.

默认开启记录`productId(产品ID)`,`tenantId(租户ID)`,`members(租户成员ID)`,`bindings(维度绑定信息)`标签来进行细粒度统计以实现数据权限控制.

注意：在产品数量较多,开启了租户,并且设备分布在不同的租户下可能导致监控数据量较大.如果不需要细粒度的统计,
可以通过启动参数关闭相关的标签记录:

```java
java -Djetlinks.device-message.meter.tags.disabled=productId,members
```

关闭后,将无法使用对应的条件查询统计数据.