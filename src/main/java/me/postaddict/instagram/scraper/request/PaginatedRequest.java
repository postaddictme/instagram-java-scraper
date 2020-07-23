package me.postaddict.instagram.scraper.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.request.parameters.RequestParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public abstract class PaginatedRequest<R, P extends RequestParameter> {

    protected abstract Request requestInstagram(P requestParameters, PageInfo pageInfo);

    protected abstract void updateResult(R result, R current);

    protected abstract PageInfo getPageInfo(R current);

    protected abstract R mapResponse(InputStream jsonStream);

    private final InstaClient instaClient;
    @Getter(AccessLevel.PROTECTED)
    private final Mapper mapper;
    private final DelayHandler delayHandler;

    public R requestInstagramResult(P requestParameters, long pageCount, PageInfo pageCursor) throws IOException {
        R result = null;
        int currentPage = 0;
        while (currentPage++ < pageCount && pageCursor.isHasNextPage()) {

            Request request = requestInstagram(requestParameters, pageCursor);
            Response response = new Instagram(instaClient).executeHttpRequest(request);

            R current;
            try (InputStream jsonStream = response.body().byteStream()) {
                current = mapResponse(jsonStream);
            }

            if (result == null) {
                result = current;
            } else {
                updateResult(result, current);
            }

            if (currentPage < pageCount && pageCursor.isHasNextPage() && delayHandler != null) {
                delayHandler.onNextPage(currentPage, pageCount, getClass(), pageCursor);
            }

            pageCursor = getPageInfo(current);
        }
        return result;
    }
}
