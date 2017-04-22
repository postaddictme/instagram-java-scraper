package me.postaddict.instagram.scraper;

import com.google.gson.Gson;
import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.domain.Comment;
import me.postaddict.instagram.scraper.domain.Media;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Instagram implements AuthenticatedInsta {

    private OkHttpClient httpClient;
    private Gson gson;

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
        RequestBody formBody = new FormBody.Builder()
                .add("q", Endpoint.getAccountJsonInfoLinkByAccountId(id))
                .build();

        Request request = new Request.Builder()
                .url(Endpoint.INSTAGRAM_QUERY_URL)
                .header("Referer", Endpoint.BASE_URL + "/")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
        String jsonString = response.body().string();
        response.body().close();

        Map userJson = gson.fromJson(jsonString, Map.class);
        return Account.fromAccountPage(userJson);
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
            RequestBody formBody = new FormBody.Builder()
                    .add("q", Endpoint.getCommentsBeforeCommentIdByCode(code, 20, commentId))
                    .build();
            Request request = new Request.Builder()
                    .url(Endpoint.INSTAGRAM_QUERY_URL)
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .post(formBody)
                    .build();

            Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
            String jsonString = response.body().string();
            response.body().close();

            Map commentsMap = gson.fromJson(jsonString, Map.class);
            List nodes = (List) ((Map) commentsMap.get("comments")).get("nodes");
            for (Object node : nodes) {
                if (index == count) {
                    return comments;
                }
                index++;
                Map commentMap = (Map) node;
                Comment comment = Comment.fromApi(commentMap);
                comments.add(comment);
            }
            hasNext = (Boolean) ((Map) ((Map) commentsMap.get("comments")).get("page_info")).get("has_previous_page");
            commentId = (String) ((Map) ((Map) commentsMap.get("comments")).get("page_info")).get("start_cursor");
        }
        return comments;
    }
}
