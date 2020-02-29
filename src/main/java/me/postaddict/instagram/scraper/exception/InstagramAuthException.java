package me.postaddict.instagram.scraper.exception;

import me.postaddict.instagram.scraper.ErrorType;

public class InstagramAuthException extends InstagramException {

    public InstagramAuthException() {
    }

    public InstagramAuthException(String message) {
        super(message, ErrorType.UNKNOWN_ERROR);
    }

    public InstagramAuthException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
