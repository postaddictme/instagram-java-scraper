package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Tag {
    private String name;//"name": "corgi",
    private Integer count;//"count": 4923427,
    private MediaRating mediaRating;
}
