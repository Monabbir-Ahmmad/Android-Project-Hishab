package com.example.hishab;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;


public class OverviewFragment extends Fragment {

    TextView textView_current_date, textView_income, textView_expense, textView_balanceleft;
    ImageButton button_filter;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        textView_income = view.findViewById(R.id.textView_income_value);
        textView_expense = view.findViewById(R.id.textView_expense_value);
        textView_balanceleft = view.findViewById(R.id.textView_balanceleft_value);
        textView_current_date = view.findViewById(R.id.textView_current_date);

        button_filter = view.findViewById(R.id.button_filter);
        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        topPanelCalculation();


        return view;
    }

    //This is the filter dialog
    private void openFilterDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.filter_layout_dialog, null);

        ImageButton button_cancel = mview.findViewById(R.id.button_filter_cancel);
        ImageButton button_save = mview.findViewById(R.id.button_filter_ok);

        alert.setView(mview);

        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    //This calculates the top panel values on startup
    private void topPanelCalculation() {
        //This will set the current time
        Calendar calendar = Calendar.getInstance();
        textView_current_date.setText(DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime()));

    }
}