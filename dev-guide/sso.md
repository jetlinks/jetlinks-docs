# 统一单点登录

从`1.5.0版本`后,企业版增加了统一的单点登录支持. 默认实现了通用OAuth2方式登录，还可以自定义实现登录方案.

## 流程

![sso](./sso.svg)

## 自定义单点登录

实现接口`ThirdPartyProvider`,并注入到spring即可.

## 配置

```yml
system:
  config:
    scopes:
      - id: paths
        name: 访问路径配置
        public-access: true
        properties:
          - key: base-path
            name: 接口根路径
            default-value: http://localhost:9000/api
          - key: sso-redirect
            name: sso回调路径
            default-value: http://localhost:9000  #访问平台的根地址
          - key: sso-bind
            name: sso用户绑定路径
            default-value: http://localhost:9000/#/account/center/bind # 和第三方用户绑定的地址,通常就是登录本平台的地址.
          - key: sso-token-set
            name: sso登陆成功后Token设置路径
            default-value: http://localhost:9000/api/token-set.html # 设置token的地址,此地址将参数中的token设置到本地,然后跳转到首页
```
## OAuth2

平台实现了通用到OAuth2方式登录，通过应用管理配置。


