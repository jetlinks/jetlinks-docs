# 转发设备消息到消息中间件

在`1.3`版本中增加了将设备消息转发到`rabbitMQ`或者`kafka`的功能.

## 转发到RabbitMQ

修改配置文件`application.yml`:

```yml
spring: 
  rabbitmq: # rabbitMQ配置
    host: localhost
    port: 5672
    username: admin
    password: jetlinks
device:
  message:
    writer:
      time-series:
        enabled: true # 直接写出设备消息数据到时序数据库
      rabbitmq:
        enabled: true  # 推送设备消息到rabbitMQ
        consumer: false  # 从rabbitMQ订阅消息并写入到时序数据库,与time-series.enabled不能同时为true
        thread-size: 4 # 消费线程数
        auto-ack: true  # 自定应答,为true可能导致数据丢失,但是性能最高。
        topic-name: device.message  # rabbitMQ exchange名称
        #ignore-message-types: INVOKE_FUNCTION,READ_PROPERTY # 忽略转发消息类型
        #include-message-types: EVENT    # 只转发指定的消息类型
```


## 转发到Kafka

修改配置文件`application.yml`:

```yml
spring:
  kafka:
    consumer:
      client-id: ${spring.application.name}-consumer:${server.port}
      group-id: ${spring.application.name}
      max-poll-records: 1000
    producer:
      client-id: ${spring.application.name}-producer:${server.port}
      acks: 1
      retries: 3
    bootstrap-servers: ["127.0.0.1:9092"]
device:
  message:
    writer:
      time-series:
        enabled: true # 直接写出设备消息数据到时序数据库
      kafka:
        enabled: true  # 推送设备消息到kafka
        consumer: false  # 从kafka订阅消息并写入到时序数据库,与time-series.enabled不能同时为true
        topic-name: device.message  # kafka topic名称
        #ignore-message-types: INVOKE_FUNCTION,READ_PROPERTY # 忽略转发消息类型
        #include-message-types: EVENT    # 只转发指定的消息类型
```