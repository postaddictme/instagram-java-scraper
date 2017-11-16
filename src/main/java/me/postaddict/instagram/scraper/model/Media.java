package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
public class Media {
    private MediaType mediaType;
    private long id;//"id": "1624161336743314458",
    private String shortcode;//"shortcode": "BaKLiFugkQa",
    private MediaResource mediaResource;//"dimensions": {"height": 864,
                                        //"dimensions": {"width": 1080
                                        //"display_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/e35/22351958_144137916205565_6923513639366295552_n.jpg",
    private String gatingInfo;//"gating_info": null,
    private String caption;//"edge_media_to_caption": {"edges": [{"node": {"text": "This morning, I attended Vogue\u2019s Forces of Fashion conference, where I joined @marcjacobs and Vogue\u2019s Sally Singer on stage to talk all things fashion and Instagram. Afterwards, I was able to meet a bunch of young fashion enthusiasts to learn how they use Instagram to find inspiration. Scroll through to see more!"
    private Integer commentCount;//"edge_media_to_comment": {"count": 132,
    private PageObject<Comment> commentPreview; //"edge_media_to_comment": {"page_info": {"has_next_page": true,"end_cursor": "AQCevACiK8OqBEIOauzs11dVcr0cpnu_alC-_D2CRyT-JEo8kFTwozd8RidbD4FFlQahzAYq6gOO3TLN7Ut78KU2ZL5vCDV7Kio1zjbyMjGr0A"},
    private Boolean commentsDisabled;//"comments_disabled": false,
    private long takenAtTimestamp;//"taken_at_timestamp": 1507835140,
    private Integer likeCount;//"edge_media_preview_like": {"count": 5219,
    private Collection<Account> likesPreview;//"edge_media_preview_like": {"edges": [{"node": {"id": "5723685056","profile_pic_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-19/s150x150/23421372_534899493542750_8049254439145439232_n.jpg","username": "cicinss1502"
    private Location location;//"location": {"id": "26929","has_public_page": true,"name": "Milk Media","slug": "milk-media"},
    private Account owner;//"owner": {"id": "3","profile_pic_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-19/s150x150/13732144_1764457777134045_549538515_a.jpg","username": "kevin",
    private Boolean viewerHasLiked;//"viewer_has_liked": false,
    private Boolean viewerHasSaved;//"viewer_has_saved": false,
    private Boolean viewerHasSavedToCollection;//"viewer_has_saved_to_collection": false,
    private Boolean isAdvertising;//"is_ad": false,
    private Collection<MediaResource> carouselMedia;//"edge_sidecar_to_children": {"edges": [{"node": {
}
