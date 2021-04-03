package com.example.puppigram.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.puppigram.model.AbstractPost;
import com.example.puppigram.model.User;
import com.example.puppigram.repos.UserRepo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostHelper {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addPost(User user, final UserRepo.SuccessListener listener) {
        db.collection("posts").document(user.getId())
                .set(user.create()).addOnSuccessListener(aVoid -> {
            Log.d("TAG", "Post added successfully");
            listener.onComplete(true);
        }).addOnFailureListener(e -> {
            Log.d("TAG", "failed adding Post");
            listener.onComplete(true);
        });
    }

//    public void updatePost(User user, final UserRepo.SuccessListener listener, FirebaseAuth auth) {
//        Map<String, Object> editUser = new HashMap<>();
//        String id = auth.getCurrentUser().getUid();
//        String ownerId = AbstractPost.getOwnerId();
//        String description = AbstractPost;
//        String email = user.getEmail();
//        String bio = user.getBio();
//        String imageUri = user.getImageUri();
//
//        editUser.put("id", id);
//        editUser.put("name", name);
//        editUser.put("userName", userName);
//        editUser.put("email", email);
//        editUser.put("bio", bio);
//        editUser.put("imageUri", imageUri);
//
//        db.collection("Users").document(id)
//                .update(editUser).addOnSuccessListener(aVoid -> {
//            Log.d("TAG", "User added successfully");
//            listener.onComplete(true);
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("TAG", "failed adding User");
//                listener.onComplete(true);
//            }
//        });
//    }
}

