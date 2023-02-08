# UDP接入

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        适用于UDP设备接入平台
    </p>
</div>



## 指导介绍

<p>
1. <a href='/dev-guide/device-access-udp.html#新增接入协议'>新增接入协议</a><br>
2. <a href='/dev-guide/device-access-udp.html#新增网络组件'>新增网络组件</a><br>
3. <a href='/dev-guide/device-access-udp.html#新增设备接入网关'>新增设备接入网关</a><br>
4. <a href='/dev-guide/device-access-udp.html#新增产品'>新增产品</a><br>
5. <a href='/dev-guide/device-access-udp.html#新增设备'>新增设备</a><br>
6. <a href='/dev-guide/device-access-udp.html#配置产品物模型'>配置产品物模型</a><br>
7. <a href='/dev-guide/device-access-udp.html#设备接入'>设备接入</a><br>
</p>



## 新增接入协议

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        设备与平台进行通信需要有协议包的支持，平台支持基于UDP协议通过自定义协议包的方式，以解析不同厂家、不同设备上报的数据。
    </p>
</div>

<p>
 1、 <code>登录</code>JetLinks平台，选择顶部导航栏<code>物联网</code>>>>左侧菜单栏<code>运维管理</code>>>><code>协议管理</code>
</p>


![协议管理页面](./images/device-access-http/conf-http-protocol.png)

<p>2、<code>clone</code>或者<code>下载</code>协议包，此示例使用的为<a href='https://github.com/jetlinks/jetlinks-official-protocol' target='_blank'>JetLinks官方设备接入协议</a>V3分支。将协议包下载或者<code>clone</code>到本地磁盘，如果<code>clone</code>过程中出现错误可以参考<a href='./pull-code.html#配置ssh-key' target='_blank'>如何配置ssh-key</a>。</p>

![如何下载协议包](./images/device-access-http/how-to-download-protocal.png)

完成后的协议包目录如下，如果选择直接下载压缩包，则解压后目录下没有`.git`文件夹

![clone或者下载完成解压后的protocol](./images/device-access-http/how-to-download-protocal-01.png)

<p>3、在JetLinks平台<code>协议管理</code>内选择<code>新增</code></p>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        <code>Jar</code>和<code>Local</code>方式新增协议的区别主要在于调试协议包时，<code>jar包</code>在每次更新协议包代码后需要使用<code>Maven的package命令</code>并重新上传生成的<code>Jar包</code>，<code>Local</code>只需要使用<code>Maven的compile命令</code>重新生成<code>Classes</code>文件夹即可，省去了重新选择上传<code>Jar包</code>这一步。
    </p>
</div>


通过<code>Jar包</code>的方式新增

![添加新协议](./images/device-access-http/add-new-protocol.png)

协议包为一个基于`Maven`管理的Java项目，如果安装有`IDEA`则直接打开项目

![Jar包路径](./images/device-access-http/idea-open-protocol.png)

点击右侧的`Maven`->`package`等待打包完成，目录下会生成一个`target`文件夹，复制`target`内的`jetlinks-official-protocol-3.0-SNAPSHOT.jar`文件绝对路径

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
   <p>
       如果在打包过程中出现错误，可以删除<code>src</code>下的<code>test</code>目录，然后继续进行打包操作即可。
    </p>
</div>

![idea打包成功](./images/device-access-http/idea-package-success.png)

也可以直接安装<a href='https://maven.apache.org/download.cgi' target='_blank'>Maven</a>进行编译，其中配置`Maven`环境变量可以参考<a href='https://www.runoob.com/maven/maven-setup.html' target='_blank'>Maven环境变量配置</a>。配置完成`Maven`环境后可以在下载的协议包目录内通过`Maven`命令方式进行打包生成`target`目录

![Mvn命令打包](./images/device-access-http/maven-package.png)

![mvn命令打包成功](./images/device-access-http/mvn-package-success.png)

之后在`上传Jar包`选择打包完成生成的`Jar`包

![上传Jar包](./images/device-access-http/upload-jar.png)

新增完成后平台显示新增的协议如下

![Jar包方式新增协议完成](./images/device-access-http/add-protocol-jar.png)

<br>

