# 如何在协议包里面使用平台的业务方法

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    在自定义协议包开发过程中，根据自己的需要，使用JetLinks平台提供的业务方法获取数据
</div>

## 指导介绍

  <p>1.<a href="/dev-guide/jetlinks-protocol-use-business-method.
html#在协议包pom文件中引入elasticsearch依赖并获取容器中的elasticsearch">在协议包pom文件中引入elasticsearch依赖并获取容器中的elasticsearch</a> </p>
  <p>2.<a href="/dev-guide/jetlinks-protocol-use-business-method.
html#在协议包中构建elasticsearch查询条件并调用API查询数据">在协议包中构建elasticsearch查询条件并调用API查询数据</a></p>

## 问题指引

<table>
   <tr>
   <td>
     <a href="/dev-guide/jetlinks-protocol-use-business-method.html#如何确定es的索引名称">如何确定es的索引名称</a>
   </td>
   <td>
     <a href="/dev-guide/jetlinks-protocol-use-business-method.html#如何在协议包封装自定义查询条件">如何在协议包封装自定义查询条件</a>
   </td>
   </tr>
  <tr>
   <td>
     <a href="/dev-guide/jetlinks-protocol-use-business-method.html#程序打包运行时-提示无法找到es">程序打包运行时，提示无法找到es</a>
   </td>
   <td>
     <a href="/dev-guide/jetlinks-protocol-use-business-method.html#无法使用spring注解引入es-注入为null">无法使用spring注解引入es，注入为null</a>
   </td>
   </tr>
  <tr>
   <td>
     <a href="/dev-guide/jetlinks-protocol-use-business-method.html#es只能查询10000条数据">es只能查询10000条数据的问题</a>
   </td>
   </tr>
</table>
  

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>举例说明</span>
  </p>
    在协议包中查询设备属性es历史数据
</div>

## 在协议包pom文件中引入elasticsearch依赖并获取容器中的elasticsearch

 1.在自己的pom.xml文件中引入es依赖

```java
<dependency>
  <groupId>org.jetlinks.pro</groupId>
  <artifactId>elasticsearch-component</artifactId>
  <version>1.20.0-SNAPSHOT</version>
  <scope>compile</scope> 
</dependency>
```

 2.在ProtocolSupportProvider的实现类中使用Api引入es

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   协议包是一个独立的项目，不能直接使用@Autowired注入JetLinks-pro的es，可以由getService()方式从容器中获取，然后由构造函数传入
</div>
平台getService(类.class) Api底层是使用的applicationContext.getBean("bean 名称")方式

```java
        //获取相关Bean
        ReactiveElasticSearchService reactiveElasticSearchService=context
                .getService(ReactiveElasticSearchService.class).get();
        //使用构造函数传入Bean
        CustomMqttDeviceMessageCodec codec=new CustomMqttDeviceMessageCodec(DefaultTransport.MQTT,reactiveElasticSearchService);
```

 3.在DeviceMessageCodec的实现类中创建构造函数引入es

``` java
public CustomMqttDeviceMessageCodec(Transport transport,ReactiveElasticSearchService reactiveElasticSearchService) {
        this.reactiveElasticSearchService=reactiveElasticSearchService;
        this.transport = transport;
    }
```

## 在协议包中构建elasticsearch查询条件并调用API查询数据
 使用DeviceMessageCodec的实现类的decode方法构建相关代码

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

## 常见问题

#### 如何确定es的索引名称
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
  <p>Q:如何确定当前设备上报历史属性数据，在es中的存储索引名称？</p>
  <p>A:平台设备的属性数据,默认命名规则为："properties_".concat(productId),更多时序库相关命名规则，参见平台：<code>org.jetlinks.pro.device.timeseries.
DeviceTimeSeriesMetric</code>接口</p>
  

</div>

#### 如何在协议包封装自定义查询条件
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>
  <p>Q:如何封装查询条件？</p>
  <p>A:使用QueryParamEntity封装DSL语法条件</p>

</div>

#### 程序打包运行时，提示无法找到es
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题3</span>
  </p>
  <p>Q:程序打包运行时，提示无法找到es</p>
  <p>A:使用maven install命令重新将程序打包后在尝试</p>

</div>

#### 无法使用spring注解引入es，注入为null
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题4</span>
  </p>
  <p>Q:程序执行时，ReactiveElasticSearchService为null</p>
  <p>A:在<code>org.jetlinks.core.spi</code>的实现类的<code>create(ServiceContext context)方法中,
使用context.getService(类.class).get()</code>方式获取组件进行注入
  </p>

</div>

#### ES只能查询10000条数据
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题5</span>
  </p>
  <p>Q:es官方默认限制索引查询最多只能查询10000条数据，查询第10001条数据开始就会报错：<code>Result window is too large, from + size must be less than or 
equal to</code></p>
  <p>A:解决方案：在kibana中执行,解除索引最大查询数的限制.</p>

```java
put 索引名称/_settings
	{
	"index.max_result_window":200000
	}
```
</div>


