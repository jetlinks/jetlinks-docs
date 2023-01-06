# 应用管理

应用管理将多个应用系统的登录简化为一次登录，实现多处访问、集中管控的业务场景。


<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

本功能仅在企业版中提供。

</div>

## 名称解释

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
            内部独立应用适用于将官方开发的其他应用与物联网平台相互集成，例如将可视化平台集成至物联网平台，或者将物联网平台集成至可视化平台。以实现多处访问、集中管控的业务场景。
             <li>内部集成应用</li>
            内部集成应用适用于将官方开发的其他应用与物联网平台相互集成，例如将可视化平台集成至物联网平台，或者将物联网平台集成至可视化平台。以实现多处访问、集中管控的业务场景。
            <li>微信网站应用</li>
            适用于通过微信账户登录jetlinks物联网平台。
           <li>钉钉企业内部应用</li>
           适用于通过钉钉账户登录jetlinks物联网平台。
           <li>第三方应用</li>
           适用于第三方应用与物联网平台相互集成。例如将公司业务管理系统集成至物联网平台，或者将物联网平台集成至业务管理系统。以实现多处访问、集中管控的业务场景。
            </td>
          </tr>
          <tr>
            <td>接入方式</td>
            <td>
            <li>页面集成</li>
            将其他应用系统的页面集成至jetlinks物联网平台。集成时通常需要配置API服务。（内部集成时无需配置）
            <li>API客户端</li>
            将jetlinks物联网平台作为客户端集成到其他应用系统中，其他应用系统可通过Oauth2的方式访问物联网平台。
             <li>API服务</li>
             将jetlinks物联网API能力提供给其他应用，其他应用可通过接口的方式直接使用API能力。
             <li>单点登录</li>
             使用第三方应用账户登录jetlinks物联网平台。
            </td>
          </tr>
             </tbody>
        </table>

## 内部独立应用
适用于将官方开发的其他应用与物联网平台相互集成，例如将可视化平台集成至物联网平台，或者将物联网平台集成至可视化平台。本文以将可视化平台集成至物联网平台为例。

#### 前置条件
1、可视化平台与物联网平台已成功部署，物联网平台的地址为：V1.jetlinks.cn，可视化平台地址为：https://view.jetlinks.cn

### 操作步骤
1.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
2.选择**内部独立应用**，勾选**页面集成**、**API服务**配置，填写相关配置后点击**保存**。
![](./img/288.png)

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>appid</td>
            <td>调用API服务时所需的用户账号。配置API服务时，系统自动创建</td>
          </tr>
          <tr>
            <td>secureKey</td>
            <td>调用API服务时所需的用户密码,系统自动创建，可自定义编辑</td>
          </tr>
          <tr>
            <td>回调地址</td>
            <td>授权之后跳转到具体页面的回调地址，填写示例如截图：https://view.jetlinks.cn</td>
          </tr>
          <tr>
            <td>角色</td>
            <td>为API用户分配角色，根据绑定的角色，进行系统菜单赋权</td>
          </tr>
          <tr>
            <td>组织</td>
            <td>为API用户分配所属组织，根据绑定的组织，进行数据隔离</td>
          </tr>
          <tr>
            <td>接入地址</td>
            <td>访问其它平台的地址，填写示例如截图：https://view.jetlinks.cn</td>
          </tr>
           <tr>
            <td>路由方式</td>
            <td><li>hash：使用URL的hash来模拟一个完整的URL, 其显示的网络路径中会有 “#” 号</li>
            <li>history：路径中不包含“#”。依赖于Html5 的 history api</li>
            </td>
          </tr>
        </tbody>
      </table>

3.**登录**view可视化大屏系统，进入**系统管理>应用管理**菜单，点击**新增**，配置**API客户端**。

![](./img/view-client.png)

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>接口地址</td>
            <td>访问API服务的地址，填写示例如截图：https://V1.jetlinks.cn/ietlinks</td>
          </tr>
          <tr>
            <td>授权地址</td>
            <td>认证授权地址，填写示例如截图：https://V1.jetlinks.cn/#/oauth</td>
          </tr>
          <tr>
            <td>token地址</td>
            <td>身份认证地址，填写示例如截图：https://V1.jetlinks.cn/jetlinks/oauth2/token</td>
          </tr>
          <tr>
            <td>回调地址</td>
            <td>授权之后跳转到具体页面的回调地址，填写示例如截图：https://V1.jetlinks.cn</td>
          </tr>
          <tr>
            <td>appid</td>
            <td>填写对应物联网平台应用的API服务中生成的appid，填写示例如截图：TZDcypZcdiCsPd83。</td>
          </tr>
          <tr>
            <td>appKey</td>
            <td>填写对应物联网平台应用的API服务中生成的appKey，填写示例如截图：pkHr7DZ36KRTWitmMksDTab3XxW4raBK</td>
          </tr>
        </tbody>
      </table>

4.物联网平台端，在列表页点击**应用卡片-其他-集成菜单**在弹框页面，勾选需要集成的菜单，然后点击**确定**。
![](./img/Integrated-menu.png)


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 接入方式选择页面集成时将在其应用卡片中出现集成菜单按钮，可选择要集成哪些菜单至系统中。
</div>

