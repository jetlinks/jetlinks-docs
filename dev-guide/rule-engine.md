
# 规则引擎说明
平台中内置了规则引擎,`设备告警`,`数据转发`,`场景联动`均由规则引擎执行.

名词说明:

* **RuleModel(规则模型)**:由多个`RuleNode(规则节点)`,`RuleLink(规则连线)`组成
* **RuleNode(规则节点)**: 规则节点描述具体执行的逻辑
* **RuleLink(规则连线)**: 用于将多个节点连接起来,将上一个节点的输出结果作为下一个节点的输入结果.
* **Input(输入)**: 规则节点的数据输入
* **Output(输出)**: 规则节点的数据输出
* **Scheduler(调度器)**: 负责将模型转为任务(`Job`),并进行任务调度到`Worker`
* **Worker(工作器)**: 负责执行,维护任务.
* **ExecutionContext(执行上下文)**: 启动任务时的上下文,通过上下文获取输入输出配置信息等进行任务处理.
* **TaskExecutor(任务执行器)**: 具体执行任务逻辑的实现
* **TaskExecutorProvider(任务执行器提供商)**: 用于根据模型配置以及上下文创建任务执行器.
* **RuleData(规则数据)**: 任务执行过程中的数据实例

## 规则模型数据结构

```text
//规则模型
RuleModel{ 
    events:[ RuleLink ]     # 事件连接点,用于自定义规则事件的处理规则
    nodes:[ RuleNodeModel ] # 所有节点信息，包含事件节点
}
//节点模型
RuleNodeModel{
    executor: ""            # 节点执行器标识
    configuration: { Map }  # 节点配置
    events:[ RuleLink ]     # 事件连接点,用于自定义节点事件的处理规则
    inputs:[ RuleLink ]     # 输入连接点
    outputs:[ RuleLink ]    # 输出连接点
}
//连接点，将2个规则节点关联
RuleLink{
    type: ""                # 类型，为事件节点连接时值为对应当事件标识
    condition: Condition    # 连接条件
    source: RuleNodeModel   # 连接节点
    target: RuleNodeModel   # 被连接节点
}
//条件
Condition{
    type: ""                # 条件类型。如: expression
    configuration: { Map }  # 条件配置
}
```

## 自定义规则节点

1. 实现接口`TaskExecutorProvider`
2. 注解`@Component`

```java
@Component
@AllArgsConstructor
public class SceneRuleTaskExecutorProvider implements TaskExecutorProvider {

    private final EventBus eventBus;

    @Override
    public String getExecutor() {
        return "scene";
    }

    @Override
    public Mono<TaskExecutor> createTask(ExecutionContext context) {

        return Mono.just(new DeviceSceneTaskExecutor(context));
    }

    class DeviceSceneTaskExecutor extends FunctionTaskExecutor {

        private String id;

        private String name;

        public DeviceSceneTaskExecutor(ExecutionContext context) {
            super("场景联动", context);
            reload();
        }

        @Override
        public void reload() {
            //从任务配置中获取配置
            this.id = (String) getContext().getJob().getConfiguration().get("id");
            this.name = (String) getContext().getJob().getConfiguration().get("name");
        }

        @Override
        protected Publisher<RuleData> apply(RuleData input) {
            Map<String, Object> data = new HashMap<>();
            data.put("sceneId", id);
            data.put("sceneName", name);
            data.put("executeTime", System.currentTimeMillis());

            input.acceptMap(data::putAll);

            return eventBus
                .publish(String.join("/", "scene", id), data)
                //转换新的数据
                .thenReturn(context.newRuleData(input.newData(data)));
        }
    }
}

```

## 内置规则节点

### 定时任务

```js
{
    executor: "timer",
    configuration:{"cron":"cron表达式"}
}
```

### 延迟(限流)执行

```js
{
    executor: "delay",
    configuration:{
        pauseType:"延迟类型: delayv(上游节点指定固定延迟),delay(固定延迟),random(随机延迟),rate(速率限制),group(分组速率限制)",
        //延迟类型为delay时,使用以下配置
        timeout:10,//延迟时间
        timeoutUnits: "延迟时间单位:Seconds(秒),Millis(毫秒)",
        //延迟类型为random时,使用以下配置
        randomFirst: 100, //最小延迟时间
        randomLast: 1000 //最大延迟时间
        randomUnits: "随机延迟时间单位:Seconds(秒),Millis(毫秒)",
        //延迟类型为rate或者group时使用以下配置
        rate:10,//速率，如: 10条
        nbRateUnits:10,//速率时间单位,如: 1秒
        rateUnits:"速率时间单位:Seconds(秒),Millis(毫秒)",
        //延迟类型为group时使用以下配置
        groupExpression:"deviceId" //分组表达式,表达式语言为jsonata
    }
}
```

### 函数(脚本)节点

```js
{
    executor:"script",
    configuration:{
        "lang":"脚本语言: js,groovy",
        "script":"基本内容,见说明"
    }
}
```

