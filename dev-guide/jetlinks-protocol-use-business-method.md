### 如何在协议包里面使用平台的业务方法

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    在自定义协议包开发过程中，根据自己的需要，通过JetLinks平台提供的一些业务方法获取数据
</div>

#### 指导介绍

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <p><a href="#1">1. 在协议包中引入elasticsearch依赖并在与业务中使用elasticsearch</a> </p>
  <p><a href="#2">2. 在协议包的中构建elasticsearch查询条件和查询数据</a></p>
  <p><a href="#3">3. 常见问题</a></p>

</div>

### 使用场景：在协议包中查询设备属性es历史数据

### <font id="1">在协议包中引入elasticsearch依赖并在与业务中使用elasticsearch</font>

#### 1.在自己的pom.xml文件中引入es依赖

```java
  <dependency>
<groupId>org.jetlinks.pro</groupId>
<artifactId>elasticsearch-component</artifactId>
<version>1.20.0-SNAPSHOT</version>
<scope>compile</scope>
</dependency>
```

#### 2.在ProtocolSupportProvider的实现类中使用Api引入es

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   协议包和jetlinks-Pro不是同一个项目，不能直接使用@Autowired注入es，可以由getService()方式获取，然后由构造函数传入
</div>
平台getService(类.class) Api底层是使用的applicationContext.getBean("bean 名称")方式

```java
        //获取相关Bean
        ReactiveElasticSearchService reactiveElasticSearchService=context
                .getService(ReactiveElasticSearchService.class).get();
        //使用构造函数传入Bean
        CustomMqttDeviceMessageCodec codec=new CustomMqttDeviceMessageCodec(DefaultTransport.MQTT,reactiveElasticSearchService);
```

#### 3.在DeviceMessageCodec的实现类中创建构造函数引入es

``` java
public CustomMqttDeviceMessageCodec(Transport transport,ReactiveElasticSearchService reactiveElasticSearchService) {
        this.reactiveElasticSearchService=reactiveElasticSearchService;
        this.transport = transport;
    }
```

### <font id="2">在协议包的中构建elasticsearch查询条件和查询数据</font>

#### 使用DeviceMessageCodec的实现类的decode方法构建相关代码

```java
   @Nonnull
@Override
public Mono<DeviceMessage> decode(@Nonnull MessageDecodeContext context) {

        MqttMessage message = (MqttMessage) context.getMessage();
        /**
         * 如何查询当前设备的es历史数据
         */
        String[] topics = ResolveTopic(message.getTopic());
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

public static String[] ResolveTopic(String topic) {
        if (!(topic.startsWith("/"))) {
        topic = topic + "/";
        }
        String[] topicArr = topic.split("/");
        String[] topics = Arrays.copyOfRange(topicArr, 1, topicArr.length);
        return topics;
        }


```

### <font id="3">常见问题</font>

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
  <li>Q:如何确定当前设备上报历史属性数据，在es中的存储索引名称？</li>
  <li>A:平台设备的属性数据,默认命名规则为："properties_".concat(productId),更多时序库相关命名规则，参见平台：<code>org.jetlinks.pro.device.timeseries.
DeviceTimeSeriesMetric</code>接口</li>

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
  <li>Q:程序执行时，ReactiveElasticSearchService为null</li>
  <li>A:在<code>org.jetlinks.core.spi</code>的实现类的<code>create(ServiceContext context)方法中,
使用context.getService(类.class).get()</code>方式获取组件进行注入</li>

</div>




