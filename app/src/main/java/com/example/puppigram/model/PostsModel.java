package com.example.puppigram.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.List;

public class PostsModel {
    public interface GetAllPostsListener{
        void onComplete(List<ImagePost> posts);
    }
    public void getAllPosts(PostsModelSQL.GetAllPostsListener listener){
        @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
            List<ImagePost> posts;
            @Override
            protected Object doInBackground(Object[] objects) {
                posts = AppLocalDb.db.postDao().getAllPosts();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(posts);
            }
        };
        task.execute();
    }

    public interface AddPostListener{
        void onComplete();
    }

    public void addPost(ImagePost post, PostsModelSQL.AddPostListener listener){
        @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.postDao().insertAllPosts(post);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener != null){
                    listener.onComplete();
                }
            }
        };
    }
}
