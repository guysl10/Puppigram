package com.example.puppigram.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.puppigram.R;
import com.example.puppigram.repos.Repo;
import com.example.puppigram.repos.UserRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FirebaseModel {

    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    ImagePost imagePost = null;
    public FirebaseAuth firebaseAuth;
    private static final String TAG = "FirebaseModel";

    public FirebaseModel() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new
                FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public void login(String email, String password, final UserRepo.LoginUserListener listener) {
        Log.d(TAG, "login current user");
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onComplete: " + task.getResult().getUser());
                firebaseUser = task.getResult().getUser();
            }
            listener.onComplete(task.isSuccessful());
        });
    }

    public void logOut() {
        Log.d(TAG, "logout current user");
        getAuthInstance().signOut();
    }

    public void saveImage(Bitmap imageBitmap) {
        Log.d(TAG, "save image");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    }

    private void addPictureToGallery(File imageFile) {
        Log.d(TAG, "add picture to gallery");
        Intent mediaScanIntent = new
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApp.getAppContext().sendBroadcast(mediaScanIntent);
    }

    public void addLike(final String postId, final Repo.GetNewLikeListener listener) {
        Log.d(TAG, "add like");
        firebaseUser = getAuthInstance().getCurrentUser();
        PostsModelFirebase.getPost(postId, post -> {
            if (post != null) {
                post.getLikes().add(firebaseUser.getUid());
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void deleteLike(final String postId, final Repo.DeleteLikeListener listener) {
        Log.d(TAG, "delete like");
        firebaseUser = getAuthInstance().getCurrentUser();
        PostsModelFirebase.getPost(postId, post -> {
            if (post != null) {
                post.getLikes().remove(firebaseUser.getUid());
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void isLiked(final String postId, final ImageView imageView, final Repo.GetIsLikedListener listener) {
        firebaseUser = getAuthInstance().getCurrentUser();
        PostsModelFirebase.getPost(postId, imagePost -> {
            if (imagePost != null) {
                this.imagePost = imagePost;
                if (this.imagePost.getLikes().contains(firebaseUser.getUid())) {
                    imageView.setImageResource(R.drawable.is_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.is_liked);
                    imageView.setTag("like");
                }
                listener.onComplete(true);
            }
            listener.onComplete(false);
        });
    }
}
