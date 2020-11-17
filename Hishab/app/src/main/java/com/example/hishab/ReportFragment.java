package com.example.hishab;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class ReportFragment extends Fragment {

    PieChart pieChart;

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

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.textColorBW, typedValue, true);

        ArrayList<PieEntry> values = new ArrayList<>();

        values.add(new PieEntry(10, "A"));
        values.add(new PieEntry(20, "B"));
        values.add(new PieEntry(30, "C"));
        values.add(new PieEntry(40, "D"));
        values.add(new PieEntry(50, "E"));
        values.add(new PieEntry(60, "F"));

        PieDataSet dataSet = new PieDataSet(values, "Letters");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

        pieChart.setExtraOffsets(0, 0, 0, 0);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.getLegend().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.setTransparentCircleColor(Color.BLACK);
        pieChart.setCenterText("Monthly Expense");
        pieChart.setCenterTextColor(typedValue.data);
        pieChart.setCenterTextSize(20f);
        pieChart.animateY(1000, Easing.EaseInCubic);


    }
}