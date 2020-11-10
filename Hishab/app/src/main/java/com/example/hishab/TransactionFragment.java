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

    Button button, button2;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        getActivity().setTitle("Transaction");


        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), ActivityDataInput.class);
        ArrayList<String> type_category = new ArrayList<String>();

        if (v.getId() == R.id.button) {
            type_category.add("Income");
            type_category.add(button.getText().toString());
            intent.putExtra("key", type_category);
        }

        else if (v.getId() == R.id.button2) {
            type_category.add("Expanse");
            type_category.add(button2.getText().toString());
            intent.putExtra("key", type_category);
        }

        startActivity(intent);
    }
}