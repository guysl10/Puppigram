package com.example.puppigram.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String name;
    private String userName;
    private String email;
    private String imageUri;
    private String bio;
    private String id;

    public User() {
    }

    public User(String id, String name, String userName, String email, String imageUri, String bio) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.imageUri = imageUri;
        this.bio = bio;
    }


    public Map<String, Object> create() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("userName", userName);
        result.put("email", email);
        result.put("imageUri", imageUri);
        result.put("bio", bio);
        return result;
    }

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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

}
