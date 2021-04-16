package com.example.puppigram.viewmodel;

import com.example.puppigram.model.User;

import java.util.ArrayList;

public class UserViewModel {
    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
