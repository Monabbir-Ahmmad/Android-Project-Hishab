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

    private final TextView tv_amount;
    private final TextView tv_date;
    private final TextView tv_time;
    private final ArrayList<DataItem> dataSet;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    //Constructor
    public CustomMarkerView(Context context, ArrayList<DataItem> dataSet) {
        super(context, R.layout.custom_marker_view);
        this.dataSet = dataSet;
        tv_amount = findViewById(R.id.markerView_amount);
        tv_date = findViewById(R.id.markerView_date);
        tv_time = findViewById(R.id.markerView_time);
    }

    //Callbacks every time the MarkerView is redrawn, can be used to update the views
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tv_amount.setText(decimalFormat.format(e.getY()) + " BDT");
        tv_date.setText(dataSet.get((int) e.getX()).getDate());
        tv_time.setText(dataSet.get((int) e.getX()).getTime());
        super.refreshContent(e, highlight);
    }

    //Canvas draw position
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        if (posY > getChartView().getHeight() / 2) { //When value in above chart center
            posY = posY - getHeight();
        } else { //When value in below chart center
            posY = posY + getHeight();
        }
        super.draw(canvas, posX, posY);
        getOffsetForDrawingAtPoint(posX, posY);
    }

    //Marker position center of highlighted point
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -(getHeight() / 2));
    }
}
