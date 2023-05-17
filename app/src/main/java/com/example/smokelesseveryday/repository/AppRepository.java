package com.example.smokelesseveryday.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.smokelesseveryday.repository.database.AppDatabase;
import com.example.smokelesseveryday.repository.database.dao.HealthDao;
import com.example.smokelesseveryday.repository.database.dao.ProfileDao;
import com.example.smokelesseveryday.repository.database.entities.Health;
import com.example.smokelesseveryday.repository.database.entities.Profile;

import java.util.List;
import java.util.Objects;

public class AppRepository {
    private final ProfileDao profileDao;
    private final HealthDao healthDao;
    private final LiveData<List<Profile>> profiles;
    private final LiveData<List<Health>> achievements;
    private static AppRepository INSTANCE;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        profileDao = db.ProfileDao();
        healthDao = db.HealthDao();

        profiles = profileDao.getProfiles();
        achievements = healthDao.getAchievements();
    }

    public static AppRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(application);
        }
        return INSTANCE;
    }

    public LiveData<List<Health>> getAchievements() {
        return achievements;
    }

    public void insertAchievement(Health... health){
        AppDatabase.databaseWriteExecutor.execute(() -> healthDao.insertAchievement(health));
    }

    public void updateAchievement(Health health){
        AppDatabase.databaseWriteExecutor.execute(() -> healthDao.update(health));
    }

    public void deleteAchievement(Health health){
        AppDatabase.databaseWriteExecutor.execute(() -> healthDao.delete(health));
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