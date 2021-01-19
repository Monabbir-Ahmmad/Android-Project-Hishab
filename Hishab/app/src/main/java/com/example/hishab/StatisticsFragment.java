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
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private PieChart pieChart;
    private LineChart lineChart;
    private TabLayout tabLayout;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataItem> dataSet;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        dataSet = new ArrayList<>();

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                statisticsFilter(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pieChart = view.findViewById(R.id.pieChart);
        statisticsFilter(2);

        lineChart = view.findViewById(R.id.lineChart);
        //createLineChart();

        return view;
    }

    private void statisticsFilter(int tab) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        String startDate = null, endDate = null;

        if (tab == 0) {
            pieChart.setCenterText("Daily\nExpense");
            startDate = simpleDateFormat.format(new Date());
            endDate = startDate;

        } else if (tab == 1) {
            pieChart.setCenterText("Weekly\nExpense");
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
            startDate = simpleDateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
            endDate = simpleDateFormat.format(calendar.getTime());

        } else if (tab == 2) {
            pieChart.setCenterText("Monthly\nExpense");
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            startDate = simpleDateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = simpleDateFormat.format(calendar.getTime());

        } else if (tab == 3) {
            pieChart.setCenterText("Yearly\nExpense");
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
            startDate = simpleDateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
            endDate = simpleDateFormat.format(calendar.getTime());

        }
        startDate = generateDatetimeID(startDate, "12:00 am");
        endDate = generateDatetimeID(endDate, "11:59 pm");
        dataSet = databaseHelper.getFilteredData("All", "Default", startDate, endDate);

        setPieData();
    }


    //This sets the data into the pie chart
    private void setPieData() {
        String[] category = getResources().getStringArray(R.array.Category);
        float[] money = new float[category.length];
        Arrays.fill(money, 0);
        int index;

        for (int i = 0; i < dataSet.size(); i++) {
            index = Arrays.asList(category).indexOf(dataSet.get(i).getCategory());
            money[index] += dataSet.get(i).getMoney();
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
    private void createPieChart(PieDataSet pieDataSet, PieData pieData) {
        //This gets a color array
        int[] colorArray = getContext().getResources().getIntArray(R.array.colorArray);
        List<Integer> colorList = new ArrayList<Integer>(colorArray.length);
        for (int i : colorArray)
            colorList.add(i);

        //This gets a color according to theme
        TypedValue colorBlackWhite = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, colorBlackWhite, true);

        //Pie legend
        Legend legend = pieChart.getLegend();
        legend.setTextColor(colorBlackWhite.data);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10f);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setYEntrySpace(7f);
        legend.setDrawInside(false);

        //Pie description
        Description description = pieChart.getDescription();
        description.setEnabled(false);

        //Pie slice attr
        pieDataSet.setSliceSpace(0f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setColors(colorArray);

        //Outside values with line
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1OffsetPercentage(100f);
        pieDataSet.setValueLinePart1Length(0.25f);
        pieDataSet.setValueLinePart2Length(0.15f);
        pieDataSet.setValueLineWidth(2f);
        pieDataSet.setUsingSliceColorAsValueLineColor(true);

        //Pie value attr
        pieData.setValueTextSize(10f);
        pieData.setValueTextColors(colorList);
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        //No data text
        pieChart.setNoDataText("No data available");
        pieChart.setNoDataTextColor(colorBlackWhite.data);

        //Entry label
        pieChart.setDrawEntryLabels(false);
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
        pieChart.setCenterTextColor(colorBlackWhite.data);
        pieChart.setCenterTextSize(20f);

        //Animation
        pieChart.animateXY(1000, 1000);
        pieChart.setDragDecelerationFrictionCoef(0.97f);

        //Off set
        pieChart.setExtraOffsets(25f, 0f, 25f, 0f);

        //Refresh chart
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

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
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            lineEntryArray.add(new Entry(dataSet.size() - i, dataSet.get(i).getMoney()));
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

    //This generates DatetimeID form Date
    private String generateDatetimeID(String date, String time) {
        String datetime = date + " " + time;
        String inputPattern = "dd MMM yyyy hh:mm a";
        String outputPattern = "yyyyMMddHHmm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date datetimeInputFormat = null;
        String datetimeOutputFormat = null;

        try {
            datetimeInputFormat = inputFormat.parse(datetime);
            datetimeOutputFormat = outputFormat.format(datetimeInputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datetimeOutputFormat;
    }


}

