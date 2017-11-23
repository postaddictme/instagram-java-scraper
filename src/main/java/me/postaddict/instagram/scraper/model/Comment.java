package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@ToString
public class Comment {
    @Id
    private Long id;
    private String text;
    private Long createdAt;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Account owner;
}
