package org.jetlinks.docs.service;

import com.alibaba.fastjson.JSON;
import org.jetlinks.docs.client.ClientTaskManager;
import org.jetlinks.docs.client.DefaultClientTaskManager;
import org.jetlinks.docs.client.content.pullrequest.PullRequestScopeBuilder;
import org.jetlinks.docs.client.content.pullrequest.PullRequestTypeBuilder;
import org.jetlinks.docs.client.request.pullrequest.PullRequestCommand;
import org.jetlinks.docs.entity.PullRequestParam;
import org.jetlinks.docs.enums.PullRequestContentMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 输入描述.
 *
 * @author zhangji 2023/2/27
 */
public class DocsServiceTest {

    private final String apiToken   = "test";
    private final String apiVersion = "test";

    private DocsService service;

    @BeforeEach
    void init() throws IOException {
        ClientTaskManager manager = new DefaultClientTaskManager();

        DocsProperties properties = new DocsProperties();
        properties.setApiToken(apiToken);
        properties.setApiVersion(apiVersion);
        PullRequestCommand command = new PullRequestCommand(properties);

        PullRequestCommand.DocsApiClient client = Mockito.mock(PullRequestCommand.DocsApiClient.class);
        Mockito.when(client.queryRepos(Mockito.any(PullRequestParam.class))).thenReturn(Flux.just("test"));
        InputStream inputStream = new ClassPathResource("pull-request-response.json").getInputStream();
        String json = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        Mockito.when(client.queryPullRequest(Mockito.anyString(), Mockito.any(PullRequestParam.class)))
                .thenReturn(Mono.just(JSON.parseArray(json)));

        command.setDocsApiClient(client);
        manager.register(command);
        manager.register(new PullRequestScopeBuilder());
        manager.register(new PullRequestTypeBuilder());

        service = new DocsService(manager);
    }

    @Test
    void test() {
        PullRequestParam param = new PullRequestParam();
        service
                .queryAndBuildPullRequest(PullRequestContentMode.SCOPE.name(), param)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        service
                .queryAndBuildPullRequest(PullRequestContentMode.TYPE.name(), param)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }


    private WebClient getRepo() throws IOException {
        return getWebClient("repos.json");
    }

    private WebClient getPr() throws IOException {
        return getWebClient("pull-request-response.json");
    }

    private WebClient getWebClient(String jsonPath) throws IOException {
        WebClient webClient = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uri = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec resp = Mockito.mock(WebClient.ResponseSpec.class);
        Mockito.when(webClient.get()).thenReturn(uri);
        Mockito.when(uri.uri(Mockito.any(Function.class))).thenReturn(uri);
        Mockito.when(uri.headers(Mockito.any(Consumer.class))).thenReturn(uri);
        Mockito.when(uri.retrieve()).thenReturn(resp);
        Mockito.when(resp.onStatus(Mockito.any(Predicate.class), Mockito.any(Function.class))).thenReturn(resp);
        InputStream inputStream = new ClassPathResource(jsonPath).getInputStream();
        String repos = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        Mockito.when(resp.bodyToMono(Mockito.any(Class.class))).thenReturn(Mono.just(repos));
        return webClient;
    }
}
