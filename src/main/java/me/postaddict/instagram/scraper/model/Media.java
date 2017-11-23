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
    private long id;
    private String shortcode;
    @Embedded
    private MediaResource mediaResource;
    @Transient
    private String gatingInfo;
    private String caption;
    private Integer commentCount;
    @Transient
    private PageObject<Comment> commentPreview;
    @ManyToMany
    private List<Comment> firstComments;
    private Boolean commentsDisabled;
    private Boolean captionIsEdited;
    private Long takenAtTimestamp;
    private Integer likeCount;
    @ManyToMany
    private List<Account> firstLikes;
    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Account owner;
    private Boolean viewerHasLiked;
    private Boolean viewerHasSaved;
    private Boolean viewerHasSavedToCollection;
    private Boolean isAdvertising;
    @OneToMany(mappedBy = "parentMedia")
    private Collection<CarouselResource> carouselMedia;
    @ElementCollection
    private Collection<TaggedUser> taggedUser;
    private Date lastUpdated = new Date();
}
