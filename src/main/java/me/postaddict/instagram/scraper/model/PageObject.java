package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PageObject<T> {

    private List<T> nodes;
    private Integer count;
    private PageInfo pageInfo;
}
