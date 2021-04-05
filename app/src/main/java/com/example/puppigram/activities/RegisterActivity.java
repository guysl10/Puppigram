package com.example.puppigram.activities;

import android.content.Intent;
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
import com.example.puppigram.repos.AuthenticationRepo;
import com.example.puppigram.repos.UserRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class RegisterActivity extends AppCompatActivity {

    private static int PReqCode = 1;
    private static int REQUSECODE = 1;
    private EditText userEmail, userName, userPassword, userRePassword;
    private ProgressBar loadingProgress;
    private Button registerButton;
    private ImageView userPhoto;
    private Uri pickedImgUri = null;
    private Intent intent;
    private UserRepo userRepo;
    private AuthenticationRepo authenticationRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userPhoto = findViewById(R.id.userPhoto);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        userRePassword = findViewById(R.id.userRePassword);
        registerButton = findViewById(R.id.registerButton);
        loadingProgress = findViewById(R.id.loadingProgress);
        intent = new Intent();

        loadingProgress.setVisibility(View.INVISIBLE);

//        userPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 22) {
//                    checkAndRequestPermission();
//                } else {
//                    openGallery();
//                }
//            }
//        });
        registerButton.setOnClickListener(v -> {
            registerButton.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
            userName.setEnabled(false);
            userEmail.setEnabled(false);
            userPassword.setEnabled(false);
            userRePassword.setEnabled(false);
            userPhoto.setEnabled(false);
            loadingProgress.setVisibility(View.VISIBLE);

            final String email = userEmail.getText().toString();
            final String name = userName.getText().toString();
            final String pass = userPassword.getText().toString();
            final String repass = userRePassword.getText().toString();

            if (email.isEmpty() || name.isEmpty() || pass.isEmpty() || !pass.equals(repass) || pickedImgUri == null) {
                showMessage("Please Verify all fields");
                registerButton.setVisibility(View.VISIBLE);
                userName.setEnabled(true);
                userEmail.setEnabled(true);
                userPassword.setEnabled(true);
                userRePassword.setEnabled(true);
                userPhoto.setEnabled(true);
                loadingProgress.setVisibility(View.INVISIBLE);
            } else {
                //TODO: create user account function in db
//                    CreateUserAccount(email, name, pass);
            }

        });
    }

    private void addProfile(User user) {
        final String key = userRepo.createNewProfile();

        userRepo.addProfile(user).addOnSuccessListener((OnSuccessListener<Void>) aVoid -> authenticationRepo.updateUserAuthKey(key).addOnCompleteListener((OnCompleteListener<Void>) task -> {
            showMessage("Register complete");
            intent.putExtra("Result", "OK");
            setResult(RESULT_OK, intent);
            finish();
        }).addOnFailureListener(e -> showMessage(e.getMessage()))).addOnFailureListener(e -> showMessage(e.getMessage()));
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}

