# JetLinks 消息协议 v1.0

- [设备模型定义](#设备模型(元数据)定义)
    - [属性](#属性)
    - [功能](#功能)
    - [事件](#事件)
    - [数据类型](#数据类型)
- [MQTT接入](#MQTT(S)接入)
    - [认证](#认证)
    - [Topic](#Topic)
- [CoAP接入](#CoAP接入)
- [CoAP DTLS接入](#DTLS接入)

## 设备模型(元数据)定义

设备模型分为: 属性(properties),功能(function),事件(event).

设备模型使用场景:
1. 前端通过模型定义动态展示设备运行状态或者设备操作界面
2. 服务端可通过统一的API获取设备模型并进行相关操作,如: 在发送设备消息时进行参数校验,
在收到设备消息进行类型转换处理.

数据结构:

    {
        "id":"设备ID",
        "name":"设备名称",
        "properties":[...属性],
        "functions":[...功能],
        "events":[...事件]
    }

### 属性
用于定义设备属性,运行状态等如: 设备SN,当前CPU使用率等.
平台可主动下发消息获取设备属性,设备也通过事件上报属性.

数据结构: 

    {
       "id": "cpu_usage", //属性标识
       "name": "CPU使用率",
       "valueType": {   //值类型
         "type": "double", //类型标识,见类型表
         "maxValue":100,
         "minValue":0,
         "unit":"percent",     //单位
         "expands":{"key1":"value1"} //其他自定义拓展定义
       },
       "expands":{"key1":"value1"} //其他自定义拓展定义
     }

### 功能
用于定义设备功能,平台可主动调用,例如: 播放语音,开关操作等.

数据结构:

    {
       "id": "playVoice", //功能标识
       "name": "播放声音", //名称
       "inputs": [  //输入参数
         {
           "id": "text",
           "name": "文字内容",
           "valueType": { //参数类型
             "type": "string"
           },
           "expands":{"key1":"value1"} //其他自定义拓展定义
         }
       ],
       "output": { //输出
         "type": "boolean" //输出类型
       },
       "expands":{"key1":"value1"} //其他自定义拓展定义
     }

### 事件
用于定义设备事件, 如: 定时上报设备属性, 设备报警等.

数据结构:

    {
       "id": "fire_alarm", //事件标识
       "name": "火警",
       "valueType": {
         "type": "object",  //对象(结构体)类型
         "properties": [    //对象属性(结构与属性定义相同)
           {
             "id": "location",
             "name": "地点",
             "valueType": {
               "type": "string"
             }
           },
           {
             "id": "lng",
             "name": "经度",
             "valueType": {
               "type": "double"
             },
             "expands":{"gis":"lng"} //其他自定义拓展定义
           },
           {
             "id": "lat",
             "name": "纬度",
             "valueType": {
               "type": "double"
             },
              "expands":{"gis":"lat"} //其他自定义拓展定义
           }
         ]
       },
       "expands":{"key1":"value1"} //其他自定义拓展定义
    }

### 数据类型

所有类型共有属性:
- id 唯一标识
- name 名称
- description 描述
- expands 自定义配置

类型定义

1. int 整型
2. long 长整型
3. float 单精度浮点型
4. double 双精度浮点型

    以上均为数字类型,共有属性:
    - max 最大值
    - min 最小值
    - unit 单位
     
     例:
     
        {
        "type":"double",
        "max":100,
        "min":0,
        "unit":"percent",
        "expands":{"readonly":true}
        }
    
5. boolean 布尔类型
    
     属性
      - trueText 为true时的文本,默认为`是`
      - falseText 为false时的文本,默认为`否`
      - trueValue  为true时的值,默认为`true`
      - falseValue 为false时的值,默认为`false`
      
      例:
      
        {
            "type":"boolean",
            "trueText":"开启",
            "falseText":"关闭",
            "trueValue":"1",
            "falseValue":"0"
        }
      
6. string 字符类型

      例:
      
        {
           "type":"string",
           "expands":{"maxLen","255"}
        }
        
7. enum   枚举类型

    属性:
     - elements (Element)枚举中的元素
        
    Element:
     - value 枚举值
     - text 枚举文本
     - description 说明
         
    例:
    
        {
            "type":"enum",
            "elements":[
                {"value":"1","text":"正常"},
                {"value":"-1","text":"警告"},
                {"value":"0","text":"未知"}
            ]
        }
        
8. date   时间类型
    
    属性:
      - format 格式,如: yyyy-MM-dd
      - tz 时区,如: Asia/Shanghai
    
    例:
    
        {
            "type":"date",
            "format":"yyyy-MM-dd",
            "tz": "Asia/Shanghai"
        }

9. password 密码类型
    
    与string类型相同
    
10. file  文件类型
    
    属性:
      - bodyType 类型: url,base64,binary
      
    例:
    
        {
           "type":"file",
           "bodyType":"url"
        }
      
11. array 数组(集合)类型

    属性:
      - elementType 元素类型
      
    例:
        
        {
            "type":"array",
            "elementType":{ 
                "type":"string"
            }
        }
        
12. object  对象(结构体)类型

     属性:
        - properties 属性列表
        
     例:
        
        {
            "type":"object",
            "properties":[
                {
                 "id": "location",
                 "name": "地点",
                 "valueType": {
                   "type": "string"
                 }
               },
               {
                 "id": "lng",
                 "name": "经度",
                 "valueType": {
                   "type": "double"
                 },
                 "expands":{"gis":"lng"}
               },
               {
                 "id": "lat",
                 "name": "纬度",
                 "valueType": {
                   "type": "double"
                 },
                  "expands":{"gis":"lat"}
               }
            ]
        }
 
## MQTT(S)接入
目前支持MQTT3.1.1和3.1版本协议.

### 认证

CONNECT报文:
```
clientId: 设备实例ID
username: secureId+"|"+timestamp
password: md5(secureId+"|"+timestamp+"|"+secureKey)
```

说明: `secureId`以及`secureKey`在创建设备产品和设备实例时进行配置.
`timestamp`为当前系统时间戳(毫秒),与系统时间不能相差5分钟.


### Topic

 - 读取设备属性: 
    
    topic: `/{productId}/{deviceId}/properties/read`
    
    方向: `下行`
    
    消息格式: 
    
        {
        "messageId":"消息ID",
        "deviceId":"设备ID",
        "properties":["sn","model"] //要读取到属性列表
        }
             
    回复Topic: `/{productId}/{deviceId}/properties/read/reply`
    
    回复消息格式:
        
        //成功
        {
          "messageId":"与下行消息中的messageId相同",
          "properties":{"sn":"test","model":"test"}, //key与设备模型中定义的属性id一致
          "deviceId":"设备ID",
          "success":true,
        }
        //失败. 下同
        {
          "messageId":"与下行消息中的messageId相同",
          "success":false,
          "code":"error_code",
          "message":"失败原因"
        }
 - 修改设备属性:
    
     topic: `/{productId}/{deviceId}/properties/write`
     
     消息格式: 
        
        {
         "messageId":"消息ID",
         "deviceId":"设备ID",
         "properties":{"color":"red"} //要设置的属性
        }
     回复Topic: `/{productId}/{deviceId}/properties/wirte/reply`
     
     回复消息格式:
     
        {
          "messageId":"与下行消息中的messageId相同",
          "properties":{"color":"red"}, //设置成功后的属性,可不返回
          "success":true,
        }
        
 - 调用设备功能
 
      topic: `/{productId}/{deviceId}/function/invoke`
      
      消息格式: 
         
         {
          "messageId":"消息ID",
          "deviceId":"设备ID",
          "function":"playVoice",//功能ID
          "inputs":[{"name":"text","value":"播放声音"}] //参数
         }
         
      回复Topic: `/{productId}/{deviceId}/function/invoke/reply`
      
      回复消息格式:
         
         {
           "messageId":"与下行消息中的messageId相同",
           "output":"success", //返回执行结果
           "success":true,
         }
         
 - 设备事件上报
     
      topic: `/{productId}/{deviceId}/event/{eventId}`
      
      消息格式: 
        
        {
        "messageId":"随机消息ID",
        "data":100 //上报数据
        }
       
      拓展:
      
      定时上报属性:
                
        {
        "messageId":"随机消息ID",
        "data":{"color":"red"},//属性列表
        "headers":{"report-properties":true} //标记为上报属性事件
        } 

## CoAP接入
使用CoAP协议接入仅需要对数据进行加密即可.加密算法: AES/ECB/PKCS5Padding.

使用自定义Option: `2100:设备ID` 来标识设备.

将请求体进行加密,密钥为在创建设备产品和设备实例时进行配置的(`secureKey`).

请求地址(`URI`)与MQTT `Topic`相同.消息体(`payload`)与MQTT相同.


## DTLS接入
使用CoAP DTLS 协议接入时需要先进行认证:

发送认证请求:
```text
POST /auth
Accept: application/json
Content-Format: application/json
2100: 设备ID
2110: 签名 md5(payload+secureKey)
payload: {"timestamp":"时间戳"}
```

响应结果:
```text
2.05 (Content)
payload: {"token":"令牌"}
```

之后的请求中需要将返回的令牌携带到自定义Option:2111

例如:
```text
POST /test/device1/event/fire_alarm
2100: 设备ID
2111: 令牌
...其他Option
payload: json数据
```