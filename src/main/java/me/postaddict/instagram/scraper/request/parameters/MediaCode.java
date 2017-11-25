package me.postaddict.instagram.scraper.request.parameters;

import lombok.Value;

@Value
public class MediaCode implements RequestParameter {
    private String shortcode;
}
