package com.example.goalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;



public class NotificationActivity extends AppCompatActivity {
    private EditText targetEmailEditText;
    private EditText reasonEditText;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        targetEmailEditText = findViewById(R.id.send_request_email);
        reasonEditText = findViewById(R.id.send_request_reason);
        Button sendButton = (Button) findViewById(R.id.send_request_button);
        Button cancelButton = findViewById(R.id.cancel_request_button);
        Button callToMakeRequestButton = findViewById(R.id.call_to_make_request);
        LinearLayout makeRequestLayout = findViewById(R.id.request_making_layout);

        callToMakeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequestLayout.setVisibility(View.VISIBLE);
                callToMakeRequestButton.setVisibility(View.GONE);
            }
        });

        db = FirebaseFirestore.getInstance();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_my_page:
                        startActivity(new Intent(getApplicationContext(), UserPageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_trend:
                        startActivity(new Intent(getApplicationContext(), MainPagesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_notifications:
                        return true;
                }
                return false;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequestLayout.setVisibility(View.GONE);
                callToMakeRequestButton.setVisibility(View.VISIBLE);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targetEmail = targetEmailEditText.getText().toString();
                String reasonString = reasonEditText.getText().toString();

                // run a query on that collection,
                // get the document snapshots and then check if the snapshot is empty.
                final CollectionReference reference = db.collection(targetEmail);
                reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            //this collection is empty -->the email has not been signed up
                            Toast.makeText(view.getContext(), "The User does not exits",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            //user exits, create a new followRequest
                            // update that user's notification document
                            //TODO??: doer can only send request !once! to another doer?
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (!currentUser.getEmail().equals(targetEmail)) {
                                followRequest newRequest = new followRequest(currentUser.getEmail(), targetEmail, reasonString);
                                newRequest.sendToFirebase();
                            }
                            Toast.makeText(view.getContext(), "Request Sent!", Toast.LENGTH_LONG).show();
                            makeRequestLayout.setVisibility(View.GONE);
                            callToMakeRequestButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        //TODO: list of all notifications
        //TODO: grant deny permissions from another doer:grant-->add toUser to following, fromUser to toUsers' followers
        //                                                Deny-->delete this request on firebase
    }
}