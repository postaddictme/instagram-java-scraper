package me.postaddict.instagram.scraper.cookie;

import okhttp3.Cookie;

import java.util.Collection;

public interface CookieCache extends Iterable<Cookie> {

    void addAll(Collection<Cookie> cookies);
    void clear();

}
