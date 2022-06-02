package com.example.quotes.workers;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.quotes.api.QuotesService;
import com.example.quotes.repositories.QuoteRepository;
import com.example.quotes.viewmodels.QuoteViewModel;
import com.google.common.util.concurrent.ListenableFuture;

public class MyWorker extends Worker {

    QuoteRepository quoteRepository;

    public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        quoteRepository = new QuoteRepository((Application) getApplicationContext());
        quoteRepository.dropDailyQuote();
        return Result.success();
    }


}
