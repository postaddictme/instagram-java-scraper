package me.postaddict.instagram.scraper.exception;

import java.io.IOException;

import me.postaddict.instagram.scraper.ErrorType;

public class InstagramException extends IOException {
	
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
