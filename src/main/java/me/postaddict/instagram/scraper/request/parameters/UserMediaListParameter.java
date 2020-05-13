package me.postaddict.instagram.scraper.request.parameters;

import lombok.Value;

@Value
public class UserMediaListParameter implements RequestParameter {
    long userId;
    long pageCount;
    int lastPageSize;
}
