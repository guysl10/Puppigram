package com.example.puppigram.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.puppigram.R;
import com.example.puppigram.utils.PhotoUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.puppigram.utils.PhotoUtil.REQUEST_IMAGE_CAPTURE;
import static com.example.puppigram.utils.PhotoUtil.REQUEST_IMAGE_GALLERY;

public class MainActivity extends AppCompatActivity {

    private PhotoUtil photoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Bottom Navigation View.
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        //Pass the ID's of Different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.feedFragment, R.id.settingsFragment, R.id.uploadPostFragment, R.id.profileFragment)
                .build();

        //Initialize NavController.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        this.photoActivity = new PhotoUtil(this);
    }

    public PhotoUtil getPhotoActivity() {
        return photoActivity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_GALLERY) && resultCode == RESULT_OK){
            photoActivity.onResult(requestCode, resultCode, data);
        }
    }
}