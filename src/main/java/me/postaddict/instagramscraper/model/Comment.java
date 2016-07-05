package me.postaddict.instagramscraper.model;

import java.util.Map;

public class Comment {
    public String text;
    public long createdAt;
    public String id;

    public Account user;

    public Comment() {
    }

    public static Comment fromApi(Map commentMap) {
        Comment instance = new Comment();
        instance.text = (String) commentMap.get("text");
        instance.createdAt = (Long) commentMap.get("created_at");
        instance.id = (String) commentMap.get("id");
        instance.user = Account.fromAccountPage((Map) commentMap.get("user"));
        return instance;
    }
}
