# 常用枚举

在接口中常用的枚举将在此文档中列出。

## DeviceType

value       |    text
--------------  | ------------- 
device | 直连设备                              
childrenDevice | 网关子设备
gateway |  网关设备 

## DeviceState

value       |  text  
--------------  | ------------- 
notActive |          未激活                              
offline | 离线
online |  在线 

## DeviceLogType

value       |  text  
--------------  | ------------- 
event|事件上报
readProperty|属性读取
writeProperty|属性修改
reportProperty|属性上报
child|子设备消息
childReply|子设备消息回复
functionInvoke|调用功能
readPropertyReply|读取属性回复
writePropertyReply|修改属性回复
functionReply|调用功能回复
register|设备注册
unregister|设备注销
offline|离线
online|上线
other|其它

## Type
value      |   描述
--------------- | ----------------
Point | 点
MultiPoint | 多个点
LineString | 线
MultiLineString | 多条线
Polygon | 多边形
MultiPolygon | 多个多边形
GeometryCollection | 数据集合,包含点线
