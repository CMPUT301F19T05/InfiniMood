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

/**
 * TimePickerFragment.java
 * Fragment for picking a time, used to select the time of a new or existing mood event
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerCallback callback;
    private Date date;

    /**
     * TimePickerFragment
     * Simple constructor for TimePickerFragment
     * @param date long - The Date for containing the time
     * @param callback TimePickerCallback - The callback for notifying other activities which time was chosen
     */
    public TimePickerFragment(long date, TimePickerCallback callback) {
        this.callback = callback;
        this.date = new Date(date);
    }

    /**
     * onCreateDialog
     * Overrides onCreateDialog method. Sets the date to a default of the current time, creates
     * a TimePickerDialog so that user can select their own
     */
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

    /**
     * onTimeSet
     * Method to be called when time has been selected, simply calls the callback method
     * @param view TimePicker - The TimePicker
     * @param hourOfDay int - The hour of the day chosen
     * @param minute int - The minute of the hour chosen
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        callback.OnCallback(hourOfDay, minute);
    }
}