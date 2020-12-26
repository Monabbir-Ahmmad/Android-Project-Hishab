package com.example.hishab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class ExpenseFragment extends Fragment implements View.OnClickListener {

    private Button billsButton, clothingButton, debtButton, educationButton, entertainmentButton,
            foodButton, groceriesButton, healthcareButton, petcareButton, rentButton, savingsButton,
            transportationButton, vehicleButton, otherIncomeButton;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        getActivity().setTitle("Expense");


        billsButton = view.findViewById(R.id.billsButton);
        billsButton.setOnClickListener(this);

        clothingButton = view.findViewById(R.id.clothingButton);
        clothingButton.setOnClickListener(this);

        debtButton = view.findViewById(R.id.debtButton);
        debtButton.setOnClickListener(this);

        educationButton = view.findViewById(R.id.educationButton);
        educationButton.setOnClickListener(this);

        entertainmentButton = view.findViewById(R.id.entertainmentButton);
        entertainmentButton.setOnClickListener(this);

        foodButton = view.findViewById(R.id.foodButton);
        foodButton.setOnClickListener(this);

        groceriesButton = view.findViewById(R.id.groceriesButton);
        groceriesButton.setOnClickListener(this);

        healthcareButton = view.findViewById(R.id.healthcareButton);
        healthcareButton.setOnClickListener(this);

        petcareButton = view.findViewById(R.id.petcareButton);
        petcareButton.setOnClickListener(this);

        rentButton = view.findViewById(R.id.rentButton);
        rentButton.setOnClickListener(this);

        savingsButton = view.findViewById(R.id.savingsButton);
        savingsButton.setOnClickListener(this);

        transportationButton = view.findViewById(R.id.transportationButton);
        transportationButton.setOnClickListener(this);

        vehicleButton = view.findViewById(R.id.vehicleButton);
        vehicleButton.setOnClickListener(this);

        otherIncomeButton = view.findViewById(R.id.otherIncomeButton);
        otherIncomeButton.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), DataInputActivity.class);
        String category = "";

        Button category_button = v.findViewById(v.getId());

        category = category_button.getText().toString();

        intent.putExtra("key", category);
        startActivity(intent);

    }
}