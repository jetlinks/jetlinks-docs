# 前端部署说明

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
  <p>本文档部署环境为Centos7，提供了两种部署方式分别为源码部署和docker部署，可根据实际情况选择对应的部署方式。社区版、企业版和微服务版共用一个前端，部署方式统一参照本文档。</p>
</div>

## 指导介绍
1. <a href="/dev-guide/ui-deploy.html#打包前端镜像">使用命令打包前端镜像</a>
2. <a href="/dev-guide/ui-deploy.html#修改nginx文件">修改nginx文件，使用源码方式启动</a>
3. <a href="/dev-guide/ui-deploy.html#创建阿里云镜像仓库">创建阿里云镜像仓库</a>
4. <a href="/dev-guide/ui-deploy.html#推送镜像到仓库">推送镜像到仓库，使用镜像方式启动</a>

## 问题指引
<table>
<tr>
    <td><a href="/dev-guide/ui-deploy.html#出现node版本不兼容的报错">出现node版本不兼容的报错</a></td>
    <td><a href="/dev-guide/ui-deploy.html#启动前端失败报502状态码">启动前端失败报502状态码</a></td>
</tr>

</table>

## 源码方式部署

### 版本说明

- nodeJs v16.18.1
- npm v8.19.2
- yarn v1.22.19

### 操作步骤
#### 拉取源代码
1. 拉取源代码

```shell
$ git clone git@github.com:jetlinks/jetlinks-ui-antd.git
```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
  <p>前端源码是开源的且无额外的仓库，源码克隆下来之后，需要将分支从master切换到2.0分支。</p>
</div>

#### 打包前端镜像
2. 使用yarn打包,并将打包后生成的dist文件复制到项目的docker目录下（命令在项目根目录下执行）

```shell
yarn install
yarn build 
```
<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
<p>前端请使用yarn打包，已使用过npm打包的前端，需要删除node_modules、package-lock.json、yarn.lock文件后重新yarn及yarn打包</p>

</div>

#### 修改nginx文件
3. 将打包的`dist`资源文件，上传至服务器上。

4. 使用`pwd`查看`dist`目录的绝对路径。

