package com.example.puppigram.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.List;

public class PostsModelSQL {
    public final static PostsModelSQL instance = new PostsModelSQL();
    PostsModelSQL(){}


    public interface GetAllPostsListener{
        void onComplete(List<ImagePost> posts);
    }

    public void getAllPosts(GetAllPostsListener listener){
        class MyAsyncTask extends AsyncTask {
            List<ImagePost> posts;
            @Override
            protected Object doInBackground(Object[] objects) {
                posts = AppLocalDb.db.postDao().getAllPosts();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(posts);
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface AddPostListener{
        void onComplete();
    }

    public void addPost(ImagePost post, AddPostListener listener){
        class MyAsyncTask extends AsyncTask {
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
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public void editPost(ImagePost post, AddPostListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.postDao().updatePost(post);
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
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }


}
