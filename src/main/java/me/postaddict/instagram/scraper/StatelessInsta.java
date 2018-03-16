package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.model.*;

import java.io.IOException;

public interface StatelessInsta {

    void basePage() throws IOException;
    Account getAccountByUsername(String username) throws IOException;
    PageObject<Media> getMedias(String username, int pageCount) throws IOException;
    PageObject<Media> getMedias(long userId, int pageCount, PageInfo pageCursor) throws IOException;
    Media getMediaByUrl(String url) throws IOException;
    Media getMediaByCode(String code) throws IOException;
    Tag getTagByName(String tagName) throws IOException;

}
