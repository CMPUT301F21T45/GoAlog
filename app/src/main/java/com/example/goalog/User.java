package com.example.goalog;

import java.util.ArrayList;

/**
 * User Class
 * An object of this class represent one of our users
 * userID: login Credential, unique to firebase
 * publicName: name displayed publicly in the app
 * followers: user's followers
 * followings: user's currently following user
 *
 * Problem & Further Changes: Pending
 */
public class User {
    private String userID; // Unique to database
    private String publicName;
    private String password;
    private final ArrayList<User> followings = new ArrayList<>();
    private final ArrayList<User> followers = new ArrayList<>();

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<User> getFollowings() {
        return followings;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }
}
