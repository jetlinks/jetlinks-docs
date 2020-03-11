# 安装,启动常见问题

## 使用docker启动后无法访问系统或者提示服务器内部错误

大部分原因是由于服务未正确启动,需要根据日志检查,查看日志: `docker-compose logs jetlinks`.

### 错误日志: elasticsearch: Name or Service not know

`elasticsearch`未正确启动,使用命令`docker-compose logs elasticsearch`查看日志,根据日志提示解决.
