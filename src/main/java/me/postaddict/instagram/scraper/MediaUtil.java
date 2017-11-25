package me.postaddict.instagram.scraper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MediaUtil {

    public static final long INSTAGRAM_BORN_YEAR = 1262304000000L;

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
}
