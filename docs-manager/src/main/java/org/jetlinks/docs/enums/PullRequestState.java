package org.jetlinks.docs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PR状态定义.
 * <p>
 * https://docs.github.com/en/rest/pulls/pulls?apiVersion=2022-11-28#list-pull-requests
 *
 * @author zhangji 2023/2/24
 */
@Getter
@AllArgsConstructor
public enum PullRequestState {
    OPEN("open"),
    CLOSED("closed"),
    ALL("all");

    private final String text;
}
