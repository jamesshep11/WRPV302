package com.example.a48hourassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PetInfoActivity extends AppCompatActivity {

    ImageView imgPetImage;
    EditText txtPetName, txtPetDateOfBirth;

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);

        // Get reference to UI components
        imgPetImage = findViewById(R.id.imgPetImage);
        txtPetName = findViewById(R.id.txtPetName);
        txtPetDateOfBirth = findViewById(R.id.txtDateOfBirth);

        // Extract data from shared preferences
        SharedPreferences preferencePetInfo = getSharedPreferences("petInfo", MODE_PRIVATE);
        String petName = preferencePetInfo.getString("petName", "");
        String petDateOfBirth = preferencePetInfo.getString("petDateOfBirth", "");
        int petImage = preferencePetInfo.getInt("petImage", R.drawable.ic_launcher_foreground);

        // Load data to UI components
        txtPetName.setText(petName);
        txtPetDateOfBirth.setText(petDateOfBirth);
        imgPetImage.setImageResource(petImage);
        imgPetImage.setTag(petImage);
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
            imgPetImage.setImageBitmap(imageBitmap);
        }
    }
    //endregion

    public void onSaveClicked(View view){
        // Get data from UI
        String petName = txtPetName.getText().toString();
        String petDateOfBirth = txtPetDateOfBirth.getText().toString();
        int petImage = Integer.parseInt(imgPetImage.getTag().toString());

        if (!validDate(petDateOfBirth)) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("The date you entered is not valid.")
                    .setOnDismissListener(dialog -> {
                        Toast.makeText(this, "Could Not Save", Toast.LENGTH_LONG).show();
                    })
                    .show();
            return;
        }

        // Get shared prefs and prefs editor
        SharedPreferences preferencePetInfo = getSharedPreferences("petInfo", MODE_PRIVATE);
        SharedPreferences.Editor petEditor = preferencePetInfo.edit();

        // Change shared prefs
        petEditor.putString("petName", petName);
        petEditor.putString("petDateOfBirth", petDateOfBirth);
        petEditor.putInt("petImage", petImage);

        // Apply changes
        petEditor.apply();

        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
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
}
