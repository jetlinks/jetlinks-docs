# 开发规范

## 概述

JetLinks平台在开发初期约定了以下的开发规范、数据库设计规范以及用户界面设计原则。

   
## 平台规范
### java命名

1. maven模块名小写，多个单词使用`-`连接。

   正确: device-manager
   错误: deviceManager

2. 包名全部小写，多个单词使用多个目录层级。

   正确: org.jetlinks.pro.device.instance  
   错误: org.jetlinks.pro.deviceInstance

3. 类名首字母大写，使用驼峰命名。

   正确: DeviceInstance  
   错误: Deviceinstance, Device_Instance

4. 方法名首字母小写，使用驼峰命名。名称要见名知义。

   正确: findById(String id), deployDeviceInstance(List&lt;String&gt;deviceInstanceIdList)  
   错误: getData(String arg)

5. 局部变量首字母小写，使用驼峰命名。名称要见名知义。

   正确: String deviceId = device.getId();  
   错误: String str = device.getId();

6. 常量使用大写，多个单词使用`_`连接。

   正确: static String DEFAULT_CONFIG = "1";  
   错误: static String DEFAULTCONFIG = "1";

7. 静态资源文件规范，放在`src/main/resources`文件夹下

   参考`jetlinks-v2\jetlinks-components\rule-engine-component`下的`resources\rule-engine`目录

### java 接口

一个模块在需要提供给其他模块使用时，应该面向接口编程，对外提供相应的接口。并在当前模块提供默认的实现。

当一个模块是具体的业务功能实现时，大部分情况不需要写接口。如一个增删改查功能，不需要针对 `Service` 写一个接口。

### Restful 接口命名

URL使用小写，多个单词使用`-`或者`/`连接。如：`device-info`、`logger/system`。通常情况下应该使用URL来描述资源，
使用HTTP METHOD(`GET 查询`,`POST 新增`,`PUT 修改`,`PATCH 修改不存在则新增`,`DELETE 删除`)来描述对资源的操作。
在一些特殊操作无法使用`HTTP METHOD`来描述操作的时候，使用`_`开头加动词来描述。如: `device/_query`。

    正确: GET /device-info/1
    错误: GET /deviceInfo/1  GET /device_info/1

    正确: GET /device/1
    错误: GET /getDevice?id=1 ,  GET /getDevice/1

    正确: GET /device/_query , POST /device/_query
    错误: GET /queryDevice , POST /queryDevice

### 响应式

JetLinks 使用全响应式(<a target='_blank' href='https://projectreactor.io/'>Project Reactor</a>)编程。

约定:

1. 所有可能涉及到IO或者异步操作(`网络请求`,`数据库操作`,`文件操作`)的方法，返回值全部为`Mono`或者`Flux`。
2. 所有代码不允许出现阻塞操作，如：`Thread.sleep`，`Mono.block`，`Flux.block`。
3. 响应式方法间调用，应该组装为同一个`Publisher`。

   正确: return deviceService
   .findById(id)
   .flatMap(device->this.syncDeviceState(device));

   错误: deviceService
   .findById(id)
   .subscribe(device->this.syncDeviceState(device).subscribe())

   错误: this.syncDeviceState(deviceService.findById(id).block()).subscribe();

相关资料:

1. <a target='_blank' href='http://www.reactive-streams.org/'>reactive-streams</a>
2. <a target='_blank' href='https://projectreactor.io/'>project-reactor</a>
3. <a target='_blank' href='https://www.ibm.com/developerworks/cn/java/j-cn-with-reactor-response-encode/index.html?lnk=hmhm'>
   使用 Reactor 进行反应式编程</a>
4. <a target='_blank' href='https://space.bilibili.com/2494318'>simviso视频教程</a>

## 数据库规范

<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <p>平台使用<span class='explanation-title font-weight'>javax.persistence</span>包内的JPA规范自动创建及更新表结构，帮助开发者减少自己编写代码来将实体数据存储到数据库中。</p>
</div>

1. 命名规范
- 数据库对象，如：表、列、索引的命名的原则是简、短，易懂好记，有意义的英文单词、常用缩写，多个单词组成的。
- 数据表命名：必须带有模块前缀，如系统模块相关：`s_`, 设备模块：`dev_`。
- 如果命名由多个词汇组成，词汇之间加 _ 下划线，如：`dev_product`。

2. 设计规范
- 通常情况下每张表必须有主键。
- 每张表，每个字段都必须有正确的中文描述说明，以便生成数据字典时能明确字段含义。
- 业务数据表必要时冗余基础信息表的核心字段，如：部门编码、部门名称，需要成对出现在业务数据表中。
- 对于数据类型长度限制为8的倍数，如字符类型减少默认空间(255长度)的开辟。
- 不要盲目的加入其他索引，根据需要统一加索引。

## 用户界面

1. 对齐

- 层次结构清晰，观察 1 像素，引导视觉流向，让用户更流畅地接收信息。
- 如果页面的字段或段落较短、较散时，需要确定一个统一的视觉起点。

2. 多元化

- 根据不同的用户体验，可快速进行界面色调切换。
- 可快速折叠侧边栏和顶部导航栏。 根据用户喜好，可折叠侧边栏，默认不折叠侧边栏。
- 渐进式引导页以及首页的引导帮助用户急速上手体验平台。
- 列表操作体验清晰，不常用的操作加入按钮组隐藏。
- 列表查询包含多组条件查询可自由配置。

3. 布局色彩

- 响应式样式，卡片式布局和表格布局可动态切换，根据屏幕大小进行自适应布局。
- 色彩源于『自然』的设计价值观，设计师通过对自然场景的抽象捕捉。

4. 导航设计

- 在广义上，任何告知用户他在哪里，他能去什么地方以及如何到达那里的方式，都可以称之为导航。
- 支持侧边栏全部导航；或者顶部一级导航，二级以下导航在侧边栏。