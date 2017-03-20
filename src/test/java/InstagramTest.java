import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Account;
import me.postaddict.instagramscraper.model.Comment;
import me.postaddict.instagramscraper.model.Media;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.*;

public class InstagramTest {
    private static Instagram instagram;

    @BeforeClass
    public static void setUp() throws Exception {
        instagram = new Instagram();
        instagram.withCredentials("PASTE_YOUR_USERNAME", "PASTE_YOUR_PASSWORD");
        instagram.login();
    }

    @Test
    public void testGetAccountByUsername() throws IOException, InstagramException {
        Account account = instagram.getAccountByUsername("kevin");
        assertEquals("kevin", account.username);
    }

    @Test
    public void testGetAccountById() throws IOException, InstagramException {
        Account account = instagram.getAccountById(3);
        assertEquals("kevin", account.username);
    }

    @Test
    public void testGetMedias() throws IOException, InstagramException {
        List<Media> mediaList = instagram.getMedias("kevin", 50);
        assertEquals(50, mediaList.size());
    }

    @Test
    public void testGetMediaByUrl() throws IOException, InstagramException {
        Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
    }

    @Test
    public void testGetMediaByCode() throws IOException, InstagramException {
        Media media = instagram.getMediaByCode("BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
    }

    @Test
    public void testGetLocationMediasById() throws IOException, InstagramException {
        List<Media> list = instagram.getLocationMediasById("17326249", 13);
        assertEquals(13, list.size());
    }

    @Test
    public void testGetMediasByTag() throws Exception {
        List<Media> list = instagram.getMediasByTag("Moscow", 50);
        assertEquals(50, list.size());
    }

    @Test
    public void testGetTopMediasByTag() throws Exception {
        List<Media> list = instagram.getTopMediasByTag("Sheremetyevo");
        assertEquals(9, list.size());
    }

    @Test
    public void testGetCommentsByMediaCode() throws Exception {
        List<Comment> list = instagram.getCommentsByMediaCode("BHaRdodBouH", 50);
        assertEquals(50, list.size());
    }

    @Test
    public void testGetIdFromCode() throws Exception {
        String code = Media.getCodeFromId("1270593720437182847");
        assertEquals("BGiDkHAgBF_", code);
        code = Media.getCodeFromId("1270593720437182847_3");
        assertEquals("BGiDkHAgBF_", code);
    }

    @Test
    public void testGetCodeFromId() throws Exception {
        String id = Media.getIdFromCode("BGiDkHAgBF_");
        assertEquals("1270593720437182847", id);
    }

    @Test
    public void testPreviewComments() throws Exception {
        Media media = instagram.getMedias("kevin", 1).get(0);
        if (media.commentsCount > 0){
            assertTrue(media.previewCommentsList.size() > 0);
        } else {
            assertFalse(media.previewCommentsList.size() > 0);
        }
    }

    @Test
    public void authMethodsMultiThreadTest() throws InterruptedException {
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
                "google\n" +
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
        ExecutorService es = Executors.newFixedThreadPool(5);
        final Map<String, List<Media>> results = new HashMap();
        for (final String s : requests.split("\\n")) {
            es.execute(new Runnable() {
                public void run() {
                    try {
                        List<Media> medias = new ArrayList<Media>();
                        medias.addAll(instagram.getTopMediasByTag(s));
                        Thread.sleep(1000);
                        medias.addAll(instagram.getMediasByTag(s, 50));
                        results.put(s, medias);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InstagramException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        while (((ThreadPoolExecutor) es).getActiveCount() > 0){
            Thread.sleep(3000);
        }
        for (List<Media> medias : results.values()) {
            Assert.assertEquals(59, medias.size());
        }
    }
}
