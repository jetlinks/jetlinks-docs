package org.jetlinks.docs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * pr文档构建模式.
 *
 * @author zhangji 2023/2/27
 */
@Getter
@AllArgsConstructor
public enum PullRequestContentMode {
    SCOPE("以功能分组"),
    TYPE("以类型分组");

    private final String text;

    public static Optional<PullRequestContentMode> of(String name) {
        return Arrays.stream(values()).filter(mode -> mode.name().equalsIgnoreCase(name)).findAny();
    }

}
