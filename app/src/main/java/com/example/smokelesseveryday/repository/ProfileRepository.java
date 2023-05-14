package com.example.smokelesseveryday.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.smokelesseveryday.repository.database.AppDatabase;
import com.example.smokelesseveryday.repository.database.dao.ProfileDao;
import com.example.smokelesseveryday.repository.database.entities.Profile;

import java.util.List;
import java.util.Objects;

public class ProfileRepository {
    private final ProfileDao profileDao;
    private final LiveData<List<Profile>> profiles;

    public ProfileRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        profileDao = db.ProfileDao();
        profiles = profileDao.getProfiles();
    }

    public Profile getProfile() {
        return Objects.requireNonNull(profiles.getValue()).get(0);

    }

    public void insertProfile(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDao.insertProfile(profile));
    }

    public void updateProfile(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDao.update(profile));
    }

}