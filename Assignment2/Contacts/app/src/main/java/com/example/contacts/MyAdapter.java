package com.example.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<Contact> contacts;

    public MyAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
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
        holder.contactName.setText(contacts.get(position).getName());
        holder.contactNumber.setText(contacts.get(position).getNumber());
        holder.contactImage.setImageResource(contacts.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView contactName, contactNumber;
        ImageView contactImage;

        public MyViewHolder(View parentView) {
            super(parentView);
            contactName = parentView.findViewById(R.id.contactNumber);
            contactNumber = parentView.findViewById(R.id.contactName);
            contactImage = parentView.findViewById(R.id.contactImage);
        }
    }
}
