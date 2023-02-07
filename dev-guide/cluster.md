# 集群管理

`JetLinks`支持以集群的方式部署的方式横向扩展应用。
通过`集群管理`处理多节点，可以实现集群节点之间的功能调用、数据传递。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <li>只有企业版支持集群管理。</li>

</div>

集群管理使用`scalecube`框架，基于`gossip`协议实现。

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

`scalecube`是一个基于JVM的分布式服务框架，具有轻量化、弱一致性、可扩展等特点。使用随机故障探测算法，为节点之间提供统一标准的线性网络负载。

`gossip`协议是一个分布式协议，具有扩展线、容错高、去中心化的特点。

</div>

## 核心类

集群管理的核心类`ExtendedCluster`，用于管理和配置集群节点。
通过此类可进行`集群节点事件监听`，`节点间点对点gossip通信`等操作。
#### 核心接口代码
```java
public interface ExtendedCluster extends io.scalecube.cluster.Cluster {

    /**
     * 监听集群节点事件
     *
     * @return 集群节点事件
     */
    Flux<MembershipEvent> listenMembership();

    /**
     * 监听集群消息点对点可通过返回值{@link  Disposable#dispose()}来取消监听
     *
     * @param qualifier 消息标识
     * @param handler   消息处理器
     * @return Disposable
     */
    Disposable listenMessage(@Nonnull String qualifier,
                             BiFunction<Message, ExtendedCluster, Mono<Void>> handler);

    /**
     * 监听集群广播消息,可通过返回值{@link  Disposable#dispose()}来取消监听
     *
     * @param qualifier 消息标识
     * @param handler   消息处理器
     * @return Disposable
     */
    Disposable listenGossip(@Nonnull String qualifier,
                            BiFunction<Message, ExtendedCluster, Mono<Void>> handler);

    /**
     * 设置集群消息监听器
     *
     * @param handlerFunction 监听器构造函数
     * @return this
     */
    ExtendedCluster handler(Function<ExtendedCluster, ClusterMessageHandler> handlerFunction);

    /**
     * 设置集群消息监听器
     *
     * @param handler 监听器
     * @return this
     */
    ExtendedCluster handler(ClusterMessageHandler handler);

    /**
     * 注册当前节点的feature,可用于标识当前服务支持的功能.
     *
     * @param features feature
     */
    void registerFeatures(Collection<String> features);

    /**
     * 获取支持feature的节点信息,可用于获取支持某些功能的集群节点进行某些操作.
     *
     * @param featureId feature
     * @return 节点
     */
    List<Member> featureMembers(String featureId);

    /**
     * 判断某个节点是否支持feature.
     *
     * @param member    节点Id
     * @param featureId featureId
     * @return 是否支持
     */
    boolean supportFeature(String member, String featureId);
}
```
## 集群节点配置
集群节点的配置定义在对象`ClusterProperties`，可在resources目录下的application.yaml中配置，配置前缀为`jetlinks.cluster`。
可配置的参数如下：

<table class='table'>
        <thead>
            <tr>
              <td>配置参数</td>
              <td>描述</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>name</td>
            <td>节点名称，默认为`default`</td>
          </tr>
          <tr>
            <td>externalHost</td>
            <td>集群通信对外暴露的host</td>
          </tr>
          <tr>
            <td>externalPort</td>
            <td>集群通信对外暴露的端口</td>
          </tr>
          <tr>
            <td>port</td>
            <td>集群通信本地监听端口</td>
          </tr>
          <tr>
            <td>rpcExternalHost</td>
            <td>集群rpc对外暴露的host</td>
          </tr>
          <tr>
            <td>rpcExternalPort</td>
            <td>集群rpc对外暴露的端口</td>
          </tr>
          <tr>
            <td>seeds</td>
            <td>集群种子节点，配置所有的集群通信对外暴露地址列表</td>
          </tr>
        </tbody>
      </table>

#### 配置示例：
```yaml
jetlinks:
  cluster:
    id: ${jetlinks.server-id}
    name: ${spring.application.name}
    port: 1${server.port} 
    external-host: 127.0.0.1  
    external-port: ${jetlinks.cluster.port} 
    rpc-port: 2${server.port} 
    rpc-external-host: ${jetlinks.cluster.external-host}  
    rpc-external-port: 2${server.port} 
    seeds:  #集群种子节点,集群时,配置为集群节点的 external-host:external-port
      - 127.0.0.1:18844
```

