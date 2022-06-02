package com.example.quotes.repositories;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.quotes.api.QuoteDao;
import com.example.quotes.models.QuoteEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {QuoteEntity.class}, version = 1)
public abstract class QuoteDatabase extends RoomDatabase {
    public abstract QuoteDao quoteDao();

    private static volatile QuoteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static QuoteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            QuoteDatabase.class, "quotes")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
