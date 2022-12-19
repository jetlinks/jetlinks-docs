# 平台从第三方或者设备主动拉取数据

场景：设备无法推送数据到平台,需要平台主动去调用第三方平台接口或者去调用设备的tcp服务等。

## 流程

1. 通过实现自定义协议的`DeviceStateChecker`来自定义处理设备状态获取逻辑,比如通过调用第三方平台获取设备信息.
2. 通过实现自定义协议的`DeviceMessageSenderInterceptor.afterSent`来拦截消息发送,替换掉默认处理方式.在这里使用`WebClient`或者`Vertx`请求第三方或者设备.
3. 请求后解析数据为对应的消息,调用`DecodedClientMessageHandler.handleMessage(device,message)`完成默认消息处理之后,返回消息.

## 例子一,通过http到第三方平台获取数据.

### 第一步定义消息编码解码器

```java
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
    public Publisher<? extends Message> decode(@Nonnull MessageDecodeContext context) {
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
```

### 第二步 定义一个消息拦截器

```java

@Slf4j
@AllArgsConstructor
@Getter
@Setter
public class HttpMessageSenderInterceptor implements DeviceMessageSenderInterceptor {
    // 通过构造器注入一个编码消息处理器，用于消息的持久化
    private DecodedClientMessageHandler handler;

    private static final WebClient webclient = WebClient.builder().build();

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
                        .flatMap(values -> {
                            String url = values.getValue("url").map(Value::asString).orElse(null);
                            // 通常发起请求都是通过方法调用
                            FunctionInvokeMessage invokeMessage = (FunctionInvokeMessage) message;
                            // 从命令发起的上下文中获取消息体
                            List<FunctionParameter> inputs = invokeMessage.getInputs();
                            Map<String, Object> body = iputs
                                    .stream()
                                    .collect(Collectors
                                            .toMap(FunctionParameter::getName, FunctionParameter::getValue));
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
                                                .map(deviceMessage -> (R) deviceMessage);
                                    })
                                    // 消息持久化
                                    .flatMap(msg -> handler.handleMessage(device, msg)
                                            .thenReturn(msg));
                        })
        );
    }
}
```

### 第三步 定义一个设备状态检测器

```java
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
```

### 第四步 定义协议处理器

```java
public class HttpProtocolSupportProvider implements ProtocolSupportProvider {

    private static final DefaultConfigMetadata httpRequest = new DefaultConfigMetadata(
            "Http请求配置"
            , "")
            .add("url", "url", " http请求地址", new StringType());

    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext serviceContext) {
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
```

## 例子二,通过TCP主动向设备获取数据.

### 第一步 定义session对象

```java
/**
 * session对象用来存储TCP连接，在主动向设备获取数据时就需要从session进获取连接，从而进行消息的收发
 */
public class TcpClientSession implements DeviceSession {

    @Getter
    private String deviceId;
    @Getter
    private DeviceOperator operator;
    private long keepAliveTimeout = -1;
    private NetSocket socket;
    private long keepAliveTime = System.currentTimeMillis();
    
    public TcpClientSession(String deviceId, DeviceOperator operator, NetSocket socket) {
        this.deviceId = deviceId;
        this.operator = operator;
        this.socket = socket;
    }

    @Override
    public String getId() {
        return "tcp-client";
    }

    @Override
    public long lastPingTime() {
        return System.currentTimeMillis();
    }

    @Override
    public long connectTime() {
        return System.currentTimeMillis();
    }

    @Override
    public Mono<Boolean> send(EncodedMessage message) {
        return Mono
                .<Void>create((sink) -> {
                    if (socket == null) {
                        sink.error(new SocketException("socket closed"));
                        return;
                    }
                    ByteBuf buf = message.getPayload();
                    Buffer buffer = Buffer.buffer(buf);
                    socket.write(buffer, r -> {
                        if (r.succeeded()) {
                            keepAlive();
                            sink.success();
                        } else {
                            sink.error(r.cause());
                        }
                        ReferenceCountUtil.safeRelease(buf);
                    });
                })
                .thenReturn(true);
    }

    @Override
    public Transport getTransport() {
        return DefaultTransport.TCP;
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public void setKeepAliveTimeout(Duration timeout) {
        this.keepAliveTimeout = timeout.toMillis();
    }

    @Override
    public void ping() {}

    @Override
    public boolean isAlive() {
        boolean isAlive = true;
        socket.closeHandler(close -> {
            if (isAlive) {
                socket.close();
            }
        });
        return isAlive;
    }

    @Override
    public void onClose(Runnable call) {
        call.run();
    }
}
```

### 第二步 定义设备状态检测器

