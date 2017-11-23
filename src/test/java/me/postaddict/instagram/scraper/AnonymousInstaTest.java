package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import me.postaddict.instagram.scraper.model.*;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static me.postaddict.instagram.scraper.ContentCheck.*;
import static org.junit.Assert.*;

@Ignore
public class AnonymousInstaTest {

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
    public void testGetAccountById() throws Exception {
        Account account = client.getAccountById(3);
        assertEquals("kevin", account.getUsername());
        assertTrue(checkAccount(account));
        System.out.println(account);
    }

    @Test
    public void testGetAccountByUsername() throws Exception {
        Account account = client.getAccountByUsername("kevin");
        assertEquals("kevin", account.getUsername());
        assertTrue(checkAccount(account));
        System.out.println(account);
    }

    @Test
    public void testGetTagByName() throws Exception {
        Tag tag = client.getTagByName("corgi");
        assertEquals("corgi", tag.getName());
        assertTrue(checkTag(tag));
        System.out.println(tag);
    }

    @Test
    public void testGetMedias() throws Exception {
        PageObject<Media> mediaList = client.getMedias("kevin", 2);
        assertEquals(12*2, mediaList.getNodes().size());
        for (Media media : mediaList.getNodes()) {
            assertTrue(checkMedia(media));
        }
        System.out.println(mediaList);
    }

    @Test
    public void testGetMediaByUrl() throws Exception {
        Media media = client.getMediaByUrl("https://www.instagram.com/p/BHaRdodBouH");
        assertEquals("kevin", media.getOwner().getUsername());
        assertTrue(checkMedia(media));
        System.out.println(media);
    }

    @Test
    public void testGetMediaByCode() throws Exception {
        Media media = client.getMediaByCode("BHaRdodBouH");
        assertEquals("kevin", media.getOwner().getUsername());
        assertTrue(checkMedia(media));
        System.out.println(media);
    }

    @Test
    public void testGetMediaByCodeCarousel() throws Exception {
        Media media = client.getMediaByCode("BaKLiFugkQa");
        assertEquals("kevin", media.getOwner().getUsername());
        assertTrue(checkMedia(media));
        assertEquals(4, media.getCarouselMedia().size());
        Iterator<CarouselResource> carouselIterator = media.getCarouselMedia().iterator();
        assertEquals("https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/e35/22351958_144137916205565_6923513639366295552_n.jpg", carouselIterator.next().getDisplayUrl());
        assertEquals("https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/e35/22352110_177635979467060_738026920783904768_n.jpg", carouselIterator.next().getDisplayUrl());
        assertEquals("https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/e35/22352158_840551796104469_8224611081893445632_n.jpg", carouselIterator.next().getDisplayUrl());
        assertEquals("https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/e35/22427115_152159245386313_6593256479942246400_n.jpg", carouselIterator.next().getDisplayUrl());
        System.out.println(media);
    }

    @Test
    public void testGetLocationMediasById() throws Exception {
        String locationId = "17326249";
        Location location = client.getLocationMediasById(locationId, 2);
        Collection<Media> medias = location.getMediaRating().getMedia().getNodes();
        assertEquals(48, medias.size());
        for (Media media : medias) {
            assertTrue(checkMedia(media));
            assertNotNull(location);
            assertEquals(locationId, Long.toString(location.getId()));
            assertNotNull(location.getName());
            assertNotNull(location.getLat());
            assertNotNull(location.getLng());
        }
        System.out.println(medias);
    }

    @Test
    public void testGetMediasByTag() throws Exception {
        Tag tag = client.getMediasByTag("Moscow", 2);
        Collection<Media> list = tag.getMediaRating().getMedia().getNodes();
        assertTrue(list.size()>18);
        for (Media media : list) {
            assertTrue(checkMedia(media));
        }
        System.out.println(list);
    }

    @Test
    public void testGetTopMediasByTag() throws Exception {
        Tag tag = client.getMediasByTag("Sheremetyevo",1);
        Collection<Media> list = tag.getMediaRating().getTopPosts();
        assertEquals(9, list.size());
        for (Media media : list) {
            assertTrue(checkMedia(media));
        }
        System.out.println(list);
    }

    @Test
    public void testGetCommentsByMediaCode() throws Exception {
        PageObject<Comment> comments= client.getCommentsByMediaCode("BHaRdodBouH", 2);
        Collection<Comment> list = comments.getNodes();
        assertEquals(2*15, list.size());
        for (Comment comment : list) {
            assertTrue(checkComment(comment));
        }
        System.out.println(list);
    }

    @Test
    public void testGetIdFromCode() throws Exception {
        String code = MediaUtil.getCodeFromId("1270593720437182847");
        assertEquals("BGiDkHAgBF_", code);
        code = MediaUtil.getCodeFromId("1270593720437182847_3");
        assertEquals("BGiDkHAgBF_", code);
    }

    @Test
    public void testGetCodeFromId() throws Exception {
        String id = MediaUtil.getIdFromCode("BGiDkHAgBF_");
        assertEquals("1270593720437182847", id);
    }

    @Test
    public void testPreviewComments() throws Exception {
        Media media = client.getMediaByCode("Ba63OW3hAKq");
        if (media.getCommentCount() > 0){
            Collection<Comment> comments = media.getCommentPreview().getNodes();
            assertTrue(comments.size() > 0);
            for (Comment comment : comments) {
                assertTrue(checkComment(comment));
            }
        } else {
            assertFalse(media.getCommentPreview().getNodes().size() > 0);
        }
    }

}
