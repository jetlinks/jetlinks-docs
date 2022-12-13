# 平台开发指导手册

## 常见开发问题

- <a target='_self' href='/dev-guide/code-guide.html#%E6%BA%90%E7%A0%81%E6%8B%89%E5%8F%96%E5%8F%8A%E5%AD%90%E6%A8%A1%E5%9D%97%E6%9B%B4%E6%96%B0%E6%8C%87%E5%8D%97'>
   如何拉取源码及更新子模块？</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E4%B8%AD%E9%97%B4%E4%BB%B6%E9%83%A8%E7%BD%B2%E5%8F%8A%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98'>
  中间件部署及常见问题</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E5%9C%A8jetlinks%E4%B8%8A%E6%9E%84%E5%BB%BA%E8%87%AA%E5%B7%B1%E7%9A%84%E4%B8%9A%E5%8A%A1%E5%8A%9F%E8%83%BD'>
   在JetLinks上构建自己的业务功能？</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9D%97%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8es'>
   自定义模块如何使用es？</a>  
- <a target='_self' href='/dev-guide/code-guide.html#%E5%85%B3%E4%BA%8E%E5%B9%B3%E5%8F%B0%E5%AD%98%E5%82%A8%E7%9A%84%E8%AF%B4%E6%98%8E'>
   关于平台存储的说明</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E7%9B%91%E5%90%AC%E5%AE%9E%E4%BD%93%E5%8F%98%E5%8C%96%E5%81%9A%E4%B8%9A%E5%8A%A1'>
   实体变更后如何触发自己的业务流程？</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E7%9B%91%E5%90%AC%E5%AE%9E%E4%BD%93%E5%8F%98%E5%8C%96%E5%81%9A%E4%B8%9A%E5%8A%A1'> 自定义模块如何引入es？</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E4%BD%BF%E7%94%A8%E6%B6%88%E6%81%AF%E6%80%BB%E7%BA%BF'>
   如何获取消息总线内的数据？</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E6%B7%BB%E5%8A%A0%E8%87%AA%E5%AE%9A%E4%B9%89%E5%AD%98%E5%82%A8%E7%AD%96%E7%95%A5'>
  如何添加自定义存储策略？</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E4%B8%BB%E5%8A%A8%E4%BB%8E%E7%AC%AC%E4%B8%89%E6%96%B9%E5%B9%B3%E5%8F%B0%E3%80%81%E8%AE%BE%E5%A4%87%E8%8E%B7%E5%8F%96%E6%95%B0%E6%8D%AE'>
   主动从设备获取属性、事件如何操作？</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E7%9F%AD%E8%BF%9E%E6%8E%A5%E3%80%81%E4%BD%8E%E5%8A%9F%E8%80%97%E7%B1%BB%E8%AE%BE%E5%A4%87%E6%8E%A5%E5%85%A5%E5%B9%B3%E5%8F%B0'>
   短连接、低功耗类设备接入平台 </a>
- <a target='_self' href='/dev-guide/code-guide.html#%E8%AE%BE%E5%A4%87%E7%9B%B8%E5%85%B3%E6%95%B0%E6%8D%AE%E6%8E%A8%E9%80%81%E5%88%B0%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6'>
   设备数据推送到消息中间件 </a>
- <a target='_self' href='/dev-guide/code-guide.html#%E7%AC%AC%E4%B8%89%E6%96%B9%E5%B9%B3%E5%8F%B0%E8%AF%B7%E6%B1%82jetlinks%E6%9C%8D%E5%8A%A1%E6%8E%A5%E5%8F%A3'>
   第三方平台请求JetLinks服务接口</a>
- <a target='_self' href='/dev-guide/code-guide.html#%E8%87%AA%E5%AE%9A%E4%B9%89sql%E6%9D%A1%E4%BB%B6%E6%9E%84%E9%80%A0%E5%99%A8'>
  自定义SQL条件构造器</a>
- <a target='_self' href=''>
  如何在协议包里面使用Redis？</a>
- <a target='_self' href=''>
  如何在协议包里面使用平台的业务方法？</a>






































### 自定义SQL条件构造器

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  <li>产品在正常状态时，按钮显示为禁用；产品在启用状态时，按钮显示为启用。</li>
  <li>产品禁用后，设备无法再接入。但不影响已经接入的设备。</li>

</div>

```java
//此处将具体代码实现放入
//1.对关键部分代码进行步骤梳理及注释说明
//2.对核心部分代码用醒目的文字进行说明，说明内容包括但不限于设计思想、设计模式等
```

#### 核心类说明

| 类名 | 方法名 | 返回值 | 说明 |
|----------------| -------------------------- |--------|---------------------------|-------------------|
| DeviceOperator | getSelfConfig() |`Mono<Value>` | 从缓存中获取设备自身的配置，如果不存在则返回`Mono.empty()`|

#### 常见问题

*对开发过程中出现的问题进行总结*


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>

  <li>产品在正常状态时，按钮显示为禁用；产品在启用状态时，按钮显示为启用。</li>
  <li>产品禁用后，设备无法再接入。但不影响已经接入的设备。</li>

</div>


<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>

  <li>产品在正常状态时，按钮显示为禁用；产品在启用状态时，按钮显示为启用。</li>
  <li>产品禁用后，设备无法再接入。但不影响已经接入的设备。</li>

</div>

<div class='explanation error'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>危险</span>
  </p>

若设备限制数量不能满足您的业务需求，请
<a>提交工单</a>
说明您的需求。

</div>

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
若设备限制数量不能满足您的业务需求，请
<a>提交工单</a>
说明您的需求。
</div>

