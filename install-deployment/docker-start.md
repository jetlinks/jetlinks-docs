# 使用Docker启动

## 安装docker

根据不同操作系统选择安装docker-ce或者Docker Desktop。  
- <a href='https://hub.docker.com/editions/community/docker-ce-desktop-mac'>Mac</a>
- <a href='https://hub.docker.com/search?q=&type=edition&offering=community&sort=updated_at&order=desc&operating_system=linux'>Linux</a>
- <a href='https://hub.docker.com/editions/community/docker-ce-desktop-windows'>Windows</a>  


## 获取源代码
jetlinks源代码托管在<a href='https://gitee.com/jetlinks/jetlinks-community'>Gitee</a>，可直接前往克隆或者下载。  

建议使用git clone。
```bash

$ git clone -b 2.0 https://gitee.com/jetlinks/jetlinks-community.git && cd jetlinks-community

```

## 启动
<a href='#获取源代码'>获取源代码</a>成功后，进入docker/run-all目录。  
```bash

 cd docker/run-all
 docker-compose up

```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>

docker-compose中的jetlinks、jetlinks-ui-pro镜像持续更新中，启动docker之前请及时下载更新。

</div>

## 启动成功后访问系统  

地址: `http://localhost:9000`  

默认用户名:`admin`，密码:`admin`