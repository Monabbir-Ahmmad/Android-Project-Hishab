package com.example.hishab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FilterDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private EditText filter_startDate, filter_endDate;
    private AutoCompleteTextView filter_category, filter_sortBy;
    private Button filter_cancel, filter_apply;
    private FilterDialogListener listener;

    //Interface for FilterDialogListener
    public interface FilterDialogListener {
        void applyFilter(String category, String sortBy, String startDate, String endDate);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.filter_dialog, null);
        builder.setView(view);

        //This is the start date edit text on filter dialog
        filter_startDate = view.findViewById(R.id.filter_startDate);
        filter_startDate.setOnClickListener(this::onClick);

        //This is the end date edit text on filter dialog
        filter_endDate = view.findViewById(R.id.filter_endDate);
        filter_endDate.setOnClickListener(this::onClick);

        //This is the cancel button on filter dialog
        filter_cancel = view.findViewById(R.id.filter_cancel);
        filter_cancel.setOnClickListener(this::onClick);

        //This is the apply button on filter dialog
        filter_apply = view.findViewById(R.id.filter_apply);
        filter_apply.setOnClickListener(this::onClick);


        //This is the category dropdown
        ArrayList<String> category = new ArrayList<>();
        category.add("All");
        category.addAll(Arrays.asList(getResources().getStringArray(R.array.Category)));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_filter, category);
        filter_category = view.findViewById(R.id.filter_category);
        filter_category.setText(categoryAdapter.getItem(0), false);
        filter_category.setAdapter(categoryAdapter);

        //This is the sort by dropdown
        String[] sortBy = {"Default", "Date: Newest", "Date: Oldest", "Money: ASC", "Money: DESC"};
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_filter, sortBy);
        filter_sortBy = view.findViewById(R.id.filter_sortBy);
        filter_sortBy.setText(sortByAdapter.getItem(0), false);
        filter_sortBy.setAdapter(sortByAdapter);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.filter_apply) {
            onFilterApplyClick();

        } else if (v.getId() == R.id.filter_cancel) {
            dismiss();

        } else if (v.getId() == R.id.filter_startDate) {
            selectDate(filter_startDate);

        } else if (v.getId() == R.id.filter_endDate) {
            selectDate(filter_endDate);

        }
    }

    //When apply button is pressed check conditions for date interval and apply filter
    private void onFilterApplyClick() {
        String startDate = "100000000000";
        String endDate = "300000000000";

        if (!filter_startDate.getText().toString().isEmpty()) {
            startDate = generateDatetimeID(filter_startDate.getText().toString(), "12:00 am");
        }
        if (!filter_endDate.getText().toString().isEmpty()) {
            endDate = generateDatetimeID(filter_endDate.getText().toString(), "11:59 pm");
        }

        if (Long.parseLong(startDate) <= Long.parseLong(endDate)) {
            listener.applyFilter(filter_category.getText().toString(), filter_sortBy.getText().toString(), startDate, endDate);
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Invalid date interval", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (FilterDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FilterDialogListener");
        }
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

    //This generates DatetimeID form Date
    private String generateDatetimeID(String date, String time) {
        String datetime = date + " " + time;
        String inputPattern = "dd MMM yyyy hh:mm a";
        String outputPattern = "yyyyMMddHHmm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date datetimeInputFormat = null;
        String datetimeOutputFormat = null;

        try {
            datetimeInputFormat = inputFormat.parse(datetime);
            datetimeOutputFormat = outputFormat.format(datetimeInputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datetimeOutputFormat;
    }


}
