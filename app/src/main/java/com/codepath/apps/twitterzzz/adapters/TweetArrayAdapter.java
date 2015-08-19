package com.codepath.apps.twitterzzz.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterzzz.R;
import com.codepath.apps.twitterzzz.models.Tweet;
import com.codepath.apps.twitterzzz.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetArrayAdapter(Context context, List<Tweet> tweetList) {
        super(context, android.R.layout.simple_list_item_1, tweetList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivComposeProfileImage);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvComposeName);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvComposeScreenName);
        TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.user.profileImageUrl).into(ivProfileImage);

        tvScreenName.setText("@" + tweet.user.screenName);
        tvName.setText("(" + tweet.user.name + ")");
        tvCreatedAt.setText(Util.getRelativeTimeAgo(tweet.createdAt));
        tvText.setText(tweet.text);

        return convertView;
    }

}
