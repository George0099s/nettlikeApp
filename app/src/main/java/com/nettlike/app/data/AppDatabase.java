package com.nettlike.app.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TokenEntity.class, UserEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TokenDao tokenDao();
}
