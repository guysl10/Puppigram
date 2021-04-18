package com.example.puppigram.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.puppigram.R;
import com.example.puppigram.model.User;
import com.example.puppigram.repos.UserRepo;
import com.example.puppigram.utils.Navigator;
import com.example.puppigram.utils.PhotoUtil;

import java.util.UUID;

import static com.example.puppigram.utils.PhotoUtil.REQUEST_IMAGE_CAPTURE;
import static com.example.puppigram.utils.PhotoUtil.REQUEST_IMAGE_GALLERY;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail, userName, userPassword, userRePassword, userBio;
    private ProgressBar loadingProgress;
    private Button registerButton;
    private ImageView userPhoto;
    private final Uri pickedImgUri = null;
    private PhotoUtil photoActivity;
    private Navigator navigator;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        navigator = new Navigator(this);
        userPhoto = findViewById(R.id.register_puppi_img);
        userName = findViewById(R.id.register_username_input);
        userEmail = findViewById(R.id.register_email_input);
        userBio = findViewById(R.id.register_bio_input);
        userPassword = findViewById(R.id.register_pass_input);
        userRePassword = findViewById(R.id.register_retypepass_input);

        registerButton = findViewById(R.id.register_btn);
        loadingProgress = findViewById(R.id.register_spinner);

        loadingProgress.setVisibility(View.INVISIBLE);
        photoActivity = new PhotoUtil(this);
        userPhoto.setOnClickListener(v ->
                getPhotoActivity().checkAndRequestPermissionForCamera(userPhoto)
        );


        registerButton.setOnClickListener(v -> {
            createProfile();
        });
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
    private void createProfile() {
        registerButton.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.INVISIBLE);
        userName.setEnabled(false);
        userEmail.setEnabled(false);
        userBio.setEnabled(false);
        userPassword.setEnabled(false);
        userRePassword.setEnabled(false);
        userPhoto.setEnabled(false);
        loadingProgress.setVisibility(View.VISIBLE);

        final String username = userName.getText().toString();
        final String email = userEmail.getText().toString();
        final String bio = userBio.getText().toString();
        final String pass = userPassword.getText().toString();
        final String repass = userRePassword.getText().toString();

        if (email.isEmpty() || pass.isEmpty() || username.isEmpty() || bio.isEmpty() || !pass.equals(repass)) {
            showMessage("Please Verify all fields");
            registerButton.setVisibility(View.VISIBLE);
            userName.setEnabled(true);
            userEmail.setEnabled(true);
            userPassword.setEnabled(true);
            userRePassword.setEnabled(true);
            userPhoto.setEnabled(true);

        } else {
            User user = new User(UUID.randomUUID().toString(), userName.toString(), email, pickedImgUri, bio);
            UserRepo.instance.register(user, success -> showMessage("Register complete"), pass);
            UserRepo.instance.login(user.getEmail(), userPassword.toString(), success -> showMessage("Login completed"));
            navigator.navigate(MainActivity.class);
        }
        loadingProgress.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}