package com.example.contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
        contactImage = findViewById(R.id.avatarImage);

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

    public void onImageClicked(View view){
        ArrayList<Integer> avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.avatar_01);
        avatarImages.add(R.drawable.avatar_02);
        avatarImages.add(R.drawable.avatar_03);
        avatarImages.add(R.drawable.avatar_04);
        avatarImages.add(R.drawable.avatar_05);
        avatarImages.add(R.drawable.avatar_06);
        avatarImages.add(R.drawable.avatar_07);
        avatarImages.add(R.drawable.avatar_08);
        avatarImages.add(R.drawable.avatar_09);
        avatarImages.add(R.drawable.avatar_pokemon);

        avatarListAdapter avatarImageAdapter = new avatarListAdapter(this, avatarImages);
        new AlertDialog.Builder(this)
                .setTitle("Select Avatar")
                .setAdapter(avatarImageAdapter, (dialog, pos) ->{
                    contactImage.setImageResource(avatarImages.get(pos));
                    contactImage.setTag(avatarImages.get(pos));
                })
                .show();
    }
}
