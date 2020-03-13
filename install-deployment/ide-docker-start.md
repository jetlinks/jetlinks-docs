# 使用docker启动开发环境,使用IDE中启动JetLinks服务

- 步骤1: 启动环境

```bash
$ cd docker/dev-env
$ docker-compose up
```

- 步骤2: 启动JetLinks服务

项目导入IDE后执行`jetlinks-standalone`模块下的`org.jetlinks.community.standalone.JetLinksApplication`.
或者使用命令行运行:

```java

$  ./mvnw clean package -DskipTests && java -jar ./jetlinks-standalone/target/jetlinks-standalone.jar

```

::: tip 注意
项目需要使用最新的java8(小版本号大于200),如`1.8.0_232`
:::

- 步骤3: 启动UI

可以通过[UI源码](https://github.com/jetlinks/jetlinks-ui-antd)自行构建.

或者使用`docker`启动UI:

```bash
$ docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://host.docker.internal:8848/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd
```

::: tip 注意
环境变量`API_BASE_PATH`为后台API根地址. 由docker容器内进行自动代理. 请根据自己的系统环境配置环境变量: `API_BASE_PATH`
:::