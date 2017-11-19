package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@ToString
public class Media {
    private MediaType mediaType;
    @Id
    private long id;//"id": "1624161336743314458",
    private String shortcode;//"shortcode": "BaKLiFugkQa",
    @Embedded
    private MediaResource mediaResource;//"dimensions": {"height": 864,
                                        //"dimensions": {"width": 1080
                                        //"display_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/e35/22351958_144137916205565_6923513639366295552_n.jpg",
    @Transient
    private String gatingInfo;//"gating_info": null,
    private String caption;//"edge_media_to_caption": {"edges": [{"node": {"text": "This morning, I attended Vogue\u2019s Forces of Fashion conference, where I joined @marcjacobs and Vogue\u2019s Sally Singer on stage to talk all things fashion and Instagram. Afterwards, I was able to meet a bunch of young fashion enthusiasts to learn how they use Instagram to find inspiration. Scroll through to see more!"
    private Integer commentCount;//"edge_media_to_comment": {"count": 132,
    @Transient
    private PageObject<Comment> commentPreview; //"edge_media_to_comment": {"page_info": {"has_next_page": true,"end_cursor": "AQCevACiK8OqBEIOauzs11dVcr0cpnu_alC-_D2CRyT-JEo8kFTwozd8RidbD4FFlQahzAYq6gOO3TLN7Ut78KU2ZL5vCDV7Kio1zjbyMjGr0A"},
    @ManyToMany
    private List<Comment> firstComments;
    private Boolean commentsDisabled;//"comments_disabled": false,
    private Boolean captionIsEdited;//caption_is_edited
    private Long takenAtTimestamp;//"taken_at_timestamp": 1507835140,
    private Integer likeCount;//"edge_media_preview_like": {"count": 5219,
    @ManyToMany
    private List<Account> firstLikes;//"edge_media_preview_like": {"edges": [{"node": {"id": "5723685056","profile_pic_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-19/s150x150/23421372_534899493542750_8049254439145439232_n.jpg","username": "cicinss1502"
    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;//"location": {"id": "26929","has_public_page": true,"name": "Milk Media","slug": "milk-media"},
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Account owner;//"owner": {"id": "3","profile_pic_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-19/s150x150/13732144_1764457777134045_549538515_a.jpg","username": "kevin",
    private Boolean viewerHasLiked;//"viewer_has_liked": false,
    private Boolean viewerHasSaved;//"viewer_has_saved": false,
    private Boolean viewerHasSavedToCollection;//"viewer_has_saved_to_collection": false,
    private Boolean isAdvertising;//"is_ad": false,
    @OneToMany(mappedBy = "parentMedia")
    private Collection<CarouselResource> carouselMedia;//"edge_sidecar_to_children": {"edges": [{"node": {
    @ElementCollection
    private Collection<TaggedUser> taggedUser;//edge_media_to_tagged_user
    private Date lastUpdated = new Date();
}
