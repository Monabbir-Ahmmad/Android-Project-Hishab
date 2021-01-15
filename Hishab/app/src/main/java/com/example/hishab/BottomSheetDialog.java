package com.example.hishab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private DataItem dataItem;
    private int position;
    private TextView tv_category, tv_amount, tv_date, tv_time, tv_note;
    private Button btn_delete;
    private ImageButton btn_close;
    private BottomSheetListener listener;


    //Interface for BottomSheetListener
    public interface BottomSheetListener {
        void deleteItem(int position);
    }

    public BottomSheetDialog(DataItem dataItem, int position) {
        this.dataItem = dataItem;
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        tv_category = view.findViewById(R.id.bsheet_category);
        tv_category.setText(dataItem.getCategory());

        tv_amount = view.findViewById(R.id.bsheet_amount);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        tv_amount.setText("Amount: " + decimalFormat.format(dataItem.getMoney()) + " BDT");

        tv_date = view.findViewById(R.id.bsheet_date);
        tv_date.setText("Date: " + dataItem.getDate());

        tv_time = view.findViewById(R.id.bsheet_time);
        tv_time.setText("Time: " + dataItem.getTime());

        tv_note = view.findViewById(R.id.bsheet_note);
        if (dataItem.getNote() != null)
            tv_note.setText("Note: " + dataItem.getNote());

        btn_close = view.findViewById(R.id.bsheet_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_delete = view.findViewById(R.id.bsheet_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteItem(position);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (BottomSheetListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }
}
