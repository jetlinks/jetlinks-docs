package org.jetlinks.docs;

import org.jetlinks.docs.entity.PullRequestParam;
import org.jetlinks.docs.service.DocsService;
import org.jetlinks.docs.service.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Scanner;

import static org.jetlinks.docs.client.content.pullrequest.AbstractPullRequestBuilder.DATE_TIME_FORMATTER;

/**
 * 输入描述.
 *
 * @author zhangji 2023/2/21
 */
@SpringBootApplication(scanBasePackages = "org.jetlinks.docs")
public class JetlinksDocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetlinksDocsApplication.class);

        Scanner scanner = new Scanner(System.in);
        DocsService docsService = SpringUtils.getBean(DocsService.class);

        String command;
        do {
            command = start(docsService, scanner);
        } while (!command.equals("exit"));
    }

    private static String start(DocsService docsService,
                                Scanner scanner) {
        PullRequestParam param = new PullRequestParam();
        System.out.println("请输入PR合并起始时间（yyyy-MM-dd HH:mm:ss），回车跳过");
        String start = scanner.nextLine();
        if (StringUtils.hasText(start)) {
            param.setMergeStart(LocalDateTime.parse(start, DATE_TIME_FORMATTER));
        }

        System.out.println("请输入PR合并截止时间（yyyy-MM-dd HH:mm:ss），回车跳过");
        String end = scanner.nextLine();
        if (StringUtils.hasText(end)) {
            param.setMergeEnd(LocalDateTime.parse(end, DATE_TIME_FORMATTER));
        }

        System.out.println("请输入文档格式（scope：以功能分组；type：以类型分组），回车跳过");
        String mode = scanner.nextLine();
        docsService
                .queryAndBuildPullRequest(mode, param)
                .subscribe();

        return scanner.nextLine();
    }

}
