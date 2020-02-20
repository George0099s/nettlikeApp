package com.avla.app.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface TokenDao {

    @Query("SELECT * FROM TokenEntity")
    List<TokenEntity> getAll();

    @Query("SELECT token FROM TokenEntity")
    List<TokenEntity> getToken();

    @Insert
    void insert(TokenEntity token);

    @Update
    void update(TokenEntity token);

    @Delete
    void delete(TokenEntity token);

    @Query("DELETE FROM TokenEntity")
    void deleteAll();
}
