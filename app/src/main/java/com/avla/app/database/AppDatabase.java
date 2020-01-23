package com.avla.app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.avla.app.entity.TokenEntity;
import com.avla.app.Interface.TokenDao;

@Database(entities = {TokenEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TokenDao tokenDao();

}
