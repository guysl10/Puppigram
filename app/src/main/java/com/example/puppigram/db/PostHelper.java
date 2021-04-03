package com.example.puppigram.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.puppigram.model.ImagePost;
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

    public void updatePost(ImagePost post, final UserRepo.SuccessListener listener, FirebaseAuth auth) {

        Map<String, Object> editUser = new HashMap<>();
        String id = auth.getCurrentUser().getUid();
        String ownerId = post.getOwnerId();
        String description = post.getDescription();
        String postImage = post.getPostImage();

        editUser.put("id", id);
        editUser.put("ownerId", ownerId);
        editUser.put("description", description);
        editUser.put("postImage", postImage);

        db.collection("posts").document(id)
                .update(editUser).addOnSuccessListener(aVoid -> {
            Log.d("TAG", "Post added successfully");
            listener.onComplete(true);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "failed adding Post");
                listener.onComplete(true);
            }
        });
    }
}

