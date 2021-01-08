# DemoDevicies手册

## 新建

### 新建网络组件

1.进入系统,点击`设备接入` → `网络组件` → `新增组件`

![新建网络组件](images/device_demo/Network_components.png)

2.创建完后启动组件（蓝色为启动成功）

![启动网络组件](images/device_demo/start_network.png)

### 新建设备网关

1.点击 `设备接入 `→ `设备网关` → `新建`
![新建网关](images/device_demo/device_gateway.png)

2.创建成功后，找到刚创建的网关，开启网关

![开启网关](images/device_demo/start_gateway.png)

### 创建协议
1.点击 `设备接入` → `协议管理` → `新建`

输入类名org.jetlinks.pisenser.protocol.PiSenserProtocolSupportProvider
![创建协议](images/device_demo/create_protocol.png)

2.在操作栏中点击点击`发布`
![发布协议](images/device_demo/protocol_release.png)
### 创建产品
1.点击 `设备管理` →`产品`→ `新建`

2.点击新建过后会跳转到创建产品界面，如下图：

![创建产品](images/device_demo/create_product.png)

3.点击保存过后会跳转到产品基本信息界面，点击 `发布` → `应用配置` ，如图下：

![发布产品](images/device_demo/product_release.png)

4.导入物模型

·下载快速导入物模型的文件,再点击`应用配置`
![导入物模型](images/device_demo/download.png)
### 创建设备:

1.点击 `设备管理` → `设备` → `添加设备`
![导入物模型](images/device_demo/create_equipment.png)

2.点击保存过后会跳转到设备的基本信息，点击激活设备，如下图：
![导入物模型](images/device_demo/activate_equipment.png)

## 模拟设备接入
1.下载并安装MQTT.fx软件。请访问[MQTT官网](https://mqttfx.jensd.de/index.php/download?spm=a2c4g.11186623.2.16.20ab5800HxuVJR)下载。

2.打开MQTT.fx软件，单击设置图标。
![下载MQTT](images/device_demo/mqttfx-index.png)

3.填写相应的信息
![配置MQTT参数](images/device_demo/mqtt_config.png)

4.点击 `ok` 保存后，测试连接设备，如下图：
![MQTT开始按钮](images/device_demo/start_config.png)

5.返回系统界面,查看设备是否上线，如下图：
![MQTT开始按钮](images/device_demo/equipment_online.png)

6.断开设备连接，如下图：
![MQTT断开连接](images/device_demo/stop_config.png)
![设备断开连接](images/device_demo/off_line.png)
::: tip 温馨提示：
如果回到系统，设备还是显示的在线，请刷新一下页面
:::

## 真实设备接入
设备通过网线接入路由器后，会通过DHCP协议自动获取IP地址，在路由器界面查询到设备IP地址。

:::warning 注意:
此处的IP地址根据你的实际地址确认
:::
![查看ip地址](images/device_demo/equipment_ip.png)
1.下载并安装Xshell软件。请访问[Xshell官网](https://www.xshellcn.com/xiazai.html)下载。

2.打开软件，新建一个会话，点击连接。（主机：192.168.3.139）

:::warning 注意:
此处的IP地址根据你的实际地址确认
:::
![连接会话](images/device_demo/Connect_session.png)

3.登录名和密码（name:pi    password:  qwer1234 ）
![用户名](images/device_demo/login_name.png)
![密码](images/device_demo/login_password.png)

3.连接设备成功后，执行命令 : $vim Senser.py
![查看文件](images/device_demo/create_file.png)

4.执行完成后，在最下面修改配置文件（按i键切换为编辑模式）

![修改配置文件](images/device_demo/update_file.png)

5.编辑完过后，按ESC键退出编辑，执行命令 ：$:wq保存文件并退出该文件

![退出](images/device_demo/exit_file.png)

6.执行$sudo reboot,重启程序,如下图

![重启程序](images/device_demo/reboot_procedure.png)

7.进入系统查看设备是否上线

![设备上线](images/device_demo/online_equipment.png)