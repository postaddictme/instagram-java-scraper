package me.postaddict.instagramscraper.exception;

import me.postaddict.instagramscraper.Instagram;

public class InstagramAuthException extends Exception {

    public InstagramAuthException(){}

    public InstagramAuthException(String message) {
        super(message);
    }
}
