# 安装,启动常见问题

## 使用docker启动UI后无法访问系统或者提示服务器内部错误

可能原因： docker启动UI时设置的`API_BASE_PATH`无法访问后台服务。  
```shell script
$ docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://host.docker.internal:8848/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd
```
需要将`API_BASE_PATH`修改为后台服务ip地址+端口。    

修改`API_BASE_PATH`后仍未解决问题则可能是因为服务未正确启动,需要根据日志检查,查看日志: `docker-compose logs jetlinks`。  

## 通过UI源码自行构建启动后无法访问系统或账号密码错误 

大部分是因为前端代码中的代理后台地址配置错误  

![ui代理地址](images/ui-api.png)  

**修改上图中target为后台服务ip+端口即可**。  

## 使用docker启动所有环境后无法访问系统或者提示服务器内部错误
可能原因： docker启动UI时设置的`API_BASE_PATH`无法访问后台服务。
```yaml
  ui:
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd:1.1.1-RELEASE
    container_name: jetlinks-ce-ui
    ports:
      - 9000:80
    environment:
      - "API_BASE_PATH=http://jetlinks:8848/" #API根路径
    volumes:
      - "jetlinks-volume:/usr/share/nginx/html/upload"
    links:
      - jetlinks:jetlinks
```
查看项目目录docker/run-all下docker-compose.yml文件中ui的环境变量`API_BASE_PATH`，端口是否与后台服务端口一致，否则改之。  
## 错误日志: elasticsearch: Name or Service not know

`elasticsearch`未正确启动,使用命令`docker-compose logs elasticsearch`查看日志,根据日志提示解决.

## 更换数据库

目前平台支持mysql、mssql、postgres数据库。

更换数据库（此处以mysql为例）需要注意的事项：  

1. 更换数据库地址  
```yaml
r2dbc:
  url: r2dbc:mysql://localhost:3306/jetlinks
```

2.将application.yml中的数据库方言替换为目标方言，如：  

```yaml
easyorm:
  default-schema: jetlinks # 数据库默认的schema
  dialect: mysql #数据库方言
```
::: warning 注意:
在mysql中此处default-schema应修改为mysql中存在的数据库名称，  
如第一步url中数据库名为jetlinks，default-schema也应为jetlinks。
:::

若出现以下警告，应考虑mysql是否已创建相应库。  
![mysql-connection-warning](images/mysql-connection-warning.png)  

## maven依赖问题
大部分是因为配置了全局私服，在setting.xml文件中取消即可。  

## 协议发布失败或出现不支持的协议：xxx

大部分原因是后台配置的文件上传路径错误。
修改配置/jetlinks-standalone/src/main/resources/application.yml：  
```yaml
  file:
    upload:
      static-file-path: ./static/upload
      static-location: http://localhost:${server.port}/upload
```
::: tip 注意:
localhost应为部署平台服务的ip地址
::: 
若为docker启动的平台，则修改/docker/run-all/docker-compose.yml文件下的环境变量：  
```yaml
jetlinks:
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-standalone:1.0-SNAPSHOT
    volumes:
      - "jetlinks-volume:/static/upload"  # 持久化上传的文件
    environment:
     # - "JAVA_OPTS=-Xms4g -Xmx18g -XX:+UseG1GC"
      - "hsweb.file.upload.static-location=http://127.0.0.1:8848/upload"  #上传的静态文件访问根地址,为ui的地址.
```