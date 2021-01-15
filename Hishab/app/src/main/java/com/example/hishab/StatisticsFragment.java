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
import java.util.Arrays;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private PieChart pieChart;
    private LineChart lineChart;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataItem> allData;


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
        setPieData();

        lineChart = view.findViewById(R.id.lineChart);
        createLineChart();

        return view;
    }


    //This sets the data into the pie chart
    private void setPieData() {
        String[] category = getResources().getStringArray(R.array.Category);
        float[] money = new float[category.length];
        Arrays.fill(money, 0);
        int index;

        for (int i = 0; i < allData.size(); i++) {
            index = Arrays.asList(category).indexOf(allData.get(i).getCategory());
            money[index] += allData.get(i).getMoney();
        }


        //This adds the values into the PieEntry
        ArrayList<PieEntry> pieEntryArray = new ArrayList<>();
        for (int i = 0; i < category.length; i++) {
            if (money[i] > 0) {
                pieEntryArray.add(new PieEntry(money[i], category[i]));
            }
        }

        //Insert PieEntries into the PieDataSet and create PieData from PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryArray, null);
        PieData pieData = new PieData(pieDataSet);

        if (pieEntryArray.size() > 0) {
            pieChart.setData(pieData);
        }
        createPieChart(pieDataSet, pieData);
    }


    //This is the pie chart design
    private void createPieChart(PieDataSet dataSet, PieData pieData) {
        //This gets a color array
        int[] colorArray = getContext().getResources().getIntArray(R.array.colorArray);
        List<Integer> colorList = new ArrayList<Integer>(colorArray.length);
        for (int i : colorArray)
            colorList.add(i);

        //This gets a color according to theme
        TypedValue colorBlackWhite = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, colorBlackWhite, true);


        //Pie slice attr
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setColors(colorArray);
        pieChart.setDrawRoundedSlices(false);
        pieChart.setDrawSlicesUnderHole(false);

        //Outside values with line
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100f);
        dataSet.setValueLinePart1Length(0.25f);
        dataSet.setValueLinePart2Length(0.15f);
        dataSet.setValueLineWidth(2f);
        dataSet.setUsingSliceColorAsValueLineColor(true);

        //Pie value attr
        pieData.setValueTextSize(10f);
        pieData.setValueTextColors(colorList);
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        //No data text
        pieChart.setNoDataText("No data available");
        pieChart.setNoDataTextColor(colorBlackWhite.data);

        //Entry label
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(colorBlackWhite.data);
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
        pieChart.setCenterTextColor(colorBlackWhite.data);
        pieChart.setCenterTextSize(20f);

        //Animation
        pieChart.animateXY(1000, 1000);
        pieChart.setDragDecelerationFrictionCoef(0.97f);

        //Off set
        pieChart.setExtraOffsets(25f, 0f, 25f, 0f);

        //Pie legend
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(colorBlackWhite.data);
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

        //Pie description
        Description description = pieChart.getDescription();
        description.setEnabled(false);

    }


    //This creates the line chart
    private void createLineChart() {
        //This gets a color according to theme
        TypedValue colorPrimary = new TypedValue();
        TypedValue colorBlackWhite = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, colorPrimary, true);
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, colorBlackWhite, true);

        //This adds the values into the LineEntry
        ArrayList<Entry> lineEntryArray = new ArrayList<>();
        for (int i = allData.size() - 1; i >= 0; i--) {
            lineEntryArray.add(new Entry(allData.size() - i, allData.get(i).getMoney()));
        }

        //Insert LineEntries into the LineDataSet and create LineData from LineDataSet
        LineDataSet lineDataSet = new LineDataSet(lineEntryArray, null);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setNoDataText("No data available");
        lineChart.setData(lineData);

        lineDataSet.setLineWidth(3f);
        lineDataSet.setColor(colorPrimary.data);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(1.5f);
        lineDataSet.setCircleHoleColor(Color.TRANSPARENT);
        lineDataSet.setCircleColor(colorPrimary.data);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextColor(colorBlackWhite.data);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setCubicIntensity(0f);
        lineDataSet.setFillColor(colorPrimary.data);

        //Description of the chart
        Description description = lineChart.getDescription();
        description.setEnabled(false);

        //Legends of the chart
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawBorders(false);


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
        xAxis.setTextColor(colorBlackWhite.data);
        xAxis.setAxisLineColor(colorBlackWhite.data);

        //Y axis left
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(true);
        yAxisLeft.setDrawLabels(true);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setGranularity(5);
        yAxisLeft.setTextColor(colorBlackWhite.data);
        yAxisLeft.setAxisLineColor(colorBlackWhite.data);

        //Y axis left
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }


}

