# 数据流传

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    需要将告警数据推送至<code>Kafka</code>
</div>




## 指导介绍

<p>1. <a href='/dev-guide/data-flow.html#填写示例'>填写示例</a></p>

## 问题指引

<table>
<tr>
    <td><a href="/dev-guide/data-flow.html#如何搭建kafka服务">如何搭建kafka服务</a></td>
    </tr>
</table>





## 填写示例

##### 操作步骤

1.**登录**Jetlinks物联网平台。
2.在左侧导航栏，选择**告警中心>基础配置**，进入详情页。
3.点击页面顶部tab切换至**数据流转**tab页。
4.填写配置信息，然后点击**保存**。
![img](http://doc.jetlinks.cn/assets/img/89.e58c7eca.png)

| 参数      | 说明                         |
| --------- | ---------------------------- |
| kafka地址 | 填写kafka地址，IP地址+端口。 |
| topic     | 填写topic地址。              |
| 状态      | 启用后配置将生效。           |



## 常见问题

### 如何搭建Kafka服务

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>
    <p>
        Q：Kafka服务如何搭建
    </p>
    <p>
        A：在<code>jetlinks-components</code>下的<code>messaging-component</code>内的<code>kafka-componet</code>模块内有平台提供的docker-compose.yml文件可以搭建
    </p>
</div>

