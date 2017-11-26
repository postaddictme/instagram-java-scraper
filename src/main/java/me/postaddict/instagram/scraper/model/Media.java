package me.postaddict.instagram.scraper.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Media {
    protected Integer height;
    protected Integer width;
    protected String displayUrl;
    @ElementCollection
    protected List<DisplayResource> displayResources;
    protected Boolean isVideo;
    @Transient
    protected Boolean shouldLogClientEvent;
    @Transient
    protected String trackingToken;
    private MediaType mediaType;
    @Id
    private long id;
    private String shortcode;
    @Transient
    private String gatingInfo;
    @Column(name = "caption", length = 4096)
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

    @Transient
    public Date getCreated(){
        return takenAtTimestamp != null ? new Date(takenAtTimestamp) : null;
    }
}
