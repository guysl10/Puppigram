package com.example.puppigram.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.puppigram.model.dao.PostDao;

@Database(entities = {ImagePost.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}
public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApp.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
