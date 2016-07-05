package me.postaddict.instagramscraper.model;

import java.util.Map;

public class Tag {
    public int mediaCount;
    public String name;
    public String id;

    public Tag() {
    }

    public static Tag fromSearchPage(Map tagMap) {
        Tag instance = new Tag();
        instance.mediaCount = (Integer) tagMap.get("media_count");
        instance.name = (String) tagMap.get("name");
        instance.id = (String) tagMap.get("id");
        return instance;
    }
}
