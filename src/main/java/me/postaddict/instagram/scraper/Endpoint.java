package me.postaddict.instagram.scraper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoint {
    public static final String REFERER = "Referer";
    public static final String BASE_URL = "https://www.instagram.com";
    protected static final String ACTIVITY_FEED = "https://www.instagram.com/accounts/activity/?__a=1";
    protected static final String ACTIVITY_MARK_CHECKED = "https://www.instagram.com/web/activity/mark_checked/";
    protected static final String LOGIN_URL = "https://www.instagram.com/accounts/login/ajax/";
    private static final String MEDIA_LINK = "https://www.instagram.com/p/{{code}}";
    private static final String ACCOUNT_JSON_INFO = "https://www.instagram.com/{{username}}/?__a=1";
    /**
     * @deprecated use USER_MEDIA_JSON_BY_USER_ID, because this class hardcode 30 posts
     */
    @Deprecated
    private static final String ACCOUNT_MEDIAS = "https://instagram.com/graphql/query/?query_id=17888483320059182&id={{userId}}&first=30&after={{maxId}}";
    private static final String USER_MEDIA_JSON_BY_USER_ID = "https://instagram.com/graphql/query/?query_id=17888483320059182&id={{userId}}&first={{mediaListSize}}&after={{startPageHash}}";
    private static final String TAG_JSON_INFO = "https://www.instagram.com/explore/tags/{{tag_name}}/?__a=1";
    private static final String MEDIA_JSON_INFO = "https://www.instagram.com/p/{{code}}/?__a=1";
    private static final String MEDIA_JSON_BY_LOCATION_ID = "https://www.instagram.com/explore/locations/{{facebookLocationId}}/?__a=1&max_id={{maxId}}";
    private static final String MEDIA_JSON_BY_TAG = "https://www.instagram.com/explore/tags/{{tag}}/?__a=1&max_id={{maxId}}";
    private static final String GENERAL_SEARCH = "https://www.instagram.com/web/search/topsearch/?query={{query}}";
    private static final String ACCOUNT_JSON_INFO_BY_ID = "https://www.instagram.com/graphql/query/?query_id=17880160963012870&id={{userId}}&first=1";
    private static final String LAST_COMMENTS_BY_CODE = "ig_shortcode({{code}}){comments.last({{count}}){count,nodes{id,created_at,text,user{id,profile_pic_url,username,follows{count},followed_by{count},biography,full_name,media{count},is_private,external_url,is_verified}},page_info}}";
    private static final String COMMENTS_BEFORE_COMMENT_ID_BY_CODE = "https://www.instagram.com/graphql/query/?query_id=17852405266163336&shortcode={{shortcode}}&first={{count}}&after={{commentId}}";
    private static final String MEDIA_LIKE = "https://www.instagram.com/web/likes/{{mediaId}}/like/";
    private static final String MEDIA_UNLIKE = "https://www.instagram.com/web/likes/{{mediaId}}/unlike/";
    private static final String MEDIA_COMMENTS_ADD = "https://www.instagram.com/web/comments/{{mediaId}}/add/";
    private static final String MEDIA_COMMENTS_DELETE = "https://www.instagram.com/web/comments/{{mediaId}}/delete/{{commentId}}/";
    private static final String LIKES_BY_SHORTCODE = "https://www.instagram.com/graphql/query/?query_id=17864450716183058&variables={\"shortcode\":\"{{shortcode}}\",\"first\":{{count}},\"after\":\"{{after}}\"}";
    private static final String FOLLOWS_URL = "https://www.instagram.com/graphql/query/?query_id=17874545323001329&variables={\"id\": {{userId}}, \"first\": {{count}}, \"after\": \"{{endCursor}}\"}";
    private static final String FOLLOWERS_URL = "https://www.instagram.com/graphql/query/?query_id=17851374694183129&variables={\"id\": {{userId}}, \"first\": {{count}}, \"after\": \"{{endCursor}}\"}";
    private static final String FOLLOW_ACCOUNT = "https://www.instagram.com/web/friendships/{{userId}}/follow/";
    private static final String UNFOLLOW_ACCOUNT = "https://www.instagram.com/web/friendships/{{userId}}/unfollow/";
    private static final String USERNAME = "{{username}}";
    private static final String USER_ID = "{{userId}}";
    private static final String MAX_ID = "{{maxId}}";
    private static final String CODE = "{{code}}";
    private static final String TAG_NAME = "{{tag_name}}";
    private static final String COUNT = "{{count}}";
    private static final String AFTER = "{{after}}";
    private static final String SHORTCODE = "{{shortcode}}";
    private static final String COMMENT_ID = "{{commentId}}";
    private static final String MEDIA_ID = "{{mediaId}}";
    private static final String END_CURSOR = "{{endCursor}}";
    private static final String TAG = "{{tag}}";
    private static final String QUERY = "{{query}}";
    private static final String FACEBOOK_LOCATION_ID = "{{facebookLocationId}}";
    private static final String MEDIA_LIST_SIZE = "{{mediaListSize}}";
    private static final String START_PAGE_HASH = "{{startPageHash}}";


    public static String getAccountId(String username) {
        return ACCOUNT_JSON_INFO.replace(USERNAME, username);
    }

    public static String getAccountJsonInfoLinkByAccountId(long userId) {
        return ACCOUNT_JSON_INFO_BY_ID.replace(USER_ID, "" + userId);
    }

    public static String getUserMediaJsonByUserId(long userId, int mediaListSize, String startPageHash) {
        startPageHash = startPageHash == null ? "" : startPageHash;
        return USER_MEDIA_JSON_BY_USER_ID
                .replace(USER_ID, String.valueOf(userId))
                .replace(MEDIA_LIST_SIZE, String.valueOf(mediaListSize))
                .replace(START_PAGE_HASH, startPageHash);
    }

    /**
     * @deprecated use getUserMediaJsonByUserId, because this class hardcode 30 posts
     */
    @Deprecated
    public static String getAccountMediasJsonLink(long userId, String maxId) {
        if (maxId == null) {
            maxId = "";
        }
        return ACCOUNT_MEDIAS.replace(USER_ID, Long.toString(userId)).replace(MAX_ID, maxId);
    }

    public static String getTagJsonByTagName(String tagName) {
        return TAG_JSON_INFO.replace(TAG_NAME, tagName);
    }

    public static String getMediaPageLinkByCode(String code) {
        return MEDIA_LINK.replace(CODE, code);
    }

    public static String getMediaPageLinkByCodeMatcher() {
        return MEDIA_LINK.replace(CODE, "[\\w-_]+");
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

    public static String getLikesByShortcode(String shortcode, int count, String endCursor){
        return LIKES_BY_SHORTCODE
                    .replace(SHORTCODE, shortcode)
                    .replace(COUNT, String.valueOf(count))
                    .replace(AFTER, endCursor);
    }

    public static String getFollowAccountLink(long userId) {
        return FOLLOW_ACCOUNT
                .replace(USER_ID, String.valueOf(userId));
    }
    
    public static String getUnfollowAccountLink(long userId) {
        return UNFOLLOW_ACCOUNT
                .replace(USER_ID, String.valueOf(userId));
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
