# 非docker环境启动

请先安装以下服务: `postgresql 11`,`redis 5.x`,`elasticsearch 6.7.2`.

::: tip 提示
 `postgresql`可更换为`mysql 5.7+`或者`sqlserver`,只需要修改配置中的`spring.r2dbc`和`easyorm`相关配置项即可.
:::

- 步骤1: 根据情况修改`jetlinks-standalone`模块下的配置文件:`application.yml`中相关配置.

```yaml
spring:
  redis:
    host: 127.0.0.1 # redis配置
    port: 6379
  r2dbc:
    url: r2dbc:postgresql://127.0.0.1:5432/jetlinks  # 数据库postgresql数据库配置
    username: postgres
    password: jetlinks
easyorm:
  default-schema: public # 数据库默认的schema
  dialect: postgres #数据库方言
elasticsearch:
  client:
    host: 127.0.0.1   # elasticsearch
    port: 9200
hsweb:
  file:
    upload:
      static-file-path: ./static/upload   # 上传的文件存储路径
      static-location: http://127.0.0.1:8848/upload # 上传的文件访问根地址
```

- 步骤2: [启动JetLinks服务](ide-docker-start.md#启动JetLinks服务).

- 步骤3: [启动UI](ui-start.md)  

**启动成功后访问系统**  

地址: `http://localhost:9000`, 用户名:`admin`,密码:`admin`.