## RPC通信

集群间RPC通信使用`scalecube`框架，基于`rsocket`进行通信。
核心类`RpcManager`, 可动态注册，获取`rpc服务`进行服务调用。
```java

public interface RpcManager {

    /**
     * 当前集群节点ID
     *
     * @return 当前集群节点ID
     */
    String currentServerId();

    /**
     * 注册RPC服务实现类,可调用返回值{@link  Disposable#dispose()}来注销服务
     *
     * @param rpcService 服务
     * @param <T>        服务实现类
     * @return dispose
     */
    <T> Disposable registerService(T rpcService);

    /**
     * 注册指定id标识的RPC服务实现类,可调用返回值{@link  Disposable#dispose()}来注销服务
     *
     * @param rpcService 服务
     * @param <T>        服务实现类
     * @return dispose
     */
    <T> Disposable registerService(String serviceId, T rpcService);

    /**
     * 获取全部指定接口的服务
     *
     * @param service 服务接口类
     * @param <I>     接口类型
     * @return RPC接口
     */
    <I> Flux<RpcService<I>> getServices(Class<I> service);

    /**
     * 选择一个服务
     *
     * @param service 服务类型
     * @param <I>     服务类型
     * @return 选择结果
     */
    <I> Mono<RpcService<I>> selectService(Class<I> service);

    /**
     * 获取指定服务ID的RPC服务接口
     *
     * @param serviceId 服务ID
     * @param service   RPC接口
     * @param <I>       服务接口类
     * @return RPC接口
     */
    <I> Flux<RpcService<I>> getServices(String serviceId, Class<I> service);

    /**
     * 获取指定节点ID的RPC服务接口,用于进行点对点调用
     *
     * @param serverNodeId 集群节点ID
     * @param service      RPC接口
     * @param <I>          服务接口类
     * @return RPC接口
     */
    <I> Mono<I> getService(String serverNodeId,
                           Class<I> service);

    /**
     * 获取指定节点ID的指定服务ID的RPC服务接口,用于进行点对点调用
     *
     * @param serverNodeId 集群节点ID
     * @param service      RPC接口
     * @param serviceId    服务ID
     * @param <I>          服务接口类
     * @return RPC接口
     */
    <I> Mono<I> getService(String serverNodeId,
                           String serviceId,
                           Class<I> service);


    /**
     * 监听服务注册，注销事件
     *
     * @param service 服务接口类
     * @param <I>     服务接口类型
     * @return 事件流
     */
    <I> Flux<ServiceEvent> listen(Class<I> service);


}
```

#### 应用场景:
在一些`有状态`的业务功能，在接收到前端请求后需要同时操作集群下所有(部分)节点的功能时，可以使用
`RpcManager`来进行操作。
如:
- 网络组件、规则引擎动态启动停止
- 集群下事件总线(EventBus)的消息传递
- 集群下设备会话管理协调


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

rpc基于`scalecube`框架，定义服务时会有一些限制：

1. 服务接口必须注解`io.scalecube.services.annotations.Service`
2. 服务接口方法必须注解`io.scalecube.services.annotations.ServiceMethod`
3. 方法的返回值必须时`Mono`或`Flux`类型
4. 方法的参数只能有一个

</div> 

### RPC开发步骤
#### 1. 定义服务接口
创建接口`Api`，提供调用的方法定义。

```
@io.scalecube.services.annotations.Service
public interface Api {
    @ServiceMethod
    Mono<Void> doSomething();
}
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

<li>类要添加`@io.scalecube.services.annotations.Service`注释.</li>
<li>方法需要添加`@ServiceMethod`注释。</li>

</div>

#### 2. 编写服务的实现
创建实现类`ApiImpl`
```
public ApiImpl implements Api {
    @Override
    public Mono<Void> doSomething() {
        //
    }
}
```
#### 3. 注册实现类
使用RpcManager的registerService方法。可以在当前类初始化时引用RpcManager并调用注册方法。
```java
this.apiImpl = new ApiImpl();
rpcManager.registerService(apiImpl);
```
#### 4. 使用
通过RpcManager的getServices方法，获取所有节点上的service，然后调用对应方法。
```java
 return rpcManager
    // 获取所有节点，也可以获取一个节点，具体参照RpcManager源码    
    .getServices(Api.class)
    .flatMap(serivce -> serivce.service().doSomething());
```
