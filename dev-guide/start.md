# JetLinks 开发手册
JetLinks 基于java8,spring-boot 2.2,webflux,netty,vert.x等开发,
是一个全响应式([reactor](https://github.com/reactor))的物联网基础平台.
使用 `maven多模块`,实现组件化开发.可按需引入需要的模块.

# 技术选型
1. Java8
2. hsweb Framework       业务基础框架
3. Spring Boot 2.2.x     响应式web支持
4. vert.x,netty          高性能网络框架
5. R2DBC                 关系型数据库响应式驱动
6. Postgresql            可更换为mysql,sqlserver
7. ElasticSearch         设备数据,日志存储(可更换为其他中间件)

## 配置

JetLinks基于`SpringBoot`, 配置方式与SpringBoot完全一致:`application.yml`

主要配置项:

```yaml
    spring:
      redis:  # redis配置 ,spring-data-redis
        host: localhost 
        port: 6379
      r2dbc:  # 数据库配置
        url: r2dbc:postgresql://localhost:5432/jetlinks
        username: postgres
        password: jetlinks
    elasticsearch: # elasticsearch 配置
      client: 
         host: localhost
         port: 9200 
    easyorm:
      default-schema: public # 数据库默认的schema
      dialect: postgres #数据库方言,支持h2,mysql,postgres,sqlserver
    hsweb:
      file:
        upload: # 文件上传
            static-file-path: ./static/upload   # 静态文件保存目录
            static-location: http://localhost:8848/upload   #静态文件下载路径
```