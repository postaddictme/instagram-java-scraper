import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Account;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        Instagram instagram = new Instagram();
        try {
            Account account = instagram.getAccountByUsername("kevin");
            account = instagram.getAccountById(3);
            System.out.println(account.mediaCount);
//            List<Media> medias = instagramscraper.getMedias("durov", 12);
//            System.out.println(medias.get(0).imageHighResolutionUrl);
//            Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BGY0zB4r7X2/");
//            System.out.println(media.owner.username);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }
    }
}
