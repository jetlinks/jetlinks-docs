
# 设备接入方式

<div class='divider'></div>

### 自定义协议接入

平台支持基于MQTT、UDP、TCP透传等协议通过自定义协议包的方式，解析不同厂家、不同设备上报的数据。协议包开发规范详见<a>协议包开发</a>。

#### 自定义接入协议类型
<table class='table'>
        <thead>
            <tr>
              <td>协议类型</td>
              <td>说明</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>MQTT直连</td>
            <td>适用于轻量级的异步通信。设备通过MQTT协议接入服务网关，通过Topic发布和订阅消息。</td>
          </tr>
          <tr>
            <td>WebSocket</td>
            <td>适用于设备端和服务端进行双向数据传输的场景。设备通过Websocket接入。</td>
          </tr>
          <tr>
            <td> CoAP</td>
            <td>适用于资源受限的低功耗设备。设备通过Topic订阅和发布消息。</td>
          </tr>
          <tr>
            <td>TCP透传</td>
            <td>适用于可靠性高的场景。设备使用TCP协议连接服务并传输消息。</td>
          </tr>
          <tr>
            <td>HTTP推送</td>
            <td>适用于设备使用HTTP协议上报消息的场景。</td>
          </tr>
          <tr>
            <td>UDP</td>
            <td>适用于速度要求高的场景。设备使用UDP协议连接服务并传输消息。</td>
          </tr>
           <tr>
            <td>MQTT Broker</td>
            <td>适用于设备不直接接入平台，而是通过第三方MQTT服务接入的场景。</td>
          </tr>
          </tbody>
</table>

### 云平台接入
在特定场景下，设备无法直接接入阿里云物联网平台时，您可使用云平台对接，先将设备接入至OneNet、CTWing平台，再以API的方式实现设备数据的云接入。

### 视频类设备接入
平台针对视频类设备，提供GB/T28181、固定地址类接入方式。固定地址类接入支持RTSP、RTMP。

### 通道类接入
平台针对工业领域类设备，提供Modbus（TCP）、OPC UA协议的接入。

### 操作步骤
接入方式详细操作步骤，请参见[最佳实践](../Best_practices/Device_access.md)。

</div>
