# 物联卡管理

## 物联卡管理

物联卡管理支持对物联卡进行统一纳管。现支持**移动OneLink**、**电信CTWing**、**联通Unicon**3个平台。</br>

#### 新增
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**物联卡管理**，进入列表页。</br>
![](./img/001.png)
3.点击**新增**按钮，弹出弹框，填写物联卡信息，然后点击**确定**。</br>
![](./img/002.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  ICCID需符合对应第三方平台定义的输入规范，否则不能成功添加物联卡。系统会基于ICCID判断该物联卡属于哪一个平台。

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
            <td>卡号</td>
            <td>物联卡号码</td>
          </tr>
          <tr>
            <td>ICCID</td>
            <td>集成电路卡识别码，IC卡的唯一识别号码</td>
          </tr>
          <tr>
            <td>平台对接</td>
            <td>定义该物联卡使用具体的平台接入配置与IOT平台进行数据交互</td>
          </tr>
          <tr>
            <td>运营商</td>
            <td>定义该物联卡所属通信运营商</td>
          </tr>
          <tr>
            <td>类型</td>
            <td>定义该物联卡的业务类型，包括年卡、季卡、月卡、其他</td>
          </tr>
        </tbody>
      </table>

#### 编辑
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**物联卡管理**，进入列表页。</br>
![](./img/001.png)
3.点击具体物联卡的**编辑**按钮，弹出弹框，编辑物联卡信息，然后点击**确定**。</br>
![](./img/003.png)

#### 查看
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**物联卡管理**，进入列表页。</br>
![](./img/001.png)
3.点击具体物联卡的**查看**按钮，进入详情页。</br>
![](./img/007.png)

#### 绑定设备
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**物联卡管理**，进入列表页。</br>
![](./img/001.png)
3.点击具体物联卡的**绑定设备**按钮，弹出弹框，勾选需要绑定的设备后，点击**确定**。</br>
![](./img/004.png)

#### 激活
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**物联卡管理**，进入列表页。</br>
![](./img/001.png)
3.点击具体物联卡的**激活**按钮，弹出弹框，然后点击**确定**。</br>
![](./img/005.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

  激活指一张新卡正式投入使用，激活后物联卡将置为**正常**状态。

</div>

#### 删除
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**物联卡管理**，进入列表页。</br>
![](./img/001.png)
3.点击具体物联卡的**删除**按钮，弹出弹框，然后点击**确定**。</br>
![](./img/006.png)

#### 批量操作
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**物联卡管理**，进入列表页。</br>
![](./img/001.png)
3.勾选需要批量操作的数据，然后点击页面左上方的**批量操作**按钮，选择要执行的操作，然后点击**确定**。</br>
![](./img/008.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  未勾选数据时直接点击批量导出，将导出所有分页的数据。
</div>

## 充值管理

对**移动OneLink**的物联卡账户进行充值。</br>

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>
  本平台仅提供充值入口，具体充值结果需以运营商的充值结果为准。
</div>

#### 充值
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**充值管理**，进入列表页。</br>
![](./img/009.png)
3.点击**新增**按钮，弹出弹框，填写充值信息，然后点击**确定**。</br>
![](./img/010.png)
4.在对应渠道的支付页面完成支付。

## 平台接入

配置IOT平台与其他第三方物联卡平台对接所需的参数，实现平台间的数据互联互通。

#### 新增
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**平台接入**，进入列表页。</br>
![](./img/011.png)
3.点击**新增**按钮，进入详情页，填写平台接入配置信息，然后点击**保存**。</br>
![](./img/012.png)

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-jinggao explanation-icon'></span>
    <span class='explanation-title font-weight'>注意</span>
  </p>

  第三方平台的接口暂不支持点击保存时校验配置是否正确，在**物联卡管理**页面进行物理卡激活、停用、复机、同步状态时将进行相应提示。

</div>

#### 编辑
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**平台接入**，进入列表页。</br>
![](./img/011.png)
3.点击具体平台配置的**编辑**按钮，进入详情页，编辑平台接入配置信息，然后点击**保存**。</br>
![](./img/013.png)

#### 启用禁用
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**平台接入**，进入列表页。</br>
![](./img/011.png)
3.点击具体平台配置的**启用/禁用**按钮，弹出弹框，然后点击**确定**。</br>
![](./img/014.png)

#### 删除
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**平台接入**，进入列表页。</br>
![](./img/011.png)
3.点击具体平台配置的**删除**按钮，弹出弹框，然后点击**确定**。</br>
![](./img/015.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  正常状态下，不可删除。
</div>

## 操作记录

对物联卡的激活、停用、复机进行详细记录。

#### 查看
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.点击顶部**物联卡**，在左侧导航栏，选择**操作记录**，进入列表页。</br>
![](./img/016.png)
