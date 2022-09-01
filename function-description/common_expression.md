# 常见名词说明

<table class='table'>
    <thead>
        <tr>
          <td>名词</td>
          <td>描述</td>
        </tr>
    </thead>
    <tbody>
      <tr>
        <td>产品</td>
        <td>对某一类型<span class="font-weight">设备</span>的分类，通常是已经存在的某一个设备型号。<br>可以在产品中统一配置一些信息，例如物模型、物模型映射、存储策略、设备接入方式以及设备接入的配置。</td>
      </tr>
      <tr>
        <td>设备</td>
        <td>归属于某个<span class="font-weight">产品</span>下的具体设备。继承了<span class="font-weight">产品</span>的所有配置信息，也可以单独配置物模型。设备可以直接连接物联网平台，也可以作为子设备通过网关连接物联网平台。</td>
      </tr>
      <tr>
        <td>网络组件</td>
        <td>根据物联网平台可用的网络资源，配置各种网络服务(MQTT,TCP等)。只负责接收，发送报文，不负责任何处理逻辑。<br>通过<span class="font-weight">设备接入网关</span>，为设备接入提供网络环境。</td>
      </tr>
      <tr>
        <td>协议</td>
        <td>用于自定义设备消息的解析规则。用于设备认证，以及将设备发送给平台报文解析为平台统一的报文，以及解析平台下发给设备的指令。</td>
      </tr>
      <tr>
        <td>设备接入网关</td>
        <td>负责平台侧统一的设备接入，使用<span class="font-weight">网络组件</span>处理对应的请求以及报文，使用配置的协议将设备报文解析为平台统一的设备消息(`DeviceMessage`)，然后推送到事件总线。</td>
      </tr>
      <tr>
        <td>事件总线</td>
        <td>基于topic和事件驱动，负责进程内的数据转发。（设备告警，规则引擎都是通过事件总线订阅设备消息）。</td>
      </tr>
    </tbody>
</table>
