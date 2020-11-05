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

        recyclerView = findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = JournalListAdapter.getInstance(this);
        recyclerView.setAdapter(mAdapter);

        firstTime();
    }

    public void onAddEntryClicked(View view){
        Intent entryActivity = new Intent(this, JournalEntryActivity.class);
        startActivity(entryActivity);
    }

    public void onPetInfoClicked(View view){
        Intent petInfoActivity = new Intent(this, PetInfoActivity.class);
        startActivity(petInfoActivity);
    }

    private void firstTime(){
        SharedPreferences preferencePetInfo = getSharedPreferences("petInfo", MODE_PRIVATE);
        String petName = preferencePetInfo.getString("petName", null);

        if (petName == null)
            onPetInfoClicked(null);
    }
}
