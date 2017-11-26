package me.postaddict.instagram.scraper.request;

import lombok.SneakyThrows;
import me.postaddict.instagram.scraper.model.PageInfo;

import java.security.SecureRandom;

public class DefaultDelayHandler implements DelayHandler {

    private SecureRandom random = new SecureRandom();

    @SneakyThrows
    @Override
    public void onNextPage(int currentPage, int pageCount, Class<? extends PaginatedRequest> pageOperation, PageInfo pageCursor) {
        Thread.sleep(200L + random.nextInt(200));
    }
}
