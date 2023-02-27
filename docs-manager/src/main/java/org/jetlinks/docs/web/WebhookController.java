package org.jetlinks.docs.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.docs.service.DocsProperties;
import org.jetlinks.docs.service.WebhookService;
import org.jetlinks.docs.utils.SecretUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * webhook控制器.
 *
 * @author zhangji 2023/2/21
 */
@RestController
@RequestMapping("/docs/webhook")
@AllArgsConstructor
@Slf4j
public class WebhookController {

    private final DocsProperties properties;

    private final WebhookService service;

    /**
     * 接收webhook的请求
     *
     * @param secret
     * @param payloadMono
     * @return
     */
    @PostMapping("/pull/request")
    public Mono<ResponseEntity<String>> pullRequest(@RequestHeader("X-Hub-Signature-256") String secret,
                                                    @RequestBody Mono<String> payloadMono) {
        // TODO: 2023/2/24 验证secret
        return payloadMono
                .filter(payload -> {
                    String parsedSecret = SecretUtils.parseSecret(properties.getWebhookToken(), payload.getBytes(StandardCharsets.UTF_8));
                    return parsedSecret.equals(secret);
                })
                .flatMap(service::savePullRequest)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no available secret")))
                .onErrorResume(err -> {
                    log.error("webhooks error.pull request from github docs not saved", err);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err.getMessage()));
                });
    }

}
