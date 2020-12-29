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

        //This gets a color according to theme
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, typedValue, true);

        ArrayList<PieEntry> values = new ArrayList<>();

        String[] category = getResources().getStringArray(R.array.Category);
        //This adds the values into the PieEntry
        for (int i = 0; i < category.length; i++)
            values.add(new PieEntry((i * 2 + 3) * 7, category[i]));


        //This inserts the PieEntry into the PieDataSet
        PieDataSet dataSet = new PieDataSet(values, "Category");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setColors(getContext().getResources().getIntArray(R.array.colorArray));

        // Outside values
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(typedValue.data);
        dataSet.setValueLinePart1OffsetPercentage(100f); /** When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
        dataSet.setValueLinePart1Length(0.5f); /** When valuePosition is OutsideSlice, indicates length of first half of the line */
        dataSet.setValueLinePart2Length(0.5f); /** When valuePosition is OutsideSlice, indicates length of second half of the line */


        //This creates the PieData from PieDataSet
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setEntryLabelColor(typedValue.data);
        pieChart.setEntryLabelTextSize(10f);
        pieChart.setUsePercentValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));


        //Legends are the detailed name of each slice
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        legend.setTextColor(typedValue.data);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);

        //Description of the chart
        Description description = pieChart.getDescription();
        description.setEnabled(false);
        description.setTextColor(typedValue.data);

        //Transparent Circle
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setTransparentCircleColor(Color.BLACK);

        //Center hole
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setCenterTextSize(50f);
        pieChart.setCenterTextRadiusPercent(60f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        //Center Text
        pieChart.setCenterText("Monthly Expense");
        pieChart.setCenterTextColor(typedValue.data);
        pieChart.setCenterTextSize(20f);

        //Animation
        pieChart.animateXY(1000, 1000);
        pieChart.setDragDecelerationFrictionCoef(0.99f);

        //Insert data
        pieChart.setNoDataText("No data available");
        pieChart.setExtraOffsets(0, 20, 0, 20);
        pieChart.setData(data);
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
            values.add(new Entry(i, allData.get(i).getMoney()));
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

