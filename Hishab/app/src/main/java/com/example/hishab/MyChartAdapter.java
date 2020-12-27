package com.example.hishab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyChartAdapter extends ArrayAdapter<String> {

    Context context;
    String title[];
    float value[];
    float sum;

    MyChartAdapter(Context context, String title[], float value[], float sum) {
        super(context, R.layout.list_view, R.id.textView_title, title);
        this.context = context;
        this.title = title;
        this.value = value;
        this.sum = sum;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.list_view, parent, false);

        TextView textViewTitle = view.findViewById(R.id.textView_title);
        TextView textViewPercentValue = view.findViewById(R.id.textView_percent);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        float percent = (value[position] / sum) * 100;

        textViewTitle.setText(title[position]);
        textViewPercentValue.setText(String.format("%.2f", percent) + "%");
        progressBar.setProgress((int) percent * 100);

        return view;
    }
}
