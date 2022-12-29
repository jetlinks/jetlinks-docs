# 协议包中使用Redis
## 应用场景
jetlinks支持用户向平台的Redis中存入自定义的参数，用户可以在协议包中获取Redis里的参数进行使用，获取方式有两种，一是使用`redis.opsForValue.get()`获取平台缓存数据，二是使用`deviceOperator.getconfig()`获取设备缓存的配置信息。
## 平台缓存


### 使用示例

1. 在协议包的pom文件中引入jar包


```markdown
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <version>2.5.11</version>
</dependency>
```


2. 定义消息编码解码器
```java
public class MqttDeviceMessageCodec implements DeviceMessageCodec {
    
    private final ReactiveRedisOperations<String, String> redis;

    public MqttDeviceMessageCodec(ReactiveRedisConnectionFactory redis) {
        this.redis = new ReactiveRedisTemplate<>(redis, RedisSerializationContext.string());
    }

    @Override
    public Transport getSupportTransport() {
        return DefaultTransport.MQTT;
    }

    @Nonnull
    @Override
    public Publisher<? extends Message> decode(@Nonnull MessageDecodeContext context) {
        String deviceId = context.getDevice().getDeviceId();
        MqttMessage message = (MqttMessage) context.getMessage();
        return redis
                //用设备id为key拿到redis中存入的值
                .opsForValue()
                .get(deviceId)
                .map(number -> JSON.parseObject(number, Integer.class))
                //若redis中没有对应的值，则默认为0
                .defaultIfEmpty(0)
                .flatMap(n -> {
                    //使用报文和redis中获取到的值进行解码
                    //需要自行按照设备协议文档约定的报文格式进行解码
                    DeviceMessage rm = doDecode(context, n);
                    JSONObject payload = JSON.parseObject(message.getPayload().toString(StandardCharsets.UTF_8));
                    return redis
                            //更新redis中的值
                            .opsForValue()
                            .set(deviceId, payload.getString("number"))
                            .thenReturn(rm);
                });
    }

    public DeviceMessage doDecode(MessageDecodeContext context, int n) {
        //处理上行报文，即设备->平台,此处伪代码，需要自行解析成平台统一的消息类型
        //封装成上报消息
        MqttMessage message = (MqttMessage) context.getMessage();
        if (message.getTopic().startsWith("/report-property")) {
            JSONObject payload = JSON.parseObject(message.getPayload().toString(StandardCharsets.UTF_8));
            //在json串里拿到需要的参数
            String path = payload.getString("path");
            //根据设备需求进行解析
            ...
            HashMap<String, Object> map = new HashMap<>();
            map.put("properties", payload);
            ReportPropertyMessage rm = new ReportPropertyMessage();
            rm.setDeviceId(context.getDevice().getDeviceId());
            rm.setProperties(map);
            return rm;
        }
        return null;
    }
    @Nonnull
    @Override
    public Publisher<? extends EncodedMessage> encode(@Nonnull MessageEncodeContext context) {
        //处理下行报文，平台->设备,需要自行按照设备协议文档约定的报文格式封装指令
        return Mono.empty();
    }


}
```
3. 定义协议处理器
```java
public class ProtocolSupportProviderImpl implements ProtocolSupportProvider {
    private static final DefaultConfigMetadata mqttConfig = new DefaultConfigMetadata(
        "MQTT认证配置", "MQTT认证时需要的配置")
        .add("secureId", "secureId", "密钥ID", new StringType())
        .add("secureKey", "secureKey", "密钥KEY", new PasswordType());

    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("mqtt-redis");
        support.setName("redis缓存示例");
        support.setDescription("redis缓存示例");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);
        support.addAuthenticator(DefaultTransport.MQTT, new JetLinksAuthenticator());

        context.getService(ReactiveRedisConnectionFactory.class).ifPresent(redis -> {
            MqttDeviceMessageCodec codec = new MqttDeviceMessageCodec(redis);
            support.addMessageCodecSupport(codec);
        });
        return Mono.just(support);
    }
}
```
### 核心接口说明

核心接口:org.springframework.data.redis.core.ReactiveRedisTemplate


| 方法名                             | 返回值                                 | 说明                |
|---------------------------------|-------------------------------------|-------------------|
| hasKey(K key)                   | Mono\<Boolean\>                     | 判断是否有key所对应的值     |
| delete(K... key)                | Mono\<Long\>                        | 删除单个key值          |
| delete(Publisher\<K\> keys)     | Mono\<Long\>                        | 批量删除key           |
| rename(K oldKey, K newKey)      | Mono\<Boolean\>                     | 修改key的名称          |
| expire(K key, Duration timeout) | Mono\<Boolean\>                     | 设置key过期时间         |
| getExpire(K key)                | Mono\<Duration\>                    | 返回当前key所对应的剩余过期时间 |
| opsForValue()                   | ReactiveValueOperations\<K, V\>     | 返回对简单值的操作         |
| opsForHash()                    | ReactiveHashOperations\<K, HK, HV\> | 返回对hash集合的操作      |
| opsForList()                    | ReactiveListOperations\<K, HK, HV\> | 返回对list列表的操作      |


