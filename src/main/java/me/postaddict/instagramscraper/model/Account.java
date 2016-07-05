package me.postaddict.instagramscraper.model;

import java.util.List;
import java.util.Map;

public class Account {
    public String username;
    public int followsCount;
    public int followedByCount;
    public String profilePicUrl;
    public long id;
    public String biography;
    public String fullName;
    public int mediaCount;
    public boolean isPrivate;
    public String externalUrl;

    public Account() {
    }

    public static Account fromAccountPage(Map userJson) {
        Map userMap = (Map) ((Map) ((List) ((Map) userJson.get("entry_data")).get("ProfilePage")).get(0)).get("user");
        Account instance = new Account();
        instance.username = (String) userMap.get("username");
        instance.followsCount = ((Double) (((Map) (userMap.get("follows"))).get("count"))).intValue();
        instance.followedByCount = ((Double) (((Map) (userMap.get("followed_by"))).get("count"))).intValue();
        instance.profilePicUrl = (String) userMap.get("profile_pic_url");
        instance.id = Long.parseLong((String) userMap.get("id"));
        instance.biography = (String) userMap.get("biography");
        instance.fullName = (String) userMap.get("full_name");
        instance.mediaCount = ((Double) (((Map) (userMap.get("media"))).get("count"))).intValue();
        instance.isPrivate = (Boolean) userMap.get("is_private");
        instance.externalUrl = (String) userMap.get("external_url");
        return instance;
    }

    public static Account fromMediaPage(Map userMap) {
        Account instance = new Account();
        instance.id = Long.parseLong((String) userMap.get("id"));
        instance.username = (String) userMap.get("username");
        instance.profilePicUrl = (String) userMap.get("profile_pic_url");
        instance.fullName = (String) userMap.get("full_name");
        instance.isPrivate = (Boolean) userMap.get("is_private");
        return instance;
    }
}
