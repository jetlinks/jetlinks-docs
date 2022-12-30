### 如何在协议包里面使用平台的业务方法

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    在自定义协议包开发过程中，根据自己的需要，通过JetLinks平台提供的一些业务方法获取数据
</div>
#### 使用示例

##### 在协议包中查询es历史数据

###### 1.在自己的pom.xml文件中引入es依赖

```
  <dependency>
    <groupId>org.jetlinks.pro</groupId>
    <artifactId>elasticsearch-component</artifactId>
    <version>1.20.0-SNAPSHOT</version>
    <scope>compile</scope>
  </dependency>
```

2.在DeviceMessageCodec的实现类中引入es

```
private ElasticSearchService elasticSearchService;
```

3.相关代码

```java
        /**
         * 如何查询当前设备的es历史数据
         */
        //构建查询条件
        QueryParamEntity param = new QueryParamEntity();
        ArrayList<Term> terms = new ArrayList<>();
        Term term = new Term();
        //查询字段
        term.and("_id", TermType.eq, "customDev");
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
        Mono<PagerResult<DeviceMessage>> result = elasticSearchService
            .queryPager("properties_" + customPro, param, map -> JSONObject.parseObject(JSONObject.toJSONString(map), DeviceMessage.class));
        System.out.println(result);
```



#### 常见问题

*对开发过程中出现的问题进行总结*

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
  <li>如何确定当前数据的索引名称？</li>
  <li>平台的属性数据默认以properties_开头+产品ID，</li>

</div>


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>
  <li>如何封装查询条件？</li>
  <li>使用QueryParamEntity封装DSL语法条件</li>

</div>




