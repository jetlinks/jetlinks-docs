package org.jetlinks.docs.client.content;

import com.alibaba.fastjson.JSONArray;
import org.jetlinks.docs.entity.PullRequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 文档生成器.
 *
 * @author zhangji 2023/2/22
 */
public interface DocsContentBuilder {

    /**
     * @return 类型
     */
    String getType();

    /**
     * 生成markdown文档
     *
     * @param param     请求参数
     * @param jsonArray json数据
     * @return markdown文本
     */
    Mono<String> buildMarkdown(PullRequestParam param,
                               Flux<JSONArray> jsonArray);

}
