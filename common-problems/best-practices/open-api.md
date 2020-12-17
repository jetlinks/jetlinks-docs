# OpenApi使用

基于数据签名的OpenApi模块.用于对外提供接口.

::: tip 注意
本功能仅在专业版中提供.
:::

## 创建客户端

进入平台:[系统设置]-[OpenApi客户端]

点击新建按钮:

![新建客户端](images/create-openapi.png)

填写对应的内容保存.

::: tip 注意
 `clientId`和`secureKey`需要提供给客户端开发者.
 用户名和密码是系统统一的用户主体,会自动创建到用户管理中.使用此用户名密码也能登录到系统中.
 与其他用户相同,可以将用户绑定到机构实现数据权限控制.
:::

## 赋权

点击操作列中的赋权按钮对客户端进行赋权.大部分情况下只需要勾选: `设备操作API`和`设备数据API`权限即可.

::: tip 注意
此赋权操作实际上是对`OpenAPI客户端`对应对`用户主体`进行赋权.
:::

## 验证流程

![流程](images/OpenApi认证流程.png)

::: tip 说明

1. 图中`Signature`函数为客户端设置的签名方式,支持`MD5`和`Sha256`.
2. 发起请求的签名信息都需要放到请求头中,而不是请求体.
3. OpenApi对开发是透明的,开发只需要关心权限控制即可.OpenAPI和后台接口使用的是相同的权限控制API.
因此开发一个`OpenAPI接口`就是写一个`WebFlux Controller`. [查看使用方式](../dev-guide/crud.md#web)

:::

## 签名

平台使用签名来校验客户端请求的完整性以及合法性.

例:

ClientId为`testId`,
SecureKey为:`testSecure`.
客户端请求接口: `/api/v1/device/dev0001/log/_query`,参数为`pageSize=20&pageIndex=0`,签名方式为`md5`.

1. 将参数key按ascii排序得到: pageIndex=0&pageSize=20
2. 使用拼接时间戳以及密钥得到: pageIndex=0&pageSize=201574993804802testSecure
3. 使用`md5("pageIndex=0&pageSize=201574993804802testSecure")`得到`837fe7fa29e7a5e4852d447578269523`

最终发起http请求为:

```text
GET /api/device?pageIndex=0&pageSize=20
X-Client-Id: testId
X-Timestamp: 1574993804802
X-Sign: 837fe7fa29e7a5e4852d447578269523
```

响应结果:

```text
HTTP/1.1 200 OK
X-Timestamp: 1574994269075
X-Sign: c23faa3c46784ada64423a8bba433f25

{"status":200,result:[]}

```

## 验签

使用和签名相同的算法(不需要对响应结果排序):

```java

String secureKey = ...; //密钥
String responseBody = ...;//服务端响应结果
String timestampHeader = ...;//响应头: X-Timestamp
String signHeader = ...; //响应头: X-Sign

String sign = DigestUtils.md5Hex(responseBody+timestampHeader+secureKey);
if(sign.equalsIgnoreCase(signHeader)){
    //验签通过

}

```
