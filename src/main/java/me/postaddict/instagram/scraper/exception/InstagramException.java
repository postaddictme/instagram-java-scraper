package me.postaddict.instagram.scraper.exception;

import me.postaddict.instagram.scraper.ErrorType;

public class InstagramException extends RuntimeException {

    private ErrorType errorType;

    public InstagramException() {
    }

    public InstagramException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " : " + getErrorType();
    }
}
