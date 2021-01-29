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
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomMarkerView extends MarkerView {

    private final TextView tv_amount;
    private final TextView tv_dateTime;
    private final long startTimestamp;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy\nhh:mm a", Locale.getDefault());

    //Constructor
    public CustomMarkerView(Context context, long startTimestamp) {
        super(context, R.layout.custom_marker_view);
        this.startTimestamp = startTimestamp;
        tv_amount = findViewById(R.id.markerView_amount);
        tv_dateTime = findViewById(R.id.markerView_dateTime);
    }

    //Callbacks every time the MarkerView is redrawn, can be used to update the views
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tv_amount.setText(decimalFormat.format(e.getY()) + " BDT");
        tv_dateTime.setText(dateFormat.format((startTimestamp + (long) e.getX()) * 1000L));
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
