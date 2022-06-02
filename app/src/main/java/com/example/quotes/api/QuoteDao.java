package com.example.quotes.api;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quotes.models.Quote;
import com.example.quotes.models.QuoteEntity;

import java.util.List;

@Dao
public interface QuoteDao {

    @Query("Select * FROM quotes")
    LiveData<QuoteEntity> getDailyQuote();

    @Query("DELETE FROM quotes")
    void dropDailyQuote();

    @Insert
    void insertDailyQuote(QuoteEntity quote);


}
