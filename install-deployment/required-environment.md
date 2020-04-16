# 平台所需环境

## 硬件环境要求

处理器:4核及以上；  
内存：8G以上；  
磁盘：根据需求调整。  

## 需安装的服务
postgresql 11,redis 5.x,elasticsearch 6.8.x.  

::: tip 注意
以上服务在docker方式部署时使用docker-compose启动，可不再安装。
:::
::: tip 提示
 `postgresql`可更换为`mysql 5.7+`或者`sqlserver`,只需要修改配置中的`spring.r2dbc`和`easyorm`相关配置项即可.
:::