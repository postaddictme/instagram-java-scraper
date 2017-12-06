package me.postaddict.instagram.scraper.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Activity {
    @Id
    private String id;
    private Double timestamp;
    private Integer type;
    private ActivityType activityType;
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account user;
    @ManyToOne
    @JoinColumn(name="media_id")
    private Media media;
    private String text;
}
