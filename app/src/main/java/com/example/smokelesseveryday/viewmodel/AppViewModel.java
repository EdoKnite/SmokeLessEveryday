package com.example.smokelesseveryday.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smokelesseveryday.repository.AppRepository;
import com.example.smokelesseveryday.repository.database.entities.Health;
import com.example.smokelesseveryday.repository.database.entities.Profile;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private final AppRepository appRepository;

    public AppViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);

    }

    public LiveData<List<Health>> getAchievements() {
        return appRepository.getAchievements();
    }

    public void insertAchievement(Health... health) {
        appRepository.insertAchievement(health);
    }

    public void updateAchievement(Health health) {
        appRepository.updateAchievement(health);
    }

    public void deleteAchievement(Health health) {
        appRepository.deleteAchievement(health);
    }

    public LiveData<List<Profile>> getProfiles() {
        return appRepository.getProfiles();

    }

    public void insertProfile(Profile profile) {
        appRepository.insertProfile(profile);
    }

    public void updateProfile(Profile profile) {
        appRepository.updateProfile(profile);
    }
}
