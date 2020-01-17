package com.avla.app.Interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.avla.app.Entity.TokenEntity;

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
}
