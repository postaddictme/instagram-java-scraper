package me.postaddict.instagram.scraper.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;


@Getter
@Setter
@AllArgsConstructor
public class InstaClient {

    private OkHttpClient httpClient;
    /**
     * csrf_token
     */
    private String csrfToken;
    /**
     * rollout_hash
     */
    private String rolloutHash;


    public InstaClient(OkHttpClient httpClient) {
        this(httpClient, "", "");
    }
}


