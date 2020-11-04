package com.example.a48hourassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
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
        mAdapter = new JournalListAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    public void onAddEntryClicked(View view){
        Intent entryActivity = new Intent(this, JournalEntryActivity.class);
        startActivity(entryActivity);
    }

    public void onPetInfoClicked(View view){
        Intent petInfoActivity = new Intent(this, PetInfoActivity.class);
        startActivity(petInfoActivity);
    }

}
