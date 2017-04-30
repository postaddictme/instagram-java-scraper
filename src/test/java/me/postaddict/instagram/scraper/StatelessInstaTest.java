package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.domain.Media;
import me.postaddict.instagram.scraper.domain.Tag;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static me.postaddict.instagram.scraper.ContentCheck.checkAccount;
import static me.postaddict.instagram.scraper.ContentCheck.checkMedia;
import static me.postaddict.instagram.scraper.ContentCheck.checkTag;
import static org.junit.Assert.*;

@Ignore
public class StatelessInstaTest {

    private static StatelessInsta client;

    @BeforeClass
    public static void setUp() throws Exception {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new UserAgentInterceptor(UserAgents.OSX_CHROME))
                .addInterceptor(new ErrorInterceptor())
                .build();
        client = new Instagram(httpClient);
        client.basePage();
    }

    @Test
    public void testGetAccountByUsername() throws Exception {
        Account account = client.getAccountByUsername("kevin");
        assertEquals("kevin", account.username);
        assertTrue(checkAccount(account));
        System.out.println(account);
    }

    @Test
    public void testGetTagByName() throws Exception {
        Tag tag = client.getTagByName("corgi");
        assertEquals("corgi", tag.name);
        assertTrue(checkTag(tag));
        System.out.println(tag);
    }

    @Test
    public void testGetMedias() throws Exception {
        List<Media> mediaList = client.getMedias("kevin", 50);
        assertEquals(50, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        System.out.println(mediaList);
    }

    @Test
    public void testGetMediaByUrl() throws Exception {
        Media media = client.getMediaByUrl("https://www.instagram.com/p/BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
        assertTrue(checkMedia(media));
        System.out.println(media);
    }

    @Test
    public void testGetMediaByCode() throws Exception {
        Media media = client.getMediaByCode("BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
        assertTrue(checkMedia(media));
        System.out.println(media);
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
        Media media = client.getMedias("kevin", 1).get(0);
        if (media.commentsCount > 0){
            assertTrue(media.previewCommentsList.size() > 0);
        } else {
            assertFalse(media.previewCommentsList.size() > 0);
        }
    }

}
