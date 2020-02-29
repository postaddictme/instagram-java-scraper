package me.postaddict.instagram.scraper.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class FakeBrowserInterceptor implements Interceptor {

    private final String userAgent;

    public FakeBrowserInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = originalRequest.newBuilder()
                .header("User-Agent", userAgent)
                .header("Accept", "*/*")
                .header("Accept-Language", "ja,en-US;q=0.7,en;q=0.3")
                .header("DNT", "1")
                .build();
        return chain.proceed(newRequest);
    }
}
