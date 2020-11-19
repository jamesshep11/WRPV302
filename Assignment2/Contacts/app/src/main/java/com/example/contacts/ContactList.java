package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;

public class ContactList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private final String[] sampleNames = {"James Shepherd", "Matthew Denga", "Stephanie Lewis", "Nigel Hendricks", "Lorenzo Napoli", "Siwe Masola", "Summer Anderson", "Brook Hendricks", "Lauren Agar", "Langa Mandaha"};
    private final String[] sampleNumbers = {"0123456789", "9876543210", "1597534862", "4568521793", "1793284650", "1478523690", "0963258741", "3692581470", "0153624798", "7594863210"};
    private final Integer[] sampleImages = {R.drawable.avatar_01,  R.drawable.avatar_02, R.drawable.avatar_03, R.drawable.avatar_04, R.drawable.avatar_05, R.drawable.avatar_06, R.drawable.avatar_07, R.drawable.avatar_08, R.drawable.avatar_09, R.drawable.avatar_pokemon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        recyclerView = findViewById(R.id.rvContacts);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new contactListAdapter(this);
        recyclerView.setAdapter(mAdapter);

        // Ask to populate the list with test data or not
        populateTestData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();
        mAdapter.notifyItemRangeChanged(0, getSharedPreferences("contactNames", MODE_PRIVATE).getAll().size());
    }

    public void onNewContactClicked(View view){
        Intent contactActivity = new Intent(this, ContactCard.class);
        startActivity(contactActivity);
    }

    private void populateTestData(){
        new AlertDialog.Builder(this)
                .setTitle("Populate List")
                .setMessage("Would you like to populate your contacts list with sample test data? \nAll existing contacts will be lost.")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Get shared preferences
                    SharedPreferences preferenceNames = getSharedPreferences("contactNames", MODE_PRIVATE);
                    SharedPreferences preferenceNumbers = getSharedPreferences("contactNumbers", MODE_PRIVATE);
                    SharedPreferences preferenceImages = getSharedPreferences("contactImages", MODE_PRIVATE);
                    // Get shared preferences editors
                    SharedPreferences.Editor namesEditor = preferenceNames.edit();
                    SharedPreferences.Editor numbersEditor = preferenceNumbers.edit();
                    SharedPreferences.Editor imagesEditor = preferenceImages.edit();

                    // Clear all existing contacts
                    namesEditor.clear();
                    numbersEditor.clear();
                    imagesEditor.clear();

                    // Add sample data to preferences
                    for (int i = 0; i < sampleNames.length; i++){
                        String pos = Integer.toString(i);
                        namesEditor.putString(pos, sampleNames[i]);
                        numbersEditor.putString(pos, sampleNumbers[i]);
                        imagesEditor.putInt(pos, sampleImages[i]);
                    }

                    // Save changes to preferences
                    namesEditor.apply();
                    numbersEditor.apply();
                    imagesEditor.apply();

                    new AlertDialog.Builder(this)
                            .setTitle("Restart")
                            .setMessage("You contact list will be cleared and repopulated with the sample data. \nPlease restart the app to apply changes.")
                            .setPositiveButton("Ok", (newDialog, newWhich) -> finish())
                            .show();
                })
                .show();
    }
}
