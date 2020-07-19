package me.postaddict.instagram.scraper;

import me.postaddict.instagram.scraper.client.InstaClient;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.ActionResponse;
import me.postaddict.instagram.scraper.model.ActivityFeed;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.Location;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;
import me.postaddict.instagram.scraper.request.GetCommentsByMediaCode;
import me.postaddict.instagram.scraper.request.GetFollowersRequest;
import me.postaddict.instagram.scraper.request.GetFollowsRequest;
import me.postaddict.instagram.scraper.request.GetLocationRequest;
import me.postaddict.instagram.scraper.request.GetMediaByTagRequest;
import me.postaddict.instagram.scraper.request.GetMediaLikesRequest;
import me.postaddict.instagram.scraper.request.GetMediasRequest;
import me.postaddict.instagram.scraper.request.GetUserMediaRequest;
import me.postaddict.instagram.scraper.request.parameters.LocationParameter;
import me.postaddict.instagram.scraper.request.parameters.MediaCode;
import me.postaddict.instagram.scraper.request.parameters.TagName;
import me.postaddict.instagram.scraper.request.parameters.UserMediaListParameter;
import me.postaddict.instagram.scraper.request.parameters.UserParameter;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


//@AllArgsConstructor
public class Instagram extends AuthenticatedInsta {

    public static final int MAX_USER_MEDIA_PAGE_SIZE = 50;
    // TODO: 14.06.2020: p.saharchuk: move to InstaClient and private
//    protected final OkHttpClient httpClient;
//    protected final Mapper mapper;
//    protected final DelayHandler delayHandler;
//    protected OkHttpClient httpClient;
    //    protected String csrf_token;
//    protected String rollout_hash;
//    protected Mapper mapper;
    private static final PageInfo FIRST_PAGE = new PageInfo(true, "");

    public Instagram(InstaClient instaClient) {
        super(instaClient);
    }

    // TODO: 14.06.2020: p.saharchuk: delete
//    public Instagram(OkHttpClient httpClient) {
//        this(httpClient, new ModelMapper(), new DefaultDelayHandler(),"","");
//    }

