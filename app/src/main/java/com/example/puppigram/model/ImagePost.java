package com.example.puppigram.model;

/**
 * Represent a image kind of post.
 */
public class ImagePost extends AbstractPost{
    private String image;

    public ImagePost(int id, int owner_id, String description, String image) {
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
