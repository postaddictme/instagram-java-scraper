package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Tag {
    private String name;
    private Integer count;
    private MediaRating mediaRating;
}
