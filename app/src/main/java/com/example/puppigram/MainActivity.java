package com.example.puppigram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FeedFragment feed_fragment = new FeedFragment();
        FragmentManager feed_manager = getSupportFragmentManager();
        FragmentTransaction feed_tran = feed_manager.beginTransaction();
        feed_tran.add(R.id.main_container, feed_fragment);
        feed_tran.commit();
    }
}