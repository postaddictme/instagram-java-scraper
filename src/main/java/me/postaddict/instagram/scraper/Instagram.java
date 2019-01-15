package me.postaddict.instagram.scraper;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.AllArgsConstructor;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.mapper.Mapper;
import me.postaddict.instagram.scraper.mapper.ModelMapper;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.ActionResponse;
import me.postaddict.instagram.scraper.model.ActivityFeed;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.Location;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;
import me.postaddict.instagram.scraper.request.DefaultDelayHandler;
import me.postaddict.instagram.scraper.request.DelayHandler;
import me.postaddict.instagram.scraper.request.GetCommentsByMediaCode;
import me.postaddict.instagram.scraper.request.GetFollowersRequest;
import me.postaddict.instagram.scraper.request.GetFollowsRequest;
import me.postaddict.instagram.scraper.request.GetLocationRequest;
import me.postaddict.instagram.scraper.request.GetMediaByTagRequest;
import me.postaddict.instagram.scraper.request.GetMediaLikesRequest;
import me.postaddict.instagram.scraper.request.GetMediasRequest;
import me.postaddict.instagram.scraper.request.parameters.LocationParameter;
import me.postaddict.instagram.scraper.request.parameters.MediaCode;
import me.postaddict.instagram.scraper.request.parameters.TagName;
import me.postaddict.instagram.scraper.request.parameters.UserParameter;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@AllArgsConstructor
public class Instagram implements AuthenticatedInsta {

    private static final PageInfo FIRST_PAGE = new PageInfo(true, "");
    protected final OkHttpClient httpClient;
    protected final Mapper mapper;
    protected final DelayHandler delayHandler;
    protected String csrf_token;
    protected String rollout_hash;

    public Instagram(OkHttpClient httpClient) {
        this(httpClient, new ModelMapper(), new DefaultDelayHandler(),"","");
    }

    protected Request withCsrfToken(Request request) {
    	return request.newBuilder()
              .addHeader("X-CSRFToken", csrf_token)
              .addHeader("X-Instagram-AJAX", (rollout_hash.isEmpty() ? "1" : rollout_hash))
              .build();
    }
    
