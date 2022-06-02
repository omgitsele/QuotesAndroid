package com.example.quotes.repositories;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.services.storage.internal.TestStorageUtil;

import com.example.quotes.api.QuoteDao;
import com.example.quotes.models.Quote;
import com.example.quotes.models.QuoteEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class QuoteDatabaseTest {

        private QuoteDao quoteDao;
        private QuoteDatabase db;

        @Before
        public void createDb() {
            Context context = ApplicationProvider.getApplicationContext();
            db = Room.inMemoryDatabaseBuilder(context, QuoteDatabase.class).build();
            quoteDao = db.quoteDao();
        }

        @After
        public void closeDb() throws IOException {
            db.close();
        }

        @Test
        public void writeQuoteAndRead() throws Exception {
            QuoteEntity quote = new QuoteEntity("123", "Test text", "Test");
            quoteDao.insertDailyQuote(quote);
            QuoteEntity quote1 = quoteDao.getDailyQuote().getValue();
            assertNotNull(quote);
            assertNotNull(quote1);
            assertThat(quote1, equalTo(quote));

        }
    }

