### 如何在协议包里面使用平台的业务方法

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    在自定义协议包开发过程中，根据自己的需要，通过JetLinks平台提供的一些业务方法获取数据
</div>

##### 在协议包中查询设备属性es历史数据

###### 1.在自己的pom.xml文件中引入es依赖

```
  <dependency>
    <groupId>org.jetlinks.pro</groupId>
    <artifactId>elasticsearch-component</artifactId>
    <version>1.20.0-SNAPSHOT</version>
    <scope>compile</scope>
  </dependency>
```

2.在ProtocolSupportProvider的实现类中使用Api引入es


```java
//获取组件
        ReactiveElasticSearchService reactiveElasticSearchService = context
            .getService(ReactiveElasticSearchService.class).get();
        CustomMqttDeviceMessageCodec codec = new CustomMqttDeviceMessageCodec(DefaultTransport.MQTT, reactiveElasticSearchService);
```
3.在DeviceMessageCodec的实现类中创建构造函数引入es
```
public CustomMqttDeviceMessageCodec(Transport transport,ReactiveElasticSearchService reactiveElasticSearchService) {
        this.reactiveElasticSearchService=reactiveElasticSearchService;
        this.transport = transport;
    }
```

4.DeviceMessageCodec的实现类相关代码

```java
   public Mono<DeviceMessage> decode(@Nonnull MessageDecodeContext context) {

        MqttMessage message = (MqttMessage) context.getMessage();
        /**
         * 如何查询当前设备的es历史数据
         */
        String[] topics = TopicMessageCodec.ResolveTopic(message.getTopic());
        //构建查询条件
        QueryParamEntity param = new QueryParamEntity();
        ArrayList<Term> terms = new ArrayList<>();
        Term term = new Term();
        //查询字段
        term.and("_id", TermType.eq, topics[1]);
        term.and("name", TermType.eq, "online");
        term.and("timestamp", TermType.lte, System.currentTimeMillis());
        terms.add(term);
        //排序字段
        ArrayList<Sort> sorts = new ArrayList<>();
        Sort sort = new Sort();
        sort.setOrder("timestamp");
        //查询参数
        param.setTerms(terms);
        param.setSorts(sorts);

        Mono<PagerResult<Object>> result = reactiveElasticSearchService
        .queryPager("properties_" + topics[0], param, map -> JSONObject.toJSONString(map));
        System.out.println(result);
        return Mono.just(result).then(Mono.empty());

        }
```



#### 常见问题

*对开发过程中出现的问题进行总结*

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
  <li>Q:如何确定当前数据的索引名称？</li>
  <li>A:平台的属性数据,默认命名规则为：properties_+产品ID</li>

</div>


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>
  <li>Q:如何封装查询条件？</li>
  <li>A:使用QueryParamEntity封装DSL语法条件</li>

</div>

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题3</span>
  </p>
  <li>Q:程序打包运行时，提示无法找到es</li>
  <li>A:使用maven install命令重新将程序打包后在尝试</li>

</div>

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题4</span>
  </p>
  <li>Q:程序执行时，es为null</li>
  <li>A:在org.jetlinks.core.spi的实现类的create(ServiceContext context)中,
使用context.getService(类.class).get()方式获取组件进行注入</li>

</div>




