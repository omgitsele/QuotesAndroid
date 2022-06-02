package com.example.quotes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {
    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("text")
    @Expose
    private String quoteText;

    @SerializedName("id")
    @Expose
    private String id;

    public Quote(String quoteText, String author)
    {
        this.quoteText = quoteText;
        this.author = author;
    }

    public Quote(){}

    public String getAuthor() {
        return author;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author){ this.author = author;}

    public void setQuoteText(String text){this.quoteText = text;}
}
