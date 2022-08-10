package com.gptapptest.batchemailsender.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EmailList.class}, version=1)
public abstract class AppDB extends RoomDatabase {

    public abstract EmailListDao emailListDao();

    private static AppDB INSTANCE;

    public static AppDB getDbInstance(Context context, String dbName) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDB.class, dbName).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
