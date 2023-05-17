package com.example.smokelesseveryday.repository.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Health {
    @NonNull
    @PrimaryKey
    public String title;
    public int percent;
    public String status;
    public String description;

    public Health(@NonNull String title, int percent, String status, String description) {
        this.title = title;
        this.percent = percent;
        this.status = status;
        this.description = description;
    }
}
