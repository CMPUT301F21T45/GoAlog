package com.example.goalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * RequestActivity
 * Request to follow others. This leads to add others as your followers.
 * The target user will be added to your following list upon approval.
 * Swipe to accept or decline a request.
 */
public class RequestActivity extends AppCompatActivity {
    private EditText targetEmailEditText;
    private EditText reasonEditText;
    SwipeMenuListView requestListView;
    Integer selection;
    ArrayList<FollowRequest> requests = new ArrayList<>();
    ArrayAdapter<FollowRequest> requestAdapter;
    FirebaseFirestore db;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        db = FirebaseFirestore.getInstance();
        requestAdapter = new RequestAdapter(this, R.layout.content_request_list, requests);
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

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selection = i;
            }
        });


        callToMakeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequestLayout.setVisibility(View.VISIBLE);
                callToMakeRequestButton.setVisibility(View.GONE);
            }
        });

        try {
            final CollectionReference collectionReference = db.collection(Objects.requireNonNull(currentUser.getEmail()))
                    .document("Notification")
                    .collection("notification");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(
                        @Nullable QuerySnapshot queryDocumentSnapshots,
                        @Nullable FirebaseFirestoreException error) {
                    requests.clear();

                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        // Adding the FollowRequest from FireStore
                        HashMap<String, Object> map = (HashMap<String, Object>) doc.getData().get("requestInfo");
                        if (doc.getData().get("requestInfo") != null) {
                            String fromUser = (String) map.get("fromUser");
                            String message = (String) map.get("message");
                            String toUser = (String) map.get("toUser");
                            requests.add(new FollowRequest(fromUser, toUser, message));
                        }
                    }
                    requestAdapter.notifyDataSetChanged();
                }
            });

            // for swipe delete
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                /**
                 * Create the "left swipe" menu
                 * @param menu
                 *  Swipe menu
                 */
                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    openItem.setBackground(new ColorDrawable(Color.rgb(0xB2, 0xff,
                            0x66)));
                    // set item width
                    openItem.setWidth(180);
                    // set item title
                    openItem.setIcon(R.drawable.ic_baseline_check);
                    // add to menu
                    menu.addMenuItem(openItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                            0x66, 0x66)));
                    // set item width
                    deleteItem.setWidth(180);
                    // set a icon
                    deleteItem.setIcon(R.drawable.ic_delete);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };

            // set creator
            requestListView.setMenuCreator(creator);

            requestListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                /**
                 * On Menu Item Click
                 * @param position Clicked position in menu.
                 * @param menu The current menu
                 * @param index The index
                 * @return A boolean value
                 */
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            // Check (check icon is clicked)
                            // update toUser's followers array
                            // update fromUser's following array
                            User toUserOb;
                            User fromUserOb;
                            FollowRequest selectedRequests = requests.get(position);
                            String toUser = selectedRequests.getToUser();
                            String fromUser = selectedRequests.getFromUser();

                            DocumentReference toUserRef = db.collection(toUser).document("Info");
                            toUserRef.update("followers", FieldValue.arrayUnion(fromUser));
                            DocumentReference fromUserRef = db.collection(fromUser).document("Info");
                            fromUserRef.update("following",FieldValue.arrayUnion(toUser));

                            collectionReference.document(selectedRequests.getFromUser()).delete();
                            break;
                        case 1:
                            // Delete (trash can is clicked)
                            collectionReference.document(requests.get(position).getFromUser()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                /**
                                 * Successfully deleted a document.
                                 * @param aVoid
                                 *  aVoid
                                 */
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Sample", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                /**
                                 * Failed to delete a document
                                 * @param e
                                 *  Exception e
                                 */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Sample", "Error deleting document", e);
                                }
                            });
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
                String targetEmail = targetEmailEditText.getText().toString().toLowerCase(Locale.ROOT);
                String reasonString = reasonEditText.getText().toString();

                if (targetEmail.equals("")) {
                    Toast.makeText(view.getContext(), "Please Enter an Email!",
                            Toast.LENGTH_LONG).show();
                } else {
                    // run a query on that collection,
                    // get the document snapshots and then check if the snapshot is empty.
                    final CollectionReference reference = db.collection(targetEmail);
                    reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                //this collection is empty -->the email has not been signed up
                                Toast.makeText(view.getContext(), "The User does not exits",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                //user exits, create a new followRequest
                                // update that user's notification document
                                if (!currentUser.getEmail().equals(targetEmail)) {
                                    String quoteReason = "'" + reasonString + "'";
                                    FollowRequest newRequest = new FollowRequest(currentUser.getEmail(), targetEmail, quoteReason);
                                    newRequest.sendToFirebase();
                                    Toast.makeText(view.getContext(), "Request Sent!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(view.getContext(), "Can't Send to Yourself!", Toast.LENGTH_LONG).show();
                                }
                                makeRequestLayout.setVisibility(View.GONE);
                                callToMakeRequestButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
}