通过`Local`方式新增协议

![local方式新增协议](./images/device-access-http/add-protocal-local.png)

协议包为一个基于`Maven`管理的Java项目，如果安装有`IDEA`则直接打开项目

![IDEA打开协议包项目](./images/device-access-http/idea-open-protocol.png)

点击右侧的`Maven`->`compile`等待编译完成，目录下会生成一个`target`文件夹

![IDEA编译协议包](./images/device-access-http/compile-protocol.png)

复制生成的`target`文件夹内的`classes`目录的绝对路径，填入平台新增协议的`文件地址`内，点击确认

![idea复制classes文件夹路径](./images/device-access-http/idea-copy-classes-path.png)

![完成新增local协议配置](./images/device-access-http/add-protocol-local-complete.png)

也可以直接安装<a href='https://maven.apache.org/download.cgi' target='_blank'>Maven</a>进行编译，其中配置`Maven`环境变量可以参考<a href='https://www.runoob.com/maven/maven-setup.html' target='_blank'>Maven环境变量配置</a>。配置完成`Maven`环境后可以在下载的协议包目录内通过`Maven`命令方式进行编译生成`target`目录

![通过Maven方式编译](./images/device-access-http/add-protocal-maven.png)

![maven编译成功后](./images/device-access-http/maven-compile.png)

同样的复制生成的`target`文件夹下的`classes`文件夹路径填入平台新增协议的`文件地址`内，点击确认

![复制路径](./images/device-access-http/copy-classes-path.png)

![完成Local导入协议配置](./images/device-access-http/add-protocol-local-complete.png)



## 新增网络组件

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        网络组件是用于管理网络服务动态配置、启停，只负责接收、发送报文，不负责任何处理逻辑。
    </p>
</div>

<p>
    1、 <code>登录</code>JetLinks平台，选择顶部导航栏<code>物联网</code>>>>左侧菜单栏<code>运维管理</code>>>><code>网络组件</code>
</p>

<p>2、填入配置参数，点击<code>保存</code>按钮</p>

| 填写参数 | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| 名称     | 必填项，该网络组件的名称                                     |
| 类型     | 必选项，此处选择UDP                                          |
| 集群     | 必选项，共享配置：集群下所有节点共用同一配置<br>独立配置：集群下不同节点使用不同配置 |
| 本地地址 | 绑定到服务器上的网卡地址，此处固定为0.0.0.0，表示接收所有请求 |
| 本地端口 | 必填项，监听指定端口的请求                                   |
| 公网地址 | 必填项，对外提供访问的地址，内网环境时填写服务器的内网IPv4地址。仅用于展示，给运维人员做公网端口和本地地址绑定关系展示使用。 |
| 公网端口 | 必填项，对外提供访问的端口                                   |
| 开启DTLS | 必选项，是否开启DTLS加密，如果选择开启则需要配置证书，配置详情可以参考<a href='/Mocha_ITOM/certificate_management.html#使用平台脚本生成证书' target='_blank'>证书配置</a> |
| 说明     | 选填项，该网络组件的备注说明                                 |

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        本地端口内可选择的端口号范围可以在代码<code>jetlinks-standalone</code>模块内的<code>application.yml</code>配置文件中通过修改<code>network.resources</code>的值进行指定，文件完整路径为<code>jetlinks-standalone/resources/application.yml</code>
    </p>
</div>


![添加UDP网络组件](./images/device-access-http/udp-network-config.png)



## 新增设备接入网关

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        负责平台侧统一的设备接入，使用网络组件处理对应的请求以及报文，使用配置的协议解析为平台统一的设备消息(DeviceMessage)，然后推送到事件总线。
    </p>
</div>

<p>
    1、 <code>登录</code>JetLinks平台，选择上方<code>物联网</code>>>>左侧<code>运维管理</code>>>><code>设备接入网关</code>
</p>

<p>2、点击<code>新增</code>>>><code>UDP接入</code>，选择配置好的<code>UDP网络组件</code>，<code>JetLinks官方协议</code>，命名后点击<code>保存</code></p>

![网络组件添加配置](./images/device-access-http/udp-config-gateway01.png)

![UDP网关配置](./images/device-access-http/gateway-add-config02.png)

