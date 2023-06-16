package org.jetlinks.docs.configuration;

import org.jetlinks.docs.client.ClientTaskManager;
import org.jetlinks.docs.client.DefaultClientTaskManager;
import org.jetlinks.docs.client.content.DocsContentBuilder;
import org.jetlinks.docs.client.request.RequestCommand;
import org.jetlinks.docs.service.DocsProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 输入描述.
 *
 * @author zhangji 2023/2/21
 */
@EnableConfigurationProperties(DocsProperties.class)
@Configuration
public class DocsConfiguration {

    @Bean
    ClientTaskManager clientTaskManager(ObjectProvider<DocsContentBuilder> builders,
                                        ObjectProvider<RequestCommand> handlers) {
        ClientTaskManager manager = new DefaultClientTaskManager();
        builders.forEach(manager::register);
        handlers.forEach(manager::register);
        return manager;
    }
}
