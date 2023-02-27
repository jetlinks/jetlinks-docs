package org.jetlinks.docs.client;

import org.jetlinks.docs.client.content.DocsContentBuilder;
import org.jetlinks.docs.client.request.RequestCommand;
import org.jetlinks.docs.enums.BuilderType;
import org.jetlinks.docs.enums.RequestType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认请求任务管理器.
 *
 * @author zhangji 2023/2/22
 */
public class DefaultClientTaskManager implements ClientTaskManager {

    // 临时文件目录
    public static final String TEMP_DIR = "./temp";

    static {
        File folder = new File(TEMP_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private final Map<String, DocsContentBuilder> contentBuilder = new HashMap<>();
    private final Map<String, RequestCommand>     requestHandler = new HashMap<>();

    @Override
    public void register(DocsContentBuilder builder) {
        contentBuilder.put(builder.getType(), builder);
    }

    @Override
    public void register(RequestCommand handler) {
        requestHandler.put(handler.getType(), handler);
    }

    @Override
    public ClientTask getTask(RequestType requestType) {
        return getTask(requestType, requestType.getBuilderType());
    }

    @Override
    public ClientTask getTask(RequestType requestType,
                              BuilderType builderType) {
        DocsContentBuilder builder = getContentBuilder(builderType);
        RequestCommand handler = getRequestHandler(requestType);
        handler.setContentBuilder(builder);
        return new ClientTask(handler);
    }

    private DocsContentBuilder getContentBuilder(BuilderType builderType) {
        return contentBuilder.get(builderType.name());
    }

    private RequestCommand getRequestHandler(RequestType requestType) {
        return requestHandler.get(requestType.name()).clone();
    }

}
