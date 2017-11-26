package me.postaddict.instagram.scraper.model;

import lombok.Data;

@Data
public class MediaPagination extends PageObject<Media> {
    private Integer count;
}
