package com.example.quotes.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quotes.ui.CreateQuoteFragment;
import com.example.quotes.ui.QuoteFragment;
import com.example.quotes.ui.SingleQuoteFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {



    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SingleQuoteFragment();
            case 1:
                return new QuoteFragment();
            case 2:
                return new CreateQuoteFragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

