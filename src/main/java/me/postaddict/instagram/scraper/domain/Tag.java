package me.postaddict.instagram.scraper.domain;

import java.util.Map;

public class Tag {

    public int mediaCount;
    public String name;
    public String id;

    public static Tag fromSearchPage(Map tagMap) {
        Tag instance = new Tag();
        instance.mediaCount = (Integer) tagMap.get("media_count");
        instance.name = (String) tagMap.get("name");
        instance.id = (String) tagMap.get("id");
        return instance;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "mediaCount=" + mediaCount +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
