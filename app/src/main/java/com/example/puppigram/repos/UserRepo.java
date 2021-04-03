package com.example.puppigram.repos;

import com.example.puppigram.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public interface UserRepo {

    interface SuccessListener {
        void onComplete(boolean result);
    }

    DatabaseReference getUser(String id);

    String createNewProfile();

    Task addProfile(User user);

    Task getDownloadUserImageUrl(String path);

    void changeName(String uid, String name);

    void deleteUser(String key);
}

