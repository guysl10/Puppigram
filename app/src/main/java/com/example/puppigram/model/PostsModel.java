package com.example.puppigram.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.example.puppigram.viewmodel.PostsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostsModel {

    public final static PostsModel instance = new PostsModel();
    PostsViewModel viewModel = new PostsViewModel();
    PostsModelSQL modelSQL = new PostsModelSQL();
    PostsModelFirebase modelFirebase = new PostsModelFirebase();

    public interface Listener<T> {
        void onComplete(T result);
    }

    public interface DeletePostListener {
        void onComplete(boolean success);
    }

    public interface GetAllPostsListener {
        void onComplete(ArrayList<ImagePost> data);
    }

//    public interface getAllPostsListener {
//        void onComplete();
//    }

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
        if (viewModel.getImagePosts() == null) {
            viewModel.setImagePosts(modelSQL.getAllPosts());
            refreshAllPosts(null);
        }
        return viewModel.getImagePosts();
    }

    public void refreshAllPosts(final PostsModel.GetAllPostsListener listener) {
        final SharedPreferences sp = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        modelFirebase.getAllPosts((PostsModel.GetAllPostsListener) imagePosts -> {
            long lastU = 0;
            for (ImagePost imagePost : imagePosts) {
                modelSQL.addPost(imagePost, null);
                if (imagePost.getLastUpdate() > lastU) {
                    lastU = imagePost.getLastUpdate();
                }
            }
            sp.edit().putLong("lastUpdated", lastU).apply();
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
}
