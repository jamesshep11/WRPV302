package com.example.a48hourassignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.entryViewHolder> {

    private Context context;
    private ArrayList<Entry> entries = new ArrayList<>();

    public JournalListAdapter(Context context) {
        this.context = context;
        loadEntries();
    }

    private void loadEntries(){
        SharedPreferences preferenceImages = context.getSharedPreferences("entryImages", MODE_PRIVATE);
        SharedPreferences preferenceDates = context.getSharedPreferences("entryDates", MODE_PRIVATE);
        SharedPreferences preferenceTypes = context.getSharedPreferences("entryTypes", MODE_PRIVATE);
        SharedPreferences preferenceTexts = context.getSharedPreferences("entryTexts", MODE_PRIVATE);

        for (int i = 0; i < preferenceDates.getAll().size(); i++) {
            String pos = Integer.toString(i);
            Entry newEntry = new Entry(preferenceImages.getInt(pos, R.drawable.vet), preferenceDates.getString(pos, ""), preferenceTypes.getString(pos, ""), preferenceTexts.getString(pos, ""));
            entries.add(newEntry);
        }
    }

    @NonNull
    @Override
    public entryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.journal_entry, parent, false);
        return new entryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull entryViewHolder holder, int position) {

        Entry currentEntry = entries.get(position);

        holder.entryDate.setText(currentEntry.getDate());
        holder.entryText.setText(currentEntry.getText());
        holder.entryImage.setImageResource(currentEntry.getImage());
    }

    @Override
    public int getItemCount() {
        return entries.size()-1;
    }

    public static class entryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView entryDate, entryText;
        ImageView entryImage;

        public entryViewHolder(View parentView) {
            super(parentView);
            entryDate = parentView.findViewById(R.id.txtDate);
            entryText = parentView.findViewById(R.id.txtEntryText);
            entryImage = parentView.findViewById(R.id.imgEntryImage);
        }
    }
}
