package com.example.hishab.ui.overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hishab.DateTimeUtil;
import com.example.hishab.R;
import com.example.hishab.data.DataItem;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private final Context context;
    private final ArrayList<DataItem> dataSet;
    private DateTimeUtil dateTimeUtil;
    private onItemClickListener listener;
    private String currency;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, listener);
        dateTimeUtil = new DateTimeUtil();
        currency = context.getResources().getString(R.string.currency);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.recIcon.setImageResource(dataSet.get(position).getIcon());
        holder.recCategory.setText(dataSet.get(position).getCategory());
        holder.recAmount.setText(String.format("%s%s", currency, decimalFormat.format(dataSet.get(position).getAmount())));
        holder.recDateTime.setText(dateTimeUtil.getTimeAgo(dataSet.get(position).getTimestamp()));
        if (dataSet.get(position).getNote() != null) {
            holder.recNote.setText(String.format("Note: %s", dataSet.get(position).getNote()));
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

        public final ImageView recIcon;
        public final TextView recCategory;
        public final TextView recAmount;
        public final TextView recDateTime;
        public final TextView recNote;

        //Inner classConstructor
        public RecyclerViewHolder(View itemView, onItemClickListener listener) {
            super(itemView);

            //Find views
            recIcon = itemView.findViewById(R.id.recycleView_icon);
            recCategory = itemView.findViewById(R.id.recycleView_category);
            recAmount = itemView.findViewById(R.id.recycleView_amount);
            recDateTime = itemView.findViewById(R.id.recycleView_dateTime);
            recNote = itemView.findViewById(R.id.recycleView_note);

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
