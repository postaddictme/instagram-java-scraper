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
