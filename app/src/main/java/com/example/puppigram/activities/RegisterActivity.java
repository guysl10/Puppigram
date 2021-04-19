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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.puppigram.R;
import com.example.puppigram.model.User;
import com.example.puppigram.repos.UserRepo;
import com.example.puppigram.utils.Navigator;
import com.example.puppigram.utils.PhotoUtil;

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
    private TextView cancel;
    private TextView empty;

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
        cancel = findViewById(R.id.register_cancel_text);
        registerButton = findViewById(R.id.register_btn);
        loadingProgress = findViewById(R.id.register_spinner);
        empty = findViewById(R.id.register_empty_text);
        loadingProgress.setVisibility(View.INVISIBLE);
        photoActivity = new PhotoUtil(this);
        userPhoto.setOnClickListener(v ->
                getPhotoActivity().checkAndRequestPermissionForCamera(userPhoto)
        );


        registerButton.setOnClickListener(v -> {
            createProfile();
        });

        empty.setOnClickListener(v -> emptyFields());

        cancel.setOnClickListener(v -> navigator.navigate(LoginActivity.class));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void emptyFields() {
        this.userPhoto.setImageDrawable(
                getResources().getDrawable(
                        getResources().getIdentifier(
                                "circle_cropped",
                                "drawable",
                                getPackageName()
                        )
                )
        );
        this.userBio.setText("");
        this.userEmail.setText("");
        this.userName.setText("");
        this.userPassword.setText("");
        this.userRePassword.setText("");
    }

    public PhotoUtil getPhotoActivity() {
        return photoActivity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_GALLERY) && resultCode == RESULT_OK) {
            photoActivity.onResult(requestCode, resultCode, data);
        }
    }
    private void createProfile() {
        registerButton.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.INVISIBLE);
//        userName.setEnabled(false);
//        userEmail.setEnabled(false);
//        userBio.setEnabled(false);
//        userPassword.setEnabled(false);
//        userRePassword.setEnabled(false);
//        userPhoto.setEnabled(false);
//        loadingProgress.setVisibility(View.VISIBLE);

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
            User user = new User(username, email, bio);
            UserRepo.instance.register(user, pass, success -> {
                showMessage("Register complete");
                UserRepo.instance.login(user.getEmail(), userPassword.toString(), v -> {
                    if (success) {
                        navigator.navigate(MainActivity.class);
                    } else
                        showMessage("SignIn failed");
                });
            });
        }
        loadingProgress.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}