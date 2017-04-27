package me.postaddict.instagram.scraper.interceptor;

import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.exception.InstagramException;
import me.postaddict.instagram.scraper.exception.InstagramNotFoundException;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class ErrorInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int code = response.code();

        if (code == 200) {
            return response;
        } else {
            response.body().close();
        }

        switch (code) {
            case 401:
                throw new InstagramAuthException("Unauthorized");
            case 403:
                throw new InstagramAuthException("Access denied");
            case 404:
                throw new InstagramNotFoundException("Resource does not exist");
            default:
                throw new InstagramException("Response code is not equal 200. Something went wrong. Please report issue.");
        }
    }
}
