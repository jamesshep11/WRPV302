package com.example.a48hourassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

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
import android.widget.Toast;

import com.example.a48hourassignment.PubSubBroker.Broker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
        ConstraintLayout journalEntryLayout = findViewById(R.id.journalEntryLayout);

        // Extract data from intent
        Intent thisIntent = getIntent();
        pos = thisIntent.getIntExtra("pos", getSharedPreferences("entryImages", MODE_PRIVATE).getAll().size());
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

        // Link Entry type with entryImage
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
        // Link entryType with entry colour
        switch (thisEntry.getType()){
            case 0: journalEntryLayout.setBackgroundResource(R.color.vet);
                break;
            case 1: journalEntryLayout.setBackgroundResource(R.color.medication);
                break;
            case 2: journalEntryLayout.setBackgroundResource(R.color.appointment);
                break;
            case 3: journalEntryLayout.setBackgroundResource(R.color.selfie);
                break;
            default: journalEntryLayout.setBackgroundResource(R.color.selfie);
                break;
        }
    }

    private Boolean validDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date tempDate;

        try {
            tempDate = dateFormat.parse(date);
        } catch (Exception ex) {
            return false;
        }

        if (!dateFormat.format(tempDate).equals(date)) {
            return false;
        }

        // Get entered date components
        String[] dateBreakdown = date.split("/");
        String year = dateBreakdown[0],
                month = dateBreakdown[1],
                day = dateBreakdown[2];

        // Get today's date components
        String[] todayBreakdown = (new SimpleDateFormat("yyyy/MM/dd").format(new Date())).split("/");
        String thisYear = todayBreakdown[0],
                thisMonth = todayBreakdown[1],
                thisDay = todayBreakdown[2];

        if (year.compareTo(thisYear) > 0 ||                                         // Year is in the future
                (year.equals(thisYear) && (month.compareTo(thisMonth) > 0 ||        // Month is in the future
                        (month.equals(thisMonth) && day.compareTo(thisDay) > 0))))  // Day is in the future
            return false;

        return true;
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
                        .setTitle(R.string.error)
                        .setMessage(R.string.error_creating_image_file)
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
        // Get data  from UI
        thisEntry.setImage(Integer.parseInt(imgEntryImage.getTag().toString()));
        thisEntry.setDate(txtEntryDate.getText().toString());
        thisEntry.setType(txtEntryType.getSelectedItemPosition());
        thisEntry.setText(txtEntryText.getText().toString());

        // Validate the date
        if (!validDate(thisEntry.getDate())) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error)
                    .setMessage(R.string.invalid_data)
                    .setOnDismissListener(dialog -> {
                        Toast.makeText(this, R.string.save_failed, Toast.LENGTH_LONG).show();
                    })
                    .show();
            return;
        }

        // Alter ListAdapter to add/update the Entry
        HashMap<String, Object> params = new HashMap<>();
        params.put("entry", thisEntry);
        params.put("pos", pos);
        broker.publish(this, "SaveEntry", params);
    }
}
