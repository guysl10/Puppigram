package com.example.puppigram.model;

import com.example.puppigram.viewmodel.UserViewModel;

import java.util.ArrayList;

public class UserModel {
    public final static UserModel instance = new UserModel();
    UserViewModel viewModel = new UserViewModel();
    UsersModelFirebase modelFirebase = new UsersModelFirebase();

    public interface Listener<T> {
        void onComplete(T result);
    }

    public interface DeleteUserListener {
        void onComplete(boolean success);
    }

    public interface GetAllUsersListener {
        void onComplete(ArrayList<ImagePost> data);
    }

    public interface GetUserListener {
        void onComplete(ImagePost postModel);
    }

    public interface AddUserListener {
        void onComplete();
    }

    public interface UpdateUserListener extends PostsModel.AddPostListener {
    }
}
