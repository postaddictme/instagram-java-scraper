package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.mapper.ModelMapper;
import me.postaddict.instagram.scraper.model.*;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Instagram implements AuthenticatedInsta {

    protected OkHttpClient httpClient;
    protected Mapper mapper;

    public Instagram(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        this.mapper = new ModelMapper();
    }

    public Instagram(OkHttpClient httpClient, Mapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper;
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
                .header("Referer", Endpoint.BASE_URL + "/")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }

    public Account getAccountById(long id) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByAccountId(id))
                .header("Referer", Endpoint.BASE_URL + "/")
                .build();
        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        try (ResponseBody body = response.body()){
            return getMediaByCode(mapper.getLastMediaShortCode(body.byteStream())).getOwner();
        }
    }

    public Account getAccountByUsername(String username) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByUsername(username))
                .build();

        Response response = this.httpClient.newCall(request).execute();
        try (ResponseBody body = response.body()){
            return mapper.mapAccount(body.byteStream());
        }
    }

    public PageObject<Media> getMedias(String username, int pageCount) throws IOException {
        int index = 0;
        ArrayList<Media> medias = new ArrayList<>();
        String maxId = "";
        boolean isMoreAvailable = true;

        Account firstAccount = null;
        while (index < pageCount && isMoreAvailable) {
            Request request = new Request.Builder()
                    .url(Endpoint.getAccountMediasJsonLink(username, maxId))
                    .build();

            Response response = this.httpClient.newCall(request).execute();
            Account currentAccount;
            try (ResponseBody responseBody = response.body()){
                currentAccount = mapper.mapMediaList(responseBody.byteStream());
            }

            if(firstAccount==null){
                firstAccount = currentAccount;
            } else {
                firstAccount.getMedia().getNodes().addAll(currentAccount.getMedia().getNodes());
                firstAccount.getMedia().setPageInfo(currentAccount.getMedia().getPageInfo());
            }

            index++;
            maxId = currentAccount.getMedia().getPageInfo().getEndCursor();
            isMoreAvailable = currentAccount.getMedia().getPageInfo().isHasNextPage();
        }
        return firstAccount.getMedia();
    }

    public Media getMediaByUrl(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url + "/?__a=1")
                .build();

        Response response = this.httpClient.newCall(request).execute();
        try (ResponseBody responseBody = response.body()){
            return mapper.mapMedia(responseBody.byteStream()).getPayload();
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
        int index = 0;
        String offset = "";
        boolean hasNext = true;

        Location firstLocation = null;
        while (index < pageCount && hasNext) {
            Request request = new Request.Builder()
                    .url(Endpoint.getMediasJsonByLocationIdLink(locationId, offset))
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();

            Location currentLocation;
            try (ResponseBody responseBody = response.body()){
                currentLocation = mapper.mapLocation(responseBody.byteStream());
            }

            if(firstLocation==null){
                firstLocation=currentLocation;
            } else {
                firstLocation.getMediaRating().getMedia().getNodes().addAll(currentLocation.getMediaRating().getMedia().getNodes());
                firstLocation.getMediaRating().getMedia().setPageInfo(currentLocation.getMediaRating().getMedia().getPageInfo());
            }

            index++;
            hasNext = currentLocation.getMediaRating().getMedia().getPageInfo().isHasNextPage();
            offset = currentLocation.getMediaRating().getMedia().getPageInfo().getEndCursor();
        }
        return firstLocation;
    }

    public Tag getMediasByTag(String tag, int pageCount) throws IOException {
        int index = 0;
        String maxId = "";
        boolean hasNext = true;

        Tag firstTag = null;
        while (index < pageCount && hasNext) {
            Request request = new Request.Builder()
                    .url(Endpoint.getMediasJsonByTagLink(tag, maxId))
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            Tag currentTag;
            try (ResponseBody responseBody = response.body()){
                currentTag = mapper.mapTag(responseBody.byteStream());
            }

            if(firstTag==null){
                firstTag = currentTag;
            } else {
                firstTag.getMediaRating().getMedia().getNodes().addAll(currentTag.getMediaRating().getMedia().getNodes());
                firstTag.getMediaRating().getMedia().setPageInfo(currentTag.getMediaRating().getMedia().getPageInfo());
            }

            index++;
            hasNext = currentTag.getMediaRating().getMedia().getPageInfo().isHasNextPage();
            maxId = currentTag.getMediaRating().getMedia().getPageInfo().getEndCursor();
        }
        return firstTag;
    }

    public PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException {
        int index = 0;
        String commentId = "0";
        boolean hasNext = true;

        PageObject<Comment> firstComments = null;
        while (index < pageCount && hasNext) {
            Request request = new Request.Builder()
                    .url(Endpoint.getCommentsBeforeCommentIdByCode(code, 20, commentId))
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            PageObject<Comment> currentComments;
            try (ResponseBody responseBody = response.body()){
                currentComments = mapper.mapComments(responseBody.byteStream()).getPayload();
            }

            if(firstComments==null){
                firstComments=currentComments;
            } else {
                firstComments.getNodes().addAll(currentComments.getNodes());
                firstComments.setPageInfo(currentComments.getPageInfo());
            }

            index++;
            hasNext = currentComments.getPageInfo().isHasNextPage();
            commentId = Long.toString(currentComments.getNodes().get(currentComments.getNodes().size()-1).getId());
        }
        return firstComments;
    }

    public void likeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaLikeLink(MediaUtil.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }

    public PageObject<Account> getFollows(long userId, int pageCount) throws IOException {
        int idx = 0;
        boolean hasNext = true;
        String followsLink = Endpoint.getFollowsLinkVariables(userId, 200, "");
        PageObject<Account> firstFollows = null;
        while (idx++ < pageCount && hasNext) {

            Request request = new Request.Builder()
                    .url(followsLink)
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            PageObject<Account> currentFollows;
            try (ResponseBody responseBody = response.body()){
                currentFollows = mapper.mapFollow(responseBody.byteStream()).getPayload();
            }

            if(firstFollows==null){
                firstFollows = currentFollows;
            } else {
                firstFollows.getNodes().addAll(currentFollows.getNodes());
                firstFollows.setPageInfo(currentFollows.getPageInfo());
            }

            hasNext = currentFollows.getPageInfo().isHasNextPage();
            if (hasNext) {
                followsLink = Endpoint.getFollowsLinkVariables(userId, 200, currentFollows.getPageInfo().getEndCursor());
            }
        }
        return firstFollows;
    }

    public PageObject<Account> getFollowers(long userId, int pageCount) throws IOException {
        int idx = 0;
        boolean hasNext = true;
        String followsLink = Endpoint.getFollowersLinkVariables(userId, 200, "");
        PageObject<Account> firstFollows = null;
        while (idx++ < pageCount && hasNext) {

            Request request = new Request.Builder()
                    .url(followsLink)
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            PageObject<Account> currentFollows;
            try (ResponseBody responseBody = response.body()){
                currentFollows = mapper.mapFollowers(responseBody.byteStream()).getPayload();
            }

            if(firstFollows==null){
                firstFollows = currentFollows;
            } else {
                firstFollows.getNodes().addAll(currentFollows.getNodes());
                firstFollows.setPageInfo(currentFollows.getPageInfo());
            }

            hasNext = currentFollows.getPageInfo().isHasNextPage();
            if (hasNext) {
                followsLink = Endpoint.getFollowersLinkVariables(userId, 200, currentFollows.getPageInfo().getEndCursor());
            }
        }
        return firstFollows;
    }

    public void unlikeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaUnlikeLink(MediaUtil.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
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
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
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
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }
}
