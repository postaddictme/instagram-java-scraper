package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.model.*;

import java.io.IOException;

public interface AuthenticatedInsta extends AnonymousInsta {

    void login(String username, String password) throws IOException;
    void likeMediaByCode(String code) throws IOException;
    void unlikeMediaByCode(String code) throws IOException;
    void followAccountByUsername(String username) throws IOException;
    void unfollowAccountByUsername(String username) throws IOException;
    void followAccount(long userId) throws IOException;
    void unfollowAccount(long userId) throws IOException;
    
    ActionResponse<Comment> addMediaComment(String code, String commentText) throws IOException;

    void deleteMediaComment(String code, String commentId) throws IOException;

    PageObject<Account> getMediaLikes(String shortcode, int pageCount) throws IOException;

    PageObject<Account> getFollows(long userId, int pageCount) throws IOException;

    PageObject<Account> getFollowers(long userId, int pageCount) throws IOException;

    ActivityFeed getActivityFeed() throws IOException;

    Long getLoginUserId();
}
