package com.example.puppigram.model.post;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
/**
 * Represent a image kind of post.
 */
public class ImagePost extends AbstractPost implements Serializable, Parcelable {
    public String postImage;

    public ImagePost(){
        super();

    }

    protected ImagePost(Parcel in) {
        this.setId(in.readString());
        this.setPostImage(in.readString());
        this.setOwnerId(in.readString());
        this.setDescription(in.readString());
        this.setLastUpdate(in.readLong());
    }
    public ImagePost(String id, String ownerId, String description, String postImage, Long lastUpdate) {
        super(id, ownerId, description, lastUpdate);
        this.postImage = postImage;
    }

    public static final Creator<ImagePost> CREATOR = new Creator<ImagePost>() {
        @Override
        public ImagePost createFromParcel(Parcel in) {
            return new ImagePost(in);
        }

        @Override
        public ImagePost[] newArray(int size) {
            return new ImagePost[size];
        }
    };

    public void fromMap(Map<String, Object> map) {
        this.setId((String) map.get("id"));
        this.setOwnerId((String) map.get("ownerId"));
        this.setPostImage((String) map.get("postImage"));
        this.setDescription((String) map.get("description"));
    }

    public Map<String, String> create() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", this.getId());
        result.put("ownerId", this.getOwnerId());
        result.put("description", this.getDescription());
        result.put("postImage", this.getPostImage());
        return result;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String image) {
        this.postImage = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getId());
        dest.writeString(postImage);
        dest.writeString(this.getOwnerId());
        dest.writeString(this.getDescription());
        dest.writeLong(getLastUpdate());
    }
}
