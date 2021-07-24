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


        return view;
    }


    @Override
    public void onClick(View v) {


    }


    //This sets the data into the pie chart
    private void initPieChart(int choice) {

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

