package com.example.puppigram.repos;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.puppigram.model.FirebaseModel;
import com.example.puppigram.model.MyApp;
import com.google.firebase.auth.FirebaseAuth;

public class Repo {
    final public static Repo instance = new Repo();

    private static Context context;
    FirebaseModel firebaseModel;

    public Repo() {
        context = MyApp.getAppContext();
        firebaseModel = new FirebaseModel();
    }

    public void logOut() {
        firebaseModel.logOut();
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        firebaseModel.saveImage(imageBitmap);
    }

    public FirebaseAuth getAuthInstance() {
        return firebaseModel.firebaseAuth;
    }

    public interface AddPostListener {
        void onComplete(boolean success);
    }

    public interface GetIsLikedListener {
        void onComplete(boolean success);
    }

//    public void isLiked(final String postId, final ImageView imageView, GetIsLikedListener listener) {
//        firebaseModel.isLiked(postId, imageView, listener);
//    }

    public interface GetNewSaveListener {
        void onComplete(boolean success);
    }

    public interface GetNewLikeListener {
        void onComplete(boolean success);
    }

    public void addLike(final String postId, GetNewLikeListener listener) {
        firebaseModel.addLike(postId, listener);
    }

    public interface DeleteLikeListener {
        void onComplete(boolean success);
    }

    public void deleteLike(final String postId, DeleteLikeListener listener) {
        firebaseModel.deleteLike(postId, listener);
    }

    public interface EditPostListener {
        void onComplete(boolean success);
    }

    public interface EditProfileListener {
        void onComplete(boolean success);
    }

    public interface UploadFileListener {
        void onComplete(boolean success);
    }

}

