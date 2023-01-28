# 远程升级
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

远程升级及空中下载技术，通过JetLinks物联网平台的远程升级功能，可对分布在各地的IoT设备进行远程升级。

</div>

## 指导介绍

  <p>1. <a href="/Mocha_ITOM/remote_upgrade.html#新增">新增</a></p>
  <p>2. <a href="/Mocha_ITOM/remote_upgrade.html#升级任务">升级任务</a></p>
  <p>3. <a href="/Mocha_ITOM/remote_upgrade.html#编辑">编辑</a></p>
  <p>4. <a href="/Mocha_ITOM/remote_upgrade.html#删除">删除</a></p>

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

本功能仅在企业版中提供。

</div>

## 新增
### 操作步骤
1.**登录**JetLinks物联网平台。</br>
2.在左侧导航栏，选择**运维管理>远程升级**，进入列表页。</br>
3.点击**新增**按钮，在弹框页填写相关信息，然后点击**确定**。
![](./img/90.png)
<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>名称</td>
            <td>为固件命名，最多可输入64个字符。</td>
          </tr>
          <tr>
            <td>所属产品</td>
            <td>选择支持固件升级的产品与固件进行关联。产品是否支持升级由关联的协议包决定。</td>
          </tr>
          <tr>
            <td>版本号</td>
            <td>拟定上传固件的版本号。 </td>
          </tr>
           <tr>
            <td>版本序号</td>
            <td>拟定固件版本序号。 设备拉取时可按序号从小往大升级，也可直接拉取最大序号的固件。</td>
          </tr>
          <tr>
            <td>签名方式</td>
            <td>固件加密方式</td>
          </tr>
           <tr>
            <td>签名</td>
            <td>输入本地文件进行签名加密后的值，以验证上传后的文件是否与本地一致。</td>
          </tr>
           <tr>
            <td>固件上传</td>
            <td>上传固件文件</td>
          </tr>
           <tr>
            <td>其他配置</td>
            <td>自定义远程升级所需的其他参数配置。</td>
          </tr>
           <tr>
            <td>说明</td>
            <td>备注说明，最多可输入200个字符。</td>
          </tr>
        </tbody>
      </table>

## 升级任务
### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**运维管理>远程升级**，进入列表页。</br>
3.点击具体数据的**升级任务**按钮，进入列表页。</br>
4.点击**新增**按钮，在弹框页填写任务信息，然后点击确定。</br>
![](./img/91.png)

### 后续操作
1.点击**查看**按钮，查看升级进度。</br>
![](./img/92.png)
2.点击**批量重试**按钮，对升级失败的升级进行再次下方升级指令。
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
只有升级方式为平台推送类型时才支持此功能
</div>

3.点击**停止**按钮，不再对等待升级的设备下发升级指令，或点击**继续升级**按钮，对已停止状态的设备继续下发升级指令。
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
存在等待升级状态的设备时显示停止按钮，存在已停止状态的设备时显示继续升级按钮。
</div>

## 编辑
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**运维管理>远程升级**，进入列表页。</br>
3.点击具体数据的**编辑**按钮，在弹框页中编辑相关信息，然后点击**确定**。</br>
![](./img/93.png)

## 删除
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**运维管理>远程升级**，进入列表页。</br>
3.点击具体数据的**删除**按钮，然后点击**确定**。</br>
![](./img/94.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
固件下存在升级任务数据时，不可删除。
</div>