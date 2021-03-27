package com.example.puppigram.repos;

import com.example.puppigram.model.Profile;
//import com.google.android.gms.tasks.Task;

public interface ProfileRepo {

//    DatabaseReference getProfile(String id);

    String createNewProfile();

//    Task addProfile(Profile profile);

//    Task getDownloadUserPhotoUrl(String path);

    void changeName(String uid, String name);

    void deleteUser(String key);
}

