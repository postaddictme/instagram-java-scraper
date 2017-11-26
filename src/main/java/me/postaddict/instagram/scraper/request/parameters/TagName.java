package me.postaddict.instagram.scraper.request.parameters;

import lombok.Value;

@Value
public class TagName implements RequestParameter {
    private String tag;
}
