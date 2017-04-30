package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.domain.Media;
import me.postaddict.instagram.scraper.domain.Tag;

import java.io.IOException;
import java.util.List;

public interface StatelessInsta {

    void basePage() throws IOException;
    Account getAccountByUsername(String username) throws IOException;
    List<Media> getMedias(String username, int count) throws IOException;
    Media getMediaByUrl(String url) throws IOException;
    Media getMediaByCode(String code) throws IOException;
    Tag getTagByName(String tagName) throws IOException;

}
