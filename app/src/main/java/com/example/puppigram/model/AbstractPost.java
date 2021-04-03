package com.example.puppigram.model;

import java.util.HashSet;

/**
 * Represent a general post.
 */
public abstract class AbstractPost {
    /**
     * @param id: Post id.
     * @param owner_id: User own the post.
     * @param title: Title of the post.
     * @param likers: Users liked the post.
     */
    private String id;
    private String ownerId;
    private String description;
    private HashSet<Integer> likers;


    public AbstractPost(String id, String ownerId, String description) {
        this.id = id;
        this.ownerId = ownerId;
        this.description = description;
        this.likers = null;
    }

    public int getNumLikers() {
        return this.likers.size();
    }

    public void removeLiker(int liker_id) {
        this.likers.remove(liker_id);
    }

    public void addLiker(int liker_id) {
        this.likers.add(liker_id);
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashSet<Integer> getLikers() {
        return likers;
    }

    public void setLikers(HashSet<Integer> likers) {
        this.likers = likers;
    }
}
