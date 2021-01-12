package com.example.hishab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OverviewFragment extends Fragment implements FilterDialog.FilterDialogListener {

    private TextView textView_expense;
    private ExtendedFloatingActionButton btn_filter;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataHolder> allData;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        getActivity().setTitle("Overview");

        databaseHelper = new DatabaseHelper(getActivity());
        allData = new ArrayList<>(databaseHelper.getAllData());

        textView_expense = view.findViewById(R.id.textView_expense_value);

        //This calculates the top panel values on startup
        topPanelCalculation();

        btn_filter = view.findViewById(R.id.button_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);

        //This creates the RecyclerView
        createRecyclerView(allData);

        return view;
    }


    //This creates the RecyclerView
    private void createRecyclerView(ArrayList<DataHolder> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.Adapter recyclerView_adapter = new RecyclerViewAdapter(dataList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerView_adapter);

    }

    //This is the filter dialog
    private void openFilterDialog() {
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.setTargetFragment(OverviewFragment.this, 1);
        filterDialog.show(getActivity().getSupportFragmentManager(), "FilterDialog");

    }

    //Applies the filters
    @Override
    public void applyFilter(String category, String sortBy, String startDate, String endDate) {
        Toast.makeText(getContext(), category + " & " + sortBy + " & " + startDate + " & " + endDate, Toast.LENGTH_SHORT).show();
    }


    //This calculates the top panel values on startup
    private void topPanelCalculation() {
        float expense = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        ArrayList<DataHolder> allData = new ArrayList<>(dbHelper.getAllData());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        for (int i = 0; i < allData.size(); i++) {
            expense += allData.get(i).getMoney();
        }

        //This will set the current total expense
        textView_expense.setText(decimalFormat.format(expense) + " BDT");

    }


}