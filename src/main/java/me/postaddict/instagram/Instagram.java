package me.postaddict.instagram;

import com.google.gson.Gson;
import me.postaddict.instagram.model.Account;
import me.postaddict.instagram.model.Media;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Instagram {
    private static final String INSTAGRAM_URL = "https://www.instagram.com/";
    private OkHttpClient httpClient;
    private Gson gson;

    public Instagram() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    private static String getJsonPayload(String pageString) {
        String[] parts = pageString.split("window._sharedData = ");
        return parts[1].split(";</script>")[0];
    }

    public Account getAccount(String username) throws IOException, InstagramException {
        Request request = new Request.Builder()
                .url(INSTAGRAM_URL + username)
                .build();
        Response response = this.httpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new InstagramException("Account with given username does not exist.");
        }
        if (response.code() != 200) {
            throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
        }
        String jsonString = getJsonPayload(response.body().string());
        Map userJson = gson.fromJson(jsonString, Map.class);
        return Account.fromAccountPage(userJson);
    }

    public List<Media> getMedias(String username, int count) throws IOException, InstagramException {
        int index = 0;
        ArrayList<Media> medias = new ArrayList<Media>();
        String maxId = "";
        boolean isMoreAvailable = true;
        while (index < count && isMoreAvailable) {
            Request request = new Request.Builder()
                    .url(INSTAGRAM_URL + username + "/media/?max_id=" + maxId)
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

    public Media getMediaByCode(String code) throws IOException, InstagramException {
        return getMediaByUrl(INSTAGRAM_URL + "p/" + code);
    }

    public Media getMediaByUrl(String url) throws IOException, InstagramException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = this.httpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new InstagramException("Media with given url does not exist.");
        }
        if (response.code() != 200) {
            throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
        }
        String jsonString = getJsonPayload(response.body().string());
        Map pageMap = gson.fromJson(jsonString, Map.class);
        
        return Media.fromMediaPage(pageMap);
    }
}
