package me.postaddict.instagramscraper;

import com.google.gson.Gson;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.exception.InstagramNotFoundException;
import me.postaddict.instagramscraper.model.Account;
import me.postaddict.instagramscraper.model.Media;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Instagram {
    private OkHttpClient httpClient;
    private Gson gson;

    public Instagram() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    public Account getAccountByUsername(String username) throws IOException, InstagramException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByUsername(username))
                .build();
        Response response = this.httpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new InstagramNotFoundException("Account with given username does not exist.");
        }
        if (response.code() != 200) {
            throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
        }
        String jsonString = response.body().string();
        Map userJson = gson.fromJson(jsonString, Map.class);
        return Account.fromAccountPage((Map) userJson.get("user"));
    }

    public Account getAccountById(long id) throws IOException, InstagramException {
        String parameters = Endpoint.getAccountJsonInfoLinkByAccountId(id);
        String random = generateRandomString(10);
        RequestBody formBody = new FormBody.Builder()
                .add("q", parameters)
                .build();
        Request request = new Request.Builder()
                .url(Endpoint.INSTAGRAM_QUERY_URL)
                .post(formBody)
                .header("Cookie", String.format("csrftoken=%s;", random))
                .header("X-Csrftoken", random)
                .header("Referer", "https://www.instagram.com/")
                .build();
        Response response = this.httpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new InstagramNotFoundException("Account with given user id does not exist.");
        }

        if (response.code() != 200) {
            throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
        }
        String jsonString = response.body().string();
        Map userJson = gson.fromJson(jsonString, Map.class);
        return Account.fromAccountPage(userJson);
    }

    private String generateRandomString(int length) {
        char[] characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        Random random = new SecureRandom();
        int charactersLength = characters.length;
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            randomString.append(characters[random.nextInt(charactersLength)]);
        }
        return randomString.toString();
    }


    public List<Media> getMedias(String username, int count) throws IOException, InstagramException {
        int index = 0;
        ArrayList<Media> medias = new ArrayList<Media>();
        String maxId = "";
        boolean isMoreAvailable = true;
        while (index < count && isMoreAvailable) {
            Request request = new Request.Builder()
                    .url(Endpoint.getAccountMediasJsonLink(username, maxId))
                    .build();
            Response response = this.httpClient.newCall(request).execute();
            if (response.code() != 200) {
                throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
            }
            String jsonString = response.body().string();
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

    public Media getMediaByUrl(String url) throws IOException, InstagramException {
        Request request = new Request.Builder()
                .url(url + "/?__a=1")
                .build();
        Response response = this.httpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new InstagramException("Media with given url does not exist.");
        }
        if (response.code() != 200) {
            throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
        }
        String jsonString = response.body().string();
        Map pageMap = gson.fromJson(jsonString, Map.class);

        return Media.fromMediaPage((Map) pageMap.get("media"));
    }

    public Media getMediaByCode(String code) throws IOException, InstagramException {
        return getMediaByUrl(Endpoint.getMediaPageLinkByCode(code));
    }

    public List<Media> getLocationMediasById(String facebookLocationId, int quantity) throws IOException, InstagramException {
        int index = 0;
        ArrayList<Media> medias = new ArrayList<Media>();
        String offset = "";
        boolean hasNext = true;
        while(index < quantity && hasNext) {
            Request request = new Request.Builder()
                    .url(Endpoint.getMediasJsonByLocationIdLink(facebookLocationId, offset))
                    .build();
            Response response = this.httpClient.newCall(request).execute();
            if (response.code() != 200) {
                throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
            }
            String jsonString = response.body().string();
            Map locationMap = gson.fromJson(jsonString, Map.class);
            List nodes = (List) ((Map)((Map) locationMap.get("location")).get("media")).get("nodes");
            for (Object node : nodes) {
                if(index == quantity) {
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

}
