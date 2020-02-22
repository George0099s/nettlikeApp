package com.nettlike.app.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    UserEntity getUser();

    @Insert
    long addUser(UserEntity user);

    @Delete
    void deleteDog(UserEntity user);

    @Update
    void updateDog(UserEntity user);
}
