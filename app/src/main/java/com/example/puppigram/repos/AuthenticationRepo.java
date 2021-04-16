package com.example.puppigram.repos;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationRepo {
    FirebaseUser getCurrentUser();

    void signOut();

    void delete();

    static Task createUserAuth(String email, String pass);

    Task updateUserAuthKey(String key);

    Task signIn(String email, String pass);

    void changePass(String pass);
}
