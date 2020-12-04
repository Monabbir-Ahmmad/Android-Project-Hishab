package com.example.hishab;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class OverviewFragment extends Fragment {

    private TextView textView_current_date, textView_income, textView_expense, textView_balanceleft;
    private ImageButton button_filter;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        getActivity().setTitle("Overview");

        textView_income = view.findViewById(R.id.textView_income_value);
        textView_expense = view.findViewById(R.id.textView_expense_value);
        textView_balanceleft = view.findViewById(R.id.textView_balanceleft_value);
        textView_current_date = view.findViewById(R.id.textView_current_date);

        //This calculates the top panel values on startup
        topPanelCalculation();

        button_filter = view.findViewById(R.id.button_filter);
        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        return view;
    }

    //This is the filter dialog
    private void openFilterDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.filter_layout_dialog, null);

        Button button_cancel = mView.findViewById(R.id.button_filter_cancel);
        Button button_apply = mView.findViewById(R.id.button_filter_apply);

        alert.setView(mView);

        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();


        //This is the category spinner
        String[] COUNTRIES = {"Item 1", "Item 2", "Item 3", "Item 4"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.dropdown_menu_filter,
                        COUNTRIES);

        AutoCompleteTextView dropdown_category = mView.findViewById(R.id.dropdown_category);
        dropdown_category.setText(adapter.getItem(0), false);
        dropdown_category.setAdapter(adapter);

        AutoCompleteTextView dropdown_date = mView.findViewById(R.id.dropdown_date);
        dropdown_date.setText(adapter.getItem(0), false);
        dropdown_date.setAdapter(adapter);


        //This is what happens when cancel button is pressed
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //This is what happens when apply button is pressed
        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), dropdown_category.getText() + " & " + dropdown_date.getText(), Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();
            }
        });


    }

    //This calculates the top panel values on startup
    private void topPanelCalculation() {
        int income = 0, expense = 0, balance_left = 0;
        DatabaseHelper databaseHelper1 = new DatabaseHelper(getActivity());
        ArrayList<DataHolder> allData = new ArrayList<>(databaseHelper1.getAllData());

        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i).getTransaction_type().equals("Income")) {
                income += allData.get(i).getMoney();
            } else {
                expense += allData.get(i).getMoney();
            }
        }

        //This will set the current total income
        textView_income.setText(String.valueOf(income) + "tk");
        //This will set the current total expense
        textView_expense.setText(String.valueOf(expense) + "tk");
        //This will set the current balance left
        textView_balanceleft.setText(String.valueOf(income - expense) + "tk");
        //This will set the current time
        textView_current_date.setText(new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(new Date()));
    }
}