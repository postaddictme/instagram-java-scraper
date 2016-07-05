import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Media;

import java.io.IOException;

/**
 * Created by raiym on 5/6/16.
 */
public class App {
    public static void main(String[] args) {
        Instagram instagram = new Instagram();
        try {
            me.postaddict.instagramscraper.model.Account account = instagram.getAccount("kevin");
            System.out.println(account.mediaCount);
//            List<Media> medias = instagramscraper.getMedias("durov", 12);
//            System.out.println(medias.get(0).imageHighResolutionUrl);
            Media media = instagram.getMediaByUrl("https://www.instagramscraper.com/p/BGY0zB4r7X2/");
            System.out.println(media.owner.username);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }
    }
}
