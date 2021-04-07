package com.example.puppigram.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PostsModelSQL {
    public final static PostsModelSQL instance = new PostsModelSQL();

    PostsModelSQL() {
    }

    public interface GetAllPostsListener {
        void onComplete(List<ImagePost> posts);
    }

    public interface AddPostListener {
        void onComplete();
    }

    public LiveData<List<ImagePost>> getAllPosts() {
        return AppLocalDb.db.postDao().getAllPosts();
    }

    public void addPost(ImagePost post, AddPostListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.postDao().insertAllPosts(post);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete();
                }
            }
        }
        ;
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
                if (listener != null) {
                    listener.onComplete();
                }
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public void deletePost(ImagePost post, AddPostListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.postDao().delete(post);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete();
                }
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
