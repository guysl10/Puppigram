package com.example.puppigram.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String id;
    private String name;
    private String userName;
    private Uri userImage;
    private String email;
    private String bio;

    public User(String id, String name, String userName, String email, Uri userImage, String bio) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.userImage = userImage;
        this.bio = bio;
    }

    public Map<String, Object> create() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("userName", userName);
        result.put("userImage", userImage);
        result.put("email", email);
        result.put("bio", bio);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getUserImage() {
        return userImage;
    }

    public void setUserImage(Uri userImage) {
        this.userImage = userImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
