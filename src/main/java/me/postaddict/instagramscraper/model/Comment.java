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

        try {
            instance.createdAt = (long) (0d + (Double) commentMap.get("created_at"));
        } catch (NullPointerException e) {
            instance.createdAt = new Long((String) commentMap.get("created_time"));
        }

        instance.id = (String) commentMap.get("id");

        try {
            instance.user = Account.fromAccountPage((Map) commentMap.get("user"));
        } catch (NullPointerException e) {
            instance.user = Account.fromComments((Map) commentMap.get("from"));
        }

        return instance;
    }
}
