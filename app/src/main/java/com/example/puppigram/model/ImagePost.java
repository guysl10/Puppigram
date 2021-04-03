package com.example.puppigram.model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.room.Entity;

import java.util.List;
import java.io.Serializable;


/**
 * Represent a image kind of post.
 */
@Entity
public class ImagePost extends AbstractPost implements Serializable {
    private String image;

    public ImagePost(String id, String owner_id, String description, String image) {
        super(id, owner_id, description);
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
