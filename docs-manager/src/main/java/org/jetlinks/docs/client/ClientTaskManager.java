package org.jetlinks.docs.client;

import org.jetlinks.docs.client.content.DocsContentBuilder;
import org.jetlinks.docs.client.request.RequestCommand;
import org.jetlinks.docs.enums.BuilderType;
import org.jetlinks.docs.enums.RequestType;

/**
 * 请求任务管理器.
 *
 * @author zhangji 2023/2/22
 */
public interface ClientTaskManager {

    /**
     * 注册文档生成器类型
     *
     * @param builder 文档生成器
     */
    void register(DocsContentBuilder builder);

    /**
     * 注册请求命令类型
     *
     * @param handler 请求命令
     */
    void register(RequestCommand handler);

    /**
     * 生成任务
     * 使用请求命令默认的文档类型
     *
     * @param requestType 请求命令类型
     * @return 任务
     */
    ClientTask getTask(RequestType requestType);

    /**
     * 生成任务
     *
     * @param requestType 请求命令类型
     * @param builderType 文档类型
     * @return 任务
     */
    ClientTask getTask(RequestType requestType,
                       BuilderType builderType);

}
