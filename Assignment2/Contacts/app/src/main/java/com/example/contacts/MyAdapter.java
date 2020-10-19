package com.example.contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private RecyclerView recyclerView;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.contact_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Contact currentContact = getContactAt(position);

        holder.contactName.setText(currentContact.getName());
        holder.contactNumber.setText(currentContact.getNumber());
        holder.contactImage.setImageResource(currentContact.getImage());

        holder.contactImage.setOnClickListener(view -> {
            if (holder.actionButton.getVisibility() == View.GONE)
                holder.actionButton.setVisibility(View.VISIBLE);
            else
                holder.actionButton.setVisibility(View.GONE);
        });

        holder.editButton.setOnClickListener(view -> {
            Intent contactActivity = new Intent(context, ContactCard.class);
            contactActivity.putExtra("position", position);
            context.startActivity(contactActivity);
        });

        holder.deleteButton.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Confirm", (dialogInterface, i)
                        -> removeContactAt(position))
                .setNegativeButton("Cancel", null)
                .show();
        });
    }

    private Contact getContactAt(int position){
        SharedPreferences preferenceNames = context.getSharedPreferences("contactNames", MODE_PRIVATE);
        SharedPreferences preferenceNumbers = context.getSharedPreferences("contactNumbers", MODE_PRIVATE);
        SharedPreferences preferenceImages = context.getSharedPreferences("contactImages", MODE_PRIVATE);

        String pos = Integer.toString(position);

        return new Contact(preferenceNames.getString(pos, ""), preferenceNumbers.getString(pos, ""), preferenceImages.getInt(pos, R.drawable.avatar_01));
    }

    private void removeContactAt(int position){
        String pos = Integer.toString(position);

        // Get shared preferences
        SharedPreferences preferenceNames = context.getSharedPreferences("contactNames", MODE_PRIVATE);
        SharedPreferences preferenceNumbers = context.getSharedPreferences("contactNumbers", MODE_PRIVATE);
        SharedPreferences preferenceImages = context.getSharedPreferences("contactImages", MODE_PRIVATE);

        // Get shared preferences editors
        SharedPreferences.Editor namesEditor = preferenceNames.edit();
        SharedPreferences.Editor numbersEditor = preferenceNumbers.edit();
        SharedPreferences.Editor imagesEditor = preferenceImages.edit();

        // Add change to preferences
        namesEditor.remove(pos);
        numbersEditor.remove(pos);
        imagesEditor.remove(pos);

        // Save changes to preferences
        namesEditor.apply();
        numbersEditor.apply();
        imagesEditor.apply();

        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return context.getSharedPreferences("contactNames", MODE_PRIVATE).getAll().size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView contactName, contactNumber;
        ImageView contactImage;
        ConstraintLayout actionButton;
        FloatingActionButton editButton, chatButton, callButton, deleteButton;

        public MyViewHolder(View parentView) {
            super(parentView);
            contactName = parentView.findViewById(R.id.contactNumber);
            contactNumber = parentView.findViewById(R.id.contactName);
            contactImage = parentView.findViewById(R.id.contactImage);

            actionButton = parentView.findViewById(R.id.actionButtons);

            editButton = parentView.findViewById(R.id.editButton);
            chatButton = parentView.findViewById(R.id.chatButton);
            callButton = parentView.findViewById(R.id.callButton);
            deleteButton = parentView.findViewById(R.id.deleteButton);
        }
    }
}
