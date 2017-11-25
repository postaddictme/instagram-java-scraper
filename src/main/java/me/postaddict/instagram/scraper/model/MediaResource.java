package me.postaddict.instagram.scraper.model;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.List;

@Data
@Embeddable
public class MediaResource {
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
}
