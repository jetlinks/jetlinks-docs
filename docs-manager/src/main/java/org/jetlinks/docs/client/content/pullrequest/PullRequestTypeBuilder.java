package org.jetlinks.docs.client.content.pullrequest;

import lombok.extern.slf4j.Slf4j;
import org.jetlinks.docs.entity.PullRequestInfo;
import org.jetlinks.docs.enums.BuilderType;
import org.springframework.stereotype.Component;

/**
 * 生成PR文档-根据类型分组.
 *
 * @author zhangji 2023/2/22
 */
@Slf4j
@Component
public class PullRequestTypeBuilder extends AbstractPullRequestBuilder {

    @Override
    public String getType() {
        return BuilderType.PULL_REQUEST_TYPE.name();
    }

    @Override
    protected String getHead() {
        return "# 更新记录\n";
    }

    @Override
    protected String doBuildMarkdown(MarkdownInfo markdownInfo) {
        StringBuilder markdown = new StringBuilder();
        PullRequestInfo.User user = markdownInfo.getUser();
        markdown.append("- ");
        markdown.append(markdownInfo.getTitle().getDescription());
        markdown.append(" by ").append(user.getLogin());
        markdown.append(" ").append(DATE_TIME_FORMATTER.format(markdownInfo.getMergedAt()));
        markdown.append("\n");

        markdownInfo.getTypeInfo().forEach((type, detailList) -> {
            markdown.append("> #### ").append(type).append("\n");
            for (Detail detail : detailList) {
                markdown.append("> #### - [").append(detail.getRepoName()).append("]")
                        .append("(").append(detail.getRepoUrl()).append(") ")
                        .append("`").append(detail.getScope()).append("` ")
                        .append("[#").append(detail.getNumber()).append("]")
                        .append("(").append(detail.getHtmlUrl()).append(") ")
                        .append(detail.getDescription());
            }
        });
        return markdown.toString();
    }

}
