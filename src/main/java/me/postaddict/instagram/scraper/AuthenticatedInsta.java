package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.ActionResponse;
import me.postaddict.instagram.scraper.model.ActivityFeed;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.PageObject;

import java.io.IOException;

public abstract class AuthenticatedInsta extends AnonymousInsta {


    public AuthenticatedInsta(InstaClient instaClient) {
        super(instaClient);
    }


    abstract void login(String username, String password) throws IOException;

    abstract void likeMediaByCode(String code) throws IOException;

    abstract void unlikeMediaByCode(String code) throws IOException;

    abstract void followAccountByUsername(String username) throws IOException;

    abstract void unfollowAccountByUsername(String username) throws IOException;

    abstract void followAccount(long userId) throws IOException;

    abstract void unfollowAccount(long userId) throws IOException;

    abstract ActionResponse<Comment> addMediaComment(String code, String commentText) throws IOException;

    abstract void deleteMediaComment(String code, String commentId) throws IOException;

    abstract PageObject<Account> getMediaLikes(String shortcode, int pageCount) throws IOException;

    abstract PageObject<Account> getFollows(long userId, int pageCount) throws IOException;

    abstract PageObject<Account> getFollowers(long userId, int pageCount) throws IOException;

    abstract ActivityFeed getActivityFeed() throws IOException;

    abstract Long getLoginUserId();
}
