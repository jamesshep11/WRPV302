package com.example.a48hourassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Calendar;

public class JournalEntryActivity extends AppCompatActivity {

    Entry thisEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);

        // Get reference to UI components
        ImageView imgEntryImage = findViewById(R.id.imgJournalEntryImage);
        EditText txtEntryDate = findViewById(R.id.txtDate);
        Spinner txtEntryType = findViewById(R.id.txtEntryType);
        EditText txtEntryText = findViewById(R.id.txtEntryText);

        // Extract data from intent
        Intent thisIntent = getIntent();
        int img = thisIntent.getIntExtra("image", R.drawable.vet);
        String date = thisIntent.getStringExtra("date");
        date = (date != null) ? date : Calendar.getInstance().getTime().toString(); // default date is today
        int type = thisIntent.getIntExtra("type", 0);
        String text = thisIntent.getStringExtra("text");
        thisEntry = new Entry(img, date, type, text);

        // Load data to UI components
        imgEntryImage.setImageResource(thisEntry.getImage());
        txtEntryDate.setText(thisEntry.getDate());
        txtEntryType.setSelection(thisEntry.getType());
        txtEntryText.setText(thisEntry.getText());
    }
}
