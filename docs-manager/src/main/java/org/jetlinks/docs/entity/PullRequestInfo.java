package org.jetlinks.docs.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * pr信息.
 *
 * @author zhangji 2023/2/22
 */
@Getter
@Setter
public class PullRequestInfo {

    private String id;

    private String url;

    private String htmlUrl;

    private int number;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime closedAt;

    private LocalDateTime mergedAt;

    private Head head;

    private User user;

    public static List<PullRequestInfo> of(JSONArray jsonArray) {
        return jsonArray.toJavaList(PullRequestInfo.class);
    }


    @Getter
    @Setter
    public static class Head {

        private Repo repo;

    }

    @Getter
    @Setter
    public static class Repo {

        private String id;

        private String name;

        private String fullName;

        private String htmlUrl;

    }

    @Getter
    @Setter
    public static class User {

        private String id;

        private String login;
    }

}
