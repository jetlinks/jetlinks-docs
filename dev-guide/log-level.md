## 日志输出级别

#### 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        需要自定义日志的打印格式、内容、等级以及日志的保存方式和策略。
    </p>
</div>

#### 指导介绍

<p>1. <a href='/dev-guide/log-level.html#通过yaml配置文件更改'>通过yaml配置文件更改</a></p>
<p>2. <a href='/dev-guide/log-level.html#通过xml配置文件更改'>通过xml配置文件更改</a></p>

### 通过yaml配置文件更改

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        通过yaml配置文件修改只是配置控制台的日志打印。
    </p>
</div>

<p>
    在jetlinks-standalone下找到<code>application.yml</code>配置文件，指定日志输出等级即可，文件完整路径为<code>jetlinks-standalone/src/main
/resources
/application.yml</code>
</p> 

```yaml
logging:
  level:
    org.jetlinks: debug #指定平台的日志等级
    rule.engine: debug 
    org.hswebframework: debug #sql语句相关日志，如果设置为debug等级以上则不会看到sql语句的打印
    org.springframework.transaction: debug #spring事务相关日志
    org.springframework.data.r2dbc.connectionfactory: warn
    io.micrometer: warn
    org.hswebframework.expands: error
    system: debug
    org.jetlinks.rule.engine: debug #规则引擎模块相关日志
    org.jetlinks.supports.event: warn 
    org.springframework: warn #spring框架相关日志
    org.apache.kafka: warn #kafka相关日志
    org.jetlinks.pro.device.message.writer: warn
    org.jetlinks.pro.elastic.search.service: warn
    org.jetlinks.pro.elastic.search.service.reactive: warn
    org.jetlinks.pro.network: warn
    org.jetlinks.demo: warn
    io.vertx.mqtt.impl: warn
    org.jetlinks.supports.device.session: warn
    org.elasticsearch.deprecation: off
    #    org.jetlinks.supports.cluster: trace
    #    org.springframework.data.elasticsearch.client: trace
    #    org.elasticsearch: error
    org.jetlinks.pro.influx: trace
    org.elasticsearch.deprecation.search.aggregations.bucket.histogram: off
  config: classpath:logback-spring.xml
  logback:
    rollingpolicy:
      max-file-size: 100MB
```

### 通过xml配置文件更改

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    <p>
        xml配置文件中可以配置日志的打印颜色、样式、等级以及指定打印时机和持久化等。
    </p>
</div>

<p>
    在jetlinks-standalone下找到<code>logback-spring.xml</code>配置文件，通过新增或修改文件中的内容自定义日志输出，文件完整路径为<code>jetlinks-standalone/src
