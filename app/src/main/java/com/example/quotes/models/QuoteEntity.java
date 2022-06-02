package com.example.quotes.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotes")
public class QuoteEntity {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "quote_text")
    public String quoteText;

    //Unnecessary to use the annotation but I like it.
    @ColumnInfo(name = "author")
    public String author;

    public QuoteEntity(@NonNull String id, @NonNull String quoteText, @NonNull String author ){
        this.quoteText = quoteText;
        this.id = id;
        this.author = author;
    }

}