![UDP网关配置](./images/device-access-http/udp-config-gateway02.png)



## 新增产品

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        对某一型设备的分类，通常是已经存在的某一个设备型号。
    </p>
</div>

<p>
    1、 <code>登录</code>JetLinks平台，选择顶部导航栏<code>物联网</code>>>>左侧菜单栏<code>设备管理</code>>>><code>产品</code>
</p>
<p>2、点击<code>新增</code>，填写产品<code>名称</code>，选择<code>设备类型</code>，之后点击<code>确定</code></p>

![新增UDP产品](./images/device-access-http/udp-add-product.png)

<p>3、点击创建好的产品进入产品详情页，选择<code>设备接入</code>，点击<code>选择</code>并在弹出的设备接入配置中选择配置好的<code>UDP设备接入网关</code>，点击确定</p>

![UDP产品配置网关](./images/device-access-http/udp-product-select-gateway.png)

<p>4、继续在此页面的底端找到<code>UDP认证配置</code>，填入<code>secureKey</code>的值，然后在页面的顶端<code>启用</code>产品</p>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        <code>secureKey</code>是设备连接到平台时的身份验证，可以在协议包中进行配置一型一密还是一机一密。
    </p>
</div>


![UDP产品信息配置完成](./images/device-access-http/start-udp-product.png)



## 新增设备

<p>
    1、 <code>登录</code>JetLinks平台，选择顶部导航栏<code>物联网</code>>>>左侧菜单栏<code>设备管理</code>>>><code>设备</code>
</p>

<p>2、点击<code>新增</code>，填写设备<code>名称</code>，选择<code>所属产品</code>，此处选择之后点击<code>确定</code>，并在此页面<code>启用</code>刚才新增的设备</p>

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        此处设置ID是方便后续设备上线、属性上报等操作的报文较为一致。
    </p>
</div>

![新增UDP设备](./images/device-access-http/udp-add-device.png)

![启用UDP设备](./images/device-access-http/udp-start-device.png)



## 配置产品物模型

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        <a href='/Device_access/Configuration_model3.4.html' target='_blank'>物模型</a>是对某一类产品自身功能的一种抽象，在平台可表述为属性、功能、事件和标签。当在产品中配置物模型后，该类型下的设备都会继承该产品的物模型。但如果在设备中单独进行配置，即使未对继承的物模型进行更改，该设备的物模型也会脱离产品，需要独自进行维护。
    </p>
</div>

<p>
    1、 进入<code>物联网</code>>>><code>设备管理</code>>>><code>产品</code>页面，点击上文创建的产品，进入该产品的产品详情页，选择<code>物模型</code>
</p>


![UDP产品配置物模型](./images/device-access-http/udp-product-add-things.png)

<p>2、在<code>物模型</code>>>><code>属性定义</code>内点击右侧的<code>新增</code>来新增<code>属性定义</code></p>

| 填写参数 | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| 标识     | 必填项，该属性的标识                                         |
| 名称     | 必填项，该属性的名称                                         |
| 数据类型 | 必选项，该字段的数据类型                                     |
| 最大长度 | 选填项，最大字节数                                           |
| 来源     | 必选项，选择为设备时表示该属性来源与设备<br>选择为手动时表示属性来源于手动设置<br>选择为规则时表示该属性来源于某种计算规则 |
| 读写类型 | 必选项，读表示平台可以下发读取属性的指令，要求设备回复当前的属性值<br>写表示平台可以修改该属性的值，当数据来源为设备时，填写的值将下发到设备<br>上报表示设备可以上报该属性到平台 |
| 其他配置 | 可选项，可以选择是否存储，默认为存储                         |
| 指标配置 | 选填项，场景联动页面可引用指标配置作为触发条件               |
| 说明     | 选填项，该属性的说明                                         |

![配置UDP产品物模型属性](./images/device-access-http/udp-product-add-things01.png)

<p>3、选择<code>功能定义</code>>>>右侧<code>新增</code>，注意此处配置的为<code>输出参数</code>，参数配置完成后右上角选择<code>保存</code></p>

