package me.postaddict.instagram.scraper.cookie;

import okhttp3.Cookie;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CookieHashSet implements CookieCache {

    private Set<CookieBox> boxes;

    public CookieHashSet() {
        boxes = new HashSet<CookieBox>();
    }

    @Override
    public void addAll(Collection<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            CookieBox box = new CookieBox(cookie);
            boxes.remove(box);
            boxes.add(box);
        }
    }

    @Override
    public void clear() {
        boxes.clear();
    }

    @Override
    public Iterator<Cookie> iterator() {
        return new CookieHashSetIterator();
    }

    private class CookieHashSetIterator implements Iterator<Cookie> {

        private Iterator<CookieBox> iterator;

        CookieHashSetIterator() {
            iterator = boxes.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Cookie next() {
            return iterator.next().getCookie();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
