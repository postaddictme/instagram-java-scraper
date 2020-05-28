package me.postaddict.instagram.scraper.client;

import me.postaddict.instagram.scraper.ErrorType;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.exception.InstagramException;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.FakeBrowserInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class InstaClientFactory {
    private OkHttpClient httpClient;
    private Instagram instaClient;
    private InstaClientType intaClientType;

    public InstaClientFactory(InstaClientType intaClientType) {
        this.intaClientType = intaClientType;
    }

    public Instagram getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // TODO: 08.05.2020: Move to config
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                // TODO: 08.05.2020: Move to config and random
                .addInterceptor(new FakeBrowserInterceptor(UserAgents.OSX_CHROME))
                // TODO: 08.05.2020: Move to config
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new ErrorInterceptor());

        switch (intaClientType) {
            case STATELESS:
                break;
            case ANONYMOUS:
            case AUTHENTICATED:
                builder.cookieJar(new DefaultCookieJar(new CookieHashSet()));
                break;
            default:
                String message = String.format("Instagram client is not found:%s", intaClientType);
                throw new InstagramException(message, ErrorType.UNKNOWN_ERROR);
        }

        httpClient = builder.build();
        instaClient = new Instagram(httpClient);
        instaClient = getBasePage();
        return instaClient;
    }

    private Credentials getCredentials() {
        try {
            return new Credentials();
        } catch (IOException e) {
            String message = String.format("Can not create credentials:%n%s", e);
            throw new InstagramException(message, ErrorType.UNKNOWN_ERROR);
        }
    }

    private Instagram getBasePage() {
        try {
            instaClient.basePage();

            if (intaClientType == InstaClientType.AUTHENTICATED) {
                Credentials credentials = getCredentials();
                instaClient.login(credentials.getLogin(), credentials.getEncPassword());
                instaClient.basePage();
            }

        } catch (IOException e) {
            String message = String.format("Can not get base page data:%n%s", e);
            throw new InstagramException(message, ErrorType.UNKNOWN_ERROR);
        }
        return instaClient;
    }

    public enum InstaClientType {
        /**
         * Client without cookies
         */
        STATELESS,
        /**
         * Client with cookies, but without login
         */
        ANONYMOUS,
        /**
         * Client with login
         */
        AUTHENTICATED
    }
}
