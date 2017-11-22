package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageInfo {
    private boolean hasNextPage;
    private String endCursor;
}
