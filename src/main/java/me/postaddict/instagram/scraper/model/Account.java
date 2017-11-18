package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@ToString
public class Account {
    @Id
    private long id;//"id": "3",
    @Column(name = "username", nullable = false)
    private String username;//"username": "kevin",
    private String fullName;//"full_name": "Kevin Systrom",
    private Boolean isPrivate;//"is_private": false,
    private Boolean isVerified;//"is_verified": true,
    @Column(name = "biography", length = 2048)
    private String biography; //"biography": "CEO \u0026 Co-founder of Instagram",
    private String externalUrl;//"external_url": null,
    private Integer followed_by;//"followed_by": {"count": 1492539},
    private Integer follows;// "follows": {"count": 628},
    private String blockedByViewer; //"blocked_by_viewer": false,
    private Boolean countryBlock;//"country_block": false,
    private String externalUrlLinkshimmed;//"external_url_linkshimmed": null,
    private Boolean followedByViewer;//"followed_by_viewer": false,
    private Boolean followsViewer;//"follows_viewer": false,
    private Boolean hasBlockedViewer;//"has_blocked_viewer": false,
    private Boolean hasRequestedViewer;//"has_requested_viewer": false,
    private String profilePicUrl;//"profile_pic_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-19/s150x150/13732144_1764457777134045_549538515_a.jpg",
    private String profilePicUrlHd;//"profile_pic_url_hd": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-19/s150x150/13732144_1764457777134045_549538515_a.jpg",
    private Boolean requestedByViewer;//"requested_by_viewer": false,
    private String connectedFbPage;//"connected_fb_page": null,
    private Date lastUpdated = new Date();
}
