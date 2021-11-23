package com.example.goalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RequestActivity extends AppCompatActivity {
    private EditText targetEmailEditText;
    private EditText reasonEditText;
    ListView requestListView;
    ArrayList<FollowRequest> requests = new ArrayList<>();
    ArrayAdapter<FollowRequest> requestAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        requestAdapter = new RequestAdapter(this, R.layout.content_request_list, requests);
        requests.add(new FollowRequest("sender 1", "receiver", "'hi there, hottie'"));
        requests.add(new FollowRequest("sender 2", "receiver", "'Sample Long Message \n long long'"));
        requestListView = findViewById(R.id.request_list_view);
        requestListView.setAdapter(requestAdapter);

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
                //TODO: no empty email allowed
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
                                // Todo: Check
                                String quoteReason = "'" + reasonString + "'";
                                FollowRequest newRequest = new FollowRequest(currentUser.getEmail(), targetEmail, quoteReason);
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
    }
}