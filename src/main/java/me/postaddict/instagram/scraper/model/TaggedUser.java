package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@Data
@ToString
public class TaggedUser {
    private String username;//"user": {"username":
    private float x;//"x": 0.590629
    private float y;//"y": 0.604375
}
