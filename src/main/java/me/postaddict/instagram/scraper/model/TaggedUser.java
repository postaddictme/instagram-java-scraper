package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Embeddable;

@ToString
@Data
@Embeddable
public class TaggedUser {
    private String username;
    private Float x;
    private Float y;
}
