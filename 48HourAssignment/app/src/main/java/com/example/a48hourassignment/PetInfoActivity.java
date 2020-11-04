package com.example.a48hourassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PetInfoActivity extends AppCompatActivity {

    ImageView imgPetImage;
    EditText txtPetName, txtPetDateOfBirth;

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

    public void onSaveClicked(View view){
        // Get data from UI
        String petName = txtPetName.getText().toString();
        String petDateOfBirth = txtPetDateOfBirth.getText().toString();
        int petImage = Integer.parseInt(imgPetImage.getTag().toString());

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
}
