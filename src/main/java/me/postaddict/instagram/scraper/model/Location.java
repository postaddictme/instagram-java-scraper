package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Data
@ToString
public class Location {
    @Id
    private long id;
    private Boolean hasPublicPage;
    private String name;
    private String slug;
    private Double lat;
    private Double lng;

    private Integer count;//"media": {"count"
    @Transient
    private MediaRating mediaRating;
}
