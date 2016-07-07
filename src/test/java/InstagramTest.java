import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Account;
import org.junit.Test;

import java.io.IOException;

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
}
