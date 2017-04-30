package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.domain.Comment;
import me.postaddict.instagram.scraper.domain.Media;
import me.postaddict.instagram.scraper.domain.Tag;

/**
 * Created by vasily on 27.04.17.
 */
public class ContentCheck {
    static boolean checkMedia(Media m) {
        if (m == null) {
            System.out.println("NULL");
            return false;
        }
        if (m.shortcode != null
                && m.shortcode.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                && m.createdTime > 0
                && (m.ownerId != null  || m.owner != null)) {
            return true;
        } else {
            System.out.println(m);
            return false;
        }
    }

    static boolean checkComment(Comment c){
        if (c == null) {
            System.out.println("NULL");
            return false;
        }
        if (c.id != null
                && c.id.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                && c.createdAt > 0
                && c.text != null
                && c.text.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                && c.user != null) {
            return true;
        } else {
            System.out.println(c);
            return false;
        }
    }

    static boolean checkAccount(Account o) {
        if (o == null) {
            return false;
        }
        if (o.id > 0
                && o.username != null
                && o.username.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                ) {
            return true;
        } else {
            System.out.println(o);
            return false;
        }
    }

    static boolean checkTag(Tag t) {
        if (t == null) {
            return false;
        }
        if (t.name != null
                && t.name.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                && t.mediaCount >= 0
            ) {
            return true;
        } else {
            System.out.println(t);
            return false;
        }
    }
}
