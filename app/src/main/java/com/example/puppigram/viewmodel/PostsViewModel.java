package com.example.puppigram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModel;
import com.example.puppigram.model.PostsModelSQL;

import java.util.LinkedList;
import java.util.List;

public class PostsViewModel extends ViewModel {
//    private MutableLiveData<List<ImagePost>> imagePosts = (MutableLiveData<List<ImagePost>>) PostsModel.instance.getAllPosts();
    private List<ImagePost> imagePosts = new LinkedList<ImagePost>();

    public PostsViewModel(){
        PostsModelSQL.instance.getAllPosts(db_posts -> this.setImagePosts(db_posts));
    }
    public List<ImagePost> getImagePosts() {
        return imagePosts;
    }

    public void setImagePosts(List<ImagePost> imagePosts) {
        this.imagePosts = imagePosts;
    }




}
