package com.example.hishab.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.hishab.DataInputActivity;
import com.example.hishab.DataItem;
import com.example.hishab.R;

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

        gridView = view.findViewById(R.id.gridView);
        createGridButtons();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DataInputActivity.class);
                intent.putExtra("category", category[position]);
                startActivity(intent);
            }
        });

        return view;
    }

    //This creates the category grid buttons
    private void createGridButtons() {
        category = getResources().getStringArray(R.array.categoryArray);
        ArrayList<DataItem> btn_array = new ArrayList<>();
        for (int i = 0; i < category.length; i++) {
            DataItem dataItem = new DataItem(getActivity());
            dataItem.setCategory(category[i]);
            btn_array.add(dataItem);
        }

        GridAdapter gridAdapter = new GridAdapter(getActivity(), btn_array);
        gridView.setAdapter(gridAdapter);
    }


}