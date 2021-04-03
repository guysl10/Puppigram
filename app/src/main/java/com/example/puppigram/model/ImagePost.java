package com.example.puppigram.model;

import java.io.Serializable;

/**
 * Represent a image kind of post.
 */
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
}