/main
/resources/logback-spring.xml</code>
</p> 

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <!--引入spring自带的defaults配置文件-->
    <include resource="org/springframework/boot/logging/logback/defaults.xml" />
    <!--自定义属性名为LOG_FILE 值为./logs/jetlinks-pro.log-->
    <property name="LOG_FILE" value="./logs/jetlinks-pro.log"/>

    <!--引入spring自带的console-appender配置文件-->
    <include resource="org/springframework/boot/logging/logback/console-appender.xml" />

    <!--appender可以理解为一个日志的渲染器（或者说格式化日志输出）。比如渲染console日志为某种格式，渲染文件日志为另一种格式
    appender中有name和class两个属性，有rollingPolicy和encoder两个子节点。name表示该渲染器的名字，class表示使用的输出策略，
    常见的有控制台输出策略和文件输出策略-->
    
    <!--定义一个名为LOGEventPublisher、输出策略使用org.jetlinks.pro.logging.logback.SystemLoggingAppender的日志渲染器-->
    <appender name="LOGEventPublisher" class="org.jetlinks.pro.logging.logback.SystemLoggingAppender"/>


    <!--定义一个名为ErrorLOGEventPublisher、输出策略使用org.jetlinks.pro.logging.logback.
    SystemLoggingAppender的日志渲染器，并且过滤掉WARN级别以下的日志-->
    <appender name="ErrorLOGEventPublisher" class="org.jetlinks.pro.logging.logback.SystemLoggingAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>WARN</level>
        </filter>
    </appender>

    <!--定义一个名为LOGEventPublisher、输出策略使用ch.qos.logback.core.rolling.RollingFileAppender的日志渲染器-->
    <!--文件日志渲染器-->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--编码模式-->
        <encoder>
            <!--使用引入的defaults.xml中配置的样式以及编码-->
            <pattern>${FILE_LOG_PATTERN}</pattern>
            <charset>${FILE_LOG_CHARSET}</charset>
        </encoder>
        <!--生成的日志文件名-->
        <file>${LOG_FILE}</file>
        <!-- 日志记录器的滚动策略，按日期和大小记录 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--保存的文件名格式 例如jetlinks-pro.log.2023-02-03.0.gz -->
            <fileNamePattern>${LOGBACK_ROLLINGPOLICY_FILE_NAME_PATTERN:-${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz}</fileNamePattern>
            <!--是否在启动的时候清理历史日志，默认是false-->
            <cleanHistoryOnStart>${LOGBACK_ROLLINGPOLICY_CLEAN_HISTORY_ON_START:-false}</cleanHistoryOnStart>
            <!--单个日志文件最大10M，达到设定值，则会再创建一个日志文件，日志文件名字会在末尾+1-->
            <maxFileSize>${LOGBACK_ROLLINGPOLICY_MAX_FILE_SIZE:-10MB}</maxFileSize>
            <!--所有的日志文件最大多少G，超过就会删除旧的日志,0则表示不删除-->
            <totalSizeCap>${LOGBACK_ROLLINGPOLICY_TOTAL_SIZE_CAP:-0}</totalSizeCap>
            <!--日志文件保留天数-->
            <maxHistory>${LOGBACK_ROLLINGPOLICY_MAX_HISTORY:-7}</maxHistory>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>WARN</level>
        </filter>
    </appender>

    <!--控制台使用异步打印，防止阻塞-->
    <appender name="AsyncConsoleAppender" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 队列的深度，该值会影响性能，默认 256 -->
        <queueSize>256</queueSize>
        <!-- 设为 0 表示队列达到 80%，也不丢弃任务-->
        <discardingThreshold>0</discardingThreshold>
        <!-- 日志上下文关闭后，AsyncAppender 继续执行写任务的时间，单位毫秒 -->
        <maxFlushTime>1000</maxFlushTime>
        <!-- 队列满了是否直接丢弃要写的消息，false、丢弃，true、不丢弃 -->
        <neverBlock>true</neverBlock>
        <!--是否记录调用栈-->
        <includeCallerData>true</includeCallerData>
        <!--One and only one appender may be attached to AsyncAppender，添加多个的话后面的会被忽略-->
        <appender-ref ref="CONSOLE"/>
    </appender>

    <!--配置多环境日志输出  可以在application.properties中配置选择哪个profiles : spring.profiles.active=dev-->
    <!--最终的策略为 : 基本策略(root级) + 根据profile在启动时, logger标签中定制化package日志级别(优先级高于root级)-->
    <!--dev环境-->
    <springProfile name="dev">

        <logger name="system" level="debug">
            <appender-ref ref="LOGEventPublisher"/>
        </logger>

        <root level="INFO">
            <appender-ref ref="AsyncConsoleAppender"/>
            <appender-ref ref="ErrorLOGEventPublisher"/>
        </root>
    </springProfile>

    <!-- test环境-->
    <springProfile name="test">
        <root level="INFO">
            <appender-ref ref="AsyncConsoleAppender"/>
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>

    <!-- prod环境-->
    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="AsyncConsoleAppender"/>
            <appender-ref ref="LOGEventPublisher"/>
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>

</configuration>
```

