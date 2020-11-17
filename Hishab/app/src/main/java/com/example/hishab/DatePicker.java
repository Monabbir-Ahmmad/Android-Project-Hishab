package com.example.hishab;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePicker implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText mEditText;
    private Calendar mCalendar;
    private SimpleDateFormat mFormat;
    private Context mContext;

    public DatePicker(EditText editText, Context context) {
        this.mEditText = editText;
        mEditText.setOnFocusChangeListener(this);
        mEditText.setOnClickListener(this);
        this.mContext = context;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            showPicker(view);
        }
    }

    @Override
    public void onClick(View view) {
        showPicker(view);
    }

    private void showPicker(View view) {
        if (mCalendar == null)
            mCalendar = Calendar.getInstance();

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), this, year, month, dayOfMonth).show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {

        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (mFormat == null) {
            mFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        }
        this.mEditText.setText(mFormat.format(mCalendar.getTime()));
    }
}
