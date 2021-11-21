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
 * This class handles the list adapter
 * for the coin flip queue.
 */
public class CoinFlipQueueAdapter extends ArrayAdapter<Child> {
    private final Context context;
    private final Integer resource;

    public CoinFlipQueueAdapter(Context context, Integer resource, ArrayList<Child> queueList) {
        super(context, resource, queueList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).getName();
        Bitmap bitmap = getItem(position).getChildImage();

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View childView = inflater.inflate(resource, parent, false);

        ImageView childImage = childView.findViewById(R.id.child_image);
        childImage.setImageBitmap(bitmap);

        TextView nameText = childView.findViewById(R.id.childNameTV);
        nameText.setText(name);

        return childView;
    }
}
