package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;
import me.postaddict.instagram.scraper.domain.Media;

import java.util.Collection;

@Data
@ToString
public class Tag {
    private String name;//"name": "corgi",
    private Integer count;//"count": 4923427,
    private PageObject<Media> media;//media": {"nodes"
    private Collection<Media> topPosts;//top_posts
}
