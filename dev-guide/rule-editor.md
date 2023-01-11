# 规则编排

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>
     提供可视化,流程化的数据(逻辑)处理工具。支持对实时数据流进行计算，并将计算结果进行推送MQTT、写入Kafka、写入数据库等操作。
    </p>
</div>



## 指导介绍

<p>1. <a href='/dev-guide/rule-editor.html#新增'>新增规则编排</a></p>

<p>2. <a href='/dev-guide/rule-editor.html#编辑'>编辑规则编排</a></p>

<p>3. <a href='/dev-guide/rule-editor.html#启用-禁用'>启用/禁用规则编排</a></p>

<p>4. <a href='/dev-guide/rule-editor.html#删除'>删除规则编排</a></p>



## 新增

#### 具体操作步骤

<p>1、<b>登录</b>JetLinks物联网平台。</p>

<p>2、在左侧导航栏，选择<b>规则引擎>规则编排</b>，进入列表页。</p>

![进入规则编排界面](./images/rule-editor/into-rule-editor.png)

<p>3、点击<b>新增</b>按钮，在弹框页填写名称，然后点击<b>确定</b>按钮。</p>


![新增规则编排](./images/rule-editor/add-new-rule.png) 

![新增规则编排成功](./images/rule-editor/add-new-rule-success.png)

<p>4、点击卡片，进入画布页，<b>拖拽</b>左侧组件到画布页，并<b>双击</b>组件，填写相关配置。</p>


![编辑规则编排组件](./images/rule-editor/edit-rule-components.png)

右侧为该组件的**配置详情** 

![编辑规则编排组件](./images/rule-editor/config-rule-components.png)



<p>5、多个组件之间，通过<b>连线</b>进行连接，上游的数据会通过<b>连线</b>流到下游。</p>

![规则连线](./images/rule-editor/rule-line.png) 

<p>6、配置完节点后点击页面右上角<b>部署</b>。</p>

![部署规则编排](./images/rule-editor/deploy-rule.png)

## 编辑

#### 操作步骤

<p>1、<b>登录</b>Jetlinks物联网平台。<br>
2、在左侧导航栏，选择<b>规则引擎>规则编排</b>，进入列表页。<br>
3、点击具体数据的<b>编辑</b>按钮，在弹框页编辑名称，然后点击<b>确定</b>按钮。</p>

![编辑界面](./images/rule-editor/edit-page.png)



## 启用/禁用

#### 操作步骤

<p>1、<b>登录</b>Jetlinks物联网平台。<br>
2、在左侧导航栏，选择<b>规则引擎>规则编排</b>，进入列表页。<br>
3、点击具体数据的<b>启用/禁用</b>按钮，然后点击<b>确定</b>。</p>


![启动或停止规则编排](./images/rule-editor/start-or-stop-rule.png)



## 删除

#### 操作步骤

<p>1、<b>登录</b>Jetlinks物联网平台。<br>
2、在左侧导航栏，选择<b>规则引擎>规则编排</b>，进入列表页。<br>
3、点击具体数据的<b>删除</b>按钮，在弹框页编辑名称，然后点击<b>确定</b>按钮。</p>


![删除规则编排](./images/rule-editor/delete-rule.png)