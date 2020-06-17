package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class StatelessInsta extends BasicInsta {
    public StatelessInsta(InstaClient instaClient) {
        super(instaClient);
    }

    abstract void basePage() throws IOException;

    abstract Tag getMediasByTag(String tag) throws IOException;

    abstract Tag getMediasByTag(String tag, int pageCount) throws IOException;

    abstract Account getAccountByUsername(String username) throws IOException;

    /**
     * @deprecated send tree request, but should be only one
     */
    @Deprecated
    abstract Account getAccountById(long id) throws IOException;

    abstract PageObject<Media> getMediaByUserId(long userId) throws IOException;

    abstract PageObject<Media> getMediaByUserId(long userId, long mediaListSize) throws IOException;

    /**
     * @deprecated use getMediaByUserId
     */
    @Deprecated
    abstract PageObject<Media> getMedias(String username, int pageCount) throws IOException;

    /**
     * @deprecated use getMediaByUserId, because it is private method by 'pageCursor'
     */
    @Deprecated
    abstract PageObject<Media> getMedias(long userId, int pageCount, PageInfo pageCursor) throws IOException;

    abstract Media getMediaByUrl(String url) throws IOException;

    abstract Media getMediaByCode(String code) throws IOException;
}
