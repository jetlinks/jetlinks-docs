package org.jetlinks.docs.client.request.pullrequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.docs.client.content.DocsContentBuilder;
import org.jetlinks.docs.client.request.RequestCommand;
import org.jetlinks.docs.entity.PullRequestParam;
import org.jetlinks.docs.enums.RequestType;
import org.jetlinks.docs.service.DocsProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.jetlinks.docs.entity.PullRequestParam.DEFAULT_OWNER;

/**
 * 查询github仓库的PR列表，然后用{@link DocsContentBuilder}生成文档.
 *
 * @author zhangji 2023/2/22
 */
@Slf4j
@Component
public class PullRequestCommand implements RequestCommand {

    public static final String API_BASE_URL = "https://api.github.com/";

    @Setter
    private DocsApiClient docsApiClient;

    @Setter
    private DocsContentBuilder contentBuilder;

    public PullRequestCommand(DocsProperties properties) {
        WebClient webClient = WebClient
                .builder()
                .baseUrl(API_BASE_URL)
                .defaultHeaders(header -> {
                    header.add("Accept", "application/vnd.github+json");
                    header.add("X-GitHub-Api-Version", properties.getApiVersion());
                })
                // 设置响应数据大小限制
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
        this.docsApiClient = new DocsApiClient(webClient, properties.getApiToken());
    }

    @Override
    public PullRequestCommand clone() {
        PullRequestCommand handler = null;
        try {
            handler = (PullRequestCommand) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("clone PullRequestHandler error: {}", e.getMessage());
        }
        return handler;
    }

    @Override
    public String getType() {
        return RequestType.PULL_REQUEST.name();
    }

    @Override
    public Mono<String> apply(PullRequestParam param) {
        return docsApiClient
                .queryRepos(param)
                .flatMap(repo -> docsApiClient
                        .queryPullRequest(repo, param)
                        .flatMap(jsonArray -> {
                            // 自动查询下一页，直到返回空数组
                            if (jsonArray.size() != 0) {
                                param.setPage(param.getPage() + 1);
                                return docsApiClient
                                        .queryPullRequest(repo, param)
                                        .map(jsonArrayNextPage -> {
                                            jsonArray.addAll(jsonArrayNextPage);
                                            return jsonArray;
                                        });
                            } else {
                                return Mono.just(jsonArray);
                            }
                        }))
                .as(flux -> contentBuilder.buildMarkdown(param, flux));
    }

    @Getter
    @Setter
    public static class RepoInfo {
        private String fullName;
    }

    @AllArgsConstructor
    public static class DocsApiClient {

        private final WebClient webClient;

        private final String token;

        /**
         * 查询pr列表
         * <p>
         * https://docs.github.com/en/rest/pulls/pulls?apiVersion=2022-11-28#list-pull-requests
         *
         * @param repo  仓库名称
         * @param param 请求参数
         * @return json
         */
        public Mono<JSONArray> queryPullRequest(String repo,
                                                PullRequestParam param) {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/repos/" + repo + "/pulls")
                            .queryParams(param.toParam())
                            .build())
                    .headers(headers -> {
                        String apiToken = StringUtils.hasText(param.getApiToken()) ? param.getApiToken() : token;
                        headers.add("Authorization", "Bearer " + apiToken);
                    })
                    .retrieve()
                    .onStatus(HttpStatus::isError, resp -> Mono
                            .error(() -> new RuntimeException(resp.statusCode().getReasonPhrase())))
                    .bodyToMono(String.class)
                    .map(JSON::parseArray);
        }

        /**
         * https://docs.github.com/en/rest/repos/repos#list-repositories-for-the-authenticated-user
         *
         * @return 查询授权的仓库
         */
        public Flux<String> queryRepos(PullRequestParam param) {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/user/repos")
                            .queryParam("type", "all")
                            .queryParam("per_page", "100")
                            .build())
                    .headers(headers -> {
                        String apiToken = StringUtils.hasText(param.getApiToken()) ? param.getApiToken() : token;
                        headers.add("Authorization", "Bearer " + apiToken);
                    })
                    .retrieve()
                    .onStatus(HttpStatus::isError, resp -> Mono
                            .error(() -> new RuntimeException(resp.statusCode().getReasonPhrase())))
                    .bodyToMono(String.class)
                    .map(JSON::parseArray)
                    .flatMapIterable(jsonArray -> jsonArray.toJavaList(RepoInfo.class))
                    .filter(repo -> matchOwner(repo, StringUtils.hasText(param.getOwner()) ? param.getOwner() : DEFAULT_OWNER))
                    .map(RepoInfo::getFullName);
        }

        private boolean matchOwner(RepoInfo repo,
                                   String owner) {
            String fullName = repo.getFullName();
            if (!StringUtils.hasText(fullName)) {
                return false;
            }
            // fullName格式：owner/repo
            String[] parts = fullName.split("/");
            return parts.length == 2 && owner.equals(parts[0]);
        }
    }
}
