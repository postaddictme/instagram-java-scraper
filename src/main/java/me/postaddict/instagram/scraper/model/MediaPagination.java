package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MediaPagination extends PageObject<Media> {
    private Integer count;
}
