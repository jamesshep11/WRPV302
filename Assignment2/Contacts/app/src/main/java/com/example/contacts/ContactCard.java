package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactCard extends AppCompatActivity {

    TextView contactName, contactNumber;
    ImageView contactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);

        contactName = findViewById(R.id.contactName);
        contactNumber = findViewById(R.id.contactNumber);
        contactImage = findViewById(R.id.contactImage);

        Intent thisIntent = getIntent();
        contactName.setText(thisIntent.getStringExtra("contactName"));
        contactNumber.setText(thisIntent.getStringExtra("contactNumber"));
        contactImage.setImageResource(thisIntent.getIntExtra("contactImage", 0));
    }
}
