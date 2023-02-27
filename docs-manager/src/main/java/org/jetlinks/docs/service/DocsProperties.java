package org.jetlinks.docs.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文档配置信息.
 *
 * @author zhangji 2023/2/21
 */
@ConfigurationProperties(prefix = "jetlinks.docs.github")
@Getter
@Setter
public class DocsProperties {

    // webhook的token
    private String webhookToken;

    // api的token
    private String apiToken;

    // api版本
    private String apiVersion;

}
