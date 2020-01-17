package com.avla.app.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.avla.app.Entity.TokenEntity;
import com.avla.app.Interface.TokenDao;

@Database(entities = {TokenEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TokenDao tokenDao();

}
