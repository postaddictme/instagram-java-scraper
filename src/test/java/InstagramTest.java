import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Account;
import me.postaddict.instagramscraper.model.Comment;
import me.postaddict.instagramscraper.model.Media;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InstagramTest {
    Instagram instagram;

    @Before
    public void setUp() throws Exception {
        instagram = new Instagram();
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
}
