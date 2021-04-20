package com.example.puppigram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.puppigram.model.post.ImagePost;
import com.example.puppigram.model.post.PostsModel;

import java.util.List;

public class PostsViewModel extends ViewModel {
    private LiveData<List<ImagePost>> imagePosts;
    private LiveData<List<ImagePost>> userImagePosts;

    public PostsViewModel() {
        this.imagePosts = PostsModel.instance.getAllPosts();
    }

    public LiveData<List<ImagePost>> getImagePosts() {
        return this.imagePosts;
    }

    public LiveData<List<ImagePost>> getAllUserPosts(String userId){
        return PostsModel.instance.getAllUserPosts(userId);
    }

    public void setImagePosts(LiveData<List<ImagePost>> imagePosts) {
        this.imagePosts = imagePosts;
    }
}
