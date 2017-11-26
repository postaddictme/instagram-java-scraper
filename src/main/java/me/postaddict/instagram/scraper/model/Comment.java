package me.postaddict.instagram.scraper.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Comment {
    @Id
    private Long id;
    @Column(name = "text", length = 4096)
    private String text;
    private Long createdAt;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Account owner;

    @Transient
    public Date getCreated(){
        return createdAt != null ? new Date(createdAt) : null;
    }
}
