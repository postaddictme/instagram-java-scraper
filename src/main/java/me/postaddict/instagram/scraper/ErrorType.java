package me.postaddict.instagram.scraper;

public enum ErrorType {

    CHECKPOINT_REQUIRED,

    TWO_FACTOR_REQUIRED,

    UNAUTHORIZED,

    TEMPORARY_ACTION_BLOCKED,

    ACTION_BLOCKED,

    FOLLOWING_THE_MAX_LIMIT_OF_ACCOUNTS,

    RATE_LIMITED,

    INSTAGRAM_SERVER_ERROR,

    UNKNOWN_ERROR
}
