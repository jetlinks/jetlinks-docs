# 网络组件

各种网络通信协议组件实现，通过可视化配置HTTP(S),TCP(TLS),CoAP(DTLS),UDP(DTLS)
等客户端及服务端,以及提供多维度多监控(统计,流量,负载,异常...).

## 证书管理

证书管理用于统一管理各个网络组件所需的TLS证书,支持证书格式:`JKS`,`P12`,`PEM`.

### 生成证书

> 使用平台脚本生成的加密文件进行连接测试。平台生成脚本在目录 `D:\code\jetlinks\jetlinks-pro\jetlinks-manager\network-manager\src\test\resources\create.sh`,windows环境，在create.sh目录下执行以下命令

```bash
./create.sh
```

会生成以下文件

├─ resource
│  ├─ client.csr        #用于生成客户端私钥和证书的文件
│  ├─ client.p12        #用于生成P12证书类型的客户端秘钥的文件
│  ├─ client.pem        #PEM类型客户端CA文件
│  ├─ ec_private.pem    #生成的ec算法私钥文件
│  ├─ ec_public.pem     #生成的ec算法公钥文件
│  ├─ keyStore.jks      #生成JKS证书类型的服务秘钥(server key)和证书文件
│  ├─ server.csr        #用于生成服务端私钥和证书的文件
│  ├─ server.p12        #用于生成P12证书类型的服务端秘钥的文件
│  ├─ server.pem        #PEM类型服务端私钥文件
│  ├─ trustStore.jks    #生成JKS证书类型的根秘钥(root key)和证书的文件
│  ├─ trustStore.p12    #用于生成P12证书类型秘钥的文件
│  └─ trustStore.pem    #信任库文件

### 使用PEM证书

> 平台上传秘钥库和信任库为同一文件,需要将`client.pem`和`ec_private.pem`合并成一个文件，即重新复制一份`ec_private.pem`文件后将`client.pem`文件全部内容粘贴在`ec_private.pem`文件内容后面，在平台的证书管理内上传合并后的文件。

> 特别注意：平台证书管理只允许上传证书文件，不允许复制内容粘贴在输入框内，粘贴的方式会报错。

![证书配置](images/network/ca-pem.png)

开启证书TLS认证

![网络组件](images/network/ca-pem-network.png)

客户端配置

![网络组件](images/network/ca-pem-client.png)

### 使用JKS证书

JKS秘钥库密码：endPass
JKS信任库密码：rootPass

> 上传秘钥库：keyStore.jks
> 上传信任库：trustStore.jks
> 以上两个密码可以在create.sh文件内修改
> 修改参数如下：

```bash
KEY_STORE=keyStore.jks
KEY_STORE_PWD=endPass
TRUST_STORE=trustStore.jks
TRUST_STORE_PWD=rootPass
```

客户端配置

CA证书使用client.pem

### 使用P12证书

PFX秘钥库密码：endPass
PFX信任库密码：rootPass


![网络组件](images/network/ca-p12.png)

CA证书使用client.pem

## HTTP管理

用于统一管理HTTP客户端以及服务.

### 配置客户端

### 配置服务端


## TCP管理

### 配置客户端

### 配置服务端


## CoAP管理

### 配置客户端

### 配置服务端

## MQTT管理

### 配置客户端

### 配置服务端


## WebSocket管理

### 配置客户端

### 配置服务端

## UDP管理

[下一步,设备管理](device-manager.md)