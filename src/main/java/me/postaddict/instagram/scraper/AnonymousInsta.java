package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.Location;
import me.postaddict.instagram.scraper.model.PageObject;

import java.io.IOException;

public abstract class AnonymousInsta extends StatelessInsta {

    public AnonymousInsta(InstaClient instaClient) {
        super(instaClient);
    }

    abstract Location getLocationMediasById(String locationId, int pageCount) throws IOException;

    abstract PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException;

}
