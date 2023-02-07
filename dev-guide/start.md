# 介绍
## JetLinks 开发手册
JetLinks 基于java8，spring-boot 2.7，webflux，netty，vert.x等开发，
是一个全响应式(<a target='_blank' href='https://github.com/reactor'>reactor</a>)的物联网基础平台。
使用 `maven多模块`，实现组件化开发。可按需引入需要的模块。

## 技术选型
<table class='table'>
        <thead>
            <tr>
              <td>技术栈</td>
              <td>描述</td>
            </tr>
        </thead>
        <tbody>
          <tr>
            <td>Java8</td>
            <td>编程语言</td>
          </tr>
          <tr>
            <td>hsweb Framework</td>
            <td>业务基础框架</td>
          </tr>
          <tr>
            <td>Spring Boot 2.7.x</td>
            <td>响应式web支持</td>
          </tr>
          <tr>
            <td>vert.x，netty</td>
            <td>高性能网络框架</td>
          </tr>
          <tr>
            <td>R2DBC</td>
            <td>关系型数据库响应式驱动</td>
          </tr>
          <tr>
            <td>Postgresql</td>
            <td>关系型数据库，可更换为mysql、sqlserver</td>
          </tr>
          <tr>
            <td>ElasticSearch</td>
            <td>设备数据与日志存储，可更换为其他中间件</td>
          </tr>
          <tr>
            <td>Redis</td>
            <td>用户信息与权限缓存、设备注册中心缓存</td>
          </tr>
          <tr>
            <td>scalecube</td>
            <td>基于JVM的分布式服务框架，支持响应式</td>
          </tr>
          <tr>
            <td>micrometer</td>
            <td>监控指标框架</td>
          </tr>
        </tbody>
      </table>

## 配置

JetLinks基于`SpringBoot`, 配置方式与SpringBoot完全一致：`application.yml`

主要配置项:

```yaml
    spring:
      redis:  # redis配置 ,spring-data-redis
        host: localhost 
        port: 6379
      r2dbc:  # 数据库配置
        url: r2dbc:postgresql://localhost:5432/jetlinks
        username: postgres
        password: jetlinks
      elasticsearch: # elasticsearch 配置
        uris: localhost:9200
        socket-timeout: 10s
        connection-timeout: 15s
        webclient:
          max-in-memory-size: 100MB
    easyorm:
      default-schema: public # 数据库默认的schema
      dialect: postgres #数据库方言,支持h2,mysql,postgres,sqlserver
    hsweb:
      file:
        upload: # 文件上传
            static-file-path: ./static/upload   # 静态文件保存目录
            static-location: http://localhost:8848/upload   #静态文件下载路径
    system:
      config:
        scopes:
          - id: front
            name: 前端配置
            public-access: true
          - id: paths
            name: 访问路径配置
            public-access: true
            properties:
              - key: base-path
                name: 接口根路径
                default-value: http://localhost:9000/api
              - key: sso-redirect
                name: sso回调路径
                default-value: http://localhost:9000
              - key: sso-bind
                name: sso用户绑定路径
                default-value: http://localhost:9000/#/account/center/bind
              - key: sso-token-set
                name: sso登陆成功后Token设置路径
                default-value: http://localhost:9000/api/token-set.html
          - id: amap
            name: 高德地图配置
            public-access: false
            properties:
              - key: apiKey # 配置id
                name: 高德地图ApiKey # 名称
    network:
      resources: # 可用网络资源配置
        - 1883-1890
        - 8800-8810
        - 5060-5061
```