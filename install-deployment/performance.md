# 性能优化

部署到生产时,可能需要对相关配置进行优化.

## 调整日志级别

在生产环境中,应避免打印过多的debug日志. 打印过多的日志会严重影响系统性能.
也应避免直接将控制台日志重定向到文件,因为可能会导致打印日志线程阻塞.推荐配置`logback-spring.xml`来将日志异步输出到文件.
通过修改`application.yml`中的`logging.level.`相关配置来调整日志级别.

## 关闭reactor `debug-agent`

reactor `debug-agent` 会在reactor调用链中添加追踪信息,可能会影响性能.
通过配置`spring.reactor.debug-agent.enabled=false`来关闭.

## 关闭最新数据存储

在企业版中,开启了[设备最新数据存储](http://doc.jetlinks.cn/best-practices/start.html#%E8%AE%B0%E5%BD%95%E8%AE%BE%E5%A4%87%E6%9C%80%E6%96%B0%E6%95%B0%E6%8D%AE%E5%88%B0%E6%95%B0%E6%8D%AE%E5%BA%93)时,会将设备数据写到关系型数据库.
此功能使用场景: 通过最新数据来关联查询设备相关的数据比如: 查询温度大于40度的设备列表.
如果没有场景使用最新数据,可以关闭此功能:`jetlinks.device.storage.enable-last-data-in-db=false`.

## 关闭地理位置自动识别

在企业版中,在收到设备上报属性时,会根据物模型中配置的地理位置类型来获取对应的地理位置数据.
在设备属性较多,或者属性上报比较频繁时会造成一定性能影响。

优化方式1: 关闭地理位置自动识别.`device.message.geo.enable-property=false`.

优化方式2: 在需要上报地理位置时,在属性消息中添加header:`message.addHeader("containsGeo",true)`以及:`message.addHeader("geoProperty","地理位置属性ID")`。

## 尝试设置设备存储策略为不存储

平台主要性能瓶颈为数据存储,不同的存储策略,写入性能不同.可通过设置存储策略为不存储来排除是否为存储策略导致的性能问题.

## 问题定位

建议配置内存溢出时自动dump:`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heapdump.hprof`

在运行过程中,发生内存溢出,或者cpu过高时. 
1. 查看日志是否有大量的日志输出,如果有,根据日志内容来分析.
2. 使用`jps`命令查看进程`pid`,执行`jstack -l [pid]`.查看是否有被`BLOCKED`,或者调用栈比较长的线程,根据调用栈分析对应的代码.
3. 使用`async profiler`或者[Arthas](https://arthas.aliyun.com/doc/profiler.html?highlight=profiler)来收集火焰图,分析调用栈耗时情况.
   在本地可以使用`Idea`提供的`Profiler`来监控.