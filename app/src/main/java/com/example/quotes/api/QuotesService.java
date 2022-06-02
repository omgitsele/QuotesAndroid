package com.example.quotes.api;

import com.example.quotes.models.Quote;
import com.example.quotes.models.QuotesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface QuotesService {

    @GET("/api/quotes/")
    Call<List<Quote>> getAllQuotes();

    @GET("/api/quotes/{id}")
    Call<Quote> getSingleQuote(@Path("id") String id);

    @GET("/api/quotes/random")
    Call<Quote> getRandomQuote();

    @POST("/api/quotes/")
    Call<Void> createQuote(@Body Quote quote);

    @DELETE("/api/quotes/{id}")
    Call<Void> deleteQuote(@Path("id") String id);

    @PUT("/api/quotes/{id}")
    Call<Void> updateQuote(@Path("id") String id, @Body Quote quote);

}
