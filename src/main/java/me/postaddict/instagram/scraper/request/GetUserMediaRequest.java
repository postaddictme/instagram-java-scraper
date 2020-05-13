package me.postaddict.instagram.scraper.request;

import me.postaddict.instagram.scraper.Endpoint;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.request.parameters.UserMediaListParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetUserMediaRequest extends PaginatedRequest<PageObject<Media>, UserMediaListParameter> {
    private long pageCount = 0;

    public GetUserMediaRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(UserMediaListParameter requestParameters, PageInfo pageCursor) {
        pageCount++;
        int mediaListSize = pageCount < requestParameters.getPageCount() ?
                Instagram.MAX_USER_MEDIA_PAGE_SIZE : requestParameters.getLastPageSize();
        return new Request.Builder()
                .url(Endpoint.getUserMediaJsonByUserId(
                        requestParameters.getUserId(),
                        mediaListSize,
                        pageCursor.getEndCursor())
                )
                .build();
    }

    @Override
    protected void updateResult(PageObject<Media> result, PageObject<Media> current) {
        result.getNodes().addAll(current.getNodes());
        result.setPageInfo(current.getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(PageObject<Media> current) {
        return current.getPageInfo();
    }

    @Override
    protected PageObject<Media> mapResponse(InputStream jsonStream) {
        return getMapper().mapMedias(jsonStream);
    }
}