| 填写参数 | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| 标识     | 必填项，该功能的标识                                         |
| 名称     | 必填项，该功能的名称                                         |
| 是否异步 | 必选项，如果为异步，则不需要等待设备回复消息；如果为同步，则需要等待设备回复消息 |
| 输入参数 | 选填项，调用设备功能时需要手动配置参数下发到设备             |
| 输出参数 | 选填项，设备回复功能调用时需要上报的参数信息                 |
| 说明     | 选填项，该功能的说明                                         |

![配置UDP产品功能](./images/device-access-http/udp-function-param01.png)

![配置UDP产品功能](./images/device-access-http/udp-function-param02.png)

![配置UDP产品功能](./images/device-access-http/udp-function-param03.png)

<p>4、选择<code>事件定义</code>>>>右侧<code>新增</code>，填入参数后右上角选择<code>保存</code></p>

| 填写参数 | 说明                                               |
| -------- | -------------------------------------------------- |
| 标识     | 必填项，该事件的标识                               |
| 名称     | 必填项，该事件的名称                               |
| 级别     | 必选项，危险程度                                   |
| 输出参数 | 默认为结构体，需要在下方的JSON对象中配置具体的参数 |
| 说明     | 选填项，该事件的说明                               |

![UDP产品添加事件](./images/device-access-http/udp-event-param01.png)

![UDP产品添加事件](./images/device-access-http/udp-event-param02.png)

![UDP产品添加事件](./images/device-access-http/udp-event-param03.png)

<p>4、配置完成后可以选择<code>设备</code>并找到对应产品下的设备，点击查看设备详情，选择<code>运行状态</code>，可以直观的看到配置完成的属性和事件；选择设备功能可以看到已经配置好的产品功能</p>

![设备运行状态页面](./images/device-access-http/udp-property.png)

![设备功能页面](./images/device-access-http/udp-function.png)



## 设备接入

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        本示例将使用<code>SocketTool</code>模拟设备进行连接平台以及属性、事件上报和功能调用回复。
    </p>
</div>



### 设备上线

<p>
    1、 下载<a href='https://pan.baidu.com/s/1Cpp4yCPaDDEcdybWZKIsDw&pwd=q14o' target='_blank'>SocketTool</a>，下载完成后打开。</b>
</p>

![socket-tool](./images/device-access-http/socket-tool.png)

<p>2、配置连接参数连接平台</p>

![配置连接参数](./images/device-access-http/socket-tool-config.png)

![连接成功](./images/device-access-http/socket-connect-success.png)

<p>3、填写报文<code>00000561646d696e01000001862606b03e0077000e656c655f6465765f615f315f3133000561646d696e</code>（<a herf='#'>报文是如何计算的?</a>），之后点击发送数据，进行设备上线。</p>

![socket工具发送数据](./images/device-access-http/socket-send-message.png)

![收到平台消息](./images/device-access-http/socket-receive-message.png)

4、设备成功上线

![udp设备上线](./images/device-access-http/udp-device-online.png)

### 设备上报属性

<p>填写报文<code>00000561646D696E03000001862666B12B0088000E656C655F6465765F615F315F31330001000B636F6E73756D7074696F6E0A40888A8F5C28F5C3</code>（<a herf='#'>报文是如何计算的?</a>），之后点击发送数据，可以在设备运行状态页面查看上报的属性。</p>

![udp设备上报属性](./images/device-access-http/udp-report-property.png)

![查看属性上报](./images/device-access-http/confirm-udp-report.png)

### 设备事件上报

<p>填写报文（注意去除每一段之间的换行符）<code>00000561646D696E0a000001862666B12B0088000E656C655F6465765F615F315F3133000d73746174655f7761726e696e6700<br>0300086c6f636174696f6e0B000c6a696e6b655f615f315f3133000974696d657374616d700500000186281cb583000a7374617<br>4655f636f64650B000e696c6c656167616c5f7374617465</code>（<a herf='#'>报文是如何计算的?</a>），之后点击发送数据，可以在设备运行状态页面查看上报的事件。</p>

![设备事件上报](./images/device-access-http/udp-device-event.png)

![事件上报成功](./images/device-access-http/udp-event-success.png)

### 设备功能调用回复

// TODO



## 报文是如何计算的

// TODO