package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;
import me.postaddict.instagram.scraper.domain.Media;

import java.util.Collection;

@Data
@ToString
public class Location {
    private long id;
    private Boolean hasPublicPage;
    private String name;
    private String slug;
    private Double lat;
    private Double lng;

    private Integer count;//"media": {"count"
    private PageObject<Media> media;//media": {"nodes"
    private Collection<Media> topPosts;//top_posts
}
