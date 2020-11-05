package com.example.a48hourassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a48hourassignment.PubSubBroker.Broker;
import com.example.a48hourassignment.PubSubBroker.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.entryViewHolder> {

    private Broker broker;
    private Context context;

    public JournalListAdapter(Context context) {
        this.context = context;
        implementPubSub();
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

        Entry currentEntry = getEntryAt(position);

        holder.entryDate.setText(currentEntry.getDate());
        holder.entryText.setText(currentEntry.getText());
        loadImgToView(holder.entryImage, currentEntry.getImage());
        if (!currentEntry.hasPhoto()) {
            ViewGroup.LayoutParams imageViewParams = holder.entryImage.getLayoutParams();
            imageViewParams.height *= 0.5;
            holder.entryImage.setLayoutParams(imageViewParams);
        }

        holder.entryCard.setOnClickListener(view->{
            Intent entryActivity = new Intent(context, JournalEntryActivity.class);

            entryActivity.putExtra("pos", position);
            entryActivity.putExtra("image", currentEntry.getImage());
            entryActivity.putExtra("date", currentEntry.getDate());
            entryActivity.putExtra("type", currentEntry.getType());
            entryActivity.putExtra("text", currentEntry.getText());
            entryActivity.putExtra("hasPhoto", currentEntry.hasPhoto());

            context.startActivity(entryActivity);
        });

        // Link entryType with entry colour
        switch (currentEntry.getType()){
            case 0: holder.entryCard.setBackgroundResource(R.color.vet);
                break;
            case 1: holder.entryCard.setBackgroundResource(R.color.medication);
                break;
            case 2: holder.entryCard.setBackgroundResource(R.color.appointment);
                break;
            case 3: holder.entryCard.setBackgroundResource(R.color.selfie);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return context.getSharedPreferences("entryImages", MODE_PRIVATE).getAll().size();
    }

    private void implementPubSub(){
        broker = Broker.getInstance();
        broker.subscribe("SaveEntry", (publisher, topic, params)->{
            // extract params
            Entry newEntry = (Entry)params.get("entry");
            int pos = (int)params.get("pos");

            putEntryAt(pos, newEntry);

            sortList();

            notifyDataSetChanged();
            notifyItemRangeChanged(0, getItemCount());

            Toast.makeText((Context)publisher, R.string.save_successful, Toast.LENGTH_SHORT).show();
        });
    }

    private Entry getEntryAt(int position){
        String pos = Integer.toString(position);

        SharedPreferences preferenceImages = context.getSharedPreferences("entryImages", MODE_PRIVATE);
        SharedPreferences preferenceDates = context.getSharedPreferences("entryDates", MODE_PRIVATE);
        SharedPreferences preferenceTypes = context.getSharedPreferences("entryTypes", MODE_PRIVATE);
        SharedPreferences preferenceTexts = context.getSharedPreferences("entryTexts", MODE_PRIVATE);
        SharedPreferences preferenceHasPhoto = context.getSharedPreferences("entryHasPhoto", MODE_PRIVATE);

        String image = preferenceImages.getString(pos, Integer.toString(R.drawable.vet));
        String date = preferenceDates.getString(pos, "");
        int type = preferenceTypes.getInt(pos, 0);
        String text = preferenceTexts.getString(pos, "");
        Boolean hasPhoto = preferenceHasPhoto.getBoolean(pos, false);

        Entry entry = new Entry(image, date, type, text);
        entry.setHasPhoto(hasPhoto);

        return entry;
    }

    private void putEntryAt(int position, Entry entry){
        // Get shared prefs
        SharedPreferences preferenceImages = context.getSharedPreferences("entryImages", MODE_PRIVATE);
        SharedPreferences preferenceDates = context.getSharedPreferences("entryDates", MODE_PRIVATE);
        SharedPreferences preferenceTypes = context.getSharedPreferences("entryTypes", MODE_PRIVATE);
        SharedPreferences preferenceTexts = context.getSharedPreferences("entryTexts", MODE_PRIVATE);
        SharedPreferences preferenceHasPhoto = context.getSharedPreferences("entryHasPhoto", MODE_PRIVATE);

        // Get prefs editors
        SharedPreferences.Editor imageEditor = preferenceImages.edit();
        SharedPreferences.Editor dateEditor = preferenceDates.edit();
        SharedPreferences.Editor typeEditor = preferenceTypes.edit();
        SharedPreferences.Editor textEditor = preferenceTexts.edit();
        SharedPreferences.Editor hasPhotoEditor = preferenceHasPhoto.edit();

        // Change prefs
        String pos = Integer.toString(position);
        imageEditor.putString(pos, entry.getImage());
        dateEditor.putString(pos, entry.getDate());
        typeEditor.putInt(pos, entry.getType());
        textEditor.putString(pos, entry.getText());
        hasPhotoEditor.putBoolean(pos, entry.hasPhoto());

        // Apply changes
        imageEditor.apply();
        dateEditor.apply();
        typeEditor.apply();
        textEditor.apply();
        hasPhotoEditor.apply();
    }

    private void sortList(){
        ArrayList<Entry> list = new ArrayList<>();

        for (int i = 0; i < getItemCount(); i++)
            list.add(getEntryAt(i));

        Collections.sort(list, new Entry.sortingComparator());

        for (int i = 0; i < list.size(); i++)
            putEntryAt(i, list.get(i));
    }

    private void loadImgToView(ImageView imageView, String imagePath){
        try{
            int imageResource = Integer.parseInt(imagePath);
            imageView.setImageResource(imageResource);
            imageView.setTag(imagePath);
        }catch(Exception ex) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(imageBitmap);
            imageView.setTag(imagePath);
        }
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

            entryCard = (ConstraintLayout)parentView.findViewById(R.id.EntryCard);
        }
    }
}
