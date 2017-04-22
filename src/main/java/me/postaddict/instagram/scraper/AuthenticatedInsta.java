package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.domain.Account;

import java.io.IOException;

public interface AuthenticatedInsta extends AnonymousInsta {

    void login(String username, String password) throws IOException;
    Account getAccountById(long id) throws IOException;
    void likeMediaByCode(String code) throws IOException;
    void unlikeMediaByCode(String code) throws IOException;

}
