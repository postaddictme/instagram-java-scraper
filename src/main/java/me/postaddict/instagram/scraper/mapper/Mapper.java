package me.postaddict.instagram.scraper.mapper;

import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.ActionResponse;
import me.postaddict.instagram.scraper.model.ActivityFeed;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.Location;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;

import java.io.InputStream;

public interface Mapper {
    Account mapAccount(InputStream jsonStream);
    PageObject<Media> mapMedias(InputStream jsonStream);
    Media mapMedia(InputStream jsonStream);
    PageObject<Comment> mapComments(InputStream jsonStream);
    Location mapLocation(InputStream jsonStream);
    Tag mapTag(InputStream jsonStream);
    PageObject<Account> mapFollow(InputStream jsonStream);
    PageObject<Account> mapFollowers(InputStream jsonStream);
    ActionResponse<Comment> mapMediaCommentResponse(InputStream jsonStream);
    String getLastMediaShortCode(InputStream jsonStream);
    PageObject<Account> mapLikes(InputStream likesStream);
    ActivityFeed mapActivity(InputStream jsonStream);
    boolean isAuthenticated(InputStream jsonStream);
}
