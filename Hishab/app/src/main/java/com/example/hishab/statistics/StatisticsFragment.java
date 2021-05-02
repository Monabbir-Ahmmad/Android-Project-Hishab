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

import androidx.fragment.app.Fragment;

import com.example.hishab.DateTimeUtil;
import com.example.hishab.R;
import com.example.hishab.data.DataItem;
import com.example.hishab.database.DatabaseHelper;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
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
    private DatabaseHelper databaseHelper;
    private ArrayList<DataItem> dataSet;
    private TypedValue colorBlackWhite, colorPrimary;
    private DateTimeUtil dateTimeUtil;
    private long startTimestamp, endTimestamp;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        getActivity().setTitle("Statistics");


        //Find views
        pieChart = view.findViewById(R.id.pieChart);
        lineChart = view.findViewById(R.id.lineChart);

        databaseHelper = new DatabaseHelper(getActivity());
        dateTimeUtil = new DateTimeUtil(getActivity());

        //This gets a color according to theme
        colorBlackWhite = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.textColorDark, colorBlackWhite, true);
        colorPrimary = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, colorPrimary, true);


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

        startTimestamp = dateTimeUtil.getTimestamp(startDate, dateTimeUtil.START_OF_DAY);
        endTimestamp = dateTimeUtil.getTimestamp(endDate, dateTimeUtil.END_OF_DAY);
        dataSet = databaseHelper.getFilteredData("All", "Date: Oldest", startTimestamp, endTimestamp);

        setLineData();
        setPieData();
    }


    //This sets the data into the pie chart
    private void setPieData() {
        //Clear chart before updating data
        pieChart.clear();

        if (dataSet.size() > 0) { //If there is any data
            String[] category = getResources().getStringArray(R.array.categoryArray);
            float[] amount = new float[category.length];
            Arrays.fill(amount, 0);
            int index;

            ArrayList<PieEntry> pieEntryArray = new ArrayList<>();
            //This calculates sum of each category for pie chart
            for (int i = 0; i < dataSet.size(); i++) {
                index = Arrays.asList(category).indexOf(dataSet.get(i).getCategory());
                amount[index] += dataSet.get(i).getAmount();
            }

            //This adds the values of each category into the PieEntry
            for (int i = 0; i < category.length; i++) {
                if (amount[i] > 0) {
                    pieEntryArray.add(new PieEntry(amount[i], category[i]));
                }
            }

            //Insert PieEntries into the PieDataSet and create PieData from PieDataSet
            PieDataSet pieDataSet = new PieDataSet(pieEntryArray, null);
            PieData pieData = new PieData(pieDataSet);

            pieChart.setData(pieData);
            createPieChart(pieDataSet);
        }

        //No data text
        pieChart.setNoDataText("No data to display!");
        pieChart.setNoDataTextColor(colorBlackWhite.data);
        pieChart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(20f));

    }

    //This sets the data into the line chart
    private void setLineData() {
        //Clear chart before updating data
        lineChart.clear();

        if (dataSet.size() > 0) { //If there is any data
            ArrayList<Entry> lineEntryArray = new ArrayList<>();

            long lineStartPosX = dateTimeUtil.getTimestamp(
                    dateTimeUtil.getDate(dataSet.get(0).getTimestamp()), dateTimeUtil.START_OF_DAY);

            long day = lineStartPosX;
            float index = 0;
            float dailyExpense = 0;

            //Calculate total amount for each day
            for (int i = 0; i < dataSet.size(); i++) {
                if (dataSet.get(i).getTimestamp() >= day
                        && dataSet.get(i).getTimestamp() < day + 86400L) {
                    dailyExpense += dataSet.get(i).getAmount();

                } else {
                    lineEntryArray.add(new Entry(index, dailyExpense));
                    dailyExpense = dataSet.get(i).getAmount();

                    //Calculate index to skip based on date
                    while (!(dataSet.get(i).getTimestamp() >= day
                            && dataSet.get(i).getTimestamp() < day + 86400L)) {
                        index += 1;
                        day += 86400L;
                    }
                }
                if (i == dataSet.size() - 1) {
                    lineEntryArray.add(new Entry(index, dailyExpense));
                }
            }

            //Insert LineEntries into the LineDataSet and create LineData from LineDataSet
            LineDataSet lineDataSet = new LineDataSet(lineEntryArray, null);
            LineData lineData = new LineData(lineDataSet);

            lineChart.setData(lineData);
            createLineChart(lineDataSet, lineStartPosX);
        }

        lineChart.setNoDataText("No data to display!");
        lineChart.setNoDataTextColor(colorBlackWhite.data);
        lineChart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(20f));

    }


    //This is the pie chart design
    private void createPieChart(PieDataSet pieDataSet) {
        //This gets a color array
        int[] colorArray = getContext().getResources().getIntArray(R.array.colorArray);
        List<Integer> colorList = new ArrayList<>(colorArray.length);
        for (int i : colorArray) {
            colorList.add(i);
        }

        //Pie chart click event
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //Change the center text to selected entry value and label with SpannableString styling
                PieEntry entry = (PieEntry) e;
                String amount = decimalFormat.format(e.getY()) + " BDT";
                String label = entry.getLabel();
                SpannableString centerText = new SpannableString(amount + "\n" + label);
                centerText.setSpan(new RelativeSizeSpan(.7f), amount.length(), centerText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                pieChart.setCenterText(centerText);
            }

            @Override
            public void onNothingSelected() { //Reset center text
                pieChart.setCenterText("");
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
        pieChart.setCenterText("");
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
    private void createLineChart(LineDataSet lineDataSet, long lineStartPosX) {
        //Marker view
        CustomMarkerView markerView = new CustomMarkerView(getActivity(), lineStartPosX);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);

        //Touch attribute
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(false);

        //Line attribute
        lineDataSet.setLineWidth(3f);
        lineDataSet.setColor(colorPrimary.data);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(1.5f);
        lineDataSet.setCircleColor(colorPrimary.data);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);

        //Highlight
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setHighLightColor(colorPrimary.data);
        lineDataSet.setHighlightLineWidth(1f);

        //Description of the chart
        Description description = lineChart.getDescription();
        description.setEnabled(false);

        //Legends of the chart
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        //Y axis left
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(true);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setGranularity(10);
        yAxisLeft.setLabelCount(5);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setTextColor(colorBlackWhite.data);
        yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value >= 1000) {
                    yAxisLeft.setGranularity(1000);
                    return String.valueOf(Math.round(value / 1000)) + "k";
                }
                return decimalFormat.format(value);
            }
        });

        //X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(5);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);
        xAxis.setTextColor(colorBlackWhite.data);
        xAxis.enableGridDashedLine(30f, 10000f, 10f);
        xAxis.setGridLineWidth(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return dateFormat.format((lineStartPosX + (long) value * 86400L) * 1000L);
            }
        });

        //Y axis left
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        //Animation
        lineChart.animateY(1000);

        //View port offset
        lineChart.setExtraOffsets(0, 0, 10f, 10f);

        //Refresh chart
        lineChart.notifyDataSetChanged();
        lineChart.fitScreen();
        lineChart.invalidate();
    }


}

