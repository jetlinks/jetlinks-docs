package org.jetlinks.docs.client.request;

import org.jetlinks.docs.client.content.DocsContentBuilder;
import org.jetlinks.docs.entity.PullRequestParam;
import reactor.core.publisher.Mono;

/**
 * api请求命令.
 *
 * @author zhangji 2023/2/22
 */
public interface RequestCommand extends Cloneable {

    /**
     * @return 类型
     */
    String getType();

    /**
     * 设置文档生成器
     *
     * @param contentBuilder 文档生成器
     */
    void setContentBuilder(DocsContentBuilder contentBuilder);

    /**
     * 执行查询并生成文档
     *
     * @param param 查询参数
     * @return 文档
     */
    Mono<String> apply(PullRequestParam param);

    RequestCommand clone();

}
