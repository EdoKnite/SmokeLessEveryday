package com.example.smokelesseveryday.repository.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    public String quittingDate;
    public int cigarettesPerDay;
    public int cigarettesInPack;
    public int yearsOfSmoking;
    public float pricePerPack;
    public String currency;

    public Profile(int uid, String quittingDate, int cigarettesPerDay, int cigarettesInPack, int yearsOfSmoking, float pricePerPack, String currency) {
        this.uid = uid;
        this.quittingDate = quittingDate;
        this.cigarettesPerDay = cigarettesPerDay;
        this.cigarettesInPack = cigarettesInPack;
        this.yearsOfSmoking = yearsOfSmoking;
        this.pricePerPack = pricePerPack;
        this.currency = currency;
    }
}

