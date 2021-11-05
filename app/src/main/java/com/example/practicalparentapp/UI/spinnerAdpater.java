package com.example.practicalparentapp.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.practicalparentapp.R;

class SpinnerAdapter extends ArrayAdapter<String> {
    private String[] the_minutes;

    public SpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.the_minutes=objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomDropDownView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        TextView label= row.findViewById(R.id.spinner_text);
        label.setText(the_minutes[position]);
        return row;
    }

    private View getCustomDropDownView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_dropdown, parent, false);
        TextView label= row.findViewById(R.id.spinner_dropdown_text);
        label.setText(the_minutes[position]);
        return row;
    }
}