package com.example.puppigram.model.user;

import com.example.puppigram.model.post.PostsModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UsersModel {
    public static final UsersModel instance = new UsersModel();
    private final UsersModeFirebase usersModeFirebase;

    public UsersModel() {
        usersModeFirebase = new UsersModeFirebase();
    }

    public FirebaseAuth getAuthInstance() {
        return usersModeFirebase.firebaseAuth;
    }

    public interface GetAllUsersListener {
        void onComplete(ArrayList<User> data);
    }

    public void getAllUsers(GetAllUsersListener listener) {
        usersModeFirebase.getAllUsers(listener);
    }

    public interface GetUserListener {
        void onComplete(User userModel);
    }

    public void getUser(String id, GetUserListener listener) {
        UsersModeFirebase.getUser(id, listener);
    }

    public interface AddUserListener {
        void onComplete(boolean success);
    }

    public void register(final User user, String password, AddUserListener listener) {
        usersModeFirebase.register(user, password, listener);
    }

    public void login(String email, String password, UsersModeFirebase.LoginUserListener loginUserListener) {
        usersModeFirebase.login(email, password, loginUserListener);
    }

    public void logOut(Callable<Void> function) {
        usersModeFirebase.logOut(function);
    }

    public void updateProfile(final String userName, final String bio, final String pass, final PostsModel.EditProfileListener listener) {
        usersModeFirebase.updateProfile(userName, bio, pass, listener);
    }
}

