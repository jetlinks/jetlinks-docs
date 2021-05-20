# 物模型说明

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

## 属性

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

## 功能

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

## 事件

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

## 数据类型

所有类型共有属性:

- id 唯一标识
- name 名称
- description 描述
- expands 自定义配置

### 数字类型

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

### boolean 布尔类型

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

### string 字符类型

      例:

        {
           "type":"string",
           "expands":{"maxLen":"255"}
        }

### enum 枚举类型

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

### date 时间类型

    属性:
      - format 格式,如: yyyy-MM-dd
      - tz 时区,如: Asia/Shanghai
    
    例:
    
        {
            "type":"date",
            "format":"yyyy-MM-dd",
            "tz": "Asia/Shanghai"
        }

### password 密码类型

    与string类型相同

### file 文件类型

    属性:
      - bodyType 类型: url,base64,binary
      
    例:
    
        {
           "type":"file",
           "bodyType":"url"
        }

### array 数组(集合)类型

    属性:
      - elementType 元素类型
      
    例:
        
        {
            "type":"array",
            "elementType":{ 
                "type":"string"
            }
        }

### object 对象(结构体)类型

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

### geoPoint Geo地理位置类型

支持以逗号分割的经纬度字符串以及map类型.
默认支持3种格式转换: 逗号分割字符:`145.1214,126.123` ,json格式:`{"lat":145.1214,"lon":126.123}`.

      例:

        {
          "type":"geoPoint"
        }
