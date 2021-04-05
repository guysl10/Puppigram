package com.example.puppigram.model;

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
    public ImagePost(String id, String ownerId, String description, Uri postImage) {
        super(id, ownerId, description);
        this.postImage = postImage.toString();
    }

    public Map<String, Object> create() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", this.getId());
        result.put("ownerId", this.getOwnerId());
        result.put("description", this.getDescription());
        result.put("postImage", postImage);
        return result;
    }

    public Uri getPostImage() {
        return Uri.parse(postImage);
    }

    public void setPostImage(Uri image) {
        this.postImage = image.toString();
    }
}
