# 设备身份认证

## 应用场景
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>设备身份认证：是一种物联网设备的可信身份标识,在设备接入平台时,对用户名和密码进行认证</p>
</div>

## 指导介绍
   <p>1. <a href="/dev-guide/IoT_device_identity_authentication.html#新建产品和设备" >新建产品和设备</a></p>
   <p>2. <a href="/dev-guide/IoT_device_identity_authentication.html#设备认证" >设备认证</a></p>
   <p>3. <a href="/dev-guide/IoT_device_identity_authentication.html#一型一密和一机一密" >一型一密和一机一密</a></p>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>1.MQTT认证配置,需要在协议包中进行配置,本文以官方协议的MQTT协议为例</p>
  2.不存在如下配置的情况下，需要在官方协议的<code>org.jetlinks.protocol.official.JetLinksProtocolSupportProvider</code>类中第一行添加如下代码
</div>

 获取官方协议地址：[获取官方协议](https://github.com/jetlinks/jetlinks-official-protocol)

```java
 private static final DefaultConfigMetadata mqttConfig = new DefaultConfigMetadata(
            "MQTT认证配置"
            , "MQTT认证时需要的配置,mqtt用户名,密码算法:\n" +
                    "username=secureId|timestamp\n" +
                    "password=md5(secureId|timestamp|secureKey)\n" +
                    "\n" +
                    "timestamp为时间戳,与服务时间不能相差5分钟")
            .add("secureId", "secureId", "密钥ID", new StringType())
            .add("secureKey", "secureKey", "密钥KEY", new PasswordType());
```

## 新建产品和设备
 创建产品
 参考：[创建产品](/Device_access/Create_product3.1.html)

 创建好产品之后，点击产品，点击设备接入
 
 查看接入方式中的mqtt配置
 
图中的secureId和secureKey会和连接请求传入的Username、Password相匹配

![](./images/MQTT_authentication_configuration.png)

 创建设备
 参考：[创建设备](/Device_access/Create_Device3.2.html)

## 设备认证

1.需要在官方协议的`org.jetlinks.protocol.official.JetLinksProtocolSupportProvider`类中create()方法内任意位置处添加如下代码添加如下代码
```java
CompositeProtocolSupport support = new CompositeProtocolSupport();
support.addAuthenticator(DefaultTransport.MQTT, new JetLinksAuthenticator());
```
2.官方协议中的JetLinksAuthenticator类核心认证代码

代码中的secureId和secureKey的值是下图中MQTT认证配置中secureId和secureKey的值
![](./images/MQTT_authentication_configuration.png)

```java
if (request instanceof MqttAuthenticationRequest) {
            //获取mqtt连接
            MqttAuthenticationRequest mqtt = ((MqttAuthenticationRequest) request);
            //获取用户名，格式：secureId|timestamp
            String username = mqtt.getUsername();
            //获取密码：原密码被MD5加密，md5(secureId|timestamp|secureKey)
            String password = mqtt.getPassword();
            String requestSecureId;
            try {
                //判断用户名中是否包含'|',不包含抛出异常
                String[] arr = username.split("[|]");
                if (arr.length <= 1) {
                    return Mono.just(AuthenticationResponse.error(401, "用户名格式错误"));
                }
                //取不包含时间戳的用户名作为SecureId
                requestSecureId = arr[0];
                //解析传入时间戳
                long time = Long.parseLong(arr[1]);
                //和设备时间差大于5分钟则认为无效
                if (Math.abs(System.currentTimeMillis() - time) > TimeUnit.MINUTES.toMillis(5)) {
                    return Mono.just(AuthenticationResponse.error(401, "设备时间不同步"));
                }
                //获取设备配置的secureId和secureKey
                return deviceOperation.getConfigs("secureId", "secureKey")
                        .map(conf -> {
                            //此处的secureId为设备接入处，MQTT认证配置中的secureId值
                            String secureId =  conf.getValue("secureId").map(Value::asString).orElse(null);
                            //此处的secureKey为设备接入处，MQTT认证配置中的secureKey值
                            String secureKey = conf.getValue("secureKey").map(Value::asString).orElse(null);
                            //签名
                            String digest = DigestUtils.md5Hex(username + "|" + secureKey);
                            if (requestSecureId.equals(secureId) && digest.equals(password)) {
                                return AuthenticationResponse.success(deviceOperation.getDeviceId());
                            } else {
                                return AuthenticationResponse.error(401, "密钥错误");
                            }
                        });
            } catch (NumberFormatException e) {
                return Mono.just(AuthenticationResponse.error(401, "用户名格式错误"));
            }
        }
        return Mono.just(AuthenticationResponse.error(400, "不支持的授权类型:" + request));
    }
```
## 一型一密和一机一密
获取官方协议地址：[获取官方协议](https://github.com/jetlinks/jetlinks-official-protocol)

配置一机一密和一型一密前，需要在官方协议或者自定义协议中的
`org.jetlinks.protocol.official.JetLinksProtocolSupportProvider`类中配置MQTT认证，如图1所示

图1
![图1](./images/MQTT_authentication.png)
                


### 一型一密
每一台产品下的设备使用相同的认证密码

在平台使用一型一密
使用以下配置代替上述图1配置

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>一型一密关键参数：scope(DeviceConfigScope.product)</p>
</div>

```java
 private static final DefaultConfigMetadata mqttConfig = new DefaultConfigMetadata(
            "MQTT认证配置"
            , "MQTT认证时需要的配置,mqtt用户名,密码算法:\n" +
                    "username=secureId|timestamp\n" +
                    "password=md5(secureId|timestamp|secureKey)\n" +
                    "\n" +
                    "timestamp为时间戳,与服务时间不能相差5分钟")
            .add("secureId", "secureId", "密钥ID", new StringType())
            .add("secureKey", "secureKey", "密钥KEY", new PasswordType())
            .scope(DeviceConfigScope.product);
```

### 一机一密
每一台设备都有自己的认证密码

在平台使用一型一密
使用以下配置代替上述图1配置

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>一机一密关键参数：scope(DeviceConfigScope.device)</p>
</div>

```java
 private static final DefaultConfigMetadata mqttConfig = new DefaultConfigMetadata(
            "MQTT认证配置"
            , "MQTT认证时需要的配置,mqtt用户名,密码算法:\n" +
                    "username=secureId|timestamp\n" +
                    "password=md5(secureId|timestamp|secureKey)\n" +
                    "\n" +
                    "timestamp为时间戳,与服务时间不能相差5分钟")
            .add("secureId", "secureId", "密钥ID", new StringType())
            .add("secureKey", "secureKey", "密钥KEY", new PasswordType())
            .scope(DeviceConfigScope.device);
```


