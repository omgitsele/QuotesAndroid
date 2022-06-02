package com.example.quotes.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuotesResponse {

    @SerializedName("quotes")
    private List<Quote> quotesList = null;

    public List<Quote> getQuotesList()
    {
        return quotesList;
    }
}
