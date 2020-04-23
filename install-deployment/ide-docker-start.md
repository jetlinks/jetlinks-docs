# 使用docker启动开发环境,使用IDE启动JetLinks服务

在[获取源代码](docker-start.md#获取源代码)、[安装docker](docker-start.md#安装docker)基础上，使用docker+源代码启动环境。  

## 所需环境
### 硬件环境要求

处理器:4核及以上；  
内存：8G以上；  
磁盘：根据需求调整。  

### 需安装的服务
postgresql 11,redis 5.x,elasticsearch 6.8.x.  
::: warning 注意
以上服务在docker方式部署时使用docker-compose启动，可不再安装。
:::

::: tip 提示
 `pwsostgresql`可更换为`mysql 5.7+`或者`sqlserver`,只需要修改配置中的`spring.r2dbc`和`easyorm`相关配置项即可.
:::
## 启动环境
进入项目docker目录，执行docker-compose命令。  
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

::: warning 注意
1.项目需要使用最新的java8(小版本号大于200),如`1.8.0_232`。  
2.需安装lombox插件。  
3.maven不能配置全局私服。
:::
