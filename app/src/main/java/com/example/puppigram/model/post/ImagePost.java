package com.example.puppigram.model.post;

import android.net.Uri;

import androidx.room.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
/**
 * Represent a image kind of post.
 */
public class ImagePost extends AbstractPost implements Serializable {
    public String postImage;

    public ImagePost(){
        super();

    }
    public ImagePost(String id, String ownerId, String description, String postImage, Long lastUpdate) {
        super(id, ownerId, description, lastUpdate);
        this.postImage = postImage;
    }

    public void fromMap(Map<String, Object> map) {
        this.setId((String) map.get("id"));
        this.setOwnerId((String) map.get("ownerId"));
        this.setPostImage((String) map.get("postImage"));
        this.setDescription((String) map.get("description"));
    }

    public Map<String, String> create() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", this.getId());
        result.put("ownerId", this.getOwnerId());
        result.put("description", this.getDescription());
        result.put("postImage", this.getPostImage());
        return result;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String image) {
        this.postImage = image;
    }
}
