package com.nettlike.app.data;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class From1To2 extends Migration {
    public From1To2() {
        super(1, 2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `user` (`first name` TEXT, `last name` TEXT,`age` TEXT, `about myself` TEXT, `country` TEXT, `city` TEXT, " +
                "`picture id`, `email` TEXT, `id` TEXT NOT NULL , `job title` TEXT, PRIMARY KEY(`id`))");
    }
}