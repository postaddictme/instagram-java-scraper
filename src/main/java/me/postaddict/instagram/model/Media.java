package me.postaddict.instagram.model;

import java.util.List;
import java.util.Map;

public class Media {
    public static final String INSTAGRAM_URL = "https://www.instagram.com/";
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
    public Account owner;

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
        Map mediaMap = (Map) ((Map) (((List) (((Map) pageMap.get("entry_data")).get("PostPage"))).get(0))).get("media");
        Media instance = new Media();
        instance.id = (String) mediaMap.get("id");
        instance.type = TYPE_IMAGE;
        if ((Boolean) mediaMap.get("is_video")) {
            instance.type = TYPE_VIDEO;
            instance.videoStandardResolutionUrl = (String) mediaMap.get("video_url");
        }
        instance.createdTime = ((Double) mediaMap.get("date")).longValue();
        instance.code = (String) mediaMap.get("code");
        instance.link = INSTAGRAM_URL + "p/" + instance.code;
        instance.imageStandardResolutionUrl = (String) mediaMap.get("display_src");
        instance.caption = (String) mediaMap.get("caption");
        instance.owner = Account.fromMediaPage((Map) mediaMap.get("owner"));
        return instance;
    }
}
