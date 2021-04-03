package com.example.puppigram.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class PostsModel {
    public final static PostsModel instance = new PostsModel();

    //TODO: create instance of firebase-model.
    PostsModelSQL modelsql = new PostsModelSQL();
//    MutableLiveData<List<ImagePost>> post_list = new MutableLiveData<List<ImagePost>>();
//    public LiveData<List<ImagePost>> getAllPosts(){
    List<ImagePost> post_list = new LinkedList<ImagePost>();
    public List<ImagePost> getAllPosts(){
//        @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
//            List<ImagePost> posts;
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                posts = AppLocalDb.db.postDao().getAllPosts();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                listener.onComplete(posts);
//            }
//        };
//        task.execute();

        return post_list;
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
