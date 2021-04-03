package com.example.puppigram.model;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.puppigram.repos.UserRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import static android.content.ContentValues.TAG;

public class FirebaseModel {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    public void logInToFireBase(String email, String password, Activity activity, UserRepo.SuccessListener listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(activity, "Sign In was Successfully", Toast.LENGTH_SHORT).show();
                        listener.onComplete(true);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        listener.onComplete(false);
                    }
                });
    }

    public void forgotPassword(String email, Activity activity, UserRepo.SuccessListener listener) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(activity, "Reset was successful", Toast.LENGTH_SHORT).show();
                    listener.onComplete(true);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(activity, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                    listener.onComplete(false);
                }
            }
        });
    }

}
