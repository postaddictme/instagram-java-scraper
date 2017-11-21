package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.ActionResponse;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.PageObject;

import java.io.IOException;

public interface AuthenticatedInsta extends AnonymousInsta {

    void login(String username, String password) throws IOException;
    void likeMediaByCode(String code) throws IOException;
    void unlikeMediaByCode(String code) throws IOException;

    ActionResponse<Comment> addMediaComment(String code, String commentText) throws IOException;

    void deleteMediaComment(String code, String commentId) throws IOException;

    PageObject<Account> getFollows(long userId, int pageCount) throws IOException;

    PageObject<Account> getFollowers(long userId, int pageCount) throws IOException;
}
