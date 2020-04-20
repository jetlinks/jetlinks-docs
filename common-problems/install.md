# 安装,启动常见问题

## 使用docker启动后无法访问系统或者提示服务器内部错误

大部分原因是由于服务未正确启动,需要根据日志检查,查看日志: `docker-compose logs jetlinks`.

### 错误日志: elasticsearch: Name or Service not know

`elasticsearch`未正确启动,使用命令`docker-compose logs elasticsearch`查看日志,根据日志提示解决.

## 更换数据库

目前平台支持mysql、mssql、oracle、postgres、postgres数据库。

更换数据库需要注意的事项：  
1. 将application.yml中的数据库方言替换为目标方言，如：  

```yaml
easyorm:
  default-schema: public # 数据库默认的schema
  dialqqect: mysql #数据库方言
```

2.更换数据库地址  
```yaml
r2dbc:
  url: r2dbc:mysql://localhost:3306/jetlinks
```

## maven依赖问题
大部分是因为配置了全局私服，在setting.xml文件中取消即可。  