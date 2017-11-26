package me.postaddict.instagram.scraper.model;

import lombok.Data;

import java.util.List;

@Data
public class PageObject<T> {

    private List<T> nodes;
    private Integer count;
    private PageInfo pageInfo;
}
