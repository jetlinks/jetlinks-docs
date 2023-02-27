package org.jetlinks.docs.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.docs.client.request.RequestCommand;
import org.jetlinks.docs.entity.PullRequestParam;
import org.jetlinks.docs.utils.MarkdownFileUtils;
import reactor.core.publisher.Mono;

/**
 * 文档查询任务.
 *
 * @author zhangji 2023/2/22
 */
@Slf4j
@AllArgsConstructor
public class ClientTask {

    // api请求命令
    private final RequestCommand requestCommand;

    /**
     * 执行请求命令，并且生成文档
     *
     * @param param 请求参数
     * @return 文档
     */
    public Mono<String> apply(PullRequestParam param) {
        return requestCommand
                .apply(param)
                .doOnNext(markdown -> {
                    log.info(markdown);
                    // 生成临时文件
                    MarkdownFileUtils.writeToFile(markdown, param.getMergeStart(), param.getMergeEnd());
                });
    }

}
