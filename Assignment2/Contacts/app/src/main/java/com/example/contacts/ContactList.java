package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactList extends AppCompatActivity {

    private List<Contact> contacts = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        mAdapter = new MyAdapter(this, contacts);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        contacts.clear();

        // Load saved contact information

        // Get info from shared preferences
        SharedPreferences preferenceNames = getSharedPreferences("contactNames", MODE_PRIVATE);
        SharedPreferences preferenceNumbers = getSharedPreferences("contactNumbers", MODE_PRIVATE);
        SharedPreferences preferenceImages = getSharedPreferences("contactImages", MODE_PRIVATE);

        // load info into global contacts list
        for (int x = 0; x < preferenceNames.getAll().size(); x++){
            String pos = Integer.toString(x);
            Contact newContact = new Contact(preferenceNames.getString(pos, ""), preferenceNumbers.getString(pos, ""), preferenceImages.getInt(pos, R.drawable.avatar_01));
            contacts.add(newContact);
        }
    }

    public void onNewContactClicked(View view){
        Intent contactActivity = new Intent(this, ContactCard.class);
        startActivity(contactActivity);
    }
}
