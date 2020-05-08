package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.Location;
import me.postaddict.instagram.scraper.model.PageObject;

import java.io.IOException;

public interface AnonymousInsta extends StatelessInsta {

    Location getLocationMediasById(String locationId, int pageCount) throws IOException;
    PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException;
    Account getAccountById(long id) throws IOException;

}
