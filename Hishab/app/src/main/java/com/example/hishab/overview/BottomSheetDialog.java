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
import com.example.hishab.DataItem;
import com.example.hishab.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private final DataItem dataItem;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private TextView tv_category, tv_amount, tv_date, tv_time, tv_note;
    private Button btn_edit;
    private ImageButton btn_close;
    private CustomDateTime cDateTime;


    //Constructor
    public BottomSheetDialog(DataItem dataItem) {
        this.dataItem = dataItem;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        cDateTime = new CustomDateTime(getActivity());

        tv_category = view.findViewById(R.id.bsheet_category);
        tv_category.setText(dataItem.getCategory());

        tv_amount = view.findViewById(R.id.bsheet_amount);
        tv_amount.setText(String.format("Amount: %s BDT", decimalFormat.format(dataItem.getAmount())));

        tv_date = view.findViewById(R.id.bsheet_date);
        tv_date.setText(String.format("Date: %s", cDateTime.getDate(dataItem.getTimestamp())));

        tv_time = view.findViewById(R.id.bsheet_time);
        tv_time.setText(String.format("Time: %s", cDateTime.getTime(dataItem.getTimestamp())));

        tv_note = view.findViewById(R.id.bsheet_note);
        if (dataItem.getNote() != null) {
            tv_note.setText(String.format("Note: %s", dataItem.getNote()));
        }

        btn_close = view.findViewById(R.id.bsheet_close);
        btn_close.setOnClickListener(v -> dismiss());

        btn_edit = view.findViewById(R.id.bsheet_edit);
        btn_edit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DataInputActivity.class);
            intent.putExtra("update", true);
            intent.putExtra("id", dataItem.getId());
            intent.putExtra("category", dataItem.getCategory());
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