#### 脚本说明 

脚本使用jsr223引擎,
通过调用内置变量handler.onMessage注册消息监听函数,当上游产生数据时,此函数将被调用,并传入数据.

例如:

```js
var ctx = context;
handler.onMessage(function(ruleData){

var data = ruleData.data; //上游节点的输出

return { // 输出到下一个节点
        "key":"value"
    }
});

```

通过指定输出数量值,可以控制输出到指定的节点,如:

```js
var ctx = context;
handler.onMessage(function(ruleData){

return [
    {"to":"node1"}, //输出到第一个节点
    {"to":"node2"}  //输出到第二个节点
    ];
});
```

你还可以通过上下文作用域保存,获取数据.
```js
var ctx = context;
handler.onMessage(function(ruleData){

var data = ruleData.data;

return ctx.node()
        .counter()
        .inc(data.value) // 获取当前节点的计数器并递增
        .map(function(i){
           return {
              "total":i;
           }
        })
```

#### 作用域

* ctx.scope(String id)或者ctx.scope(RuleData ruleData)上下文作用域,根据ruleData.contextId决定.
* ctx.node()当前节点作用域
* ctx.node(String id)指定节点作用域
* ctx.flow()当前流程作用域
* ctx.flow(String id)指定流程作用域
* ctx.flow(String id).node(String id)指定流程指定节点的作用域
* ctx.global()全局作用域

作用域支持方法:

* .all(String... key)获取指定key的数据,如果未指定这返回全部,类型为Mono&lt;Map&lt;String,Object&gt;&gt;
* .get(String key)获取指定key的数据,返回类型为Mono&lt;Object&gt;
* .put(String key,Object value)设置值,返回类型为Mono&lt;Void&gt;
* .putAll(Map&lt;String,Object&gt;)设置多个值,参数为Map,返回类型为Mono&lt;Void&gt;
* .clear()清空作用域,返回类型为Mono&lt;Void&gt;
* .counter()获取计数器
* .counter(String name)获取指定名字的计数器
* .counter().inc(double number)计数器递增,返回最新值:Mono&lt;Double&gt;
* .counter().dec(double number)计数器递减,返回最新值:Mono&lt;Double&gt;
* .counter().getAndSet(double number)获取最新值后设置新的值,返回:Mono&lt;Double&gt;
* .counter().setAndGet(double number)设置最新值后返回最新的值,返回:Mono&lt;Double&gt;

::: tip 特别注意
作用域的返回值均是reactor的API ,注意将操作组合成一个流后返回,如:
```js
return ctx
   .node()
   .set("tmp",val)
   .thenReturn({"success":true})
```
:::

#### 日志输出和错误处理
使用以下功能输出日志：

ctx.getLogger().debug("Log message {}",data);
ctx.getLogger().warn("Warning");
ctx.getLogger().error("Error");
使用以下功能触发错误：

throw new Error("错误");
throw new java.lang.RuntimeException("错误");

### ReactorQL

```js
{
    executor:"reactor-ql",
    configuration:{
        "sql":"ReactorQL语句"
    }
}
```

::: tip 说明
通过ReactorQL可以订阅设备消息等消息,还可以进行分组聚合计算等操作.
见: [ReactorQL说明](reactor-ql.md)
:::

### 设备指令

```js
{
    executor:"device-message-sender",
    configuration:{
        "productId":"产品ID",
        "deviceId":"设备ID,为空时发送到产品下所有设备",
        "selector":"设备选择器",//见设备选择器说明
        "from":"消息来源:pre-node(上游节点),fixed(固定消息)",
        "timeout":"10s",//超时时间
        "message":{ //设备指令内容
            "messageType":"消息类型"
        },
        "waitType":"等待类型:sync(等待设备回复),forget(忽略返回结果)"
    }
}
```

::: tip 说明
设备指令内容见:[平台统一设备消息定义](../best-practices/start.md#平台统一设备消息定义)
:::

#### 设备选择器说明

如果下发指令的设备是动态获取的，可使用表达式函数来获取设备并发送到对应到设备。

例如:

* 获取产品ID为demo-device的设备:`product('demo-device')`

* 获取分组为demo-group下的设备:`in_group('demo-group')`

* 获取当前设备相同分组下的设备:`same_group(deviceId)`

* 获取标签supplier为测试厂商下的设备:`tag('supplier','测试厂商')`

* 按状态筛选 :`state('online')`,状态:`online,offline,notActive`

* 函数的参数可以是固定的字符串,如:`product('demo-device')`,也可以是上游节点传递的变量,如: `same_group(deviceId)`

* 多个表达式使用,分隔,例如:`same_group(deviceId),tag('supplier','测试厂商')`


### 消息通知

```js
{
    executor:"notifier",
    configuration:{
        "notifyType":"通知类型:sms(短信),email(邮件),voice(语音),dingTalk(钉钉),weixin(微信);",
        "notifierId":"通知配置ID",
        "templateId":"模版ID"
    }
}
```
