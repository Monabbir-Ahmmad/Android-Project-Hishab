package com.example.hishab.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hishab.DataItem;
import com.example.hishab.R;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataItem> dataSet;

    GridAdapter(Context context, ArrayList<DataItem> dataSet) {

        this.context = context;
        this.dataSet = dataSet;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.category_grid_view, parent, false);

        TextView gridLabel = view.findViewById(R.id.grid_label);
        ImageView gridIcon = view.findViewById(R.id.grid_icon);

        gridLabel.setText(dataSet.get(position).getCategory());
        gridIcon.setImageResource(dataSet.get(position).getIcon());

        return view;
    }
}
