package com.codepath.apps.twitterzzz.models;


import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String tagline;
    public int followingCount;
    public int followersCount;

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.tagline = jsonObject.getString("description");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.followingCount = jsonObject.getInt("friends_count");
            user.followersCount = jsonObject.getInt("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
