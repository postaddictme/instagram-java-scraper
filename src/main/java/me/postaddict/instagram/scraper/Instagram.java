package me.postaddict.instagram.scraper;

import lombok.AllArgsConstructor;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.mapper.ModelMapper;
import me.postaddict.instagram.scraper.model.*;
import me.postaddict.instagram.scraper.request.*;
import me.postaddict.instagram.scraper.request.parameters.*;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class Instagram implements AuthenticatedInsta {

    private static final PageInfo FIRST_PAGE = new PageInfo(true, "");
    protected final OkHttpClient httpClient;
    protected final Mapper mapper;
    protected final DelayHandler delayHandler;

    public Instagram(OkHttpClient httpClient) {
        this(httpClient, new ModelMapper(), new DefaultDelayHandler());
    }

    private Request withCsrfToken(Request request) {
        List<Cookie> cookies = httpClient.cookieJar()
                .loadForRequest(request.url());
        cookies.removeIf(cookie -> !cookie.name().equals("csrftoken"));
        if (!cookies.isEmpty()) {
            Cookie cookie = cookies.get(0);
            return request.newBuilder()
                    .addHeader("X-CSRFToken", cookie.value())
                    .build();
        }
        return request;
    }

    public void basePage() throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.BASE_URL)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        response.body().close();
    }

    public void login(String username, String password) throws IOException {
        if (username == null || password == null) {
            throw new InstagramAuthException("Specify username and password");
        }

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Endpoint.LOGIN_URL)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }

    public Account getAccountById(long id) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByAccountId(id))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        try (ResponseBody body = response.body()){
            return getMediaByCode(mapper.getLastMediaShortCode(body.byteStream())).getOwner();
        }
    }

    public Account getAccountByUsername(String username) throws IOException {
        return getAccount(username, 1, FIRST_PAGE);
    }

    public PageObject<Media> getMedias(String username, int pageCount) throws IOException {
        Account account = getAccount(username, pageCount, FIRST_PAGE);
        return account!=null? account.getMedia() : null;
    }

    public Account getAccount(String username, int pageCount, PageInfo pageCursor) throws IOException {
        GetAccountRequest getAccountRequest = new GetAccountRequest(httpClient, mapper, delayHandler);
        return getAccountRequest.requestInstagramResult(new UsernameParameter(username), pageCount, pageCursor);
    }

    public Media getMediaByUrl(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url + "/?__a=1")
                .build();

        Response response = this.httpClient.newCall(request).execute();
        try (ResponseBody responseBody = response.body()){
            return mapper.mapMedia(responseBody.byteStream());
        }
    }

    public Media getMediaByCode(String code) throws IOException {
        return getMediaByUrl(Endpoint.getMediaPageLinkByCode(code));
    }

    public Tag getTagByName(String tagName) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getTagJsonByTagName(tagName))
                .build();

        Response response = this.httpClient.newCall(request).execute();
        try (ResponseBody responseBody = response.body()){
            return mapper.mapTag(responseBody.byteStream());
        }

    }

    public Location getLocationMediasById(String locationId, int pageCount) throws IOException {
        GetLocationRequest getLocationRequest = new GetLocationRequest(httpClient, mapper, delayHandler);
        return getLocationRequest.requestInstagramResult(new LocationParameter(locationId), pageCount, FIRST_PAGE);
    }

    public Tag getMediasByTag(String tag, int pageCount) throws IOException {
        GetMediaByTagRequest getMediaByTagRequest = new GetMediaByTagRequest(httpClient, mapper, delayHandler);
        return getMediaByTagRequest.requestInstagramResult(new TagName(tag), pageCount, FIRST_PAGE);
    }

    public PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException {
        GetCommentsByMediaCode getCommentsByMediaCode = new GetCommentsByMediaCode(httpClient, mapper, delayHandler);
        return getCommentsByMediaCode.requestInstagramResult(new MediaCode(code), pageCount,
                    new PageInfo(true,"0"));
    }

    public void likeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaLikeLink(MediaUtil.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }

    public PageObject<Account> getFollows(long userId, int pageCount) throws IOException {
        GetFollowsRequest getFollowsRequest = new GetFollowsRequest(httpClient, mapper, delayHandler);
        return getFollowsRequest.requestInstagramResult(new UserParameter(userId), pageCount, FIRST_PAGE);
    }

    public PageObject<Account> getFollowers(long userId, int pageCount) throws IOException {
        GetFollowersRequest getFollowersRequest = new GetFollowersRequest(httpClient, mapper, delayHandler);
        return getFollowersRequest.requestInstagramResult(new UserParameter(userId),pageCount, FIRST_PAGE);
    }

    public void unlikeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaUnlikeLink(MediaUtil.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }

    public ActionResponse<Comment> addMediaComment(String code, String commentText) throws IOException {
        String url = Endpoint.addMediaCommentLink(MediaUtil.getIdFromCode(code));
        FormBody formBody = new FormBody.Builder()
                .add("comment_text", commentText)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        try (ResponseBody responseBody = response.body()){
            return mapper.mapMediaCommentResponse(responseBody.byteStream());
        }
    }

    public void deleteMediaComment(String code, String commentId) throws IOException {
        String url = Endpoint.deleteMediaCommentLink(MediaUtil.getIdFromCode(code), commentId);
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }
}
