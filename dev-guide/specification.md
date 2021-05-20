# 命名

## java命名
1. maven模块名小写,多个单词使用`-`连接
    
    正确: device-manager
    错误: deviceManager
    
2. 包名全部小写,多个单词使用多个目录层级.
    
    正确: org.jetlinks.pro.device.instance
    错误: org.jetlinks.pro.deviceInstance

3. 类名首字母大写,使用驼峰命名.
    
    正确: DeviceInstance
    错误: Deviceinstance , Device_Instance

4. 方法名首字母小写,使用驼峰命名. 名称要见名知义.
    
    正确: findById(String id), deployDeviceInstance(List&lt;String&gt;deviceInstanceIdList)
    错误: getData(String arg)
 
5. 局部变量首字母小写,使用驼峰命名. 名称要见名知义.

    正确: String deviceId = device.getId();
    错误: String str = device.getId();

6. 常量使用大写,多个单词使用`_`分割.
    
    正确: static String DEFAULT_CONFIG = "1";
    错误: static String DEFAULTCONFIG = "1";

## java 接口

一个模块在需要提供给其他模块使用时,应该面向接口编程,对外提供相应的接口.并在当前模块提供默认的实现.

当一个模块是具体的业务功能实现时,大部分情况不需要写接口,如一个增删改查功能 不需要针对 `Service` 写一个接口.

## Restful 接口命名

URL使用小写,多个单词使用`-`或者`/`分割,如:`device-info`,`logger/system`.通常情况下应该使用URL来描述资源,
使用HTTP METHOD(`GET 查询`,`POST 新增`,`PUT 修改`,`PATCH 修改不存在则新增`,`DELETE 删除`)来描述对资源的操作.
在一些特殊操作无法使用`HTTP METHOD`来描述操作的时候,使用`_`开头加动词来描述,如: device/_query

    正确: GET /device-info/1
    错误: GET /deviceInfo/1  GET /device_info/1

    正确: GET /device/1
    错误: GET /getDevice?id=1 ,  GET /getDevice/1

    正确: GET /device/_query , POST /device/_query
    错误: GET /queryDevice , POST /queryDevice


# 响应式
JetLinks 使用全响应式([reactor](https://projectreactor.io/))编程.

约定: 

1. 所有可能涉及到IO或者异步操作(`网络请求`,`数据库操作`,`文件操作`)的方法,返回值全部为`Mono`或者`Flux`.
2. 所有代码不允许出现阻塞操作,如: `Thread.sleep`,`Mono.block`,`Flux.block`.
3. 响应式方法间调用,应该组装为同一个`Publisher`.
    
    正确: return deviceService
                     .findById(id)
                     .flatMap(device->this.syncDeviceState(device));
    
    错误: deviceService
                    .findById(id)
                    .subscribe(device->this.syncDeviceState(device).subscribe())
                    
    错误: this.syncDeviceState(deviceService.findById(id).block()).subscribe();


相关资料:

1. [reactive-streams](http://www.reactive-streams.org/)
2. [project-reactor](https://projectreactor.io/)
3. [使用 Reactor 进行反应式编程](https://www.ibm.com/developerworks/cn/java/j-cn-with-reactor-response-encode/index.html?lnk=hmhm)
4. [simviso视频教程](https://space.bilibili.com/2494318)
