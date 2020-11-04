package com.example.a48hourassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.a48hourassignment.PubSubBroker.Broker;

import java.util.Calendar;
import java.util.HashMap;

public class JournalEntryActivity extends AppCompatActivity {

    private Broker broker;

    ImageView imgEntryImage;
    EditText txtEntryDate, txtEntryText;
    Spinner txtEntryType;

    Entry thisEntry;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);

        broker = Broker.getInstance();

        // Get reference to UI components
        imgEntryImage = findViewById(R.id.imgJournalEntryImage);
        txtEntryDate = findViewById(R.id.txtDate);
        txtEntryType = findViewById(R.id.txtEntryType);
        txtEntryText = findViewById(R.id.txtEntryText);

        // Extract data from intent
        Intent thisIntent = getIntent();
        pos = thisIntent.getIntExtra("pos", -1);
        int img = thisIntent.getIntExtra("image", R.drawable.vet);
        String date = thisIntent.getStringExtra("date");
        date = (date != null) ? date : Calendar.getInstance().getTime().toString(); // default date is today
        int type = thisIntent.getIntExtra("type", 0);
        String text = thisIntent.getStringExtra("text");
        thisEntry = new Entry(img, date, type, text);

        // Load data to UI components
        txtEntryDate.setText(thisEntry.getDate());
        txtEntryType.setSelection(thisEntry.getType());
        txtEntryText.setText(thisEntry.getText());
        imgEntryImage.setImageResource(thisEntry.getImage());
        imgEntryImage.setTag(thisEntry.getImage());
    }

    public void changeImage(View view){

    }

    public void onSaveClicked(View view){
        thisEntry.setImage(Integer.parseInt(imgEntryImage.getTag().toString()));
        thisEntry.setDate(txtEntryDate.getText().toString());
        thisEntry.setType(txtEntryType.getSelectedItemPosition());
        thisEntry.setText(txtEntryText.getText().toString());

        HashMap<String, Object> params = new HashMap<>();
        params.put("entry", thisEntry);
        params.put("pos", pos);
        broker.publish(this, "SaveEntry", params);
    }
}
