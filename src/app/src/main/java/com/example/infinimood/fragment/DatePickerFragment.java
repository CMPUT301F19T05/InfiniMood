package com.example.infinimood.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.example.infinimood.controller.DatePickerCallback;

import java.util.Calendar;
import java.util.Date;

/**
 * DatePickerFragment.java
 * Fragment for picking a date, used to select the date of a new or existing mood event
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerCallback callback;
    private Date date;

    /**
     * DatePickerFragment
     * Simple constructor for DatePickerFragment
     * @param date The Date
     * @param callback The callback for notifying other activities which date was chosen
     */
    public DatePickerFragment(long date, DatePickerCallback callback) {
        this.callback = callback;
        this.date = new Date(date);
    }

    /**
     * onCreateDialog
     * Overrides onCreateDialog method. Sets the date to a default of the current date, creates
     * a DatePickerDialog so that user can select their own
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return dialog;
    }

    /**
     * onDateSet
     * Method to be called when the date has been selected, simply calls the callback function
     * @param view DatePicker - The DatePicker
     * @param year int - The year chosen by user
     * @param month int - The month chosen by user
     * @param day int - The day chosen by user
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        callback.OnCallback(year, month, day);
    }
}
