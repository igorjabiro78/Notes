package com.example.notes.Database_for_notification;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insertAll(EntityClass entityClass);

    @Query("SELECT * FROM myTable")
    List<EntityClass> getAllData();

}
