package com.example.puppigram.repos;

import android.net.Uri;

import com.example.puppigram.model.ImagePost;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public interface PostRepo {
    String createNewPost();

    Task addPost(ImagePost post);

    DatabaseReference getPost(String id);

    void updateLikers(String key, List<String> list);

    Task deletePost(String key);

    UploadTask uploadPhoto(String path, Uri photo);

    Task getDownloadPhotoUrl(String path);

    void deleteAllPostsByUserKey(String key);

    interface SuccessListener {
        void onComplete(boolean result);
    }
}
