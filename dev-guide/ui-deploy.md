# 前端部署及常见问题
本文档部署环境为Centos7，提供了两种部署前端的方式分别为源码部署和docker部署，可根据实际情况选择对应的部署方式。
## 源码方式部署
### 版本说明
<li>nodeJs v12.18.1</li>
<li>yarn 1.22.19</li>

### 打包前端文件
1. 获取源代码
```shell script
$ git clone git@github.com:jetlinks/jetlinks-ui-antd.git
```
<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>源码克隆下来之后，需要将分支从master切换到2.0分支</li>
</div>

2. 使用yarn打包,并将打包后生成的dist文件复制到项目的docker目录下（命令在项目根目录下执行）  
```shell script
yarn install
yarn run-script build 
cp -r dist docker/       
```

<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>

`yarn install`时出现版本`node`版本不兼容的报错
```
error react-dev-utils@12.0.1: The engine "node" is incompatible with this module. Expected version ">=14". Got "12.18.1"
error Found incompatible module.
```
使用`yarn install --ignore-engines`命令忽略，再重新执行`yarn install`命令

</div>




### 使用docker部署前端
1. 构建docker镜像
```bash
docker build -t registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0 ./docker
```
查看镜像是否构建成功`docker images`
```
$ docker images
REPOSITORY                                                    TAG                 IMAGE ID       CREATED          SIZE
registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-pro    2.0.0               f76af6002bcd   13 seconds ago   176MB
```

2. 将镜像推送到远程仓库
```bash
#登录阿里云仓库
docker login --username=[username] registry.cn-hangzhou.aliyuncs.com
#设置tag
docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0-SNAPSHOT
#推送到阿里云仓库
docker push registry.cn-hangzhou.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0-SNAPSHOT
```

查看是否推送成功

![](./images/push_images.png)

3. 运行docker镜像
```bash
docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://xxx:8844/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-pro:2.0.0
```

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
环境变量API_BASE_PATH为后台API根地址，由docker容器内进行自动代理，请根据自己的系统环境配置环境变量，API_BASE_PATH建议使用ipv4地址，若使用locahost或者127.0.0.1，可能会导致启动镜像失败
</div>

### 使用nginx部署前端
1. 将打包的dist文件，传入服务器`/usr/share/nginx/html`路径下
2. 修改nginx.conf配置文件，nginx配置参考: 

```conf
server {
    listen 80;
    # gzip config
    gzip on;
    gzip_min_length 1k;
    gzip_comp_level 9;
    gzip_types text/plain text/css text/javascript application/json application/javascript application/x-javascript application/xml;
    gzip_vary on;
    gzip_disable "MSIE [1-6]\.";

    root /usr/share/nginx/html;
    include /etc/nginx/mime.types;
    location / {
        index  index.html;
    }

    location ^~/api/ {
        proxy_pass http://xxx:8844/; #修改此地址为后台服务地址
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Host $host:$server_port;
        proxy_set_header X-Real-IP  $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_connect_timeout   1;
        proxy_buffering off;
        chunked_transfer_encoding off;
        proxy_cache off;
        proxy_send_timeout      30m;
        proxy_read_timeout      30m;
        client_max_body_size    100m;
    }
}
```
3. 使用`./nginx`命令启动前端

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
  <li>前端启动后，要保证后端也是启动状态，才能加载出web页面</li>
</div>