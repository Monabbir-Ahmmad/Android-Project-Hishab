package com.example.hishab.overview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.util.Pair;

import com.example.hishab.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class FilterDialog extends AppCompatDialogFragment {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private AutoCompleteTextView filterCategory, filterSortBy;
    private Button filterCancel, filterApply, filterDateRange;
    private FilterDialogListener listener;
    private long startTimestamp = 1L;
    private long endTimestamp = 4200000000L;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.filter_dialog, null);
        builder.setView(view);

        //Find views
        filterApply = view.findViewById(R.id.filter_apply);
        filterCancel = view.findViewById(R.id.filter_cancel);
        filterDateRange = view.findViewById(R.id.filter_dateRange);
        filterCategory = view.findViewById(R.id.filter_category);
        filterSortBy = view.findViewById(R.id.filter_sortBy);


        //This is the start date edit text on filter dialog
        filterDateRange.setOnClickListener(v -> {
            MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select date")
                    .build();

            dateRangePicker.show(getActivity().getSupportFragmentManager(), "Date picker");

            dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                @Override
                public void onPositiveButtonClick(Pair<Long, Long> selection) {
                    filterDateRange.setText(String.format("%s - %s", dateFormat.format(selection.first), dateFormat.format(selection.second)));
                    startTimestamp = selection.first / 1000;
                    endTimestamp = selection.second / 1000;
                }
            });
        });


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

        //This is the cancel button on filter dialog
        filterCancel.setOnClickListener(v -> dismiss());

        //This is the apply button on filter dialog
        filterApply.setOnClickListener(v -> {
            listener.applyFilter(filterCategory.getText().toString(), filterSortBy.getText().toString(), startTimestamp, endTimestamp);
            dismiss();
        });

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

    //Interface for FilterDialogListener
    public interface FilterDialogListener {
        //This applies the filter
        void applyFilter(String category, String sortBy, long startTimestamp, long endTimestamp);
    }


}
