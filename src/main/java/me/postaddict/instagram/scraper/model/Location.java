package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

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
    private MediaRating mediaRating;
}
