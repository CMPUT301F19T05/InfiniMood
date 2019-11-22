package com.example.infinimood.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.infinimood.controller.StringCallback;
import com.example.infinimood.controller.TimePickerCallback;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerCallback callback;
    private Date date;

    public TimePickerFragment(long date, TimePickerCallback callback) {
        this.callback = callback;
        this.date = new Date(date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        return dialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        callback.OnCallback(hourOfDay, minute);
    }
}