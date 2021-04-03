package com.example.puppigram.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent a image kind of post.
 */
public class ImagePost extends AbstractPost implements Serializable {
    private String postImage;

    public ImagePost(String id, String ownerId, String description, String postImage) {
        super(id, ownerId, description);
        this.postImage = postImage;
    }

    public Map<String, Object> create() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", this.getId());
        result.put("ownerId", this.getOwnerId());
        result.put("description", this.getDescription());
        result.put("postImage", postImage);
        return result;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String image) {
        this.postImage = image;
    }
}
