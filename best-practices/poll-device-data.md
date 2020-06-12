# 平台从第三方或者设备主动拉取数据

场景：设备无法推送数据到平台,需要平台主动去调用第三方平台接口或者去调用设备的tcp服务等。

## 流程

1. 通过实现自定义协议的`DeviceStateChecker`来自定义处理设备状态获取逻辑,比如通过调用第三方平台获取设备信息.
2. 通过实现自定义协议的`DeviceMessageSenderInterceptor.afterSent`来拦截消息发送,替换掉默认处理方式.在这里使用`WebClient`或者`Vertx`请求第三方或者设备.
3. 请求后解析数据为对应的消息,调用`DecodedClientMessageHandler.handleMessage(device,message)`完成默认消息处理之后,返回消息.

## 例子一,通过http到第三方平台获取数据.

TODO


## 例子二,通过tcp短链接从设备拉取数据.

TODO