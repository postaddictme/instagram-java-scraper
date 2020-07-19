package me.postaddict.instagram.scraper.request;

import lombok.SneakyThrows;
import me.postaddict.instagram.scraper.model.PageInfo;

import java.security.SecureRandom;

public class DefaultDelayHandler implements DelayHandler {
    private final long MIN_PAUSE = 500L;
    private final int MAX_DELTA = 250;
    private ThreadLocal<Long> lastRequestTime = ThreadLocal.withInitial(System::currentTimeMillis);
    private SecureRandom random = new SecureRandom();

    @Override
    @SneakyThrows
    public void onEachRequest() {
        long currentTime = System.currentTimeMillis();

        if((currentTime - lastRequestTime.get()) < MIN_PAUSE){
            lastRequestTime.set(currentTime);
            Thread.sleep(MIN_PAUSE + random.nextInt(MAX_DELTA));
        }
    }

    @Override
    @SneakyThrows
    public void onNextPage(long currentPage, long pageCount, Class<? extends PaginatedRequest> pageOperation, PageInfo pageCursor) {
        Thread.sleep(MIN_PAUSE + random.nextInt(MAX_DELTA));
    }
}
