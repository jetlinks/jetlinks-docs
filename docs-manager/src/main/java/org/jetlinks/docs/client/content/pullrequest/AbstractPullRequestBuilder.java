package org.jetlinks.docs.client.content.pullrequest;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.docs.client.content.DocsContentBuilder;
import org.jetlinks.docs.entity.PullRequestInfo;
import org.jetlinks.docs.entity.PullRequestParam;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * markdown生成器.
 *
 * @author zhangji 2023/2/22
 */
@Slf4j
@Component
public abstract class AbstractPullRequestBuilder implements DocsContentBuilder {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Mono<String> buildMarkdown(PullRequestParam param,
                                      Flux<JSONArray> jsonArrayFlux) {
        return jsonArrayFlux
                .map(PullRequestInfo::of)
                .flatMapIterable(this::parsePullRequestInfo)
                .filter(markdownInfo -> {
                    LocalDateTime mergeAt = markdownInfo.getMergedAt();
                    // 过滤没有合并的PR（合并时间为空，表示未合并）
                    if (mergeAt == null) {
                        return false;
                    }
                    // 按指定时间过滤
                    boolean filter = true;
                    if (param.getMergeStart() != null) {
                        filter = param.getMergeStart().isBefore(mergeAt);
                    }
                    if (param.getMergeEnd() != null) {
                        filter = filter && param.getMergeEnd().isAfter(mergeAt);
                    }
                    return filter;
                })
                .sort(Comparator.comparing(MarkdownInfo::getMergedAt).reversed())
                .map(this::doBuildMarkdown)
                .collectList()
                .map(markdownList -> getHead() + String.join("\n", markdownList));
    }

    /**
     * 自定义标题
     *
     * @return 标题部分
     */
    protected abstract String getHead();

    /**
     * 自定义格式
     *
     * @param markdownInfo 文档信息
     * @return 文档
     */
    protected abstract String doBuildMarkdown(MarkdownInfo markdownInfo);

    private Collection<MarkdownInfo> parsePullRequestInfo(List<PullRequestInfo> pullRequestInfoList) {
        // 按名称+时间去重
        Map<String, MarkdownInfo> cache = new HashMap<>();
        for (PullRequestInfo info : pullRequestInfoList) {
            MarkdownInfo markdownInfo = MarkdownInfo.of(info);
            if (markdownInfo == null) {
                continue;
            }
            cache.compute(markdownInfo.getTitle().getDescription(), (key, old) -> {
                if (old == null) {
                    old = markdownInfo;
                } else {
                    // 相同提交信息，一天以内的PR都视为同一个分支
                    if (old.getMergedAt().isBefore(markdownInfo.getMergedAt().plusDays(1)) &&
                            old.getMergedAt().isAfter(markdownInfo.getMergedAt().minusDays(1))) {
                        // 合并分支信息
                        old.merge(markdownInfo);
                    }
                }
                return old;
            });
        }

        return cache.values();
    }

    @Getter
    @Setter
    public static class MarkdownInfo {

        private Title title;

        private PullRequestInfo.User user;

        private LocalDateTime mergedAt;

        private Map<String, List<Detail>> scopeInfo = new HashMap<>();

        private Map<String, List<Detail>> typeInfo = new HashMap<>();

        public static MarkdownInfo of(PullRequestInfo pullRequestInfo) {
            MarkdownInfo markdownInfo = new MarkdownInfo();
            Title title = Title.of(pullRequestInfo.getTitle());
            if (title == null ||
                    !StringUtils.hasText(title.getType()) ||
                    !StringUtils.hasText(title.getScope()) ||
                    !StringUtils.hasText(title.getDescription())) {
                return null;
            }

            markdownInfo.setTitle(title);
            markdownInfo.setUser(pullRequestInfo.getUser());
            markdownInfo.setMergedAt(pullRequestInfo.getMergedAt());
            Detail detail = Detail.of(pullRequestInfo).with(title);
            addDetail(markdownInfo.getScopeInfo(), detail.getScope(), Arrays.asList(detail));
            addDetail(markdownInfo.getTypeInfo(), detail.getType(), Arrays.asList(detail));

            return markdownInfo;
        }

        public MarkdownInfo merge(MarkdownInfo markdownInfo) {
            if (markdownInfo.getScopeInfo() != null) {
                markdownInfo
                        .getScopeInfo()
                        .forEach((key, value) -> addDetail(this.getScopeInfo(), key, value));
            }

            if (markdownInfo.getTypeInfo() != null) {
                markdownInfo
                        .getTypeInfo()
                        .forEach((key, value) -> addDetail(this.getTypeInfo(), key, value));
            }

            return this;
        }

        private static void addDetail(Map<String, List<Detail>> scopeInfo,
                                      String key,
                                      List<Detail> detail) {
            if (scopeInfo == null) {
                return;
            }
            scopeInfo.compute(key, (scope, detailList) -> {
                if (CollectionUtils.isEmpty(detailList)) {
                    detailList = new ArrayList<>();
                }
                detailList.addAll(detail);

                return detailList;
            });
        }
    }

    @Getter
    @Setter
    public static class Title {

        private String type;

        private String scope;

        private String description;

        /**
         * 解析PR标题
         * 格式：^(feat|fix|test|refactor|docs|style|chore|ci|revert|perf|build)\(.*\):.*$ ]]
         *
         * @param text
         * @return
         */
        public static Title of(String text) {
            try {
                Title title = new Title();
                title.setType(text.substring(0, text.indexOf("(")));
                title.setScope(text.substring(text.indexOf("(") + 1, text.indexOf("):")));
                title.setDescription(text.substring(text.indexOf("):") + 2));
                return title;
            } catch (Exception e) {
                log.warn("parse title error. text={}", text);
                return null;
            }
        }
    }

    @Getter
    @Setter
    public static class ScopeInfo {

        private String scope;

        private List<Detail> detail;
    }

    @Getter
    @Setter
    public static class TypeInfo {

        private String type;

        private List<Detail> detail;
    }

    @Getter
    @Setter
    public static class Detail {

        private String repoName;

        private String repoUrl;

        private String scope;

        private String type;

        private int number;

        private String htmlUrl;

        private String description;

        public static Detail of(PullRequestInfo pullRequestInfo) {
            Detail detail = new Detail();
            PullRequestInfo.Repo repo = pullRequestInfo.getHead().getRepo();
            detail.setRepoName(repo.getName());
            detail.setRepoUrl(repo.getHtmlUrl());
            detail.setNumber(pullRequestInfo.getNumber());
            detail.setHtmlUrl(pullRequestInfo.getHtmlUrl());
            return detail;
        }

        public Detail with(Title title) {
            setScope(title.getScope());
            setType(title.getType());
            setDescription(title.getDescription());
            return this;
        }
    }
}
