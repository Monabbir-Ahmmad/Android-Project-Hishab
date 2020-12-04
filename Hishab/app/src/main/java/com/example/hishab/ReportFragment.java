package com.example.hishab;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {

    private PieChart pieChart;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        getActivity().setTitle("Report");


        pieChart = view.findViewById(R.id.pieChart);
        createPieChart();

        return view;
    }


    //This creates the pie chart
    private void createPieChart() {

        //This gets a color according to theme
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.textColorBW, typedValue, true);

        ArrayList<PieEntry> values = new ArrayList<>();

        //This adds the values into the PieEntry
        values.add(new PieEntry(13, "Shopping"));
        values.add(new PieEntry(11, "Clothing"));
        values.add(new PieEntry(10, "Education"));
        values.add(new PieEntry(12, "Bills"));
        values.add(new PieEntry(13, "Food"));
        values.add(new PieEntry(11, "Car"));
        values.add(new PieEntry(10, "Pets"));
        values.add(new PieEntry(12, "Entertainment"));
        values.add(new PieEntry(13, "Transportation"));
        values.add(new PieEntry(11, "Communication"));
        values.add(new PieEntry(10, "Health"));
        values.add(new PieEntry(10, "Rent"));
        values.add(new PieEntry(12, "Other"));


        //This inserts the PieEntry into the PieDataSet
        PieDataSet dataSet = new PieDataSet(values, "Category");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(getContext().getResources().getIntArray(R.array.colorArray));

        //This creates the PieData from PieDataSet
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setEntryLabelColor(typedValue.data);
        pieChart.setUsePercentValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));


        //Legends are the detailed name of each slice
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(typedValue.data);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);

        //Description of the chart
        Description description = pieChart.getDescription();
        description.setEnabled(false);
        description.setTextColor(typedValue.data);

        //Insert data
        pieChart.setData(data);
        pieChart.setExtraOffsets(10, 0, 10, 50);

        //Transparent Circle
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setTransparentCircleColor(Color.BLACK);

        //Center hole
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        //Center Text
        pieChart.setCenterText("Monthly Expense");
        pieChart.setCenterTextColor(typedValue.data);
        pieChart.setCenterTextSize(20f);

        //Animation
        pieChart.animateXY(1000, 1000);
        pieChart.setDragDecelerationFrictionCoef(0.99f);

    }
}