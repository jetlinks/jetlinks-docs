package org.jetlinks.docs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * api请求命令定义.
 *
 * @author zhangji 2023/2/22
 */
@Getter
@AllArgsConstructor
public enum RequestType {
    PULL_REQUEST("查询PR列表", BuilderType.PULL_REQUEST_SCOPE);

    // 类型名称
    private final String text;

    // 默认的文档生成器类型
    private final BuilderType builderType;

}
