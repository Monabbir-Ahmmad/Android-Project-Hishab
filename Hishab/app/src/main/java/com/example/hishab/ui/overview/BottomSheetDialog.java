package com.example.hishab.ui.overview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hishab.DataInputActivity;
import com.example.hishab.DateTimeUtil;
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
    private DateTimeUtil dateTimeUtil;


    //Constructor
    public BottomSheetDialog(DataItem dataItem) {
        this.dataItem = dataItem;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        //Find views
        tvCategory = view.findViewById(R.id.bottomSheet_category);
        tvAmount = view.findViewById(R.id.bottomSheet_amount);
        tvDate = view.findViewById(R.id.bottomSheet_date);
        tvTime = view.findViewById(R.id.bottomSheet_time);
        tvNote = view.findViewById(R.id.bottomSheet_note);
        btnClose = view.findViewById(R.id.bottomSheet_close);
        btnEdit = view.findViewById(R.id.bottomSheet_edit);

        dateTimeUtil = new DateTimeUtil();

        tvCategory.setText(dataItem.getCategory());
        tvAmount.setText(String.format("%s BDT", decimalFormat.format(dataItem.getAmount())));
        tvDate.setText(dateTimeUtil.getDate(dataItem.getTimestamp()));
        tvTime.setText(dateTimeUtil.getTime(dataItem.getTimestamp()));

        if (dataItem.getNote() != null) {
            tvNote.setText(dataItem.getNote());
        } else {
            tvNote.setText("");
        }

        btnClose.setOnClickListener(v -> dismiss());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DataInputActivity.class);
            intent.putExtra("update", true);
            intent.putExtra("id", dataItem.getId());
            intent.putExtra("category", dataItem.getCategory());
            intent.putExtra("icon", dataItem.getIcon());
            intent.putExtra("amount", String.valueOf(dataItem.getAmount()));
            intent.putExtra("timestamp", dataItem.getTimestamp());
            intent.putExtra("note", dataItem.getNote());
            startActivity(intent);
            dismiss();
        });

        return view;
    }

}
