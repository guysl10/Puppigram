package com.example.puppigram.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a general post.
 */
//@Entity
public abstract class AbstractPost {
    /**
     * @param id: Post id.
     * @param ownerId: User own the post.
     * @param description: Title of the post.
     * @param likes: Users liked the post.
     */
    @PrimaryKey
    @NonNull
    private String id;
    private String ownerId;
    private String description;
    private Long lastUpdate;
    private ArrayList<String> likes;

    public AbstractPost(String id, String ownerId, String description) {
        this.id = id;
        this.ownerId = ownerId;
        this.description = description;
        likes = new ArrayList<>();
    }

    public AbstractPost() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<String> getLikes() {
        return this.likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public static class Connverter {


        @TypeConverter
        public String fromValuesToList(ArrayList<String> value) {
            if (value== null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.toJson(value, type);
        }

        @TypeConverter
        public ArrayList<String> toOptionValuesList(String value) {
            if (value== null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            return gson.fromJson(value, type);
        }
    }
}