<!-- ## 内部集成应用
适用于将官方开发的其他应用集成至jetlinks物联网平台，并且共享后端服务。本文以接入view可视化大屏系统为例。

#### 前置条件
1、可视化平台与物联网平台已成功部署，物联网平台的地址为：V1.jetlinks.cn

### 操作步骤
1.**登录**view可视化大屏系统，进入**系统管理>应用管理**菜单，点击**新增**，选择**内部集成应用**，配置**API客户端**。
![](./img/view-client.png)

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>接口地址</td>
            <td>访问API服务的地址，填写示例如截图：https://v1 .jetlinks.cn/ietlinks</td>
          </tr>
        </tbody>
      </table>

2.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
3.选择**内部集成应用**，勾选**页面集成**配置，填写相关配置后点击**保存**。
![](./img/Internal-integration.png)

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>接入地址</td>
            <td>填写访问其它平台的地址，填写示例如截图：https://view.jetlinks.cn</td>
          </tr>
        </tbody>
      </table>

4.在弹出的**集成菜单**弹框中，勾选需要集成的菜单，然后点击**确定**。
![](./img/Integrated-menu.png)


<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 接入方式选择页面集成时将在其应用卡片中出现集成菜单按钮，可选择要集成哪些菜单至系统中。
</div> -->

## 微信网站应用
适用于通过微信账户登录jetlinks物联网平台。

### 前置条件
1.已经在微信开放平台开通网站应用。

### 操作步骤
1.**登录**微信开放平台，进入**系统管理>应用管理**菜单，查看**AppID**、**AppSecret**。
![](./img/292.png)
2.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
3.选择**微信网站应用**，勾选**单点登录**配置，填写相关配置后点击**保存**。
![](./img/289.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 自动创建用户开启后，初次通过微信网站应用登录JetLinks物联网平台时将自动在平台端创建与之绑定的新用户。
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
            <td>appid</td>
            <td>应用唯一标识，此处填写操作步骤1截图中的AppID</td>
          </tr>
           <tr>
            <td>appsecret</td>
            <td>应用唯一标识的秘钥，此处填写操作步骤1截图中的AppSecret</td>
          </tr>
        </tbody>
      </table>
  
  4.进入物联网平台首页，页面底部将出现**钉钉登录的icon**，点击icon，进行**钉钉登录**。
  ![](./img/304.png)
  ![](./img/305.png)

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

初次扫码登录时将进行账号绑定。

</div>

## 钉钉企业内部应用
适用于通过钉钉账户登录jetlinks物联网平台。
### 前置条件
1.已经在钉钉开放平台开通了企业内部应用。

### 操作步骤
1.**登录**钉钉开发平台，进入**系统管理>应用管理**菜单，查看**appKey**、**appSecret**。
![](./img/ding-app.png)
2.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
3.选择**钉钉企业内部应用**，勾选**单点登录**配置，填写相关配置后点击**保存**。
![](./img/ding-app1.png)

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>appkey</td>
            <td>应用唯一标识，此处填写操作步骤1截图中的appKey</td>
          </tr>
           <tr>
            <td>appsecret</td>
            <td>应用唯一标识的秘钥，此处填写操作步骤1截图中的AppSecret</td>
          </tr>
        </tbody>
      </table>

  4.进入物联网平台首页，页面底部将出现**钉钉登录的icon**，点击icon，进行**钉钉登录**。
  ![](./img/304.png)
  ![](./img/306.png)

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

初次扫码登录时将进行账号绑定。

</div>

<!-- ## 第三方应用
适用于将其他三方应用集成至jetlinks物联网平台。本文以第三方应用集成jetlinks物联网平台API服务为例，实现三方系统通过接口调用平台API能力。

### 操作步骤
1.**登录**Jetlinks物联网平台，进入**系统管理>应用管理**菜单，点击**新增**。</br>
![](./img/287.png)
2.选择**第三方应用**，勾选**API服务**配置，填写相关配置后点击**保存**。
![](./img/290.png)

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>appid</td>
            <td>调用API服务时所需的用户账号。配置API服务时，系统自动创建</td>
          </tr>
          <tr>
            <td>secureKey</td>
            <td>调用API服务时所需的用户密码,系统自动创建，可自定义编辑</td>
          </tr>
          <tr>
            <td>角色</td>
            <td>为API用户分配角色，用以访问平台内的菜单</td>
          </tr>
          <tr>
            <td>组织</td>
            <td>为API用户绑定所属组织</td>
          </tr>
          <tr>
            <td>redirectUrl</td>
            <td>授权之后跳转到具体页面的回调地址，url地址+端口</td>
          </tr>
          <tr>
            <td>IP白名单</td>
            <td>白名单中的地址可调用API服务，多个地址用回车分隔，不填默认均可访问</td>
          </tr>
        </tbody>
      </table>

3.点击对应**应用管理卡片**的**其他**按钮，选择**赋权**。该应用可调用已赋权范围内的API服务。
![](./img/291.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
 接入方式选择API服务时将在对应应用的卡片中出现赋权、查看API按钮。
</div> -->
