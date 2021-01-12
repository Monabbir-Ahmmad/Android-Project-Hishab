package com.example.hishab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private DataHolder data;
    private int position;
    private TextView textView_category, textView_amount, textView_date, textView_time, textView_note;
    private Button btn_delete;
    private BottomSheetListener listener;


    //Interface for BottomSheetListener
    public interface BottomSheetListener {
        void deleteData(int position);
    }

    public BottomSheetDialog(DataHolder data, int position) {
        this.data = data;
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        textView_category = view.findViewById(R.id.bsheet_category);
        textView_category.setText(data.getCategory());

        textView_amount = view.findViewById(R.id.bsheet_amount);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        textView_amount.setText("Amount: " + decimalFormat.format(data.getMoney()) + " BDT");

        textView_date = view.findViewById(R.id.bsheet_date);
        textView_date.setText("Date: " + data.getDate());

        textView_time = view.findViewById(R.id.bsheet_time);
        textView_time.setText("Time: " + data.getTime());

        textView_note = view.findViewById(R.id.bsheet_note);
        if (data.getNote() != null)
            textView_note.setText("Note: " + data.getNote());

        btn_delete = view.findViewById(R.id.bsheet_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteData(position);
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
