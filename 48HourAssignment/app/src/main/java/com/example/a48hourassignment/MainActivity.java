package com.example.a48hourassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JournalListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTime();

        recyclerView = findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new JournalListAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    public void onAddEntryClicked(View view){
        Intent entryActivity = new Intent(this, JournalEntryActivity.class);
        entryActivity.putExtra("pos", mAdapter.getItemCount());
        startActivity(entryActivity);
    }

    public void onPetInfoClicked(View view){
        Intent petInfoActivity = new Intent(this, PetInfoActivity.class);
        startActivity(petInfoActivity);
    }

    private void firstTime(){
        SharedPreferences preferencePetInfo = getSharedPreferences("petInfo", MODE_PRIVATE);
        String petName = preferencePetInfo.getString("petName", null);

        if (petName != null)
            return;

        onPetInfoClicked(null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Get shared prefs
        SharedPreferences preferenceImages = getSharedPreferences("entryImages", MODE_PRIVATE);
        SharedPreferences preferenceDates = getSharedPreferences("entryDates", MODE_PRIVATE);
        SharedPreferences preferenceTypes = getSharedPreferences("entryTypes", MODE_PRIVATE);
        SharedPreferences preferenceTexts = getSharedPreferences("entryTexts", MODE_PRIVATE);

        // Get prefs editors
        SharedPreferences.Editor imageEditor = preferenceImages.edit();
        SharedPreferences.Editor dateEditor = preferenceDates.edit();
        SharedPreferences.Editor typeEditor = preferenceTypes.edit();
        SharedPreferences.Editor textEditor = preferenceTexts.edit();

        // Change prefs
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            String pos = Integer.toString(i);
            Entry currentEntry = mAdapter.getEntryAt(i);
            imageEditor.putInt(pos, currentEntry.getImage());
            dateEditor.putString(pos, currentEntry.getDate());
            typeEditor.putInt(pos, currentEntry.getType());
            textEditor.putString(pos, currentEntry.getText());
        }

        // Apply changes
        imageEditor.apply();
        dateEditor.apply();
        typeEditor.apply();
        textEditor.apply();
    }
}
