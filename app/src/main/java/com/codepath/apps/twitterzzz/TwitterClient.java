package com.codepath.apps.twitterzzz;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.concurrent.locks.ReadWriteLock;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "";
	public static final String REST_CONSUMER_SECRET = "";
	public static final String REST_CALLBACK_URL = "oauth://twitterzzz";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams requestParams = new RequestParams();
		requestParams.put("count", 25);
        if (maxId > 0)
            requestParams.put("max_id", maxId);
        getClient().get(apiUrl, requestParams, handler);
	}

    public void getMentionsTimeline(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", 25);
        if (maxId > 0)
            requestParams.put("max_id", maxId);
        getClient().get(apiUrl, requestParams, handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", 25);
        requestParams.put("screen_name", screenName);
        getClient().get(apiUrl, requestParams, handler);
    }

	public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl;
        if (screenName != null) {
            apiUrl = getApiUrl("users/show.json");
            RequestParams requestParams = new RequestParams();
            requestParams.put("screen_name", screenName);
            getClient().get(apiUrl, requestParams, handler);
        } else {
            apiUrl = getApiUrl("account/verify_credentials.json");
            getClient().get(apiUrl, null, handler);
        }
	}

    public void postTweet(String content, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams requestParams = new RequestParams();
        requestParams.put("status", content);
        getClient().post(apiUrl, requestParams, handler);
    }


}