package com.example.goalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * WelcomeActivity
 * In this activity, you are going to log in or sign up and use the application
 * Once you logged in, your status will be kept until you sign out.
 * (also the activity will be skipped if you've logged in)
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            startActivity(new Intent(getApplicationContext(), MainPagesActivity.class));
            finish();
        }

        TabLayout tablayout = findViewById(R.id.tabLayout_login);
        ViewPager2 viewPager = findViewById(R.id.view_pager_login);
        Button loginButton = findViewById(R.id.login_button);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);

        tablayout.addTab(tablayout.newTab().setText("Login"));
        tablayout.addTab(tablayout.newTab().setText("Register"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.Theme_Signin) // ~/themes/themes.xml
                        //.setAvailableProviders(providers)
                        .build();
                signInLauncher.launch(signInIntent);
            }
        });


    }
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent = new Intent(this, MainPagesActivity.class);
            assert currentUser != null;

            final CollectionReference collRef = FirebaseFirestore.getInstance()
                    .collection(currentUser.getEmail());
            collRef.document("Info").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (!doc.exists()) {
                            User newUser = new User(currentUser.getUid(), currentUser.getEmail(), currentUser.getDisplayName());
                            newUser.setCreated(true);
                            newUser.setToFirebase();
                        }
                    }
                }
            });

            finish();
            startActivity(intent);
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}