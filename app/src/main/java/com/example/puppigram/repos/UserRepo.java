package com.example.puppigram.repos;

import com.example.puppigram.model.FirebaseModel;
import com.example.puppigram.model.User;
import com.example.puppigram.model.UsersModelFirebase;

import java.util.ArrayList;

public class UserRepo {

    static FirebaseModel firebaseModel;
    static UsersModelFirebase usersModelFirebase;

    public interface GetAllUsersListener {
        void onComplete(ArrayList<User> data);
    }

    public void getAllUsers(GetAllUsersListener listener) {
        usersModelFirebase.getAllUsers(listener);
    }

    public interface GetUserListener {
        void onComplete(User userModel);
    }

    public void getUser(String id, GetUserListener listener) {
        UsersModelFirebase.getUser(id, listener);
    }

    public interface AddUserListener {
        void onComplete(boolean success);
    }

    public static void register(final User user, AddUserListener listener, String password) {
        usersModelFirebase.register(user, password, listener);
    }

    public interface LoginUserListener {
        void onComplete(boolean success);
    }

    public static void login(String email, String password, LoginUserListener listener) {
        firebaseModel.login(email, password, listener);
    }

    public void updateProfile(final String userName, final String bio, Repo.EditProfileListener listener) {
        usersModelFirebase.updateProfile(userName, bio, listener);
    }

    public void uploadImage(User user) {
        UsersModelFirebase.uploadImage(user);
    }
}

