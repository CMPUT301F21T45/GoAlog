package com.example.goalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * WelcomeFragment1
 * Welcome Slogan
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment1 extends Fragment {

    private Button loginButton;

    public WelcomeFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment1 newInstance(String param1, String param2) {
        WelcomeFragment1 fragment = new WelcomeFragment1();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome_1, container, false);

        // Inflate the layout for this fragment
        return view;
    }


}