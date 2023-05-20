package com.example.smokelesseveryday.repository.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smokelesseveryday.repository.database.entities.Profile;

import java.util.List;

@Dao
public interface ProfileDao {
    @Insert
    void insertProfile(Profile profile);

    @Query("SELECT * FROM profile")
    LiveData<List<Profile>> getProfiles();

    @Update
    void update(Profile... profile);

}
