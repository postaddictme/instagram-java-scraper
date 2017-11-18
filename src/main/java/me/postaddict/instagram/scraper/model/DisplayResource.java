package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Embeddable;

@Data
@ToString
@Embeddable
public class DisplayResource {
    private String src;//"src": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/s640x640/sh0.08/e35/22427115_152159245386313_6593256479942246400_n.jpg",
    private Integer width;//"config_width": 640,
    private Integer height;//"config_height": 514
}
