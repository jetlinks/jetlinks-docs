# Jetlinks-Pro镜像试用教程

1. 安装docker、docker-compose。  

2. docker登录到registry.cn-shenzhen.aliyuncs.com。  

    ```shell script
    sudo docker login --username=test@jetlinks regsitry.cn-shenzhen.aliyuncs.com
    ```
    **密码为：test123456**  

3. 创建docker-compose.yml文件。  

    内容为：  
    ```yaml
    version: '2'
    services:
      redis:
        image: redis:5.0.4
        container_name: jetlinks-redis
        ports:
          - "6379:6379"
        volumes:
          - "./data/redis:/data"
        command: redis-server --appendonly yes
        environment:
          - TZ=Asia/Shanghai
      elasticsearch:
        image: elasticsearch:6.8.10
        container_name: jetlinks-elasticsearch
        environment:
          ES_JAVA_OPTS: "-Djava.net.preferIPv4Stack=true -Xms2g -Xmx2g"
          transport.host: 0.0.0.0
          discovery.type: single-node
          bootstrap.memory_lock: "true"
          discovery.zen.minimum_master_nodes: 1
          discovery.zen.ping.unicast.hosts: elasticsearch
        volumes:
          - ./data/elasticsearch:/usr/share/elasticsearch/data
        ports:
          - "9200:9200"
          - "9300:9300"
      kibana:
        image: kibana:6.8.10
        container_name: jetlinks-kibana
        environment:
          ELASTICSEARCH_URL: http://elasticsearch:9200
        links:
          - elasticsearch:elasticsearch
        ports:
          - "5601:5601"
        depends_on:
          - elasticsearch
      postgres:
        image: postgres:11-alpine
        container_name: jetlinks-postgres
        ports:
          - "5432:5432"
        volumes:
          - "./data/postgres:/var/lib/postgresql/data"
        environment:
          POSTGRES_PASSWORD: jetlinks
          POSTGRES_DB: jetlinks
          TZ: Asia/Shanghai
      ui:
        image: registry.cn-shenzhen.aliyuncs.com/jetlinks/jetlinks-ui-antd:1.4.0
        container_name: jetlinks-pro-ui
        ports:
          - 9000:80
        environment:
          - "API_BASE_PATH=http://jetlinks:8844/" #API根路径
        volumes:
          - "./data/upload:/usr/share/nginx/html/upload"
        depends_on:
          - jetlinks
      jetlinks:
        image: registry.cn-shenzhen.aliyuncs.com/jetlinks-pro/jetlinks-standalone:1.4.0-SNAPSHOT
        container_name: jetlinks-pro
        ports:
          - 8844:8844 # API端口
          - 1883:1883 # MQTT端口
          - 8100-8110:8100-8110 # 预留端口
          - 8200-8210:8200-8210/udp # udp端口
        volumes:
          - "./data/upload:/static/upload"  # 持久化上传的文件
        environment:
            #- "JAVA_OPTS=-Xms4g -Xmx10g -XX:+UseG1GC"  # jvm参数，根据情况调整
            - "hsweb.file.upload.static-location=http://127.0.0.1:9000/upload"  #上传的静态文件访问根地址,为宿主机的IP或者域名。需要前后端都能访问。
            - "spring.r2dbc.url=r2dbc:postgresql://postgres:5432/jetlinks" #数据库连接地址
            - "spring.r2dbc.username=postgres"
            - "spring.r2dbc.password=jetlinks"
            - "elasticsearch.client.host=elasticsearch"
            - "elasticsearch.client.post=9200"
            - "device.message.writer.elastic.enabled=false"
            - "spring.redis.host=redis"
            - "spring.redis.port=6379"
    #        - "logging.level.io.r2dbc=warn"
    #        - "logging.level.org.springframework.data=warn"
    #        - "logging.level.org.springframework=warn"
    #        - "logging.level.org.jetlinks=warn"
    #        - "logging.level.org.hswebframework=warn"
    #        - "logging.level.org.springframework.data.r2dbc.connectionfactory=warn"
        links:
            - redis:redis
            - postgres:postgres
            - elasticsearch:elasticsearch
        depends_on:
          - elasticsearch
          - postgres
          - redis
    ```
4. 执行docker-compose。  

    在docker-compose.yml所在目录下执行docker-compose启动命令。  
    
    ```shell script
    docker-compose up -d
    ``` 

5. 启动完成后访问系统。  

    `http://宿主机IP:9000`，用户名：admin， 密码： amdin。
