package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GraphQlResponse<T> {
    private T payload;
}
