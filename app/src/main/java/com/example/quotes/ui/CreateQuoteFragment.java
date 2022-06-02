package com.example.quotes.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotes.R;
import com.example.quotes.adapters.QuoteAdapter;
import com.example.quotes.viewmodels.QuoteViewModel;

public class CreateQuoteFragment extends Fragment {

    private QuoteViewModel quoteViewModel;
    private Button createButton;
    private EditText quoteEditText, authorEditText;
    private String CREATE_NEW_QUOTE ;

    public CreateQuoteFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CREATE_NEW_QUOTE = getString(R.string.create_new_quote);

        quoteViewModel = new ViewModelProvider(getActivity()).get(QuoteViewModel.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_quote, container, false);

        createButton = view.findViewById(R.id.create_btn);

        quoteEditText = view.findViewById(R.id.edit_quote_text);
        authorEditText = view.findViewById(R.id.edit_author_text);

        quoteViewModel.getSuccessLiveData().observe(getViewLifecycleOwner(), success ->{
            if (success){
                quoteEditText.setText("");
                authorEditText.setText("");
            }
        });


        createButton.setOnClickListener(v ->{
            String author;
            String text = quoteEditText.getText().toString();
            if (text.isEmpty())
                Toast.makeText(getContext(), "Quote cannot be empty!", Toast.LENGTH_SHORT).show();
            else {
                author = authorEditText.getText().toString();
                if (author.isEmpty())
                    author = "Unknown";
                createNewQuote(text, author);


            }
        });

        return view;
    }

    public void createNewQuote(String text, String author){
        quoteViewModel.createNewQuote(text, author);
    }
}