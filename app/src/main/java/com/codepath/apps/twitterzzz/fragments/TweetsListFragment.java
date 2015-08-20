package com.codepath.apps.twitterzzz.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitterzzz.R;
import com.codepath.apps.twitterzzz.adapters.TweetArrayAdapter;
import com.codepath.apps.twitterzzz.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    private ListView lvTweets;
    View tweetsListFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        tweetsListFragmentView = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        lvTweets = (ListView) tweetsListFragmentView.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        return tweetsListFragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public ListView getLvTweets() {
        return lvTweets;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public TweetArrayAdapter getTweetsAdapter() {
        return aTweets;
    }

    public View getTweetsListFragmentView() {
        return tweetsListFragmentView;
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
