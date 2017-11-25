package me.postaddict.instagram.scraper.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@Data
public class Tag {
    private String name;
    private Integer count;
    @Transient
    private MediaRating mediaRating;
}
