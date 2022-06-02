package com.example.quotes.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quotes.api.QuoteDao;
import com.example.quotes.api.QuotesService;
import com.example.quotes.models.Quote;
import com.example.quotes.models.QuoteEntity;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuoteRepository {
    private static final String QUOTE_SERVICE_BASE_URL = "http://192.168.1.9:3001/";

    private QuotesService quotesService;
    private MutableLiveData<List<Quote>> quotesLiveData;
    private MutableLiveData<Throwable> throwableMutableLiveData;
    private MutableLiveData<Boolean> showProgressBarLiveData;
    private MutableLiveData<Quote> singleQuoteLiveData;
    private MutableLiveData<String> progressDialogText;
    private MutableLiveData<Boolean> successLiveData;
    private QuoteDao quoteDao;
    private LiveData<QuoteEntity> dailyLiveData;

    public QuoteRepository(Application application)
    {

        // Setup room database
        QuoteDatabase db = QuoteDatabase.getDatabase(application);
        quoteDao = db.quoteDao();
        dailyLiveData = quoteDao.getDailyQuote();

        quotesLiveData = new MutableLiveData<>();
        throwableMutableLiveData = new MutableLiveData<>();
        showProgressBarLiveData = new MutableLiveData<>();
        singleQuoteLiveData = new MutableLiveData<>();
        progressDialogText = new MutableLiveData<>();
        successLiveData = new MutableLiveData<>();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        quotesService = new Retrofit.Builder()
                .baseUrl(QUOTE_SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuotesService.class);


    }



    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<QuoteEntity> getDailyQuote() {
        return dailyLiveData;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertQuote(QuoteEntity quote) {
        QuoteDatabase.databaseWriteExecutor.execute(() -> {
            quoteDao.insertDailyQuote(quote);
        });
    }

    public void dropDailyQuote(){
        QuoteDatabase.databaseWriteExecutor.execute(()->{
            quoteDao.dropDailyQuote();
        });
    }

    public void updateQuote(Quote quote){
        progressDialogText.postValue("Updating Quote");
        showProgressBarLiveData.postValue(true);
        quotesService.updateQuote(quote.getId(), quote).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showProgressBarLiveData.postValue(false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showProgressBarLiveData.postValue(false);
                throwableMutableLiveData.postValue(t);
            }
        });
    }


    public void deleteQuote(String id){
        progressDialogText.postValue("Deleting Quote");
        showProgressBarLiveData.postValue(true);
        quotesService.deleteQuote(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (dailyLiveData.getValue()!=null)
                            if(id.equals(dailyLiveData.getValue().id))
                            {
                                dropDailyQuote();
                            }
                        showProgressBarLiveData.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showProgressBarLiveData.postValue(false);
                        throwableMutableLiveData.postValue(t);
                    }
                });
    }

    public void createNewQuote(String text, String author){
        progressDialogText.postValue("Creating new quote");
        showProgressBarLiveData.postValue(true);
        quotesService.createQuote(new Quote(text,author))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        showProgressBarLiveData.postValue(false);
                        successLiveData.postValue(true);
                        System.out.println("SUCCESS CREATING QUOTE!");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showProgressBarLiveData.postValue(false);
                        throwableMutableLiveData.postValue(t);
                        successLiveData.postValue(false);

                    }
                });
    }

    public void getRandomQuote()
    {
        progressDialogText.postValue("Getting random quote");
        showProgressBarLiveData.postValue(true);
        quotesService.getRandomQuote()
                .enqueue(new Callback<Quote>() {
                    @Override
                    public void onResponse(Call<Quote> call, Response<Quote> response) {
                        showProgressBarLiveData.postValue(false);
                        singleQuoteLiveData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Quote> call, Throwable t) {
                        showProgressBarLiveData.postValue(false);
                        throwableMutableLiveData.postValue(t);
                    }
                });
    }

    public void getAllQuotes()
    {
        progressDialogText.postValue("Getting all quotes");
        showProgressBarLiveData.postValue(true);
        quotesService.getAllQuotes()
                .enqueue(new Callback<List<Quote>>() {
                    @Override
                    public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                        if (response.body() != null)
                        {
                            showProgressBarLiveData.postValue(false);
                            quotesLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Quote>> call, Throwable t) {
                        showProgressBarLiveData.postValue(false);
                        throwableMutableLiveData.postValue(t);
                        quotesLiveData.postValue(null);
                    }
                });
    }

    public LiveData<List<Quote>> getQuoteLiveData()
    {
        return quotesLiveData;
    }

    public LiveData<Quote> getSingleQuoteLiveData(){
        return singleQuoteLiveData;
    }

    public LiveData<Throwable> getThrowableLiveData()
    {
        return throwableMutableLiveData;
    }

    public LiveData<Boolean> getShowProgressBar(){ return showProgressBarLiveData; }

    public LiveData<String> getProgressStringLiveData(){ return progressDialogText;}

    public LiveData<Boolean> getSuccessLiveData(){ return  successLiveData;}


}
