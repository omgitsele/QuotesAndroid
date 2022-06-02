package com.example.quotes.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotes.R;
import com.example.quotes.models.Quote;
import com.example.quotes.repositories.QuoteRepository;
import com.example.quotes.viewmodels.QuoteViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteResultHolder> {
    private List<Quote> quotes = new ArrayList<>();
    private MutableLiveData<Quote> quoteLiveData = new MutableLiveData<>();
    private Context ctx;
    //private QuoteRepository quoteRepository;
    private QuoteViewModel quoteViewModel;
    private final ViewModelStoreOwner viewModelStoreOwner;

    public QuoteAdapter(Context ctx, ViewModelStoreOwner viewModelStoreOwner){
        this.ctx = ctx;
        this.viewModelStoreOwner = viewModelStoreOwner;
    }

    @NonNull
    @Override
    public QuoteResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        quoteViewModel = new ViewModelProvider(viewModelStoreOwner).get(QuoteViewModel.class);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quote_item, parent, false);
        return new QuoteResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteAdapter.QuoteResultHolder holder, int position) {
        Quote quote = quotes.get(position);
        //quoteRepository = new QuoteRepository((Application) ctx.getApplicationContext());

        holder.quoteText.setText(quote.getQuoteText());
        holder.quoteAuthor.setText(quote.getAuthor());

        holder.authorEditText.setText(quote.getAuthor());
        holder.quoteEditText.setText(quote.getQuoteText());
        holder.menuOptions.setOnClickListener( v -> {
            PopupMenu popup = new PopupMenu(ctx, holder.menuOptions);
            popup.inflate(R.menu.options_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.option_edit:
                        holder.quoteText.setVisibility(View.GONE);
                        holder.quoteAuthor.setVisibility(View.GONE);
                        holder.separatorText.setVisibility(View.GONE);

                        holder.quoteEditText.setVisibility(View.VISIBLE);
                        holder.authorEditText.setVisibility(View.VISIBLE);
                        holder.separatorEditText.setVisibility(View.VISIBLE);
                        holder.checkButton.setVisibility(View.VISIBLE);
                        holder.menuOptions.setVisibility(View.GONE);
                        return true;
                    case R.id.option_delete:
                        String id = quote.getId();
                        quoteViewModel.deleteQuote(id);
                        quotes.remove(quote);
                        setResults(quotes);
                        return true;
                    default:
                        return false;
                }
            });
            popup.setForceShowIcon(true);
            popup.show();

        });


        holder.checkButton.setOnClickListener(v ->{
            String newAuthor = holder.authorEditText.getText().toString();
            String newText = holder.quoteEditText.getText().toString();
            quote.setAuthor(newAuthor);
            quote.setQuoteText(newText);

            holder.quoteText.setText(newText);
            holder.quoteText.setVisibility(View.VISIBLE);
            holder.quoteAuthor.setText(newAuthor);
            holder.quoteAuthor.setVisibility(View.VISIBLE);
            holder.separatorText.setVisibility(View.VISIBLE);
            holder.menuOptions.setVisibility(View.VISIBLE);

            holder.quoteEditText.setVisibility(View.GONE);
            holder.authorEditText.setVisibility(View.GONE);
            holder.separatorEditText.setVisibility(View.GONE);
            holder.checkButton.setVisibility(View.GONE);


            quoteViewModel.updateQuote(quote);

        });

    }

    @Override
    public int getItemCount() {
        if(quotes!=null)
        return quotes.size();
        else return 0;
    }

    public void setResults(List<Quote> quotes) {
        this.quotes = quotes;
        notifyDataSetChanged();
    }



    class QuoteResultHolder extends RecyclerView.ViewHolder{
        private TextView quoteText, quoteAuthor;
        private ImageButton menuOptions;
        private ImageView checkButton;
        private View separatorText, separatorEditText;
        private EditText quoteEditText, authorEditText;


        public QuoteResultHolder(@NonNull View itemView) {
            super(itemView);

            quoteText = itemView.findViewById(R.id.quoteText);
            quoteAuthor = itemView.findViewById(R.id.quoteAuthor);
            menuOptions = itemView.findViewById(R.id.optionsMenu);

            checkButton = itemView.findViewById(R.id.check_button);

            quoteEditText = itemView.findViewById(R.id.edit_quote_text);
            authorEditText = itemView.findViewById(R.id.edit_author_text);

            separatorText = itemView.findViewById(R.id.separator_text);
            separatorEditText = itemView.findViewById(R.id.separator_edit_text);

        }
    }
}
