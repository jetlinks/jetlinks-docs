# 使用docker启动开发环境,使用IDE启动JetLinks服务

在[获取源代码](docker-start.md#获取源代码)、[安装docker](docker-start.md#安装docker)基础上，使用docker+源代码启动环境。  
## 启动环境

```bash
$ cd docker/dev-env
$ docker-compose up
```

## 启动JetLinks服务

项目导入IDE后执行`jetlinks-standalone`模块下的`org.jetlinks.community.standalone.JetLinksApplication`.
或者使用命令行运行:

```java

$  ./mvnw clean package -DskipTests && java -jar ./jetlinks-standalone/target/jetlinks-standalone.jar

```

::: tip 注意
项目需要使用最新的java8(小版本号大于200),如`1.8.0_232`
:::
