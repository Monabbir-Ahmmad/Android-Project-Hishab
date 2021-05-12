package com.example.hishab.statistics;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.example.hishab.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class BarChartMarker extends MarkerView {

    private final TextView tvAmount;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    //Constructor
    public BarChartMarker(Context context) {
        super(context, R.layout.marker_bar_chart);

        //Find views
        tvAmount = findViewById(R.id.marker_barChart_amount);
    }

    //Callbacks every time the MarkerView is redrawn, can be used to update the views
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvAmount.setText(String.format("%s BDT", decimalFormat.format(e.getY())));
        super.refreshContent(e, highlight);
    }

    //Canvas draw position


    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        posY = posY - getHeight() / 4;
        super.draw(canvas, posX, posY);
        getOffsetForDrawingAtPoint(posX, posY);
    }

    //Marker position center of highlighted point
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
