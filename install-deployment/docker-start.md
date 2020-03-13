# 使用docker快速启动全部环境

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