    public void basePage() throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.BASE_URL)
                .build();

        Response response = executeHttpRequest(request);
        try (ResponseBody responseBody = response.body()) {
            String body = IOUtils.toString(responseBody.byteStream(), StandardCharsets.UTF_8);

            getCSRFToken(body);
            getRolloutHash(body);
        }
    }

    public void login(String username, String encPassword) throws IOException {
        if (username == null || encPassword == null) {
            throw new InstagramAuthException("Specify username and enc_password");
        } else if (instaClient.getCsrfToken().isEmpty()) {
            throw new NullPointerException("Please run before base()");
        }

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("enc_password", encPassword)
                .add("queryParams", "{}")
                .add("optIntoOneTap", "true")
                .build();

        Request request = new Request.Builder()
                .url(Endpoint.LOGIN_URL)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/accounts/login/")
                .post(formBody)
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        try (InputStream jsonStream = response.body().byteStream()) {
            if (!mapper.isAuthenticated(jsonStream)) {
                throw new InstagramAuthException("Credentials rejected by instagram", ErrorType.UNAUTHORIZED);
            }
        }
    }

    @Override
    public Account getAccountById(long id) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByAccountId(id))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
        Response response = executeHttpRequest(withCsrfToken(request));
        try (InputStream jsonStream = response.body().byteStream()) {
            return getAccountByUsername(getMediaByCode(mapper.getLastMediaShortCode(jsonStream)).getOwner().getUsername());
        }
    }

    /**
     * Return 24 first posts by user id - it is first user page size
     *
     * @param userId - user id
     * @return PageObject<Media> with media list
     * @throws IOException
     */
    public PageObject<Media> getMediaByUserId(long userId) throws IOException {
        return getMediaByUserId(userId, 24);
    }

    /**
     * @param userId        - account userId
     * @param mediaListSize - posts count.
     * @return PageObject<Media> with media list
     * @throws IOException
     */
    public PageObject<Media> getMediaByUserId(long userId, long mediaListSize) throws IOException {
        long pageCount = (long) Math.ceil((double) mediaListSize / MAX_USER_MEDIA_PAGE_SIZE);
        int lastPageSize = (int) mediaListSize % MAX_USER_MEDIA_PAGE_SIZE;
        lastPageSize = lastPageSize == 0 && mediaListSize > 0 ? MAX_USER_MEDIA_PAGE_SIZE : lastPageSize;
        GetUserMediaRequest getMediasRequest = new GetUserMediaRequest(instaClient, mapper, delayHandler);
        return getMediasRequest
                .requestInstagramResult(new UserMediaListParameter(userId, pageCount, lastPageSize), pageCount, FIRST_PAGE);
    }

    public Account getAccountByUsername(String username) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountId(username))
                .build();
        Response response = executeHttpRequest(request);
        try (InputStream jsonStream = response.body().byteStream()) {
            return mapper.mapAccount(jsonStream);
        }
    }

    public PageObject<Media> getMedias(String username, int pageCount) throws IOException {
        long userId = getAccountByUsername(username).getId();
        return getMedias(userId, pageCount, FIRST_PAGE);
    }

    public PageObject<Media> getMedias(long userId, int pageCount, PageInfo pageCursor) throws IOException {
        GetMediasRequest getMediasRequest = new GetMediasRequest(instaClient, mapper, delayHandler);
        return getMediasRequest.requestInstagramResult(new UserParameter(userId), pageCount, pageCursor);
    }

    public Media getMediaByUrl(String url) throws IOException {
        String urlRegexp = Endpoint.getMediaPageLinkByCodeMatcher();
        if (url == null || !url.matches(urlRegexp)) {
            throw new IllegalArgumentException("Media URL not matches regexp: " + urlRegexp + " current value: " + url);
        }
        Request request = new Request.Builder()
                .url(url + "/?__a=1")
                .build();

        Response response = executeHttpRequest(request);
        try (InputStream jsonStream = response.body().byteStream()) {
            return mapper.mapMedia(jsonStream);
        }
    }

    public Media getMediaByCode(String code) throws IOException {
        return getMediaByUrl(Endpoint.getMediaPageLinkByCode(code));
    }

    public Location getLocationMediasById(String locationId, int pageCount) throws IOException {
        GetLocationRequest getLocationRequest = new GetLocationRequest(instaClient, mapper, delayHandler);
        return getLocationRequest.requestInstagramResult(new LocationParameter(locationId), pageCount, FIRST_PAGE);
    }

    public Tag getMediasByTag(String tag) throws IOException {
        return getMediasByTag(tag, 1);
    }

    public Tag getMediasByTag(String tag, int pageCount) throws IOException {
        validateTagName(tag);
        GetMediaByTagRequest getMediaByTagRequest = new GetMediaByTagRequest(instaClient, mapper, delayHandler);
        return getMediaByTagRequest.requestInstagramResult(new TagName(tag), pageCount, FIRST_PAGE);
    }

    public PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException {
        GetCommentsByMediaCode getCommentsByMediaCode = new GetCommentsByMediaCode(instaClient, mapper, delayHandler);
        return getCommentsByMediaCode.requestInstagramResult(new MediaCode(code), pageCount,
                new PageInfo(true, "0"));
    }

    public void likeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaLikeLink(MediaUtil.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        response.body().close();
    }

    public void followAccountByUsername(String username) throws IOException {
        Account account = getAccountByUsername(username);
        followAccount(account.getId());
    }

    public void followAccount(long userId) throws IOException {
        String url = Endpoint.getFollowAccountLink(userId);
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .post(new FormBody.Builder().build())
                .build();
        Response response = executeHttpRequest(withCsrfToken(request));
        response.body().close();
    }

    public void unfollowAccountByUsername(String username) throws IOException {
        Account account = getAccountByUsername(username);
        unfollowAccount(account.getId());
    }

    public void unfollowAccount(long userId) throws IOException {
        String url = Endpoint.getUnfollowAccountLink(userId);
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .post(new FormBody.Builder().build())
                .build();
        Response response = executeHttpRequest(withCsrfToken(request));
        response.body().close();
    }

    public PageObject<Account> getMediaLikes(String shortcode, int pageCount) throws IOException {
        GetMediaLikesRequest getMediaLikesRequest = new GetMediaLikesRequest(instaClient, mapper, delayHandler);
        return getMediaLikesRequest.requestInstagramResult(new MediaCode(shortcode), pageCount, FIRST_PAGE);
    }

    public PageObject<Account> getFollows(long userId, int pageCount) throws IOException {
        GetFollowsRequest getFollowsRequest = new GetFollowsRequest(instaClient, mapper, delayHandler);
        return getFollowsRequest.requestInstagramResult(new UserParameter(userId), pageCount, FIRST_PAGE);
    }

    public PageObject<Account> getFollowers(long userId, int pageCount) throws IOException {
        GetFollowersRequest getFollowersRequest = new GetFollowersRequest(instaClient, mapper, delayHandler);
        return getFollowersRequest.requestInstagramResult(new UserParameter(userId), pageCount, FIRST_PAGE);
    }

    public void unlikeMediaByCode(String code) throws IOException {
        String url = Endpoint.getMediaUnlikeLink(MediaUtil.getIdFromCode(code));
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        response.body().close();
    }

    public ActionResponse<Comment> addMediaComment(String code, String commentText) throws IOException {
        String url = Endpoint.addMediaCommentLink(MediaUtil.getIdFromCode(code));
        FormBody formBody = new FormBody.Builder()
                .add("comment_text", commentText)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(formBody)
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        try (InputStream jsonStream = response.body().byteStream()) {
            return mapper.mapMediaCommentResponse(jsonStream);
        }
    }

    public void deleteMediaComment(String code, String commentId) throws IOException {
        String url = Endpoint.deleteMediaCommentLink(MediaUtil.getIdFromCode(code), commentId);
        Request request = new Request.Builder()
                .url(url)
                .header(Endpoint.REFERER, Endpoint.getMediaPageLinkByCode(code) + "/")
                .post(new FormBody.Builder().build())
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        response.body().close();
    }

    @Override
    public ActivityFeed getActivityFeed() throws IOException {

        Request request = new Request.Builder()
                .url(Endpoint.ACTIVITY_FEED)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        try (InputStream jsonStream = response.body().byteStream()) {
            ActivityFeed activityFeed = mapper.mapActivity(jsonStream);
            markActivityChecked(activityFeed);
            return activityFeed;
        }

    }

    private void markActivityChecked(ActivityFeed activityFeed) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.ACTIVITY_MARK_CHECKED)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .post(new FormBody.Builder().add("timestamp", activityFeed.getTimestamp()).build())
                .build();
        try (ResponseBody response = executeHttpRequest(withCsrfToken(request)).body()) {
            //skip
        }
    }

    private void validateTagName(String tag) {
        if (tag == null || tag.isEmpty() || tag.startsWith("#")) {
            throw new IllegalArgumentException("Please provide non empty tag name that not starts with #");
        }
    }

    @Override
    public Long getLoginUserId() {
        for (Cookie cookie : this.instaClient.getHttpClient().cookieJar().loadForRequest(HttpUrl.parse(Endpoint.BASE_URL))) {
            if ("ds_user_id".equals(cookie.name())) {
                return Long.parseLong(cookie.value());
            }
        }
        return null;
    }
}
