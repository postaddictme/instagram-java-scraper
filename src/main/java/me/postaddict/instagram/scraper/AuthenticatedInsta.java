package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.domain.Comment;

import java.io.IOException;
import java.util.List;

public interface AuthenticatedInsta extends AnonymousInsta {

    void login(String username, String password) throws IOException;
    void likeMediaByCode(String code) throws IOException;
    void unlikeMediaByCode(String code) throws IOException;

    Comment addMediaComment(String code, String commentText) throws IOException;

    void deleteMediaComment(String code, String commentId) throws IOException;

    List<Account> getFollows(long userId, int count) throws IOException;

    List<Account> getFollowers(long userId, int count) throws IOException;
}
