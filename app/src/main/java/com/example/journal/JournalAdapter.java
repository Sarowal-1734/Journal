package com.example.journal;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Journal> journals;

    public JournalAdapter(Context context, ArrayList<Journal> journals) {
        this.context = context;
        this.journals = journals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.journal_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Journal journal = journals.get(position);
        String imageUrl = journal.getImageUrl();
        // Picasso library for download & show image
        Picasso.get().load(imageUrl).placeholder(R.drawable.image).fit().into(holder.imageView);

        holder.titleTV.setText(journal.getTitle());
        holder.thoughtTV.setText(journal.getThought());

        // Adding time ago format
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(journal.getTimeAdded().getSeconds()*1000);
        holder.timeAddedTV.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView titleTV, thoughtTV, timeAddedTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.journalImage);
            titleTV = itemView.findViewById(R.id.journalTitle);
            thoughtTV = itemView.findViewById(R.id.journalThought);
            timeAddedTV = itemView.findViewById(R.id.journalDate);
        }
    }
}
