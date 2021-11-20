package com.example.goalog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class LoginRegistrationActivity extends AppCompatActivity {

    private TabLayout loginTab;
    private ViewPager2 viewPager;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        loginTab = findViewById(R.id.tabLayout_login);
        viewPager = findViewById(R.id.view_pager_login);

        /*
        Button loginButton = findViewById(R.id.login_button);
        Button registrationButton = findViewById(R.id.regis_button);

        EditText userLoginID = findViewById(R.id.user_id_login_edittext);
        EditText userRegisID = findViewById(R.id.user_id_regis_edittext);
        EditText userLoginPWD = findViewById(R.id.password_login_edittext);
        EditText userRegisPWD = findViewById(R.id.password_regis_edittext);
        EditText nameRegis = findViewById(R.id.name_regis_edittext);

         */

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);

        loginTab.addTab(loginTab.newTab().setText("Login"));
        loginTab.addTab(loginTab.newTab().setText("Register"));

        loginTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                loginTab.selectTab(loginTab.getTabAt(position));
            }
        });



    }
}