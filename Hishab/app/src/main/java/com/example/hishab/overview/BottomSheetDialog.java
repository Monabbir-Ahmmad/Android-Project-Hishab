package com.example.hishab.overview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hishab.CustomDateTime;
import com.example.hishab.DataInputActivity;
import com.example.hishab.R;
import com.example.hishab.data.DataItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private final DataItem dataItem;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private TextView tvCategory, tvAmount, tvDate, tvTime, tvNote;
    private Button btnEdit;
    private ImageButton btnClose;
    private CustomDateTime cDateTime;


    //Constructor
    public BottomSheetDialog(DataItem dataItem) {
        this.dataItem = dataItem;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        cDateTime = new CustomDateTime(getActivity());

        tvCategory = view.findViewById(R.id.bottomSheet_category);
        tvCategory.setText(dataItem.getCategory());

        tvAmount = view.findViewById(R.id.bottomSheet_amount);
        tvAmount.setText(String.format("%s BDT", decimalFormat.format(dataItem.getAmount())));

        tvDate = view.findViewById(R.id.bottomSheet_date);
        tvDate.setText(cDateTime.getDate(dataItem.getTimestamp()));

        tvTime = view.findViewById(R.id.bottomSheet_time);
        tvTime.setText(cDateTime.getTime(dataItem.getTimestamp()));

        tvNote = view.findViewById(R.id.bottomSheet_note);
        if (dataItem.getNote() != null) {
            tvNote.setText(dataItem.getNote());
        } else {
            tvNote.setText("");
        }

        btnClose = view.findViewById(R.id.bottomSheet_close);
        btnClose.setOnClickListener(v -> dismiss());

        btnEdit = view.findViewById(R.id.bottomSheet_edit);
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DataInputActivity.class);
            intent.putExtra("update", true);
            intent.putExtra("id", dataItem.getId());
            intent.putExtra("category", dataItem.getCategory());
            intent.putExtra("icon", dataItem.getIcon());
            intent.putExtra("amount", String.valueOf(dataItem.getAmount()));
            intent.putExtra("date", cDateTime.getDate(dataItem.getTimestamp()));
            intent.putExtra("time", cDateTime.getTime(dataItem.getTimestamp()));
            intent.putExtra("note", dataItem.getNote());
            startActivity(intent);
            dismiss();
        });

        return view;
    }


}
