package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.domain.Comment;
import me.postaddict.instagram.scraper.domain.Media;

import java.io.IOException;
import java.util.List;

public interface AnonymousInsta extends StatelessInsta {

    List<Media> getLocationMediasById(String locationId, int count) throws IOException;

    List<Media> getMediasByTag(String tag, int count) throws IOException;

    List<Media> getTopMediasByTag(String tag) throws IOException;
    List<Comment> getCommentsByMediaCode(String code, int count) throws IOException;
    Account getAccountById(long id) throws IOException;

}
