# 压力测试

压力测试主要对设备连接数以及设备消息处理速度进行测试.

测试场景:

1. 10万设备连接
2. 30万设备连接
3. 50万设备连接
4. 每秒随机10000个设备发送1条消息. 总计10000每秒.
5. 每秒随机20000个设备发送1条消息. 总计20000每秒

::: tip 注意
本测试在本地虚拟机中进行,屏蔽了网络环境影响,仅仅是对平台性能进行测试.
:::

## 压测环境

使用2个本地Linux虚拟机,一个作为服务端,一个作为压测客户端.

主机: i7 8700K, 64G内存 ,SSD, macOS,

服务端虚拟机: 6核28G内存,ubuntu 16.4.

客户端虚拟机: 4核10G内存,ubuntu 16.4.

初始数据: 200万设备实例,使用demo中的型号以及物模型.

```sql
--生成200万模拟数据
insert into dev_device_instance
select 1584008694260,null,null,null,'演示型号',null,null,
       'demo-device',
       concat('测试-',generate_series(0,2000000)),
       'admin','admin',
       'notActive',
       concat('test-',generate_series(0,2000000))
        ;
```

服务端使用[docker快速启动](/install-deployment/docker-start.md),除了`elasticsearch`和`jetlinks`对内存配置,没有做其他修改.

客户端使用[模拟器](https://github.com/jetlinks/device-simulator)批量模拟设备.

参考脚本:

```bash
#!/usr/bin/env bash
sudo sysctl -p
sudo ufw disable

# 创建虚拟网卡
for i in {10..40}; do sudo ifconfig enp0s6:$i 10.211.55.2$i up ;done

java -jar -Xmx8G -Xms1024m -XX:+UseG1GC device-simulator.jar \
mqtt.batchSize=1000 \
mqtt.port=1883 \
mqtt.address=10.211.55.6 \
mqtt.scriptFile=./scripts/demo-device.js \
mqtt.timeout=1000 \
mqtt.limit=200000 \
mqtt.enableEvent=false \
mqtt.eventLimit=40000 \
mqtt.eventRate=1000 \
mqtt.threadSize=128 \
mqtt.bindPortStart=10000 \
mqtt.binds=10.211.55.210,10.211.55.211,10.211.55.212,10.211.55.213,10.211.55.214,10.211.55.215\
,10.211.55.217,10.211.55.218,10.211.55.219,10.211.55.220,10.211.55.221,10.211.55.222,10.211.55.223\
,10.211.55.224,10.211.55.225,10.211.55.226,10.211.55.227,10.211.55.228,10.211.55.229,10.211.55.230\
,10.211.55.231,10.211.55.232,10.211.55.233,10.211.55.234,10.211.55.235,10.211.55.236,10.211.55.237\
,10.211.55.238,10.211.55.239
```
 
## 测试结果

### 10万设备连接

1000并发请求连接.总计10万连接.

第一次,CPU平均使用率48%.JVM内存:4.64G.

```bash
create mqtt client: 99000 ok
create mqtt client: 100000 ok

max : 2364ms
min : 386ms
avg : 1036ms

> 5000ms : 0(0.00%)
> 2000ms : 418(0.42%)
> 1000ms : 54172(54.17%)
> 500ms : 45374(45.37%)
> 200ms : 36(0.04%)
> 100ms : 0(0.00%)
> 10ms : 0(0.00%)
```

第二次,CPU平均使用率78%.JVM内存:4.91G.

```bash
create mqtt client: 99000 ok
create mqtt client: 100000 ok

max : 934ms
min : 127ms
avg : 257ms

> 5000ms : 0(0.00%)
> 2000ms : 0(0.00%)
> 1000ms : 0(0.00%)
> 500ms : 3360(3.36%)
> 200ms : 58178(58.18%)
> 100ms : 38462(38.46%)
> 10ms : 0(0.00%)
```

::: tip
第一次为刚启动服务,设备首次连接均需要到redis中获取认证信息.
第二次设备信息已经加载到了内存,连接速度快了3倍以上,cpu使用率也更高.
:::


### 30万设备连接

1000并发请求连接.总计30万连接.

第一次,CPU平均使用率65%,JVM内存:5.16G

```bash
create mqtt client: 298000 ok
create mqtt client: 299000 ok
create mqtt client: 300000 ok

max : 1549ms
min : 312ms
avg : 972ms

> 5000ms : 0(0.00%)
> 2000ms : 0(0.00%)
> 1000ms : 103558(34.52%)
> 500ms : 196319(65.44%)
> 200ms : 123(0.04%)
> 100ms : 0(0.00%)
> 10ms : 0(0.00%)
```

第二次,CPU平均使用率81%,JVM内存: 4.38G

```bash
create mqtt client: 298000 ok
create mqtt client: 299000 ok
create mqtt client: 300000 ok

max : 5959ms
min : 120ms
avg : 358ms

> 5000ms : 257(0.09%)
> 2000ms : 6486(2.16%)
> 1000ms : 6902(2.30%)
> 500ms : 16411(5.47%)
> 200ms : 179659(59.89%)
> 100ms : 90285(30.09%)
> 10ms : 0(0.00%)
```

## 50万设备连接

1000并发请求连接.总计50万连接.

第一次,CPU平均使用率58%,JVM内存:5.38G

```bash
create mqtt client: 499000 ok
create mqtt client: 500000 ok

max : 1743ms
min : 156ms
avg : 878ms

> 5000ms : 0(0.00%)
> 2000ms : 0(0.00%)
> 1000ms : 57223(11.44%)
> 500ms : 431645(86.33%)
> 200ms : 9621(1.92%)
> 100ms : 1511(0.30%)
> 10ms : 0(0.00%)
```

第二次,CPU平均使用率88%,JVM内存:8.2G

```bash
create mqtt client: 499000 ok
create mqtt client: 500000 ok

max : 4662ms
min : 128ms
avg : 294ms

> 5000ms : 0(0.00%)
> 2000ms : 2421(0.48%)
> 1000ms : 5273(1.05%)
> 500ms : 34368(6.87%)
> 200ms : 278833(55.77%)
> 100ms : 179105(35.82%)
> 10ms : 0(0.00%)
```

## 数据上报,每秒上报10000条属性消息.

持续30分钟,CPU使用率: 43%.JVM最大内存:3.5G. 
数据无堆积,ElasticSearch索引速率`20,002.9/s`.

## 数据上报,每秒上报20000条属性消息.

持续30分钟,CPU使用率: 79%.JVM最大内存:7.3G. 
数据无堆积,ElasticSearch索引速率`40,528.1/s`.

::: tip

由于硬件资源不足,没有进行更大量的消息测试. 理论上性能瓶颈会在`elasticsearch`的索引速度.

:::