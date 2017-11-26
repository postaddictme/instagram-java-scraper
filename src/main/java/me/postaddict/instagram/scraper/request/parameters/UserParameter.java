package me.postaddict.instagram.scraper.request.parameters;

import lombok.Value;

@Value
public class UserParameter implements RequestParameter {
    private long userId;
}
