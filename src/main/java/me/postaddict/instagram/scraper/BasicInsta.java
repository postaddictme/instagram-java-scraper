package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.mapper.ModelMapper;
import me.postaddict.instagram.scraper.request.DefaultDelayHandler;
import me.postaddict.instagram.scraper.request.DelayHandler;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class BasicInsta {
    protected final InstaClient instaClient;
    protected final DelayHandler delayHandler = new DefaultDelayHandler();
    protected final Mapper mapper = new ModelMapper();

    public BasicInsta(InstaClient instaClient) {
        this.instaClient = instaClient;
    }


    protected void getCSRFToken(ResponseBody body) throws IOException {
        String csrf_token = getToken("\"csrf_token\":\"", 32, body.byteStream());
        instaClient.setCsrfToken(csrf_token);
    }

    private String getCSRFToken() {
        for (Cookie cookie : instaClient.getHttpClient().cookieJar().loadForRequest(HttpUrl.parse(Endpoint.BASE_URL))) {
            if ("csrftoken".equals(cookie.name())) {
                return cookie.value();
            }
        }
        return instaClient.getCsrfToken();
    }

    protected void getRolloutHash(ResponseBody body) {
        try {
            String rollout_hash = getToken("\"rollout_hash\":\"", 12, body.byteStream());
            instaClient.setRolloutHash(rollout_hash);
        } catch (IOException e) {
            instaClient.setRolloutHash("1");
        }
    }

    private String getToken(String seek, int length, InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));

        String line;
        while ((line = in.readLine()) != null) {
            int index = line.indexOf(seek);
            if (index != -1) {
                return line.substring(index + seek.length(), index + seek.length() + length);
            }
        }
        throw new NullPointerException("Couldn't find " + seek);
    }

    protected Request withCsrfToken(Request request) {
        String rollout_hash = instaClient.getRolloutHash();
        return request.newBuilder()
                .addHeader("X-CSRFToken", getCSRFToken())
                .addHeader("X-Instagram-AJAX", (rollout_hash.isEmpty() ? "1" : rollout_hash))
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("X-IG-App-ID", "936619743392459")
                .build();
    }

    public Response executeHttpRequest(Request request) throws IOException {
        String csrf_token = instaClient.getCsrfToken();
        String rollout_hash = instaClient.getRolloutHash();

        // TODO: 08.05.2020: Add LOGGER
        System.out.println(String.format("%s: %s", Utils.getCurrentTime(), request.url()));
        System.out.println(String.format("csrf_token: %s", csrf_token));
        System.out.println(String.format("rollout_hash: %s", rollout_hash));

        Response response = instaClient.getHttpClient().newCall(request).execute();
        if (delayHandler != null) {
            delayHandler.onEachRequest();
        }
        return response;
    }
}
