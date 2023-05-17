package com.example.smokelesseveryday.repository.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smokelesseveryday.repository.database.entities.Health;

import java.util.List;

@Dao
public interface HealthDao {

    @Insert
    void insertAchievement(Health... health);

    @Query("SELECT * FROM health")
    LiveData<List<Health>> getAchievements();

    @Update
    void update(Health health);

    @Delete
    void delete(Health health);
}
