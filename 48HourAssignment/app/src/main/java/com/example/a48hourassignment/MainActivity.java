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
    private JournalListAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listAdapter = JournalListAdapter.getInstance(this);
        recyclerView.setAdapter(listAdapter);

        // Is the first time on the app?
        firstTime();
    }

    // Add new entry
    public void onAddEntryClicked(View view){
        Intent entryActivity = new Intent(this, JournalEntryActivity.class);
        startActivity(entryActivity);
    }

    // View pet info
    public void onPetInfoClicked(View view){
        Intent petInfoActivity = new Intent(this, PetInfoActivity.class);
        startActivity(petInfoActivity);
    }

    // Is the first time on the app?
    private void firstTime(){
        SharedPreferences preferencePetInfo = getSharedPreferences("petInfo", MODE_PRIVATE);
        String petName = preferencePetInfo.getString("petName", null);

        // If it's the first time then open PetInfo Activity
        if (petName == null)
            onPetInfoClicked(null);
    }
}
