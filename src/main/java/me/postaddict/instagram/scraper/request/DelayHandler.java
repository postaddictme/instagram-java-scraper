package me.postaddict.instagram.scraper.request;

import me.postaddict.instagram.scraper.model.PageInfo;

public interface DelayHandler {
    void onNextPage(int currentPage, int pageCount, Class<? extends PaginatedRequest> pageOperation, PageInfo pageCursor);
}
