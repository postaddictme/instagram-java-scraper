package me.postaddict.instagram.scraper.model;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;

@ToString
@Getter
@Embeddable
public class TaggedUser {
    private String username;//"user": {"username":
    private Float x;//"x": 0.590629
    private Float y;//"y": 0.604375

    public void setUsername(String username) {
        this.username = username;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
