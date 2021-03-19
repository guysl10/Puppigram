package com.example.puppigram.model;

import java.util.HashSet;

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
    private int id;
    private int owner_id;
    private String description;
    private HashSet<Integer> likers;


    public AbstractPost(int id, int owner_id, String description){
        this.id = id;
        this.owner_id = owner_id;
        this.description = description;
        this.likers = null;
    }

    public int getNumLikers(){
        return this.likers.size();
    }

    public void removeLiker(int liker_id){
        this.likers.remove(liker_id);
    }

    public void addLiker(int liker_id){
        this.likers.add(liker_id);
    }

    public int getId() {
        return id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
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
