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

    private EditText editText_filter_start_date, editText_filter_end_date;
    private AutoCompleteTextView dropdown_category, dropdown_sortBy;
    private Button button_cancel, button_apply;
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
        editText_filter_start_date = view.findViewById(R.id.filterStartDate);
        editText_filter_start_date.setOnClickListener(this::onClick);

        //This is the end date edit text on filter dialog
        editText_filter_end_date = view.findViewById(R.id.filterEndDate);
        editText_filter_end_date.setOnClickListener(this::onClick);

        //This is the cancel button on filter dialog
        button_cancel = view.findViewById(R.id.button_filter_cancel);
        button_cancel.setOnClickListener(this::onClick);

        //This is the apply button on filter dialog
        button_apply = view.findViewById(R.id.button_filter_apply);
        button_apply.setOnClickListener(this::onClick);


        //This is the category dropdown
        ArrayList<String> category = new ArrayList<>();
        category.add("All");
        category.addAll(Arrays.asList(getResources().getStringArray(R.array.Category)));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_filter, category);
        dropdown_category = view.findViewById(R.id.dropdown_category);
        dropdown_category.setText(categoryAdapter.getItem(0), false);
        dropdown_category.setAdapter(categoryAdapter);

        //This is the sort by dropdown
        String[] sortBy = {"Default", "Date: Newest", "Date: Oldest", "Money: ASC", "Money: DESC"};
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_filter, sortBy);
        dropdown_sortBy = view.findViewById(R.id.dropdown_sortBy);
        dropdown_sortBy.setText(sortByAdapter.getItem(0), false);
        dropdown_sortBy.setAdapter(sortByAdapter);

        return builder.create();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_filter_cancel) {
            dismiss();

        } else if (v.getId() == R.id.button_filter_apply) {
            String startDate = "01 Jan 1000";
            String startTime = "12:00 am";
            String endDate = "01 Jan 3000";
            String endTime = "11:59 pm";

            if (!editText_filter_start_date.getText().toString().isEmpty()) {
                startDate = editText_filter_start_date.getText().toString();
            }

            if (!editText_filter_end_date.getText().toString().isEmpty()) {
                endDate = editText_filter_end_date.getText().toString();
            }

            String startDateID = generateDatetimeID(startDate, startTime);
            String endDateID = generateDatetimeID(endDate, endTime);

            if (Long.parseLong(startDateID) <= Long.parseLong(endDateID)) {
                listener.applyFilter(dropdown_category.getText().toString(), dropdown_sortBy.getText().toString(), startDateID, endDateID);
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Wrong date interval", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.filterStartDate) {
            selectDate(editText_filter_start_date);

        } else if (v.getId() == R.id.filterEndDate) {
            selectDate(editText_filter_end_date);

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
