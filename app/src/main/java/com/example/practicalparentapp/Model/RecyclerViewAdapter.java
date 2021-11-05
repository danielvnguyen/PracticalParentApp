package com.example.practicalparentapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.practicalparentapp.R;
import com.example.practicalparentapp.UI.NewChildActivity;

import java.util.ArrayList;

/**
 * This class handles sending names and updating the
 * children list.
 */
public class RecyclerViewAdapter extends ArrayAdapter<Child>{

    private final Context context;
    private final Integer resource;
    public static final String STRING_EXTRA = "Is edit";
    public static final String POSITION_EXTRA = "Child position";

    public RecyclerViewAdapter(Context context, Integer resource, ArrayList<Child> childList) {
        super(context, resource, childList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).getName();

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View childView = inflater.inflate(resource, parent, false);

        //setting views
        TextView nameText = childView.findViewById(R.id.childNameTV);

        //display name
        nameText.setText(name);

        //when click start in edit mode (allow parent to delete or change name)
        childView.setOnClickListener(view -> {
            Intent intent = NewChildActivity.makeIntent(context);
            intent.putExtra(STRING_EXTRA, true);
            intent.putExtra(POSITION_EXTRA, position);
            context.startActivity(intent);
        });

        return childView;
    }
}
