# 启动UI

可以通过[UI源码](https://github.com/jetlinks/jetlinks-ui-antd)自行构建.

或者使用`docker`启动UI:

```bash
$ docker run -it --rm -p 9000:80 -e "API_BASE_PATH=http://host.docker.internal:8848/" registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd
```

::: tip 注意
环境变量`API_BASE_PATH`为后台API根地址. 由docker容器内进行自动代理. 请根据自己的系统环境配置环境变量: `API_BASE_PATH`
:::