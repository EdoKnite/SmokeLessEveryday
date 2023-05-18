package com.example.smokelesseveryday.repository.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smokelesseveryday.repository.database.dao.HealthDao;
import com.example.smokelesseveryday.repository.database.dao.ProfileDao;
import com.example.smokelesseveryday.repository.database.entities.Health;
import com.example.smokelesseveryday.repository.database.entities.Profile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Profile.class, Health.class}, version = 1)
public abstract class SmokeLessDatabase extends RoomDatabase {
    public abstract ProfileDao ProfileDao();

    public abstract HealthDao HealthDao();

    private static volatile SmokeLessDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static SmokeLessDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SmokeLessDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    SmokeLessDatabase.class, "smoke-less-db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                HealthDao healthDao = INSTANCE.HealthDao();
                ProfileDao profileDao = INSTANCE.ProfileDao();

                healthDao.insertAchievement(
                        new Health("After 20 minutes", 0, "In 20 minutes",
                                "Your blood pressure and heart rate drop."),
                        new Health("After 8 hours", 0, "In 8 hours",
                                "The level of carbon monoxide in your blood is back to normal."));

                profileDao.insertProfile(new Profile(2, "string", 20, 20, 5, 20, "RON"));

            });
        }
    };
}
