package me.postaddict.instagram.scraper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class Utils {

    private Utils() {
    }

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.of(TimeZone.getDefault().getID()));
    }

}
