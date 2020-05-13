package me.postaddict.instagram.scraper;

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

    private static StatelessInsta client;

    @BeforeClass
    public static void setUp() {
        client = new InstaClientFactory(InstaClientFactory.InstaClientType.STATELESS).getClient();
    }

    @Test
    public void testGetMediasByTag() throws Exception {
        Tag tag = client.getMediasByTag("Moscow");
        Collection<Media> list = tag.getMediaRating().getMedia().getNodes();
        assertTrue(list.size() > 18);
        for (Media media : list) {
            assertTrue(checkMedia(media));
        }
        System.out.println(list.size());
    }

    @Test
    public void testGetMediasByTagFewPages() throws Exception {
        Tag tag = client.getMediasByTag("Moscow", 2);
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
        PageObject<Media> mediaList1 = client.getMediaByUserId(userId, size);
        List<Media> mediaList = mediaList1.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 4;
        size = 50;
        PageObject<Media> mediaList2 = client.getMediaByUserId(userId, size);
        mediaList = mediaList2.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 5;
        size = 100;
        PageObject<Media> mediaList3 = client.getMediaByUserId(userId, size);
        mediaList = mediaList3.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 6;
        size = 125;
        PageObject<Media> mediaList4 = client.getMediaByUserId(userId, size);
        mediaList = mediaList4.getNodes();
        assertEquals(size, mediaList.size());
        for (Media media : mediaList) {
            assertTrue(checkMedia(media));
        }
        assertEquals(userId, mediaList.get(0).getOwner().getId());
        System.out.println(mediaList.size());

        userId = 11;
        int defaultSize = 24;
        PageObject<Media> mediaList5 = client.getMediaByUserId(userId);
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
    public void testGetMediaByUserName() throws Exception {
        PageObject<Media> medias = client.getMedias("kevin", 2);
        Collection<Media> mediaList = medias.getNodes();
        assertEquals(50, mediaList.size());
        for (Media media : mediaList) {
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
        Media media = client.getMediaByCode("Ba63OW3hAKq");
        if (media.getCommentCount() > 0) {
            assertTrue(media.getCommentPreview().getNodes().size() > 0);
        } else {
            assertFalse(media.getCommentPreview().getNodes().size() > 0);
        }
    }

}
