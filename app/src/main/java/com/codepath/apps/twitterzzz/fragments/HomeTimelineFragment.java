package com.codepath.apps.twitterzzz.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.codepath.apps.twitterzzz.R;
import com.codepath.apps.twitterzzz.TwitterClient;
import com.codepath.apps.twitterzzz.Twitterzzz;
import com.codepath.apps.twitterzzz.activities.TimelineActivity;
import com.codepath.apps.twitterzzz.listeners.EndlessScrollListener;
import com.codepath.apps.twitterzzz.models.Tweet;
import com.codepath.apps.twitterzzz.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private SharedPreferences userInfo;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = Twitterzzz.getRestClient(); // singleton client
        populateTimeline(0);
        // setScrollListener();
        // setSwipeContainer();
        setUserInfo();
    }

    private void populateTimeline(final long maxId) {
        // if (maxId == 0)
        // fragmentTweetsList.clear();

        if (isNetworkAvailable()) {
            client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                    ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    if (maxId != 0) tweets.remove(0);
                    addAll(tweets);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getActivity(), R.string.get_timeline_fail, Toast.LENGTH_SHORT).show();
                }
            });
            // aTweets.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void setScrollListener() {
        getLvTweets().setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                ArrayList<Tweet> tweets = getTweets();
                long id = tweets.get(tweets.size() - 1).id;
                populateTimeline(id);
            }
        });
    }

    /*
    private void setSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) getFragmentManager().findFragmentById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TweetArrayAdapter aTweets = getTweetsAdapter();
                swipeContainer.setRefreshing(true);
                aTweets.notifyDataSetChanged();
                aTweets.clear();
                populateTimeline(0);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
    */



    private void setUserInfo() {
        FragmentActivity activity = getActivity();
        userInfo = activity.getSharedPreferences(TimelineActivity.USER_INFO, activity.MODE_PRIVATE);
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                User user = User.fromJson(jsonObject);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putString("name", user.name);
                editor.putString("screenName", user.screenName);
                editor.putString("profileImageUrl", user.profileImageUrl);
                editor.commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), R.string.get_user_info_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
