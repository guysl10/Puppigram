package com.example.puppigram.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.puppigram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Initialize Bottom Navigation View.
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        //Pass the ID's of Different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.feedFragment, R.id.searchFragment, R.id.uploadPostFragment, R.id.profileFragment)
                .build();

        //Initialize NavController.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }


}