# JetLinks Pro 开发手册
JetLinks Pro 基于java8,spring-boot 2.2,webflux,netty,vert.x等开发,
是一个全响应式([reactor](https://github.com/reactor))的物联网基础平台.

# 技术选型
1. Java8
2. hsweb Framework       业务基础框架
3. Spring Boot 2.2.x     响应式web支持
4. vert.x,netty          高性能网络框架
5. R2DBC                 关系型数据库响应式驱动
6. Postgresql            可更换为mysql,sqlserver
7. ElasticSearch         设备数据,日志存储(可更换为其他中间件)

## 模块说明

JetLinks Pro 使用 [git submodule](https://git-scm.com/book/en/v2/Git-Tools-Submodules) + `maven多模块`
来进行代码管理,实现组件化开发.可按需引入需要的模块.


## 配置项



## API