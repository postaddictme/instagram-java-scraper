package me.postaddict.instagram.scraper.cookie;

import me.postaddict.instagram.scraper.Logger;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultCookieJar implements CookieJar {

    private final CookieCache cache;
    private static final Logger LOGGER = Logger.getInstance();


    public DefaultCookieJar(CookieCache cache) {
        this.cache = cache;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cache.addAll(cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie cookie = it.next();
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                it.remove();
            } else if (cookie.matches(url)) {
                cookies.add(cookie);
            }
        }
        LOGGER.debug("Cookies:\r\n" + StringUtils.join(cookies, "\r\n"));
        return cookies;
    }

}
