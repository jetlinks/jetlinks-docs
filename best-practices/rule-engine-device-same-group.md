# 当房间开门时,打开对应房间当空调

原理: 

1. 利用`ReactorQL节点`来订阅设备的开门事件. 
2. 利用`设备指令`节点选择相同分组内的另外一个设备.

## 创建智能门锁设备

产品ID: t-lock

TODO

## 创建智能空调设备

产品ID: t-smart-ac

TODO

## 创建房间分组

1. 创建分组
2. 关联智能门锁和智能空调


## 创建规则实例

### 创建ReactorQL节点

TODO

```sql
select 
t.deviceId
from "/device/t-lock/*/message/event/open-door" t
```

### 创建设备指令

选择设备输入:
```text
same_group(deviceId),product('t-smart-ac')
```

消息内容输入:
```json
{
  "messageType": "INVOKE_FUNCTION",
  "functionId":"open",
  "inputs": []
}
```

## 模拟器设备消息

TODO

