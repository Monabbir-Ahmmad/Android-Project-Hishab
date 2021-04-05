package com.example.hishab.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.hishab.DataInputActivity;
import com.example.hishab.R;
import com.example.hishab.data.DataItem;

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


        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getActivity(), DataInputActivity.class);
            intent.putExtra("category", category[position]);
            startActivity(intent);
        });

        return view;
    }

    //This creates the category grid buttons
    private void createGridButtons() {
        category = getResources().getStringArray(R.array.categoryArray);
        ArrayList<DataItem> btnArray = new ArrayList<>();
        for (String s : category) {
            DataItem dataItem = new DataItem(getActivity());
            dataItem.setCategory(s);
            btnArray.add(dataItem);
        }

        GridAdapter gridAdapter = new GridAdapter(getActivity(), btnArray);
        gridView.setAdapter(gridAdapter);
    }


}