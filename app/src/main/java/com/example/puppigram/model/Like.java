package com.example.puppigram.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Like implements Serializable {

    String id;
    String userId;
    String postId;

    public Like(String id, String userId, String postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }

    public Map<String, Object> create() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("userId", userId);
        result.put("postId", postId);
        return result;
    }

    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void add(){

    }
}
