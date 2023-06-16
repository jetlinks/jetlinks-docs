package org.jetlinks.docs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.jetlinks.docs.enums.PullRequestSort;
import org.jetlinks.docs.enums.PullRequestState;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * pr请求参数.
 *
 * @author zhangji 2023/2/24
 */
@Getter
@Setter
public class PullRequestParam {

    public static final String DEFAULT_BASE_BRANCH = "master";
    public static final String DEFAULT_OWNER       = "jetlinks-v2";

    // github 仓库的owner
    private String owner;

    // token
    private String apiToken;

    // 合并时间-开始
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mergeStart;

    // 合并时间-截止
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mergeEnd;


//    github api query参数

    /**
     * PR状态
     *
     * @see PullRequestState
     */
    private String state;

    /**
     * 来源分支
     * 通过
     * 格式：项目名:合并来源的分支名 例如：jetlinks-v2:perf
     */
    private String head;

    /**
     * 目标分支
     */
    private String base;

    /**
     * 排序
     *
     * @see PullRequestSort
     */
//    private String sort;

    /**
     * 排序顺序
     * asc 或者 desc
     */
//    private String direction;

    /**
     * 分页-每页数量
     * 默认30，最大100
     */
    private Integer perPage;

    /**
     * 分页-页码
     * 从1开始
     */
    private Integer page = 1;

    public MultiValueMap<String, String> toParam() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("state", StringUtils.hasText(state) ? state : PullRequestState.CLOSED.getText());
        param.add("head", head);
        param.add("base", StringUtils.hasText(base) ? base : DEFAULT_BASE_BRANCH);
//        param.add("sort", StringUtils.hasText(sort) ? sort : PullRequestSort.UPDATED.getText());
//        param.add("direction", StringUtils.hasText(direction) ? direction : "desc");
        param.add("per_page", perPage == null ? "50" : perPage.toString());
        param.add("page", page == null ? "1" : page.toString());

        return param;
    }


}
