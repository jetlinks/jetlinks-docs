package org.jetlinks.docs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 输入描述.
 *
 * @author zhangji 2023/2/21
 */
@Slf4j
@Service
@AllArgsConstructor
public class WebhookService {

    public Mono<String> savePullRequest(String payload) {
        log.info("save github pull request, payload:{}", payload);
        // TODO: 2023/2/21 生成文档，按名称+时间去重
        return Mono.just("ok");
    }
}
