# 协议开发常见问题

## 如何在协议包中获取spring中的bean

注意,协议包中的类`不支持`spring自动注入,可以通过从`ServiceContext`中获取bean,如:

```java
public class MyProtocolSupportProvider implements ProtocolSupportProvider {

    @Override
    public Mono<ProtocolSupport> create(ServiceContext context) {
         //获取bean,作用和ApplicationContext相同
         DeviceDataManager dataManager = context.getService(DeviceDataManager.class).orElseThrow(IllegalStateException::new);    
         
         ....
    }

}
```

::: tip 说明
DeviceDataManager 可以获取设备属性数据缓存以及标签信息等
:::

## 如何在协议包中使用第三方的依赖或者sdk

1. 在`pom.xml`中添加依赖
2. 使用`maven-shade-plugin`将依赖的jar打包在一起
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.1.1</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <artifactSet>
                    <includes>
              <!--   添加需要打包在一起的第三方依赖信息  -->
              <!--  <include>com.domain:*</include>  -->
                    </includes>
                </artifactSet>
            </configuration>
        </execution>
    </executions>
</plugin>
```

::: tip 提示
请勿将`jetlinks`,`spring`等平台自身的依赖打包在一起.

更多`maven-shade-plugin`功能请查询[相关文档](https://maven.apache.org/plugins/maven-shade-plugin/usage.html)
:::

## 本地开发时如何调试协议包

1. 将协议项目文件和jetlinks平台代码放在一起(不用添加依赖,只需要放到同一个项目目录即可)
2. 添加为Maven项目,`右键目录`-`Add as Maven Project`,等待加载完成.
3. 在响应代码打断点即可,和普通项目debug没有区别

![debug](./img/add-debug-protocol.gif)

::: tip 温馨提示

在`linux`和`macOS`下可以通过软连接的方式将文件链接到平台项目中,不用复制粘贴导致文件重复.
如: `ln -s ~/custom-protocol dev/` 

:::

## 协议包是响应式的,不会写怎么办

协议包编解码等操作是响应式的,如果实际编解码不存在异步操作(获取设备配置信息,下发回复给设备等)的话,
可以单独封装一个非响应式的方法来做解析,最终使用此方法来做解析,然后在编解码方法中调用即可.

```java

public Flux<DeviceMessage> decode(MessageDecodeContext context){

MqttMessage message = (MqttMessage) ctx.getMessage();

List<DeviceMessage> result = doDecode(message);

return Flux.fromIterable(result);

}

private List<DeviceMessage> doDecode(MqttMessage message){
    
    //.... 解码为设备消息
}

```

::: tip 温馨提示
如果需要深入了解和开发协议以及JetLinks平台,建议学习一下[Project Reactor](https://projectreactor.io/docs/core/release/reference/)框架.
:::

## 如何在收到设备的指令后,给设备进行应答

在一些tcp或者udp的设备可能需要平台处理消息后进行应答.

```java
 //解码,回复设备并返回一个消息
  public Mono<DeviceMessage> decode(MessageDecodeContext context){
 
   EncodedMessage message = context.getMessage();
   ByteBuf payload = message.payload();//上报的数据
 
   DeviceMessage message = doEncode(payload); //解码
 
   //强转为FromDeviceMessageContext
   FromDeviceMessageContext ctx = (FromDeviceMessageContext)context;
 
   EncodedMessage msg = createAckMessage(); //构造应答消息
 
   return ctx
      .getSession()
      .send(msg) //发送消息给设备
      .thenReturn(message);
  }
 
```

::: warning 特别注意

`DeviceSession.send`是<span style="color:red">响应式操作,需要和整个响应式流组合在一起</span>,否则可能无法发送到设备.

错误的例子:
```java
ctx.getSession().send(msg);

return Flux.just(message);
```

正确的例子:
```java
ctx
.getSession()
.send(msg)
.thenReturn(message);
```

:::

## 如何在解码时获取设备信息


1. `context.getDevice()`; 此方法可能返回null,比如在tcp首次发送报文时.

```java

   public Mono<DeviceMessage> decode(MessageDecodeContext context) {
      
      FromDeviceMessageContext ctx = ((FromDeviceMessageContext) context);
      ByteBuf payload = message.payload();
      
      DeviceOperator device =  context.getDevice();
      //device为null说明时首次连接,平台未识别到当前连接属于哪个设备
      //返回了DeviceMessage之后,将会与DeviceMessage对应的设备进行绑定,之后getDevice()则为此设备对应的信息.
      if(device==null){
         //处理认证等逻辑,具体根据实际协议来处理
        return handleFirstMessage(payload);
      } else {
        return handleMessage(payload);
      }

    }

```


2. `context.getDevice(deviceId)`;使用已知的设备ID来获取设备信息.

 ```java

   public Mono<DeviceMessage> decode(MessageDecodeContext context) {
      
      FromDeviceMessageContext ctx = ((FromDeviceMessageContext) context);
      ByteBuf payload = message.payload();
      //从报文中获取
      String deviceId = getDeviceIdFromMessage(payload);

     return context
     .getDevice(deviceId)
     //获取设备的配置
     .flatMap(device-> device.getSelfConfig("des_key"))
     //设备或者设备配置des_key不存在时会走swichIfEmpty的逻辑
     .swichIfEmpty(Mono.defer(()->handleDeviceNotFound(ctx).then(Mono.empty())))
     .mapNotNull(key->{
        //解码
        return doDecodeMessage(key.asString(),payload)
     })
   }
    
    private Mono<Void> handleDeviceNotFound(FromDeviceMessageContext ctx){
        //构造设备不存在的消息
        EncodedMessage ack = ...;
        return ctx
                .getSession()
                .send(ack)
                .then();
    }

    private DeviceMessage doDecodeMessage(String key,ByteBuf payload){
       //解码并返回设备消息
       return  ...;
    }

    private String getDeviceIdFromMessage(ByteBuf buf){
       //根据协议约定从报文中获取设备ID,比如设备sn等.
       return ...;
    }

```
