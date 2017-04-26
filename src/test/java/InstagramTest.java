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
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class InstagramTest {
    private static Instagram instagram;

    @BeforeClass
    public static void setUp() throws Exception {
        instagram = new Instagram();
        instagram.withCredentials("rozhnikov", "luv4us");
        instagram.login();
    }

    @Test
    public void testGetAccountByUsername() throws IOException, InstagramException {
        Account account = instagram.getAccountByUsername("kevin");
        assertTrue(checkAccount(account));
        assertEquals("kevin", account.username);
    }

    @Test
    public void testGetAccountById() throws IOException, InstagramException {
        Account account = instagram.getAccountById(3);
        assertTrue(checkAccount(account));
        assertEquals("kevin", account.username);
    }

    @Test
    public void testGetMedias() throws IOException, InstagramException {
        List<Media> list = instagram.getMedias("kevin", 50);
        assertEquals(50, list.size());
        for (Media media : list) {
            Assert.assertTrue(checkMedia(media));
        }
    }

    @Test
    public void testGetMediaByUrl() throws IOException, InstagramException {
        Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
        Assert.assertTrue(checkMedia(media));
        Assert.assertTrue(checkAccount(media.owner));
    }

    @Test
    public void testGetMediaByCode() throws IOException, InstagramException {
        Media media = instagram.getMediaByCode("BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
        Assert.assertTrue(checkMedia(media));
    }

    @Test
    public void testGetLocationMediasById() throws IOException, InstagramException {
        List<Media> list = instagram.getLocationMediasById("17326249", 13);
        assertEquals(13, list.size());
        for (Media media : list) {
            Assert.assertTrue(checkMedia(media));
        }
    }

    @Test
    public void testGetMediasByTag() throws Exception {
        List<Media> list = instagram.getMediasByTag("Moscow", 50);
        assertEquals(50, list.size());
        for (Media media : list) {
            Assert.assertTrue(checkMedia(media));
        }
    }

    @Test
    public void testGetTopMediasByTag() throws Exception {
        List<Media> list = instagram.getTopMediasByTag("Sheremetyevo");
        assertEquals(9, list.size());
        for (Media media : list) {
            Assert.assertTrue(checkMedia(media));
        }
    }

    @Test
    public void testGetCommentsByMediaCode() throws Exception {
        List<Comment> list = instagram.getCommentsByMediaCode("BHaRdodBouH", 50);
        assertEquals(50, list.size());
        for (Comment comment : list) {
            Assert.assertTrue(checkComment(comment));
            Assert.assertTrue(checkAccount(comment.user));
        }
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
        if (media.commentsCount > 0) {
            assertTrue(media.previewCommentsList.size() > 0);
        } else {
            assertFalse(media.previewCommentsList.size() > 0);
        }
    }

    @Test
    public void testAuthMethodsMultiThread() throws InterruptedException, ExecutionException {
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
                        medias.addAll(instagram.getTopMediasByTag(s));
                        Thread.sleep(1000);
                        medias.addAll(instagram.getMediasByTag(s, 50));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InstagramException e) {
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
            Assert.assertEquals(59, result.size());
        }
    }

    private static boolean checkMedia(Media m) {
        if (m == null) {
            System.out.println("NULL");
            return false;
        }
        if (m.code != null
                && m.code.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                && m.createdTime > 0
                && (m.ownerId != null  || m.owner != null)) {
            return true;
        } else {
            System.out.println(m);
            return false;
        }
    }

    private static boolean checkComment(Comment c){
        if (c == null) {
            System.out.println("NULL");
            return false;
        }
        if (c.id != null
                && c.id.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                && c.createdAt > 0
                && c.text != null
                && c.text.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                && c.user != null) {
            return true;
        } else {
            System.out.println(c);
            return false;
        }
    }

    private static boolean checkAccount(Account o) {
        if (o == null) {
            return false;
        }
        if (o.id > 0
                && o.username != null
                && o.username.replace(" ", "").length() > 0 //instead of trim() and isEmpty()
                ) {
            return true;
        } else {
            System.out.println(o);
            return false;
        }
    }
}
