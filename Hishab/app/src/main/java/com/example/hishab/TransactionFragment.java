package com.example.hishab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class TransactionFragment extends Fragment implements View.OnClickListener {

    Button salaryButton, foodButton;


    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        getActivity().setTitle("Transaction");


        salaryButton = view.findViewById(R.id.salaryButton);
        foodButton = view.findViewById(R.id.foodButton);
        salaryButton.setOnClickListener(this);
        foodButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), DataInputActivity.class);
        ArrayList<String> type_category = new ArrayList<String>();

        switch (v.getId()) {
            case R.id.salaryButton:
                type_category.add("Income");
                type_category.add(salaryButton.getText().toString());
                break;
            case R.id.foodButton:
                type_category.add("Expanse");
                type_category.add(foodButton.getText().toString());
                break;
        }
        intent.putExtra("key", type_category);
        startActivity(intent);

    }
}