package me.postaddict.instagram.scraper.model;

import lombok.Data;

@Data
public class PageInfo {
    private boolean hasNextPage;
    private String endCursor;
}
