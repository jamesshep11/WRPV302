package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactCard extends AppCompatActivity {

    TextView contactName, contactNumber;
    ImageView contactImage;

    boolean saved;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);

        saved = false;

        // Get reference to UI components
        contactName = findViewById(R.id.contactName);
        contactNumber = findViewById(R.id.contactNumber);
        contactImage = findViewById(R.id.contactImage);

        // Read data passed through intent
        Intent thisIntent = getIntent();
        position = thisIntent.getIntExtra("position", getSharedPreferences("contactNames", MODE_PRIVATE).getAll().size());
        Contact currentContact = getContactAt(position);
        // Load data to UI
        contactName.setText(currentContact.getName());
        contactNumber.setText(currentContact.getNumber());
        contactImage.setImageResource(currentContact.getImage());
        contactImage.setTag(currentContact.getImage());
    }

    @Override
    public void onBackPressed() {
        if(!saved)
            new AlertDialog.Builder(this)
                    .setTitle("Save changes")
                    .setMessage("Would you like to save these changes?")
                    .setPositiveButton("Save", (dialogInterface, i) -> {
                        onSaveClicked(null);
                        super.onBackPressed();
                        })
                    .setNegativeButton("Don't save", (dialogInterface, i)
                            -> super.onBackPressed())
                    .show();
        else
            super.onBackPressed();
    }

    public void onSaveClicked(View view){
        // Get info from UI
        String name = contactName.getText().toString();
        String number = contactNumber.getText().toString();
        int image = (int)contactImage.getTag();

        Contact newContact = new Contact(name, number, image);
        setContactAt(position, newContact);

        saved = true;
        Toast.makeText(this, name + " saved", Toast.LENGTH_SHORT).show();
    }

    private Contact getContactAt(int position){
        SharedPreferences preferenceNames = getSharedPreferences("contactNames", MODE_PRIVATE);
        SharedPreferences preferenceNumbers = getSharedPreferences("contactNumbers", MODE_PRIVATE);
        SharedPreferences preferenceImages = getSharedPreferences("contactImages", MODE_PRIVATE);

        String pos = Integer.toString(position);

        return new Contact(preferenceNames.getString(pos, ""), preferenceNumbers.getString(pos, ""), preferenceImages.getInt(pos, R.drawable.avatar_01));
    }

    private void setContactAt(int position, Contact contact){
        String pos = Integer.toString(position);

        // Get shared preferences
        SharedPreferences preferenceNames = getSharedPreferences("contactNames", MODE_PRIVATE);
        SharedPreferences preferenceNumbers = getSharedPreferences("contactNumbers", MODE_PRIVATE);
        SharedPreferences preferenceImages = getSharedPreferences("contactImages", MODE_PRIVATE);
        // Get shared preferences editors
        SharedPreferences.Editor namesEditor = preferenceNames.edit();
        SharedPreferences.Editor numbersEditor = preferenceNumbers.edit();
        SharedPreferences.Editor imagesEditor = preferenceImages.edit();

        // Add change to preferences
        namesEditor.putString(pos, contact.getName());
        numbersEditor.putString(pos, contact.getNumber());
        imagesEditor.putInt(pos, contact.getImage());

        // Save changes to preferences
        namesEditor.apply();
        numbersEditor.apply();
        imagesEditor.apply();
    }

    public void onCallClicked(View view){
        String phoneNumber = contactNumber.getText().toString();

        Intent makeCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(makeCall);
    }
}
