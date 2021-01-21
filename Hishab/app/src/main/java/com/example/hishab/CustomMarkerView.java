package com.example.hishab;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomMarkerView extends MarkerView {

    private TextView tv_amount, tv_date, tv_time;
    private ArrayList<DataItem> dataSet;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.##");


    public CustomMarkerView(Context context, ArrayList<DataItem> dataSet) {
        super(context, R.layout.custom_marker_view);
        this.dataSet = dataSet;
        tv_amount = (TextView) findViewById(R.id.markerView_amount);
        tv_date = (TextView) findViewById(R.id.markerView_date);
        tv_time = (TextView) findViewById(R.id.markerView_time);
    }

    //Callbacks every time the MarkerView is redrawn, can be used to update the views
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tv_amount.setText(decimalFormat.format(e.getY()) + " BDT");
        tv_date.setText(dataSet.get((int) e.getX()).getDate());
        tv_time.setText(dataSet.get((int) e.getX()).getTime());
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        super.draw(canvas, posX, posY);
        getOffsetForDrawingAtPoint(posX, posY);
    }

    //Marker position
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
