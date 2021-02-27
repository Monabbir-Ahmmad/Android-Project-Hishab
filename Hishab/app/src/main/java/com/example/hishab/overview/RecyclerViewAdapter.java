package com.example.hishab.overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hishab.CustomDateTime;
import com.example.hishab.DataItem;
import com.example.hishab.R;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private final Context context;
    private final ArrayList<DataItem> dataSet;
    private CustomDateTime cDateTime;
    private onItemClickListener listener;


    //Constructor
    public RecyclerViewAdapter(ArrayList<DataItem> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @NotNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, listener);
        cDateTime = new CustomDateTime(context);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.rView_icon.setImageResource(dataSet.get(position).getIcon());
        holder.rView_category.setText(dataSet.get(position).getCategory());
        holder.rView_amount.setText(String.format("%s BDT", decimalFormat.format(dataSet.get(position).getAmount())));
        holder.rView_dateTime.setText(String.format("%s %s", cDateTime.getDate(dataSet.get(position).getTimestamp()),
                cDateTime.getTime(dataSet.get(position).getTimestamp())));
        if (dataSet.get(position).getNote() != null) {
            holder.rView_note.setText(String.format("Note: %s", dataSet.get(position).getNote()));
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //Interface for onItemClickListener
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    //Inner view holder class
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView rView_icon;
        public TextView rView_category, rView_amount, rView_dateTime, rView_note;

        //Inner classConstructor
        public RecyclerViewHolder(View itemView, onItemClickListener listener) {
            super(itemView);

            rView_icon = itemView.findViewById(R.id.rView_icon);
            rView_category = itemView.findViewById(R.id.rView_category);
            rView_amount = itemView.findViewById(R.id.rView_amount);
            rView_dateTime = itemView.findViewById(R.id.rView_dateTime);
            rView_note = itemView.findViewById(R.id.rView_note);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }


}