5. 修改nginx.conf配置文件，nginx配置参考:

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

    # 修改为pwd命令指向的完整资源路径
    root /usr/share/nginx/html;
    include /etc/nginx/mime.types;
    location / {
        index  index.html;
    }

    location ^~/api/ {
        #修改此地址为后台服务地址
        proxy_pass http://xxx:8844/; 
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

6. 启动nginx服务。
```shell
#进入nginx下的sbin文件，sbin路径根据实际情况进行替换,示例如下
cd /usr/local/nginx/sbin
./nginx
```



### 使用docker部署前端

#### 创建阿里云镜像仓库

1. 进入阿里云容器镜像服务页面
网址:`https://cr.console.aliyun.com/cn-hangzhou/instances`
2. 开通镜像服务

   ![open-image](./images/open-image.png)
   ![registry-password](./images/registry-password.png)

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
<p>这里设置的密码需要记住，后面推送镜像到仓库时需要用到</p>

</div>

3. 创建命名空间

   ![registry-name](./images/registry-name.png)

4. 创建镜像仓库
   ![registry-create](./images/registry-create.png)

#### 推送镜像到仓库

1. 将`dist`资源文件目录拷贝至`docker`目录下
2. 构建docker镜像

```bash
docker build -t registry.cn-shenzhen.aliyuncs.com/jetlinks-ui-test/jetlinks-ui-pro:2.0.0 ./docker
```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
  <p>如需提交自己的仓库，请自行更换自己的仓库地址。</p>
</div>


3. 查看镜像是否构建成功`docker images`

```
$ docker images
REPOSITORY                                                            TAG                 IMAGE ID       CREATED          SIZE
registry.cn-shenzhen.aliyuncs.com/jetlinks-ui-test/jetlinks-ui-pro    2.0.0               f76af6002bcd   13 seconds ago   176MB
```

4. 将镜像推送到远程仓库

```bash
#登录阿里云镜像仓库，此处会让你输密码，就是创建镜像服务时自己设置的密码
docker login --username=[username] registry.cn-hangzhou.aliyuncs.com
#设置tag
docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/jetlinks-ui-test/jetlinks-ui-pro:2.0.0-SNAPSHOT
#推送到阿里云镜像仓库
docker push registry.cn-hangzhou.aliyuncs.com/jetlinks-ui-test/jetlinks-ui-pro:2.0.0-SNAPSHOT
```

<div class='explanation info'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
  username和ImageId这两个参数需要自行替换,username为阿里云账户全名,ImageId为本地构建镜像的id
</div>

5. 查看是否推送成功

![](./images/push_images.png)

6. 运行docker镜像

```bash
docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://xxx:8844/" registry.cn-shenzhen.aliyuncs.com/jetlinks-ui-test/jetlinks-ui-pro:2.0.0
```

<div class='explanation info'>
  <p class='explanation-title-warp'> 
    <span class='iconfont icon-tishi explanation-icon'></span>
    <span class='explanation-title font-weight'>提示</span>
  </p>
<p>环境变量<span class='explanation-title font-weight'>API_BASE_PATH</span>为后台API根地址，由docker容器自行转发代理，请根据自己的系统配置，
<span class='explanation-title font-weight'>API_BASE_PATH</span>请使用ipv4地址，使用locahost或者127.0.0.1会因docker地址解析错误而启动镜像失败。</p>
</div>

### 使用docker离线部署前端

1. 将打包生成的dist文件传入根目录下的docker文件下
2. 创建前端镜像文件，命令如下:
```shell
docker build -t jetlinks-ui:2.0.0 .
```
3. 导出前端镜像文件，命令如下:

```shell
#命令格式
docker save -o [命名].tar [镜像名:版本号]
#命令示例
docker save -o jetlinks-ui.tar jetlinks-ui:2.0.0
```
4. 修改docker-compose文件
```yaml
version: '2'
services:
  ui:
    image: jetlinks-ui:2.0.0 #根据自己构建的镜像名:版本号替换
    container_name: jetlinks-ce-ui-2.0
    ports:
      - 9000:80
    environment:
      - "API_BASE_PATH=http://192.168.66.171:8844/" #API根路径 此处需要修改，改为后端实际部署服务器ip地址以及后端端口号
    volumes:
      - "./data/upload:/usr/share/nginx/html/upload"
```
5. 将镜像文件和docker-compose文件传入服务器
6. 导入镜像文件
```shell
#导入命令格式
docker load -i [镜像名.tar]
#导入命令示例
docker load -i jetlinks-ui.tar 
```
7.查看是否镜像导入成功
```shell
[root@localhost ~]# docker images
REPOSITORY                                                    TAG               IMAGE ID       CREATED        SIZE
jetlinks-ui-antd                                              2.0.0             b5b3c833c060   4 weeks ago    187MB
```
8.启动前端
```shell
docker-compose up -d
```

### 常见问题

#### 出现node版本不兼容的报错
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题1</span>
  </p>

Q:`yarn install`时出现`node`版本不兼容的报错。

```shell
error react-dev-utils@12.0.1: The engine "node" is incompatible with this module. Expected version ">=14". Got "12.18.1"
error Found incompatible module.
```

A：使用`yarn install --ignore-engines`命令忽略，再重新执行`yarn build`命令打包资源文件。

</div>

#### 启动前端失败报502状态码
<div class='explanation warning'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>问题2</span>
  </p>
  <p>Q：如访问前端时发现接口出现502状态码？</p>
  <p>A：502、504状态码一般是后端服务异常、或者nginx配置文件内的<span class='explanation-title font-weight'>proxy_pass</span>不正确导致的。</p>
</div>