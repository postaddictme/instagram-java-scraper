package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Data
@ToString
public class MediaRating {
    private PageObject<Media> media;//media": {"nodes"
    private List<Media> topPosts;//"top_posts": {"nodes": [
}
