package com.example.puppigram.repos;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.FirebaseModel;
import com.example.puppigram.model.User;
import com.example.puppigram.model.UsersModelFirebase;
import com.example.puppigram.utils.Navigator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UserRepo {
    public static final UserRepo instance = new UserRepo();
    private final FirebaseModel firebaseModel;
    private final UsersModelFirebase usersModelFirebase;
    public UserRepo() {
        firebaseModel = new FirebaseModel();
        usersModelFirebase = new UsersModelFirebase();
    }


    public FirebaseAuth getAuthInstance() {
        return firebaseModel.firebaseAuth;
    }

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

    public void register(final User user, String password, AddUserListener listener) {
        usersModelFirebase.register(user, password, listener);
    }

    public void login(String email, String password, FirebaseModel.LoginUserListener loginUserListener) {
        firebaseModel.login(email, password, loginUserListener);
    }

    public void logOut(Callable<Void> function) {
        firebaseModel.logOut(function);
    }

    public void updateProfile(final String userName, final String bio, Repo.EditProfileListener listener) {
        usersModelFirebase.updateProfile(userName, bio, listener);
    }

    public void uploadImage(User user) {
        UsersModelFirebase.uploadImage(user);
    }
}

