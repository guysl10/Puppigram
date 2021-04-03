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
     * @param id: Post id.
     * @param ownerId: User own the post.
     * @param description: Title of the post.
     * @param likers: Users liked the post.
     */
    @PrimaryKey
    @NonNull
    private String id;
    private String ownerId;
    private String description;

    public AbstractPost(String id, String ownerId, String description) {
        this.id = id;
        this.ownerId = ownerId;
        this.description = description;
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
}
