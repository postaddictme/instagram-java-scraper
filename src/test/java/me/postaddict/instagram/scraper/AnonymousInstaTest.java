package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.client.InstaClientFactory;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.CarouselResource;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.Location;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static me.postaddict.instagram.scraper.ContentCheck.checkAccount;
import static me.postaddict.instagram.scraper.ContentCheck.checkComment;
import static me.postaddict.instagram.scraper.ContentCheck.checkMedia;
import static me.postaddict.instagram.scraper.ContentCheck.checkTag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AnonymousInstaTest {

    private static AnonymousInsta client;

    @BeforeClass
    public static void setUp() {
        InstaClient instaClient = new InstaClientFactory(InstaClientFactory.InstaClientType.ANONYMOUS).getClient();
        client = new Instagram(instaClient);
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
        Tag tag = client.getMediasByTag("corgi");
        assertEquals("corgi", tag.getName());
        assertTrue(checkTag(tag));
        System.out.println(tag);
    }

    @Test
    public void testGetMedias() throws Exception {
        PageObject<Media> mediaList = client.getMedias("kevin", 2);
        assertEquals(60, mediaList.getNodes().size());
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

    @Test(expected = IllegalArgumentException.class)
    public void testGetMalformedMediaByUrl() throws Exception {
        client.getMediaByUrl("https://www.instagram.com/p/BHaRdodBouH/");
    }

    @Test
    public void testNonLiteralMediaCode() throws Exception {
        Media media = client.getMediaByCode("BWizrpZgg-w");
        assertEquals("pixar", media.getOwner().getUsername());
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
        Media media = client.getMediaByCode("CBizCfxHdsk");
        assertEquals("pechennikova_m", media.getOwner().getUsername());
        assertTrue(checkMedia(media));
        assertEquals(4, media.getCarouselMedia().size());
        Iterator<CarouselResource> carouselIterator = media.getCarouselMedia().iterator();
        assertThat(carouselIterator.next().getDisplayUrl()).
                contains("/t51.2885-15/e35/s1080x1080/104218734_612831389332362_1574222533739090610_n.jpg");
        assertThat(carouselIterator.next().getDisplayUrl()).
                contains("/t51.2885-15/e35/s1080x1080/103535418_301618131014269_461318135598219361_n.jpg");
        assertThat(carouselIterator.next().getDisplayUrl()).
                contains("/t51.2885-15/e35/s1080x1080/104055511_322181605622243_3584951769058175602_n.jpg");
        assertThat(carouselIterator.next().getDisplayUrl()).
                contains("/t51.2885-15/e35/s1080x1080/103940314_1198920703801651_2533723929517596122_n.jpg");
        System.out.println(media);
    }


    @Test
    public void testGetMediaByCodeVideoPost() throws Exception {
        Media media = client.getMediaByCode("Bde90J7n6ba");
        assertEquals("corgillection", media.getOwner().getUsername());
        assertThat(media.getVideoUrl()).contains(".mp4");
        assertThat(media.getDisplayUrl()).contains(".jpg");
        assertTrue(checkMedia(media));
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
        System.out.println(list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMediasByTagError() throws Exception {
        client.getMediasByTag("#HarryWright", 20);
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
        assertTrue(list.size()>20);
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
