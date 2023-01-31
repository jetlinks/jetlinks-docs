# API配置

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 统一配置系统内对外输出的API服务，API配置将在为第三方平台赋权时引用。
</div>

## 指导介绍

  <p>1. <a href="/System_settings/System_api_configuration.html#平台api配置" >平台API配置</a></p>
  <p>2. <a href="/System_settings/System_api_configuration.html#自定义配置swagger-api" >配置API</a></p>

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

本功能仅在企业版中提供。

</div>

## 平台API配置
### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**系统管理**，在左侧导航栏，选择**API配置**，进入详情页。</br>
3.勾选需要支持对外输出的API服务，然后点击**保存**。</br>
![](./img/193.png)

## 自定义配置Swagger API
<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

本平台使用的是spring boot集成springdoc接口文档生成

</div>

### 1.在 <code>jetlinks-pro/jetlinks-standalone/src/main/resources/application.yaml</code>文件中进行配置
![在yaml文件中配置API](./img/api-swagger-configuration-yaml.png)
### 2.在<code>org.jetlinks.pro.standalone.configuration.doc.SwaggerConfiguration</code>配置swaager相关说明信息和JWT认证
![在配置类中配置swagger相关](./img/api-swagger-configuration-code.png)

