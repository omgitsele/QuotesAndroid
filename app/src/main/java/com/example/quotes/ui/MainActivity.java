package com.example.quotes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.quotes.R;
import com.example.quotes.adapters.ViewPagerAdapter;
import com.example.quotes.repositories.QuoteRepository;
import com.example.quotes.viewmodels.QuoteViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BROADCAST TAG";
    BottomNavigationView bottomNavigationView;
    private QuoteViewModel quoteViewModel;
    private ViewPager2 myViewPager2;
    private ViewPagerAdapter myAdapter;
    private TabLayout tabLayout;
    private ProgressDialog progressDialog;
    private QuoteRepository quoteRepository;


    private final String[] tabsArray = new String[]{"Daily", "All", "Create"};
    private final int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_list,
            R.drawable.ic_edit
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.tab_layout);

        quoteRepository = new QuoteRepository(getApplication());

        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        quoteViewModel.init(quoteRepository);
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Loading Quote");

        myViewPager2 = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.mainTabs);
        myAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        myViewPager2.setAdapter(myAdapter);

        new TabLayoutMediator(tabLayout, myViewPager2,
                ((tab, position) -> tab.setText(tabsArray[position]))).attach();

        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        // Set the icons for the tab layout
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        // Observe our Throwable live data and if we get a throwable Toast it.
        quoteViewModel.getThrowableLiveData().observe(this, throwable -> {
            Toast.makeText(this, "Error: "+throwable, Toast.LENGTH_SHORT).show();
        });

        quoteViewModel.getProgressDialog().observe(this, text -> {
            progressDialog.setTitle(text);
        });


        // Observe a boolean in order to show/hide the progressDialog
        quoteViewModel.getShowProgressBar().observe(this, aBoolean -> {
            if (aBoolean && !progressDialog.isShowing()){
                progressDialog.show();
            }
            else if (!aBoolean && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        });

        quoteViewModel.getSuccessLiveData().observe(this, success -> {
            if (success)
                Toast.makeText(this, "Successfully uploaded new quote!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show();
        });



    }

}