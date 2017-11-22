package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MediaRating {
    private PageObject<Media> media;
    private List<Media> topPosts;
}
