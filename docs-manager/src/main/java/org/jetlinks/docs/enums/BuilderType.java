package org.jetlinks.docs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档生成类型.
 *
 * @author zhangji 2023/2/22
 */
@Getter
@AllArgsConstructor
public enum BuilderType {
    PULL_REQUEST_SCOPE("生成PR的文档-根据功能分组"),
    PULL_REQUEST_TYPE("生成PR的文档-根据类型分组");

    private final String text;

}
