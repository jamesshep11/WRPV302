package com.example.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class avatarListAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private List<Integer> avatarList;

    public avatarListAdapter(@NonNull Context context, @LayoutRes ArrayList<Integer> list) {
        super(context, 0 , list);
        this.context = context;
        avatarList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.alertdialog_image_picker, parent, false);

        int currentImage = avatarList.get(position);

        ImageView image = listItem.findViewById(R.id.avatarImage);
        image.setImageResource(currentImage);

        return listItem;
    }
}
