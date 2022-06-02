package com.example.quotes.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.quotes.R;
import com.example.quotes.adapters.QuoteAdapter;
import com.example.quotes.viewmodels.QuoteViewModel;


public class QuoteFragment extends Fragment {

    private QuoteViewModel quoteViewModel;
    private QuoteAdapter quoteAdapter;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteAdapter = new QuoteAdapter(getActivity(), getActivity());
        quoteViewModel = new ViewModelProvider(getActivity()).get(QuoteViewModel.class);
        getAllQuotes();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quote, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.quotes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(quoteAdapter);
        // Create the pull to refresh listener
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            getAllQuotes();
            pullToRefresh.setRefreshing(false);
        });

        // Observe any changes on quotes.
        /*
        hint: https://stackoverflow.com/questions/59521691/use-viewlifecycleowner-as-the-lifecycleowner
         In observe why not use "observe(this, quote)"?
            ViewLifecycleOwner is tied to when the fragment has (and loses) its UI (onCreateView(), onDestroyView())
            • "this" is tied to the fragment's overall lifecycle (onCreate(), onDestroy()), which may be substantially longer
            • if onDestroyView() is called, but onDestroy() is not, we will continue observing the LiveData, perhaps crashing when we try populating a non-existent RecyclerView.
            By using viewLifecycleOwner, we avoid that risk.
         */
        quoteViewModel.getQuoteLiveData().observe(getViewLifecycleOwner(), quotes -> {
            quoteAdapter.setResults(quotes);
        });

        return view;
    }

    private void getAllQuotes() {
        quoteViewModel.getAllQuotes();
    }
}