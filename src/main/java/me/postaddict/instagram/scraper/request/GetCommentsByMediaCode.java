package me.postaddict.instagram.scraper.request;

import me.postaddict.instagram.scraper.Endpoint;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.request.parameters.MediaCode;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;
import java.util.List;

public class GetCommentsByMediaCode extends PaginatedRequest<PageObject<Comment>, MediaCode> {

    public GetCommentsByMediaCode(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(MediaCode requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getCommentsBeforeCommentIdByCode(
                        requestParameters.getShortcode(), 20, pageInfo.getEndCursor()))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
    }

    @Override
    protected void updateResult(PageObject<Comment> result, PageObject<Comment> current) {
        result.getNodes().addAll(current.getNodes());
        result.setPageInfo(current.getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(PageObject<Comment> current) {
        List<Comment> comments = current.getNodes();
        return new PageInfo(current.getPageInfo().isHasNextPage(),Long.toString(comments.get(comments.size()-1).getId()));
    }

    @Override
    protected PageObject<Comment> mapResponse(InputStream jsonStream) {
        return getMapper().mapComments(jsonStream);
    }
}
