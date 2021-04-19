package com.example.puppigram.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.puppigram.R;
import com.example.puppigram.repos.Repo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Callable;

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

    public interface LoginUserListener{
        void onComplete(boolean success);
    }

    public void login(String email, String password, LoginUserListener loginUserListener) {
        Log.d(TAG, "login current user");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        firebaseUser = firebaseAuth.getCurrentUser();
                        Log.d(TAG, "onComplete: " + firebaseUser);
                        loginUserListener.onComplete(true);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        loginUserListener.onComplete(false);
                    }
                });
    }

    public void logOut(Callable<Void> onComplete) {
        Log.d(TAG, "logout current user");
        firebaseAuth.signOut();
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            if(firebaseAuth.getCurrentUser() == null) {
                try {
                    onComplete.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
