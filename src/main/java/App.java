import me.postaddict.instagram.Instagram;
import me.postaddict.instagram.InstagramException;
import me.postaddict.instagram.model.Media;

import java.io.IOException;

/**
 * Created by raiym on 5/6/16.
 */
public class App {
    public static void main(String[] args) {
        Instagram instagram = new Instagram();
        try {
//            me.postaddict.instagram.model.Account account = instagram.getAccount("durov");
//            System.out.println(account.profilePicUrl);
//            List<Media> medias = instagram.getMedias("durov", 12);
//            System.out.println(medias.get(0).imageHighResolutionUrl);
            Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BEz_ahOsmS_/");
            System.out.println(media.owner.username);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }
    }
}
