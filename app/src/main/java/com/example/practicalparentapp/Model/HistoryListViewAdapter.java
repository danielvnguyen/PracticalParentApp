package com.example.practicalparentapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.practicalparentapp.R;

import java.util.ArrayList;

/**
 * This class handles adapting the list of
 * history components in the Coin Flip activity.
 */
public class HistoryListViewAdapter extends ArrayAdapter<History> {
    private final Context context;
    private final Integer resource;

    public HistoryListViewAdapter(Context context, Integer resource, ArrayList<History> historyList) {
        super(context, resource, historyList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).getHistInfo();
        Bitmap bitmap = getItem(position).getChildWhoFlipped().getChildImage();

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View historyView = inflater.inflate(resource, parent, false);

        ImageView childImage = historyView.findViewById(R.id.history_child_image);
        childImage.setImageBitmap(bitmap);

        TextView historyText = historyView.findViewById(R.id.history_TV);
        historyText.setText(name);

        return historyView;
    }
}
