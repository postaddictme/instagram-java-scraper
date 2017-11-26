package me.postaddict.instagram.scraper.request;

import me.postaddict.instagram.scraper.Endpoint;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.request.parameters.UsernameParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetAccountRequest extends PaginatedRequest<Account, UsernameParameter> {

    public GetAccountRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(UsernameParameter requestParameters, PageInfo pageCursor) {
        return  new Request.Builder()
                .url(Endpoint.getAccountMediasJsonLink(requestParameters.getUsername(), pageCursor.getEndCursor()))
                .build();
    }

    @Override
    protected void updateResult(Account result, Account current) {
        result.getMedia().getNodes().addAll(current.getMedia().getNodes());
        result.getMedia().setPageInfo(current.getMedia().getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(Account current) {
        return current.getMedia().getPageInfo();
    }

    @Override
    protected Account mapResponse(InputStream jsonStream) {
        return getMapper().mapAccount(jsonStream);
    }
}
