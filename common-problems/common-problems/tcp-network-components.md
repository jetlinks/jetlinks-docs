# TCP网络组件常见问题

使用TCP网络组件时的常见问题

## 粘拆包处理

由于TCP存在粘拆包问题,在创建`TCP服务`或者`TCP客户端`时需要指定数据包的`解析方式`.
TCP组件会根据解析方式,将收到的TCP数据包解析为完整的数据包,再交给对应的消息协议处理.

目前支持的解析方式:

1. 不处理:收到数据包时直接交给消息协议进行处理.
2. 分隔符:使用分隔符来处理粘拆包,例如:`\n`.
3. 固定长度:使用固定长度来处理粘拆包.
4. 脚本: 通过脚本来自定义处理规则,脚本内置了一个`parser`进行处理逻辑定义,例如:

```js
var BytesUtils = org.jetlinks.pro.network.utils.BytesUtils;
parser.fixed(4)//1. 首先读取4个字节
       .handler(function(buffer){
            var len = BytesUtils.highBytesToInt(buffer.getBytes());//2. 高位在前将4字节转为int,代表接下来数据包的完整长度.
            parser.fixed(len);//3. 设置下一步要读取固定长度
        })
       .handler(function(buffer){
            parser.result(buffer.toString("UTF-8"))//4. 设置数据包结果.
                   .complete(); //5. 完成本次解析,重新回到1开始读取下一个数据包.
        });
```