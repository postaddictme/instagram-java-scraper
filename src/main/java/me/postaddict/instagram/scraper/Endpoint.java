package me.postaddict.instagram.scraper;

public abstract class Endpoint {
    public static final String BASE_URL = "https://www.instagram.com";
    public static final String LOGIN_URL = "https://www.instagram.com/accounts/login/ajax/";
    public static final String ACCOUNT_PAGE = "https://www.instagram.com/{{username}}";
    public static final String MEDIA_LINK = "https://www.instagram.com/p/{{code}}";
    public static final String ACCOUNT_MEDIAS = "https://www.instagram.com/{{username}}/?__a=1&max_id={{maxId}}";
    public static final String ACCOUNT_JSON_INFO = "https://www.instagram.com/{{username}}/?__a=1";
    public static final String TAG_JSON_INFO = "https://www.instagram.com/explore/tags/{{tag_name}}/?__a=1";
    public static final String MEDIA_JSON_INFO = "https://www.instagram.com/p/{{code}}/?__a=1";
    public static final String MEDIA_JSON_BY_LOCATION_ID = "https://www.instagram.com/explore/locations/{{facebookLocationId}}/?__a=1&max_id={{maxId}}";
    public static final String MEDIA_JSON_BY_TAG = "https://www.instagram.com/explore/tags/{{tag}}/?__a=1&max_id={{maxId}}";
    public static final String GENERAL_SEARCH = "https://www.instagram.com/web/search/topsearch/?query={{query}}";
    public static final String ACCOUNT_JSON_INFO_BY_ID = "https://www.instagram.com/graphql/query/?query_id=17880160963012870&id={{userId}}&first=1";
    public static final String LAST_COMMENTS_BY_CODE = "ig_shortcode({{code}}){comments.last({{count}}){count,nodes{id,created_at,text,user{id,profile_pic_url,username,follows{count},followed_by{count},biography,full_name,media{count},is_private,external_url,is_verified}},page_info}}";
    public static final String COMMENTS_BEFORE_COMMENT_ID_BY_CODE = "https://www.instagram.com/graphql/query/?query_id=17852405266163336&shortcode={{shortcode}}&first={{count}}&after={{commentId}}";
    public static final String MEDIA_LIKE = "https://www.instagram.com/web/likes/{{mediaId}}/like/";
    public static final String MEDIA_UNLIKE = "https://www.instagram.com/web/likes/{{mediaId}}/unlike/";
    public static final String MEDIA_COMMENTS_ADD = "https://www.instagram.com/web/comments/{{mediaId}}/add/";
    public static final String MEDIA_COMMENTS_DELETE = "https://www.instagram.com/web/comments/{{mediaId}}/delete/{{commentId}}/";
    public static final String FOLLOWS_URL = "https://www.instagram.com/graphql/query/?query_id=17874545323001329&variables={\"id\": {{userId}}, \"first\": {{count}}, \"after\": \"{{endCursor}}\"}";
    public static final String FOLLOWERS_URL = "https://www.instagram.com/graphql/query/?query_id=17851374694183129&variables={\"id\": {{userId}}, \"first\": {{count}}, \"after\": \"{{endCursor}}\"}";
    public static final String INSTAGRAM_QUERY_URL = "https://www.instagram.com/query/";
    public static final String INSTAGRAM_CDN_URL = "https://scontent.cdninstagram.com/";

    public static String getAccountPageLink(String username) {
        return ACCOUNT_PAGE.replace("{{username}}", username);
    }

    public static String getAccountJsonInfoLinkByUsername(String username) {
        return ACCOUNT_JSON_INFO.replace("{{username}}", username);
    }

    public static String getAccountJsonInfoLinkByAccountId(long userId) {
        return ACCOUNT_JSON_INFO_BY_ID.replace("{{userId}}", "" + userId);
    }

    public static String getAccountMediasJsonLink(String username, String maxId) {
        if (maxId == null) {
            maxId = "";
        }
        return ACCOUNT_MEDIAS.replace("{{username}}", username).replace("{{maxId}}", maxId);
    }

    public static String getTagJsonByTagName(String tagName) {
        return TAG_JSON_INFO.replace("{{tag_name}}", tagName);
    }

    public static String getMediaPageLinkByCode(String code) {
        return MEDIA_LINK.replace("{{code}}", code);
    }

    public static String getMediaJsonLinkByShortcode(String shortcode) {
        return MEDIA_JSON_INFO.replace("{{code}}", shortcode);
    }

    public static String getMediasJsonByLocationIdLink(String facebookLocationId, String maxId) {
        if (maxId == null) {
            maxId = "";
        }
        return MEDIA_JSON_BY_LOCATION_ID.replace("{{facebookLocationId}}", facebookLocationId).replace("{{maxId}}", maxId);
    }

    public static String getMediasJsonByTagLink(String tag, String maxId) {
        if (maxId == null) {
            maxId = "";
        }
        return MEDIA_JSON_BY_TAG.replace("{{tag}}", tag).replace("{{maxId}}", maxId);
    }

    public static String getGeneralSearchJsonLink(String query) {
        return GENERAL_SEARCH.replace("{{query}}", query);
    }

    public static String getLastCommentsByCodeLink(String code, int count) {
        return LAST_COMMENTS_BY_CODE
                .replace("{{code}}", code)
                .replace("{{count}}", "" + count);
    }

    public static String getCommentsBeforeCommentIdByCode(String shortcode, int count, String commentId) {
        return COMMENTS_BEFORE_COMMENT_ID_BY_CODE
                .replace("{{shortcode}}", shortcode)
                .replace("{{count}}", "" + count)
                .replace("{{commentId}}", commentId);
    }

    public static String getMediaLikeLink(String mediaId) {
        return MEDIA_LIKE.replace("{{mediaId}}", mediaId);
    }

    public static String getMediaUnlikeLink(String mediaId) {
        return MEDIA_UNLIKE.replace("{{mediaId}}", mediaId);
    }

    public static String addMediaCommentLink(String mediaId) {
        return MEDIA_COMMENTS_ADD.replace("{{mediaId}}", mediaId);
    }

    public static String deleteMediaCommentLink(String mediaId, String commentId) {
        return MEDIA_COMMENTS_DELETE
                .replace("{{mediaId}}", mediaId)
                .replace("{{commentId}}", commentId);
    }

    public static String getFollowsLinkVariables(long userId, int count, String endCursor) {
        return FOLLOWS_URL
                .replace("{{userId}}", String.valueOf(userId))
                .replace("{{count}}", String.valueOf(count))
                .replace("{{endCursor}}", endCursor);
    }

    public static String getFollowersLinkVariables(long userId, int count, String endCursor) {
        return FOLLOWERS_URL
                .replace("{{userId}}", String.valueOf(userId))
                .replace("{{count}}", String.valueOf(count))
                .replace("{{endCursor}}", endCursor);
    }
}
