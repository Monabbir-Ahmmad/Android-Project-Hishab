package com.example.hishab.overview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hishab.DataItem;
import com.example.hishab.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private final ArrayList<DataItem> dataSet;
    private onItemClickListener listener;


    //Constructor
    public RecyclerViewAdapter(ArrayList<DataItem> dataSet) {
        this.dataSet = dataSet;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, listener);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        holder.rView_icon.setImageResource(dataSet.get(position).getIcon());
        holder.rView_category.setText(dataSet.get(position).getCategory());
        holder.rView_amount.setText(decimalFormat.format(dataSet.get(position).getAmount()) + " BDT");
        holder.rView_dateTime.setText(dataSet.get(position).getDate() + " " + dataSet.get(position).getTime());
        if (dataSet.get(position).getNote() != null)
            holder.rView_note.setText("Note: " + dataSet.get(position).getNote());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }


}
