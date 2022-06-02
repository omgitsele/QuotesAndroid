package com.example.quotes.ui;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotes.R;
import com.example.quotes.adapters.QuoteAdapter;
import com.example.quotes.models.Quote;
import com.example.quotes.models.QuoteEntity;
import com.example.quotes.viewmodels.QuoteViewModel;
import com.example.quotes.workers.MyWorker;

import java.util.concurrent.TimeUnit;


public class SingleQuoteFragment extends Fragment {

    private QuoteViewModel quoteViewModel;
    private RelativeLayout relativeLayout;
    private TextView quoteText, quoteAuthor, dailyQuoteTitle;
    private ImageView checkButton;
    private ImageButton optionsMenu;
    private View separatorText, separatorEditText;
    private EditText quoteEditText, authorEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteViewModel = new ViewModelProvider(getActivity()).get(QuoteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.quote_item, container, false);

        // Set layout_height to "MATCH_PARENT"
        relativeLayout = view.findViewById(R.id.item_relative_layout);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        quoteText = view.findViewById(R.id.quoteText);
        quoteAuthor = view.findViewById(R.id.quoteAuthor);

        optionsMenu = view.findViewById(R.id.optionsMenu);

        dailyQuoteTitle = view.findViewById(R.id.daily_title);
        dailyQuoteTitle.setVisibility(View.VISIBLE);

        checkButton = view.findViewById(R.id.check_button);

        quoteEditText = view.findViewById(R.id.edit_quote_text);
        authorEditText = view.findViewById(R.id.edit_author_text);

        separatorText = view.findViewById(R.id.separator_text);
        separatorEditText = view.findViewById(R.id.separator_edit_text);

        optionsMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), optionsMenu);
            popup.inflate(R.menu.options_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.option_edit:
                        quoteText.setVisibility(View.GONE);
                        quoteAuthor.setVisibility(View.GONE);
                        separatorText.setVisibility(View.GONE);

                        quoteEditText.setVisibility(View.VISIBLE);
                        authorEditText.setVisibility(View.VISIBLE);
                        separatorEditText.setVisibility(View.VISIBLE);
                        checkButton.setVisibility(View.VISIBLE);
                        optionsMenu.setVisibility(View.GONE);
                        return true;
                    case R.id.option_delete:
                        if (quoteViewModel.getDailyQuoteLiveData().getValue()!=null) {
                            quoteViewModel.deleteQuote(quoteViewModel.getDailyQuoteLiveData().getValue().id);
                            getRandomQuote();
                        }
                        return true;
                    default:
                        return false;
                }
            });
            popup.setForceShowIcon(true);
            popup.show();
        });


        checkButton.setOnClickListener(v ->{
            String newText = quoteEditText.getText().toString();
            String newAuthor = authorEditText.getText().toString();
            quoteText.setText(newText);
            quoteAuthor.setText(newAuthor);
            quoteText.setVisibility(View.VISIBLE);
            quoteAuthor.setVisibility(View.VISIBLE);
            separatorText.setVisibility(View.VISIBLE);

            optionsMenu.setVisibility(View.VISIBLE);

            quoteEditText.setVisibility(View.GONE);
            authorEditText.setVisibility(View.GONE);
            separatorEditText.setVisibility(View.GONE);
            checkButton.setVisibility(View.GONE);
            if (quoteViewModel.getDailyQuoteLiveData().getValue()!=null) {
                Quote quote = new Quote(quoteEditText.getText().toString(), authorEditText.getText().toString());
                quote.setId(quoteViewModel.getDailyQuoteLiveData().getValue().id);
                quoteViewModel.updateQuote(quote);
            }


        });

        // Observe any changes on quote.
        quoteViewModel.getSingleQuoteLiveData().observe(getViewLifecycleOwner(), quote -> {
            quoteText.setText(quote.getQuoteText());
            quoteAuthor.setText(quote.getAuthor());
            authorEditText.setText(quote.getAuthor());
            quoteEditText.setText(quote.getQuoteText());
            QuoteEntity quoteEntity = new QuoteEntity(quote.getId(), quote.getQuoteText(), quote.getAuthor());
            quoteViewModel.insertDailyQuoteRoom(quoteEntity);
        });

        quoteViewModel.getDailyQuoteLiveData().observe(getViewLifecycleOwner(), quoteEntity -> {
            if (quoteEntity != null) {
                quoteText.setText("");
                quoteAuthor.setText("");
                String text = quoteText.getText().toString().concat(" "+quoteEntity.quoteText);
                String author = quoteAuthor.getText().toString().concat(" "+quoteEntity.author);
                quoteText.setText(text);
                quoteAuthor.setText(author);
                quoteEditText.setText(text);
                authorEditText.setText(author);
            }
            else
            {
                getRandomQuote();
            }
        });

        return view;
    }

    private void getRandomQuote() {
        quoteViewModel.getRandomQuote();
    }

}