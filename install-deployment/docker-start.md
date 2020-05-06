# 使用docker快速启动全部环境

## 所需环境
### 硬件环境要求

处理器:4核及以上；  
内存：8G以上；  
磁盘：根据需求调整。  

### 需安装的服务
postgresql 11,redis 5.x,elasticsearch 6.8.x.  

::: tip 注意
以上服务在docker方式部署时使用docker-compose启动，可不再安装。
:::
::: tip 提示
 `postgresql`可更换为`mysql 5.7+`或者`sqlserver`,只需要修改配置中的`spring.r2dbc`和`easyorm`相关配置项即可.
:::

## 获取源代码
jetlinks源代码托管在[GitHub](https://github.com/jetlinks/jetlinks-community)和[gitee](https://gitee.com/jetlinks/jetlinks-community)，可直接前往克隆或者下载。  

建议使用git clone。  
```bash

$ git clone https://github.com/jetlinks/jetlinks-community.git && cd jetlinks-community

```

## 安装docker

根据不同操作系统选择安装docker-ce或者Docker Desktop。  
- [Mac](https://hub.docker.com/editions/community/docker-ce-desktop-mac)
- [Linux](https://hub.docker.com/search?q=&type=edition&offering=community&sort=updated_at&order=desc&operating_system=linux)
- [Windows](https://hub.docker.com/editions/community/docker-ce-desktop-windows)  

## 启动
[获取源代码](#获取源代码)成功后，进入docker/run-all目录。  
```bash

$ cd docker/run-all
$ docker-compose up

```
::: tip 注意：
docker-compose中的jetlinks镜像持续更新中，启动docker之前请及时下载更新。
:::