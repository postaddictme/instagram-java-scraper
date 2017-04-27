package me.postaddict.instagram.scraper.cookie;

import okhttp3.Cookie;

class CookieBox {

    private Cookie cookie;

    CookieBox(Cookie cookie) {
        this.cookie = cookie;
    }

    Cookie getCookie() {
        return cookie;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CookieBox)) return false;
        CookieBox that = (CookieBox) other;
        return that.cookie.name().equals(this.cookie.name())
                && that.cookie.domain().equals(this.cookie.domain())
                && that.cookie.path().equals(this.cookie.path())
                && that.cookie.secure() == this.cookie.secure()
                && that.cookie.hostOnly() == this.cookie.hostOnly();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + cookie.name().hashCode();
        hash = 31 * hash + cookie.domain().hashCode();
        hash = 31 * hash + cookie.path().hashCode();
        hash = 31 * hash + (cookie.secure() ? 0 : 1);
        hash = 31 * hash + (cookie.hostOnly() ? 0 : 1);
        return hash;
    }
}
