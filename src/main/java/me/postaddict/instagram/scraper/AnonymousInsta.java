package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.model.*;

import java.io.IOException;

public interface AnonymousInsta extends StatelessInsta {

    Location getLocationMediasById(String locationId, int pageCount) throws IOException;

    Tag getMediasByTag(String tag, int pageCount) throws IOException;

    PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException;

    Account getAccountById(long id) throws IOException;

}
