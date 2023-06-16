package org.jetlinks.docs.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetlinks.docs.client.DefaultClientTaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文件工具类.
 *
 * @author zhangji 2023/2/24
 */
@Slf4j
public class MarkdownFileUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 生成临时文件
     *
     * @param markdown   文档
     * @param mergeStart 起始时间
     * @param mergeEnd   截止时间
     */
    public static void writeToFile(String markdown,
                                   LocalDateTime mergeStart,
                                   LocalDateTime mergeEnd) {
        try {
            String latestFilename = DefaultClientTaskManager.TEMP_DIR + File.separator + "pull_request_latest.MD";
            String startDate = mergeStart == null ? "all" : DATE_FORMATTER.format(mergeStart);
            String endDate = DATE_FORMATTER.format(mergeEnd == null ? LocalDate.now() : mergeEnd);
            String currentFilename = DefaultClientTaskManager.TEMP_DIR + File.separator + "pull_request_" + startDate + "_" + endDate + ".MD";
            // 保存最新文档
            Files.write(
                    Paths.get(latestFilename),
                    markdown.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE
            );

            // 按日期保存一份
            Files.copy(Paths.get(latestFilename), Paths.get(currentFilename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("write file error!", e);
        }
    }
}
