package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.List;

@Data
@ToString
@Embeddable
public class MediaResource {
    protected Integer height; //"dimensions": {"height": 864,
                        //"config_height": 512
    protected Integer width;  //"dimensions": {"width": 1080
                        //"config_width": 640,
    protected String displayUrl;//"display_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/e35/22351958_144137916205565_6923513639366295552_n.jpg",
                                //"src": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-15/s640x640/sh0.08/e35/22351958_144137916205565_6923513639366295552_n.jpg",
    @ElementCollection
    protected List<DisplayResource> displayResources;//display_resources
    protected Boolean isVideo;//"is_video": false,
    @Transient
    protected Boolean shouldLogClientEvent;//"should_log_client_event": false,
    @Transient
    protected String trackingToken;//"tracking_token": "eyJ2ZXJzaW9uIjo1LCJwYXlsb2FkIjp7ImlzX2FuYWx5dGljc190cmFja2VkIjp0cnVlLCJ1dWlkIjoiZDkwNWNmMjFmZDc3NDQ1ZmJlZjI0MTM3MjBhMzg2YjUxNjI0MTYxMzM2NzQzMzE0NDU4In0sInNpZ25hdHVyZSI6IiJ9",
}
