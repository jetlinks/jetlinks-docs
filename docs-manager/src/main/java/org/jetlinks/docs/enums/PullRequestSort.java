package org.jetlinks.docs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 查询排序定义
 * <p>
 * https://docs.github.com/en/rest/pulls/pulls?apiVersion=2022-11-28#list-pull-requests.
 *
 * @author zhangji 2023/2/24
 */
@Getter
@AllArgsConstructor
public enum PullRequestSort {
    CREATED("created"),
    UPDATED("updated"),
    POPULARITY("popularity"),
    LONG_RUNNING("long-running");

    private final String text;
}
