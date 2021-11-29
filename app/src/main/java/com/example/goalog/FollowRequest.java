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

        CollectionReference ref =  FirebaseFirestore.getInstance().collection(this.toUser).
                document("Notification").collection("notification");
        ref.document(this.fromUser).set(data);
    }


    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public String getMessage() {
        return message;
    }

}
