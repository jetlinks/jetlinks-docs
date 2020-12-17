# 启动UI

## 源码自行构建
可以通过[UI源码](https://github.com/jetlinks/jetlinks-ui-antd)自行构建.

```bash
$ git clone https://github.com/jetlinks/jetlinks-ui-antd.git
```
修改后台接口地址：  
后台接口配置文件地址：`config/proxy.ts`:  

```typescript
/**
 * 在生产环境 代理是无法生效的，所以这里没有生产环境的配置
 */
export default {
  dev: {
    '/jetlinks': {
      target: '后台地址',
      changeOrigin: true,
      pathRewrite: { '^/jetlinks': '' },
    },
  },
  test: {
    '/jetlinks': {
      target: '后台地址',
      changeOrigin: true,
      pathRewrite: { '^/jetlinks': '' },
    },
  },
  pre: {
    '/jetlinks': {
      target: '后台地址',
      changeOrigin: true,
      pathRewrite: { '^/jetlinks': '' },
    },
  },
};
```
启动：  
```bash
$ cd jetlinks-ui-antd
$ npm install
$ npm start         # visit http://localhost:8000
```
本地开发项目建议使用如下命令启动项目

```bash
$ npm run start:dev     //支持：dev、test、pre环境
```

项目多处采用了 SSE 接口交互，开发需要使用 dev 环境变量（生产环境使用 nginx 代理了 EventSource 接口）。  

::: tip 注意：
本地开发环境要求   
- nodeJs v12.14
- npm v6.13
- Chrome v80.0
:::

## docker启动
使用`docker`启动UI:

```bash
$ docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://host.docker.internal:8848/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd
```

::: tip 注意：
环境变量`API_BASE_PATH`为后台API根地址. 由docker容器内进行自动代理. 请根据自己的系统环境配置环境变量: `API_BASE_PATH`
:::

## docker-compose 启动

在[使用docker快速启动所有环境](./docker-start.md#启动)中包含了ui的启动，配置如下：  
```yaml
  ui:
    image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd:1.3.0
    container_name: jetlinks-ce-ui
    ports:
      - 9000:80
    environment:
      - "API_BASE_PATH=http://jetlinks:8848/" #API根路径，此处docker-compose已链接到jetlinks服务，只需修改对应服务端口
    volumes:
      - "jetlinks-volume:/usr/share/nginx/html/upload"
    links:
      - jetlinks:jetlinks  
```

::: tip 注意：
本地源码构建后ui的端口为8000，docker或者docker-compose启动的需自行映射端口，本文档默认为9000端口。
:::