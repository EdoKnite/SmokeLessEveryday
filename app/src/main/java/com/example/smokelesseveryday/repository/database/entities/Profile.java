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
    public int pricePerPack;
    public String currency;

    public Profile(int uid, String quittingDate, int cigarettesPerDay, int cigarettesInPack, int yearsOfSmoking, int pricePerPack, String currency, float moneySpentOnRewards) {
        this.uid = uid;
        this.quittingDate = quittingDate;
        this.cigarettesPerDay = cigarettesPerDay;
        this.cigarettesInPack = cigarettesInPack;
        this.yearsOfSmoking = yearsOfSmoking;
        this.pricePerPack = pricePerPack;
        this.currency = currency;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getQuittingDate() {
        return quittingDate;
    }

    public void setQuittingDate(String quittingDate) {
        this.quittingDate = quittingDate;
    }

    public int getCigarettesPerDay() {
        return cigarettesPerDay;
    }

    public void setCigarettesPerDay(int cigarettesPerDay) {
        this.cigarettesPerDay = cigarettesPerDay;
    }

    public int getCigarettesInPack() {
        return cigarettesInPack;
    }

    public void setCigarettesInPack(int cigarettesInPack) {
        this.cigarettesInPack = cigarettesInPack;
    }

    public int getYearsOfSmoking() {
        return yearsOfSmoking;
    }

    public void setYearsOfSmoking(int yearsOfSmoking) {
        this.yearsOfSmoking = yearsOfSmoking;
    }

    public int getPricePerPack() {
        return pricePerPack;
    }

    public void setPricePerPack(int pricePerPack) {
        this.pricePerPack = pricePerPack;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

