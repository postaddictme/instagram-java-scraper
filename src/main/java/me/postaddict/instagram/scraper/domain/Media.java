package me.postaddict.instagram.scraper.domain;

import me.postaddict.instagram.scraper.Endpoint;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Media {

    public static final String INSTAGRAM_URL = "https://www.instagram.com/";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_CAROUSEL = "carousel";
    public static final long INSTAGRAM_BORN_YEAR = 1262304000000L;
    public String id;
    public long createdTime;
    public String type;
    public String link;
    public ImageUrls imageUrls = new ImageUrls();
    public List<CarouselMedia> carouselMedia;
    public String caption;
    public VideoUrls videoUrls = new VideoUrls();
    public String shortcode;
    public int commentsCount;
    public List<Comment> previewCommentsList;
    public int likesCount;
    public int videoViews;
    public String ownerId;
    public Account owner;
    public String locationName;

    public static class CarouselMedia {
    	public String type;
    	public ImageUrls imageUrls = new ImageUrls();
    	public VideoUrls videoUrls = new VideoUrls();
    	public int videoViews;
    }

    public static class ImageUrls {
        public String low;
        public String thumbnail;
        public String standard;
        public String high;

        @Override
        public String toString() {
            return "{" +
                    "low='" + low + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", standard='" + standard + '\'' +
                    ", high='" + high + '\'' +
                    '}';
        }
    }

    public static class VideoUrls {
        public String low;
        public String standard;
        public String lowBandwidth;

        @Override
        public String toString() {
            return "{" +
                    "low='" + low + '\'' +
                    ", standard='" + standard + '\'' +
                    ", lowBandwidth='" + lowBandwidth + '\'' +
                    '}';
        }
    }

    public static Media fromApi(Map mediaMap) {
        Media instance = new Media();
        instance.id = (String) mediaMap.get("id");
        instance.createdTime = Long.parseLong((String) mediaMap.get("created_time"));
        fixDate(instance);
        instance.type = (String) mediaMap.get("type");
        instance.link = (String) mediaMap.get("link");
        instance.shortcode = (String) mediaMap.get("code");
        if (mediaMap.get("caption") != null) {
            instance.caption = (String) ((Map) mediaMap.get("caption")).get("text");
        }

        Map images = (Map) mediaMap.get("images");
        fillImageUrls(instance, (String)((Map)images.get("standard_resolution")).get("url"));
        instance.commentsCount = (new Double(((Map) mediaMap.get("comments")).get("count").toString())).intValue();
        instance.likesCount = (new Double(((Map) mediaMap.get("likes")).get("count").toString())).intValue();

        if(instance.type.equals(TYPE_CAROUSEL) && mediaMap.containsKey("carousel_media")){
        	instance.carouselMedia = new ArrayList<CarouselMedia>();
        	for(Map<String, Object> carouselMap : ((List<Map>) mediaMap.get("carousel_media"))){
        		CarouselMedia carouselMedia = new CarouselMedia();
        		carouselMedia.type = (String) carouselMap.get("type");
        		
        		if(carouselMap.containsKey("images")){
        			Map carouselImages = (Map) carouselMap.get("images");
        	        fillCarouselImageUrls(carouselMedia, (String)((Map)carouselImages.get("standard_resolution")).get("url"));
        		}
        		if (carouselMedia.type.equals(TYPE_VIDEO) && carouselMap.containsKey("videos")) {
                    Map carouselVideos = (Map) carouselMap.get("videos");
                    carouselMedia.videoUrls.low = (String) ((Map) carouselVideos.get("low_resolution")).get("url");
                    carouselMedia.videoUrls.standard = (String) ((Map) carouselVideos.get("standard_resolution")).get("url");
                    carouselMedia.videoUrls.lowBandwidth = (String) ((Map) carouselVideos.get("low_bandwidth")).get("url");
                }
        		instance.carouselMedia.add(carouselMedia);
        	}
        }
        if (instance.type.equals(TYPE_VIDEO) && mediaMap.containsKey("videos")) {
            Map videos = (Map) mediaMap.get("videos");
            instance.videoUrls.low = (String) ((Map) videos.get("low_resolution")).get("url");
            instance.videoUrls.standard = (String) ((Map) videos.get("standard_resolution")).get("url");
            instance.videoUrls.lowBandwidth = (String) ((Map) videos.get("low_bandwidth")).get("url");
        }

        instance.previewCommentsList = new ArrayList<Comment>();
        if (instance.commentsCount > 0){
            for (Object o: (List)((Map)mediaMap.get("comments")).get("data")){
                instance.previewCommentsList.add(Comment.fromApi((Map)o));
            }
        }
        instance.owner = Account.fromMediaPage((Map) mediaMap.get("user"));
        return instance;
    }

    private static void fixDate(Media instance) {
        if(instance.createdTime > 0 && instance.createdTime < INSTAGRAM_BORN_YEAR){
            instance.createdTime *= 1000;
        }
    }

    public static Media fromMediaPage(Map pageMap) {
        Media instance = new Media();
        instance.id = (String) pageMap.get("id");
        instance.type = TYPE_IMAGE;
        if ((Boolean) pageMap.get("is_video")) {
            instance.type = TYPE_VIDEO;
            instance.videoUrls.standard = (String) pageMap.get("video_url");
            instance.videoViews = ((Double)pageMap.get("video_view_count")).intValue();
        }
        if(pageMap.containsKey("carousel_media")){
        	instance.type = TYPE_CAROUSEL;
        	instance.carouselMedia = new ArrayList<CarouselMedia>();
        	for(Map<String, Object> carouselMap : ((List<Map>) pageMap.get("carousel_media"))){
        		CarouselMedia carouselMedia = new CarouselMedia();
        		carouselMedia.type = (String) carouselMap.get("type");
        		
        		if(carouselMap.containsKey("images")){
        			Map carouselImages = (Map) carouselMap.get("images");
        	        fillCarouselImageUrls(carouselMedia, (String)((Map)carouselImages.get("standard_resolution")).get("url"));
        		}
        		if (carouselMedia.type.equals(TYPE_VIDEO) && carouselMap.containsKey("videos")) {
                    Map carouselVideos = (Map) carouselMap.get("videos");
                    carouselMedia.videoUrls.low = (String) ((Map) carouselVideos.get("low_resolution")).get("url");
                    carouselMedia.videoUrls.standard = (String) ((Map) carouselVideos.get("standard_resolution")).get("url");
                    carouselMedia.videoUrls.lowBandwidth = (String) ((Map) carouselVideos.get("low_bandwidth")).get("url");
                }
        		instance.carouselMedia.add(carouselMedia);
        	}
        }
        instance.createdTime = ((Double) pageMap.get("taken_at_timestamp")).longValue();
        fixDate(instance);
        instance.shortcode = (String) pageMap.get("shortcode");
        instance.link = INSTAGRAM_URL + "p/" + instance.shortcode;
        instance.commentsCount = ((Double)((Map) pageMap.get("edge_media_to_comment")).get("count")).intValue();
        instance.likesCount = ((Double)((Map) pageMap.get("edge_media_preview_like")).get("count")).intValue();
        fillImageUrls(instance, (String) pageMap.get("display_url"));
        String caption = (String)((Map)((Map)((List)((Map)pageMap.get("edge_media_to_caption")).get("edges")).get(0)).get("node")).get("text");
        if (caption != null) {
            instance.caption = caption;
        }
        instance.owner = Account.fromMediaPage((Map) pageMap.get("owner"));
        return instance;
    }

    public static Media fromTagPage(Map mediaMap) {
        Media instance = new Media();
        instance.shortcode = (String) mediaMap.get("code");
        instance.link = Endpoint.getMediaPageLinkByCode(instance.shortcode);
        instance.commentsCount = ((Double) ((Map) mediaMap.get("comments")).get("count")).intValue();
        instance.likesCount = ((Double) ((Map) mediaMap.get("likes")).get("count")).intValue();
        instance.ownerId = (String) ((Map) mediaMap.get("owner")).get("id");
        if (mediaMap.get("caption") != null) {
            instance.caption = (String) mediaMap.get("caption");
        }
        instance.createdTime = ((Double) mediaMap.get("date")).longValue();
        fixDate(instance);
        fillImageUrls(instance, (String) mediaMap.get("display_src"));
        instance.type = TYPE_IMAGE;
        if ((Boolean) mediaMap.get("is_video")) {
            instance.type = TYPE_VIDEO;
            instance.videoViews = ((Double) mediaMap.get("video_views")).intValue();
        }
        if(mediaMap.containsKey("carousel_media")){
        	instance.type = TYPE_CAROUSEL;
        	instance.carouselMedia = new ArrayList<CarouselMedia>();
        	for(Map<String, Object> carouselMap : ((List<Map>) mediaMap.get("carousel_media"))){
        		CarouselMedia carouselMedia = new CarouselMedia();
        		carouselMedia.type = (String) carouselMap.get("type");
        		
        		if(carouselMap.containsKey("images")){
        			Map carouselImages = (Map) carouselMap.get("images");
        	        fillCarouselImageUrls(carouselMedia, (String)((Map)carouselImages.get("standard_resolution")).get("url"));
        		}
        		if (carouselMedia.type.equals(TYPE_VIDEO) && carouselMap.containsKey("videos")) {
                    Map carouselVideos = (Map) carouselMap.get("videos");
                    carouselMedia.videoUrls.low = (String) ((Map) carouselVideos.get("low_resolution")).get("url");
                    carouselMedia.videoUrls.standard = (String) ((Map) carouselVideos.get("standard_resolution")).get("url");
                    carouselMedia.videoUrls.lowBandwidth = (String) ((Map) carouselVideos.get("low_bandwidth")).get("url");
                }
        		instance.carouselMedia.add(carouselMedia);
        	}
        }
        instance.id = (String) mediaMap.get("id");
        return instance;
    }

    private static void fillImageUrls(final Media instance, String imageUrl) {
        URL url;
        try {
            url = new URL(imageUrl);
            String[] parts = url.getPath().split("/");
            String imageName = parts[parts.length - 1];
            instance.imageUrls.low = Endpoint.INSTAGRAM_CDN_URL + "t/s150x150/" + imageName;
            instance.imageUrls.thumbnail = Endpoint.INSTAGRAM_CDN_URL + "t/s320x320/" + imageName;
            instance.imageUrls.standard = Endpoint.INSTAGRAM_CDN_URL + "t/s640x640/" + imageName;
            instance.imageUrls.high = Endpoint.INSTAGRAM_CDN_URL + "t/" + imageName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    private static void fillCarouselImageUrls(final CarouselMedia instance, String imageUrl) {
        URL url;
        try {
            url = new URL(imageUrl);
            String[] parts = url.getPath().split("/");
            String imageName = parts[parts.length - 1];
            instance.imageUrls.low = Endpoint.INSTAGRAM_CDN_URL + "t/s150x150/" + imageName;
            instance.imageUrls.thumbnail = Endpoint.INSTAGRAM_CDN_URL + "t/s320x320/" + imageName;
            instance.imageUrls.standard = Endpoint.INSTAGRAM_CDN_URL + "t/s640x640/" + imageName;
            instance.imageUrls.high = Endpoint.INSTAGRAM_CDN_URL + "t/" + imageName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
        StringBuilder code = new StringBuilder();
        long longId = Long.parseLong(id);
        while (longId > 0) {
            long index = longId % 64;
            longId = (longId - index) / 64;
            code.insert(0, alphabet.charAt((int) index));
        }
        return code.toString();
    }

    public static String getLinkFromId(String id) {
        String code = Media.getCodeFromId(id);
        return Endpoint.getMediaPageLinkByCode(code);
    }

    @Override
    public String toString() {
        return "Media{" +
                "id='" + id + '\'' +
                ", createdTime=" + createdTime +
                ", type='" + type + '\'' +
                ", link='" + link + '\'' +
                ", imageUrls=" + imageUrls +
                ", caption='" + caption + '\'' +
                ", videoUrls=" + videoUrls +
                ", shortcode='" + shortcode + '\'' +
                ", commentsCount=" + commentsCount +
                ", previewCommentsList=" + previewCommentsList +
                ", likesCount=" + likesCount +
                ", videoViews=" + videoViews +
                ", ownerId='" + ownerId + '\'' +
                ", owner=" + owner +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
