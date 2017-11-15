package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
public class PageObject<T> {
    Collection<T> nodes;
    PageInfo pageInfo;
}
