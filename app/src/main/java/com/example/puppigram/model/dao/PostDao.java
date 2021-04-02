package com.example.puppigram.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.puppigram.model.AbstractPost;
import com.example.puppigram.model.ImagePost;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from ImagePost")
    List<ImagePost> getAllPosts();

    @Query("select * from ImagePost where owner_id == :userid")
    List<ImagePost> getAllUserPosts(int userid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPosts(ImagePost... posts);

    @Update
    void updatePost(ImagePost... post);

    @Delete
    void delete(ImagePost post);

    //TODO: update editpost function.
//    @Query("UPDATE imagePost SET description = :description, image = :image WHERE owner_id = :owner_id")
//    void editPost(ImagePost image_post);
}
