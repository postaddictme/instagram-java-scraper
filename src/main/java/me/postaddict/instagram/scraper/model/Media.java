package me.postaddict.instagram.scraper.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Media extends MediaResource{
    private MediaType mediaType;
    private long id;
    private String shortcode;
    private String gatingInfo;
    private String caption;
    private Integer commentCount;
    private PageObject<Comment> commentPreview;
    private List<Comment> firstComments;
    private Boolean commentsDisabled;
    private Boolean captionIsEdited;
    private Long takenAtTimestamp;
    private Integer likeCount;
    private List<Account> firstLikes;
    private Location location;
    private Account owner;
    private Boolean viewerHasLiked;
    private Boolean viewerHasSaved;
    private Boolean viewerHasSavedToCollection;
    private Boolean isAdvertising;
    private Collection<CarouselResource> carouselMedia;
    private Collection<TaggedUser> taggedUser;
    private Date lastUpdated = new Date();

    @Id
    public long getId() {
        return id;
    }

    @Transient
    public String getGatingInfo() {
        return gatingInfo;
    }

    @Column(name = "caption", length = 4096)
    public String getCaption() {
        return caption;
    }

    @Transient
    public PageObject<Comment> getCommentPreview() {
        return commentPreview;
    }

    @ManyToMany
    public List<Comment> getFirstComments() {
        return firstComments;
    }

    @ManyToMany
    public List<Account> getFirstLikes() {
        return firstLikes;
    }

    @ManyToOne
    @JoinColumn(name="location_id")
    public Location getLocation() {
        return location;
    }

    @ManyToOne
    @JoinColumn(name="owner_id")
    public Account getOwner() {
        return owner;
    }

    @OneToMany(mappedBy = "parentMedia")
    public Collection<CarouselResource> getCarouselMedia() {
        return carouselMedia;
    }

    @ElementCollection
    public Collection<TaggedUser> getTaggedUser() {
        return taggedUser;
    }

    @Column(name = "height") @Override
    public Integer getHeight() {
        return height;
    }

    @Column(name = "width") @Override
    public Integer getWidth() {
        return width;
    }

    @Column(name = "display_url") @Override
    public String getDisplayUrl() {
        return displayUrl;
    }

    @ElementCollection @Override
    public List<DisplayResource> getDisplayResources() {
        return displayResources;
    }

    @Column(name = "is_video") @Override
    public Boolean getIsVideo() {
        return isVideo;
    }

    @Transient
    public Date getCreated(){
        return takenAtTimestamp != null ? new Date(takenAtTimestamp) : null;
    }
}
