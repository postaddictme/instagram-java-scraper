package me.postaddict.instagram.scraper.model;

import lombok.Data;

@Data
public class GraphQlResponse<T> {
    private T payload;
}
