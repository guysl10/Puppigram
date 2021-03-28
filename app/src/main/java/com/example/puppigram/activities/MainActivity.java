package com.example.puppigram.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.puppigram.R;
import com.example.puppigram.fragments.FeedFragment;
import com.example.puppigram.fragments.TabBarFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabBarFragment tabBar = new TabBarFragment();
//        FeedFragment feed = new FeedFragment();
        FragmentManager feed_manager = getSupportFragmentManager();
        FragmentTransaction tran = feed_manager.beginTransaction();
        tran.add(R.id.appmain_frg_container, tabBar);
//        tran.add(R.id.appmain_frg_container, feed);
        tran.commit();
    }
}