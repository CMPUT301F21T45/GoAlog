package com.example.goalog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * FragmentAdapter
 * It is an Adapter for the fragments inside the Tab Layout
 * It holds WelcomeFragment1 and WelcomeFragment2
 */
public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {

            case 1:
                return new WelcomeFragment2();
        }
        return new WelcomeFragment1();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
