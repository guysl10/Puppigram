package com.example.puppigram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.puppigram.R;
import com.example.puppigram.repos.AuthenticationRepo;
import com.example.puppigram.utils.Navigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPass;
    private ProgressBar loadingProgress;
    private TextView newAccount;
    private final int REQUEST_CODE = 1;
    private Button loginButton;
    private Navigator navigator;
    private AuthenticationRepo authenticationRepo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userEmail = findViewById(R.id.email);
        userPass = findViewById(R.id.pass);
        loadingProgress = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);
        newAccount = findViewById(R.id.newAccount);
        loadingProgress.setVisibility(View.INVISIBLE);
        navigator = new Navigator(this);
        newAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            navigator.navigate(RegisterActivity.class);
        });

        loginButton.setOnClickListener(v -> navigator.navigate(MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticationRepo.getCurrentUser() != null) {
            //user is already connected to redirect him to home page
            //TODO:redirect user to home page

        }
    }

    private void singIn(String email, String pass) {
        authenticationRepo.signIn(email, pass).addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful()) {
                loadingProgress.setVisibility(View.INVISIBLE);
            } else {
                showGroup();
                loginButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
                showMessage(task.getException().getMessage());
            }
        });
    }

    void showGroup() {
        groupToBeHideOrNot(View.VISIBLE);
    }

    void groupToBeHideOrNot(int visibility) {
        userEmail.setVisibility(visibility);
        userPass.setVisibility(visibility);
        newAccount.setVisibility(visibility);
        loginButton.setVisibility(visibility);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("Result");
                if (result.equals("OK"))
                    this.onStart();
            }
        }
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}