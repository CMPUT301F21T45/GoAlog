package com.example.goalog;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FollowRequest {
    private String fromUser;
    private String toUser;
    private String message;

    public FollowRequest(String fromUser, String toUser, String message) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
    }

    public void sendToFirebase() {
        HashMap<String, FollowRequest> data = new HashMap<>();
        data.put("requestInfo", this);

        CollectionReference ref =  FirebaseFirestore.getInstance().collection(this.toUser).document("Notification").collection(this.fromUser);
        ref.document(this.fromUser).set(data);
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
