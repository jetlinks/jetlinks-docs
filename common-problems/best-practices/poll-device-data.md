# 平台从第三方或者设备主动拉取数据

场景：设备无法推送数据到平台,需要平台主动去调用第三方平台接口或者去调用设备的tcp服务等。

## 流程

1. 通过实现自定义协议的`DeviceStateChecker`来自定义处理设备状态获取逻辑,比如通过调用第三方平台获取设备信息.
2. 通过实现自定义协议的`DeviceMessageSenderInterceptor.afterSent`来拦截消息发送,替换掉默认处理方式.在这里使用`WebClient`或者`Vertx`请求第三方或者设备.
3. 请求后解析数据为对应的消息,调用`DecodedClientMessageHandler.handleMessage(device,message)`完成默认消息处理之后,返回消息.

## 例子一,通过http到第三方平台获取数据.

### 第一步定义消息编码解码器  

~~~java
public class HttpMessageCodec implements DeviceMessageCodec {
    
    // 定义一个通用的响应，用于收到请求后响应
    private static final SimpleHttpResponseMessage response = SimpleHttpResponseMessage
            .builder()
            .payload(Unpooled.wrappedBuffer("{success:true}".getBytes()))
            .contentType(MediaType.APPLICATION_JSON)
            .status(200)
            .build();
    
    @Override
    public Transport getSupportTransport() {
        return DefaultTransport.HTTP;
    }
    
    @Nonnull
    @Override
    public Publisher<? extends Message> decode(@Nonnull MessageDecodeContext context){
        // 这里用于别的平台请求/通知jetlinks的请求处理
        // 把消息转换为http消息
         HttpExchangeMessage message = (HttpExchangeMessage) context.getMessage();
        String url = message.getUrl();
        // 这里通常需要判断是不是自己需要的请求，如果不是直接返回/响应，防止非法请求
        if (!url.endsWith("/eventRcv")) {
            return message.response(response).then(Mono.empty());
        }
        // 获取具体消息类型
        ByteBuf payload = message.getPayload();
        String string = payload.toString(StandardCharsets.UTF_8);
        // 通常来说，云平台通知的定义为事件消息（也可以定义成别的消息）
        EventMessage eventMessage = new EventMessage();
        eventMessage.setEvent("test");
        eventMessage.setDeviceId(string);
        eventMessage.setData(string);
        eventMessage.setMessageId(String.valueOf(System.currentTimeMillis()));
        eventMessage.setTimestamp(System.currentTimeMillis());
        return message.response(response).thenMany(Flux.just(eventMessage));
    }
    
    
    @Nonnull
    @Override
    public Publisher<? extends EncodedMessage> encode(@Nonnull MessageEncodeContext context) {
        // 对接其他云平台，命令发起不在这里处理，所以这里返回空就可以了
        return Mono.empty();
    }
    
}
~~~

### 第二步 定义一个消息拦截器

~~~java
@Slf4j
@AllArgsConstructor
@Getter
@Setter
public class HttpMessageSenderInterceptor implements DeviceMessageSenderInterceptor{
    // 通过构造器注入一个编码消息处理器，用于消息的持久化
	private DecodedClientMessageHandler handler;
    
    private static final WebClient webclient=WebClient.builder().build();
   /**
     * 在消息发送后触发.
     *
     * @param device  设备操作接口
     * @param message 源消息
     * @param reply   回复的消息
     * @param <R>     回复的消息类型
     * @return 新的回复结果
     */
    public <R extends DeviceMessage> Flux<R> afterSent(DeviceOperator device, DeviceMessage message, Flux<R> reply) {
        return Flux.from(
            // 从配置中获取url等各种请求所需参数
            device.getConfigs("url")
                        .flatMap(values->{
                            String url=values.getValue("url").map(Value::asString).orElse(null);
                            // 通常发起请求都是通过方法调用
                            FunctionInvokeMessage invokeMessage = (FunctionInvokeMessage) message;
                            // 从命令发起的上下文中获取消息体
        				  List<FunctionParameter> inputs = invokeMessage.getInputs();
                            Map< String, Object> body=iputs
                                .stream()
                                .collect(Collectors
                                         .toMap(FunctionParameter::getName, 			FunctionParameter::getValue));
                            return webclient  // 构造WebClient
                            	.post()  // 指定请求类型
                            	.uri(url) // 请求路径
                            	.bodyValue(body) // 请求参数
                           		.retrieve() // 发起请求
                           		.bodyToMono(String.class) // 响应参数
                           		.flatMap(s -> {
                                    // 响应参数包装为功能回复参数
                               		FunctionInvokeMessageReply reply1 = new FunctionInvokeMessageReply();
                               		reply1.setSuccess(true);
                               		reply1.setMessage(s);
                               		reply1.setDeviceId(message.getDeviceId());
                               		reply1.setMessageId(message.getMessageId());
                               		reply1.setTimestamp(System.currentTimeMillis());
                               		reply1.setOutput(s);
                               		reply1.setFunctionId(((FunctionInvokeMessage) message).getFunctionId());
                               return Mono.just(reply1)
                                       .map(deviceMessage->(R)deviceMessage);
                           })
                           // 消息持久化
                           .flatMap(msg->handler.handleMessage(device,msg)
                                   .thenReturn(msg));
                        })
                );
    }
}
~~~

### 第三步 定义一个设备状态检测器

~~~java
/**
* 这个接口会在进入设备详情页面和刷新设备状态时调用
*/
@Slf4j
public class HttpDeviceStateChecker implements DeviceStateChecker {
    @Override
    public @NotNull Mono<Byte> checkState(@NotNull DeviceOperator device) {
        // 如果第三方平台有提供设备状态查询接口，则调用接口确定设备状态，否则设置为设备在线，方便发起功能或者属性查询
        return Mono.just(DeviceState.online);
    }
}
~~~

### 第四步 定义协议处理器

~~~java
public class HttpProtocolSupportProvider implements ProtocolSupportProvider{
    
        private static final DefaultConfigMetadata httpRequest = new DefaultConfigMetadata(
            "Http请求配置"
            , "")
            .add("url", "url", " http请求地址", new StringType());
    
    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext serviceContext){
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("http-demo-v1");
        support.setName("http调用第三方接口DEMO");
        support.setDescription("http调用第三方接口DEMO");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        // 设置一个编解码入口
        HttpMessageCodec codec = new HttpMessageCodec();
        support.addMessageCodecSupport(DefaultTransport.HTTP, () -> Mono.just(codec));
        // 添加配置项定义
        support.addConfigMetadata(DefaultTransport.HTTP, httpRequest);
        HttpDeviceStateChecker httpDeviceStateChecker = new HttpDeviceStateChecker();
        // 设置设备状态检查接口
        support.setDeviceStateChecker(httpDeviceStateChecker);
        // 设置HTTP消息拦截器，用于发送HTTP消息
        serviceContext.getService(DecodedClientMessageHandler.class)
                .ifPresent(handler -> support.addMessageSenderInterceptor(new HttpMessageSenderInterceptor(handler)));
        return Mono.just(support);
    }
}
~~~


## 例子二,通过tcp短链接从设备拉取数据.

TODO