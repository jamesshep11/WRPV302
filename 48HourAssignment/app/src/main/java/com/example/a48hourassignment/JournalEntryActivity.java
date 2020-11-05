package com.example.a48hourassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.a48hourassignment.PubSubBroker.Broker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class JournalEntryActivity extends AppCompatActivity {

    private Broker broker;

    ImageView imgEntryImage;
    EditText txtEntryDate, txtEntryText;
    Spinner txtEntryType;

    Entry thisEntry;
    int pos;
    String currentPhotoPath;

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
        date = (date != null) ? date : new SimpleDateFormat("yyyy/MM/dd").format(new Date());                 // default date is today
        int type = thisIntent.getIntExtra("type", 0);
        String text = thisIntent.getStringExtra("text");
        thisEntry = new Entry(img, date, type, text);

        // Load data to UI components
        txtEntryDate.setText(thisEntry.getDate());
        txtEntryType.setSelection(thisEntry.getType());
        txtEntryText.setText(thisEntry.getText());
        imgEntryImage.setImageResource(thisEntry.getImage());
        imgEntryImage.setTag(thisEntry.getImage());

        // link Entry type with entryImage
        txtEntryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int img = R.drawable.vet;
                switch (position){
                    case 0: img = R.drawable.vet;
                        break;
                    case 1: img = R.drawable.medicine;
                        break;
                    case 2: img = R.drawable.appointment;
                        break;
                    case 3: img = R.drawable.selfie;
                        break;
                }
                imgEntryImage.setImageResource(img);
                imgEntryImage.setTag(img);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
    }

    public void changeImage(View view){
        dispatchTakePictureIntent();
    }

    //region Take and Save Photo
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Could not create image file")
                        .show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgEntryImage.setImageBitmap(imageBitmap);
        }
    }
    //endregion

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
