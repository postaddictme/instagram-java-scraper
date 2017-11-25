package me.postaddict.instagram.scraper.model;

import lombok.Data;

import java.util.List;

@Data
public class MediaRating {
    private PageObject<Media> media;
    private List<Media> topPosts;
}
