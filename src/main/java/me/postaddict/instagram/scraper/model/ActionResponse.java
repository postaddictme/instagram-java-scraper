package me.postaddict.instagram.scraper.model;

import lombok.Data;

@Data
public class ActionResponse<T> {
    private String status;
    private T payload;
}
