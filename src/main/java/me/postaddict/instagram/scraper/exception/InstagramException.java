package me.postaddict.instagram.scraper.exception;

import java.io.IOException;

public class InstagramException extends IOException {

    public InstagramException() {
    }

    public InstagramException(String message) {
        super(message);
    }
}
