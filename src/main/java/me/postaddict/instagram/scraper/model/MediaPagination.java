package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
public class MediaPagination extends PageObject<Media> {
    private Integer count;
}
