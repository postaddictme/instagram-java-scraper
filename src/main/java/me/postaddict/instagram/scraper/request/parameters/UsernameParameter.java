package me.postaddict.instagram.scraper.request.parameters;

import lombok.Value;

@Value
public class UsernameParameter implements RequestParameter{
    private String username;
}
