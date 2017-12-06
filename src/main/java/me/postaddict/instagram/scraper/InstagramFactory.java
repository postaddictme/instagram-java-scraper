package me.postaddict.instagram.scraper;

import lombok.experimental.UtilityClass;
import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import okhttp3.OkHttpClient;

import java.io.IOException;

@UtilityClass
public class InstagramFactory {

    public static Instagram getAuthenticatedInstagramClient(String login, String password, String userAgent)
            throws IOException{

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor(userAgent))
                .addInterceptor(new ErrorInterceptor())
                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                .build();
        Instagram client = new Instagram(httpClient);
        client.basePage();
        client.login(login, password);
        client.basePage();
        return client;
    }
}
