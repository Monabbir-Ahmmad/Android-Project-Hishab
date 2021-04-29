package com.example.hishab.statistics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private final TextView tvAmount;
    private final TextView tvDateTime;
    private final long startTimestamp;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());

    //Constructor
    public CustomMarkerView(Context context, long startTimestamp) {
        super(context, R.layout.custom_marker_view);
        this.startTimestamp = startTimestamp;

        //Find views
        tvAmount = findViewById(R.id.markerView_amount);
        tvDateTime = findViewById(R.id.markerView_dateTime);
    }

    //Callbacks every time the MarkerView is redrawn, can be used to update the views
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvAmount.setText(String.format("%s BDT", decimalFormat.format(e.getY())));
        tvDateTime.setText(dateFormat.format((startTimestamp + (long) e.getX()) * 1000L));
        super.refreshContent(e, highlight);
    }

    //Canvas draw position
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(posX, posY, 13f, paint);

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
