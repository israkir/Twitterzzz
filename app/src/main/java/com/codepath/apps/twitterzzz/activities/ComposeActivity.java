package com.codepath.apps.twitterzzz.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterzzz.R;
import com.codepath.apps.twitterzzz.TwitterClient;
import com.codepath.apps.twitterzzz.Twitterzzz;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {

    private TwitterClient client;
    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvCharCounter;
    private EditText etContent;
    private SharedPreferences userInfo;
    private String newTweetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = Twitterzzz.getRestClient();
        setupViews();
    }

    private void setupViews() {
        userInfo = getSharedPreferences(TimelineActivity.USER_INFO, MODE_PRIVATE);

        ivProfileImage = (ImageView) findViewById(R.id.ivComposeProfileImage);
        tvName = (TextView) findViewById(R.id.tvComposeScreenName);
        tvScreenName = (TextView) findViewById(R.id.tvComposeName);
        tvCharCounter = (TextView) findViewById(R.id.tvCharCounter);
        etContent = (EditText) findViewById(R.id.etContent);

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getBaseContext()).load(userInfo.getString("profileImageUrl", "")).into(ivProfileImage);
        tvScreenName.setText("@" + userInfo.getString("screenName", ""));
        tvName.setText("(" + userInfo.getString("name", "") + ")");

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            public void afterTextChanged(Editable s) {
                tvCharCounter.setText(String.valueOf(140 - s.length()));
            }
        });
    }

    public void onTweet(View view) {
        etContent = (EditText) findViewById(R.id.etContent);
        newTweetContent = etContent.getText().toString();

        client.postTweet(newTweetContent, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), R.string.post_tweet_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }
}
