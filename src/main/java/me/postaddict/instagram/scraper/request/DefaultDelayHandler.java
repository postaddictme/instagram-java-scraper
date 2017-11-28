package me.postaddict.instagram.scraper.request;

import lombok.SneakyThrows;
import me.postaddict.instagram.scraper.model.PageInfo;

import java.security.SecureRandom;

public class DefaultDelayHandler implements DelayHandler {

    private ThreadLocal<Long> lastRequestTime = ThreadLocal.withInitial(System::currentTimeMillis);
    private SecureRandom random = new SecureRandom();

    @Override
    @SneakyThrows
    public void onEachRequest() {
        long currentTime = System.currentTimeMillis();
        if((currentTime - lastRequestTime.get()) < 200){
            lastRequestTime.set(currentTime);
            Thread.sleep(200L + random.nextInt(100));
        }
    }

    @Override
    @SneakyThrows
    public void onNextPage(int currentPage, int pageCount, Class<? extends PaginatedRequest> pageOperation, PageInfo pageCursor) {
        Thread.sleep(200L + random.nextInt(200));
    }
}