    public void basePage() throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.BASE_URL)
                .build();

        Response response = executeHttpRequest(request);
        try (ResponseBody body = response.body()){
        	if(this.csrf_token.isEmpty())
        		getCSRFToken(body);
        	else if(this.rollout_hash.isEmpty())
        		getRolloutHash(body);
        }
    }
    
    private void getCSRFToken(ResponseBody body) throws IOException {
    	this.csrf_token=getToken("\"csrf_token\":\"",32,body.byteStream());
    }
    
    private void getRolloutHash(ResponseBody body){
    	try {
			this.rollout_hash=getToken("\"rollout_hash\":\"",12,body.byteStream());
		} catch (IOException e) {
			this.rollout_hash="1";
		}
    }
    
    private String getToken(String seek, int length ,InputStream stream) throws IOException {
		DataInputStream in = new DataInputStream(stream);
		
		String line;
		while((line = in.readLine())!=null) {
			int index = line.indexOf(seek);
			if(index != -1) {
				return line.substring(index+seek.length(),index+seek.length()+length);
			}
		}
		throw new NullPointerException("Couldn't find "+seek);
	}

    public void login(String username, String password) throws IOException {
        if (username == null || password == null) {
            throw new InstagramAuthException("Specify username and password");
        }else if(this.csrf_token.isEmpty()) {
        	throw new NullPointerException("Please run before base()");
        }

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Endpoint.LOGIN_URL)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .post(formBody)
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        try(InputStream jsonStream = response.body().byteStream()) {
            if(!mapper.isAuthenticated(jsonStream)){
                throw new InstagramAuthException("Credentials rejected by instagram");
            }
        }
    }

    public Account getAccountById(long id) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountJsonInfoLinkByAccountId(id))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
        Response response = executeHttpRequest(withCsrfToken(request));
        try(InputStream jsonStream = response.body().byteStream()) {
            return getMediaByCode(mapper.getLastMediaShortCode(jsonStream)).getOwner();
        }
    }

    public Account getAccountByUsername(String username) throws IOException {
        Request request = new Request.Builder()
                .url(Endpoint.getAccountId(username))
                .build();
        Response response = executeHttpRequest(request);
        try(InputStream jsonStream = response.body().byteStream()) {
            return mapper.mapAccount(jsonStream);
        }
    }

    public PageObject<Media> getMedias(String username, int pageCount) throws IOException {
        long userId = getAccountByUsername(username).getId();
        return getMedias(userId, pageCount, FIRST_PAGE);
    }

    public PageObject<Media> getMedias(long userId, int pageCount, PageInfo pageCursor) throws IOException {
        GetMediasRequest getMediasRequest = new GetMediasRequest(httpClient, mapper, delayHandler);
        return getMediasRequest.requestInstagramResult(new UserParameter(userId), pageCount, pageCursor);
    }

    public Media getMediaByUrl(String url) throws IOException {
        String urlRegexp = Endpoint.getMediaPageLinkByCodeMatcher();
        if(url==null || !url.matches(urlRegexp)){
            throw new IllegalArgumentException("Media URL not matches regexp: "+urlRegexp+" current value: "+url);
        }
        Request request = new Request.Builder()
                .url(url + "/?__a=1")
                .build();

        Response response = executeHttpRequest(request);
        try(InputStream jsonStream = response.body().byteStream()) {
            return mapper.mapMedia(jsonStream);
        }
    }

    public Media getMediaByCode(String code) throws IOException {
        return getMediaByUrl(Endpoint.getMediaPageLinkByCode(code));
    }

    public Tag getTagByName(String tagName) throws IOException {
        validateTagName(tagName);
        Request request = new Request.Builder()
                .url(Endpoint.getTagJsonByTagName(tagName))
                .build();

        Response response = executeHttpRequest(request);
        try(InputStream jsonStream = response.body().byteStream()) {
            return mapper.mapTag(jsonStream);
        }

    }

    public Location getLocationMediasById(String locationId, int pageCount) throws IOException {
        GetLocationRequest getLocationRequest = new GetLocationRequest(httpClient, mapper, delayHandler);
        return getLocationRequest.requestInstagramResult(new LocationParameter(locationId), pageCount, FIRST_PAGE);
    }

    public Tag getMediasByTag(String tag, int pageCount) throws IOException {
        validateTagName(tag);
        GetMediaByTagRequest getMediaByTagRequest = new GetMediaByTagRequest(httpClient, mapper, delayHandler);
        return getMediaByTagRequest.requestInstagramResult(new TagName(tag), pageCount, FIRST_PAGE);
    }

    public PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException {
        GetCommentsByMediaCode getCommentsByMediaCode = new GetCommentsByMediaCode(httpClient, mapper, delayHandler);
        return getCommentsByMediaCode.requestInstagramResult(new MediaCode(code), pageCount,
                    new PageInfo(true,"0"));
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

    public void followAccountByUsername(String username) throws IOException{
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

    public void unfollowAccountByUsername(String username) throws IOException{
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

    public PageObject<Account> getMediaLikes(String shortcode, int pageCount) throws IOException{
        GetMediaLikesRequest getMediaLikesRequest = new GetMediaLikesRequest(httpClient, mapper, delayHandler);
        return getMediaLikesRequest.requestInstagramResult(new MediaCode(shortcode), pageCount, FIRST_PAGE);
    }

    public PageObject<Account> getFollows(long userId, int pageCount) throws IOException {
        GetFollowsRequest getFollowsRequest = new GetFollowsRequest(httpClient, mapper, delayHandler);
        return getFollowsRequest.requestInstagramResult(new UserParameter(userId), pageCount, FIRST_PAGE);
    }

    public PageObject<Account> getFollowers(long userId, int pageCount) throws IOException {
        GetFollowersRequest getFollowersRequest = new GetFollowersRequest(httpClient, mapper, delayHandler);
        return getFollowersRequest.requestInstagramResult(new UserParameter(userId),pageCount, FIRST_PAGE);
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
        try(InputStream jsonStream = response.body().byteStream()) {
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
    public ActivityFeed getActivityFeed() throws IOException{

        Request request = new Request.Builder()
                .url(Endpoint.ACTIVITY_FEED)
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();

        Response response = executeHttpRequest(withCsrfToken(request));
        try(InputStream jsonStream = response.body().byteStream()) {
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
        try (ResponseBody response = executeHttpRequest(withCsrfToken(request)).body()){
            //skip
        }
    }

    protected Response executeHttpRequest(Request request) throws IOException {
        Response response = this.httpClient.newCall(request).execute();
        if(delayHandler!=null){
            delayHandler.onEachRequest();
        }
        return response;
    }

    private void validateTagName(String tag) {
        if(tag==null || tag.isEmpty() || tag.startsWith("#")){
            throw new IllegalArgumentException("Please provide non empty tag name that not starts with #");
        }
    }
}
