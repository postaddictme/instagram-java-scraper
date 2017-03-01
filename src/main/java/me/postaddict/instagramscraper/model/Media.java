package me.postaddict.instagramscraper.model;

import me.postaddict.instagramscraper.Endpoint;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
    public List<Comment> previewCommentsList;
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
        String[] imageUrls = getImageUrls((String) ((Map) images.get("standard_resolution")).get("url"));
        instance.imageLowResolutionUrl = imageUrls[0];
        instance.imageThumbnailUrl = imageUrls[1];
        instance.imageStandardResolutionUrl = imageUrls[2];
        instance.imageHighResolutionUrl = imageUrls[3];
        instance.commentsCount = (new Double(((Map) mediaMap.get("comments")).get("count").toString())).intValue();
        instance.likesCount = (new Double(((Map) mediaMap.get("likes")).get("count").toString())).intValue();

        if (instance.type.equals(TYPE_VIDEO) && mediaMap.containsKey("videos")) {
            Map videos = (Map) mediaMap.get("videos");
            instance.videoLowResolutionUrl = (String) ((Map) videos.get("low_resolution")).get("url");
            instance.videoStandardResolutionUrl = (String) ((Map) videos.get("standard_resolution")).get("url");
            instance.videoLowBandwidthUrl = (String) ((Map) videos.get("low_bandwidth")).get("url");
        }

        instance.previewCommentsList = new ArrayList<Comment>();
        if (instance.commentsCount > 0){
            for (Object o: (List)((Map)mediaMap.get("comments")).get("data")){
                instance.previewCommentsList.add(Comment.fromApi((Map)o));
            }
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
        String[] imageUrls = getImageUrls((String) pageMap.get("display_src"));
        instance.imageLowResolutionUrl = imageUrls[0];
        instance.imageThumbnailUrl = imageUrls[1];
        instance.imageStandardResolutionUrl = imageUrls[2];
        instance.imageHighResolutionUrl = imageUrls[3];
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
        String[] imageUrls = getImageUrls((String) mediaMap.get("display_src"));
        instance.imageLowResolutionUrl = imageUrls[0];
        instance.imageThumbnailUrl = imageUrls[1];
        instance.imageStandardResolutionUrl = imageUrls[2];
        instance.imageHighResolutionUrl = imageUrls[3];
        instance.type = TYPE_IMAGE;
        if ((Boolean) mediaMap.get("is_video")) {
            instance.type = TYPE_VIDEO;
            instance.videoViews = ((Double) mediaMap.get("video_views")).intValue();
        }
        instance.id = (String) mediaMap.get("id");
        return instance;
    }

    private static String[] getImageUrls(String imageUrl) {
        URL url = null;
        String[] urls = new String[4];
        try {
            url = new URL(imageUrl);
            String[] parts = url.getPath().split("/");
            String imageName = parts[parts.length - 1];
            urls[0] = Endpoint.INSTAGRAM_CDN_URL + "t/s150x150/" + imageName;
            urls[1] = Endpoint.INSTAGRAM_CDN_URL + "t/s320x320/" + imageName;
            urls[2] = Endpoint.INSTAGRAM_CDN_URL + "t/s640x640/" + imageName;
            urls[3] = Endpoint.INSTAGRAM_CDN_URL + "t/" + imageName;
            return urls;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urls;
    }

    public static String getIdFromCode(String code) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        long id = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            id = id * 64 + alphabet.indexOf(c);
        }
        return id + "";
    }

    public static String getCodeFromId(String id) {
        String[] parts = id.split("_");
        id = parts[0];
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        String code = "";
        long longId = Long.parseLong(id);
        while (longId > 0) {
            long index = longId % 64;
            longId = (longId - index) / 64;
            code = alphabet.charAt((int) index) + code;
        }
        return code;
    }

    public static String getLinkFromId(String id) {
        String code = Media.getCodeFromId(id);
        return Endpoint.getMediaPageLinkByCode(code);
    }
}
