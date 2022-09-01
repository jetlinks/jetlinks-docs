# 使用MQTT接入时的常见问题

在网络组件中创建了MQTT服务,并且在设备网关中创建了MQTT服务设备网关,MQTT客户端仍然无法连接.

1. 请确定网络组件中的MQTT服务以及MQTT服务设备网关已启动.
2. 如果是docker环境,请确定MQTT服务对应的端口已经映射到主机.

## 常见错误码

错误码: `CONNECTION_REFUSED_IDENTIFIER_REJECTED`

平台无法识别客户端标识(`clientId`),可能是设备未在平台进行激活.

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

`clientId` 需要和`设备实例ID`一致.如果在平台已经激活,并且`clientId`无误,请尝试重新激活设备.

</div>

错误码:`CONNECTION_REFUSED_NOT_AUTHORIZED`

MQTT客户端没有传认证信息(username,password).

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>

MQTT服务设备网关要求所有mqtt客户端都必须传递认证信息,但是具体的认证策略由自定义的消息协议决定.

</div>

错误码:`CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD`

MQTT客户端认证信息错误(用户名密码错误).
1. 请检查传递的用户名密码是否符合设备使用消息协议的认证策略,以及是否符合在对应产品中的配置信息.
3. 修改了`用户名密码`等信息后,是否重新`应用配置`.

错误码: `CONNECTION_REFUSED_SERVER_UNAVAILABLE`

服务端发生了错误.可能原因:

1. 未创建 `MQTT服务设备网关`或`未正确与MQTT服务进行关联`.
2. `MQTT服务设备网关`被`停止`.
3. 其他,请据系统日志排查.

## 提示：mqtt客户端认证失败：不支持的认证方式或无法获取认证结果
原因：设备网关选择了认证协议，但对应协议没有实现认证接口。  
解决方案：  
    1. 取消认证协议。  
    2. 在协议中实现Authenticator接口，[推荐查看认证器](/dev-guide/protocol-support.html#认证器)。  