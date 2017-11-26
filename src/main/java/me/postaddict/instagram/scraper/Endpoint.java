package me.postaddict.instagram.scraper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoint {
    public static final String REFERER = "Referer";
    public static final String BASE_URL = "https://www.instagram.com";
    public static final String LOGIN_URL = "https://www.instagram.com/accounts/login/ajax/";
    public static final String ACCOUNT_PAGE = "https://www.instagram.com/{{username}}";
    public static final String MEDIA_LINK = "https://www.instagram.com/p/{{code}}";
    public static final String ACCOUNT_MEDIAS = "https://www.instagram.com/{{username}}/?__a=1&max_id={{maxId}}";
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
    public static final String USERNAME = "{{username}}";
    public static final String USER_ID = "{{userId}}";
    public static final String MAX_ID = "{{maxId}}";
    public static final String CODE = "{{code}}";
    public static final String TAG_NAME = "{{tag_name}}";
    public static final String COUNT = "{{count}}";
    public static final String SHORTCODE = "{{shortcode}}";
    public static final String COMMENT_ID = "{{commentId}}";
    public static final String MEDIA_ID = "{{mediaId}}";
    public static final String END_CURSOR = "{{endCursor}}";
    public static final String TAG = "{{tag}}";
    public static final String QUERY = "{{query}}";
    public static final String FACEBOOK_LOCATION_ID = "{{facebookLocationId}}";

    public static String getAccountPageLink(String username) {
        return ACCOUNT_PAGE.replace(USERNAME, username);
    }

    public static String getAccountJsonInfoLinkByAccountId(long userId) {
        return ACCOUNT_JSON_INFO_BY_ID.replace(USER_ID, "" + userId);
    }

    public static String getAccountMediasJsonLink(String username, String maxId) {
        if (maxId == null) {
            maxId = "";
        }
        return ACCOUNT_MEDIAS.replace(USERNAME, username).replace(MAX_ID, maxId);
    }

    public static String getTagJsonByTagName(String tagName) {
        return TAG_JSON_INFO.replace(TAG_NAME, tagName);
    }

    public static String getMediaPageLinkByCode(String code) {
        return MEDIA_LINK.replace(CODE, code);
    }

    public static String getMediaJsonLinkByShortcode(String shortcode) {
        return MEDIA_JSON_INFO.replace(CODE, shortcode);
    }

    public static String getMediasJsonByLocationIdLink(String facebookLocationId, String maxId) {
        if (maxId == null) {
            maxId = "";
        }
        return MEDIA_JSON_BY_LOCATION_ID.replace(FACEBOOK_LOCATION_ID, facebookLocationId).replace(MAX_ID, maxId);
    }

    public static String getMediasJsonByTagLink(String tag, String maxId) {
        if (maxId == null) {
            maxId = "";
        }
        return MEDIA_JSON_BY_TAG.replace(TAG, tag).replace(MAX_ID, maxId);
    }

    public static String getGeneralSearchJsonLink(String query) {
        return GENERAL_SEARCH.replace(QUERY, query);
    }

    public static String getLastCommentsByCodeLink(String code, int count) {
        return LAST_COMMENTS_BY_CODE
                .replace(CODE, code)
                .replace(COUNT, "" + count);
    }

    public static String getCommentsBeforeCommentIdByCode(String shortcode, int count, String commentId) {
        return COMMENTS_BEFORE_COMMENT_ID_BY_CODE
                .replace(SHORTCODE, shortcode)
                .replace(COUNT, "" + count)
                .replace(COMMENT_ID, commentId);
    }

    public static String getMediaLikeLink(String mediaId) {
        return MEDIA_LIKE.replace(MEDIA_ID, mediaId);
    }

    public static String getMediaUnlikeLink(String mediaId) {
        return MEDIA_UNLIKE.replace(MEDIA_ID, mediaId);
    }

    public static String addMediaCommentLink(String mediaId) {
        return MEDIA_COMMENTS_ADD.replace(MEDIA_ID, mediaId);
    }

    public static String deleteMediaCommentLink(String mediaId, String commentId) {
        return MEDIA_COMMENTS_DELETE
                .replace(MEDIA_ID, mediaId)
                .replace(COMMENT_ID, commentId);
    }

    public static String getFollowsLinkVariables(long userId, int count, String endCursor) {
        return FOLLOWS_URL
                .replace(USER_ID, String.valueOf(userId))
                .replace(COUNT, String.valueOf(count))
                .replace(END_CURSOR, endCursor);
    }

    public static String getFollowersLinkVariables(long userId, int count, String endCursor) {
        return FOLLOWERS_URL
                .replace(USER_ID, String.valueOf(userId))
                .replace(COUNT, String.valueOf(count))
                .replace(END_CURSOR, endCursor);
    }
}
