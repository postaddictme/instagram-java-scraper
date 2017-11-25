package me.postaddict.instagram.scraper.request.parameters;

import lombok.Value;

@Value
public class LocationParameter implements RequestParameter {
    private String locationName;
}
