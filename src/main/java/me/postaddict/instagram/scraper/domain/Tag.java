package me.postaddict.instagram.scraper.domain;

import java.util.Map;

public class Tag {

    public int mediaCount;
    public String name;

    public static Tag fromSearchPage(Map tagMap) {
        Tag instance = new Tag();
        instance.mediaCount = ((Double) (((Map) (tagMap.get("media"))).get("count"))).intValue();
        instance.name = (String) tagMap.get("name");
        return instance;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "mediaCount=" + mediaCount +
            ", name='" + name + '\'' +
            '}';
    }
}
