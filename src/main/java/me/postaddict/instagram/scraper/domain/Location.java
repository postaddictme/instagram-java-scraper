package me.postaddict.instagram.scraper.domain;

import java.util.Map;

public class Location {
    public long id;
    public Boolean hasPublicPage;
    public String name;
    public String slug;
    public Double lat;
    public Double lng;

    public static Location fromLocationMedias(Map pageMap) {
        Location location = new Location();
        location.id = Long.parseLong((String) pageMap.get("id"));
        location.hasPublicPage = (Boolean) pageMap.get("has_public_page");
        location.name = (String) pageMap.get("name");
        location.slug = (String) pageMap.get("slug");
        location.lat = (Double) pageMap.get("lat");
        location.lng = (Double) pageMap.get("lat");
        return location;
    }
}
