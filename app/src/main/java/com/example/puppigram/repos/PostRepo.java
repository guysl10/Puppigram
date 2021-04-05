package com.example.puppigram.repos;

import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModelFirebase;

public class PostRepo {
    PostsModelFirebase postsModelFirebase;

    public void addPost(final ImagePost post, final Repo.AddPostListener listener) {
        postsModelFirebase.addPost(post, listener);
    }

    public void deletePost(final String postId, Repo.DeletePostListener listener) {
        postsModelFirebase.deletePost(postId, listener);
    }

    public void getAllPost(final Repo.GetAllPostsListener listener) {
        postsModelFirebase.getAllPost(listener);
    }

    public void getPost(final String postId, Repo.GetPostListener listener) {
        postsModelFirebase.getPost(postId, listener);
    }

    public void editPost(final String postId, final String description, Repo.EditPostListener listener) {
        postsModelFirebase.editPost(postId, description, listener);
    }
}
