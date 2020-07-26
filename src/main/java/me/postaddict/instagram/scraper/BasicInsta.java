package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.mapper.ModelMapper;
import me.postaddict.instagram.scraper.request.DefaultDelayHandler;
import me.postaddict.instagram.scraper.request.DelayHandler;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class BasicInsta {
    private static final Logger LOGGER = Logger.getInstance();
    protected final InstaClient instaClient;
    protected final DelayHandler delayHandler = new DefaultDelayHandler();
    protected final Mapper mapper = new ModelMapper();


    public BasicInsta(InstaClient instaClient) {
        this.instaClient = instaClient;
    }


    protected void getCSRFToken(String body) throws IOException {
        // TODO: p.saharchuk: 19.07.2020: Get JSON from '<script type="text/javascript">window._sharedData ='
        //  and pars it
        String csrf_token = getToken("\"csrf_token\":\"", 32, IOUtils.toInputStream(body, StandardCharsets.UTF_8));
        instaClient.setCsrfToken(csrf_token);
    }

    private String getCSRFToken() {
        LOGGER.debug("Get CSRF Token from cookies");
        for (Cookie cookie : instaClient.getHttpClient().cookieJar().loadForRequest(HttpUrl.parse(Endpoint.BASE_URL))) {
            if ("csrftoken".equals(cookie.name())) {
                return cookie.value();
            }
        }
        return instaClient.getCsrfToken();
    }

    protected void getRolloutHash(String body) throws IOException {
        try {
            // TODO: p.saharchuk: 19.07.2020: Get JSON from '<script type="text/javascript">window._sharedData ='
            //  and pars it
            String rollout_hash = getToken("\"rollout_hash\":\"", 12, IOUtils.toInputStream(body, StandardCharsets.UTF_8));
            instaClient.setRolloutHash(rollout_hash);
        } catch (IOException e) {
            // TODO: p.saharchuk: 19.07.2020: Check it
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
        LOGGER.debug("Request >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        LOGGER.info(request.url());
        LOGGER.debug(String.format("headers:%n%s", request.headers()));

        Response response = instaClient.getHttpClient().newCall(request).execute();
        LOGGER.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        delayHandler.onEachRequest();
        return response;
    }
}
