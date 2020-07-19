package me.postaddict.instagram.scraper.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.OkHttpClient;


@Data
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


