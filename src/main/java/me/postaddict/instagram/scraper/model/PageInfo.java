package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageInfo {
    private boolean hasNextPage;//has_next_page	true
    private String endCursor;//end_cursor	"J0HWgzACwAAAF0HWgy02wAAAFiYA"
}
