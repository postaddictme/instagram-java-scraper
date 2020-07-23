package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.client.InstaClientFactory;
import me.postaddict.instagram.scraper.model.Media;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static me.postaddict.instagram.scraper.ContentCheck.checkMedia;

public class MultiThreadTest {
    private static AnonymousInsta instagram;

    @BeforeClass
    public static void setUp() {
        InstaClient instaClient = new InstaClientFactory(InstaClientFactory.InstaClientType.ANONYMOUS).getClient();
        instagram = new Instagram(instaClient);
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
                        medias.addAll(instagram.getMediasByTag(s, 1).getMediaRating().getTopPosts());
                        Thread.sleep(1000);
                        medias.addAll(instagram.getMediasByTag(s, 3).getMediaRating().getMedia().getNodes());
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
            Assert.assertTrue(result.size() > 200);
        }
    }
}
