# 应用管理
应用管理将多个应用系统的登录简化为一次登录，实现多处访问、集中管控的业务场景。

## 名称解释
</div>

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>应用</td>
            <td>
            <li>内部独立应用</li>
            适用于将官方开发的其他应用集成至jetlinks物联网平台。例如可将可视化大屏集成至物联网平台。集成时jetlinks物联网平台端需要勾选页面集成、API服务，view端需勾选API客户端。集成后的view后端独立运行。
             <li>内部集成应用</li>
             适用于将官方开发的其他应用集成至jetlinks物联网平台。例如可将可视化大屏集成至物联网平台。集成时jetlinks物联网平台端需要勾选页面集成，view端需勾选API客户端。集成后的view后端与jetlinks在同一环境下运行。
            <li>微信网站应用</li>
            适用于通过微信账户登录jetlinks物联网平台。
           <li>钉钉企业内部应用</li>
           适用于通过钉钉账户登录jetlinks物联网平台。
           <li>第三方应用</li>
           适用于将其他三方应用集成至jetlinks物联网平台。例如可将某公司OA系统集成至物联网平台。集成时jetlinks物联网平台端需要勾选页面集成、API服务，OA系统端作为客户端需自行设置接入所需的接入配置。
            </td>
          </tr>
          <tr>
            <td>接入方式</td>
            <td>
            <li>页面集成</li>
            将其他的页面集成至jetlinks物联网平台。集成时通常也需要配置API服务。
            <li>API客户端</li>
            将jetlinks物联网平台作为客户端集成到其他系统中，其他系统可通过Oauth2的方式直接访问物联网平台。
             <li>API服务</li>
             将jetlinks物联网API能力提供给其他应用，其他应用可通过接口的方式直接使用API能力，无须自行编写后端逻辑。
             <li>单点登录</li>
             使用第三方应用账户登录jetlinks物联网平台。
            </td>
          </tr>
             </tbody>
        </table>

## 内部独立应用
适用于将官方开发的其他应用集成至jetlinks物联网平台，并且后端各自独立运行。本文以接入view可视化大屏系统为例。

### 操作步骤
1.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
2.选择**内部独立应用**，勾选**页面集成**、**API服务**配置，填写相关配置后点击**保存**。
![](./img/288.png)
3.**登录**view可视化大屏系统，进入**系统管理>应用管理**菜单，点击**新增**，配置**API客户端**。
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 接入方式选择页面集成时将在其应用卡片中出现集成菜单按钮。
</div>

## 内部集成应用
适用于将官方开发的其他应用集成至jetlinks物联网平台，并且共享后端服务。本文以接入view可视化大屏系统为例。

### 操作步骤
1.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
2.选择**内部集成应用**，勾选**页面集成**、**API服务**配置，填写相关配置后点击**保存**。
![](./img/288.png)
3.**登录**view可视化大屏系统，进入**系统管理>应用管理**菜单，点击**新增**，配置**API客户端**。

## 微信网站应用
适用于通过微信账户登录jetlinks物联网平台。
### 前置条件
1.已经在微信开放平台开通网站应用。

### 操作步骤
1.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
2.选择**微信网站应用**，勾选**单点登录**配置，填写相关配置后点击**保存**。
![](./img/289.png)
3.**登录**微信开放平台，进入**系统管理>应用管理**菜单，点击**新增**，配置**API客户端**。
![](./img/292.png)

## 第三方应用
适用于将其他三方应用集成至jetlinks物联网平台。本文以第三方应用集成jetlinks物联网平台API服务为例，实现三方系统通过接口调用平台API能力。

### 操作步骤
1.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
2.选择**第三方应用**，勾选**API服务**配置，填写相关配置后点击**保存**。
![](./img/290.png)
3.点击对应**应用管理卡片**的**其他**按钮，选择**赋权**。该应用可调用已赋权范围内的API能力。
![](./img/291.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 接入方式选择API服务时将在对应应用的卡片中出现赋权、查看API按钮。
</div>
