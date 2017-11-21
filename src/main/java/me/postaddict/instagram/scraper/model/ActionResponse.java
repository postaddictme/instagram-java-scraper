package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ActionResponse<T> {
    private String status;
    private T payload;
}
