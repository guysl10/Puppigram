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
import com.example.puppigram.model.FirebaseModel;
import com.example.puppigram.repos.UserRepo;
import com.example.puppigram.utils.Navigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Callable;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPass;
    private ProgressBar loadingProgress;
    private TextView newAccount;
    private final int REQUEST_CODE = 1;
    private Button loginButton;
    private Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.login_email_input);
        userPass = findViewById(R.id.login_password_test);
        loadingProgress = findViewById(R.id.login_spinner);
        loginButton = findViewById(R.id.login_btn);
        newAccount = findViewById(R.id.login_signup_text);
        loadingProgress.setVisibility(View.INVISIBLE);
        navigator = new Navigator(this);
        newAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            navigator.navigate(RegisterActivity.class);
        });

        loginButton.setOnClickListener(v -> navigator.navigate(MainActivity.class));
        newAccount.setOnClickListener(v -> navigator.navigate(RegisterActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UserRepo.instance.getAuthInstance().getCurrentUser() != null) {
            navigator.navigate(MainActivity.class);
        }
    }

    private void singIn(String email, String pass) {
        UserRepo.instance.login(email, pass, success -> {
            if (success) {
                loadingProgress.setVisibility(View.INVISIBLE);
                navigator.navigate(MainActivity.class);
            } else {
                groupToBeHideOrNot();
                loginButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(
                        getApplicationContext(),
                        "signin failed",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    void groupToBeHideOrNot() {
        userEmail.setVisibility(View.VISIBLE);
        userPass.setVisibility(View.VISIBLE);
        newAccount.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
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
}