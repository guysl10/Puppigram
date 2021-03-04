package com.example.puppigram.model;

/**
 * Represent a general post.
 */
public abstract class AbstractPost {
    /**
     *
     * @param id: Post id.
     * @param owner_id: User own the post.
     * @param title: Title of the post.
     * @param likers: Users liked the post.
     */
    public int id;
    public int owner_id;
    public String title;
    public AbstractUser[] likers;
    public Comment[] comments;


    public AbstractPost(int id, int owner_id, String title){
        this.id = id;
        this.owner_id = owner_id;
        this.title = title;
        this.likers = null;
        this.comments = null;
    }
}
