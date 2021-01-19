package com.example.hishab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OverviewFragment extends Fragment implements FilterDialog.FilterDialogListener, BottomSheetDialog.BottomSheetListener {

    private TextView tv_expense;
    private ExtendedFloatingActionButton btn_filter;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataItem> dataSet;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        dataSet = new ArrayList<>(databaseHelper.getAllData());

        tv_expense = view.findViewById(R.id.textView_expense);

        //This calculates the top panel values on startup
        topPanelCalculation();

        btn_filter = view.findViewById(R.id.button_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This opens the filter dialog
                FilterDialog filterDialog = new FilterDialog();
                filterDialog.setTargetFragment(OverviewFragment.this, 1);
                filterDialog.show(getActivity().getSupportFragmentManager(), "FilterDialog");
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);

        //This creates the RecyclerView
        createRecyclerView();

        return view;
    }


    //This creates the RecyclerView
    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdapter = new RecyclerViewAdapter(dataSet);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //This opens a bottom sheet with details from recyclerView item
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(dataSet.get(position), position);
                bottomSheetDialog.setTargetFragment(OverviewFragment.this, 2);
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "BottomDialog");
            }
        });

    }

    //Applies the filters
    @Override
    public void applyFilter(String category, String sortBy, String startDate, String endDate) {
        dataSet = databaseHelper.getFilteredData(category, sortBy, startDate, endDate);
        createRecyclerView();
        topPanelCalculation();
    }

    //Delete data when delete button is pressed on bottom sheet
    @Override
    public void deleteItem(int position) {
        databaseHelper.deleteData(dataSet.get(position).getId());
        dataSet.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        topPanelCalculation();
    }

    //This calculates the top panel values on startup
    private void topPanelCalculation() {
        float expense = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        for (int i = 0; i < dataSet.size(); i++) {
            expense += dataSet.get(i).getMoney();
        }

        //This will set the current total expense
        tv_expense.setText(decimalFormat.format(expense) + " BDT");

    }

}