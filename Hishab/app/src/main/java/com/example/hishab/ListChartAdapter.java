package com.example.hishab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ListChartAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataItem> dataSet;
    float sum;

    ListChartAdapter(Context context, ArrayList<DataItem> dataSet) {

        this.context = context;
        this.dataSet = dataSet;
        for (int i = 0; i < dataSet.size(); i++)
            this.sum += dataSet.get(i).getMoney();
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
        View view = inflater.inflate(R.layout.list_view, parent, false);

        TextView labelText = view.findViewById(R.id.textView_label);
        TextView percentText = view.findViewById(R.id.textView_percent);
        ImageView iconView = view.findViewById(R.id.imageView_icon);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        float percent = (dataSet.get(position).getMoney() / sum) * 100;
        labelText.setText(dataSet.get(position).getCategory());
        percentText.setText(String.format("%.2f", percent) + "% = " + String.valueOf(dataSet.get(position).getMoney()));
        iconView.setImageResource(dataSet.get(position).getIcon());
        progressBar.setProgress((int) percent * 100);

        return view;
    }
}
