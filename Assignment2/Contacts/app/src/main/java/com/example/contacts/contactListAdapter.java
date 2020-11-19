package com.example.contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Context.MODE_PRIVATE;

class contactListAdapter extends RecyclerView.Adapter<contactListAdapter.contactViewHolder> {

    private Context context;

    public contactListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public contactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.contact_card, parent, false);
        return new contactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contactViewHolder holder, int position) {

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
        holder.chatButton.setOnClickListener(view -> {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();

            String phoneNumber = holder.contactNumber.getText().toString();
            Intent makeCall = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto", phoneNumber, null));
            context.startActivity(makeCall);
        });
        holder.callButton.setOnClickListener(view -> {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();

            String phoneNumber = holder.contactNumber.getText().toString();
            Intent makeCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            context.startActivity(makeCall);
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

        for (int i = position; i < preferenceNames.getAll().size() - 1; i++) {
            // Add change to preferences
            namesEditor.putString(Integer.toString(i), preferenceNames.getString(Integer.toString(i+1), ""));
            numbersEditor.putString(Integer.toString(i), preferenceNumbers.getString(Integer.toString(i+1), ""));
            imagesEditor.putInt(Integer.toString(i), preferenceImages.getInt(Integer.toString(i+1), R.drawable.avatar_01));
        }
        namesEditor.remove(Integer.toString(preferenceNames.getAll().size()-1));
        numbersEditor.remove(Integer.toString(preferenceNumbers.getAll().size()-1));
        imagesEditor.remove(Integer.toString(preferenceImages.getAll().size()-1));


        // Save changes to preferences
        namesEditor.apply();
        numbersEditor.apply();
        imagesEditor.apply();

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, preferenceNames.getAll().size()-position-1);
    }

    @Override
    public int getItemCount() {
        return context.getSharedPreferences("contactNames", MODE_PRIVATE).getAll().size();
    }

    public static class contactViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView contactName, contactNumber;
        ImageView contactImage;
        ConstraintLayout actionButton;
        FloatingActionButton editButton, chatButton, callButton, deleteButton;

        public contactViewHolder(View parentView) {
            super(parentView);
            contactName = parentView.findViewById(R.id.contactNumber);
            contactNumber = parentView.findViewById(R.id.contactName);
            contactImage = parentView.findViewById(R.id.avatarImage);

            actionButton = parentView.findViewById(R.id.actionButtons);

            editButton = parentView.findViewById(R.id.editButton);
            chatButton = parentView.findViewById(R.id.chatButton);
            callButton = parentView.findViewById(R.id.callButton);
            deleteButton = parentView.findViewById(R.id.deleteButton);
        }
    }
}
