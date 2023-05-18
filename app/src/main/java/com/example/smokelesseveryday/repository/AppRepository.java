package com.example.smokelesseveryday.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.smokelesseveryday.repository.database.SmokeLessDatabase;
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

    public AppRepository(Application application) {
        SmokeLessDatabase db = SmokeLessDatabase.getDatabase(application);
        profileDao = db.ProfileDao();
        healthDao = db.HealthDao();

        profiles = profileDao.getProfiles();
        achievements = healthDao.getAchievements();
    }

    public LiveData<List<Health>> getAchievements() {
        return achievements;
    }

    public void insertAchievement(Health... health){
        SmokeLessDatabase.databaseWriteExecutor.execute(() -> healthDao.insertAchievement(health));
    }

    public void updateAchievement(Health health){
        SmokeLessDatabase.databaseWriteExecutor.execute(() -> healthDao.update(health));
    }

    public void deleteAchievement(Health health){
        SmokeLessDatabase.databaseWriteExecutor.execute(() -> healthDao.delete(health));
    }

    public Profile getProfile() {
        return Objects.requireNonNull(profiles.getValue()).get(0);

    }

    public void insertProfile(Profile profile) {
        SmokeLessDatabase.databaseWriteExecutor.execute(() -> profileDao.insertProfile(profile));
    }

    public void updateProfile(Profile profile) {
        SmokeLessDatabase.databaseWriteExecutor.execute(() -> profileDao.update(profile));
    }

}