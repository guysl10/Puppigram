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

        FeedActivity feed_fragment = new FeedActivity();
        FragmentManager feed_manager = getSupportFragmentManager();
        FragmentTransaction feed_tran = feed_manager.beginTransaction();
        feed_tran.commit();
    }


}