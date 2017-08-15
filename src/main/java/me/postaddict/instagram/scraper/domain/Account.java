package me.postaddict.instagram.scraper.domain;

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
    public boolean isVerified;

    public static Account fromAccountPage(Map userJson) {
        Account instance = new Account();
        instance.username = (String) userJson.get("username");
        instance.followsCount = ((Double) (((Map) (userJson.get("follows"))).get("count"))).intValue();
        instance.followedByCount = ((Double) (((Map) (userJson.get("followed_by"))).get("count"))).intValue();
        instance.profilePicUrl = (String) userJson.get("profile_pic_url");
        instance.id = Long.parseLong((String) userJson.get("id"));
        instance.biography = (String) userJson.get("biography");
        instance.fullName = (String) userJson.get("full_name");
        instance.mediaCount = ((Double) (((Map) (userJson.get("media"))).get("count"))).intValue();
        instance.isPrivate = (Boolean) userJson.get("is_private");
        instance.externalUrl = (String) userJson.get("external_url");
        instance.isVerified = (Boolean) userJson.get("is_verified");
        return instance;
    }

    public static Account fromMediaPage(Map userMap) {
        Account instance = new Account();
        instance.id = Long.parseLong((String) userMap.get("id"));
        instance.username = (String) userMap.get("username");
        if (userMap.containsKey("profile_pic_url")) {
            instance.profilePicUrl = (String) userMap.get("profile_pic_url");
        } else if (userMap.containsKey("profile_picture")) {
            instance.profilePicUrl = (String) userMap.get("profile_picture");
        }
        instance.fullName = (String) userMap.get("full_name");
        if (userMap.containsKey("is_private")) {
            instance.isPrivate = (Boolean) userMap.get("is_private");
        }
        return instance;
    }

    public static Account fromComments(Map userMap) {
        Account instance = new Account();
        instance.id = Long.parseLong((String) userMap.get("id"));
        instance.username = (String) userMap.get("username");
        instance.profilePicUrl = (String) userMap.get("profile_picture");
        instance.fullName = (String) userMap.get("full_name");
        if(instance.fullName == null){
            instance.fullName = instance.username;
        }
        return instance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", followsCount=" + followsCount +
                ", followedByCount=" + followedByCount +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                ", id=" + id +
                ", biography='" + biography + '\'' +
                ", fullName='" + fullName + '\'' +
                ", mediaCount=" + mediaCount +
                ", isPrivate=" + isPrivate +
                ", externalUrl='" + externalUrl + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }
}
