package me.postaddict.instagram.scraper.request;

import me.postaddict.instagram.scraper.Endpoint;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.request.parameters.UserParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetMediasRequest extends PaginatedRequest<PageObject<Media>, UserParameter> {

    public GetMediasRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(UserParameter requestParameters, PageInfo pageCursor) {
        return  new Request.Builder()
                .url(Endpoint.getAccountMediasJsonLink(requestParameters.getUserId(), pageCursor.getEndCursor()))
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
