package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.client.InstaClientFactory;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static me.postaddict.instagram.scraper.ContentCheck.checkAccount;
import static me.postaddict.instagram.scraper.ContentCheck.checkMedia;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatelessInstaTest {

    private static StatelessInsta instagram;

    @BeforeClass
    public static void setUp() {
        InstaClient instaClient = new InstaClientFactory(InstaClientFactory.InstaClientType.STATELESS).getClient();
        instagram = new Instagram(instaClient);
    }

    @Test
    public void testGetMediasByTag() throws Exception {
        Tag tag = instagram.getMediasByTag("Moscow");
        Collection<Media> list = tag.getMediaRating().getMedia().getNodes();
        assertTrue(list.size() > 18);
        for (Media media : list) {
            assertTrue(checkMedia(media));
        }
        System.out.println(list.size());
    }

    @Test
    public void testGetMediasByTagFewPages() throws Exception {
        Tag tag = instagram.getMediasByTag("Moscow", 2);
        Collection<Media> list = tag.getMediaRating().getMedia().getNodes();
        assertTrue(list.size() > 18);
        for (Media media : list) {
            assertTrue(checkMedia(media));
        }
        System.out.println(list.size());
    }

    @Test
    public void testGetMediaByUserId() throws Exception {
        int userId = 3;
        int size = 33;
        PageObject<Media> mediaList1 = instagram.getMediaByUserId(userId, size);
        List<Media> mediaList = mediaList1.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 4;
        size = 50;
        PageObject<Media> mediaList2 = instagram.getMediaByUserId(userId, size);
        mediaList = mediaList2.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 5;
        size = 100;
        PageObject<Media> mediaList3 = instagram.getMediaByUserId(userId, size);
        mediaList = mediaList3.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 6;
        size = 125;
        PageObject<Media> mediaList4 = instagram.getMediaByUserId(userId, size);
        mediaList = mediaList4.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 11;
        int defaultSize = 24;
        PageObject<Media> mediaList5 = instagram.getMediaByUserId(userId);
        mediaList = mediaList5.getNodes();
        assertEquals(defaultSize, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());
    }

    @Test
    public void testGetAccountById() throws Exception {
        Account account = instagram.getAccountById(3);
        assertEquals("kevin", account.getUsername());
        assertTrue(checkAccount(account));
        System.out.println(account);
    }

    @Test
    public void testGetAccountByUsername() throws Exception {
        Account account = instagram.getAccountByUsername("kevin");
        assertEquals("kevin", account.getUsername());
        assertTrue(checkAccount(account));
        System.out.println(account);
    }

    @Test
    public void testGetMediaByUserName() throws Exception {
        PageObject<Media> medias = instagram.getMedias("kevin", 2);
        Collection<Media> mediaList = medias.getNodes();
        assertEquals(60, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        System.out.println(mediaList);
    }

    @Test
    public void testGetMediaByUrl() throws Exception {
        Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BHaRdodBouH");
        assertEquals("kevin", media.getOwner().getUsername());
        assertTrue(checkMedia(media));
        System.out.println(media);
    }

    @Test
    public void testGetMediaByCode() throws Exception {
        Media media = instagram.getMediaByCode("BHaRdodBouH");
        assertEquals("kevin", media.getOwner().getUsername());
        assertTrue(checkMedia(media));
        System.out.println(media);
    }

    @Test
    public void testGetIdFromCode() {
        String code = MediaUtil.getCodeFromId("1270593720437182847");
        assertEquals("BGiDkHAgBF_", code);
        code = MediaUtil.getCodeFromId("1270593720437182847_3");
        assertEquals("BGiDkHAgBF_", code);
    }

    @Test
    public void testGetCodeFromId() {
        String id = MediaUtil.getIdFromCode("BGiDkHAgBF_");
        assertEquals("1270593720437182847", id);
    }

    @Test
    public void testPreviewComments() throws Exception {
        Media media = instagram.getMediaByCode("Ba63OW3hAKq");
        if (media.getCommentCount() > 0) {
            assertTrue(media.getCommentPreview().getNodes().size() > 0);
        } else {
            assertFalse(media.getCommentPreview().getNodes().size() > 0);
        }
    }

}
