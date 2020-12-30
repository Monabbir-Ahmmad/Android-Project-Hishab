package com.example.hishab;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private PieChart pieChart;
    private LineChart lineChart;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataHolder> allData;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        getActivity().setTitle("Statistics");

        databaseHelper = new DatabaseHelper(getActivity());
        allData = new ArrayList<>(databaseHelper.getAllData());

        pieChart = view.findViewById(R.id.pieChart);
        createPieChart();

        lineChart = view.findViewById(R.id.lineChart);
        createLineChart();

        return view;
    }


    //This creates the pie chart
    private void createPieChart() {

        String[] category = getResources().getStringArray(R.array.Category);

        int[] colorArray = getContext().getResources().getIntArray(R.array.colorArray);
        List<Integer> colorList = new ArrayList<Integer>(colorArray.length);
        for (int i : colorArray)
            colorList.add(i);

        //This gets a color according to theme
        TypedValue color = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, color, true);

        //This adds the values into the PieEntry
        ArrayList<PieEntry> values = new ArrayList<>();
        for (int i = 0; i < category.length; i++)
            values.add(new PieEntry(i + 1, category[i]));


        //This inserts the PieEntry into the PieDataSet
        PieDataSet dataSet = new PieDataSet(values, null);
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setColors(colorArray);

        //Outside values with line
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.15f);
        dataSet.setValueLineWidth(2f);
        dataSet.setUsingSliceColorAsValueLineColor(true);


        //This creates the PieData from PieDataSet
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColors(colorList);
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        //Insert data
        pieChart.setNoDataText("No data available");
        pieChart.setData(pieData);

        //Slice shape
        pieChart.setDrawRoundedSlices(false);
        pieChart.setDrawSlicesUnderHole(false);

        //Entry label
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(color.data);
        pieChart.setEntryLabelTextSize(10f);
        pieChart.setUsePercentValues(true);

        //Transparent circle
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setTransparentCircleColor(Color.BLACK);

        //Center hole
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(85f);
        pieChart.setCenterTextSize(50f);
        pieChart.setCenterTextRadiusPercent(60f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        //Center Text
        pieChart.setCenterText("Weekly\nExpense");
        pieChart.setCenterTextColor(color.data);
        pieChart.setCenterTextSize(20f);

        //Animation
        pieChart.animateXY(1000, 1000);
        pieChart.setDragDecelerationFrictionCoef(0.97f);

        //Off set
        pieChart.setExtraOffsets(25f, 0f, 25f, 0f);


        //Legends are the detailed name of each slice
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(color.data);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10f);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //legend.setXOffset(0f);
        //legend.setYOffset(5f);
        //legend.setXEntrySpace(10f);
        legend.setYEntrySpace(7f);
        legend.setDrawInside(false);

        //Description of the chart
        Description description = pieChart.getDescription();
        description.setEnabled(false);
        description.setTextColor(color.data);

    }


    //This creates the line chart
    private void createLineChart() {

        //This gets a color according to theme
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        TypedValue typedValue2 = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, typedValue2, true);

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < allData.size(); i++) {
            values.add(new Entry(i+1, allData.get(i).getMoney()));
        }

        LineDataSet lineDataSet = new LineDataSet(values, "Data set");
        lineDataSet.setLineWidth(3f);
        lineDataSet.setColor(typedValue.data);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(1.5f);
        lineDataSet.setCircleHoleColor(Color.TRANSPARENT);
        lineDataSet.setCircleColor(typedValue.data);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextColor(typedValue2.data);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setCubicIntensity(0f);
        lineDataSet.setFillColor(typedValue.data);

        LineData lineData = new LineData(lineDataSet);

        //Description of the chart
        Description description = lineChart.getDescription();
        description.setEnabled(false);

        //Legends are the detailed name of each slice
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawBorders(false);
        lineChart.setNoDataText("No data available");
        lineChart.setData(lineData);

        //X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceMin(0.1f);
        xAxis.setSpaceMax(0.1f);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(typedValue2.data);
        xAxis.setAxisLineColor(typedValue2.data);

        //Y axis left
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(true);
        yAxisLeft.setDrawLabels(true);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setGranularity(5);
        yAxisLeft.setTextColor(typedValue2.data);
        yAxisLeft.setAxisLineColor(typedValue2.data);

        //Y axis left
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }


}

