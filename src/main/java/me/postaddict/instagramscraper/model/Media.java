package me.postaddict.instagramscraper.model;

import me.postaddict.instagramscraper.Endpoint;

import java.util.List;
import java.util.Map;

public class Media {
    public static final String INSTAGRAM_URL = "https://www.instagramscraper.com/";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";

    public String id;
    public long createdTime;
    public String type;
    public String link;
    public String imageLowResolutionUrl;
    public String imageThumbnailUrl;
    public String imageStandardResolutionUrl;
    public String imageHighResolutionUrl;
    public String caption;
    public String videoLowResolutionUrl;
    public String videoStandardResolutionUrl;
    public String videoLowBandwidthUrl;
    public String code;
    public int commentsCount;
    public int likesCount;
    public int videoViews;
    public String ownerId;
    public Account owner;
    public String locationName;

    public Media() {
    }

    public static Media fromApi(Map mediaMap) {
        Media instance = new Media();
        instance.id = (String) mediaMap.get("id");
        instance.createdTime = Long.parseLong((String) mediaMap.get("created_time"));
        instance.type = (String) mediaMap.get("type");
        instance.link = (String) mediaMap.get("link");
        instance.code = (String) mediaMap.get("code");
        if (mediaMap.get("caption") != null) {
            instance.caption = (String) ((Map) mediaMap.get("caption")).get("text");
        }

        Map images = (Map) mediaMap.get("images");
        instance.imageLowResolutionUrl = (String) ((Map) images.get("low_resolution")).get("url");
        instance.imageThumbnailUrl = (String) ((Map) images.get("thumbnail")).get("url");
        instance.imageStandardResolutionUrl = (String) ((Map) images.get("standard_resolution")).get("url");

        if (instance.type.equals(TYPE_VIDEO)) {
            Map videos = (Map) mediaMap.get("videos");
            instance.videoLowResolutionUrl = (String) ((Map) videos.get("low_resolution")).get("url");
            instance.videoStandardResolutionUrl = (String) ((Map) videos.get("standard_resolution")).get("url");
            instance.videoLowBandwidthUrl = (String) ((Map) videos.get("low_bandwidth")).get("url");
        }
        return instance;
    }

    public static Media fromMediaPage(Map pageMap) {
        Media instance = new Media();
        instance.id = (String) pageMap.get("id");
        instance.type = TYPE_IMAGE;
        if ((Boolean) pageMap.get("is_video")) {
            instance.type = TYPE_VIDEO;
            instance.videoStandardResolutionUrl = (String) pageMap.get("video_url");
        }
        instance.createdTime = ((Double) pageMap.get("date")).longValue();
        instance.code = (String) pageMap.get("code");
        instance.link = INSTAGRAM_URL + "p/" + instance.code;
        instance.imageStandardResolutionUrl = (String) pageMap.get("display_src");
        if (pageMap.get("caption") != null) {
            instance.caption = (String) pageMap.get("caption");
        }
        instance.owner = Account.fromMediaPage((Map) pageMap.get("owner"));
        return instance;
    }

    public static Media fromTagPage(Map mediaMap) {
        Media instance = new Media();
        instance.code = (String) mediaMap.get("code");
        instance.link = Endpoint.getMediaPageLinkByCode(instance.code);
        instance.commentsCount = ((Double) ((Map) mediaMap.get("comments")).get("count")).intValue();
        instance.likesCount = ((Double) ((Map) mediaMap.get("likes")).get("count")).intValue();
        instance.ownerId = (String) ((Map) mediaMap.get("owner")).get("id");
        if (mediaMap.get("caption") != null) {
            instance.caption = (String) mediaMap.get("caption");
        }
        instance.createdTime = ((Double) mediaMap.get("date")).longValue();
        instance.imageThumbnailUrl = (String) mediaMap.get("thumbnail_src");
        instance.imageStandardResolutionUrl = (String) mediaMap.get("display_src");
        instance.type = TYPE_IMAGE;
        if((Boolean) mediaMap.get("is_video")) {
            instance.type = TYPE_VIDEO;
            instance.videoViews = ((Double) mediaMap.get("video_views")).intValue();
        }
        instance.id = (String) mediaMap.get("id");
        return instance;
    }
}
