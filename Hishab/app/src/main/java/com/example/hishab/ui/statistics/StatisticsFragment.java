package com.example.hishab.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.hishab.DateTimeUtil;
import com.example.hishab.R;
import com.example.hishab.database.DatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticsFragment extends Fragment implements View.OnClickListener {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    private PieChart pieChart;
    private LineChart lineChart;
    private BarChart barChart;
    private Button btnLineSort, btnBarSort;
    private TextView tvDailyAvg, tvMonthlyTotal, tvMonthAvg, tvYearlyTotal;
    private MaterialButtonToggleGroup btnPieSort;
    private DatabaseHelper databaseHelper;
    private TypedValue colorBlackWhite, colorPrimary;
    private String currency;

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
        barChart = view.findViewById(R.id.barChart);
        btnLineSort = view.findViewById(R.id.button_lineChart_sort);
        btnPieSort = view.findViewById(R.id.button_pieChart_sort);
        btnBarSort = view.findViewById(R.id.button_barChart_sort);
        tvDailyAvg = view.findViewById(R.id.textView_dailyAvg);
        tvMonthlyTotal = view.findViewById(R.id.textView_monthlyTotal);
        tvMonthAvg = view.findViewById(R.id.textView_monthlyAvg);
        tvYearlyTotal = view.findViewById(R.id.textView_yearlyTotal);

        databaseHelper = new DatabaseHelper(getActivity());
        currency = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("currency", "$");

        //This gets a color according to theme
        colorBlackWhite = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBlackWhite, colorBlackWhite, true);
        colorPrimary = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, colorPrimary, true);

        btnLineSort.setOnClickListener(this);
        btnBarSort.setOnClickListener(this);
        btnPieSort.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_week) {
                    initPieChart(0);
                } else if (checkedId == R.id.btn_month) {
                    initPieChart(1);
                } else if (checkedId == R.id.btn_year) {
                    initPieChart(2);
                }
            }
        });

        Calendar calendar = Calendar.getInstance();

        initPieChart(0);

        btnLineSort.setText(monthYearFormat.format(calendar.getTime()));
        initLineChart(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        btnBarSort.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        initBarChart(calendar.get(Calendar.YEAR));


        return view;
    }


    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();

        if (v.getId() == R.id.button_lineChart_sort) { //Open Month and Year picker
            MonthYearPicker monthYearPicker = new MonthYearPicker()
                    .setYearMin(1970)
                    .setYearMax(3000)
                    .setYear(calendar.get(Calendar.YEAR))
                    .setMonth(calendar.get(Calendar.MONTH));

            monthYearPicker.setOnPositiveButtonClickListener((selectedMonth, selectedYear) -> {
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                btnLineSort.setText(monthYearFormat.format(calendar.getTime()));
                initLineChart(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            });

            monthYearPicker.show(getActivity().getSupportFragmentManager(), "MonthYearPicker");

        } else if (v.getId() == R.id.button_barChart_sort) { //Open Year picker
            MonthYearPicker yearPicker = new MonthYearPicker()
                    .setYearMin(1970)
                    .setYearMax(3000)
                    .setYear(calendar.get(Calendar.YEAR))
                    .setShowYearOnly(true);

            yearPicker.setOnPositiveButtonClickListener((selectedMonth, selectedYear) -> {
                btnBarSort.setText(String.valueOf(selectedYear));
                initBarChart(selectedYear);
            });

            yearPicker.show(getActivity().getSupportFragmentManager(), "MonthYearPicker");
        }


    }


    //This sets the data into the pie chart
    private void initPieChart(int choice) {
        //Clear chart before updating data
        pieChart.clear();

        ArrayList<PieEntry> pieEntryArray = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        long startTime = 0, endTime = 0;
        float categorySum, totalExpense = 0;
        String[] category = getResources().getStringArray(R.array.categoryArray);

        if (choice == 0) {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            startTime = calendar.getTimeInMillis();
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            endTime = calendar.getTimeInMillis() + DateTimeUtil.DAY_IN_MS - 1000L;

        } else if (choice == 1) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startTime = calendar.getTimeInMillis();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endTime = calendar.getTimeInMillis() + DateTimeUtil.DAY_IN_MS - 1000L;

        } else if (choice == 2) {
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            startTime = calendar.getTimeInMillis();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
            endTime = calendar.getTimeInMillis() + DateTimeUtil.DAY_IN_MS - 1000L;
        }

        //This adds the values of each category into the PieEntry
        for (String label : category) {
            categorySum = databaseHelper.getFilteredSum(label, startTime, endTime);
            totalExpense += categorySum;
            if (categorySum > 0)
                pieEntryArray.add(new PieEntry(categorySum, label));

        }

        if (pieEntryArray.size() > 0) { //Insert PieEntries into the PieDataSet and create PieData from PieDataSet
            PieDataSet pieDataSet = new PieDataSet(pieEntryArray, null);
            PieData pieData = new PieData(pieDataSet);

            pieChart.setData(pieData);
            renderPieChart(pieDataSet, totalExpense);
        }

        //No data text
        pieChart.setNoDataText("No data found!");
        pieChart.setNoDataTextColor(colorBlackWhite.data);
        pieChart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(20f));

    }

    //This sets the data into the line chart
    private void initLineChart(int selectedMonth, int selectedYear) {

    }


    //This sets the data into the bar chart
    private void initBarChart(int selectedYear) {


    }


    //This is the pie chart design
    private void renderPieChart(PieDataSet pieDataSet, float totalExpense) {

    }

    //This creates the line chart
    private void renderLineChart(LineDataSet lineDataSet, long lineStartPosX) {

    }

    //This creates the bar chart
    private void renderBarChart(BarDataSet barDataSet, BarData barData) {

    }

}

