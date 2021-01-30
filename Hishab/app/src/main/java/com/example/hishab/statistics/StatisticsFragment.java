package com.example.hishab.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.hishab.CustomDateTime;
import com.example.hishab.DataItem;
import com.example.hishab.DatabaseHelper;
import com.example.hishab.R;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private PieChart pieChart;
    private LineChart lineChart;
    private TabLayout tabLayout;
    private TextView tv_total, tv_avg, tv_count;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataItem> dataSet;
    private TypedValue colorBlackWhite;
    private CustomDateTime customDateTime;
    private long startTimestamp, endTimestamp;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        customDateTime = new CustomDateTime(getActivity());

        //This gets a color according to theme
        colorBlackWhite = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, colorBlackWhite, true);

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

        tv_total = view.findViewById(R.id.textView_totalExpense);
        tv_avg = view.findViewById(R.id.textView_avgExpense);
        tv_count = view.findViewById(R.id.textView_expenseCount);
        pieChart = view.findViewById(R.id.pieChart);
        lineChart = view.findViewById(R.id.lineChart);

        statisticsFilter(0);

        return view;
    }


    //This is the top filter for statistics
    private void statisticsFilter(int tab) {
        Calendar calendar = Calendar.getInstance();
        String startDate = null, endDate = null;

        if (tab == 0) {
            startDate = simpleDateFormat.format(new Date());
            endDate = startDate;

        } else if (tab == 1) {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            startDate = simpleDateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            endDate = simpleDateFormat.format(calendar.getTime());

        } else if (tab == 2) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = simpleDateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = simpleDateFormat.format(calendar.getTime());

        } else if (tab == 3) {
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            startDate = simpleDateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
            endDate = simpleDateFormat.format(calendar.getTime());

        } else if (tab == 4) {
            startDate = "01 Jan 1970";
            endDate = "31 Dec 2100";

        }

        startTimestamp = customDateTime.getTimestamp(startDate, "12:00 am");
        endTimestamp = customDateTime.getTimestamp(endDate, "11:59 pm");
        dataSet = databaseHelper.getFilteredData("All", "Date: Oldest", startTimestamp, endTimestamp);

        setLineData();
        setPieData();
    }


    //This sets the data into the pie chart
    private void setPieData() {
        //Clear chart before updating data
        pieChart.clear();

        String[] category = getResources().getStringArray(R.array.categoryArray);
        float[] money = new float[category.length];
        Arrays.fill(money, 0);
        int index;
        float sum = 0, avg = 0;

        if (dataSet.size() > 0) { //If there is any data
            ArrayList<PieEntry> pieEntryArray = new ArrayList<>();

            //This calculates sum of each category for pie chart
            for (int i = 0; i < dataSet.size(); i++) {
                index = Arrays.asList(category).indexOf(dataSet.get(i).getCategory());
                money[index] += dataSet.get(i).getMoney();
                sum += dataSet.get(i).getMoney(); //Calculate the total expense
            }
            avg = sum / dataSet.size(); //Calculate the average expense

            //This adds the values of each category into the PieEntry
            for (int i = 0; i < category.length; i++) {
                if (money[i] > 0) {
                    pieEntryArray.add(new PieEntry(money[i], category[i]));
                }
            }

            //Insert PieEntries into the PieDataSet and create PieData from PieDataSet
            PieDataSet pieDataSet = new PieDataSet(pieEntryArray, null);
            PieData pieData = new PieData(pieDataSet);

            pieChart.setData(pieData);
            createPieChart(pieDataSet);

        }

        //No data text
        pieChart.setNoDataText("No data available");
        pieChart.setNoDataTextColor(colorBlackWhite.data);

        tv_total.setText(decimalFormat.format(sum) + " BDT");
        tv_avg.setText(decimalFormat.format(avg) + " BDT");
        tv_count.setText(String.valueOf(dataSet.size()));

    }

    //This sets the data into the line chart
    private void setLineData() {
        //Clear chart before updating data
        lineChart.clear();
        float index = 0;

        if (dataSet.size() > 0) { //If there is any data
            ArrayList<Entry> lineEntryArray = new ArrayList<>();

            //This adds the values into the LineEntry
            for (int i = 0; i < dataSet.size(); i++) {
                if (i > 0) {
                    //If current data's timestamp equals previous timestamp, increase index value by 1sec to avoid error
                    if (index >= (dataSet.get(i).getTimestamp() - dataSet.get(0).getTimestamp())) {
                        index += 1;
                    }
                    //Else set new index value
                    else {
                        index = dataSet.get(i).getTimestamp() - dataSet.get(0).getTimestamp();
                    }
                }
                lineEntryArray.add(new Entry(index, dataSet.get(i).getMoney()));
            }

            //Insert LineEntries into the LineDataSet and create LineData from LineDataSet
            LineDataSet lineDataSet = new LineDataSet(lineEntryArray, null);
            LineData lineData = new LineData(lineDataSet);

            lineChart.setData(lineData);
            createLineChart(lineDataSet);
        }

        lineChart.setNoDataText("No data available");
        lineChart.setNoDataTextColor(Color.WHITE);

    }


    //This is the pie chart design
    private void createPieChart(PieDataSet pieDataSet) {
        //This gets a color array
        int[] colorArray = getContext().getResources().getIntArray(R.array.colorArray);
        List<Integer> colorList = new ArrayList<Integer>(colorArray.length);
        for (int i : colorArray) {
            colorList.add(i);
        }

        //Pie chart click event
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //Change the center text to selected entry value and label with SpannableString styling
                PieEntry entry = (PieEntry) e;
                String money = decimalFormat.format(e.getY()) + " BDT";
                String label = entry.getLabel();
                SpannableString centerText = new SpannableString(money + "\n" + label);
                centerText.setSpan(new RelativeSizeSpan(.7f), money.length(), money.length() + label.length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                pieChart.setCenterText(centerText);
            }

            @Override
            public void onNothingSelected() { //Reset center text
                pieChart.setCenterText("Expense");
            }
        });

        //Legends of the chart
        Legend legend = pieChart.getLegend();
        legend.setTextColor(colorBlackWhite.data);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10f);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setYEntrySpace(7f);
        legend.setDrawInside(true);

        //Description of the chart
        Description description = pieChart.getDescription();
        description.setEnabled(false);

        //Pie slice attribute
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
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setValueTextColors(colorList);
        pieDataSet.setValueFormatter(new PercentFormatter(pieChart));

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
        pieChart.setCenterTextRadiusPercent(70f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        //Center Text
        pieChart.setCenterText("Expense");
        pieChart.setCenterTextColor(colorBlackWhite.data);
        pieChart.setCenterTextSize(20f);

        //Animation
        pieChart.animateXY(1000, 1000);
        pieChart.setDragDecelerationFrictionCoef(0.97f);

        //Off set
        pieChart.setExtraOffsets(0f, 10f, 0f, 60f);

        //Refresh chart
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

    }

    //This creates the line chart
    private void createLineChart(LineDataSet lineDataSet) {
        //Marker view
        CustomMarkerView markerView = new CustomMarkerView(getActivity(), dataSet.get(0).getTimestamp());
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);

        //Touch attribute
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawBorders(false);

        //Line attribute
        lineDataSet.setLineWidth(3f);
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(1.5f);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.WHITE);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);

        //Highlight
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setHighLightColor(Color.WHITE);
        lineDataSet.setHighlightLineWidth(1f);

        //Description of the chart
        Description description = lineChart.getDescription();
        description.setEnabled(false);

        //Legends of the chart
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        //X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);

        //Y axis left
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        //Y axis left
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        //Animation
        lineChart.animateY(1000);

        //View port offset
        lineChart.setViewPortOffsets(0, 0, 0, 0);

        //Refresh chart
        lineChart.notifyDataSetChanged();
        lineChart.fitScreen();
        lineChart.invalidate();
    }


}

