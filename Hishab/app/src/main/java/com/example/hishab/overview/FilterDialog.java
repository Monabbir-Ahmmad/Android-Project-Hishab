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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class FilterDialog extends AppCompatDialogFragment {

    private EditText filterStartDate, filterEndDate;
    private AutoCompleteTextView filterCategory, filterSortBy;
    private Button filterCancel, filterApply;
    private FilterDialogListener listener;
    private CustomDateTime customDateTime;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.filter_dialog, null);
        builder.setView(view);

        //Find views
        filterStartDate = view.findViewById(R.id.filter_startDate);
        filterEndDate = view.findViewById(R.id.filter_endDate);
        filterApply = view.findViewById(R.id.filter_apply);
        filterCategory = view.findViewById(R.id.filter_category);
        filterSortBy = view.findViewById(R.id.filter_sortBy);

        customDateTime = new CustomDateTime(getActivity());

        //This is the start date edit text on filter dialog
        filterStartDate.setOnClickListener(v -> customDateTime.pickDate(filterStartDate));

        //This is the end date edit text on filter dialog
        filterEndDate.setOnClickListener(v -> customDateTime.pickDate(filterEndDate));

        //This is the cancel button on filter dialog
        filterCancel = view.findViewById(R.id.filter_cancel);
        filterCancel.setOnClickListener(v -> dismiss());

        //This is the apply button on filter dialog
        filterApply.setOnClickListener(v -> onFilterApplyClick());


        //This is the category dropdown
        ArrayList<String> category = new ArrayList<>();
        category.add("All");
        category.addAll(Arrays.asList(getResources().getStringArray(R.array.categoryArray)));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_filter, category);
        filterCategory.setText(categoryAdapter.getItem(0), false);
        filterCategory.setAdapter(categoryAdapter);

        //This is the sort by dropdown
        String[] sortBy = getResources().getStringArray(R.array.sortByArray);
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_filter, sortBy);
        filterSortBy.setText(sortByAdapter.getItem(0), false);
        filterSortBy.setAdapter(sortByAdapter);

        return builder.create();
    }


    @Override
    public void onAttach(@NotNull Context context) {
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

        if (!filterStartDate.getText().toString().isEmpty()) {
            startTimestamp = customDateTime.getTimestamp(filterStartDate.getText().toString(), customDateTime.START_OF_DAY);
        }
        if (!filterEndDate.getText().toString().isEmpty()) {
            endTimestamp = customDateTime.getTimestamp(filterEndDate.getText().toString(), customDateTime.END_OF_DAY);
        }

        if (startTimestamp <= endTimestamp) {
            listener.applyFilter(filterCategory.getText().toString(), filterSortBy.getText().toString(), startTimestamp, endTimestamp);
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Invalid date interval", Toast.LENGTH_SHORT).show();
        }
    }

    //Interface for FilterDialogListener
    public interface FilterDialogListener {
        //This applies the filter
        void applyFilter(String category, String sortBy, long startTimestamp, long endTimestamp);
    }


}
