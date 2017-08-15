package me.postaddict.instagram.scraper;

import com.google.gson.Gson;
import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.domain.Comment;
import me.postaddict.instagram.scraper.domain.Media;
import me.postaddict.instagram.scraper.domain.Tag;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext(); ) {
            Cookie cookie = iterator.next();
            if (!cookie.name().equals("csrftoken")) {
                iterator.remove();
            }
        }
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
        String jsonString = response.body().string();
        response.body().close();

        Map userJson = gson.fromJson(jsonString, Map.class);
        String shortCode = (String) ((Map)((Map)((List)((Map)((Map)((Map)userJson.get("data")).get("user")).get("edge_owner_to_timeline_media")).get("edges")).get(0)).get("node")).get("shortcode");
        Media m = getMediaByCode(shortCode);
        return m.owner;
    }

    public Account getAccountByUsername(String username) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByUsername(username))
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String jsonString = response.body().string();
        response.body().close();

        Map userJson = gson.fromJson(jsonString, Map.class);
        return Account.fromAccountPage((Map) userJson.get("user"));
    }

    public List<Media> getMedias(String username, int count) throws IOException {
        int index = 0;
        ArrayList<Media> medias = new ArrayList<Media>();
        String maxId = "";
        boolean isMoreAvailable = true;

        while (index < count && isMoreAvailable) {
            Request request = new Request.Builder()
                    .url(Endpoint.getAccountMediasJsonLink(username, maxId))
                    .build();

            Response response = this.httpClient.newCall(request).execute();
            String jsonString = response.body().string();
            response.body().close();

            Map mediasMap = gson.fromJson(jsonString, Map.class);
            List items = (List) mediasMap.get("items");

            for (Object item : items) {
                if (index == count) {
                    return medias;
                }
                index++;
                Map mediaMap = (Map) item;
                Media media = Media.fromApi(mediaMap);
                medias.add(media);
                maxId = media.id;
            }
            isMoreAvailable = (Boolean) mediasMap.get("more_available");
        }
        return medias;
    }

    public Media getMediaByUrl(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url + "/?__a=1")
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String jsonString = response.body().string();
        response.body().close();

        Map pageMap = gson.fromJson(jsonString, Map.class);
        return Media.fromMediaPage((Map) ((Map) pageMap.get("graphql")).get("shortcode_media"));
    }

    public Media getMediaByCode(String code) throws IOException {
        return getMediaByUrl(Endpoint.getMediaPageLinkByCode(code));
    }

    public Tag getTagByName(String tagName) throws IOException {
        Request request = new Request.Builder()
            .url(Endpoint.getTagJsonByTagName(tagName))
            .build();

        Response response = this.httpClient.newCall(request).execute();
        String jsonString = response.body().string();
        response.body().close();

        Map tagJson = gson.fromJson(jsonString, Map.class);
        return Tag.fromSearchPage((Map) tagJson.get("tag"));

    }

    public List<Media> getLocationMediasById(String locationId, int count) throws IOException {
        int index = 0;
        ArrayList<Media> medias = new ArrayList<Media>();
        String offset = "";
        boolean hasNext = true;

        while (index < count && hasNext) {
            Request request = new Request.Builder()
                    .url(Endpoint.getMediasJsonByLocationIdLink(locationId, offset))
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            String jsonString = response.body().string();
            response.body().close();

            Map locationMap = gson.fromJson(jsonString, Map.class);
            List nodes = (List) ((Map) ((Map) locationMap.get("location")).get("media")).get("nodes");
            for (Object node : nodes) {
                if (index == count) {
                    return medias;
                }
                index++;
                Map mediaMap = (Map) node;
                Media media = Media.fromTagPage(mediaMap);
                medias.add(media);
            }
            hasNext = (Boolean) ((Map) ((Map) ((Map) locationMap.get("location")).get("media")).get("page_info")).get("has_next_page");
            offset = (String) ((Map) ((Map) ((Map) locationMap.get("location")).get("media")).get("page_info")).get("end_cursor");
        }
        return medias;
    }

    public List<Media> getMediasByTag(String tag, int count) throws IOException {
        int index = 0;
        ArrayList<Media> medias = new ArrayList<Media>();
        String maxId = "";
        boolean hasNext = true;

        while (index < count && hasNext) {
            Request request = new Request.Builder()
                    .url(Endpoint.getMediasJsonByTagLink(tag, maxId))
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            String jsonString = response.body().string();
            response.body().close();

            Map tagMap = gson.fromJson(jsonString, Map.class);
            List nodes = (List) ((Map) ((Map) tagMap.get("tag")).get("media")).get("nodes");
            for (Object node : nodes) {
                if (index == count) {
                    return medias;
                }
                index++;
                Map mediaMap = (Map) node;
                Media media = Media.fromTagPage(mediaMap);
                medias.add(media);
            }
            hasNext = (Boolean) ((Map) ((Map) ((Map) tagMap.get("tag")).get("media")).get("page_info")).get("has_next_page");
            maxId = (String) ((Map) ((Map) ((Map) tagMap.get("tag")).get("media")).get("page_info")).get("end_cursor");
        }
        return medias;
    }

    public List<Media> getTopMediasByTag(String tag) throws IOException {
        ArrayList<Media> medias = new ArrayList<Media>();
        String maxId = "";

        Request request = new Request.Builder()
                .url(Endpoint.getMediasJsonByTagLink(tag, maxId))
                .header("Referer", Endpoint.BASE_URL + "/")
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        String jsonString = response.body().string();
        response.body().close();

        Map tagMap = gson.fromJson(jsonString, Map.class);
        List nodes = (List) ((Map) ((Map) tagMap.get("tag")).get("top_posts")).get("nodes");
        for (Object node : nodes) {
            Map mediaMap = (Map) node;
            Media media = Media.fromTagPage(mediaMap);
            medias.add(media);
        }
        return medias;
    }

    public List<Comment> getCommentsByMediaCode(String code, int count) throws IOException {
        List<Comment> comments = new ArrayList<Comment>();
        int index = 0;
        String commentId = "0";
        boolean hasNext = true;

        while (index < count && hasNext) {
            Request request = new Request.Builder()
                    .url(Endpoint.getCommentsBeforeCommentIdByCode(code, 20, commentId))
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            String jsonString = response.body().string();
            response.body().close();

            Map commentsMap = gson.fromJson(jsonString, Map.class);
            List nodes = (List) ((Map)((Map)((Map) commentsMap.get("data")).get("shortcode_media")).get("edge_media_to_comment")).get("edges");
            for (Object node : nodes) {
                if (index == count) {
                    return comments;
                }
                index++;
                Map commentMap = (Map) node;
                Comment comment = Comment.fromApi(commentMap);
                comments.add(comment);
            }
            hasNext = (Boolean) ((Map) (((Map)((Map)((Map) commentsMap.get("data")).get("shortcode_media")).get("edge_media_to_comment"))).get("page_info")).get("has_next_page");
            commentId = (String) ((Map) (((Map)((Map)((Map) commentsMap.get("data")).get("shortcode_media")).get("edge_media_to_comment"))).get("page_info")).get("end_cursor");
        }
        return comments;
    }

    public void likeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaLikeLink(Media.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }

    public void unlikeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaUnlikeLink(Media.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }

    public Comment addMediaComment(String code, String commentText) throws IOException {
        String url = Endpoint.addMediaCommentLink(Media.getIdFromCode(code));
        FormBody formBody = new FormBody.Builder()
                .add("comment_text", commentText)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        String jsonString = response.body().string();
        response.body().close();

        Map commentMap = gson.fromJson(jsonString, Map.class);
        return Comment.fromApi(commentMap);
    }

    public void deleteMediaComment(String code, String commentId) throws IOException {
        String url = Endpoint.deleteMediaCommentLink(Media.getIdFromCode(code), commentId);
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        response.body().close();
    }
}
