import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Account;
import me.postaddict.instagramscraper.model.Media;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InstagramTest {

    @Test
    public void testGetAccountByUsername() throws IOException, InstagramException {
        Instagram instagram = new Instagram();
        Account account = instagram.getAccountByUsername("kevin");
        assertEquals("kevin", account.username);
    }

    @Test
    public void testGetAccountById() throws IOException, InstagramException {
        Instagram instagram = new Instagram();
        Account account = instagram.getAccountById(3);
        assertEquals("kevin", account.username);
    }
    
    @Test
    public void testGetMedias() throws IOException, InstagramException {
        Instagram instagram = new Instagram();
        List<Media> mediaList = instagram.getMedias("kevin", 50);
        assertEquals(50, mediaList.size());
    }
    
    @Test
    public void testGetMediaByUrl() throws IOException, InstagramException {
        Instagram instagram = new Instagram();
        Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
    }
    
    @Test
    public void testGetMediaByCode() throws IOException, InstagramException {
        Instagram instagram = new Instagram();
        Media media = instagram.getMediaByCode("BHaRdodBouH");
        assertEquals("kevin", media.owner.username);
    }

    @Test
    public void testGetLocationMediasById() throws IOException, InstagramException {
        Instagram instagram = new Instagram();
        List<Media> list = instagram.getLocationMediasById("17326249", 13);
        assertEquals(13, list.size());
    }
}
