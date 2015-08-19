package com.codepath.apps.twitterzzz.activities;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterzzz.R;
import com.codepath.apps.twitterzzz.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterzzz.fragments.MentionsTimelineFragment;


public class TimelineActivity extends ActionBarActivity {

    public static final String USER_INFO = "UserInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem mi) {
        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onComposeView(MenuItem mi) {
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivity(intent);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = { "Home", "Mentions" };

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
