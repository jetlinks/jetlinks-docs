# 开发环境最佳实践

推荐使用docker+源码的方式来快速搭建开发环境.

使用docker安装所需环境(redis,elasticsearch,PostgreSQL).

## 安装docker

dockerDesktop[安装地址](https://www.docker.com/products/docker-desktop)

## 获取源代码
jetlinks源代码托管在[GitHub](https://github.com/jetlinks/jetlinks-community)和[gitee](https://gitee.
com/jetlinks/jetlinks-community)，可直接前往克隆或者下载。(记得star哟，您的star是我们前进的动力)


## 启动环境
[获取源代码](#获取源代码)成功后，进入docker/dev-env目录,启动所需要的环境。

```bash

$ cd docker/dev-env
$ docker-compose up
```


 

