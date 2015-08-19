package com.codepath.apps.twitterzzz.fragments;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitterzzz.R;
import com.codepath.apps.twitterzzz.TwitterClient;
import com.codepath.apps.twitterzzz.Twitterzzz;
import com.codepath.apps.twitterzzz.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = Twitterzzz.getRestClient(); // singleton client
        populateTimeline(0);
    }

    private void populateTimeline(final long maxId) {
        if (isNetworkAvailable()) {
            client.getMentionsTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                    ArrayList<Tweet> mentions = Tweet.fromJsonArray(jsonArray);
                    addAll(mentions);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("mentions timeline fail", errorResponse.toString());
                    Toast.makeText(getActivity(), R.string.get_timeline_fail, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_SHORT).show();
        }
    }
}
