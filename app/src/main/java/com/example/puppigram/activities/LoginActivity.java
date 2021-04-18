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
import com.example.puppigram.utils.Navigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPass;
    private ProgressBar loadingProgress;
    private TextView newAccount;
    private final int REQUEST_CODE = 1;
    private Button loginButton;
    private Navigator navigator;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.firebaseAuth = FirebaseAuth.getInstance();
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
        if (firebaseAuth.getCurrentUser() != null) {
            navigator.navigate(MainActivity.class);
        }
    }

    private void singIn(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful()) {
                loadingProgress.setVisibility(View.INVISIBLE);
                navigator.navigate(MainActivity.class);
            } else {
                groupToBeHideOrNot();
                loginButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
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