package me.postaddict.instagram.scraper.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Data
public class Account {
    @Id
    private long id;
    @Column(name = "username", nullable = false)
    private String username;
    private String fullName;
    private Boolean isPrivate;
    private Boolean isVerified;
    @Column(name = "biography", length = 4096)
    private String biography;
    @Column(name = "external_url", length = 4096)
    private String externalUrl;
    private Integer followedBy;
    private Integer follows;
    private String blockedByViewer;
    private Boolean countryBlock;
    @Transient
    private String externalUrlLinkshimmed;
    private Boolean followedByViewer;
    private Boolean followsViewer;
    private Boolean hasBlockedViewer;
    private Boolean hasRequestedViewer;
    private String profilePicUrl;
    private String profilePicUrlHd;
    private Boolean requestedByViewer;
    private String connectedFbPage;
    private Boolean isUnpublished;
    @Transient
    private PageObject<Media> media;
    private Date lastUpdated = new Date();
}
