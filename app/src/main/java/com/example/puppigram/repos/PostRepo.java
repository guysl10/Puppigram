package com.example.puppigram.repos;

import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModel;
import com.example.puppigram.model.PostsModelFirebase;

public class PostRepo {
    static PostsModelFirebase postsModelFirebase;

    public void addPost(final ImagePost post, final PostsModel.AddPostListener listener) {
        postsModelFirebase.addPost(post, listener);
    }

    public void deletePost(final String postId, PostsModel.DeletePostListener listener) {
        postsModelFirebase.deletePost(postId, listener);
    }

    public void getAllPost(final PostsModel.GetAllPostsListener listener) {
        postsModelFirebase.getAllPosts(listener);
    }

    public static void getPost(final String postId, PostsModel.GetPostListener listener) {
        postsModelFirebase.getPost(postId, listener);
    }

    public static void editPost(final String postId, final String description, Repo.EditPostListener listener) {
        postsModelFirebase.editPost(postId, description, listener);
    }
}
