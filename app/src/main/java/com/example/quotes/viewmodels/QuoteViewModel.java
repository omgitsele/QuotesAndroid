package com.example.quotes.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.quotes.models.Quote;
import com.example.quotes.models.QuoteEntity;
import com.example.quotes.repositories.QuoteRepository;
import com.example.quotes.workers.MyWorker;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuoteViewModel extends AndroidViewModel {

    private QuoteRepository quoteRepository;
    private LiveData<List<Quote>> quoteLiveData;
    private LiveData<Throwable> throwableLiveData;
    private LiveData<Boolean> showProgressBar;
    private LiveData<Quote> singleQuoteLiveData;
    private LiveData<String> progressDialogText;
    private LiveData<Boolean> successLiveData;
    private LiveData<QuoteEntity> dailyQuoteLiveData;
    private WorkManager mWorkManager;
    private Application application;
    private Constraints constraint;
    private PeriodicWorkRequest workRequest;

    public QuoteViewModel(@NonNull Application application) {

        super(application);
        this.application = application;
    }

    public void init(QuoteRepository repository)
    {
        this.quoteRepository = repository;
        quoteLiveData = quoteRepository.getQuoteLiveData();
        throwableLiveData = quoteRepository.getThrowableLiveData();
        showProgressBar = quoteRepository.getShowProgressBar();
        singleQuoteLiveData = quoteRepository.getSingleQuoteLiveData();
        progressDialogText = quoteRepository.getProgressStringLiveData();
        successLiveData = quoteRepository.getSuccessLiveData();
        dailyQuoteLiveData = quoteRepository.getDailyQuote();
        mWorkManager = WorkManager.getInstance(application);

        constraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

         workRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.MINUTES)
                 .setInitialDelay(calculateDelay(), TimeUnit.MILLISECONDS)
                .setConstraints(constraint)
                .build();
         startWorker();


    }


    public void getAllQuotes() {
        quoteRepository.getAllQuotes();
    }

    public void getRandomQuote(){
        quoteRepository.getRandomQuote();
    }

    public void createNewQuote(String text, String author){
         quoteRepository.createNewQuote(text, author);
    }

    public LiveData<Quote> getSingleQuoteLiveData(){ return singleQuoteLiveData;}

    public LiveData<List<Quote>> getQuoteLiveData()
    {
        return  quoteLiveData;
    }

    public LiveData<Throwable> getThrowableLiveData()
    {
        return throwableLiveData;
    }

    public LiveData<Boolean> getShowProgressBar() { return showProgressBar; }

    public LiveData<String> getProgressDialog() { return progressDialogText; }

    public LiveData<Boolean> getSuccessLiveData() { return successLiveData; }

    public LiveData<QuoteEntity> getDailyQuoteLiveData() {

        return dailyQuoteLiveData; }

    public void insertDailyQuoteRoom(QuoteEntity quoteEntity){
        quoteRepository.insertQuote(quoteEntity);
    }

    public void updateQuote(Quote quote){
        quoteRepository.updateQuote(quote);
    }

    public void startWorker(){
        mWorkManager.enqueueUniquePeriodicWork("my_unique_worker", ExistingPeriodicWorkPolicy.REPLACE, workRequest);

    }

    public void deleteQuote(String id){
        quoteRepository.deleteQuote(id);
    }


    private long calculateDelay() {

        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, 23);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 0);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();


        // calculate how much time until the desirable time to run the worker
        long delta = Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis());

        Log.d("DELTA:", "IS: "+delta/1000/60);
        return (delta);
    }

}