核心接口:org.springframework.data.redis.core.ReactiveValueOperations


| 方法名                                   | 返回值               | 说明                    |
|---------------------------------------|-------------------|-----------------------|
| get(Object key)                       | Mono\<V\>         | 取出key值所对应的值           |
| set(K key, V value)                   | Mono\<Boolean\>   | 存入key以及value值         |
| set(K key, V value, Duration timeout) | Mono\<Boolean\>   | 存入key以及value值，并设置过期时间 |
| multiGet(Collection\<K\> keys)        | Mono\<List\<V\>\> | 批量获取key值所对应的值         |
| size(K key)                           | Mono\<Long\>      | 获取key对应值的字符串长度        |

## 设备缓存

### 使用示例
1. 定义消息编码解码器
```java
public class MqttDeviceMessageCodec implements DeviceMessageCodec {

    public MqttDeviceMessageCodec() {
    }

    @Override
    public Transport getSupportTransport() {
        return DefaultTransport.MQTT;
    }

    @Nonnull
    @Override
    public Publisher<? extends Message> decode(@Nonnull MessageDecodeContext context) {
        MqttMessage message = (MqttMessage) context.getMessage();
        JSONObject payload = JSON.parseObject(message.getPayload().toString(StandardCharsets.UTF_8));
        return context.getDevice()
                //根据存入的key从缓存中拿到对应的value
                .getConfigs("time", "number")
                .flatMap(values -> {
                    //转为对应的类型并设置参数为空时的默认值
                    String time = values.getValue("time").map(Value::asString).orElse("2022");
                    Integer number = values.getValue("number").map(Value::asInt).orElse(1);
                    //调用doDecode方法进行解码
                    DeviceMessage reportMessage = doDecode(context, time, number);
                    //更新缓存中的数据
                    Map<String, Object> configMap = new HashMap<>();
                    configMap.put("time", payload.getString("time"));
                    configMap.put("number", payload.getString("number"));
                    return context.getDevice()
                            .setConfigs(configMap)
                            .flatMap(val -> Mono.just(reportMessage));
                });
    }

    public DeviceMessage doDecode(MessageDecodeContext context, String time, int number) {
        //处理上行报文，即设备->平台,此处伪代码，需要自行解析成平台统一的消息类型
        MqttMessage message = (MqttMessage) context.getMessage();
        if (message.getTopic().startsWith("/report-property")) {
            //将设备报文转为json
            JSONObject payload = JSON.parseObject(message.getPayload().toString(StandardCharsets.UTF_8));
            //在json串里拿到需要的参数
            String path = payload.getString("path");
            //根据设备需求进行解析
            ...
            //封装平台上报消息
            HashMap map = new HashMap<String, Object>();
            map.put("properties", path);
            ReportPropertyMessage rm = new ReportPropertyMessage();
            rm.setDeviceId(context.getDevice().getDeviceId());
            rm.setProperties(map);
            return rm;
        }
        return null;
    }

    @Nonnull
    @Override
    public Publisher<? extends EncodedMessage> encode(@Nonnull MessageEncodeContext context) {
        //处理下行报文，平台->设备,需要自行按照设备协议文档约定的报文格式封装指令
        return Mono.empty();
    }
}
```
2. 定义协议处理器
```java
public class ProtocolSupportProviderImpl implements ProtocolSupportProvider {
    private static final DefaultConfigMetadata mqttConfig = new DefaultConfigMetadata(
            "MQTT认证配置", "MQTT认证时需要的配置")
            .add("secureId", "secureId", "密钥ID", new StringType())
            .add("secureKey", "secureKey", "密钥KEY", new PasswordType());

    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("mqtt-config");
        support.setName("设备缓存示例");
        support.setDescription("设备缓存示例");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        support.addConfigMetadata(DefaultTransport.MQTT, mqttConfig);
        support.addAuthenticator(DefaultTransport.MQTT, new JetLinksAuthenticator());

        support.addMessageCodecSupport(new MqttDeviceMessageCodec());
        return Mono.just(support);
    }
}
```
### 核心接口说明

核心接口:org.jetlinks.core.Configurable

| 方法名                                  | 返回值             | 说明                        |
|--------------------------------------|-----------------|---------------------------|
| getConfig(String key)                | Mono\<Value\>   | 获取单个配置信息                  |
| getConfigs(String... keys)           | Mono\<Values\>  | 获取多个配置信息，如果未指定key,则获取全部配置 |
| setConfig(String key, Object value)  | Mono\<Boolean\> | 设置单个配置信息                  |
| setConfigs(Map<String, Object> conf) | Mono\<Boolean\> | 设置多个配置信息                  |
| removeConfig(String key)             | Mono\<Boolean\> | 删除配置信息                    |
| refreshAllConfig()                   | Mono\<Void\>    | 刷新配置信息                    |





