package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Embeddable;

@Data
@ToString
@Embeddable
public class DisplayResource {
    private String src;
    private Integer width;
    private Integer height;
}
