package com.example.puppigram.model;

public abstract class AbstractPost {
    public int id;
    public int owner_id;
    public String title;

    //TODO: add comments of post.
    //TODO: add likes (number & who liked the post).

    public AbstractPost(int id, int owner_id, String title){
        this.id = id;
        this.owner_id = owner_id;
        this.title = title;
    }
}
