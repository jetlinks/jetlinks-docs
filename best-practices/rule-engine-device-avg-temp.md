# 在规则引擎中统计设备平均温度

原理: 利用`ReactorQL`订阅设备的实时数据,使用聚合函数来处理数据.

## 创建规则实例

TODO

## 创建ReactorQL节点

TODO

```sql

select 
avg(this.properties.temperature) avg, --平均温度
max(this.properties.temperature) max, --最大值
min(this.properties.temperature) min, --最小值
count(1) count  --总计上报次数
from "/device/t-sensor/*/message/property/**" --订阅t-sensor型号下所有设备
group by interval('1m')
having avg > 30 -- 平均温度高于30度 就发送数据到下一个节点

```

## 发送邮件通知

TODO