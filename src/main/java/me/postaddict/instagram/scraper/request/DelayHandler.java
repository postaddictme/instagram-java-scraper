package me.postaddict.instagram.scraper.request;

import me.postaddict.instagram.scraper.model.PageInfo;

public interface DelayHandler {
    void onEachRequest();

    void onNextPage(long currentPage, long pageCount, Class<? extends PaginatedRequest> pageOperation, PageInfo pageCursor);
}
