package me.postaddict.instagram.scraper.mapper;

import me.postaddict.instagram.scraper.model.*;

import java.io.InputStream;

public interface Mapper {
    Account mapAccount(InputStream jsonStream);
    Media mapMedia(InputStream jsonStream);
    PageObject<Comment> mapComments(InputStream jsonStream);
    Location mapLocation(InputStream jsonStream);
    Tag mapTag(InputStream jsonStream);
    PageObject<Account> mapFollow(InputStream jsonStream);
    PageObject<Account> mapFollowers(InputStream jsonStream);
    ActionResponse<Comment> mapMediaCommentResponse(InputStream jsonStream);
    String getLastMediaShortCode(InputStream jsonStream);
}
