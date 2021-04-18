package com.example.puppigram.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModel;

import java.util.List;

public class PostsViewModel extends ViewModel {
    private LiveData<List<ImagePost>> imagePosts;

    public PostsViewModel() {
        this.imagePosts = PostsModel.instance.getAllPosts();
    }

    public LiveData<List<ImagePost>> getImagePosts() {
        return this.imagePosts;
    }

    public void setImagePosts(LiveData<List<ImagePost>> imagePosts) {
        this.imagePosts = imagePosts;
    }
}
