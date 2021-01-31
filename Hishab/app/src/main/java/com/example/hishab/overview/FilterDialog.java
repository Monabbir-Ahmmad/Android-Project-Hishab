package com.example.hishab.overview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.hishab.CustomDateTime;
import com.example.hishab.R;

import java.util.ArrayList;
import java.util.Arrays;

public class FilterDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private EditText filter_startDate, filter_endDate;
    private AutoCompleteTextView filter_category, filter_sortBy;
    private Button filter_cancel, filter_apply;
    private FilterDialogListener listener;
    private CustomDateTime customDateTime;


    //Interface for FilterDialogListener
    public interface FilterDialogListener {
        void applyFilter(String category, String sortBy, long startTimestamp, long endTimestamp);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.filter_dialog, null);
        builder.setView(view);

        customDateTime = new CustomDateTime(getActivity());

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
        category.addAll(Arrays.asList(getResources().getStringArray(R.array.categoryArray)));
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
            customDateTime.pickDate(filter_startDate);

        } else if (v.getId() == R.id.filter_endDate) {
            customDateTime.pickDate(filter_endDate);

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

    //When apply button is pressed check conditions for date interval and apply filter
    private void onFilterApplyClick() {
        long startTimestamp = 1L;
        long endTimestamp = 4200000000L;

        if (!filter_startDate.getText().toString().isEmpty()) {
            startTimestamp = customDateTime.getTimestamp(filter_startDate.getText().toString(), customDateTime.START_OF_DAY);
        }
        if (!filter_endDate.getText().toString().isEmpty()) {
            endTimestamp = customDateTime.getTimestamp(filter_endDate.getText().toString(), customDateTime.END_OF_DAY);
        }

        if (startTimestamp <= endTimestamp) {
            listener.applyFilter(filter_category.getText().toString(), filter_sortBy.getText().toString(), startTimestamp, endTimestamp);
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Invalid date interval", Toast.LENGTH_SHORT).show();
        }
    }



}
