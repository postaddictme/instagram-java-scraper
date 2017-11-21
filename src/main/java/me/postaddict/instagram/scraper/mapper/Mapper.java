package me.postaddict.instagram.scraper.mapper;

import me.postaddict.instagram.scraper.model.*;

import java.io.InputStream;

public interface Mapper {
    Account mapAccount(InputStream jsonStream);
    GraphQlResponse<Media> mapMedia(InputStream jsonStream);
    GraphQlResponse<PageObject<Comment>> mapComments(InputStream jsonStream);
    Location mapLocation(InputStream jsonStream);
    Account mapMediaList(InputStream jsonStream);
    Tag mapTag(InputStream jsonStream);
    GraphQlResponse<PageObject<Account>> mapFollow(InputStream jsonStream);
    GraphQlResponse<PageObject<Account>> mapFollowers(InputStream jsonStream);
    ActionResponse<Comment> mapMediaCommentResponse(InputStream jsonStream);
    String getLastMediaShortCode(InputStream jsonStream);
}
