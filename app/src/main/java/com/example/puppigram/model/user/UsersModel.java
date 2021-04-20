package com.example.puppigram.model.user;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UsersModel {
    public static final UsersModel instance = new UsersModel();
    private final UsersModeFirebase usersModeFirebase;

    public interface Listener<T> {
        void onComplete(T result);
    }

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

    public interface AddStudentListener {
        void onComplete();
    }

    public void addUser(final User student, final AddStudentListener listener) {
        usersModeFirebase.addUser(student, listener);
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

    public void updateUser(User user, final AddStudentListener listener) {
        usersModeFirebase.updateUser(user, listener);
    }

    public interface UploadImageListener extends Listener<String> {
    }

    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        usersModeFirebase.uploadImage(imageBmp, name, listener);
    }

    public void changeUserPassword(final String pass) {
        usersModeFirebase.changeUserPassword(pass);
    }
}

