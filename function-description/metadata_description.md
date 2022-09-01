# 物模型说明

设备模型分为: 属性(properties)，功能(functions)，事件(events)，标签（tags）。

设备模型使用场景：

1. 前端通过模型定义动态展示设备运行状态或者设备操作界面
2. 服务端可通过统一的API获取设备模型并进行相关操作，如：在发送设备消息时进行参数校验，
   在收到设备消息进行类型转换处理。

数据结构:
```json5
    {
        "properties":[...属性],
        "functions":[...功能],
        "events":[...事件],
        "tags":[...标签]
    }
```

## 属性

用于定义设备属性，描述设备运行时的具体信息和状态。例如: 设备SN，当前CPU使用率等。
平台可主动下发指令获取设备属性，设备也可以通过消息上报属性。
#### 值类型
可以使用平台中定义的数据类型来配置属性，提供格式化的显示，例如精度、单位等。  

其他模块在使用物模型的属性时，可根据数据类型进行判断。例如场景联动中，选择内置参数时，根据属性的类型判断是否可以作为参数。

#### 拓展定义
在拓展定义中，可以配置属性的额外特性，例如读写类型、指标、存储、规则属性等。
<table class='table'>
    <thead>
        <tr>
          <td>拓展定义</td>
          <td>名称</td>
          <td>描述</td>
        </tr>
    </thead>
    <tbody>
      <tr>
        <td>source</td>
        <td>来源</td>
        <td>写入属性值的方式。<br>设备（device）：由设备上报消息时写入<br>手动（manual）：手动写入<br>规则（rule）：由脚本编写规则写入</td>
      </tr>
      <tr>
        <td>virtualRule</td>
        <td>规则配置</td>
        <td>编写JavaScript脚本，返回需要的属性值。<br>可以为规则配置窗口函数，实现聚合计算多次写入属性值。例如求和、求平均值。</td>
      </tr>
      <tr>
        <td>type</td>
        <td>读写类型</td>
        <td>设置属性支持的读写类型。例如场景联动时，设备动作选择时，根据属性支持的读写类型展示对应属性</td>
      </tr>
      <tr>
        <td>metrics</td>
        <td>指标配置</td>
        <td>产品中可以添加属性的指标配置，例如设置属性的范围、最大值等。设备无法添加指标配置，但每个设备可以单独设定指标的值。<br>可供其他模块使用，例如场景联动以属性的指标作为判断条件。</td>
      </tr>
      <tr>
        <td>storageType</td>
        <td>存储方式</td>
        <td>设置属性的存储方式。<br>direct：存储<br>ignore：不存储<br>json-string：将数据序列话为JSON字符串进行存储（Object类型时可用）。</td>
      </tr>
    </tbody>
</table>

#### 数据结构

```json5
    {
       "id": "cpu_usage", //属性标识
       "name": "CPU使用率",
       "valueType": {   //值类型
         "type": "double", //类型标识,见类型表
         "scale":2, //精度
         "unit":"percent", //单位
         "expands":{"key1":"value1"} //其他自定义拓展定义
       },
          "expands":{ //其他自定义拓展定义
                "source":"rule", //来源
                "virtualRule":{
                    "type":"window", //规则类型
                    "window":{ //规则窗口配置
                        "span":"3", //窗口长度
                        "every":"6" //步长
                    },
                    "windowType":"num", //窗口类型（num：次数窗口，time：时间窗口）
                    "aggType":"avg", //聚合函数
                    "script":"return $recent(\"temperature\")" //脚本内容
                }
                "type":[ //读写类型
                    "read",
                    "write",
                    "report"
                ],
                "metrics":[ //指标配置
                    {
                        "id":"max",
                        "name":"阈值",
                        "value":[
                            60
                        ]
                    }
                ],
                "storageType":"direct" //存储配置
            },
     }
```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

- 属性的值类型（**valueType**），由`org.jetlinks.core.metadata.DataType`的实现类定义。属性类型定义详情，见<a href='#数据类型'>数据类型</a>
- 属性的规则配置（**virtualRule**）的聚合函数在`org.jetlinks.pro.device.message.streaming.AggType`中定义。规则类型的定义可以参照`org.jetlinks.pro.device.message.streaming.DevicePropertyStreamingFactory`
- 指标配置（**metrics**）的管理参照`org.jetlinks.pro.things.impl.metric.DefaultPropertyMetricManager`。设备单独配置的指标保存在数据表`thing_property_metric`中。

</div>

## 功能

根据设备可供外部调用的指令或方法，定义设备功能。平台可主动调用设备功能。例如：播放语音，开关操作等。

#### 数据结构

    {
       "id": "playVoice", //功能标识
       "name": "播放声音", //名称
       "async": false, //是否异步
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
         "type": "boolean", //输出类型
         "properties": [ //输出参数类型为Object时，可配置属性
            {
               "valueType": {
                   "type": "string"
               },
               "name": "名称",
               "id": "id"
            }
         ]
       },
       "expands":{"key1":"value1"} //其他自定义拓展定义
     }

