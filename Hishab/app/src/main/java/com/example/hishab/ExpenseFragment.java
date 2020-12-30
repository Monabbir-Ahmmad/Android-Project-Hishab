package com.example.hishab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class ExpenseFragment extends Fragment implements View.OnClickListener {

    private Button btn;


    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        getActivity().setTitle("Expense");

        btn = view.findViewById(R.id.button_Misc);
        btn.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), DataInputActivity.class);

        Button category_button = v.findViewById(v.getId());

        intent.putExtra("key", category_button.getText().toString());
        startActivity(intent);

    }
}