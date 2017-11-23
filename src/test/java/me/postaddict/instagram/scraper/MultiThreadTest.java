package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import me.postaddict.instagram.scraper.model.Media;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static me.postaddict.instagram.scraper.ContentCheck.checkMedia;

/**
 * Created by vasily on 27.04.17.
 */
@Ignore
public class MultiThreadTest {
    private static AnonymousInsta client;

    @BeforeClass
    public static void setUp() throws Exception {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new UserAgentInterceptor(UserAgents.OSX_CHROME))
                .addInterceptor(new ErrorInterceptor())
                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                .build();
        client = new Instagram(httpClient);
        client.basePage();
    }

    @Test
    public void multiThreadTest() throws InterruptedException, ExecutionException {
        String requests = "trump\n" +
                "obama\n" +
                "putin\n" +
                "medvedev\n" +
                "madonna\n" +
                "hilton\n" +
                "clinton\n" +
                "ferrari\n" +
                "porche\n" +
                "toyota\n" +
                "honda\n" +
                "civic\n" +
                "boeing\n" +
                "microsoft\n" +
                "android\n" +
                "yahoo\n" +
                "yandex\n" +
                "india\n" +
                "ireland\n" +
                "police\n" +
                "school\n" +
                "usa\n" +
                "canada\n" +
                "russia\n" +
                "sweden\n" +
                "iraq\n" +
                "moscow\n" +
                "london\n" +
                "washington\n" +
                "china\n" +
                "japan";
        ExecutorService es = Executors.newFixedThreadPool(3);
        Map<String, Future<List<Media>>> futures = new HashMap();
        for (final String s : requests.split("\\n")) {
            Future<List<Media>> f = es.submit(new Callable<List<Media>>() {
                public List<Media> call() throws Exception {
                    List<Media> medias = new ArrayList<Media>();
                    try {
                        medias.addAll(client.getMediasByTag(s,1).getMediaRating().getTopPosts());
                        Thread.sleep(1000);
                        medias.addAll(client.getMediasByTag(s, 3).getMediaRating().getMedia().getNodes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return medias;
                }
            });
            futures.put(s, f);
        }

        for (Map.Entry<String, Future<List<Media>>> f : futures.entrySet()) {
            List<Media> result = f.getValue().get();
            for (Media media : result) {
                Assert.assertTrue(checkMedia(media));
            }
            Assert.assertEquals(63, result.size());
        }
    }
}
