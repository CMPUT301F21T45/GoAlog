package com.example.goalog;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

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
    private String email;
    private String displayName;
    private final ArrayList<User> followings = new ArrayList<>();
    private final ArrayList<User> followers = new ArrayList<>();

    public User(String userID, String email, String publicName) {
        this.userID = userID;
        this.email = email;
        this.displayName = publicName;
    }

    public void sendToFirebase() {
        HashMap<String, User> data = new HashMap<>();
        data.put("UserInfo", this);

        CollectionReference ref =  FirebaseFirestore.getInstance().collection(this.email);
        ref.document("Info").set(data);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<User> getFollowings() {
        return followings;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }
}
