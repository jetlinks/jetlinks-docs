package org.jetlinks.docs.service;

import lombok.AllArgsConstructor;
import org.jetlinks.docs.client.ClientTask;
import org.jetlinks.docs.client.ClientTaskManager;
import org.jetlinks.docs.entity.PullRequestParam;
import org.jetlinks.docs.enums.BuilderType;
import org.jetlinks.docs.enums.PullRequestContentMode;
import org.jetlinks.docs.enums.RequestType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 文档服务.
 *
 * @author zhangji 2023/2/22
 */
@Service
@AllArgsConstructor
public class DocsService {

    private final ClientTaskManager clientTaskManager;

    /**
     * 查询PR并生成文档
     *
     * @param mode  文档生成模式
     * @param param 请求参数
     * @return 文档
     * @see PullRequestContentMode
     */
    public Mono<String> queryAndBuildPullRequest(String mode,
                                                 PullRequestParam param) {
        return this
                .getTask(mode)
                .flatMap(clientTask -> clientTask.apply(param));
    }

    private Mono<ClientTask> getTask(String mode) {
        return Mono
                .justOrEmpty(PullRequestContentMode.of(mode))
                .map(docMode -> {
                    switch (docMode) {
                        case SCOPE:
                            return clientTaskManager.getTask(RequestType.PULL_REQUEST, BuilderType.PULL_REQUEST_SCOPE);
                        case TYPE:
                            return clientTaskManager.getTask(RequestType.PULL_REQUEST, BuilderType.PULL_REQUEST_TYPE);
                        default:
                            return clientTaskManager.getTask(RequestType.PULL_REQUEST);
                    }
                })
                .switchIfEmpty(Mono.just(clientTaskManager.getTask(RequestType.PULL_REQUEST)));
    }
}
