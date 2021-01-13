package com.example.hishab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<DataItem> dataList;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView rv_icon;
        public TextView rv_category, rv_date_time, rv_note, rv_money;

        public RecyclerViewHolder(View itemView, onItemClickListener listener) {
            super(itemView);

            rv_icon = itemView.findViewById(R.id.rView_icon);
            rv_category = itemView.findViewById(R.id.rView_category);
            rv_date_time = itemView.findViewById(R.id.rView_date_time);
            rv_note = itemView.findViewById(R.id.rView_note);
            rv_money = itemView.findViewById(R.id.rView_money);

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

    public RecyclerViewAdapter(ArrayList<DataItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, listener);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        DataItem dataItem = dataList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        holder.rv_icon.setImageResource(dataItem.getIcon());
        holder.rv_category.setText(dataItem.getCategory());
        holder.rv_money.setText(decimalFormat.format(dataItem.getMoney()) + " BDT");
        holder.rv_date_time.setText(dataItem.getTime() + "; " + dataItem.getDate());
        if (dataItem.getNote() != null)
            holder.rv_note.setText("Note: " + dataItem.getNote());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
