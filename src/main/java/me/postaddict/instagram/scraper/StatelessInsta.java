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

    /**
     * @deprecated send tree request, but should be only one
     */
    @Deprecated
    Account getAccountById(long id) throws IOException;

    PageObject<Media> getMediaByUserId(long userId) throws IOException;

    PageObject<Media> getMediaByUserId(long userId, long mediaListSize) throws IOException;

    /**
     * @deprecated use getMediaByUserId
     */
    @Deprecated
    PageObject<Media> getMedias(String username, int pageCount) throws IOException;

    /**
     * @deprecated use getMediaByUserId, because it is private method by 'pageCursor'
     */
    @Deprecated
    PageObject<Media> getMedias(long userId, int pageCount, PageInfo pageCursor) throws IOException;

    Media getMediaByUrl(String url) throws IOException;

    Media getMediaByCode(String code) throws IOException;

}
