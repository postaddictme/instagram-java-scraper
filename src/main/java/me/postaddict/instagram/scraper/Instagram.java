package me.postaddict.instagram.scraper;

import com.google.gson.Gson;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.model.*;
import okhttp3.*;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Instagram implements AuthenticatedInsta {

    public OkHttpClient httpClient;
    public Gson gson;

    public Instagram(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        this.gson = new Gson();
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
        String jsonString = getResponseJson(response);
        response.body().close();

        Map userJson = gson.fromJson(jsonString, Map.class);
        String shortCode = (String) ((Map) ((Map) ((List) ((Map) ((Map) ((Map) userJson.get("data")).get("user")).get("edge_owner_to_timeline_media")).get("edges")).get(0)).get("node")).get("shortcode");
        Media m = getMediaByCode(shortCode);
        return m.getOwner();
    }

    private String getResponseJson(Response response) throws IOException {
        String json = response.body().string();
        if(Boolean.getBoolean("dumpResponse")) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            File out = new File(stackTrace[2].getMethodName() + "_" + UUID.randomUUID().toString() + ".json");
            try (FileWriter writer = new FileWriter(out)) {
                IOUtils.write(json, writer);
            }
        }
        return json;
    }

    public Account getAccountByUsername(String username) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByUsername(username))
                .build();

        Response response = this.httpClient.newCall(request).execute();
        try (ResponseBody body = response.body()){
            return ModelMapper.mapAccount(body.byteStream());
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
                currentAccount = ModelMapper.mapMediaList(responseBody.byteStream());
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
            return ModelMapper.mapMedia(responseBody.byteStream()).getPayload();
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
            return ModelMapper.mapTag(responseBody.byteStream());
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
                currentLocation = ModelMapper.mapLocation(responseBody.byteStream());
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
                currentTag = ModelMapper.mapTag(responseBody.byteStream());
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
                currentComments = ModelMapper.mapComments(responseBody.byteStream()).getPayload();
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

    public List<me.postaddict.instagram.scraper.domain.Account> getFollows(long userId, int count) throws IOException {
        boolean hasNext = true;
        List<me.postaddict.instagram.scraper.domain.Account> follows = new ArrayList<>();
        String followsLink = Endpoint.getFollowsLinkVariables(userId, 200, "");
        while (follows.size() < count && hasNext) {
            Request request = new Request.Builder()
                    .url(followsLink)
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            String jsonString = getResponseJson(response);
            response.body().close();

            Map commentsMap = gson.fromJson(jsonString, Map.class);
            Map edgeFollow = (Map) ((Map) ((Map) commentsMap.get("data")).get("user")).get("edge_follow");
            List edges = (List) edgeFollow.get("edges");
            for (Object edgeObj : edges) {
                me.postaddict.instagram.scraper.domain.Account account = account((Map) edgeObj);
                follows.add(account);
                if (count == follows.size()) {
                    return follows;
                }
            }
            boolean hasNexPage = (Boolean) ((Map) edgeFollow.get("page_info")).get("has_next_page");
            if (hasNexPage) {
                followsLink = Endpoint.getFollowsLinkVariables(userId, 200, (String) ((Map) edgeFollow.get("page_info")).get("end_cursor"));
                hasNext = true;
            } else {
                hasNext = false;
            }
        }
        return follows;
    }

    private me.postaddict.instagram.scraper.domain.Account account(Map edgeObj) {
        me.postaddict.instagram.scraper.domain.Account account = new me.postaddict.instagram.scraper.domain.Account();
        Map edgeNode = (Map) edgeObj.get("node");
        account.id = Long.valueOf((String) edgeNode.get("id"));
        account.username = (String) edgeNode.get("username");
        account.profilePicUrl = (String) edgeNode.get("profile_pic_url");
        account.isVerified = (Boolean) edgeNode.get("is_verified");
        account.fullName = (String) edgeNode.get("full_name");
        return account;
    }

    public List<me.postaddict.instagram.scraper.domain.Account> getFollowers(long userId, int count) throws IOException {
        boolean hasNext = true;
        List<me.postaddict.instagram.scraper.domain.Account> followers = new ArrayList<>();
        String followsLink = Endpoint.getFollowersLinkVariables(userId, 200, "");
        while (followers.size() < count && hasNext) {
            Request request = new Request.Builder()
                    .url(followsLink)
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            String jsonString = getResponseJson(response);
            response.body().close();

            Map commentsMap = gson.fromJson(jsonString, Map.class);
            Map edgeFollow = (Map) ((Map) ((Map) commentsMap.get("data")).get("user")).get("edge_followed_by");
            List edges = (List) edgeFollow.get("edges");
            for (Object edgeObj : edges) {
                me.postaddict.instagram.scraper.domain.Account account = account((Map) edgeObj);
                followers.add(account);
                if (count == followers.size()) {
                    return followers;
                }
            }
            boolean hasNexPage = (Boolean) ((Map) edgeFollow.get("page_info")).get("has_next_page");
            if (hasNexPage) {
                followsLink = Endpoint.getFollowersLinkVariables(userId, 200, (String) ((Map) edgeFollow.get("page_info")).get("end_cursor"));
                hasNext = true;
            } else {
                hasNext = false;
            }
        }
        return followers;
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

    public me.postaddict.instagram.scraper.domain.Comment addMediaComment(String code, String commentText) throws IOException {
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
        String jsonString = getResponseJson(response);
        response.body().close();

        Map commentMap = gson.fromJson(jsonString, Map.class);
        return me.postaddict.instagram.scraper.domain.Comment.fromApi(commentMap);
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
