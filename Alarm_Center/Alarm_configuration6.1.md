
## 告警配置
统一管理系统内的告警规则配置，触发告警规则时可产生对应的告警记录数据。
#### 新增
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警配置**，进入列表页。</br>
![](./img/82.png)
3.点击新增按钮，在详情页中填写配置信息，然后点击**确定**。</br>
![](./img/83.png)

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
            <td>为告警规则命名，最多可输入64个字符。</td>
          </tr>
          <tr>
            <td>类型</td>
            <td>定义告警目标类型，支持产品、设备、组织、其他4种类型。</td>
          </tr>
          <tr>
            <td>级别</td>
            <td>设定告警规则的告警级别，单选。 </td>
          </tr>
           <tr>
            <td>说明</td>
            <td>告警规则的备注说明信息，非必填。</td>
          </tr>
        </tbody>
      </table>

4.点击关联场景联动，根据业务需要绑定对应的场景联动规则。
![](./img/scene.png)

#### 编辑
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警配置**，进入列表页。</br>
3.点击具体告警配置的**编辑**按钮，在详情页面中修改配置信息，然后点击**确定**。</br>
![](./img/edit.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  告警配置已产生告警数据时，类型、关联触发场景不支持编辑。
</div>


#### 启用/禁用
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警配置**，进入列表页。</br>
3.点击具体告警配置的**启用/禁用**按钮，然后点击**确定**。</br>
![](./img/$81.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  告警配置未关联场景联动将导致启用失败。
</div>

#### 删除
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警配置**，进入列表页。</br>
3.点击具体告警配置的**删除**按钮，然后点击**确定**。</br>
![](./img/85.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
告警配置已产生告警数据时，不支持删除。
</div>

#### 手动触发
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警配置**，进入列表页。</br>
3.点击具体告警配置的**手动触发**按钮，然后点击**确定**。</br>
![](./img/86.png)
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
告警配置所关联的场景联动包含手动触发类型时，将出现此按钮，点击后将生成一条对应的告警记录数据。
</div>

## 基础配置
告警基础配置，包括告警级别、告警数据流转的统一配置管理。
### 告警级别配置
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>基础配置**，进入详情页。</br>
3.填写告警级别配置信息，然后点击**保存**。</br>
![](./img/88.png)

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
告警严重程度：级别1>级别2>级别3>级别4>级别5。此处配置的告警级别架构在告警规则配置页面被引用。
</div>

### 数据流转配置

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

本功能仅在企业版中提供。

</div>

##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>基础配置**，进入详情页。</br>
3.点击页面顶部tab切换至**数据流转**tab页。</br>
4.填写配置信息，然后点击**保存**。</br>
![](./img/89.png)

<table class='table'>
        <thead>
            <tr>
              <td>参数</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>kafka地址</td>
            <td>填写kafka地址，IP地址+端口。</td>
          </tr>
          <tr>
            <td>topic</td>
            <td>填写topic地址。</td>
          </tr>
          <tr>
            <td> 状态</td>
            <td>启用后配置将生效。</td>
          </tr>
          </tbody>
</table>

## 告警记录
统一维护根据告警规则产生的告警数据，可对告警记录进行详情查看与告警处理。
### 告警处理
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警记录**，进入列表页。</br>
![](./img/90.png)
3.点击具体告警数据的**告警处理**按钮，在弹框页中填写处理结果，然后点击**确定**。</br>
![](./img/91.png)

### 告警日志
查看同一个告警目标下的历史告警数据。
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警记录**，进入列表页。</br>
3.点击具体告警数据的**告警日志**按钮，进入详情页。</br>
![](./img/92.png)
4.点击具体日志数据的**查看**按钮，查看详情。</br>
![](./img/93.png)

### 处理记录
查看同一个告警目标下的历史告警处理记录，包括人工处理和系统自动恢复处理的记录。
##### 操作步骤
1.**登录**Jetlinks物联网平台。</br>
2.在左侧导航栏，选择**告警中心>告警记录**，进入列表页。</br>
3.点击具体告警数据的**处理记录**按钮，在弹框页中查看处理记录明细数据。</br>
![](./img/94.png)



