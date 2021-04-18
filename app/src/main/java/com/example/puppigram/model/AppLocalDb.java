package com.example.puppigram.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.puppigram.adapters.PostAdapter;
import com.example.puppigram.model.dao.PostDao;

@Database(entities = {ImagePost.class}, version = 3)
@TypeConverters({AbstractPost.Connverter.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApp.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
