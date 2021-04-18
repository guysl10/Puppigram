package com.example.puppigram.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puppigram.R;
import com.example.puppigram.model.User;
import com.example.puppigram.repos.UserRepo;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private static final int PReqCode = 1;
    private static final int REQUSECODE = 1;
    private EditText userFullName, userEmail, userName, userPassword, userRePassword, userBio;
    private ProgressBar loadingProgress;
    private Button registerButton;
    private ImageView userPhoto;
    private final Uri pickedImgUri = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        userPhoto = findViewById(R.id.profile_image);
        userFullName = findViewById(R.id.profile_full_name);
        userName = findViewById(R.id.profile_username_text);
        userEmail = findViewById(R.id.profile_email);
        userBio = findViewById(R.id.profile_bio_text);
        userPassword = findViewById(R.id.userPassword);
        userRePassword = findViewById(R.id.userRePassword);

        registerButton = findViewById(R.id.registerButton);
        loadingProgress = findViewById(R.id.loadingProgress);

        loadingProgress.setVisibility(View.INVISIBLE);

        registerButton.setOnClickListener(v -> {
            registerButton.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
            userFullName.setEnabled(false);
            userName.setEnabled(false);
            userEmail.setEnabled(false);
            userBio.setEnabled(false);
            userPassword.setEnabled(false);
            userRePassword.setEnabled(false);
            userPhoto.setEnabled(false);
            loadingProgress.setVisibility(View.VISIBLE);

            final String name = userFullName.getText().toString();
            final String username = userName.getText().toString();
            final String email = userEmail.getText().toString();
            final String bio = userBio.getText().toString();
            final String pass = userPassword.getText().toString();
            final String repass = userRePassword.getText().toString();

            if (email.isEmpty() || name.isEmpty() || pass.isEmpty() || username.isEmpty() || bio.isEmpty() || !pass.equals(repass)) {
                showMessage("Please Verify all fields");
                registerButton.setVisibility(View.VISIBLE);
                userName.setEnabled(true);
                userEmail.setEnabled(true);
                userPassword.setEnabled(true);
                userRePassword.setEnabled(true);
                userPhoto.setEnabled(true);
                loadingProgress.setVisibility(View.INVISIBLE);
            } else {
                User user = new User(UUID.randomUUID().toString(), name, userName.toString(), email, pickedImgUri, bio);
                UserRepo.register(user, success -> showMessage("Register complete"), pass);
            }
        });
    }

    private void addProfile(User user) {
        UserRepo.login(user.getEmail(), userPassword.toString(), success -> showMessage("Login completed"));
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}