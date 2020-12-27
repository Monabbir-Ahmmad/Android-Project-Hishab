package com.example.hishab;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class OverviewFragment extends Fragment implements View.OnClickListener {

    private TextView textView_expense;
    private ExtendedFloatingActionButton button_filter;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alert;
    private EditText editText_filter_start_date, editText_filter_end_date;
    private AutoCompleteTextView dropdown_category;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        getActivity().setTitle("Overview");

        textView_expense = view.findViewById(R.id.textView_expense_value);

        //This calculates the top panel values on startup
        topPanelCalculation();

        button_filter = view.findViewById(R.id.button_filter);
        button_filter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_filter:
                openFilterDialog();
                break;
            case R.id.button_filter_apply:
                Toast.makeText(getContext(), dropdown_category.getText() + " & " + editText_filter_start_date.getText() + " & " + editText_filter_end_date.getText(), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                break;
            case R.id.button_filter_cancel:
                alertDialog.dismiss();
                break;
            case R.id.filterStartDate:
                selectDate(editText_filter_start_date);
                break;
            case R.id.filterEndDate:
                selectDate(editText_filter_end_date);
                break;
            default:
                break;
        }

    }

    //This is the filter dialog
    private void openFilterDialog() {

        View mView = getLayoutInflater().inflate(R.layout.filter_layout_dialog, null);
        //This is the start date edit text on filter dialog
        editText_filter_start_date = mView.findViewById(R.id.filterStartDate);
        editText_filter_start_date.setOnClickListener(this::onClick);

        //This is the end date edit text on filter dialog
        editText_filter_end_date = mView.findViewById(R.id.filterEndDate);
        editText_filter_end_date.setOnClickListener(this::onClick);

        //This is the cancel button on filter dialog
        Button button_cancel = mView.findViewById(R.id.button_filter_cancel);
        button_cancel.setOnClickListener(this::onClick);

        //This is the apply button on filter dialog
        Button button_apply = mView.findViewById(R.id.button_filter_apply);
        button_apply.setOnClickListener(this::onClick);

        alert = new AlertDialog.Builder(getActivity());
        alert.setView(mView);

        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        //This is the category spinner
        String[] category = getResources().getStringArray(R.array.Category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_filter, category);
        dropdown_category = mView.findViewById(R.id.dropdown_category);
        dropdown_category.setText(adapter.getItem(0), false);
        dropdown_category.setAdapter(adapter);

    }

    //This is date picker dialog
    private void selectDate(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    //This calculates the top panel values on startup
    private void topPanelCalculation() {
        float expense = 0;
        DatabaseHelper databaseHelper1 = new DatabaseHelper(getActivity());
        ArrayList<DataHolder> allData = new ArrayList<>(databaseHelper1.getAllData());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        for (int i = 0; i < allData.size(); i++) {
            expense += allData.get(i).getMoney();
        }

        //This will set the current total expense
        textView_expense.setText(decimalFormat.format(expense) + " BDT");

    }

}