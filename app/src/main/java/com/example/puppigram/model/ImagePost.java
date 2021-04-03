package com.example.puppigram.model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.room.Entity;

import java.util.List;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
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
//    private ImageView image;
//
//    public ImagePost(int id, int owner_id, String description, ImageView image) {
//        super(id, owner_id, description);
//        this.image = image;
//    }
//
//    public ImageView getImage() {
//        return image;
//    }
//
//    public void setImage(ImageView image) {
//        this.image = image;
//    }
}
