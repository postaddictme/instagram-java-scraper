package me.postaddict.instagram.scraper.request;

import me.postaddict.instagram.scraper.Endpoint;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.Tag;
import me.postaddict.instagram.scraper.request.parameters.TagName;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetMediaByTagRequest extends PaginatedRequest<Tag, TagName> {

    public GetMediaByTagRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(TagName requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getMediasJsonByTagLink(requestParameters.getTag(), pageInfo.getEndCursor()))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
    }

    @Override
    protected void updateResult(Tag result, Tag current) {
        result.getMediaRating().getMedia().getNodes().addAll(current.getMediaRating().getMedia().getNodes());
        result.getMediaRating().getMedia().setPageInfo(current.getMediaRating().getMedia().getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(Tag current) {
        return current.getMediaRating().getMedia().getPageInfo();
    }

    @Override
    protected Tag mapResponse(InputStream jsonStream) {
        return getMapper().mapTag(jsonStream);
    }
}
