package com.example.puppigram.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashSet;


/**
 * Represent a general post.
 */
@Entity
public abstract class AbstractPost {
    /**
     *
     * @param id: Post id.
     * @param owner_id: User own the post.
     * @param title: Title of the post.
     * @param likers: Users liked the post.
     */
    @PrimaryKey
    @NonNull
    private int id;
    private int owner_id;
    private String description;
    private Integer likers;


    public AbstractPost(int id, int owner_id, String description){
        this.id = id;
        this.owner_id = owner_id;
        this.description = description;
        this.likers = null;
    }

//    public int getNumLikers(){
//        return this.likers.size();
//    }

    public Integer getLikers(){return this.likers;}

    public void removeLiker(int liker_id){
        this.likers--;
    }

    public void addLiker(int liker_id){
        this.likers++;
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

    public void setLikers(Integer likers) {
        this.likers = likers;
    }
}
