# OpenApi使用

基于数据签名的OpenApi模块.用于对外提供接口.

::: tip 注意
本功能仅在企业版中提供.
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

例1（GET请求）:

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

例2（POST请求）:

ClientId为`MmXnSF4Wba7eMf6n`,
SecureKey为:`eajQWkGa4DHRxwJCQRtkfCpe`.
客户端请求接口: `/api/v1/device/_query`,参数为`{"paging":false}`,签名方式为`md5`.

1. 将body参数整体直接传入
2. 使用拼接时间戳以及密钥得到: {"paging":false}1626666148780eajQWkGa4DHRxwJCQRtkfCpe
3. 使用md5("{"paging":false}1626666148780eajQWkGa4DHRxwJCQRtkfCpe")得到签名串af686d000a31978c1e6c7a9d59c0012a

最终发起http请求为:
```text
POST /api/v1/device/_query
X-Client-Id: MmXnSF4Wba7eMf6n
X-Timestamp: 1626666148780
X-Sign: af686d000a31978c1e6c7a9d59c0012a

{"paging":false}
```

响应结果:
```text
HTTP/1.1 200 OK
X-Timestamp: 1626666148780
X-Sign: af686d000a31978c1e6c7a9d59c0012a

{
	"result": {
		"total": 7,
		"data": [{
			"productId": "gb28181-pro",
			"registerTime": 1625820354503,
			"createTime": 1625820354515,
			"name": "34020000001110000001",
			"id": "34020000001110000001",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"productName": "国标产品",
			"parentId": "jetlinks-agent"
		}, {
			"productId": "gateway1",
			"registerTime": 1625448767613,
			"createTime": 1625447824635,
			"name": "网关设备",
			"id": "gateway1",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"productName": "网关设备"
		}, {
			"productId": "edge-gateway",
			"registerTime": 1625565370567,
			"createTime": 1625564667544,
			"name": "测试",
			"id": "jetlinks-agent",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"productName": "测试"
		}, {
			"productId": "mqtt_pro",
			"registerTime": 1626161777107,
			"createTime": 1626161652399,
			"name": "test",
			"id": "233",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"productName": "mqtt产品"
		}, {
			"productId": "udp-pro",
			"registerTime": 1626322047249,
			"createTime": 1626322044526,
			"name": "测试upd设备",
			"id": "udp-1",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"productName": "udp产品"
		}, {
			"productId": "coap-pro",
			"registerTime": 1626225975637,
			"createTime": 1626225972273,
			"name": "coap设备001",
			"id": "coap-test-001",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"productName": "coap产品"
		}, {
			"productId": "sx-smart-pro",
			"registerTime": 1626258298747,
			"createTime": 1626244562681,
			"name": "烟雾报警器(智慧社区)",
			"id": "7ecc817877ad434a97f6110a95e2ae97",
			"state": {
				"text": "离线",
				"value": "offline"
			},
			"productName": "陕西省建筑设计研究院-智慧产品-烟雾报警器(智慧社区)"
		}],
		"pageIndex": 0,
		"pageSize": 25
	},
	"message": "success",
	"status": 200,
	"timestamp": 1626666148826
}
```

::: tip
参考示例[协议代码](https://github.com/jetlinks/jetlinks-openapi-demo) 
:::
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
