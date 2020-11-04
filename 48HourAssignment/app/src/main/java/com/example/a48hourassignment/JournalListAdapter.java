package com.example.a48hourassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a48hourassignment.PubSubBroker.Broker;
import com.example.a48hourassignment.PubSubBroker.Subscriber;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.entryViewHolder> {

    private Broker broker;
    private Context context;
    private ArrayList<Entry> entries = new ArrayList<>();

    public JournalListAdapter(Context context) {
        this.context = context;
        implementPubSub();
        loadEntries();
    }

    @NonNull
    @Override
    public entryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.entry, parent, false);
        return new entryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull entryViewHolder holder, int position) {

        Entry currentEntry = entries.get(position);

        holder.entryDate.setText(currentEntry.getDate());
        holder.entryText.setText(currentEntry.getText());
        holder.entryImage.setImageResource(currentEntry.getImage());

        holder.entryCard.setOnClickListener(view->{
            Intent entryActivity = new Intent(context, JournalEntryActivity.class);

            entryActivity.putExtra("pos", position);
            entryActivity.putExtra("image", entries.get(position).getImage());
            entryActivity.putExtra("date", entries.get(position).getDate());
            entryActivity.putExtra("type", entries.get(position).getType());
            entryActivity.putExtra("text", entries.get(position).getText());

            context.startActivity(entryActivity);
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    private void loadEntries(){
        Toast.makeText(context, "loading...", Toast.LENGTH_SHORT).show();

        SharedPreferences preferenceImages = context.getSharedPreferences("entryImages", MODE_PRIVATE);
        SharedPreferences preferenceDates = context.getSharedPreferences("entryDates", MODE_PRIVATE);
        SharedPreferences preferenceTypes = context.getSharedPreferences("entryTypes", MODE_PRIVATE);
        SharedPreferences preferenceTexts = context.getSharedPreferences("entryTexts", MODE_PRIVATE);

        for (int i = 0; i < preferenceDates.getAll().size(); i++) {
            String pos = Integer.toString(i);
            Entry newEntry = new Entry(preferenceImages.getInt(pos, R.drawable.vet), preferenceDates.getString(pos, ""), preferenceTypes.getInt(pos, 0), preferenceTexts.getString(pos, ""));
            entries.add(newEntry);
        }
    }

    private void implementPubSub(){
        broker = Broker.getInstance();
        broker.subscribe("SaveEntry", (publisher, topic, params)->{
            // extract params
            Entry newEntry = (Entry)params.get("entry");
            int pos = (int)params.get("pos");

            if (pos >= entries.size()){
                entries.add(newEntry);
            } else {
                entries.set(pos, newEntry);
            }
            notifyDataSetChanged();
            Toast.makeText((Context)publisher, "Saved Successfully", Toast.LENGTH_SHORT).show();
        });
    }

    public Entry getEntryAt(int pos){
        return entries.get(pos);
    }

    public static class entryViewHolder extends RecyclerView.ViewHolder {
        TextView entryDate, entryText;
        ImageView entryImage;
        ConstraintLayout entryCard;

        public entryViewHolder(View parentView) {
            super(parentView);
            entryDate = parentView.findViewById(R.id.txtDate);
            entryText = parentView.findViewById(R.id.txtEntryText);
            entryImage = parentView.findViewById(R.id.imgEntryImage);

            entryCard = (ConstraintLayout)parentView;
        }
    }
}