## 事件

用于定义设备事件，接收设备运行时，主动上报给平台的信息。如: 定时上报设备属性, 设备报警等.

数据结构:
```json5
{
    "id": "事件ID", 
    "name": "事件名称",
    "valueType": {
      "type": "object",  //对象(结构体)类型
      "properties": [    //对象属性(结构与属性定义相同)
        {
          "id": "属性ID",
          "name": "属性名称",
          "valueType": {
            "type": "string"
          }, 
           "expands":{"gis":"lng"} //其他自定义拓展定义
        }
      ]
    },
    "expands":{"key1":"value1"} //其他自定义拓展定义
 }
```
    

## 标签

用于设备的额外属性，添加拓展字段。

产品可以统一定义标签，设备默认自动继承标签配置。设备可以单独配置标签的值。

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

设备配置的标签值，存储在数据表`dev_device_tags`。

</div>


数据结构：
```json5
   {
      "valueType":{
         "type":"geoPoint",
         "expands":{ //其他自定义拓展定义
            "key1":"value1"
         },
         "unit": "percent" //单位
      },
      "expands":{
           "metrics":[
   
           ],
           "type":[
               "read",
               "write",
               "report"
           ]
       },
       "id":"标签ID",
       "name":"标签名称"
   }
```

## 数据类型

由`org.jetlinks.core.metadata.DataType`的实现类定义。  

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
```json5
   {
     "type":"double",
     "max":100,
     "min":0,
     "unit":"percent",
     "expands":{"readonly":true}
   }
```


### boolean 布尔类型

##### 属性
- trueText 为true时的文本,默认为`是`
- falseText 为false时的文本,默认为`否`
- trueValue  为true时的值,默认为`true`
- falseValue 为false时的值,默认为`false`

例:
```json5
        {
            "type":"boolean",
            "trueText":"开启",
            "falseText":"关闭",
            "trueValue":"1",
            "falseValue":"0"
        }
```
### string 字符类型

      例:
```json5
  {
     "type":"string",
     "expands":{"maxLen":"255"}
  }
```


### enum 枚举类型

##### 属性
  - elements (Element)枚举中的元素

 Element:
  - value 枚举值
  - text 枚举文本
  - description 说明

    例:
```json5
   {
      "type":"enum",
      "elements":[
          {"value":"1","text":"正常"},
          {"value":"-1","text":"警告"},
          {"value":"0","text":"未知"}
      ]
   }
```
### date 时间类型

##### 属性
   - format 格式,如: yyyy-MM-dd
   - tz 时区,如: Asia/Shanghai
    
 例:
```json5    
   {
      "type":"date",
      "format":"yyyy-MM-dd",
      "tz": "Asia/Shanghai"
   }
```

### password 密码类型

 与string类型相同

### file 文件类型

##### 属性
   - bodyType 类型: url,base64,binary
   
 例:
```json5    
   {
     "type":"file",
     "bodyType":"url"
   }
```        

### array 数组(集合)类型

##### 属性
   - elementType 元素类型
   
 例:
```json5        
   {
      "type":"array",
      "elementType":{ 
          "type":"string"
      }
   }
```

### object 对象(结构体)类型

##### 属性
     - properties 属性列表
     
  例:
```json5        
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
```

### geoPoint Geo地理位置类型

支持以逗号分割的经纬度字符串以及map类型.
默认支持3种格式转换: 逗号分割字符:`145.1214,126.123` ,json格式:`{"lat":145.1214,"lon":126.123}`.

   例:
```json5
   {
    "type":"geoPoint"
   }
```

## 产品与设备物模型关系
##### 产品与设备关系
产品和设备都有物模型配置。一个产品可以添加多个设备。
##### 物模型配置
当产品配置物模型时，产品下的设备自动继承所有的物模型配置。  

当设备单独配置物模型时，将脱离产品物模型的继承。设备拥有独立的物模型，对产品物模型的修改不会影响该设备。
<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

产品物模型的属性为`metadata`，设备派生物模型的属性为`deriveMetadata`。可使用接口`PUT` `/device-instance/{id}/metadata`更新设备的物模型。
，同时将在设备注册中心`org.jetlinks.core.device.DeviceRegistry`中添加设备物模型信息。通过`DeviceOperator#setConfigs`方法将物模型存储在设备自定义配置中，键值为`org.jetlinks.core.device.DeviceConfigKey.metadata`  

使用设备物模型时，优先使用设备的派生物模型。不存在时才会使用产品的物模型。

</div>

## 物模型的权限
物模型可根据协议包配置的特性（`MetadataFeature`）控制增删改以及设备派生的权限。详情见<a href='/dev-guide/protocol-support.html#配置基本信息'>协议开发说明</a>