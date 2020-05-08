package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;

import java.io.IOException;

public interface StatelessInsta {

    void basePage() throws IOException;

    Tag getMediasByTag(String tag) throws IOException;

    Tag getMediasByTag(String tag, int pageCount) throws IOException;

    Account getAccountByUsername(String username) throws IOException;

    Account getAccountById(long id) throws IOException;

    PageObject<Media> getMedias(String username, int pageCount) throws IOException;

    PageObject<Media> getMedias(long userId, int pageCount, PageInfo pageCursor) throws IOException;

    Media getMediaByUrl(String url) throws IOException;

    Media getMediaByCode(String code) throws IOException;

}
