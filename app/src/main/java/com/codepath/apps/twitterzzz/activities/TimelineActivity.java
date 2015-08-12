package com.codepath.apps.twitterzzz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterzzz.R;
import com.codepath.apps.twitterzzz.TwitterClient;
import com.codepath.apps.twitterzzz.Twitterzzz;
import com.codepath.apps.twitterzzz.adapters.TweetArrayAdapter;
import com.codepath.apps.twitterzzz.listeners.EndlessScrollListener;
import com.codepath.apps.twitterzzz.models.Tweet;
import com.codepath.apps.twitterzzz.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    protected static final String USER_INFO = "UserInfo";
    private SharedPreferences userInfo;
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);

        client = Twitterzzz.getRestClient(); // singleton client

        populateTimeline(0);

        setScrollListener();
        setSwipeContainer();
        setUserInfo();
    }

    private void setScrollListener() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                long id = tweets.get(tweets.size() - 1).id;
                populateTimeline(id);
            }
        });
    }

    private void setSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

    private void setUserInfo() {
        userInfo = getSharedPreferences(USER_INFO, MODE_PRIVATE);
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
                Toast.makeText(getBaseContext(), R.string.get_user_info_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateTimeline(long maxId) {
        if (maxId == 0) {
            tweets.clear();
            aTweets.notifyDataSetChanged();
        }

        if (isNetworkAvailable()) {
            client.getTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                    ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    tweets.remove(0);
                    aTweets.addAll(tweets);
                    aTweets.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getBaseContext(), R.string.get_timeline_fail, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_compose) {
            openCompose();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCompose() {
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivity(intent);
    }
}
