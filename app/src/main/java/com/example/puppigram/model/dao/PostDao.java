package com.example.puppigram.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.puppigram.model.AbstractPost;
import com.example.puppigram.model.ImagePost;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from ImagePost")
    List<ImagePost> getAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPosts(ImagePost... abstractPosts);

    @Delete
    void deletePost(ImagePost post);
}
