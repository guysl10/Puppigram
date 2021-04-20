package com.example.puppigram.model.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;

import com.example.puppigram.model.MyApp;

import java.util.ArrayList;
import java.util.List;

public class PostsModel {

    public final static PostsModel instance = new PostsModel();
    PostsModelSQL modelSQL = new PostsModelSQL();
    PostsModelFirebase modelFirebase = new PostsModelFirebase();
    private LiveData<List<ImagePost>> imagePosts;
    Context context = MyApp.getAppContext();


    public interface Listener<T> {
        void onComplete(T result);
    }

    public interface DeletePostListener {
        void onComplete(boolean success);
    }

    public interface GetAllPostsListener {
        void onComplete(ArrayList<ImagePost> data);
    }

    public interface GetPostListener {
        void onComplete(ImagePost postModel);
    }

    public interface AddPostListener {
        void onComplete();
    }

    public interface UpdatePostListener extends AddPostListener {
    }

    public interface UploadImageListener extends Listener<String> {
    }

    public LiveData<List<ImagePost>> getAllPosts() {
        if (imagePosts == null) {
            imagePosts = modelSQL.getAllPosts();
            refreshAllPosts(null);
        }
        return imagePosts;
    }

    public void refreshAllPosts(final PostsModel.GetAllPostsListener listener) {
        final SharedPreferences sp = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        modelFirebase.getAllPosts((PostsModel.GetAllPostsListener) imagePosts -> {
            long lastU = 0;
            if(imagePosts!= null) {
                for (ImagePost imagePost : imagePosts) {
                    modelSQL.addPost(imagePost, null);
                    if (imagePost.getLastUpdate() > lastU) {
                        lastU = imagePost.getLastUpdate();
                    }
                }
                sp.edit().putLong("lastUpdated", lastU).apply();
            }
            if (listener != null) {
                listener.onComplete(null);
            }
        });
    }

    public void addPost(ImagePost post, final AddPostListener listener) {
        modelFirebase.addPost(post, () -> refreshAllPosts(data -> listener.onComplete()));
    }

    public void updatePost(final ImagePost imagePost, final AddPostListener listener) {
        modelFirebase.updatePost(imagePost, listener);
    }

    public void deletePost(final ImagePost imagePost, final DeletePostListener listener) {
        modelFirebase.deletePost(imagePost.getId(), listener);
    }

    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap);
    }

    public interface GetIsLikedListener {
        void onComplete(boolean success);
    }

    public void isLiked(final String postId, final ImageView imageView, GetIsLikedListener listener) {
        modelFirebase.isLiked(postId, imageView, listener);
    }

    public interface GetNewSaveListener {
        void onComplete(boolean success);
    }

    public interface GetNewLikeListener {
        void onComplete(boolean success);
    }

    public void addLike(final String postId, GetNewLikeListener listener) {
        modelFirebase.addLike(postId, listener);
    }

    public interface DeleteLikeListener {
        void onComplete(boolean success);
    }

    public void deleteLike(final String postId, DeleteLikeListener listener) {
        modelFirebase.deleteLike(postId, listener);
    }

    public interface EditPostListener {
        void onComplete(boolean success);
    }

    public interface EditProfileListener {
        void onComplete(boolean success);
    }
}