package com.example.hishab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class ExpenseFragment extends Fragment {

    private GridView gridView;
    private String[] category;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        getActivity().setTitle("Expense");

        gridView = view.findViewById(R.id.gridView);
        createGridButtons();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DataInputActivity.class);
                intent.putExtra("key", category[position]);
                startActivity(intent);
            }
        });

        return view;
    }

    private void createGridButtons() {

        category = getResources().getStringArray(R.array.Category);
        ArrayList<DataItem> btn_array = new ArrayList<>();
        for (int i = 0; i < category.length; i++) {
            DataItem dataItem = new DataItem();
            dataItem.setCategory(category[i]);
            btn_array.add(dataItem);
        }

        GridAdapter gridAdapter = new GridAdapter(getActivity(), btn_array);
        gridView.setAdapter(gridAdapter);

    }

}