```java
/**
 * 这个接口会在进入设备详情页面和刷新设备状态时调用
 */
@Slf4j
@AllArgsConstructor
public class TcpStateChecker implements DeviceStateChecker {

    private final Vertx vertx = Vertx.vertx();
    private DecodedClientMessageHandler handler;
    private DeviceSessionManager sessionManager;

    @Override
    public @NotNull Mono<Byte> checkState(@NotNull DeviceOperator device) {
        return device.getConfigs("host", "port")
                .flatMap(values -> {
                    //从设备的配置文件中拿到host地址和端口号
                    String host = values.getValue("host").map(Value::asString).orElse(null);
                    int port = values.getValue("port").map(Value::asInt).orElse(0);
                    //若不存在host地址和端口号则不再与设备创建连接
                    if (ObjectUtils.isEmpty(host) || port == 0) {
                        return Mono.just(DeviceState.offline);
                    }
                    return sessionManager
                            .getSession(device.getDeviceId())
                            //判断session是否存在，存在则表明已与设备建立连接，不存在则表明需要与设备建立连接
                            .switchIfEmpty(Mono.create((sink) -> vertx
                                    //通过host地址和端口号和建立连接
                                    .createNetClient()
                                    .connect(port, host, async -> {
                                        if (async.succeeded()) {
                                            //若连接成功，则创建session并把连接放入其中
                                            TcpClientSession newSession = new TcpClientSession(device.getDeviceId(), device, async.result());
                                            //设备发送消息时，会触发async.result().handler(...)
                                            async.result().handler(buffer -> {
                                                //将设备发送的消息转为byte数组，再根据实际的设备文档进行按位解析
                                                byte[] bytes = buffer.getBytes();
                                                HashMap<String, Object> reportMap = doDecode(bytes);
                                                //封装设备上报消息
                                                ReportPropertyMessage rpm = new ReportPropertyMessage();
                                                rpm.setDeviceId(device.getDeviceId());
                                                rpm.setProperties(reportMap);
                                                //将封装的消息交给消息处理器处理
                                                handler.handleMessage(device, rpm)
                                                        .subscribe();
                                            });
                                            //设备主动断开连接时，会触发async.result().closeHandler(...)
                                            async.result().closeHandler(close -> {
                                                //关闭session
                                                newSession.close();
                                                //清除session
                                                sessionManager.remove(device.getDeviceId(), false);
                                                //封装设备离线消息
                                                DeviceOfflineMessage off = new DeviceOfflineMessage();
                                                off.setDeviceId(device.getDeviceId());
                                                //将封装的消息交给消息处理器处理
                                                handler.handleMessage(device, off)
                                                        .subscribe();
                                            });
                                            //将新建的session放入流中
                                            sink.success(newSession);
                                        } else {
                                            sink.error(async.cause());
                                        }
                                    })
                            ))
                            //将session存入session对象中
                            .flatMap(session -> sessionManager
                                    .compute(device.getDeviceId(), old -> old
                                            .map(sess -> session)
                                            //会话已存在则替换为新的会话
                                            .defaultIfEmpty(session))
                            )
                            //使设备实例上线
                            .thenReturn(DeviceState.online)
                            //出现错误时，清除设备session并使设备离线
                            .onErrorResume(err -> sessionManager.remove(device.getDeviceId(), false)
                                    .thenReturn(DeviceState.offline));
                });
    }
}
```

### 第三步 定义设备状态检测器

```java
@Slf4j
@AllArgsConstructor
public class TcpMessageSenderInterceptor implements DeviceMessageSenderInterceptor {
    private DeviceSessionManager sessionManager;
    
    @Override
    public <R extends DeviceMessage> Flux<R> afterSent(DeviceOperator device, DeviceMessage message, Flux<R> reply) {
        if (message instanceof FunctionInvokeMessage) {
            //将平台下发的消息编码为设备可识别的十六进制报文
            byte[] encodeMessage = doEncode(message);
            return sessionManager
                    .getSession(device.getDeviceId())
                    .flatMap(session -> session.
                         //拿到session，调用send方法向设备发送编码之后的报文调用send方法向设备发送编码之后的报文
                         send(EncodedMessage.simple(Unpooled.wrappedBuffer(JSONObject.toJSONBytes(encodeMessage))))
                    )
                    .thenMany(Mono.empty());
        }
        return Flux.empty();
    }
}
```

### 第四步 定义协议处理器
```java
public class TcpShortSupportProvider implements ProtocolSupportProvider {
    private static final DefaultConfigMetadata config = new DefaultConfigMetadata("tcp请求", "")
        .add("host", "host", "host", StringType.GLOBAL)
        .add("port", "port", "port", IntType.GLOBAL);

    @Override
    public Mono<? extends ProtocolSupport> create(ServiceContext context) {
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("tcp-vertx");
        support.setName("tcp主动获取设备数据");
        support.setDescription("tcp主动获取设备数据");
        support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
        support.addConfigMetadata(DefaultTransport.TCP, config);

        return Mono.zip(
                Mono.justOrEmpty(context.getService(DecodedClientMessageHandler.class)),
                Mono.justOrEmpty(context.getService(DeviceSessionManager.class))
            )
            .map(tp2 -> {
                support.setDeviceStateChecker(new TcpStateChecker(tp2.getT1(), tp2.getT2()));
                return new TcpMessageSenderInterceptor(tp2.getT2());
            })
            .doOnNext(interceptor -> {
                support.addMessageSenderInterceptor(interceptor);
            })
            .thenReturn(support);
    }
}
```